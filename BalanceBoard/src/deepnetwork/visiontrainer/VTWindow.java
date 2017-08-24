package deepnetwork.visiontrainer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import deepnetwork.math.dVector2;

public class VTWindow extends JFrame implements ActionListener, ItemListener, KeyListener, MouseListener{
	private static final long serialVersionUID = -5090017517481350937L;

	public static VTPanel panel;
	
	public static JFileChooser fileChooser = new JFileChooser();
	
	public static Rectangle trainData_photo_preview_rect = new Rectangle(50, 50, 300, 300);
	public static Rectangle trainData_board_preview_rect = new Rectangle(500, 50, 300, 300);
	
	public static Point trainData_path_preview_position = new Point(850, 60);
	public static Point checkData_path_preview_position = new Point(850, 90);
	public static String trainData_path_preview_text = "Train Data path: ";
	public static String checkData_path_preview_text = "Check Data path: ";
	
	public static String trainData_path = "";
	public static String checkData_path = "";
	
	public static ArrayList<File> trainData_loadedImages = new ArrayList<File>();
	public static ArrayList<File> checkData_loadedImages = new ArrayList<File>();
	
	public static BufferedImage image_preview;
	public static int image_preview_index = 0;
	public static int image_preview_list = 0;
	
	public static dVector2 image_loadedPosition = new dVector2(-999, -999);
	public static Point image_loadedPosition_textPosition = new Point(500, 370);
	
	public static dVector2 network_resultPosition = new dVector2(-999, -999);
	public static Point network_resultPosition_textPosition = new Point(500, 390);
	
	public static double[] network_input;
	
	public static double network_distance_error = 0;
	public static Point network_distance_error_textPosition = new Point(500, 405);
	
	public static int board_preview_realPosition_square_size = 3;
	
	public static Rectangle image_loadImagesButton_rect = new Rectangle(850, 105, 120, 30);
	public static Rectangle image_nextImageButton_rect = new Rectangle(940, 140, 85, 30);
	public static Rectangle image_previousImageButton_rect = new Rectangle(850, 140, 85, 30);
	
	public static Rectangle network_getResultButton_rect = new Rectangle(50, 360, 145, 30);
	
	public static Rectangle network_trainButton_rect = new Rectangle(50, 395, 120, 30);
	
	public static Rectangle network_checkButton_rect = new Rectangle(50, 430, 120, 30);
	
	public VTWindow(String name)
	{
		setTitle(name);
		setPreferredSize(new Dimension(1280, 720));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setAlwaysOnTop(false);
		setVisible(true);
		
		panel = new VTPanel();
		panel.setLayout(null);
		setContentPane(panel);
		
		PaintForArgument(VTPanel.PAINTARG_UI_INIT);
		
		InitMenu();
		InitButtons();
		
		pack();
		
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
	
	void PaintForArgument(int arg)
	{
		panel.paintArg = arg;
		repaint();
	}
	
	void InitMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		
		//data menu start
		JMenu dataMenu = new JMenu("Data");
		
		JMenu dataMenu_setSource = new JMenu("Set Source");
		JMenuItem dataMenu_setSource_trainData = new JMenuItem("Train Data");
		JMenuItem dataMenu_setSource_checkData = new JMenuItem("Check Data");
		
		dataMenu_setSource_trainData.addActionListener(this);
		dataMenu_setSource_checkData.addActionListener(this);
		
		dataMenu_setSource_trainData.setActionCommand("SET_TRAINDATA_PATH");
		dataMenu_setSource_checkData.setActionCommand("SET_CHECKDATA_PATH");
		
		dataMenu_setSource.add(dataMenu_setSource_trainData);
		dataMenu_setSource.add(dataMenu_setSource_checkData);
		
		dataMenu.add(dataMenu_setSource);
		
		menuBar.add(dataMenu);
		//data menu end
		
		//network menu start
		JMenu networkMenu = new JMenu("Network");
		
		JMenuItem networkMenu_new = new JMenuItem("New");
		JMenuItem networkMenu_save = new JMenuItem("Save");
		JMenuItem networkMenu_load = new JMenuItem("Load");
		
		networkMenu_new.addActionListener(this);
		networkMenu_save.addActionListener(this);
		networkMenu_load.addActionListener(this);
		
		networkMenu.add(networkMenu_new);
		networkMenu.add(networkMenu_save);
		networkMenu.add(networkMenu_load);
		
		menuBar.add(networkMenu);
		//network menu end
		
		setJMenuBar(menuBar);
	}

