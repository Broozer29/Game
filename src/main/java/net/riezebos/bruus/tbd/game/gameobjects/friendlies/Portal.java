package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;

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
