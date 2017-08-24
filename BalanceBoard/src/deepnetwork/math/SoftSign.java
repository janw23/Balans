package deepnetwork.math;

public class SoftSign implements ActivationFunction
{
	public double Compute(double arg)
	{
		return arg / (Math.abs(arg) + 1d);
	}

	public double Derivative(double arg)
	{
		return 1d / Math.pow(1 + Math.abs(arg), 2);
	}
	
	public double Backwards(double arg)
	{
		return arg / (1 - Math.abs(arg));
	}
}
