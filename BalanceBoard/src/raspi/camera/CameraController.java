package raspi.camera;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.AWB;
import com.hopding.jrpicam.enums.DRC;
import com.hopding.jrpicam.enums.MeteringMode;
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
		.setAWB(AWB.SUN)
		.setDRC(DRC.MEDIUM)
		.setContrast(0)
		.setSharpness(0)
		.setQuality(75)
		.setTimeout(5000)
		.setFullPreviewOff()
		.turnOffPreview()
		.setMeteringMode(MeteringMode.AVERAGE);
		
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
