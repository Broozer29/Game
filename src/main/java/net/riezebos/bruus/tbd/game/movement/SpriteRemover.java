package net.riezebos.bruus.tbd.game.movement;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.util.OutOfBoundsCalculator;

public class SpriteRemover {

	private static SpriteRemover instance = new SpriteRemover();
	
	private SpriteRemover() {
		
	}
	
	public static SpriteRemover getInstance() {
		return instance;
	}
	
	
	public boolean shouldRemoveVisibility(GameObject gameObject) {
		if(OutOfBoundsCalculator.isOutOfBounds(gameObject)) {
			return true;
		} else return false;

	}

}
