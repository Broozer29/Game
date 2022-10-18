package gameManagers;

import java.util.ArrayList;
import java.util.List;

import gameObjectes.Animation;

public class GlobalAnimationStorage {

	private static GlobalAnimationStorage instance = new GlobalAnimationStorage();
	private List<Animation> animationList = new ArrayList<Animation>();

	private GlobalAnimationStorage() {

	}

	public static GlobalAnimationStorage getInstance() {
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
