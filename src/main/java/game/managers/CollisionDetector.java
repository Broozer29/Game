package game.managers;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class CollisionDetector {

	private static CollisionDetector instance = new CollisionDetector(); 
	private int threshold = 600;
	private int boardBlockThreshold = 4;
	
	private CollisionDetector() {
		
	}
	
	public static CollisionDetector getInstance() {
		return instance;
	}
	
	
	public boolean detectCollision(Sprite sprite1, Sprite sprite2) {
		if(isNearby(sprite1, sprite2)) {
			Rectangle r1 = sprite1.getBounds();
			Rectangle r2 = sprite2.getBounds();
			if(r1.intersects(r2)) {
				if(checkPixelCollision(sprite1, sprite2)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	private boolean isWithinBoardBlockThreshold(Sprite sprite1, Sprite sprite2) {
		// This causes all other "updatecurrentBoardBlocks" to be redundant
		sprite1.updateCurrentBoardBlock();
		sprite2.updateCurrentBoardBlock();

		int blockDifference = Math.abs(sprite1.getCurrentBoardBlock() - sprite2.getCurrentBoardBlock());
		return blockDifference <= boardBlockThreshold;
	}

	private boolean isNearby(Sprite sprite1, Sprite sprite2) {
		if (!isWithinBoardBlockThreshold(sprite1, sprite2)) {
			return false;
		}

		double distance = Math.hypot(sprite1.getXCoordinate() - sprite2.getXCoordinate(),
				sprite1.getYCoordinate() - sprite2.getYCoordinate());
		return distance < threshold;
	}

	private boolean checkPixelCollision(Sprite sprite1, Sprite sprite2) {
		BufferedImage img1 = null;
		BufferedImage img2 = null;
		if (sprite1 instanceof SpriteAnimation) {
			img1 = ((SpriteAnimation) sprite1).getCurrentFrameImage(false);
		} else {
			img1 = sprite1.getImage();
		}

		if (sprite2 instanceof SpriteAnimation) {
			img2 = ((SpriteAnimation) sprite2).getCurrentFrameImage(false);
		} else {
			img2 = sprite2.getImage();
		}

		if (img1 != null || img2 != null) {
			int xStart = Math.max(sprite1.getXCoordinate(), sprite2.getXCoordinate());
			int yStart = Math.max(sprite1.getYCoordinate(), sprite2.getYCoordinate());
			int xEnd = Math.min(sprite1.getXCoordinate() + img1.getWidth(), sprite2.getXCoordinate() + img2.getWidth());
			int yEnd = Math.min(sprite1.getYCoordinate() + img1.getHeight(),
					sprite2.getYCoordinate() + img2.getHeight());

			for (int y = yStart; y < yEnd; y++) {
				for (int x = xStart; x < xEnd; x++) {
					int pixel1 = img1.getRGB(x - sprite1.getXCoordinate(), y - sprite1.getYCoordinate());
					int alpha1 = (pixel1 >> 24) & 0xff;

					int pixel2 = img2.getRGB(x - sprite2.getXCoordinate(), y - sprite2.getYCoordinate());
					int alpha2 = (pixel2 >> 24) & 0xff;

					if (alpha1 != 0 && alpha2 != 0) {
						return true; // Collision detected
					}
				}
			}
			return false; // No collision detected
		} else {
			return true; //Invisible images, cannot detect pixels because there are none
		}
	}
}
