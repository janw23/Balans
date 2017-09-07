package deepnetwork.visiontrainer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import deepnetwork.math.Mathd;
import deepnetwork.math.dVector2;
import deepnetwork.math.iVector2;

//this is vision algorithm which is meant to find ball's position from the photo but does not use neural networks
public class ClassicVision
{
	public static int threshold = 18;	//minimum pixel difference (only brighter) for object to be detected
	public static Filter filter = null;
	
	public static dVector2 GetBallPosition(int[][] input)
	{
		dVector2 ballPos = new dVector2(-999, -999);
		
		int pixelAverage = 0;	//average pixel value on photo
		
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
		
		//SaveImage(input, cmap);
		
		if(filter == null)
			filter = new Filter(17, 5, 6);	//filter's size (odd number), ball radius, square radius
		
		
		//double[][] smap = new double[input.length][input[0].length];	//double map of each pixel's score in filter classification
		/*
		//move filter over the image
		for(x = filter_center; x < input.length - filter_center - 1; x++)
		{
			for(y = filter_center; y < input[0].length - filter_center - 1; y++)
			{
				int[] dom_normal = new int[256];
				int[] dom_disc = new int[256];
				int[] dom_square = new int[256];
				
				double avg_normal = 0;
				double avg_square = 0;
				double avg_disc = 0;
				iVector2 minmax_normal = new iVector2(1000, -1000);
				
				for(int lx = -filter_center; lx <= filter_center; lx++)
				{
					for(int ly = -filter_center; ly <= filter_center; ly++)
					{
						int gx = x + lx, gy = y + ly, tile_type = filter[lx + filter_center][ly + filter_center];
						
						if(tile_type == 0)
						{
							avg_normal += input[gx][gy];
							if(input[gx][gy] < minmax_normal.x) minmax_normal.x = input[gx][gy];
							if(input[gx][gy] > minmax_normal.y) minmax_normal.y = input[gx][gy];
							dom_normal[input[gx][gy]]++;
						}
						else if(tile_type == 2)
						{
							avg_disc += input[gx][gy];
							dom_disc[input[gx][gy]]++;
						}
						else if(tile_type == 1)
						{
							avg_square += input[gx][gy];
							dom_square[input[gx][gy]]++;
						}
					}
				}
				
				avg_normal /= tile_normal_count;
				avg_square /= tile_square_count;
				avg_disc /= tile_disc_count;
				
				smap[x][y] = GetTileScore(avg_normal, avg_square, avg_disc, minmax_normal, Mathd.getDominantPixel(dom_normal), Mathd.getDominantPixel(dom_square), Mathd.getDominantPixel(dom_disc));
			}
		}
		*/
		
		//temporary but working
		double score_max = -100000;
		iVector2 score_max_pixel = new iVector2(0, 0);
		
		for(x = filter.filter_center; x < input.length - filter.filter_center - 1; x++)
		{
			for(y = filter.filter_center; y < input[0].length - filter.filter_center - 1; y++)
			{
				double count_normal = 0;
				double count_square = 0;
				double count_disc = 0;
				
				for(int lx = -filter.filter_center; lx <= filter.filter_center; lx++)
				{
					for(int ly = -filter.filter_center; ly <= filter.filter_center; ly++)
					{
						int tile_type = filter.tiles[lx + filter.filter_center][ly + filter.filter_center];
						
						if(cmap[x + lx][y + ly])
						{
							if(tile_type == 0) count_normal++;
							else if(tile_type == 2) count_disc++;
							else if(tile_type == 1) count_square++;
						}
					}
				}
				
				double pixel = count_disc/filter.tile_disc_count - count_square/filter.tile_square_count - count_normal/filter.tile_normal_count;
				if(pixel > score_max)
				{
					score_max = pixel;
					score_max_pixel.Set(x, y);
				}
			}
		}
		
		
		//SaveImage(input, smap);
		
		ballPos.Set( 2*(score_max_pixel.x - input.length*0.5)/input.length, 2*(score_max_pixel.y - input[0].length*0.5)/input[0].length );
		
		return ballPos;
	}
	
	/*public static double GetTileScore(double avg_normal, double avg_square, double avg_disc, iVector2 minmax_normal, int dom_normal, int dom_square, int dom_disc)	//pixel's score count rules
	{
		//System.out.println("Min = " + minmax_normal.x + " Max = " + minmax_normal.y);
		return Math.abs(avg_normal - avg_disc);//Math.abs(minmax_normal.x - minmax_normal.y)/10;
	}*/
	
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
	
	public static void SaveImage(int[][] data, double[][] smap)
	{
		BufferedImage img = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < data.length; x++)
		{
			for(int y = 0; y < data[0].length; y++)
			{
				int col = data[x][y];
				int red = (int)(100 * smap[x][y]) + col;
				
				if(red > 255) red = 255;
				else if(red < 0) red = 0;
				
				Color color = new Color(red, col, col);
				img.setRGB(x, y, color.getRGB());
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

class Filter
{
	public int filter_size = 0;
	public int ball_radius = 0;
	public int square_side = 0;
	
	public int filter_center = 0;
	
	public int tile_square_count = 0;
	public int tile_normal_count = 0;
	public int tile_disc_count = 0;
	
	int[][] tiles;	//0 - normal tile, 1 - square's tile, 2 - disc's tile
	
	public Filter(int f_size, int b_radius, int s_side)
	{
		if(f_size % 2 == 0) f_size++;
		
		filter_size = f_size;
		ball_radius = b_radius;
		square_side = s_side;
		
		tile_square_count = (2*square_side+1) * (2*square_side+1);
		tile_normal_count = filter_size * filter_size - tile_square_count;
		
		tiles = new int[filter_size][filter_size];	
		filter_center = (filter_size - 1)/2;
		
		//prepare filter
		//generate square
		int x = 0, y = 0;
		
		for(x = -square_side; x <= square_side; x++)
			for(y = -square_side; y <= square_side; y++)
				tiles[filter_center + x][filter_center + y] = 1;
		
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
				tiles[i][filter_center + y] = 2;
				tiles[i][filter_center - y] = 2;
			}
			for(int i = filter_center - y; i <= filter_center + y; i++)
			{
				tiles[i][filter_center + x] = 2;
				tiles[i][filter_center - x] = 2;
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
				if(tiles[x][y] == 2) tile_disc_count++;
		
		tile_square_count -= tile_disc_count;
	}
}
