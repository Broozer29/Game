package visual.objects;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.movement.Direction;
import game.movement.Point;
import gamedata.DataClass;
import gamedata.image.ImageCropper;
import gamedata.image.ImageDatabase;
import gamedata.image.ImageEnums;
import gamedata.image.ImageResizer;
import gamedata.image.ImageRotator;

public class Sprite {
	private ImageDatabase imgDatabase = ImageDatabase.getInstance();
	private ImageRotator imageRotator = ImageRotator.getInstance();
	private ImageResizer imageResizer = ImageResizer.getInstance();
	protected int xCoordinate;
	protected int yCoordinate;
	protected int width;
	protected int height;
	protected boolean visible;
	protected BufferedImage image;
	protected float scale;
	protected int xOffset;
	protected int yOffset;
	protected Rectangle bounds;
	protected Point currentLocation;
	protected int currentBoardBlock;
	
	protected float transparancyAlpha;
	protected boolean increaseTransparancy;
	protected float transparancyStepSize;

	public Sprite(int x, int y, float scale) {
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.scale = scale;
		this.visible = true;
		this.bounds = new Rectangle();
		this.transparancyAlpha = (float) 1.0;
		this.increaseTransparancy = false;
		this.transparancyStepSize = 0;
		this.currentLocation = new Point(x, y);
	}

	protected void loadImage(ImageEnums imageName) {
		this.image = imgDatabase.getImage(imageName);
		if (this.image == null) {
			System.out.println("Crashed because getting " + imageName + " returned an empty/null image");
		}
		if (scale != 1 && this.image != null) {
			this.image = imageResizer.getScaledImage(image, scale);
		}
		getImageDimensions();
//		 Zet collision ook op die getallen en shits & giggles
	}

	protected void getImageDimensions() {
		width = image.getWidth(null);
		height = image.getHeight(null);
	}

	// Only used to re-use objects and change the image. Currently only used for
	// background planets.
	protected void setImage(BufferedImage image) {
		this.image = image;
		getImageDimensions();
	}

	protected void rotateImage(Direction rotation) {
		if (rotation != Direction.LEFT) {
			this.image = imageRotator.rotate(image, rotation);
			getImageDimensions();
		}
	}

	protected void setScale(float newScale) {
		if (this.scale != newScale) {
			this.scale = newScale;

			if (this.image == null) {
				System.out.println("Crashed because resizing an image that was null/empty");
			}
			this.image = imageResizer.getScaledImage(image, scale);
			getImageDimensions();
		}

	}

	public int getCenterXCoordinate() {
		return xCoordinate + (width / 2);
	}

	public int getCenterYCoordinate() {
		return yCoordinate + (height / 2);
	}

	public void setCenterCoordinates(int newXCoordinate, int newYCoordinate) {
		this.xCoordinate = newXCoordinate - (this.width / 2);
		this.yCoordinate = newYCoordinate - (this.height / 2);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getXCoordinate() {
		return xCoordinate + xOffset;
	}

	public int getYCoordinate() {
		return yCoordinate + yOffset;
	}

	public void setX(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public void setY(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public void addXOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getXOffset() {
		return this.xOffset;
	}

	public void addYOffset(int yoffset) {
		this.yOffset = yoffset;
	}

	public void setImageDimensions(int newWidth, int newHeight) {
		ImageResizer imageResizer = ImageResizer.getInstance();
		this.image = imageResizer.resizeImageToDimensions(this.image, newWidth, newHeight);
		getImageDimensions();

	}

	protected void cropWidth(float cropPercentage) {
		ImageCropper imageCropper = ImageCropper.getInstance();
		this.image = imageCropper.cropImage(this.image, cropPercentage);
	}

	public Point getPoint() {
		return this.currentLocation;
	}

	public Rectangle getBounds() {
		if (this instanceof SpriteAnimation) {
			System.out.println("I returned Sprite bounds for a SpriteAnimation!");
		}
		return this.bounds;
	}

	protected void setBounds(int xCoordinate, int yCoordinate, int width, int height) {
		this.bounds.setBounds(xCoordinate, yCoordinate, width, height);
	}

	public void updateCurrentBoardBlock() {
		if (xCoordinate >= 0 && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 1)) {
			this.currentBoardBlock = 0;
		} else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 1)
				&& xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 2)) {
			this.currentBoardBlock = 1;
		} else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 2)
				&& xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 3)) {
			this.currentBoardBlock = 2;
		} else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 3)
				&& xCoordinate <= (DataClass.getInstance().getBoardBlockWidth())) {
			this.currentBoardBlock = 3;
		} else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 4)
				&& xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 5)) {
			this.currentBoardBlock = 4;
		} else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 5)
				&& xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 6)) {
			this.currentBoardBlock = 5;
		} else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 6)
				&& xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 7)) {
			this.currentBoardBlock = 6;
		} else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 7)
				&& xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 8)) {
			this.currentBoardBlock = 7;
		} else if (xCoordinate > DataClass.getInstance().getBoardBlockWidth() * 8) {
			this.currentBoardBlock = 8;
		}
	}

	public int getCurrentBoardBlock() {
		return this.currentBoardBlock;
	}
	
	//Sets the new transparancy values, also tells the managers to increase the transparancy or not
	public void setTransparancyAlpha(boolean shouldIncrease, float newAlphaTransparancy, float transparacyStepSize) {
		this.increaseTransparancy = shouldIncrease;
		this.transparancyAlpha = newAlphaTransparancy;
		this.transparancyStepSize = transparacyStepSize;
	}
	
	public float getTransparancyAlpha() {
		return this.transparancyAlpha;
	}
}