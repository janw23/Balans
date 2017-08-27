package deepnetwork.math;

public class iVector2 {

	public int x = 0;
	public int y = 0;
	
	public iVector2(int arg0, int arg1)
	{
		x = arg0;
		y = arg1;
	}
	
	public String ToString()
	{
		return "(" + x + "; " + y +")";
	}
	
	public void Set(int arg0, int arg1)
	{
		x = arg0;
		y = arg1;
	}
}
