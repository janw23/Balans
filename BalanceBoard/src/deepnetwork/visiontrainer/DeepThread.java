package deepnetwork.visiontrainer;

import java.util.concurrent.Callable;

import deepnetwork.neuralnetwork.DeepNetwork;

public class DeepThread implements Callable<Object>
{
	int thread_number;
	int thread_count;
	DeepNetwork network;
	double[] input;
	public int layer;
	
	public DeepThread(double[] in, DeepNetwork net, int lay, int index, int count)
	{
		input = in;
		network = net;
		thread_number = index;
		layer = lay;
		thread_count = count;
	}
	
	public void SetInput(double[] in)
	{
		input = in;
		layer = 0;
	}
	
	public Object call()
	{
		if(layer == 0)
		{
			for(int n = thread_number; n < network.layers[0].neurons.length; n += thread_count)
			{
				int nStart = n * network.input_per_neuron_size;
				for(int u = 0; u < network.input_per_neuron_size; u++)
					network.layers[0].neurons[n].ReceiveInputAsInputLayer(input[nStart + u], u);
			}
		}
		
		else
		{
			for(int n = thread_number; n < network.layers[layer].neurons.length; n += thread_count)
				for(int k = 0; k < network.layers[layer-1].neurons.length; k++)
					network.layers[layer].neurons[n].ReceiveInput(network.layers[layer-1].neurons[k].output, k);
		}
		
		layer++;
		return null;
	}

}
