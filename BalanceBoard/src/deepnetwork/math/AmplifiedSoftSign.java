package deepnetwork.math;

public class AmplifiedSoftSign implements ActivationFunction
{
	private double factor;
	
	public AmplifiedSoftSign(double arg)
	{
		factor = arg;
	}

	public double Compute(double arg)
	{
		return (arg / (Math.abs(arg) + 1)) * factor;
	}

	public double Derivative(double arg)
	{
		return factor / Math.pow(Math.abs(arg) + 1, 2);
	}
	
	public double Backwards(double arg)
	{
		return arg / (factor - Math.abs(arg));
	}
}
