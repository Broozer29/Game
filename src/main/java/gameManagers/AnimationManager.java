package gameManagers;

import java.util.ArrayList;
import java.util.List;

import Data.DataClass;
import gameObjectes.Animation;

public class AnimationManager {

	private static AnimationManager instance = new AnimationManager();
	private List<Animation> upperAnimationList = new ArrayList<Animation>();
	private List<Animation> lowerAnimationList = new ArrayList<Animation>();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private Animation playerEngineAnimation = null;

	public static AnimationManager getInstance() {
		return instance;
	}

	private AnimationManager() {
		playerEngineAnimation = new Animation(friendlyManager.getSpaceship().getXCoordinate(),
				friendlyManager.getSpaceship().getYCoordinate(), "Player Engine");
		lowerAnimationList.add(playerEngineAnimation);
	}

	// Called every gameloop, sets the engine animation below the spaceship.
	public void renderPlayerEngine() {
		int engineNewXCoordinate = friendlyManager.getSpaceship().getXCoordinate() - (playerEngineAnimation.getWidth()/2 + 5);
		int engineNewYCoordinate = friendlyManager.getSpaceship().getYCoordinate();

		playerEngineAnimation.setX(engineNewXCoordinate);
		playerEngineAnimation.setY(engineNewYCoordinate);

		if (playerEngineAnimation.getCurrentFrameCount() >= playerEngineAnimation.getFrameCount()) {
			playerEngineAnimation.refreshAnimation(engineNewXCoordinate, engineNewYCoordinate);
		}

	}

	public void addUpperAnimation(Animation animation) {
		this.upperAnimationList.add(animation);
	}

	public void addLowerAnimation(Animation animation) {
		this.lowerAnimationList.add(animation);
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
