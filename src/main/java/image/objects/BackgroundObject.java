package image.objects;

import java.awt.Image;

public class BackgroundObject extends Sprite {

	public BackgroundObject(int x, int y, Image planetImage, float scale) {
		super(x, y, scale);
		setImage(planetImage);
	}
	
	public void setNewPlanetImage(Image image) {
		setImage(image);
	}
	
}
