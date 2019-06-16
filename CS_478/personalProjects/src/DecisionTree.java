import java.util.ArrayList;

public class DecisionTree extends SupervisedLearner {
	
	public void SupervisedLearner(){}
	
	Node root;
	ArrayList<Integer> valFeats;
	
	//prune vars
	boolean rep = true;
	boolean pruned = true;
	Node pruneNode = null;
	double bestAccuracy = 0;
	
	//my experiment
	boolean buildTree = true;

	@Override
	public void train(Matrix features, Matrix labels) throws Exception {
		
		bestAccuracy = 0;
		pruned = true;
		
		ArrayList<double[]> feats = features.m_data;
		ArrayList<double[]> tempLabes = labels.m_data;
		int numAttributes = feats.get(0).length;
		int[] numAttrClasses = featMax(feats);
		int numInstances = feats.size();
		ArrayList<Integer> trainInst = new ArrayList<Integer>();
		ArrayList<Integer> valInst = new ArrayList<Integer>();
		
		//just easier to work with
		double[] labes = new double[numInstances];
		for(int i = 0; i < numInstances; i++)
		{
			labes[i] = tempLabes.get(i)[0];
		}
		
		//these help us find the entropy
		int numClasses = labelMax(labes);
		double[] classCounter = new double[numClasses];
		
		//we can turn reduced error pruning off and on
		if(rep)
		{
			//divide set into train and validation
			int barrier = numInstances * 9 / 10;
			valFeats = new ArrayList<Integer>();
			
			for(int i = 0; i < numInstances; i++)
			{
				if(i > barrier)
				{
					valFeats.add(i);
					valInst.add(i);
				}
				else
					trainInst.add(i);
			}
		}
		else
		{
		    for(int i = 0; i < numInstances; i++)
		    {
		    	trainInst.add(i);
		    }
		}
		

		//for each node this will keep track of attributes still left
		ArrayList<Integer> attr = new ArrayList<Integer>();
		
		for(int i = 0; i < numAttributes; i++)
		{
			attr.add(i);
		}
		
		root = new Node(feats, labes, trainInst, attr, 
				numClasses, numAttrClasses,classCounter, null, 0);
		
		if(!buildTree)
			makeTreeRec(root);
		else
		{
			buildTreeRec(root, feats, labes, valFeats);
			System.out.println("#nodes " + numberNodes(root));
			System.out.println("maxDepth " + maxDepth(root));
		}
		
		
		//reduced error pruning
		if(rep & !buildTree)
		{
			bestAccuracy = validationAccuracy(feats, labes, valFeats);
			//System.out.println("BEST ACCURACY: " + bestAccuracy);
			while(pruned)
			{
				pruned = false;
				pruneRec(root, feats, labes, valFeats);
				if(pruned)
				{
					pruneNode.children = null;
				}
			}
			
			//System.out.println("#nodes " + numberNodes(root));
			//System.out.println("maxDepth " + maxDepth(root));
		}
		
	}
	
	double validationAccuracy(ArrayList<double[]> feats, double[] labels,
			                                       ArrayList<Integer> valFeats)
	{
		double valSetSize = valFeats.size();
		double correct = 0;
		
		for(int i = 0; i < valFeats.size(); i++)
		{
			double prediction = predictRec(root, feats.get(i));
			if(prediction == labels[i])
				correct++;
		}
		
		return correct / valSetSize;
	}
	
	public void pruneRec(Node n, ArrayList<double[]> feats, double[] labels,
	                                             ArrayList<Integer> valFeats)
	{
		//System.out.println("class: " + n.getMostCommonClass());
		if(n.getChildren() == null)
		{
			//System.out.println("leaf node");
			//System.out.println("");
		}
		else
		{
			//System.out.println("not leaf node");
			//System.out.println("");
			
			//test accuracy if subtrees
			Node[] temp = n.children;
			n.children = null;
			
			double tempAccuracy = validationAccuracy(feats, labels, valFeats);
			
			if(tempAccuracy > bestAccuracy)
			{
				pruneNode = n;
				pruned = true;
				bestAccuracy = tempAccuracy;
				//System.out.println("TEMP ACCURACY: " + bestAccuracy);
			}
			
			n.children = temp;
			
			for(int i = 0; i < n.getChildren().length; i++)
			{
				pruneRec(n.getChildren()[i], feats, labels, valFeats);
			}
		}
	}
	
