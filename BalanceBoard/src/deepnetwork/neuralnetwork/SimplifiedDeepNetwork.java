package deepnetwork.neuralnetwork;

import java.util.Random;

import deepnetwork.math.ActivationFunction;

public class SimplifiedDeepNetwork
{
	public double[][] neurons;	//output of neuron [layer][neuronIndex]
	public double[][][] weights;	//weight between neurons [betweenLayers][neuronTo][neuronFrom]
	public double[][] biases; //bias of neuron [layer][neuron]
	
	public ActivationFunction[] activationFunctions;
	
	
	public double[] output;
	
	public SimplifiedDeepNetwork(int[] layersConfig, ActivationFunction[] afsConfig, int outputSize, double randomness)
	{
		neurons = new double[layersConfig.length][];
		for(int i = 0; i < neurons.length; i++)
			neurons[i] = new double[layersConfig[i]];
		
		Random rand = new Random();
		
		weights = new double[neurons.length-1][][];
		for(int w = 0; w < weights.length; w++)
		{
			weights[w] = new double[neurons[w+1].length][neurons[w].length];
			for(int x = 0; x < weights[w].length; x++)
				for(int y = 0; y < weights[w][x].length; y++)
					weights[w][x][y] = randomness * (rand.nextDouble() * 2 - 1);
		}
		
		biases = new double[neurons.length-1][];
		for(int b = 0; b < biases.length; b++)
		{
			biases[b] = new double[neurons[b+1].length];
			for(int x = 0; x < biases[b].length; x++)
				biases[b][x] = randomness * (rand.nextDouble() * 2 - 1);
		}
		
		activationFunctions = afsConfig;
		
		output = new double[outputSize];
	}
	
	public void Iterate(double[] input)
	{
		neurons[0] = input;
		
		for(int layer = 0; layer < weights.length; layer++)
		{
			int nextLayer = layer + 1;
			
			for(int to = 0; to < neurons[nextLayer].length; to++)
			{
				neurons[nextLayer][to] = 0;
				
				for(int from = 0; from < neurons[layer].length; from++)
					neurons[nextLayer][to] += neurons[layer][from] * weights[layer][to][from];
				
				neurons[nextLayer][to] += biases[layer][to];
				
				neurons[nextLayer][to] = activationFunctions[layer].Compute(neurons[nextLayer][to]);
			}
		}

		for(int n = 0; n < neurons[neurons.length-1].length; n++)
			output[n] = neurons[neurons.length-1][n];
	}
}
