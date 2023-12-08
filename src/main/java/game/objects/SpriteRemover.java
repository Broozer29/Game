package game.objects;

import game.movement.MovementConfiguration;
import gamedata.DataClass;
import visual.objects.Sprite;

public class SpriteRemover {

	private static SpriteRemover instance = new SpriteRemover();
	
	private SpriteRemover() {
		
	}
	
	public static SpriteRemover getInstance() {
		return instance;
	}
	
	
	public boolean shouldRemoveVisibility(Sprite sprite, MovementConfiguration moveConfig) {
		if(isOutOfBounds(sprite, moveConfig)) {
			return true;
		} else return false;

	}
	
	
	private boolean isOutOfBounds(Sprite sprite, MovementConfiguration moveConfig) {
		switch (moveConfig.getRotation()) {
		case UP:
			if (sprite.getYCoordinate() <= 0) {
				return true;
			}
			break;
		case DOWN:
			if (sprite.getYCoordinate() >= DataClass.getInstance().getWindowHeight()) {
				return true;
			}
			break;
		case LEFT:
			if (sprite.getXCoordinate() < 0) {
				return true;
			}
			break;
		case RIGHT:
			if (sprite.getXCoordinate() > DataClass.getInstance().getWindowWidth()) {
				return true;
			}
			break;
		case LEFT_DOWN:
			if (sprite.getXCoordinate() < 0 || sprite.getYCoordinate() >= DataClass.getInstance().getWindowHeight()) {
				return true;
			}
			break;
		case LEFT_UP:
			if (sprite.getXCoordinate() < 0 || sprite.getYCoordinate() <= 0) {
				return true;
			}
			break;
		case NONE:
			return true;
		case RIGHT_DOWN:
			if (sprite.getXCoordinate() > DataClass.getInstance().getWindowWidth()
					|| sprite.getYCoordinate() >= DataClass.getInstance().getWindowHeight()) {
				return true;
			}
			break;
		case RIGHT_UP:
			if (sprite.getXCoordinate() > DataClass.getInstance().getWindowWidth() || sprite.getYCoordinate() <= 0) {
				return true;
			}
			break;
		}
		return false;
	}
	
}
