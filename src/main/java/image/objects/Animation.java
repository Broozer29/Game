package image.objects;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import data.ImageDatabase;
import javafx.scene.shape.Rectangle;

public class Animation extends Sprite {

	private int currentFrame;
	private int totalFrames;
	private List<ImageIcon> frames = new ArrayList<ImageIcon>();
	private int frameDelay;
	private boolean infiniteLoop;

	public Animation(int x, int y, String imageType, boolean infiniteLoop) {
		super(x, y);
		this.initAnimation(imageType);
		this.frameDelay = 0;
		this.infiniteLoop = infiniteLoop;
	}

	protected void initAnimation(String imageType) {
		loadGifFrames(imageType);
		setImage(frames.get(0).getImage());
		getImageDimensions();
		centerAnimationFrame();
		totalFrames = frames.size();
	}

	// Sets frames, Animation shouldn't call the ImageDatabase, it should get it
	// from a manager when created.
	private void loadGifFrames(String imageType) {
		this.frames = ImageDatabase.getInstance().getGif(imageType);
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

	}

	private void removeAnimation() {
		if (this.currentFrame >= frames.size() && !infiniteLoop) {
			this.setVisible(false);
		}
	}

	// Required for the engine to refresh itself
	public void refreshAnimation(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.currentFrame = 0;
		this.frameDelay = 0;
		this.setVisible(true);
	}

	// returns current frame of the gif
	public Image getCurrentFrame() {
		if (currentFrame >= frames.size()) {
			if (infiniteLoop) {
				refreshAnimation(this.xCoordinate, this.yCoordinate);
			} else {
				removeAnimation();
			}
		}

		if (currentFrame < frames.size()) {
			Image returnImage = frames.get(currentFrame).getImage();
			width = returnImage.getWidth(null);
			height = returnImage.getHeight(null);
			if (frameDelay >= 1) {
				updateFrameCount();
				frameDelay = 0;
			} else
				frameDelay++;
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
	
	// Get bounds for sprites that have ANIMATIONS. Regular bounds don't work
	public Rectangle getAnimationBounds() {
		return new Rectangle(xCoordinate, yCoordinate, width, height);
	}

}