	void InitButtons()
	{
		//load images button start
		JButton images_load = new JButton("Load Images");
		images_load.setBounds(image_loadImagesButton_rect);
		images_load.addActionListener(this);
		images_load.setActionCommand("LOAD_IMAGES");
		add(images_load);
		//load images button end
		
		//next image button start
		JButton image_next = new JButton("Next");
		image_next.setBounds(image_nextImageButton_rect);
		image_next.addActionListener(this);
		image_next.setActionCommand("NEXT_IMAGE");
		add(image_next);
		//next image button end
		
		//previous image button start
		JButton image_previous = new JButton("Previous");
		image_previous.setBounds(image_previousImageButton_rect);
		image_previous.addActionListener(this);
		image_previous.setActionCommand("PREVIOUS_IMAGE");
		add(image_previous);
		//previous image button end
		
		//network get result button start
		JButton network_get_result = new JButton("Get CVision Result");//new JButton("Get Network Result");	//networks will be implemented in fiture
		network_get_result.setBounds(network_getResultButton_rect);
		network_get_result.addActionListener(this);
		network_get_result.setActionCommand("GET_CLASSIC_VISION_RESULT");//("GET_NETWORK_RESULT");
		add(network_get_result);
		//network get result button end
		
		//network train button start
		JButton network_train = new JButton("Train network");
		network_train.setBounds(network_trainButton_rect);
		network_train.addActionListener(this);
		network_train.setActionCommand("TRAIN_NETWORK");
		add(network_train);
		//network train button end
		
		//network check button start
		JButton network_check = new JButton("Check network");
		network_check.setBounds(network_checkButton_rect);
		network_check.addActionListener(this);
		network_check.setActionCommand("CHECK_NETWORK");
		add(network_check);
		//network check button end
		
	}
	
	void LoadImages()
	{
		if(!trainData_path.equals(""))
		{
			File folder = new File(trainData_path);
			File[] listedFiles = folder.listFiles();
			
			for(int i = 0; i < listedFiles.length; i++)
			{
				String name = listedFiles[i].getName();
				if(name.substring(name.length()-4).equals(".png"))
					trainData_loadedImages.add(listedFiles[i]);
			}
			
			Collections.shuffle(trainData_loadedImages);
		}
		
		if(!checkData_path.equals(""))
		{
			File folder = new File(checkData_path);
			File[] listedFiles = folder.listFiles();
			
			for(int i = 0; i < listedFiles.length; i++)
			{
				String name = listedFiles[i].getName();
				if(name.substring(name.length()-4).equals(".png"))
					checkData_loadedImages.add(listedFiles[i]);
			}
			
			Collections.shuffle(checkData_loadedImages);
		}
	}
	
