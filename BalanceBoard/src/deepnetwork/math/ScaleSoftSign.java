package deepnetwork.math;

public class ScaleSoftSign implements ActivationFunction
{
	double scale;
	
	public ScaleSoftSign(double s)
	{
		scale = s;
	}
	
	public double Compute(double arg)
	{
		return scale*arg / (Math.abs(scale*arg) + 1d);
	}

	public double Derivative(double arg)
	{
		return 0;
	}

	public double Backwards(double arg) {
		// TODO Auto-generated method stub
		return 0;
	}
}
