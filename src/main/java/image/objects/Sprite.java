package image.objects;

import java.awt.Image;
import java.awt.Rectangle;

import data.image.ImageDatabase;
import data.image.ImageResizer;
import data.image.ImageRotator;

public class Sprite {
	ImageDatabase imgDatabase = ImageDatabase.getInstance();
	ImageRotator imageRotator = ImageRotator.getInstance();
	ImageResizer imageResizer = ImageResizer.getInstance();
	protected int xCoordinate;
	protected int yCoordinate;
	protected int width;
	protected int height;
	protected boolean visible;
	protected Image image;
	protected float scale;
	protected int xOffset;
	protected int yOffset;

	public Sprite(int x, int y, float scale) {
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.scale = scale;
		visible = true;
	}

	protected void loadImage(String imageName) {
		image = imgDatabase.getImage(imageName);
		this.image = imageResizer.getScaledImage(image, scale);
//		setImageToScale();
		getImageDimensions();
		// Zet collision ook op die getallen en shits & giggles
	}

	protected void getImageDimensions() {
		width = image.getWidth(null);
		height = image.getHeight(null);
	}

	// Only used to re-use objects and change the image. Currently only used for
	// background planets.
	protected void setImage(Image image) {
		this.image = image;
		getImageDimensions();
	}

	protected void rotateImage(String rotation) {
		this.image = imageRotator.rotate(image, rotation);
		getImageDimensions();
	}

	protected void setScale(float newScale) {
		this.scale = newScale;
		this.image = imageResizer.getScaledImage(image, scale);
		getImageDimensions();
	}

	public int getCenterXCoordinate() {
		return xCoordinate + (width / 2);
	}

	public int getCenterYCoordinate() {
		return yCoordinate + (height / 2);
	}

	public void setCenterCoordinates(int newXCoordinate, int newYCoordinate) {
		this.xCoordinate = newXCoordinate - this.width;
		this.yCoordinate = newYCoordinate - this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public Image getImage() {
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

	// Get bounds required for collision detection for objects WITHOUT ANIMATIONS
	public Rectangle getBounds() {
		return new Rectangle(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
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
}