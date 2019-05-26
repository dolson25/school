#!/usr/bin/env python

import threading
import bs4
import requests

class requestThread (threading.Thread):
	def __init__(self,url,action,content):
		threading.Thread.__init__(self)
		self.url = url
		self.action = action
		self.content = content
		self.headlines = []
		self.lock = threading.Lock()
	def run(self):
		c = requests.get(self.url).content
		if(self.action == 1):
			#google news
			with self.lock:
				self.content.append(c)
		else:
			#twitter headlines
			soup = bs4.BeautifulSoup(c, 'html.parser')
			for headline in soup.find_all('p',{"class" :"tweet-text"}):
				self.headlines.append(headline)
    		with self.lock:
    			self.content.append(self.headlines)

content = []

googleThread = requestThread("http://news.google.com", 1, content)
twitterThread = requestThread("https://twitter.com/whatstrending", 2, content)

googleThread.start()
twitterThread.start()

googleThread.join()
twitterThread.join()

print "Content-type: text/html"
print
print "<h1>News</h1>"
#print content[1]
print "<div style='width:100%;'>" 
print	"<div style='width:45%; float: left;'>",content[0],"</div"
print	"<div style='width:45%; float: right;'>",content[1],"</div"
print "</div> ==$0"

#print "content length: ", len(content)
#print "twitter length: ", len(content[1])
