
package game.objects;

import java.awt.Image;
import java.util.Random;

import data.image.enums.BGOEnums;
import game.movement.Direction;
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
	
	public void rotateRandomDegrees() {
		rotateImage(selectRandomSpaceTheme());
	}
	
	private Direction selectRandomSpaceTheme() {
		Direction[] enums = Direction.values();
		Random random = new Random();
		Direction randomValue = enums[random.nextInt(enums.length)];
		return randomValue;
	}
	
}