import errno
import select
import socket
import sys
import traceback
import os
import time

try:
    from http_parser.parser import HttpParser
except ImportError:
    from http_parser.pyparser import HttpParser

class Poller:
    """ Polling server """
    def __init__(self,port):
        self.host = ""
        self.path = self.get_path("default")
        self.inactive_timeout = int(self.get_timeout())
        self.port = port
        self.open_socket()
        self.clients = {}
        self.caches = {}
        self.last_action_times = {}
        self.size = 1024
        self.format = '%a, %d %b %Y %H:%M:%S GMT'
        self.m_and_s_time = time.time()

    def get_timeout(self):
        with open('web.conf') as file:
            for l in file:
                words = l.split()
                if(len(words) > 0):
                    if(words[1] == "timeout"):
                        return words[2]    

    def get_path(self,host):
        with open('web.conf') as file:
            for l in file:
                words = l.split()
                if(words[0] == "host" and words[1] == host):
                    return words[2]
        return ""

    def mark_and_sweep(self):
        for key, value in self.last_action_times.items():
            if(time.time() - value > self.inactive_timeout):
                self.poller.unregister(key)
                self.clients[key].close()
                del self.clients[key]
                del self.last_action_times[key]
                del self.caches[key]


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

            #at this point either we have some action or the timeout has occured
            #check to see if mark and sweep happened at least .5 seconds ago
            if(time.time() - self.m_and_s_time > .5):
                #do mark and sweep and reset the timer
                self.mark_and_sweep()
                self.m_and_s_time = time.time()

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
                result = self.handleClient(fd)

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

            #set the time of last activity for that socket
            self.last_action_times[client.fileno()] = time.time()

    def handleClient(self,fd):
        try:
            data = self.clients[fd].recv(self.size)
        except socket.error, (value,message):
            # if no data is available, move on to another client
            if value == errno.EAGAIN or errno.EWOULDBLOCK:
                return
            print traceback.format_exc()
            sys.exit()

        if data:

            #mark the time this client was active
            self.last_action_times[fd] = time.time()

            #add new data received to what we have in the cache
            cache = self.caches[fd]
            new_data = cache + data


            #while we still have complete http requests
            while "\r\n\r\n" in new_data:
                
                #pull out the request part and save remainder for next time
                two_strings = new_data.split("\r\n\r\n")

                http_request = two_strings[0]
                http_request += "\r\n\r\n"

                remainder = ""

                for i in range(len(two_strings)):
                    if not i == 0:
                        remainder += two_strings[i]

                self.caches[fd] = remainder
                new_data = remainder


                #begin acutal http parsing
                p = HttpParser()
                nparsed = p.execute(http_request,len(http_request))

                date = time.strftime(self.format,time.gmtime())

                if(not p.is_headers_complete()):
                    # 404 Bad Request
                    response = "HTTP/1.1 400 Bad Request\r\n"
                    response += "Date: " + date + "\r\n"
                    response += "Server: David's Python Server (Ubuntu)\r\n"
                    message = "Bad HTTP Request, 400"
                    response += "Content-Length: " + str(len(message)) + "\r\n"
                    response += "Content-Type: text/html\r\n\r\n"
                    response += message
                    self.clients[fd].send(response)
                    return

                if(not (p.get_method() == "GET" or p.get_method() == "HEAD")):
                    # 501 Not Implemented
                    response = "HTTP/1.1 501 Not Implemented\r\n"
                    response += "Date: " + date + "\r\n"
                    response += "Server: David's Python Server (Ubuntu)\r\n"
                    message = "Method Not Implemented, 501"
                    response += "Content-Length: " + str(len(message)) + "\r\n"
                    response += "Content-Type: text/html\r\n\r\n"
                    response += message
                    self.clients[fd].send(response)
                    return

                #find the file from the url
                file_path = ""

                if(p.get_path() == '/'):
                    file_path = self.path + "/index.html"
                else:
                    file_path = "./" + self.path + p.get_path()


                try: 
                    open(file_path)
                except  IOError  as (errno ,strerror ):
                    response = ""
                    if errno  == 13:
                        # 403  Forbidden
                        response = "HTTP/1.1 403 Forbidden\r\n"
                        response += "Date: " + date + "\r\n"
                        response += "Server: David's Python Server (Ubuntu)\r\n"
                        message = "Access Forbidden, 403"
                        response += "Content-Length: " + str(len(message)) + "\r\n"
                        response += "Content-Type: text/html\r\n\r\n"
                        response += message
                    elif  errno  == 2:
                        # 404  Not  Found
                        response = "HTTP/1.1 404 Not Found\r\n"
                        response += "Date: " + date + "\r\n"
                        response += "Server: David's Python Server (Ubuntu)\r\n"
                        message = "File Not found, 404"
                        response += "Content-Length: " + str(len(message)) + "\r\n"
                        response += "Content-Type: text/html\r\n\r\n"
                        response += message
                    else:
                        # 500  Internal  Server  Error
                        response = "HTTP/1.1 500 Internal Server Error\r\n"
                        response += "Content-Length: 0\r\n"

                    self.clients[fd].send(response)
                    return

                #at this point we know we will use the file
                #find the right content-type for the file extension 
                filename, file_extension = os.path.splitext(file_path)
                content_type = ""

                with open('web.conf') as file:
                    for l in file:
                        words = l.split()
                        if len(words) > 0:
                            if(words[0] == "host" and words[1] == file_extension):
                                content_type = words[2]

                if(p.get_method() == "HEAD"):
                    # 200 OK JUST THE HEAD
                    response = "HTTP/1.1 200 OK\r\n"
                    response += "Date: " + date + "\r\n"
                    response += "Server: David's Python Server (Ubuntu)\r\n"
                    response += "Content-Length: " + str(os.stat(file_path).st_size) + "\r\n"
                    response += "Content-Type: " + content_type + "\r\n"
                    response += "Last-Modified: " + str(os.stat(file_path).st_mtime) + "\r\n"
                    response += "\r\n"
                    self.clients[fd].send(response)
                    return

                if('Range' in p.get_headers()):
                    with open(file_path) as fileP:
                        byte_range = p.get_headers()['Range'].split('bytes=')[1].split('-')
                        # 206 Return a Range
                        response = "HTTP/1.1 206 Range\r\n"
                        response += "Date: " + date + "\r\n"
                        response += "Server: David's Python Server (Ubuntu)\r\n"
                        response += "Content-Length: " + str(int(byte_range[1]) - int(byte_range[0]) + 1) + "\r\n"
                        response += "Content-Type: " + content_type + "\r\n"
                        response += "Last-Modified: " + str(os.stat(file_path).st_mtime) + "\r\n"
                        response += "\r\n"
                        response += fileP.read()[int(byte_range[0]):int(byte_range[1]) + 1]
                        self.clients[fd].send(response)
                        return

                #if at this point, we are dealing with a normal GET
                with open(file_path) as fileP:
                    # 200 OK
                    response = "HTTP/1.1 "
                    response += "200 OK\r\n"
                    response += "Date: " + date + "\r\n"
                    response += "Server: David's Python Server (Ubuntu)\r\n"
                    response += "Content-Length: " + str(os.stat(file_path).st_size) + "\r\n"
                    response += "Content-Type: " + content_type + "\r\n"
                    response += "Last-Modified: " + str(os.stat(file_path).st_mtime) + "\r\n"
                    response += "\r\n"
                    response += fileP.read()
                    self.clients[fd].send(response)
            
            else:
                #no full request, put in cache
                cache = self.caches[fd]
                cache += new_data
                self.caches[fd] = new_data


        else:
            self.poller.unregister(fd)
            self.clients[fd].close()
            del self.clients[fd]
            del self.last_action_times[fd]
            del self.caches[fd]