	BufferedImage ResizeImage(BufferedImage img, int x, int y)
	{
		Image tmp = img.getScaledInstance(x, y, Image.SCALE_FAST);
	    BufferedImage dimg = new BufferedImage(x, y, BufferedImage.TYPE_BYTE_GRAY);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
	
	boolean SetPreviewImage(int listIndex, int imageIndex)
	{
		if(listIndex == 0)
		{
			if(imageIndex < trainData_loadedImages.size())
			{
				try
				{
					image_preview = ResizeImage(ImageIO.read(trainData_loadedImages.get(imageIndex)), 75, 75);
					image_preview = ConvertToGreyScale(image_preview);
					
					String fileName = trainData_loadedImages.get(imageIndex).getName();
					
					Scanner sc = new Scanner(fileName);
					sc.useLocale(Locale.US);
					
					while(sc.hasNext())
					{
						if(sc.hasNextDouble())
						{
							image_loadedPosition.x = sc.nextDouble();
							image_loadedPosition.y = sc.nextDouble();
						}
						else sc.next();
					}
					
					sc.close();
					image_preview_index = imageIndex;
					image_preview_list = listIndex;
					return true;
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				return false;
			}
			return false;
		}
		
		else if(listIndex == 1)
		{
			if(imageIndex < checkData_loadedImages.size())
			{
				try
				{
					image_preview = ResizeImage(ImageIO.read(checkData_loadedImages.get(imageIndex)), 75, 75);
					image_preview = ConvertToGreyScale(image_preview);
					
					String fileName = checkData_loadedImages.get(imageIndex).getName();
					
					Scanner sc = new Scanner(fileName);
					sc.useLocale(Locale.US);
					
					while(sc.hasNext())
					{
						if(sc.hasNextDouble())
						{
							image_loadedPosition.x = sc.nextDouble();
							image_loadedPosition.y = sc.nextDouble();
						}
						else sc.next();
					}
					
					sc.close();
					image_preview_index = imageIndex;
					image_preview_list = listIndex;
					return true;
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				return false;
			}
			return false;
		}
		return false;
	}
	
	BufferedImage ConvertToGreyScale(BufferedImage img)
	{
		BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		Graphics g = newImage.getGraphics();
		
		g.drawImage(img, 0, 0, null);
		g.dispose();
		
		return newImage;
	}
	
	double[][] ConvertToClassicInput(BufferedImage img)	//input created for non-neuralnetwork vision
	{
		double[][] input = new double[img.getWidth()][img.getHeight()];
		
		int x = 0, y = 0;
		while(x < input.length)
		{
			while(y < input[0].length)
			{
				int rgb = img.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);
				
				input[x][y] = (r + g + b) / 765d;
				
				y++;
			}
			y = 0;
			x++;
		}
		
		return input;
	}
	
	double[] ConvertToNetworkInput(BufferedImage img, int neuronsCount)
	{
		double[] input = new double[img.getWidth() * img.getHeight()];
		
		int i = 0;
		for(int w = 0; w < img.getWidth(); w++)
			for(int h = 0; h < img.getHeight(); h++)
			{
				int rgb = img.getRGB(w, h);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);
				
				input[i++] = (r + g + b) / 765d;
			}
		
		return input;
		
		/*int nSize = (int) Math.sqrt(input.length / neuronsCount);
		
		int cellSize = (int) Math.sqrt(neuronsCount);
		
		int globalX = 0, globalY = 0, i = 0;
		while(true)
		{
			int localX = 0, localY = 0;
			while(true)
			{
				int rgb = img.getRGB(nSize * globalX + localX, nSize * globalY + localY);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);
				
				input[i++] = (r + g + b) / 765d;
				localX++;
				if(localX == nSize)
				{
					localX = 0;
					localY++;
					if(localY == nSize) break;
				}
			}
			
			globalX++;
			if(globalX == cellSize)
			{
				globalX = 0;
				globalY++;
				if(globalY == cellSize) return input;
			}
			
		}*/
	}
	
	BufferedImage ReconstructImage(double[] input, int neuronsCount)
	{
		BufferedImage img = new BufferedImage(150, 150, BufferedImage.TYPE_BYTE_GRAY);
		
		int nSize = (int) Math.sqrt(input.length/neuronsCount);
		int cellSize = (int) Math.sqrt(neuronsCount);
		
		int globalX = 0, globalY = 0, i = 0;
		while(true)
		{
			int localX = 0, localY = 0;
			while(true)
			{
				int color = (int)(input[i++]*255);
				int rgb = color;
				rgb = (rgb << 8) + color;
				rgb = (rgb << 8) + color;
				
				img.setRGB(nSize * globalX + localX, nSize * globalY + localY, rgb);
				localX++;
				if(localX == nSize)
				{
					localX = 0;
					localY++;
					if(localY == nSize) break;
				}
			}
			
			globalX++;
			if(globalX == cellSize)
			{
				globalX = 0;
				globalY++;
				if(globalY == cellSize) break;
			}
		}
		
		return img;
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		
	}

	public void actionPerformed(ActionEvent e)
	{
		String actionName = e.getActionCommand();
		////////
		if(actionName.equals("SET_TRAINDATA_PATH"))
		{
			int returnVal = fileChooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				trainData_path = fileChooser.getSelectedFile().getAbsolutePath();
				PaintForArgument(VTPanel.PAINTARG_TRAINDATA_PATH);
			}
		}
		////////
		else if(actionName.equals("SET_CHECKDATA_PATH"))
		{
			int returnVal = fileChooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				checkData_path = fileChooser.getSelectedFile().getAbsolutePath();
				PaintForArgument(VTPanel.PAINTARG_CHECKDATA_PATH);
			}
		}
		////////
		else if(actionName.equals("LOAD_IMAGES"))
		{
			LoadImages();
			if(SetPreviewImage(0, new Random().nextInt(Math.max(1,trainData_loadedImages.size())))) PaintForArgument(VTPanel.PAINTARG_UPDATEIMAGE);
		}
		////////
		else if(actionName.equals("NEXT_IMAGE"))
		{
			NextImage();
		}
		////////
		else if(actionName.equals("PREVIOUS_IMAGE"))
		{
			if(image_preview_list == 0)
			{
				if(--image_preview_index == -1)
					image_preview_index = trainData_loadedImages.size();
			}
			else if(image_preview_list == 1)
			{
				if(--image_preview_index == -1)
					image_preview_index = checkData_loadedImages.size();
			}
			
			if(SetPreviewImage(image_preview_list, image_preview_index)) PaintForArgument(VTPanel.PAINTARG_UPDATEIMAGE);
		}
		///////
		else if(actionName.equals("GET_NETWORK_RESULT"))	//TEMPORARY
		{
			//if(image_preview == null) return;
			if(true) return;	//networks will be implemented in future
			
			network_input = ConvertToNetworkInput(image_preview, VisionTrainer.neurons_in_input_layer);
			
			VisionTrainer.SimplifiedMulticoreIterate(network_input);
			network_resultPosition = new dVector2(VisionTrainer.simplifiedDeepNetwork.output[0], VisionTrainer.simplifiedDeepNetwork.output[1]);
			UpdateNetworkError();
		
			PaintForArgument(VTPanel.PAINTARG_UPDATE_NETWORK_RESULT);
		}
		///////
		else if(actionName.equals("TRAIN_NETWORK"))
		{
			//if(image_preview == null) return;
			if(true) return;	//networks will be implemented in future
			
			ArrayList<Integer> allowedImages = new ArrayList<Integer>();
			
			int currentAllowedImage = 0;
			
			for(int i = 0; i < 10; i++)
				allowedImages = GetRandomNotExistent(allowedImages, trainData_loadedImages.size());
			
			/*for(int i = 0; i < 20001; i++)
			{
				NextRandomImage();
				network_input = ConvertToNetworkInput(image_preview, VisionTrainer.neurons_in_input_layer);
				
				VisionTrainer.deepNetwork_vision.Iterate(network_input);
				network_resultPosition = new dVector2(VisionTrainer.deepNetwork_vision.output[0], VisionTrainer.deepNetwork_vision.output[1]);
				UpdateNetworkError();
			
				TrainNetwork();
				//System.out.println(i);
				
				if(i % 100 == 0)
				{
					GetErrorOnCheckData();
					System.out.println(i);
				}
			}*/
			
			//long time = 0;
			
			for(int i = 0; i < 100000; i++)
			{
				for(int k = 0; k < allowedImages.size(); k++)
				{
					currentAllowedImage = NextAllowedImage(allowedImages, currentAllowedImage);
					network_input = ConvertToNetworkInput(image_preview, VisionTrainer.neurons_in_input_layer);
					
					VisionTrainer.SimplifiedMulticoreIterate(network_input);
					//VisionTrainer.deepNetwork_vision.Iterate(network_input);
					
					network_resultPosition = new dVector2(VisionTrainer.simplifiedDeepNetwork.output[0], VisionTrainer.simplifiedDeepNetwork.output[1]);
					UpdateNetworkError();
					
					TrainNetwork();
				}
				
				if(i % 2 == 0)
				{
					double err = GetErrorOnAllowedCheckData(allowedImages);
					System.out.println(err + " for " + i + " and " + allowedImages.size() + " images");
					//time = System.nanoTime();
					if(err < 0.009d)
					{
						allowedImages = GetRandomNotExistent(allowedImages, trainData_loadedImages.size());
						System.out.println("Passed threshold");
						if(allowedImages.size() >= 100) break;
					}
				}
				//else if(i % 10 == 0) System.out.println(i);
			}
			
			VisionTrainer.SimplifiedMulticoreIterate(network_input);
			
			network_resultPosition = new dVector2(VisionTrainer.simplifiedDeepNetwork.output[0], VisionTrainer.simplifiedDeepNetwork.output[1]);
			UpdateNetworkError();
		
			PaintForArgument(VTPanel.PAINTARG_UPDATE_NETWORK_RESULT);
		}
		///////
		else if(actionName.equals("CHECK_NETWORK"))
		{
			GetErrorOnCheckData();
		}
		///////
		else if(actionName.equals("GET_CLASSIC_VISION_RESULT"))
		{
			if(image_preview == null) return;
			
			//for(ClassicVision.threshold = 0.01; ClassicVision.threshold < 1; ClassicVision.threshold += 0.01)
				network_resultPosition = ClassicVision.GetBallPosition(ConvertToClassicInput(image_preview));
			
			UpdateNetworkError();
		
			PaintForArgument(VTPanel.PAINTARG_UPDATE_NETWORK_RESULT);
		}
		///////
	}
	
	public int NextAllowedImage(ArrayList<Integer> ar, int current)
	{
		if(++current == ar.size())
			current = 0;
		
		if(SetPreviewImage(0, ar.get(current))) PaintForArgument(VTPanel.PAINTARG_UPDATEIMAGE);
		return current;
	}
	
	public ArrayList<Integer> GetRandomNotExistent(ArrayList<Integer> ar, int range)
	{
		Random rand = new Random();
		boolean exists = true;
		int r = 0;
		
		while(exists)
		{
			exists = false;
			r = rand.nextInt(range);
		
			for(int i = 0; i < ar.size(); i++)
			{
				if(ar.get(i) == r)
				{
					exists = true;
					break;
				}
			}
		}
		
		ar.add(r);
		
		return ar;
	}
	
	public double GetErrorOnAllowedCheckData(ArrayList<Integer> ar)
	{
		int dataSize = ar.size();
		double avgError = 0;
		
		for(int i = 0; i < dataSize; i++)
		{
			SetPreviewImage(0, ar.get(i));
			network_input = ConvertToNetworkInput(image_preview, VisionTrainer.neurons_in_input_layer);
			
			//VisionTrainer.deepNetwork_vision.Iterate(network_input);
			VisionTrainer.SimplifiedMulticoreIterate(network_input);
			network_resultPosition = new dVector2(VisionTrainer.simplifiedDeepNetwork.output[0], VisionTrainer.simplifiedDeepNetwork.output[1]);
			UpdateNetworkError();
			
			avgError += network_distance_error;
		}
		
		avgError /= dataSize;
		
		return avgError;
		//System.out.println("Average error on checkData = " + avgError);
	}
	
	public void GetErrorOnCheckData()
	{
		//if(checkData_loadedImages.size() == 0) return;
		if(true) return;	//networks will be implemented in future
		
		int dataSize = checkData_loadedImages.size();
		double avgError = 0;
		
		for(int i = 0; i < dataSize; i++)
		{
			SetPreviewImage(1, i);
			network_input = ConvertToNetworkInput(image_preview, VisionTrainer.neurons_in_input_layer);
			
			VisionTrainer.SimplifiedMulticoreIterate(network_input);
			network_resultPosition = new dVector2(VisionTrainer.simplifiedDeepNetwork.output[0], VisionTrainer.simplifiedDeepNetwork.output[1]);
			UpdateNetworkError();
			
			avgError += network_distance_error;
		}
		
		avgError /= dataSize;
		
		System.out.println("Average error on checkData = " + avgError);
	}
	
	public void NextImage()
	{
		if(image_preview_list == 0)
		{
			if(++image_preview_index == trainData_loadedImages.size())
				image_preview_index = 0;
		}
		else if(image_preview_list == 1)
		{
			if(++image_preview_index == checkData_loadedImages.size())
				image_preview_index = 0;
		}
		
		if(SetPreviewImage(image_preview_list, image_preview_index)) PaintForArgument(VTPanel.PAINTARG_UPDATEIMAGE);
	}
	
	public void NextRandomImage()
	{
		if(SetPreviewImage(0, new Random().nextInt(Math.max(1,trainData_loadedImages.size())))) PaintForArgument(VTPanel.PAINTARG_UPDATEIMAGE);
	}
	
	public void UpdateNetworkError()
	{
		network_distance_error = VisionTrainer.GetMeanSquaredError(network_resultPosition, image_loadedPosition);
	}
	
	public void TrainNetwork()
	{
		VisionTrainer.SimplifiedTrainNetwork(network_input, image_loadedPosition, network_distance_error);
	}

	public void keyPressed(KeyEvent arg0)
	{
		
	}

	public void keyReleased(KeyEvent arg0)
	{
		
	}

	public void keyTyped(KeyEvent arg0)
	{
		
	}

	public void mouseClicked(MouseEvent arg0)
	{
		
	}

	public void mouseEntered(MouseEvent arg0)
	{
		
	}

	public void mouseExited(MouseEvent arg0)
	{
		
	}

	public void mousePressed(MouseEvent arg0)
	{
		
	}

	public void mouseReleased(MouseEvent arg0)
	{
		
	}
	
	
}

