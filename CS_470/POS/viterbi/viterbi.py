import random

def viterbi(obs, states, start_p, trans_p, emit_p, obs_space):
	
	V = [{}] 

	#print V

	for st in states:
		V[0][st] = {"prob": start_p[st] * emit_p[st][obs[0]], "prev": None}
		
	#print V

	# Run Viterbi when t > 0

	for t in range(1, len(obs)):

		V.append({})

		#print V 

		for st in states:
			#print "here ", st 

			#for prev_st in states:
				#print V[t-1]
				#print V[t-1][prev_st]["prob"]

			max_tr_prob = max(V[t-1][prev_st]["prob"]*trans_p[prev_st][st] for prev_st in states)


			for prev_st in states:

				if V[t-1][prev_st]["prob"] * trans_p[prev_st][st] == max_tr_prob:

					try:
						max_prob = max_tr_prob * emit_p[st][obs[t]]
					except:
						max_prob = max_tr_prob * emit_p[st][random.choice(obs_space)]
					V[t][st] = {"prob": max_prob, "prev": prev_st}

					break

	#for line in dptable(V):
		#print line

	opt = []

	# The highest probability

	max_prob = max(value["prob"] for value in V[-1].values())

	previous = None

	# Get most probable state and its backtrack

	for st, data in V[-1].items():

		if data["prob"] == max_prob:

			opt.append(st)

			previous = st

			break


	# Follow the backtrack till the first observation

	for t in range(len(V) - 2, -1, -1):

		opt.insert(0, V[t + 1][previous]["prev"])

		previous = V[t + 1][previous]["prev"]


	print 'The steps of states are ' + ' '.join(opt) + ' with highest probability of %s' % max_prob
			


def dptable(V):

    # Print a table of steps from dictionary

    yield " ".join(("%12d" % i) for i in range(len(V)))

    for state in V[0]:

        yield "%.7s: " % state + " ".join("%.7s" % ("%f" % v[state]["prob"]) for v in V)

print "TRAINING"
infilename = "smallestTrain.txt"
trainingdata = open(infilename).read()

words_obs = []
pos_states = []

emission_dictionary = {}

for pair in trainingdata.split():
	words_obs.append(pair.split('_')[0])
	pos_states.append(pair.split('_')[1])

	#emission dictionary stuff
	if pair.split('_')[1] in emission_dictionary:
		emission_dictionary[pair.split('_')[1]].append(pair.split('_')[0])
	else:
		emission_dictionary[pair.split('_')[1]] = [pair.split('_')[0]]

#print trainingdata
#print emission_dictionary
#print ""

#observation space
obs_space = set(words_obs)
obs_size = len(obs_space)
#print obs_space
#print ""
#print "Observation Size: " , obs_size
#print ""

#print pos_states
state_space = set(pos_states)
#print state_space
#print ""
#print transition_dictionary

state_size = len(state_space)
#print state_space


emission_matrix = [[0 for x in range(obs_size)] for y in range(state_size)]



i = 0
for this_pos in state_space:
	attached_words = emission_dictionary[this_pos]
	total_attachments = len(attached_words)
	j = 0
	for another_obs_word in obs_space:
		occurences = attached_words.count(another_obs_word)
		emission_matrix[i][j] = float(occurences) / float(total_attachments)
		j += 1

 	i+=1

for i in range(state_size):
	row = ""
	for j in range(obs_size):
		row += str(format(emission_matrix[i][j],'.1f')) + " "
	#print row



transition_dictionary = {}

for i in range(len(pos_states)):
	if(not(i == (len(pos_states)-1))):
		if pos_states[i] in transition_dictionary:
			transition_dictionary[pos_states[i]].append(pos_states[i+1])
		else:
			transition_dictionary[pos_states[i]] = [pos_states[i+1]]



transition_matrix = [[0 for x in range(state_size)] for y in range(state_size)] 

i = 0
for this_pos in state_space:
	next_ones = transition_dictionary[this_pos]
	total_transitions = len(next_ones)
	j = 0
	for another_pos in state_space:
		occurences = next_ones.count(another_pos)
		transition_matrix[i][j] = float(occurences) / float(total_transitions)
		j += 1

	i += 1

for i in range(state_size):
	row = ""
	for j in range(state_size):
		row += str(format(transition_matrix[i][j],'.1f')) + " "
	print row, ""


# "initial probabilites"
probabils = []

for i in range(state_size):
	probabils.append(1 / float(state_size))

#print probabils





#state space)
#print state_space

#sequence of observations
#print words_obs

#transition matrix (probability of one state to another)

state_tuple = ()
for st in state_space:
	#print type(state_tuple)
	#print type(('st',))
	state_tuple = state_tuple + (st,)

obs_tuple = ()
for obs in obs_space:
	obs_tuple = obs_tuple + (obs,)

start_probs_dict = {}
for pos in state_space:
	start_probs_dict[pos] = (1 / float(state_size))

trans_probs_dictionary = {}
for i in range(state_size):
	inner_dict = {}
	for j in range(state_size):
		inner_dict[state_tuple[j]] = transition_matrix[i][j]
	trans_probs_dictionary[state_tuple[i]] = inner_dict	

emission_probs_dictionary = {}
for i in range(state_size):
	inner_dict = {}
	for j in range(obs_size):
		inner_dict[obs_tuple[j]] = emission_matrix[i][j]
	emission_probs_dictionary[state_tuple[i]] = inner_dict	


#obs_tuple = ()
#for obs in words_obs:
	#obs_tuple = obs_tuple + (obs,)
#obs_tuple = obs_tuple + ("ASTRINGTHEYWOULDNTHAVE",)
#print obs_tuple

#now use the training data
infilename = "smallestTest.txt"
testingdata = open(infilename).read()

words_obs = ()

for pair in testingdata.split():
	words_obs = words_obs + (pair.split('_')[0],)

#debug
#viterbi(obs_tuple, state_tuple, start_probs_dict, trans_probs_dictionary, emission_probs_dictionary)
print "TESTING"
#training/testing
viterbi(words_obs, state_tuple, start_probs_dict, trans_probs_dictionary, emission_probs_dictionary, list(obs_space))

