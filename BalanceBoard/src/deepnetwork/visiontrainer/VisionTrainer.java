package deepnetwork.visiontrainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import deepnetwork.math.ActivationFunction;
import deepnetwork.math.AmplifiedSoftSign;
import deepnetwork.math.Identity;
import deepnetwork.math.MinMax;
import deepnetwork.math.ScaleSoftSign;
import deepnetwork.math.Tanh;
import deepnetwork.math.SoftSign;
import deepnetwork.math.dVector2;
import deepnetwork.neuralnetwork.DeepNetwork;
import deepnetwork.neuralnetwork.SimplifiedDeepNetwork;
import deepnetwork.neuralnetwork.SimplifiedDeepThread;

public class VisionTrainer {

	public static VTWindow app_window;
	
	//public static DeepNetwork deepNetwork_vision;
	
	public static SimplifiedDeepNetwork simplifiedDeepNetwork;
	
	public static double training_stepSizeMax = 0.0004d;
	
	public static int neurons_in_input_layer = 5625;
	
	public static ExecutorService exec;
	public static ArrayList<Callable<Object>> callables;
	//public static ArrayList<DeepThread> deepThreads;
	public static ArrayList<SimplifiedDeepThread> simplifiedDeepThreads;
	
	public static void main(String[] args)
	{
		//int[] conf = {neurons_in_input_layer, 100, 50, 30};
		
		SoftSign sg = new SoftSign();
		
		//ActivationFunction[] afConf = {sg, sg, sg, new AmplifiedSoftSign(1.3)};
		
		//deepNetwork_vision = new DeepNetwork((int)Math.pow(150/Math.sqrt(neurons_in_input_layer), 2), 2, conf, afConf, 0.4d);
		
		int[] conf2 = {5625, 50, 40, 30, 2};
		ActivationFunction[] afConf2 = {sg, sg, sg, new AmplifiedSoftSign(1.3)};
		
		simplifiedDeepNetwork = new SimplifiedDeepNetwork(conf2, afConf2, 2, 0.4d);
		
		
		app_window = new VTWindow("VisionTrainer");
		
		double[] temp = new double[5625];

		for(int i = 0; i < temp.length; i++)
			temp[i] = 0.2d;
		
		long time = 0;
		for(int i = 0; i < 1000; i++)
		{
			long start = System.nanoTime();
			
			simplifiedDeepNetwork.Iterate(temp);
			
			time += System.nanoTime() - start;
		}
		time /= 1000;
		
		System.out.println("Simplified single Core took average " + time + "    out = " + new dVector2(simplifiedDeepNetwork.output[0], simplifiedDeepNetwork.output[1]).ToString());
		
		
		PrepareSimplifiedMulticore();
		
		for(int i = 0; i < 500; i++)
		{
			SimplifiedMulticoreIterate(temp);
		}
		
		time = 0;
		for(int i = 0; i < 1000; i++)
		{
			long start = System.nanoTime();
			
			SimplifiedMulticoreIterate(temp);
			
			time += System.nanoTime() - start;
		}
		
		time /= 1000;
		
		System.out.println("Multicore took average " + time + " nanoseconds" + "    out = " + new dVector2(simplifiedDeepNetwork.output[0], simplifiedDeepNetwork.output[1]).ToString());
	}
	
	public static void PrepareSimplifiedMulticore()
	{
		int threadsCount = 4;//Runtime.getRuntime().availableProcessors();
		
		exec = Executors.newFixedThreadPool(threadsCount);
		callables = new ArrayList<Callable<Object>>();
		simplifiedDeepThreads = new ArrayList<SimplifiedDeepThread>();
		
		for(int i = 0; i < threadsCount; i++)
		{
			simplifiedDeepThreads.add(new SimplifiedDeepThread(simplifiedDeepNetwork, i, threadsCount));
			callables.add(simplifiedDeepThreads.get(i));
		}
	}
	
