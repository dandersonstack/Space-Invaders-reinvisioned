package loaders;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import components.MainComponent;

public class InvaderModelLoader {
	
	public BufferedImage[] modles = new BufferedImage[7];//holds the models to be ready for use
	public int[][] modelDims = new int[7][2];//Dimensions of the modles: [Width, Height]
	
	public InvaderModelLoader() throws IOException {
		URL[] imgFiles = new URL[7];//intermediate step to load images
		imgFiles[0] = MainComponent.class.getResource("/recources/models/Invader_Jelly1.png");//Retrieves images in URL form
		imgFiles[1] = MainComponent.class.getResource("/recources/models/Invader_Jelly2.png");
		imgFiles[2] = MainComponent.class.getResource("/recources/models/Invader_Mid1.png");
		imgFiles[3] = MainComponent.class.getResource("/recources/models/Invader_Mid2.png");
		imgFiles[4] = MainComponent.class.getResource("/recources/models/Invader_Skull1.png");
		imgFiles[5] = MainComponent.class.getResource("/recources/models/Invader_Skull2.png");
		imgFiles[6] = MainComponent.class.getResource("/recources/models/Invader_UFO.png");
		for(int i = 0; i < imgFiles.length; i++) {
			modles[i] = ImageIO.read(imgFiles[i]);//converts to buffered image
			modelDims[i][0] = modles[i].getWidth();//retrieves dimensions of images for later use
			modelDims[i][1] = modles[i].getHeight();
		}
	}

}