class VTPanel extends JPanel{
	private static final long serialVersionUID = 4660968185771248179L;

	public static int PAINTARG_UI_INIT = 1;
	public static int PAINTARG_TRAINDATA_PATH = 2;
	public static int PAINTARG_CHECKDATA_PATH = 3;
	public static int PAINTARG_UPDATEIMAGE = 4;
	public static int PAINTARG_UPDATE_NETWORK_RESULT = 5;
	
	public int paintArg;
	
	public VTPanel()
	{
		
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		InitUI(g);
		
		g.drawString(VTWindow.trainData_path_preview_text + VTWindow.trainData_path, VTWindow.trainData_path_preview_position.x, VTWindow.trainData_path_preview_position.y);
		g.drawString(VTWindow.checkData_path_preview_text + VTWindow.checkData_path, VTWindow.checkData_path_preview_position.x, VTWindow.checkData_path_preview_position.y);
		
		if(VTWindow.image_preview != null)
		{
			//painting real position square
			g.drawImage(VTWindow.image_preview, VTWindow.trainData_photo_preview_rect.x, VTWindow.trainData_photo_preview_rect.y, VTWindow.trainData_photo_preview_rect.width+1, VTWindow.trainData_photo_preview_rect.height+1, null);
			g.drawString("Real position " + VTWindow.image_loadedPosition.ToString(), VTWindow.image_loadedPosition_textPosition.x, VTWindow.image_loadedPosition_textPosition.y);
			Point realPos_square = new Point((int) (VTWindow.trainData_board_preview_rect.x + VTWindow.trainData_board_preview_rect.width * (1+VTWindow.image_loadedPosition.x)/2),
											(int) (VTWindow.trainData_board_preview_rect.y + VTWindow.trainData_board_preview_rect.height * (1+VTWindow.image_loadedPosition.y)/2));
			g.setColor(Color.BLUE);
			g.fillRect(realPos_square.x - VTWindow.board_preview_realPosition_square_size, realPos_square.y - VTWindow.board_preview_realPosition_square_size, 2*VTWindow.board_preview_realPosition_square_size, 2*VTWindow.board_preview_realPosition_square_size);
			
			//painting predicted position square
			g.setColor(Color.RED);
			Point predictedPos_square = new Point((int) (VTWindow.trainData_board_preview_rect.x + VTWindow.trainData_board_preview_rect.width * (1+VTWindow.network_resultPosition.x)/2),
					(int) (VTWindow.trainData_board_preview_rect.y + VTWindow.trainData_board_preview_rect.height * (1+VTWindow.network_resultPosition.y)/2));
			g.fillRect(predictedPos_square.x - VTWindow.board_preview_realPosition_square_size, predictedPos_square.y - VTWindow.board_preview_realPosition_square_size, 2*VTWindow.board_preview_realPosition_square_size, 2*VTWindow.board_preview_realPosition_square_size);
			
			g.setColor(Color.BLACK);
		}
		if(paintArg == PAINTARG_UPDATE_NETWORK_RESULT)
		{
			g.drawString("Predicted position " + VTWindow.network_resultPosition.ToString(), VTWindow.network_resultPosition_textPosition.x, VTWindow.network_resultPosition_textPosition.y);
			g.drawString("Error " + VTWindow.network_distance_error, VTWindow.network_distance_error_textPosition.x, VTWindow.network_distance_error_textPosition.y);
		}
	}
	
	void InitUI(Graphics g)
	{
		g.drawRect(VTWindow.trainData_photo_preview_rect.x-1, VTWindow.trainData_photo_preview_rect.y-1, VTWindow.trainData_photo_preview_rect.width+2, VTWindow.trainData_photo_preview_rect.height+2);
		g.drawRect(VTWindow.trainData_board_preview_rect.x-1, VTWindow.trainData_board_preview_rect.y-1, VTWindow.trainData_board_preview_rect.width+2, VTWindow.trainData_board_preview_rect.height+2);
		
		g.drawString(VTWindow.trainData_path_preview_text, VTWindow.trainData_path_preview_position.x, VTWindow.trainData_path_preview_position.y);
		g.drawString(VTWindow.checkData_path_preview_text, VTWindow.checkData_path_preview_position.x, VTWindow.checkData_path_preview_position.y);
	}


}