	public void makeTreeRec(Node n)
	{
		n.go();
		Node[] children = n.getChildren();
		if(children != null)
		{
			for(int i = 0; i < children.length; i++)
			{
				makeTreeRec(children[i]);
			}
		}
	}
	
	public void buildTreeRec(Node n, ArrayList<double[]> feats, double[] labels,
                                            ArrayList<Integer> valFeats)
	{
		n.go();
		Node[] children = n.getChildren();
		double tempAccuracy = validationAccuracy(feats, labels, valFeats);
		
		if(tempAccuracy > .93)
			n.children = null;
		else if(children != null)
		{
			for(int i = 0; i < children.length; i++)
			{
				buildTreeRec(children[i], feats, labels, valFeats);
			}
		}
	}
	
	public int labelMax(double[] labels)
	{
		double max = 0;
		
		for(int i = 0; i < labels.length; i++)
		{	
			if(labels[i] > max)
				max = labels[i];
		}
		
		return (int)max + 1;
	}
	
	public int[] featMax(ArrayList<double[]> feats)
	{
		int[] temp = new int[feats.get(0).length];
		for(int i = 0; i < feats.get(0).length; i++)
		{
			double max = 0;
			
			for(int j = 0; j < feats.size(); j++)
			{
				if(feats.get(j)[i] > max)
					max = feats.get(j)[i];
			}
			
			temp[i] = (int)max + 1;
			
		}
		
		return temp;
	}
	
	public int numberNodes(Node n)
	{
		if(n.getChildren() == null)
			return 1;
		else
		{
			int subNodes = 0;
			
			for(int i = 0; i < n.getChildren().length; i++)
			{
				subNodes += numberNodes(n.getChildren()[i]);
			}
			
			return subNodes + 1;
		}
	}
	
	public int maxDepth(Node n)
	{
		if(n.getChildren() == null)
			return n.depth;
		else
		{
           ArrayList<Integer> temp = new ArrayList<Integer>();
           for(int i = 0; i < n.getChildren().length; i++)
           {
        	   temp.add(maxDepth(n.getChildren()[i]));
           }
           int maxD = 0;
           for(int i = 0; i < temp.size(); i++)
           {
        	   if(temp.get(i) > maxD)
        		   maxD = temp.get(i);
           }
           return maxD;
		}
	}

	@Override
	public void predict(double[] features, double[] labels) throws Exception {
		labels[0] = predictRec(root, features);	
	}
	
	public double predictRec(Node n, double[] features)
	{
		if(n.getChildren() == null)
			return n.getMostCommonClass();
		else
		{
			return predictRec(n.getChildren()[(int)features[(int)n.getAttrSplitOn()]], features);
		}
	}
	
	protected class Node
	{
		ArrayList<double[]> feats;
		double[] labels;
		Node[] children;
		ArrayList<Integer> instances;
		ArrayList<Integer> attributes;
		int numClasses;
		int[] numAttrClasses;
		double[] classCounter;
		double nodeEntropy;
		double mostCommonClass;
		int depth;
		Node parent;
		int attrSplitOn;
		
		protected Node(ArrayList<double[]> feats, double[] labels,
				                           ArrayList<Integer> instances,
				                           ArrayList<Integer> attributes,
				                           int numClasses, int[] numAttrClasses,
				                           double[] classCounter, Node parent,
				                           int depth)
		{
			this.feats = feats;
			this.labels = labels;
			children = null;
			this.instances = instances;
			this.attributes = attributes;
			this.numClasses = numClasses;
			this.numAttrClasses = numAttrClasses;
			this.classCounter = classCounter;
			nodeEntropy = 0;
			mostCommonClass = 0;
			this.parent = parent;
			this.depth = depth;
		}
		
