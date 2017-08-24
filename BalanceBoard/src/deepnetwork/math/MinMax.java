package deepnetwork.math;

public class MinMax implements ActivationFunction
{
	public dVector2 range;
	
	public MinMax(double min, double max)
	{
		range = new dVector2(min, max);
	}

	public double Compute(double arg)
	{
		if(arg < range.x) arg = range.x;
		else if(arg > range.y) arg = range.y;
		
		return arg;
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
