package visual.objects;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import gamedata.image.ImageCropper;
import gamedata.image.ImageDatabase;
import gamedata.image.ImageEnums;
import gamedata.image.ImageResizer;
import gamedata.image.ImageRotator;

public class SpriteAnimation extends Sprite {

	ImageResizer imageResizer = ImageResizer.getInstance();
	private int currentFrame;
	private int totalFrames;
	private List<BufferedImage> standardSizeFrames = new ArrayList<BufferedImage>();
	private List<BufferedImage> frames = new ArrayList<BufferedImage>();
	private int frameDelayCounter;
	private int frameDelay = 2;
	private boolean infiniteLoop;
	private ImageEnums imageType;
	private Rectangle animationBounds;

	private int originXCoordinate;
	private int originYCoordinate;

	public SpriteAnimation(int x, int y, ImageEnums imageType, boolean infiniteLoop, float scale) {
		super(x, y, scale);
		loadGifFrames(imageType);
		this.initAnimation();
		this.frameDelayCounter = 0;
		this.infiniteLoop = infiniteLoop;
		setAnimationScale(scale);
		animationBounds = new Rectangle(xCoordinate, yCoordinate, this.width, this.height);
	}

	protected void initAnimation() {
		setImage(frames.get(0));
//		getImageDimensions();
		centerAnimationFrame();
		totalFrames = frames.size();
	}

	// Sets frames, Animation shouldn't call the ImageDatabase, it should get it
	// from a manager when created.
	private void loadGifFrames(ImageEnums imageType) {
		this.imageType = imageType;
		this.frames = ImageDatabase.getInstance().getGif(imageType);
		this.standardSizeFrames = frames;
	}

	public void changeImagetype(ImageEnums imageType) {
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
		if (this.image == null) {
			System.out.println("Crashed because resizing an image that was null/empty");
		}
		this.frames = imageResizer.getScaledFrames(standardSizeFrames, newScale);
	}

	public void rotateAnimetion(Direction rotation) {
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
	public BufferedImage getCurrentFrameImage(boolean increaseAnimationFrame) {
		if (increaseAnimationFrame) {
			if (currentFrame >= frames.size()) {
				if (infiniteLoop) {
					refreshAnimation(this.xCoordinate, this.yCoordinate);
				} else {
					removeAnimation();
				}
			}

			if (currentFrame < frames.size()) {
				BufferedImage returnImage = frames.get(currentFrame);
				width = returnImage.getWidth(null);
				height = returnImage.getHeight(null);
				if (frameDelayCounter >= frameDelay) {
					updateFrameCount();
					frameDelayCounter = 0;
				} else
					frameDelayCounter++;
				animationBounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);

				if (this.increaseTransparancy) {
					if (this.transparancyAlpha + this.transparancyStepSize < 1.0f && 
							this.transparancyAlpha + this.transparancyStepSize > 0.0f) {
						this.transparancyAlpha += this.transparancyStepSize;
					}
				}
				
				if(this.transparancyAlpha < 0.05f && this.transparancyStepSize < 0.0f) {
					this.setVisible(false);
					this.removeAnimation();
				}
				return returnImage;
			}
		} else {
			if (currentFrame >= frames.size()) {
				return frames.get(frames.size() - 1);
			} else {
				return frames.get(currentFrame);
			}
		}

		return null;
	}

	public int getCurrentFrame() {
		return this.currentFrame;
	}

	public int getTotalFrames() {
		return this.totalFrames;
	}

	public void resizeAnimation(float scale) {
		if (this.image == null) {
			System.out.println("Crashed because resizing an image that was null/empty");
		}
		this.frames = imageResizer.getScaledFrames(frames, scale);
	}

	public float getScale() {
		return scale;
	}

	public void setFrameDelay(int frameDelay) {
		this.frameDelay = frameDelay;
	}

	public ImageEnums getImageType() {
		return this.imageType;
	}

	public Rectangle getBounds() {
		return animationBounds;
	}

	public void setAnimationBounds(int xCoordinate, int yCoordinate) {
		if (currentFrame < frames.size()) {
			this.animationBounds.setBounds(xCoordinate, yCoordinate, frames.get(currentFrame).getWidth(null),
					frames.get(currentFrame).getHeight(null));
		} else {
			this.animationBounds.setBounds(xCoordinate, yCoordinate, frames.get(frames.size() - 1).getWidth(null),
					frames.get(frames.size() - 1).getHeight(null));
		}
	}

	public void setImageDimensions(int newWidth, int newHeight) {
		ImageResizer imageResizer = ImageResizer.getInstance();

		for (int i = 0; i < frames.size(); i++) {
			frames.set(i, imageResizer.resizeImageToDimensions(frames.get(i), newWidth, newHeight));
		}
	}

	// Should be used with all animation creation when the animation has different frame
	// dimensions
	public void setCenterCoordinates(int newXCoordinate, int newYCoordinate) {
		if (currentFrame < frames.size()) {
			this.xCoordinate = newXCoordinate - (frames.get(currentFrame).getWidth(null) / 2);
			this.yCoordinate = newYCoordinate - (frames.get(currentFrame).getHeight(null) / 2);
		} else {
			this.xCoordinate = newXCoordinate - (frames.get(currentFrame - 1).getWidth(null) / 2);
			this.yCoordinate = newYCoordinate - (frames.get(currentFrame - 1).getHeight(null) / 2);
		}
	}

	public void setOriginCoordinates(int xCoordinate, int yCoordinate) {
		this.originXCoordinate = xCoordinate;
		this.originYCoordinate = yCoordinate;
	}

	public int getOriginXCoordinate() {
		return this.originXCoordinate;
	}

	public int getOriginYCoordinate() {
		return this.originYCoordinate;
	}

	public void cropAnimation() {
		ImageCropper imageCropper = ImageCropper.getInstance();
		for (int i = 0; i < frames.size(); i++) {
			frames.set(i, imageCropper.cropToContent(frames.get(i)));
		}
	}

}
