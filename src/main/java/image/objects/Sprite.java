package image.objects;

import java.awt.Image;
import java.awt.Rectangle;

import data.ImageDatabase;
import data.ImageResizer;
import data.ImageRotator;

public class Sprite {
	ImageDatabase imgDatabase = ImageDatabase.getInstance();
	ImageResizer imgResizer = ImageResizer.getInstance();
	protected int xCoordinate;
	protected int yCoordinate;
	protected int width;
	protected int height;
	protected boolean visible;
	protected Image image;

	public Sprite(int x, int y) {
		this.xCoordinate = x;
		this.yCoordinate = y;
		visible = true;
	}

	protected void loadImage(String imageName) {
		image = imgDatabase.getImage(imageName);
//		setImageToScale();
		getImageDimensions();
//		image = image.getScaledInstance(10, 20, 0);
		// Zet collision ook op die getallen en shits & giggles
	}
	
	//Ongetest!
	protected void resizeSprite(int scale) {
		Image newImage = imgResizer.getScaledImage(image, width, height, scale);
		if(newImage != null) {
			this.image = newImage;
			getImageDimensions();
		}
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

	// Perhaps never to be used again
//	private void setImageToScale() {
//		ImageResizer test = ImageResizer.getInstance();
//		image = test.getScaledImage(image, image.getWidth(null), image.getHeight(null), scale);
//	}
//	public int getScale() {
//	return this.scale;
//}

	protected void rotateImage(double angle) {
		ImageRotator imageRotator = ImageRotator.getInstance();
		this.image = imageRotator.rotate(image, angle);

	}
	
	public int getCenterXCoordinate() {
		return xCoordinate + (width / 2);
	}
	
	public int getCenterYCoordinate() {
		return yCoordinate + (height / 2);
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
		return xCoordinate;
	}

	public int getYCoordinate() {
		return yCoordinate;
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
		return new Rectangle(xCoordinate, yCoordinate, width, height);
	}
}