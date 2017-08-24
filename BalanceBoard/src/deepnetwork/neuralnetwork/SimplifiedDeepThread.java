package deepnetwork.neuralnetwork;

import java.util.concurrent.Callable;

public class SimplifiedDeepThread implements Callable<Object>
{
	int thread_number;
	int thread_count;
	SimplifiedDeepNetwork network;
	public int layer = 0;
	
	public SimplifiedDeepThread(SimplifiedDeepNetwork net, int index, int count)
	{
		network = net;
		thread_number = index;
		thread_count = count;
	}
	
	public Object call() throws Exception
	{
		int nextLayer = layer + 1;
		
		for(int to = thread_number; to < network.neurons[nextLayer].length; to += thread_count)
		{
			network.neurons[nextLayer][to] = 0;
			
			for(int from = 0; from < network.neurons[layer].length; from++)
				network.neurons[nextLayer][to] += network.neurons[layer][from] * network.weights[layer][to][from];
			
			network.neurons[nextLayer][to] += network.biases[layer][to];
			
			network.neurons[nextLayer][to] = network.activationFunctions[layer].Compute(network.neurons[nextLayer][to]);
		}
		
		layer++;
		if(layer == network.weights.length) layer = 0;
		
		return null;
	}
	
	
}
