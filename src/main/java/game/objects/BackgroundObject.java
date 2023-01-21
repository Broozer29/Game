
package game.objects;

import java.awt.Image;

import image.objects.Sprite;

public class BackgroundObject extends Sprite {

	private String bgoType;
	public BackgroundObject(int x, int y, Image planetImage, float scale, String bgoType) {
		super(x, y, scale);
		setImage(planetImage);
		this.bgoType = bgoType;
	}
	
	public void setNewPlanetImage(Image image) {
		setImage(image);
	}
	
	public String getBGOtype() {
		return this.bgoType;
	}
	
	public void setBGOtype(String bgoType) {
		this.bgoType = bgoType;
	}
	
	
	
}
