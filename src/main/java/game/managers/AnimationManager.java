package game.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import image.objects.Animation;

public class AnimationManager {

	private static AnimationManager instance = new AnimationManager();
	private List<Animation> upperAnimationList = new ArrayList<Animation>();
	private List<Animation> lowerAnimationList = new ArrayList<Animation>();

	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private Animation playerEngineAnimation = null;
	private int engineXCoordinate = 0;
	private int engineYCoordinate = 0;

	public static AnimationManager getInstance() {
		return instance;
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		upperAnimationList = new ArrayList<Animation>();
		lowerAnimationList = new ArrayList<Animation>();
		playerEngineAnimation = new Animation(friendlyManager.getSpaceship().getXCoordinate(),
				friendlyManager.getSpaceship().getYCoordinate(), "Player Engine");
		lowerAnimationList.add(playerEngineAnimation);
		engineXCoordinate = 0;
		engineYCoordinate = 0;
	}

	private AnimationManager() {
		playerEngineAnimation = new Animation(friendlyManager.getSpaceship().getXCoordinate(),
				friendlyManager.getSpaceship().getYCoordinate(), "Player Engine");
		lowerAnimationList.add(playerEngineAnimation);
	}

	// Called every gameloop, sets the engine animation below the spaceship.
	private void renderPlayerEngine() {
		engineXCoordinate = friendlyManager.getSpaceship().getXCoordinate()
				- (playerEngineAnimation.getWidth() / 2 + 5);
		engineYCoordinate = friendlyManager.getSpaceship().getYCoordinate();

		playerEngineAnimation.setX(engineXCoordinate);
		playerEngineAnimation.setY(engineYCoordinate);
		
		if (playerEngineAnimation.getFrame() >= playerEngineAnimation.getTotalFrames()) {
			playerEngineAnimation.refreshAnimation(engineXCoordinate, engineYCoordinate);
		}

	}

	public void addDestroyedExplosion(int xCoordinate, int yCoordinate) {
		Random random = new Random();
		int result = random.nextInt(4 - 1) + 1;
		String explosionType = null;

		switch (result) {
		case (1):
			explosionType = "Destroyed Explosion";
			break;
		case (2):
			explosionType = "Destroyed Explosion Right";
			break;
		case (3):
			explosionType = "Destroyed Explosion Left";
			break;
		case (4):
			explosionType = "Destroyed Explosion Reverse";
			break;
		}

		if (explosionType == null) {
			explosionType = "Destroyed Explosion";
		}
		addUpperAnimation(xCoordinate, yCoordinate, explosionType);
	}

	public void addUpperAnimation(int xCoordinate, int yCoordinate, String animationType) {
		this.upperAnimationList.add(createAnimation(xCoordinate, yCoordinate, animationType));
	}

	public void addLowerAnimation(int xCoordinate, int yCoordinate, String animationType) {
		this.lowerAnimationList.add(createAnimation(xCoordinate, yCoordinate, animationType));
	}

	private Animation createAnimation(int xCoordinate, int yCoordinate, String animationType) {
		return new Animation(xCoordinate, yCoordinate, animationType);
	}

	private void removeInvisibleAnimations() {
		for (int i = 0; i < upperAnimationList.size(); i++) {
			if (!upperAnimationList.get(i).isVisible()) {
				this.upperAnimationList.remove(upperAnimationList.get(i));
			}
		}
		for (int i = 0; i < lowerAnimationList.size(); i++) {
			if (!lowerAnimationList.get(i).isVisible()) {
				this.lowerAnimationList.remove(lowerAnimationList.get(i));
			}
		}

	}

	public List<Animation> getUpperAnimations() {
		return this.upperAnimationList;
	}

	public List<Animation> getLowerAnimations() {
		return this.lowerAnimationList;
	}

	public void updateGameTick() {
		renderPlayerEngine();
		removeInvisibleAnimations();
		
	}

}
