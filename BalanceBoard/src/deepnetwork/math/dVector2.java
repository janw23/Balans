package deepnetwork.math;

public class dVector2 {

	public double x = 0;
	public double y = 0;
	
	public dVector2(double arg0, double arg1)
	{
		x = arg0;
		y = arg1;
	}
	
	public String ToString()
	{
		return "(" + x + "; " + y +")";
	}
	
	public void Set(double arg0, double arg1)
	{
		x = arg0;
		y = arg1;
	}
}
