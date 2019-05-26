#!/usr/bin/env python
import os.path

print "Content-type: text/html"
print
print "<h1>Grades</h1>"
print "<p>"
f = open(os.path.dirname(__file__) + "/../grades.txt")

for line in f:

	if(line[0] == '#'):
		line = line.rstrip()
		print "<h2>" , line[1:], "</h2>"
	elif(line == "\n"):
		print line
	else:
		dyad = line.split()
		print dyad[0], "<br>", dyad[1], "<br>"
print "</p>"