package Main;

import gameObjectes.Sprite;
import javafx.scene.image.Image;

public class MenuTile extends Sprite {

	private String tileType;
	private Image image;

	public MenuTile(String tileType, int xCoordinate, int yCoordinate) {
		super(xCoordinate, yCoordinate);
		this.tileType = tileType;
		initTile();
	}

	private void initTile() {
		if (this.tileType.equals("StartGame")) {
			loadImage("src/resources/images/testimage.jpg");
		} else if (this.tileType.equals("Pointer")) {
			loadImage("src/resources/images/laserbeam.png");
		} else if (this.tileType.equals("SelectUser")) {
			loadImage("src/resources/images/testimage.jpg");
		}
		getImageDimensions();
	}

	public void menuTileAction() {
		if (this.tileType.equals("StartGame")) {
			System.out.println("StartGame");
		} else if (this.tileType.equals("SelectUser")) {
			System.out.println("SelectUser");
		}
	}

	public String getTileType() {
		return tileType;
	}

	public void setTileType(String tileType) {
		this.tileType = tileType;
	}

}
