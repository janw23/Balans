package deepnetwork.neuralnetwork;

import deepnetwork.math.ActivationFunction;

public class DeepNetwork
{
	public DeepLayer[] layers;
	
	public double[] output;
	
	public int input_per_neuron_size = 0;
	
	public DeepNetwork(int inputPerNeuronSize, int outputSize, int[] config, ActivationFunction[] afConfig, double randomness)
	{
		input_per_neuron_size = inputPerNeuronSize;
		
		output = new double[outputSize];
		
		layers = new DeepLayer[config.length];
		layers[0] = new DeepLayer(config[0], inputPerNeuronSize, true, afConfig[0], randomness);
		
		for(int i = 1; i < layers.length; i++)
		{
			layers[i] = new DeepLayer(config[i], layers[i-1].neurons.length, false, afConfig[i], randomness);
		}
	}
	
	public void Iterate(double[] input)
	{
		for(int i = 0; i < layers[0].neurons.length; i++)
		{
			layers[0].neurons[i].output = input[i];
			/*int nStart = i * input_per_neuron_size;
			for(int u = 0; u < input_per_neuron_size; u++)
			{
				layers[0].neurons[i].ReceiveInputAsInputLayer(input[nStart + u], u);
				//System.out.println("i = " + i + "; u = " + u + "; inputarg = " + (i * input_per_neuron_size + u));
			}*/
		}
		
		
		for(int l = 1; l < layers.length; l++)
		{
			for(int i = 0; i < layers[l].neurons.length; i++)
			{
				for(int u = 0; u < layers[l-1].neurons.length; u++)
					layers[l].neurons[i].ReceiveInput(layers[l-1].neurons[u].output, u);
			}
			layers[l-1].ResetOutputs();
		}
		
		int neurons_per_output = layers[layers.length-1].neurons.length/output.length;
		
		for(int i = 0; i < output.length; i++)
		{
			output[i] = 0;
			int nStart = i*neurons_per_output;
			
			for(int u = 0; u < neurons_per_output; u++)
				output[i] += layers[layers.length-1].neurons[nStart + u].output;
			output[i] = layers[layers.length-1].activationFunction.Compute(output[i]);
		}
		
		layers[layers.length-1].ResetOutputs();
	}
}
