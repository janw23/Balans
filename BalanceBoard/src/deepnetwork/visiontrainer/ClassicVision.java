package deepnetwork.visiontrainer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import deepnetwork.math.dVector2;

//this is vision algorithm which is meant to find ball's position from the photo but does not use neural networks
public class ClassicVision
{
	public static double threshold = 0.07;	//minimum pixel difference (only brighter) for object to be detected
	
	public static dVector2 GetBallPosition(double[][] in)
	{
		dVector2 ballPos = new dVector2(-999, -999);
		
		double[][] input = in.clone();
		double pixelAverage = 0;	//average pixel value on photo
		
		int x = 0, y = 0;
		for(x = 0;x < input.length; x++)
		{
			for(y = 0; y < input[0].length; y++)
			{
				pixelAverage += input[x][y];
			}
		}
		
		pixelAverage /= input.length * input[0].length;
		
		boolean[][] cmap = new boolean[input.length][input[0].length];	//boolean map of pixels with value difference above pixelAverage greater than threshold
		
		for(x = 0; x < cmap.length; x++)
		{
			for(y = 0; y < cmap[0].length; y++)
			{
				if(input[x][y] - pixelAverage >= threshold)
				{
					input[x][y] = 1;	//temp
					cmap[x][y] = true;
				}
			}
		}
		
		SaveImage(input);
		
		return ballPos;
	}
	
	public static void SaveImage(double[][] data)
	{
		BufferedImage img = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_BYTE_GRAY);
		
		for(int x = 0; x < data.length; x++)
		{
			for(int y = 0; y < data[0].length; y++)
			{
				int col = (int) Math.round(data[x][y] * 255d);
				img.setRGB(x, y, new Color(col, col, col).getRGB());
			}
		}
		
		Random rand = new Random();
		File file = new File("C:\\Users\\jw\\Desktop\\Temp\\" + rand.nextDouble() + ".jpg");
		
		try {
			ImageIO.write(img, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
