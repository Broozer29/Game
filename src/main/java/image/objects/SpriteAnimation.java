package image.objects;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import data.image.ImageDatabase;
import data.image.ImageResizer;
import data.image.ImageRotator;

public class SpriteAnimation extends Sprite {

	ImageResizer imageResizer = ImageResizer.getInstance();
	private int currentFrame;
	private int totalFrames;
	private List<Image> standardSizeFrames = new ArrayList<Image>();
	private List<Image> frames = new ArrayList<Image>();
	private int frameDelayCounter;
	private int frameDelay = 2;
	private boolean infiniteLoop;
	private String imageType;

	public SpriteAnimation(int x, int y, String imageType, boolean infiniteLoop, float scale) {
		super(x, y, scale);
		loadGifFrames(imageType);
		this.initAnimation();
		this.frameDelayCounter = 0;
		this.infiniteLoop = infiniteLoop;
		setAnimationScale(scale);
	}

	protected void initAnimation() {
		setImage(frames.get(0));
		getImageDimensions();
		centerAnimationFrame();
		totalFrames = frames.size();
	}
	
	
	// Sets frames, Animation shouldn't call the ImageDatabase, it should get it
	// from a manager when created.
	private void loadGifFrames(String imageType) {
		this.frames = ImageDatabase.getInstance().getGif(imageType);
		this.standardSizeFrames = frames;
	}
	

	public void changeImagetype(String imageType) {
		this.imageType = imageType;
		this.frames = ImageDatabase.getInstance().getGif(imageType);
		this.standardSizeFrames = frames;
	}
	
	// Aligns the sprite X and Y coordinate to the centre of the animation
	private void centerAnimationFrame() {
		this.setX(xCoordinate + (this.getWidth() / 2));
		this.setY(yCoordinate - (this.getHeight() / 2));
	}

	// Updates the current frame of the animation, and sets it invisible if it's
	// fully played out
	public void updateFrameCount() {
		this.currentFrame += 1;
	}
	
	public void setAnimationScale(float newScale) {
		this.scale = newScale;
		this.frames = imageResizer.getScaledFrames(standardSizeFrames, newScale);
	}
	
	//Called by Animation manager when an animation needs to be deleted but is looping permanently
	public void deleteAnimation() {
		this.infiniteLoop = false;
		this.setVisible(false);
	}
	
	
	public void rotateAnimetion(String rotation) {
		this.frames = ImageRotator.getInstance().getRotatedFrames(frames, rotation);
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
		this.frameDelayCounter = 0;
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
			Image returnImage = frames.get(currentFrame);
			width = returnImage.getWidth(null);
			height = returnImage.getHeight(null);
			if (frameDelayCounter >= frameDelay) {
				updateFrameCount();
				frameDelayCounter = 0;
			} else
				frameDelayCounter++;
			return returnImage;
		}
		return null;
	}
	
	public int getFrame() {
		return this.currentFrame;
	}

	public int getTotalFrames() {
		return this.totalFrames;
	}
	
	public void resizeAnimation(float scale) {
		this.frames = imageResizer.getScaledFrames(frames, scale);
	}

	public float getScale() {
		return scale;
	}
	
	public void setFrameDelay(int frameDelay) {
		this.frameDelay = frameDelay;
	}
	
	public String getImageType() {
		return this.imageType;
	}
	
}
