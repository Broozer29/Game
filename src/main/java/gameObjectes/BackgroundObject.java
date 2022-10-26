package gameObjectes;

import java.awt.Image;

public class BackgroundObject extends Sprite {

	public BackgroundObject(int x, int y, Image planetImage) {
		super(x, y);
		setImage(planetImage);
	}
	
	public void setNewPlanetImage(Image image) {
		setImage(image);
	}
	
}
