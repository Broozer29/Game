package game.movement;

import game.objects.GameObject;
import game.util.OutOfBoundsCalculator;

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
