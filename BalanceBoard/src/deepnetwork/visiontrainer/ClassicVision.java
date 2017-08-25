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
	public static int threshold = 18;	//minimum pixel difference (only brighter) for object to be detected
	
	public static dVector2 GetBallPosition(int[][] input)
	{
		dVector2 ballPos = new dVector2(-999, -999);
		
		/*int pixelAverage = 0;	//average pixel value on photo
		
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
					cmap[x][y] = true;
			}
		}
		
		SaveImage(input, cmap);
		*/
		
		int filter_size = 21;	//MUST BE AN ODD NUMBER! size of a filter moving over the photo
		int ball_radius = 5;	//radius of a ball
		
		int[][] filter = new int[filter_size][filter_size];	//0 - normal tile, 1 - square's tile, 2 - disc's tile
		int filter_center = (filter_size - 1)/2;
		
		
		int tile_square_count = (2*ball_radius+1) * (2*ball_radius+1);
		int tile_normal_count = filter_size * filter_size - tile_square_count;
		int tile_disc_count = 0;
		
		//prepare filter
		int x = 0, y = 0;
		//generate square
		for(x = -ball_radius; x <= ball_radius; x++)
			for(y = -ball_radius; y <= ball_radius; y++)
				filter[filter_center + x][filter_center + y] = 1;
		
		//generate disc
		x = ball_radius;
		y = 0;
		int xChange = 1 - (ball_radius << 1);
		int yChange = 0;
		int error = 0;
		
		while(x >= y)
		{
			for(int i = filter_center - x; i <= filter_center + x; i++)
			{
				filter[i][filter_center + y] = 2;
				filter[i][filter_center - y] = 2;
			}
			for(int i = filter_center - y; i <= filter_center + y; i++)
			{
				filter[i][filter_center + x] = 2;
				filter[i][filter_center - x] = 2;
			}
			
			y++;
			error += yChange;
			yChange += 2;
			if(((error << 1) + xChange) > 0)
			{
				x--;
				error += xChange;
				xChange += 2;
			}
		}
		
		for(x = 0; x < filter_size; x++)
			for(y = 0; y < filter_size; y++)
				if(filter[x][y] == 2) tile_disc_count++;
		
		tile_square_count -= tile_disc_count;
		
		//move filter on image
		x = filter_center;
		y = filter_center;
		
		while(x < input.length - filter_center - 1)
		{
			while(y < input[0].length - filter_center - 1)
			{
				double avg_normal = 0;
				double avg_square = 0;
				double avg_disc = 0;
				y++;
			}
			y = filter_center;
			x++;
		}
		
		
		return ballPos;
	}
	
	public static void SaveImage(int[][] data, boolean[][] cmap)
	{
		BufferedImage img = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < data.length; x++)
		{
			for(int y = 0; y < data[0].length; y++)
			{
				int col = data[x][y];
				
				Color color;
				if(cmap[x][y]) color = Color.red;
				else color = new Color(col, col, col);
				
				img.setRGB(x, y, color.getRGB());
			}
		}
		
		Random rand = new Random();
		File file = new File("C:\\Users\\jw\\Desktop\\Temp\\" + threshold + ".jpg");
		
		try {
			ImageIO.write(img, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void SaveImage(int[][] data)
	{
		BufferedImage img = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < data.length; x++)
		{
			for(int y = 0; y < data[0].length; y++)
			{
				int col = data[x][y]*127;
				img.setRGB(x, y, new Color(col, col, col).getRGB());
			}
		}
		
		Random rand = new Random();
		File file = new File("C:\\Users\\jw\\Desktop\\Temp\\" + threshold + ".jpg");
		
		try {
			ImageIO.write(img, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
