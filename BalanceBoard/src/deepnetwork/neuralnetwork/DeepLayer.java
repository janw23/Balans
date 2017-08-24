package deepnetwork.neuralnetwork;

import deepnetwork.math.ActivationFunction;

public class DeepLayer
{
	public DeepNeuron[] neurons;
	
	public ActivationFunction activationFunction;
	
	public DeepLayer(int neuronsCount, int inputSize, boolean isInput, ActivationFunction af, double randomness)
	{
		activationFunction = af;
		
		neurons = new DeepNeuron[neuronsCount];
		for(int i = 0; i < neuronsCount; i++)
			neurons[i] = new DeepNeuron(inputSize, isInput, activationFunction, randomness);
	}
	
	public void ReceiveInput(double input, int index)
	{
		for(int i = 0; i < neurons.length; i++)
			neurons[i].ReceiveInput(input, index);
	}
	
	public void ResetOutputs()
	{
		for(int i = 0; i < neurons.length; i++)
			neurons[i].output = 0;
	}
	
}
