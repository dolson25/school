import random
 
infilename = "obama.txt"
trainingdata = open(infilename).read()

contextconst = [""]
 
context = contextconst
model = {}



for word in trainingdata.split():
    model[str(context)] = model.setdefault(str(context),[])+ [word]
    context = (context+[word])[1:]

print model

context = contextconst
print context
context = random.choice(model.keys())
print context 

output = ""
for i in range(100):
    word = random.choice(model[str(context)])
    context = ([context]+[word])[1:]
    output += context[0] + " "

#print output

