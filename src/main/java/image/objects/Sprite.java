package image.objects;

import java.awt.Image;
import java.awt.Rectangle;

import data.ImageLoader;

public class Sprite {

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
		ImageLoader imageLoader = ImageLoader.getInstance();
		image = imageLoader.getImage(imageName);
		getImageDimensions();
		// image = image.getScaledInstance(10, 20, 0);
		// Zet collision ook op die getallen en shits & giggles
	}

	protected void getImageDimensions() {
		width = image.getWidth(null);
		height = image.getHeight(null);
	}
	
	//Only used to re-use objects and change the image. Currently only used for background planets.
	protected void setImage(Image image) {
		this.image = image;
		getImageDimensions();
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

	// Get bounds required for collision detection
	public Rectangle getBounds() {
		return new Rectangle(xCoordinate, yCoordinate, width, height);
	}
}