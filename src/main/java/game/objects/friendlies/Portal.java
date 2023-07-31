package game.objects.friendlies;

import gamedata.image.ImageEnums;
import visual.objects.SpriteAnimation;

//Used to end the game
public class Portal extends SpriteAnimation{
	
	private boolean spawned = false;
	
	public Portal(int x, int y, ImageEnums imageType, boolean infiniteLoop, float scale) {
		super(x, y, imageType, infiniteLoop, scale);
		setFrameDelay(2);
		this.setTransparancyAlpha(true, 0.0f, 0.01f);
		this.setOriginCoordinates(x, y);
		this.cropAnimation();
		updateCurrentBoardBlock();

	}
	
	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}
	
	public boolean getSpawned() {
		return this.spawned;
	}
	
	
	
}
