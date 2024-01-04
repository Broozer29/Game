package game.objects.friendlies;

import game.objects.GameObject;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

//Used to end the game
public class Portal extends GameObject {
	
	private boolean spawned = false;
	
	public Portal(SpriteAnimationConfiguration spriteAnimationConfiguration){
		super(spriteAnimationConfiguration);
		this.animation.setOriginCoordinates(spriteAnimationConfiguration.getSpriteConfiguration().getxCoordinate(),
				spriteAnimationConfiguration.getSpriteConfiguration().getyCoordinate());
		this.animation.cropAnimation();
	}
	
	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}
	
	public boolean getSpawned() {
		return this.spawned;
	}



}
