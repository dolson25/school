import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Perceptron extends SupervisedLearner {

	double L_RATE = .1;
	int NUM_EPOCHS = 5;
	double[] weights;
	
	public Perceptron(Random rand){}

	public void train(Matrix features, Matrix labels) throws Exception {

		ArrayList<double[]> feats = features.m_data;
		ArrayList<double[]> targets = labels.m_data;
		int cols = features.cols() + 1;
		
		//initialize an array that represents indices of features
		int[] shuffleArray = new int[feats.size()];
		
		for(int i=0; i<shuffleArray.length; i++)
		{
			shuffleArray[i] = i;
		}
		weights = new double[cols];
		
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
	
	}

	public void predict(double[] features, double[] labels) throws Exception {
		
		//now we just use the final weights to predict the 
        //label for each set of features
		
		double net = 0.00;
		
		for(int x = 0; x < (features.length - 1); x++)
		{
			net += features[x] * weights[x];
		}
	
		double output = 0.00;
		
	
		if(net > 0)
			output = 1.00;

		labels[0] = output;
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
