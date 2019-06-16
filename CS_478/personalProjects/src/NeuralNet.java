import java.util.ArrayList;
import java.util.Random;

public class NeuralNet extends SupervisedLearner {
	
	int hNodes = 3;
	double lr = .175;
	double momentum = .9;
	int inNodes = 2;
	int outNodes = 1;
	boolean iris = false;
	
	double[] inputs;
	
	double[] hlOut = new double[hNodes];
	double[] deltaHidden = new double[hNodes];
	
	double[] deltaOutput = new double[outNodes];
	double[] outputs;
	
	double[] targets;
	
	//weights
	ArrayList<double[]> ihWeights = new ArrayList<double[]>();
	ArrayList<double[]> hoWeights = new ArrayList<double[]>();
	
	//wieght changes
	ArrayList<double[]> ihWC = new ArrayList<double[]>();
	ArrayList<double[]> hoWC = new ArrayList<double[]>();
	
	
	public NeuralNet(Random rand) {}

	@Override
	public void train(Matrix features, Matrix labels) throws Exception {
		
		ArrayList<double[]> feats = features.m_data;
		ArrayList<double[]> myLabels = labels.m_data;
		
		inputs = new double[inNodes];
		targets = new double[outNodes];
		outputs = new double[outNodes];
	
		//weights
		//bias, x0, x1 -> h0; bias, x0, x1 -> h1 
		
		//setRandomWeights();
		setTestWeights();
		
		printWeights();
		
		System.out.println("Input Vector: " + inputs[0] + " " + inputs[1]);
		System.out.println("Output Vector: " + targets[0]);
		
		for(int x = 0; x < 3; x++)
		{
			for(int i = 0; i < feats.size(); i++)
			{
				targets = new double[outNodes];
				for(int j = 0; j < feats.get(i).length; j++)
				{
					inputs[j] = feats.get(i)[j];
				}

				if(iris)
				{
					if(myLabels.get(i)[0] == 0)
						targets[0] = 1;
					else if(myLabels.get(i)[0] == 1)
						targets[1] = 1;
					else
						targets[2] = 1;
				}
				else
					targets[0] = myLabels.get(i)[0];
				
				forwardPropogate();
				backPropogate();
				descendGradient();
				System.out.println("");
				printWeights();
			}
		}
		//initialize an array that represents indices of features
		//int[] shuffleArray = new int[feats.size()];
		
		//for(int i=0; i<shuffleArray.length; i++)
		//{
			//shuffleArray[i] = i;
		//}
		//weights = new double[cols];
		
		//int numEpochs = 0;
		//int globalNumWrong = Integer.MAX_VALUE;
		//int noChangeCount = 0;
		//boolean notImprovedFiveTimes = true;
		
		//continue until 5 epochs have passed with no improvement
		//while(notImprovedFiveTimes)
		//{
			//numEpochs++;
			//int numWrong = 0;
			
			//shuffleArray(shuffleArray);
			
			//each epoch contained in this for loop
			//for(int i = 0; i < feats.size(); i++)
		//}

	}
	
	public void forwardPropogate()
	{
		System.out.println("\nForward Propogating...");
		
		for(int i = 0; i < hNodes; i++)
		{
			//start with bias weight
			double net = ihWeights.get(i)[0]; 
			
			for(int j = 1; j < inNodes + 1; j++)
			{
				net += ihWeights.get(i)[j] * inputs[j - 1]; 
			}
			
			hlOut[i] = output(net);
		}
		
		for(int i = 0; i < outNodes; i++)
		{
			//start with bias weight
			double net = hoWeights.get(i)[0]; 
			
			for(int j = 1; j < hNodes + 1; j++)
			{
				net += hoWeights.get(i)[j] * hlOut[j - 1]; 
			}
			
			outputs[i] = output(net);
		}
		
		System.out.print("Predicted Output: ");
        for (int i = 0; i < outputs.length; i++)
        {
        	System.out.print(outputs[i] + " ");
        }
		System.out.println("");;
	}
	
	public void backPropogate()
	{
		System.out.println("\nBack Propogating...");
		for(int i = 0; i < outNodes; i++)
		{
			deltaOutput[i] = deltaOutput(targets[i], outputs[i]);
		}

		deltaHidden();
		//System.out.println("Error Values: " + deltaOutput[0] + " "
          //      							+ deltaHidden[0] + " "
            //    							+ deltaHidden[1] + " "
              //  							+ deltaHidden[2]);
	}
	
