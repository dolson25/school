#!/usr/bin/env python
import requests

r = requests.get("http://news.google.com")

print "Content-type: text/html"
print
print "<h1>News</h1>"

print r.content
