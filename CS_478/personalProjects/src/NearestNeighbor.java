import java.util.ArrayList;

public class NearestNeighbor extends SupervisedLearner{
	
	ArrayList<double[]> feats;
	double[] myLabels;
	
	//choose k, or how many neighbors are involved
	int k = 15;
	
	//options for all the variations of knn
	boolean regression = false;
	boolean weighted = true;
	boolean reduction = true;
	boolean reductionSecondPass = true;
	
	int numClasses = 0; 
	int numFeatures = 0;

	public NearestNeighbor(){};
	
	@Override
	public void train(Matrix features, Matrix labels) throws Exception {

		feats = features.m_data;
		ArrayList<double[]> tempLabels = labels.m_data;
		
		if(reduction)
		{
			reduce(tempLabels);
			
			//doesn't help the accuracy, but option is here
			if(reductionSecondPass)
				reduce(tempLabels);
		}
			

		myLabels = new double[tempLabels.size()];
		
		//just easier this way, array instead of arraylist(array)
		for(int i = 0; i < tempLabels.size(); i++)
		{
			myLabels[i] = tempLabels.get(i)[0];
		}
		
		numClasses = labelMax(myLabels);
		numFeatures = feats.get(0).length;
	}
	
	public void reduce(ArrayList<double[]> tempLabels) throws Exception
	{
		//one pass through instances 
		for(int i = 0; i < feats.size(); i++)
		{
			double[] instance = feats.remove(0);
			double[] label = tempLabels.remove(0);
			myLabels = new double[tempLabels.size()];
			for(int j = 0; j < tempLabels.size(); j++)
			{
				myLabels[j] = tempLabels.get(j)[0];
			}
		
			double[] guessLabel = new double[1];
		
			numClasses = labelMax(myLabels);
			numFeatures = feats.get(0).length;
		
			predict(instance, guessLabel);
		
			if(guessLabel[0] == label[0])
			{
				feats.add(instance);
				tempLabels.add(label);
			}
		}
		
		System.out.println("number of instances now: " + feats.size());
	}

	@Override
	public void predict(double[] features, double[] labels) throws Exception {
		
		//find k nearest neighbors
		//same for all 4 variations
		      
		double[] distances = new double[k];
		double[] instances = new double[k];
		      
		//start with nearest neighbors being first k instances
		for(int i = 0; i < k; i++)
		{
		      distances[i] = distance(features, feats.get(i));
		      instances[i] = i;
		}
		      
		int maxNeighbor = arrayMax(distances);
		      
		for(int i = k; i < feats.size(); i++)
		{
		    double curDistance = distance(features, feats.get(i));

		    if(curDistance < distances[maxNeighbor])
		    {
		    	distances[maxNeighbor] = curDistance;
		    	instances[maxNeighbor] = i;
		    	maxNeighbor = arrayMax(distances);
		    }
		}
		
		//branch into one of 4 variations
        if(regression)
        {
        	if(weighted)
        		labels[0] = regressionWeighted(instances, distances);
        	else
        	{
        		labels[0] = regression(instances);
        	}
        }
        else
        {
        	if(weighted)
        		labels[0] = weighted(instances, distances);
        	else
        		labels[0] = original(instances);
        	
        }
	}
	
	public double original(double[] instances)
	{
		  double[] guessCount = new double[numClasses];
		  
		  for(int i = 0; i < k; i++)
		  {
			  guessCount[(int)myLabels[(int)instances[i]]]++;
		  }
		  
		  return arrayMax(guessCount);
	}
	
	public double weighted(double[] instances, double[] distances)
	{
		  double[] guessCount = new double[numClasses];
		  double denominator = 0;
		  
		  for(int i = 0; i < k; i++)
		  {
			  double temp  = 1 / Math.pow(distances[i], 2);
			  guessCount[(int)myLabels[(int)instances[i]]] += temp;
			  denominator += temp;
		  }
		  
		  for(int i = 0; i < numClasses; i++)
		  {
			  guessCount[i] = guessCount[i] / denominator;
		  }
				
		  return arrayMax(guessCount);
	}
	
	public double regression(double[] instances)
	{
		  double guessCount = 0;
		  
		  for(int i = 0; i < k; i++)
		  {
			  guessCount += myLabels[(int)instances[i]];
		  }
		  
		  
		  double mean = guessCount / k;	

		  return mean;
	}
	
	public double regressionWeighted(double[] instances, double[] distances)
	{
		  double guessCount = 0;
		  double denominator = 0;
		  
		  for(int i = 0; i < k; i++)
		  {
			  double temp  = 1 / Math.pow(distances[i], 2);
			  guessCount += temp * myLabels[(int)instances[i]];
			  denominator += temp;
		  }
		  

	      double mean = guessCount / denominator;
		  return mean;
	}
	
	//distances between two instances
	public double distance(double[] inst1, double[] inst2)
	{
		double distance = 0;
		
		for(int i = 0; i < inst1.length; i++)
		{
			distance += Math.pow((inst1[i] - inst2[i]), 2);
		}
		
		return  Math.sqrt(distance);
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
	
	//to dynamically calculate the number of output classes
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

}