		protected void go()
		{
			//half if out of attributes or there were no instances in this node
			if(instances.size() == 0 || attributes.size() == 0)
			{
				mostCommonClass = parent.getMostCommonClass();
			}
			else
			{
			
				//not total, but those left in this node
				int numInstances = instances.size();
			
				//clear the output class counter
				for(int i = 0; i < classCounter.length; i++)
				{
					classCounter[i] = 0;
				}
			
				//count the labels from each class
				for(int i = 0; i < numInstances; i++)
				{
					classCounter[(int)labels[instances.get(i)]]++;
				}
				
				//decide the most common class here
				mostCommonClass = (double)classMax(classCounter);

				//calculate entropy for this node
				nodeEntropy = info(classCounter, numInstances);
			
				if(nodeEntropy == 0)
				{
					//System.out.println("no more splitting...entropy zero");
				}
				else
				{

					//find info gain of all attributes available
					attrSplitOn = maxGain();
					
					children = new Node[numAttrClasses[attrSplitOn]];
			
					//extend the tree by splitting by the attr with the most info gain
					for(int i = 0; i < numAttrClasses[attrSplitOn]; i++)
					{
						ArrayList<Integer> inst = new ArrayList<Integer>();
						ArrayList<Integer> attr = new ArrayList<Integer>();
				
						//only pass on the instances for that branch
						for(int j = 0; j < instances.size(); j++)
						{
							if(feats.get(instances.get(j))[attrSplitOn] == i)
							{
								inst.add(instances.get(j));
							}
						}
				
						//only pass on the attributes left to be split
						for(int j = 0; j < attributes.size(); j++)
						{
							if(attributes.get(j) != attrSplitOn)
							{
								attr.add(attributes.get(j));
							}
						}
					
				
						Node n = new Node(feats, labels, inst, attr, 
						        numClasses, numAttrClasses, classCounter, this
						        ,this.depth + 1);
				
						children[i] = n;
					}
				}
			}
		}
		
		protected Node[] getChildren()
		{
			return children;
		}
		
		protected double getMostCommonClass()
		{
			return mostCommonClass;
		}
		
		public double getAttrSplitOn()
		{
			return attrSplitOn;
		}
		
		private double info(double[] numers, double denom)
		{
			double result = 0;
			for(int i = 0; i < numers.length; i++)
			{
				double n = numers[i];
				if(n == 0)
					result += 0.0;
				else
					result -= n * Math.log(n / denom) / Math.log(2) / denom;
			}
			
			return result;
		}
		
		private int maxGain()
		{
			double maxGain = 0;
			int maxGainAttr = 0;
			
			//every attribute
			for(int i = 0; i < attributes.size(); i++)
			{
				double attrInfo = 0;
				//every class in that attribute
				for(int j = 0; j < numAttrClasses[attributes.get(i)]; j++)
				{
					double classCount = 0;
					
					//clear the output class counter
					for(int x = 0; x < classCounter.length; x++)
					{
						classCounter[x] = 0;
					}
					
					//every instance applicable
					for(int k = 0; k < instances.size(); k++)
					{
						if(feats.get(instances.get(k))[attributes.get(i)] == j)
						{
							classCounter[(int)labels[instances.get(k)]]++;
							classCount++;
						}
					}
					
					double entropy = info(classCounter, classCount);
					attrInfo += entropy * classCount / instances.size();
				}
				
				double infoGain = nodeEntropy - attrInfo;
				
				if(infoGain > maxGain)
				{
					maxGain = infoGain;
					maxGainAttr = attributes.get(i);
				}
			}
			
			return maxGainAttr;
		}
		
	}
		
	public int classMax(double[] classCounter)
	{
		double max = 0;
		int maxIndex = 0;
			
		for(int i = 0; i < classCounter.length; i++)
		{	
			if(classCounter[i] > max)
			{
				max = classCounter[i];
		        maxIndex = i;
			}
		}
			
		return maxIndex;
	}

}
