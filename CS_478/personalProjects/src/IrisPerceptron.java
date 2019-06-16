import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class IrisPerceptron extends SupervisedLearner {

	double L_RATE = .1;
	int NUM_EPOCHS = 5;
	double[] zero_one_weights;
	double[] one_two_weights;
	double[] zero_two_weights;
	
	public IrisPerceptron(Random rand){}

	public void train(Matrix features, Matrix labels) throws Exception {
		
		ArrayList<double[]> feats = features.m_data;
		ArrayList<double[]> targets = labels.m_data;
		
		//--------------------------------------------------------------
		// Divide into three perceptrons to separate each pair
		//--------------------------------------------------------------
		
		ArrayList<double[]> zero_one_feats = new ArrayList<double[]>();
		ArrayList<double[]> zero_one_targets = new ArrayList<double[]>();
		for(int i=0; i<feats.size(); i++)
		{
			if(targets.get(i)[0] == 0 || targets.get(i)[0] == 1)
			{
				zero_one_feats.add(feats.get(i));
				zero_one_targets.add(targets.get(i));
			}
		}
		zero_one_weights = trainPair(zero_one_feats, zero_one_targets);
		
		
		ArrayList<double[]> one_two_feats = new ArrayList<double[]>();
		ArrayList<double[]> one_two_targets = new ArrayList<double[]>();
		for(int i=0; i<feats.size(); i++)
		{
			if(targets.get(i)[0] == 1 || targets.get(i)[0] == 2)
			{
				one_two_feats.add(feats.get(i));
				
				double[] temp = new double[1];
				if(targets.get(i)[0] == 1)	
					temp[0] = 0;
				else
					temp[0] = 1;
				
				one_two_targets.add(temp);
			}
		}
		one_two_weights = trainPair(one_two_feats,one_two_targets);
		
		ArrayList<double[]> zero_two_feats = new ArrayList<double[]>();
		ArrayList<double[]> zero_two_targets = new ArrayList<double[]>();
		for(int i=0; i<feats.size(); i++)
		{
			if(targets.get(i)[0] == 0 || targets.get(i)[0] == 2)
			{
				zero_two_feats.add(feats.get(i));

				double[] temp = new double[1];
				if(targets.get(i)[0] == 0)	
					temp[0] = 0;
				else
					temp[0] = 1;
				
				zero_two_targets.add(temp);
			}
		}
		zero_two_weights = trainPair(zero_two_feats, zero_two_targets);
	}
	
	public double[] trainPair(ArrayList<double[]> feats, ArrayList<double[]> targets)
	{
		int cols = 5;
		
		//initialize an array that represents indices of features
		int[] shuffleArray = new int[feats.size()];
		
		for(int i=0; i<shuffleArray.length; i++)
		{
			shuffleArray[i] = i;
		}
		double[] weights = new double[cols];
		
		int numEpochs = 0;
		int globalNumWrong = Integer.MAX_VALUE;
		int noChangeCount = 0;
		boolean notImprovedFiveTimes = true;
		
		//continue until 5 epochs have passed with no improvement
		while(notImprovedFiveTimes)
		{
			numEpochs++;
			int numWrong = 0;
			
			shuffleArray(shuffleArray);
			
			//each epoch contained in this for loop
			for(int i = 0; i < feats.size(); i++)
			{
				double[] row = feats.get(shuffleArray[i]);
				double target = targets.get(shuffleArray[i])[0];
				double net = 0;
			
				//calculate the net based on inputs and weights
				for(int x = 0; x < (cols - 1); x++)
				{
					net += row[x] * weights[x];
				}
			
				//add in the bias weight
				net += weights[cols-1];
			
				double output = 0.00;
			
				if(net > 0)
					output = 1.00;
			
				//this is to keep track of each ephoch's accuracy
				if(target != output)
					numWrong++;
			
				//update weights accordingly
				for(int x = 0; x < (cols - 1); x++)
				{
					double weightChange = L_RATE * row[x] 
							*(target - output);
				
					weights[x] += weightChange;
				}
			
				weights[cols - 1] += L_RATE * (target - output);
			}
			
			//code to help stop when no more improvement for 5 ephochs
			if(numWrong < globalNumWrong)
			{
				globalNumWrong = numWrong;
				noChangeCount = 0;
			}
			else
			{
				noChangeCount++;
				if(noChangeCount == NUM_EPOCHS)
					notImprovedFiveTimes = false;
			}
			
			System.out.println("EPOCH NUMBER: " + numEpochs 
					+ "NUM WRONG: " + numWrong);
					
			
	    }
		
		System.out.println("Number of epochs: " + numEpochs + "\n");
	    System.out.println("Final weights");
	    for(int i = 0; i < weights.length; i++)
	    {
	    	System.out.println(weights[i]);
	    }
	    
	    System.out.println("");
		return weights;
	}

	public void predict(double[] features, double[] labels) throws Exception {
		
		double zero_one_net = 0.00;
		double one_two_net = 0.00;
		double zero_two_net = 0.00;
		
		for(int x = 0; x < (features.length - 1); x++)
		{
			zero_one_net += features[x] * zero_one_weights[x];
			one_two_net += features[x] * one_two_weights[x];
			zero_two_net += features[x] * zero_two_weights[x];
		}
	
		double zero_one_output = 0.00;
		double one_two_output = 0.00;
		double zero_two_output = 0.00;
		
	
		if(zero_one_net > 0)
			zero_one_output = 1.00;
		if(one_two_net > 0)
			one_two_output = 1.00;
		if(zero_two_net > 0)
			zero_two_output = 1.00;
		
		if(zero_one_output == 0)
		{
			if(zero_two_output == 0)
			{
				labels[0] = 0.00;
			}
			else
			{
				if(zero_one_net < zero_two_net)
					labels[0] = 0.00;
				else
					labels[0] = 2.00;
			}
		}
		else
		{
			if(one_two_output == 0)
			{
				labels[0] = 1.00;
			}
			else
			{
				if(one_two_net < one_two_net)
					labels[0] = 1.00;
				else
					labels[0] = 2.00;
			}
		}
		if(zero_two_output == 1 && one_two_output == 1)
			labels[0] = 2.00;
	}
	
	  static void shuffleArray(int[] ar)
	  {
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }

}
