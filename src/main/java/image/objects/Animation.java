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

import data.ImageDatabase;

public class Animation extends Sprite {

	private int currentFrame;
	private int totalFrames;
	private List<ImageIcon> frames = new ArrayList<ImageIcon>();

	public Animation(int x, int y, String imageType) {
		super(x, y);
		this.initAnimation(imageType);
	}

	protected void initAnimation(String imageType) {
		loadGifFrames(imageType);
		setImage(frames.get(0).getImage());
		getImageDimensions();
		centerAnimationFrame();
		totalFrames = frames.size();
	}

	// Sets frames, Animation shouldn't call the ImageDatabase, it should get it from a manager when created.
	private void loadGifFrames(String imageType) {
		switch (imageType) {
		case ("Impact Explosion One"):
			this.frames = ImageDatabase.getInstance().getImpactExplosionOneFrames();
			return;
		case ("Player Engine"):
			this.frames = ImageDatabase.getInstance().getPlayerEngineFrames();
			return;
		case ("Destroyed Explosion"):
			this.frames = ImageDatabase.getInstance().getDestroyedExplosionUpFrames();
			return;
		case ("Destroyed Explosion Right"):
			this.frames = ImageDatabase.getInstance().getDestroyedExplosionRightFrames();
			return;
		case ("Destroyed Explosion Left"):
			this.frames = ImageDatabase.getInstance().getDestroyedExplosionLeftFrames();
			return;
		case ("Destroyed Explosion Reverse"):
			this.frames = ImageDatabase.getInstance().getDestroyedExplosionDownFrames();
			return;
		}
	}

	// Centers the animation a bit further inwards to the collision spot
	private void centerAnimationFrame() {
		this.setX(xCoordinate + (this.getWidth() / 2));
		this.setY(yCoordinate - (this.getHeight() / 2));
	}

	// Updates the current frame of the animation, and sets it invisible if it's
	// fully played out
	public void updateFrameCount() {
		this.currentFrame += 1;
		if (this.currentFrame >= frames.size()) {
			this.setVisible(false);
		}
	}

	//Required for the engine to refresh itself
	public void refreshAnimation(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.currentFrame = 0;
		this.setVisible(true);
	}

	// returns current frame of the gif
	public Image getCurrentFrame() {
		if (currentFrame < frames.size()) {
			Image returnImage = frames.get(currentFrame).getImage();
			return returnImage;
		}
		return frames.get(1).getImage();
	}

	public int getFrame() {
		return this.currentFrame;
	}

	public int getTotalFrames() {
		return this.totalFrames;
	}

}
