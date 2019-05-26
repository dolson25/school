keyfilename = "smallestTest.txt"
outputfilname = "outpart.txt"

keys = open(keyfilename).read()
output = open(outputfilname).read()

output_count = 0

pos_list = []
for pos in output.split():
	output_count += 1;
	pos_list.append(pos)

#print output_count

keys_list = keys.split()

key_list = []
for i in range(output_count):
	key_list.append(keys_list[i].split('_')[1])

print len(pos_list)
print len(key_list)

correct_count = 0
for i in range(output_count):
	if (pos_list[i] == key_list[i]):
		correct_count += 1

print correct_count

final_output = ""
for i in range(output_count):
	final_output += keys_list[i] + " "

output += "\n"

for i in range(output_count):
	final_output += pos_list[i] + " "

print final_output

	