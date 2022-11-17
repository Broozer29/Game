package image.objects;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;

public class Animation extends Sprite {

	float frameCount;
	float currentFrameCount;
	String animationType;
	Image currentFrame;
	List<ImageIcon> frames = new ArrayList<ImageIcon>();

	public Animation(int x, int y, String imageType) {
		super(x, y);
		this.initAnimation(imageType);
		this.animationType = imageType;
	}

	protected void initAnimation(String imageType) {
		loadGif(imageType);
		setFrameCount(imageType);
		getImageDimensions();
		centerAnimationFrame();
	}

	// Duration length of the frames
	private void setFrameCount(String imageType) {
		switch (imageType) {
		case ("Impact Explosion One"):
			this.frameCount = 12;
			return;
		case ("Player Engine"):
			this.frameCount = 30;
			return;
		case ("Destroyed Explosion"):
			this.frameCount = 50;
			return;
		case ("Destroyed Explosion Right"):
			this.frameCount = 50;
			return;
		case ("Destroyed Explosion Left"):
			this.frameCount = 50;
			return;
		case ("Destroyed Explosion Reverse"):
			this.frameCount = 50;
			return;
		}
	}

	// Centers the animation a bit further inwards to the collision spot
	private void centerAnimationFrame() {
		this.setX(xCoordinate + (this.getWidth() / 2));
		this.setY(yCoordinate - (this.getHeight() / 2));
	}

	public float getFrameCount() {
		return this.frameCount;
	}

	public float getCurrentFrameCount() {
		return this.currentFrameCount;
	}

	public String getAnimationType() {
		return this.animationType;
	}

	// Updates the current frame of the animation, and sets it invisible if it's
	// fully played out
	public void updateFrameCount() {
		this.currentFrameCount += 1;
		if (this.currentFrameCount >= frameCount) {
			this.setVisible(false);
		}
	}

	public void refreshAnimation(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.currentFrameCount = 0;
		this.setFrameCount(animationType);
		this.setVisible(true);
	}
	
	//Updates to the next frame of the gif
	private void setNextFrame() {

	}
	
	//returns current frame of the gif
	public void getCurrentFrame() {
		
	}

}
