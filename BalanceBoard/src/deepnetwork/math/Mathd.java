package deepnetwork.math;

public class Mathd	//class with custom maths functions for my program's purposes
{
	public static int getDominantPixel(int[] ar)	//returns dominant from array of pixel's values (size = 256)
	{
		int max = ar[0];
		int index = 0;
		
		for(int i = 1; i < 256; i++)
		{
			if(ar[i] > max)
			{
				max = ar[i];
				index = i;
			}
		}
		
		return index;
	}
}
