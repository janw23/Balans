package deepnetwork.math;

public interface ActivationFunction
{
	public double Compute(double arg);
	
	public double Derivative(double arg);
	
	public double Backwards(double arg);
}
