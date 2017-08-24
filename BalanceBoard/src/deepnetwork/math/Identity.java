package deepnetwork.math;

public class Identity implements ActivationFunction
{

	public double Compute(double arg)
	{
		return arg;
	}

	public double Derivative(double arg)
	{
		return 1;
	}

	public double Backwards(double arg) {
		// TODO Auto-generated method stub
		return 0;
	}

}
