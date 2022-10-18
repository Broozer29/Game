package gameManagers;

import java.util.ArrayList;
import java.util.List;

import gameObjectes.Animation;

public class AnimationManager {

	private static AnimationManager instance = new AnimationManager();
	private List<Animation> animationList = new ArrayList<Animation>();

	private AnimationManager() {

	}

	public static AnimationManager getInstance() {
		return instance;
	}

	public void addAnimation(Animation animation) {
		this.animationList.add(animation);
	}

	public void updateAnimationList(Animation animation) {
		animation.updateFrameCount();
		removeInvisibleAnimations(animation);
	}

	public void removeInvisibleAnimations(Animation animation) {
		if (!animation.isVisible()) {
			this.animationList.remove(animation);
		}
	}

	public List<Animation> getAnimations() {
		return this.animationList;
	}

}
