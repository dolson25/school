import optparse
import socket
import sys
import errno
import select
import traceback

class Server:
    def __init__(self,port):
        self.host = ""
        self.port = port
        self.client = None
        self.cache = ''
        self.clients = {}
        self.caches = {}
        self.messages = {}
        self.keys = {}
        self.size = 2500
        self.parse_options()
        self.open_socket()
        self.run()

    def parse_options(self):
        parser = optparse.OptionParser(usage = "%prog [options]",
                                       version = "%prog 0.1")

        parser.add_option("-p","--port",type="int",dest="port",
                          default=5000,
                          help="port to listen on")

        (options,args) = parser.parse_args()
        self.port = options.port

    def open_socket(self):
        """ Setup the socket for incoming clients """
        try:
            self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR,1)
            self.server.bind((self.host,self.port))
            self.server.listen(5)
            self.server.setblocking(0)
        except socket.error, (value,message):
            if self.server:
                self.server.close()
            print "Could not open socket: " + message
            sys.exit(1)

    def run(self):
        """ Use poll() to handle each incoming client."""
        self.poller = select.epoll()
        self.pollmask = select.EPOLLIN | select.EPOLLHUP | select.EPOLLERR
        self.poller.register(self.server,self.pollmask)
        while True:
            # poll sockets
            try:
                #List of file descriptors
                fds = self.poller.poll(timeout=.5)
            except:
                return

            for (fd,event) in fds:
                # handle errors
                if event & (select.POLLHUP | select.POLLERR):
                    self.handleError(fd)
                    continue
                # handle the server socket
                if fd == self.server.fileno():
                    self.handleServer()
                    continue
                # handle client socket
                result = self.handle_client(fd)

    def handleError(self,fd):
        self.poller.unregister(fd)
        if fd == self.server.fileno():
            # recreate server socket
            self.server.close()
            self.open_socket()
            self.poller.register(self.server,self.pollmask)
        else:
            # close the socket
            self.clients[fd].close()
            del self.clients[fd]

    def handleServer(self):
        # accept as many clients as possible
        while True:
            try:
                (client,address) = self.server.accept()
            except socket.error, (value,message):
                # if socket blocks because no clients are available,
                # then return
                if value == errno.EAGAIN or errno.EWOULDBLOCK:
                    return
                print traceback.format_exc()
                sys.exit()
            # set client socket to be non blocking
            client.setblocking(0)
            self.clients[client.fileno()] = client
            self.poller.register(client.fileno(),self.pollmask)

            #start an empty cache for that client
            self.caches[client.fileno()] = ""

    def handle_client(self, fd):
        try:
            data = self.clients[fd].recv(self.size)
        except socket.error, (value,message):
            # if no data is available, move on to another client
            if value == errno.EAGAIN or errno.EWOULDBLOCK:
                return
            print traceback.format_exc()
            sys.exit()

        if data:
            #add new data received to what we have in the cache
            cache = self.caches[fd]
            new_data = cache + data


            #if we still have complete messages
            if "\n" in new_data:    
                #pull out the request part and save remainder for next time
                two_strings = new_data.split("\n")

                message = two_strings[0]

                remainder = ""

                for i in range(len(two_strings)):
                    if not i == 0:
                        remainder += two_strings[i] + "\n"

                remainder = remainder[:-1]


                self.caches[fd] = remainder
                new_data = remainder

                self.handle_message(message, fd)
        else:
            self.poller.unregister(fd)
            self.clients[fd].close()
            del self.clients[fd]
            del self.caches[fd]

    def handle_message(self,message, fd):
        response = self.parse_message(message, fd)
        if not response == "partial":
            self.send_response(response, fd)

    def parse_message(self,message, fd):
        fields = message.split()
        if not fields:
            return('error invalid message\n')
        if fields[0] == 'reset':
            self.messages = {}
            return "OK\n"
        if fields[0] == 'put':
            try:
                name = fields[1]
                subject = fields[2]
                length = int(fields[3]) - 1
            except:
                return('error invalid message\n')
            data = self.read_put(length, fd, message)
            if data == None:
                return 'error could not read entire message\n'
            elif data == "partial":
                return "partial"
            else:
                self.store_message(name,subject,data)
                return "OK\n"
        if fields[0] == 'list':
            try:
                name = fields[1]
            except:
                return('error invalid message\n')

            subjects,length = self.get_subjects(name)
            response = "list %d\n" % length
            response += subjects
            return response
        if fields[0] == 'get':
            try:
                name = fields[1]
                index = int(fields[2])
            except:
                return('error invalid message\n')
            subject,data = self.get_message(name,index)
            if not subject:
                return "error no such message for that user\n"
            response = "message %s %d\n" % (subject,len(data))
            response += data
            return response
        if fields[0] == 'store_key':
            ### store key ###
            try:
                name = fields[1]
                length = fields[2]
            except:
                return('error invalid message')

            key = self.caches[fd] 
            
            if key == None:
                return 'error could not read entire message\n'
            elif key == "partial":
                return "partial"
            else:
                self.store_key(name,key)
                self.caches[fd] = ""
                return "OK\n"
        if fields[0] == 'get_key':
            ### get key ####
            try:
                name = fields[1]
            except:
                return('error invalid message')
            key = self.keys[name]
            return "key " + str(len(key)) + "\n" + key 
        return('error invalid message\n')
    
    def store_message(self,name,subject,data):
        if name not in self.messages:
            self.messages[name] = []
        self.messages[name].append((subject,data))

    def get_subjects(self,name):
        if name not in self.messages:
            return "",0
        response = ["%d %s\n" % (index+1,message[0]) for index,message in enumerate(self.messages[name])]
        return "".join(response),len(response)

    def get_message(self,name,index):
        if index <= 0:
            return None,None
        try:
            return self.messages[name][index-1]
        except:
            return None,None

    def store_key(self, name, key):
        self.keys[name] = key

    def read_put(self,length, fd, message):
        data = self.caches[fd]

        if len(data) < length:
            self.caches[fd] = message + "\n" + data
            return "partial"
        elif len(data) > length:
            self.caches[fd] = data[length + 1:]
            data = data[:length + 1]
            return data
        else:
            self.caches[fd] = ""
            return data

    def read_key(self,length, fd, message):
        data = self.caches[fd]

        if len(data) < length:
            self.caches[fd] = message + "\n" + data
            return "partial"
        elif len(data) > length:
            self.caches[fd] = data[length + 1:]
            data = data[:length + 1]
            return data
        else:
            self.caches[fd] = ""
            return data

    def send_response(self,response, fd):
        self.clients[fd].send(response)

if __name__ == '__main__':
    s = Server(5000)