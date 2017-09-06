package raspi.camera;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

import deepnetwork.math.iVector2;

public class CameraController
{
	public static RPiCamera camera;
	
	public static void Initialize(iVector2 res)
	{
		try
		{
			camera = new RPiCamera();
		} catch (FailedToRunRaspistillException e)
		{
			e.printStackTrace();
		}
		
		camera.setWidth(res.x).setHeight(res.y)
		.enableBurst()
		.setTimeout(0);
	}
	
	public static BufferedImage TakePhoto()
	{
		try {
			return camera.takeBufferedStill();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
