package game.managers;

import java.util.ArrayList;
import java.util.List;

import data.DataClass;
import image.objects.Animation;

public class AnimationManager {

	private static AnimationManager instance = new AnimationManager();
	private List<Animation> upperAnimationList = new ArrayList<Animation>();
	private List<Animation> lowerAnimationList = new ArrayList<Animation>();

	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private Animation playerEngineAnimation = null;
	private int engineXCoordinate;
	private int engineYCoordinate;

	public static AnimationManager getInstance() {
		return instance;
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

		if (playerEngineAnimation.getCurrentFrameCount() >= playerEngineAnimation.getFrameCount()) {
			playerEngineAnimation.refreshAnimation(engineXCoordinate, engineYCoordinate);
		}

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

	public void updateAnimationList() {
		for (int i = 0; i < upperAnimationList.size(); i++) {
			if (upperAnimationList.get(i).isVisible()) {
				this.upperAnimationList.get(i).updateFrameCount();
			}
		}
		for (int i = 0; i < lowerAnimationList.size(); i++) {
			if (lowerAnimationList.get(i).isVisible()) {
				this.lowerAnimationList.get(i).updateFrameCount();
			}
		}

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
		removeInvisibleAnimations();
		updateAnimationList();
		renderPlayerEngine();
	}

}