	public static void SimplifiedMulticoreIterate(double[] input)
	{
		simplifiedDeepNetwork.neurons[0] = input;
		
		for(int l = 0; l < simplifiedDeepNetwork.weights.length; l++)
		{
			try {
				exec.invokeAll(callables);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		for(int n = 0; n < simplifiedDeepNetwork.neurons[simplifiedDeepNetwork.neurons.length-1].length; n++)
			simplifiedDeepNetwork.output[n] = simplifiedDeepNetwork.neurons[simplifiedDeepNetwork.neurons.length-1][n];
	}
	
	/*public static void PrepareMultiCore()
	{
		int threadsCount = 4;//Runtime.getRuntime().availableProcessors();
		
		exec = Executors.newFixedThreadPool(threadsCount);
		callables = new ArrayList<Callable<Object>>();
		deepThreads = new ArrayList<DeepThread>();
		
		for(int i = 0; i < threadsCount; i++)
		{
			deepThreads.add(new DeepThread(null, deepNetwork_vision, 0, i, threadsCount));
			callables.add(deepThreads.get(i));
		}
	}
	
	//@SuppressWarnings("unchecked")
	public static void MultiCoreIterate(double[] input)
	{
		for(int i = 0; i < callables.size(); i++)
			deepThreads.get(i).SetInput(input);
		
		try {
			exec.invokeAll(callables);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	
		for(int l = 1; l < deepNetwork_vision.layers.length; l++)
		{
			try {
				exec.invokeAll(callables);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
			deepNetwork_vision.layers[l-1].ResetOutputs();
		}
		
		int neurons_per_output = deepNetwork_vision.layers[deepNetwork_vision.layers.length-1].neurons.length/deepNetwork_vision.output.length;
		
		for(int i = 0; i < deepNetwork_vision.output.length; i++)
		{
			deepNetwork_vision.output[i] = 0;
			
			int nStart = i * neurons_per_output;
			for(int u = 0; u < neurons_per_output; u++)
				deepNetwork_vision.output[i] += deepNetwork_vision.layers[deepNetwork_vision.layers.length-1].neurons[nStart + u].output;
			
			deepNetwork_vision.output[i] = deepNetwork_vision.layers[deepNetwork_vision.layers.length-1].activationFunction.Compute(deepNetwork_vision.output[i]);
		}
		
		deepNetwork_vision.layers[deepNetwork_vision.layers.length-1].ResetOutputs();
	}*/
	
	public static double GetMeanSquaredError(dVector2 real, dVector2 target)
	{
		return ( Math.pow(real.x - target.x, 2) + Math.pow(real.y - target.y, 2) ) / 2d;//( Math.pow(real.x-target.x, 2) + Math.pow(real.y-target.y, 2) ) / 2d;
	}
	
	public static void SimplifiedTrainNetwork(double[] input, dVector2 targetOutput, double achievedError)
	{
		Random rand = new Random();
		double networkError = achievedError;
		
		for(int l = 0; l < simplifiedDeepNetwork.weights.length; l++)
		{
			for(int x = 0; x < simplifiedDeepNetwork.weights[l].length; x++)
			{
					double oldWeights[] = simplifiedDeepNetwork.weights[l][x].clone();
					
					for(int y = 0; y < simplifiedDeepNetwork.weights[l][x].length; y++)
					simplifiedDeepNetwork.weights[l][x][y] += (rand.nextDouble()*2 -1) * training_stepSizeMax;
					
					SimplifiedMulticoreIterate(input);
					
					dVector2 result = new dVector2(VisionTrainer.simplifiedDeepNetwork.output[0], VisionTrainer.simplifiedDeepNetwork.output[1]);
					double newError = GetMeanSquaredError(result, targetOutput);
					
					if(newError >= networkError) simplifiedDeepNetwork.weights[l][x] = oldWeights;
					else networkError = newError;
			}
		}
	}
	
	/*public static void TrainNetwork(double[] input, dVector2 targetOutput, double achievedError)
	{
		//System.out.println("Training started");
		//int n = 0, o = 0;
		
		double networkError = achievedError;
		Random rand = new Random();
		
		for(int l = 0; l < deepNetwork_vision.layers.length; l++)
		{
			for(int i = 0; i < deepNetwork_vision.layers[l].neurons.length; i++)
			{
				for(int k = 0; k < 1; k++)
				{
					double[] oldWeights = deepNetwork_vision.layers[l].neurons[i].weights.clone();
					
					for(int w = 0; w < deepNetwork_vision.layers[l].neurons[i].weights.length; w++)
					{
						deepNetwork_vision.layers[l].neurons[i].weights[w] += (rand.nextDouble()*2d-1d) * training_stepSizeMax;
					}
					
					MultiCoreIterate(input);
					//deepNetwork_vision.Iterate(input);
					
					dVector2 result = new dVector2(VisionTrainer.deepNetwork_vision.output[0], VisionTrainer.deepNetwork_vision.output[1]);
					double newError = GetMeanSquaredError(result, targetOutput);
					
					if(newError >= networkError)
					{
						//o++;
						deepNetwork_vision.layers[l].neurons[i].weights = oldWeights;
					}
					else
					{
						//n++;
						networkError = newError;
					}
				}
			}
		}
		
		//System.out.println("Training done, new " + n + "; old " + o);
	}*/
	
}