	public void descendGradient()
	{
		System.out.println("\nDescending Gradient...");
		
		//calculate weight change for input to hidden nodes
		for(int i = 0; i < ihWC.size(); i++)
		{
			for(int j = 0; j < inNodes + 1; j++)
			{
				if(j == 0)
					ihWC.get(i)[j] = ihWC.get(i)[j] * momentum
					                 + lr * deltaHidden[i];
				else 
					ihWC.get(i)[j] = ihWC.get(i)[j] * momentum
	                                 + lr * deltaHidden[i] * inputs[j-1];
			}
		}
		
		//calculate weight change for hidden to output nodes
		for(int i = 0; i < hoWC.size(); i++)
		{
			for(int j = 0; j < hNodes + 1; j++)
			{
				if(j == 0)
					hoWC.get(i)[j] = hoWC.get(i)[j] * momentum
					                 + lr * deltaOutput[i];
				else 
					hoWC.get(i)[j] = hoWC.get(i)[j] * momentum
	                                 + lr * deltaOutput[i] * hlOut[j-1];
			}
		}
		
		
		//apply weight change for input to hidden nodes
		for(int i = 0; i < ihWC.size(); i++)
		{
			for(int j = 0; j < inNodes + 1; j++)
			{
					ihWeights.get(i)[j] = ihWC.get(i)[j] + ihWeights.get(i)[j];
			}
		}
		
		//apply weight change for hidden to output nodes
		for(int i = 0; i < hoWC.size(); i++)
		{
			for(int j = 0; j < hNodes + 1; j++)
			{
					hoWeights.get(i)[j] = hoWC.get(i)[j] + hoWeights.get(i)[j];
			}
		}
	}
	
	public double output(double net)
	{
		return 1.00 / ( 1 + Math.exp(-net));
	}
	
	public double deltaOutput(double target, double output)
	{
		return (target - output) * output * (1 - output);
	}
	
	//written for one output node
	public void deltaHidden()
	{
		for(int i = 0; i < hNodes; i++)
		{
			double sum = 0;
			for(int j = 0; j < outNodes; j++)
			{
				sum += deltaOutput[j] * hoWeights.get(j)[i + 1];
			}
			deltaHidden[i] = hlOut[i] * (1 - hlOut[i]) * sum;
		}
	}
	
	public void setRandomWeights()
	{
		double[] node1 = {-.01,-.03,.03, -.02, .02};
	    double[] node2 = {-.01,-.03,.03, -.02, .01};
	    double[] node3 = {-.01,-.03,.03, -.02, -.04};
		double[] node4 = {-.01,-.03,.03, -.02, .03};
	    double[] node5 = {-.01,-.03,.03, -.02, .03};
	    double[] node6 = {-.01,-.03,.03, -.02, .02};
		double[] node7 = {-.01,-.03,.03, -.02, .02};
		double[] node8 = {-.01,-.03,.03, -.02, .02};
	    double[] node9 = {-.01,-.03,.03, -.02, .02,.02,-.02,-.02,-.02};
	    double[] node10 = {-.01,-.03,.03, -.01, .02,-.01,-.01,.04,.02};
	    double[] node11 = {-.01,-.03,.03, -.02, .02,.03,-.04,.02,.02};
	    ihWeights.add(node1);
	    ihWeights.add(node2);
	    ihWeights.add(node3);
	    ihWeights.add(node4);
	    ihWeights.add(node5);
	    ihWeights.add(node6);
	    ihWeights.add(node7);
	    ihWeights.add(node8);
		hoWeights.add(node9);
		hoWeights.add(node10);
		hoWeights.add(node11);
	    
		//initial changes in weight are all 0
		for(int i = 0; i < hNodes; i++)
		{
			double[] temp = new double[5];
			ihWC.add(temp);
		}
		for(int i = 0; i < outNodes;i++)
		{
			double[] temp = new double[9];
			hoWC.add(temp);
		}
	}
	
	public void setTestWeights()
	{
		double[] node1 = {-.01,-.03,.03};
	    double[] node2 = {.01,.04,-.02};
	    double[] node3 = {-.02,.03,.02};
	    double[] node4 = {.02,-.01,.03,.02};
	    ihWeights.add(node1);
	    ihWeights.add(node2);
	    ihWeights.add(node3);
		hoWeights.add(node4);
	    
		//initial changes in weight are all 0
	    double[] node5 = new double[3];
	    double[] node6 = new double[3];
	    double[] node7 = new double[3];
	    double[] node8 = new double[4];
	    ihWC.add(node5);
	    ihWC.add(node6);
	    ihWC.add(node7);
		hoWC.add(node8);	    
	}
	
	public void printWeights()
	{
		System.out.println("Weights:");
		for(int i = 0; i < ihWeights.size(); i++)
		{
			System.out.print("\t");
			for(int j = 0; j < ihWeights.get(i).length; j++)
			{
				System.out.print(ihWeights.get(i)[j] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
		for(int i = 0; i < hoWeights.size(); i++)
		{
			System.out.print("\t");
			for(int j = 0; j < hoWeights.get(i).length; j++)
			{
				System.out.print(hoWeights.get(i)[j] + " ");
			}
			System.out.println("");
		}
	}

	@Override
	public void predict(double[] features, double[] labels) throws Exception {
		
		 for(int i = 0; i < features.length; i++)
		 {
			 inputs[i] = features[i];
		 }
		 //System.out.println("predict input: " + inputs[0] + " " + inputs[3]);
		 forwardPropogate();
		 
		 if(outputs[0] > .5)
			 labels[0] = 1;
		 else
			 labels[0] = 0;
	}

}
