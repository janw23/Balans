package deepnetwork.math;

public class Tanh implements ActivationFunction
{
	public double Compute(double arg)
	{
		return Math.tanh(arg);
	}

	public double Derivative(double arg)
	{
		return 1d - Math.pow(Compute(arg), 2);
	}

	public double Backwards(double arg) {
		// TODO Auto-generated method stub
		return 0;
	}
}
