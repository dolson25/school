import sys
import requests
import argparse
import threading
import time

class requestThread (threading.Thread):
	def __init__(self,url,_range):
		threading.Thread.__init__(self)
		self.url = url
		self._range = _range
		self.thisBytes = threading.local()
	def run(self):
		self.thisBytes = requests.get(self.url,  headers={'Range': 'bytes='+ self._range}).content;
		

parser = argparse.ArgumentParser()
parser.add_argument('-n', '--num', type=int, action='store', help='Specify the number of threads you want to use',default='10')
parser.add_argument("echo")
args = parser.parse_args()

fileLength = int(requests.head(args.echo).headers['Content-Length']);
chunkLength = fileLength / args.num;

threads = []

start = time.time()

for i in range(args.num):
	if (i < (args.num - 1)):
		byteRange = str(chunkLength * i) + '-' + str(chunkLength * (i + 1) - 1)
	else:
		byteRange = str(chunkLength * i) + '-' + str(fileLength - 1)

	threads.append(requestThread(args.echo,byteRange))


for t in threads:
	t.start()
for t in threads:
	t.join()
end = time.time()

print args.echo, " ", args.num, " ", fileLength, " ", (end - start)

with open('htmlFile', 'wb') as f:
	for i in range(args.num):
		f.write(threads[i].thisBytes)
			












