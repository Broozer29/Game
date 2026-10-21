package net.riezebos.bruus.tbd.visualsandaudio.objects;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.*;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteAnimation extends Sprite implements Cloneable{

	private int currentFrame = 0;
	private int totalFrames;
	private List<BufferedImage> originalFrames = new ArrayList<>();
	private List<BufferedImage> increasedSizeFrames = new ArrayList<>();
	private List<BufferedImage> frames = new ArrayList<>();
	private RotationGeometry.Geometry[] frameGeometries; // Per frame, set when the frames are unrotated and drawn through a transform
	private RotationGeometry.Geometry drawnGeometry; // Geometry of the frame returned last by getCurrentFrameImage(true)
	private int frameDelayCounter;
	private int frameDelay = 2;
	private boolean infiniteLoop;

	private Rectangle animationBounds;

	private int originXCoordinate;
	private int originYCoordinate;


	public SpriteAnimation(SpriteAnimationConfiguration spriteAnimationConfiguration){
		super(spriteAnimationConfiguration.getSpriteConfiguration());
		loadGifFrames(spriteAnimationConfiguration.getSpriteConfiguration().getImageType());
		this.initAnimation();
		this.frameDelayCounter = 0;
		this.infiniteLoop = spriteAnimationConfiguration.isInfiniteLoop();
		this.frameDelay = spriteAnimationConfiguration.getFrameDelay();
		setAnimationScale(spriteAnimationConfiguration.getSpriteConfiguration().getScale());
		animationBounds = new Rectangle(xCoordinate, yCoordinate, this.width, this.height);

	}
	protected void initAnimation() {
		setImage(frames.get(0));
		totalFrames = frames.size();
	}

	// Sets frames, Animation shouldn't call the ImageDatabase, it should get it
	// from a manager when created.
	private void loadGifFrames(ImageEnums imageType) {
		this.imageEnum = imageType;
		this.frames = ImageDatabase.getInstance().getAnimation(imageType);
		this.originalFrames = frames;
		totalFrames = frames.size();
		recalculateBoundsAndSize();
	}

	public void changeImagetype(ImageEnums imageType) {
		this.imageEnum = imageType;
		this.frames = ImageDatabase.getInstance().getAnimation(imageType);
		this.increasedSizeFrames = frames;
		this.originalFrames = frames;
		totalFrames = frames.size();
		clearRotation();
		recalculateBoundsAndSize();
	}

	@Override
	public void clearRotation() {
		super.clearRotation();
		frameGeometries = null;
		drawnGeometry = null;
	}

	// Size of a frame as drawn: the geometry's size when the frames are unrotated
	private int frameWidth(int i) {
		return frameGeometries != null ? frameGeometries[i].width : frames.get(i).getWidth(null);
	}

	private int frameHeight(int i) {
		return frameGeometries != null ? frameGeometries[i].height : frames.get(i).getHeight(null);
	}

	public RotationGeometry.Geometry getCurrentGeometry() {
		if (frameGeometries == null) {
			return null;
		}
		return frameGeometries[currentFrame < frames.size() ? currentFrame : frames.size() - 1];
	}

	public RotationGeometry.Geometry getDrawnGeometry() {
		return drawnGeometry;
	}


	// Updates the current frame of the animation, and sets it invisible if it's
	// fully played out
	public void updateFrameCount() {
		this.currentFrame += 1;
	}

	public void setAnimationScale(float newScale) {
		this.scale = newScale;
		if (this.frames == null) {
			System.out.println("Crashed because resizing an image that was null/empty");
		}
		this.frames = imageResizer.getScaledFrames(originalFrames, newScale);
		this.increasedSizeFrames = frames;
		clearRotation();
		recalculateBoundsAndSize();
	}

	public void rotateAnimation (Direction rotation, boolean crop) {
		if(ImageRotator.getInstance().isBlockedFromRotating(this.getImageEnum())){
			return;
		}


		//Doesnt use original frames because possible resizes, possible loss of performance and quality but maybe not enough to do anything about it
		List<BufferedImage> base = !this.increasedSizeFrames.isEmpty() ? increasedSizeFrames : frames;
		if (RotateWhileDrawing.isEnabled(this.imageEnum)) {
			RotationGeometry.Geometry[] geometries = new RotationGeometry.Geometry[base.size()];
			for (int i = 0; i < geometries.length; i++) {
				geometries[i] = RotationGeometry.geometry(base.get(i), rotation.toAngle(), crop);
			}
			this.frames = base;
			this.frameGeometries = geometries;
			if (DevTestSettings.verifyRotateWhileDrawing) {
				RotateWhileDrawing.LOG_verifyFrames(this.imageEnum, base, ImageRotator.getInstance().getRotatedFrames(base, rotation, crop), rotation.toAngle(), true, crop, geometries);
			}
		} else if(!this.increasedSizeFrames.isEmpty()){
			this.frames = ImageRotator.getInstance().getRotatedFrames(increasedSizeFrames, rotation, crop);
		} else {
			this.frames = ImageRotator.getInstance().getRotatedFrames(frames, rotation, crop);
		}
		recalculateBoundsAndSize();
	}

	public void rotateAnimation(double angle, boolean crop){
		if(ImageRotator.getInstance().isBlockedFromRotating(this.getImageEnum())){
			return;
		}

		//Doesnt use original frames because possible resizes, possible loss of performance and quality but maybe not enough to do anything about it
		List<BufferedImage> base = !this.increasedSizeFrames.isEmpty() ? increasedSizeFrames : frames;
		if (RotateWhileDrawing.isEnabled(this.imageEnum)) {
			RotationGeometry.Geometry[] geometries = RotationGeometry.frameListGeometry(base, angle);
			this.frames = base;
			this.frameGeometries = geometries;
			if (DevTestSettings.verifyRotateWhileDrawing) {
				RotateWhileDrawing.LOG_verifyFrames(this.imageEnum, base, ImageRotator.getInstance().getRotatedFrames(base, angle), angle, false, crop, geometries);
			}
		} else if(!this.increasedSizeFrames.isEmpty()){
			this.frames = ImageRotator.getInstance().getRotatedFrames(increasedSizeFrames, angle);
		} else {
			this.frames = ImageRotator.getInstance().getRotatedFrames(frames, angle);
		}
		recalculateBoundsAndSize();
	}

	private void removeAnimation() {
		if (this.currentFrame >= frames.size() && !infiniteLoop) {
			this.setVisible(false);
		}
	}

	// Required for the engine to refresh itself
	public void refreshAnimation() {
		this.currentFrame = 0;
		this.frameDelayCounter = 0;
		this.setVisible(true);
	}

	private void resetAnimationFrames(){
		this.currentFrame = 0;
		this.frameDelayCounter = 0;
	}

	// returns current frame of the gif

	public BufferedImage getCurrentFrameImage(boolean increaseAnimationFrame) {
		if (increaseAnimationFrame) {
			if (currentFrame >= frames.size()) {
				if (infiniteLoop) {
					resetAnimationFrames();
				} else {
					removeAnimation();
				}
			}

			if (currentFrame < frames.size()) {
				BufferedImage returnImage = frames.get(currentFrame);
				drawnGeometry = frameGeometries != null ? frameGeometries[currentFrame] : null;

				width = frameWidth(currentFrame);
				height = frameHeight(currentFrame);
				if (frameDelayCounter >= frameDelay) {
					updateFrameCount();
					frameDelayCounter = 0;
				} else
					frameDelayCounter++;
				animationBounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
				this.bounds = animationBounds;


				if (this.shouldChangeTransparancy &&
						this.transparancyAlpha + this.transparancyStepSize < 1.0f &&
							this.transparancyAlpha + this.transparancyStepSize > 0.0f) {
						this.transparancyAlpha += this.transparancyStepSize;

                        if(this.transparancyAlpha > 1.0f) {
                            this.transparancyAlpha = 1.0f; //clamp it for certainty
                        } else if(this.transparancyAlpha < 0.0f) {
                            this.transparancyAlpha = 0.0f;  //clamp it for certainty
                        }
					}

				if(this.transparancyAlpha <= 0.05f && this.transparancyStepSize < 0.0f) {
					super.visible = false;
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

	@Override
	public float getScale() {
		return scale;
	}

	public void setFrameDelay(int frameDelay) {
		this.frameDelay = frameDelay;
	}

	public ImageEnums getImageEnum () {
		return this.imageEnum;
	}

	@Override
	public Rectangle getBounds() {
		return animationBounds;
	}

	public void setAnimationBounds(int xCoordinate, int yCoordinate) {
		if (currentFrame < frames.size()) {
			this.animationBounds.setBounds(xCoordinate, yCoordinate, frameWidth(currentFrame),
					frameHeight(currentFrame));
		} else {
			this.animationBounds.setBounds(xCoordinate, yCoordinate, frameWidth(frames.size() - 1),
					frameHeight(frames.size() - 1));
		}
	}

	@Override
	public void setImageDimensions(int newWidth, int newHeight) {
		ImageResizer imageResizer = ImageResizer.getInstance();
		List<BufferedImage> resizedFrames = new ArrayList<>(frames.size());
		for (int i = 0; i < frames.size(); i++) {
			resizedFrames.add(imageResizer.resizeImageToDimensions(frames.get(i), newWidth, newHeight));
		}
		this.frames = resizedFrames;
		this.increasedSizeFrames = resizedFrames;
		clearRotation();
		recalculateBoundsAndSize();
	}

	// Should be used with all animation creation when the animation has different frame
	// dimensions
	@Override
	public void setCenterCoordinates(int newXCoordinate, int newYCoordinate) {
		if (currentFrame < frames.size()) {
			this.xCoordinate = newXCoordinate - (frameWidth(currentFrame) / 2);
			this.yCoordinate = newYCoordinate - (frameHeight(currentFrame) / 2);
		} else {
			this.xCoordinate = newXCoordinate - (frameWidth(currentFrame - 1) / 2);
			this.yCoordinate = newYCoordinate - (frameHeight(currentFrame - 1) / 2);
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
		this.originalFrames = cropFrames(originalFrames);
		setAnimationScale(this.scale);
	}

	private List<BufferedImage> cropFrames(List<BufferedImage> sourceFrames) {
		ImageCropper imageCropper = ImageCropper.getInstance();
		List<BufferedImage> croppedFrames = new ArrayList<>(sourceFrames.size());
		for (int i = 0; i < sourceFrames.size(); i++) {
			croppedFrames.add(imageCropper.cropToContent(sourceFrames.get(i)));
		}
		return croppedFrames;
	}


	@Override
	public void recalculateBoundsAndSize(){
		this.width = frameWidth(0);
		this.height = frameHeight(0);
		this.animationBounds = new Rectangle(xCoordinate, yCoordinate, this.width, this.height);
		this.bounds = animationBounds;
	}


	@Override
	public String toString () {
		return "SpriteAnimation{" +
				"imageResizer=" + imageResizer +
				", currentFrame=" + currentFrame +
				", totalFrames=" + totalFrames +
				", standardSizeFrames=" + originalFrames +
				", frames=" + frames +
				", frameDelayCounter=" + frameDelayCounter +
				", frameDelay=" + frameDelay +
				", infiniteLoop=" + infiniteLoop +
				", animationBounds=" + animationBounds +
				", originXCoordinate=" + originXCoordinate +
				", originYCoordinate=" + originYCoordinate +
				", width=" + width +
				", height=" + height +
				", visible=" + visible +
				", image=" + image +
				", imageType=" + imageEnum +
				'}';
	}

	@Override
	public SpriteAnimation clone() {
		try {
			SpriteAnimation cloned = (SpriteAnimation) super.clone();
			cloned.clearRotation(); // The shallow copy shares the geometry, the clone starts unrotated

			// Load frames from the ImageDatabase
			cloned.loadGifFrames(this.imageEnum);

			// Copy other properties
			cloned.currentFrame = this.currentFrame;
			cloned.totalFrames = this.totalFrames;
			cloned.frameDelayCounter = this.frameDelayCounter;
			cloned.frameDelay = this.frameDelay;
			cloned.infiniteLoop = this.infiniteLoop;
			cloned.animationBounds = new Rectangle(this.animationBounds);
			cloned.originXCoordinate = this.originXCoordinate;
			cloned.originYCoordinate = this.originYCoordinate;

			// Re-initialize the animation
			cloned.initAnimation();
			cloned.setAnimationScale(this.scale);
			return cloned;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // Can never happen
		}
	}

	public boolean isPlaying () {
		if(currentFrame < totalFrames && visible){
            return AnimationManager.getInstance().getUpperAnimations().contains(this) ||
                    AnimationManager.getInstance().getLowerAnimations().contains(this);
		}
		return false;
	}

	public void setCurrentFrame (int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public boolean isFinished () {
        return currentFrame >= totalFrames;
    }


    public int getFrameDelay () {
		return frameDelay;
    }

	public void setInfiniteLoop (boolean infiniteLoop) {
		this.infiniteLoop = infiniteLoop;
	}

	public boolean isInfiniteLoop () {
		return infiniteLoop;
	}

    public static SpriteAnimationConfiguration createDefaultSpriteAnimationConfig(ImageEnums imageEnum){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setImageType(imageEnum);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setxCoordinate(-100);
        spriteConfiguration.setyCoordinate(-100);
        return new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
    }
}
