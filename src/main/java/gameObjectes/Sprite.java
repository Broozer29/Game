package gameObjectes;

import java.awt.Image;

import javax.swing.ImageIcon;

import Data.ImageLoader;

import java.awt.Rectangle;

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
	}

	protected void getImageDimensions() {
		width = image.getWidth(null);
		height = image.getHeight(null);
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

	//Get bounds required for collision detection
	public Rectangle getBounds() {
		return new Rectangle(xCoordinate, yCoordinate, width, height);
	}
}