package game.movement;

import java.awt.Rectangle;

public class BoardBlock {
	
	private int width;
	private int height;
	private int xCoordinate;
	private int yCoordinate;
	private int boardBlockNumber;
	
	private Rectangle rectangle;
	
	public BoardBlock(int width, int height, int xCoordinate, int yCoordinate, int boardBlockNumber) {
		this.width = width;
		this.height = height;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.boardBlockNumber = boardBlockNumber;
		calculateRectangle();
	}
	
	private void calculateRectangle() {
		this.rectangle = new Rectangle(xCoordinate, yCoordinate, width, height);
	}
	
	public int getBoardBlockNumber() {
		return this.boardBlockNumber;
	}
	
	public Rectangle getBounds() {
		return this.rectangle;
	}

}