import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class Clustering extends SupervisedLearner{

	ArrayList<double[]> feats;
	
	//number of variations of each feature, used for nominal features
	double[] numAttributes;
	//the enum to string from the matrix class
	ArrayList<TreeMap<Integer,String>> tma;
	
	ArrayList<ArrayList<Integer>> clusters;
	ArrayList<double[]> centroids;
	
	//how many clusters we will have
	int k = 4;
	
	//this is only false for part one to check the algorithm
	boolean random = false;
	//this is for my own experiment
	boolean exp = true;
	
	int numFeats = 0;
	int numInst = 0;
	double max = Double.MAX_VALUE;
	
	//sum squared error
	double sse = 0;
	
	@Override
	public void train(Matrix features, Matrix labels) throws Exception {
		//initialize stuff
		tma = features.m_enum_to_str;
		feats = features.m_data;		
		numFeats = feats.get(1).length;
		numInst = feats.size();
		numAttributes = new double[numFeats];
		
		//initialize numAttributes using treemap from matrix
		for(int i = 0; i < numFeats; i++)
		{
			numAttributes[i] = tma.get(i).size();
		}
		
		//initialize original k centroids
        makeInitialCentroids();
		
		printCentroids(centroids);

		//assign points to different centroids/clusters
		makeAssignments();
		
		//for part 4 
		calculateSilhouette();
		
		//calculate sum squared error
		calculateSSE();
		
		boolean sseChange = true;
		double curSSE = sse;
		
		int iterationCounter = 2;
		//until there is no more changing of the sse
		while(sseChange)
		{
			System.out.println("ITERATION " + iterationCounter);
			iterationCounter++;
			
			centroids = new ArrayList<double[]>();
			
			System.out.println("computing centroids");
			
			for(int i = 0; i < k; i++)
			{
				centroids.add(makeNewCentroid(clusters.get(i)));
			}
			
			printCentroids(centroids);
			
			//assign points to different centroids/clusters
			makeAssignments();
			
			//for part 4 
			calculateSilhouette();
			
			//calculate sum squared error
			calculateSSE();
			
			if(curSSE == sse)
				sseChange = false;
			else
				curSSE = sse;
		}	
	}
	
	public void makeInitialCentroids()
	{
		System.out.println("computing centroids");
		
		centroids = new ArrayList<double[]>();
		
		if(random)
		{
			Set randSet = new HashSet<Integer>();
			Random rand =  new Random();
			
			while(randSet.size() < k)
			{
				randSet.add(rand.nextInt(numInst));
			}
			
			 Iterator<Integer> it = randSet.iterator();
		     while(it.hasNext()){
		        int i = it.next();
				double[] centroid = new double[numFeats];
			
				for(int j = 0; j < numFeats; j++)
				{
					centroid[j] = feats.get(i)[j];
				}
			
				centroids.add(centroid);
			}
		     
		    
		}
		else if(exp)
		{
			
			Random rand = new Random();
			
			for(int i = 0; i < k; i++)
			{
				double[] temp = new double[numFeats];
				if(i < (k - 1))
					temp[i] = 3 * rand.nextDouble();
				centroids.add(temp);
			}
		}
		else
		{
			for(int i = 0; i < k; i++)
			{
				double[] centroid = new double[numFeats];
			
				for(int j = 0; j < numFeats; j++)
				{
					centroid[j] = feats.get(i)[j];
				}
			
				centroids.add(centroid);
			}
		}
	}
	
	public void makeAssignments()
	{
		clusters  = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < k; i++)
		{
			clusters.add(new ArrayList<Integer>());
		}
		
		System.out.println("making assignments");
		for(int i = 0; i < feats.size(); i++)
		{
			double minDistance = max  ;
			int minCentroid = 0;
			
			for(int j = 0; j < centroids.size(); j++)
			{
				double tempDist = distance(feats.get(i), centroids.get(j));
				
				if(tempDist < minDistance)
				{
					minDistance = tempDist;
					minCentroid = j;
				}
			}
			
			if(Math.floorMod(i, 8) == 0)
				System.out.println(i + "=" + minCentroid);
			else
				System.out.print(i + "=" + minCentroid + " ");
			
			clusters.get(minCentroid).add(i);
		}
		
		System.out.println("");
		
		printClusterNumbers();
	}
	
	public void printClusterNumbers()
	{
		System.out.println("");
		for(int i = 0; i < clusters.size(); i++)
		{
			System.out.println("cluster " + i + " " + clusters.get(i).size());
		}
		System.out.println("");
	}
	
	public void calculateSSE()
	{
		double sse = 0;
		
		for(int i = 0; i < clusters.size(); i++)
		{
			double clusterSSE = 0;
			for(int j = 0; j < clusters.get(i).size(); j++)
			{
				int instance = clusters.get(i).get(j);
				sse += distance(feats.get(instance), centroids.get(i));
				clusterSSE += distance(feats.get(instance), centroids.get(i));
			}
			System.out.println("Cluster " + i + " sse " + clusterSSE);
		}
		
		this.sse = sse;
		System.out.println("totalSSE " + sse + "\n");
	}
	
	public void calculateSilhouette()
	{
		System.out.println("");
		
		System.out.println("Calculating Silhouette");
		
		double si = 0;
		
		for(int i = 0; i < clusters.size(); i++)
		{
			for(int j = 0; j < clusters.get(i).size(); j++)
			{
				//calculate a(i)
				double ai = calculateDissimilarity(feats.get(clusters.get(i).get(j)), i);
			
				//calculate b(i)
				double bi = max;
				
				for(int l = 0; l < clusters.size(); l++)
				{
					if(l != i)
					{
						double tempBI = calculateDissimilarity(feats.get(clusters.get(i).get(j)), l);
						if(tempBI < bi)
							bi = tempBI;
					}
				}
				
				//calculate s(i)
				si += (bi - ai) / Math.max(ai, bi);
				//System.out.println("thisSilhouette: " + (bi - ai) / Math.max(ai, bi)); 
			}
		}
		
		System.out.println("Silhouette: " + si / numInst);
		
		System.out.println("");
	}
	
	public double calculateDissimilarity(double[] inst, int cluster)
	{
		double clusterSize = clusters.get(cluster).size();
		double distances = 0;
		
		for(int i = 0; i < clusterSize; i++)
		{
			distances += distance(feats.get(clusters.get(cluster).get(i)),inst);
		}
		
		return distances / clusterSize;
	}
	
	//returns the "distance" between two instances
	public double distance(double[] inst1, double[] inst2)
	{
		double distance = 0;
		
		for(int i = 0; i < inst1.length; i++)
		{
			double p1 = inst1[i];
			double p2 = inst2[i];
			
			//split real/nominal values
			if(tma.get(i).isEmpty())
			{
				//if missing
				if((p1 == max) | (p2 == max))
				{
					distance += 1;
				}
				else
				{
					distance += Math.pow((p1 - p2), 2);
				}
			}
			else
			{
				//if missing
				if((p1 == max) | (p2 == max) | (p1 != p2))
				{
					distance += 1;
				}
			}
			
		}
		
		return  distance;
	}
	
	public double[] makeNewCentroid(ArrayList<Integer> cluster)
	{
		double[] newCentroid = new double[numFeats];
		
		for(int i = 0; i < numFeats; i++)
		{
			//real/nominal
			if(tma.get(i).isEmpty())
			{
				double realTotal  = 0;
				int missingTotal = 0;
				
				for(int j = 0; j < cluster.size(); j++)
				{
					double value = feats.get(cluster.get(j))[i];
					
					//is it a missing value?
					if(value == max)
						missingTotal++;
					else
						realTotal += feats.get(cluster.get(j))[i];
				}
				
				//if all of the values were  ?
				if(missingTotal == cluster.size())
					newCentroid[i] = max;
				else
				{
					realTotal /= (cluster.size() - missingTotal);
					newCentroid[i] = realTotal;
				}

			}
			else
			{
				//count up instances of each attribute
				
				double[] addUp = new double[tma.get(i).size()];
				int missingTotal = 0;
				
				for(int j = 0; j < cluster.size(); j++)
				{
					double value = feats.get(cluster.get(j))[i];
					
					//is it a missing value?
					if(value == max)
						missingTotal++;
					else
						addUp[(int)value]++;
				}
				
				//if all of the values were  ?
				if(missingTotal == cluster.size())
					newCentroid[i] = max;
				else
				{
					int mostCommonClass = arrayMax(addUp);
					newCentroid[i] = mostCommonClass;
				}
			}
		}
		
		return newCentroid;
	}
	
	public void printCentroids(ArrayList<double[]> centroids)
	{
		for(int i = 0; i < centroids.size(); i++)
		{
			System.out.print("Centroid " + i + ": ");
			
			for(int j = 0; j < numFeats; j++)
			{
				if(centroids.get(i)[j] == max)
					System.out.print("   ?   ");
				else if(tma.get(j).isEmpty())
					System.out.print(String.format("%.3f",
						centroids.get(i)[j]) + ", ");
				else
					System.out.print(tma.get(j).get((int)centroids.get(i)[j]) + ", ");
			}
			
			System.out.println("");
		}
		
		System.out.println("");
	}
	
	//returns the index of the largest value in the array
	public int arrayMax(double[] array)
	{
		int maxI = 0;
		double maxD = 0;
		
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] > maxD)
			{
				maxI = i;
				maxD = array[i];
			}
		}
		
		return maxI;
	}

	@Override
	public void predict(double[] features, double[] labels) throws Exception {}

}
