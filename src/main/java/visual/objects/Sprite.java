package visual.objects;

import java.awt.Image;
import java.awt.Rectangle;

import data.image.ImageCropper;
import data.image.ImageDatabase;
import data.image.ImageEnums;
import data.image.ImageResizer;
import data.image.ImageRotator;
import game.movement.Direction;
import game.movement.Point;

public class Sprite {
	private ImageDatabase imgDatabase = ImageDatabase.getInstance();
	private ImageRotator imageRotator = ImageRotator.getInstance();
	private ImageResizer imageResizer = ImageResizer.getInstance();
	protected int xCoordinate;
	protected int yCoordinate;
	protected int width;
	protected int height;
	protected boolean visible;
	protected Image image;
	protected float scale;
	protected int xOffset;
	protected int yOffset;
	protected Rectangle bounds;
	protected Point currentLocation;
	

	public Sprite(int x, int y, float scale) {
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.scale = scale;
		this.visible = true;
		this.bounds = new Rectangle();
		currentLocation = new Point(x,y);
	}

	protected void loadImage(ImageEnums imageName) {
		this.image = imgDatabase.getImage(imageName);
		if (this.image == null) {
			System.out.println("Crashed because getting " + imageName + " returned an empty/null image");
		}
//		if (scale != 1 && this.image != null) {
		this.image = imageResizer.getScaledImage(image, scale);
//		}
		getImageDimensions();
//		 Zet collision ook op die getallen en shits & giggles
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

	protected void rotateImage(Direction rotation) {
		this.image = imageRotator.rotate(image, rotation);
		getImageDimensions();
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
		return this.bounds;
	}
}