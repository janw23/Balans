package deepnetwork.neuralnetwork;

import java.util.Random;

import deepnetwork.math.ActivationFunction;

public class DeepNeuron
{
	public double output = 0;
	
	public double[] weights;
	
	public ActivationFunction activationFunction;
	
	public DeepNeuron(int inputSize, boolean isInput, ActivationFunction af, double randomness)
	{
		if(isInput) weights = new double[inputSize];
		else weights = new double[inputSize + 1];
		
		activationFunction = af;
		
		RandomizeWeights(randomness);
	}
	
	void RandomizeWeights(double randomness)
	{
		Random rand = new Random();
		for(int i = 0; i < weights.length-1; i++)
			weights[i] = 2;//(2d * rand.nextDouble() - 1d) * randomness;
		
		weights[weights.length-1] = 2;//(2d * rand.nextDouble() - 1d) * randomness*10;
	}
	
	public void ReceiveInput(double input, int index)
	{
		output += weights[index] * input;
		
		if(index == weights.length - 2)
		{
			output += weights[index+1];
			output = activationFunction.Compute(output);
		}
	}
	
	public void ReceiveInputAsInputLayer(double input, int index)
	{
		output += weights[index] * input;
		
		if(index == weights.length - 1)
			output = activationFunction.Compute(output);
	}
}
