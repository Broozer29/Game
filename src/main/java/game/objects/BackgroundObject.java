
package game.objects;

import java.awt.Image;

import data.image.enums.BGOEnums;
import data.image.enums.ImageEnums;
import image.objects.Sprite;

public class BackgroundObject extends Sprite {

	private BGOEnums bgoType;
	public BackgroundObject(int x, int y, Image planetImage, float scale, BGOEnums bgoType) {
		super(x, y, scale);
		setImage(planetImage);
		this.bgoType = bgoType;
	}
	
	public void setNewPlanetImage(Image image) {
		setImage(image);
	}
	
	public BGOEnums getBGOtype() {
		return this.bgoType;
	}
	
	public void BGOEnums(BGOEnums bgoType) {
		this.bgoType = bgoType;
	}
	
	
	
}
