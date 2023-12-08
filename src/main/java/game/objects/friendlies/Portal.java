package game.objects.friendlies;

import game.objects.GameObject;
import gamedata.image.ImageEnums;
import visual.objects.SpriteAnimation;

//Used to end the game
public class Portal extends GameObject {
	
	private boolean spawned = false;
	
	public Portal(int x, int y, ImageEnums imageType, boolean infiniteLoop, float scale) {
		super(x,y, scale);
		this.animation = new SpriteAnimation(x,y,imageType, true, scale);
		this.animation.setFrameDelay(2);
		this.animation.setOriginCoordinates(x, y);
		this.animation.cropAnimation();
		updateCurrentBoardBlock();

	}
	
	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}
	
	public boolean getSpawned() {
		return this.spawned;
	}
	
	
	
}
