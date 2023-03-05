package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.objects.enemies.Enemy;
import game.objects.friendlies.SpaceShip;
import image.objects.Animation;

public class AnimationManager {

	private static AnimationManager instance = new AnimationManager();
	private List<Animation> upperAnimationList = new ArrayList<Animation>();
	private List<Animation> lowerAnimationList = new ArrayList<Animation>();


	public static AnimationManager getInstance() {
		return instance;
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		upperAnimationList = new ArrayList<Animation>();
		lowerAnimationList = new ArrayList<Animation>();

	}

	private AnimationManager() {
	}

	public void addDestroyedExplosion(Animation animation) {
		addUpperAnimation(animation);
	}

	public void addExhaustAnimation(Animation animation) {
		if (animation != null) {
			this.lowerAnimationList.add(animation);
		}
	}

	public void addUpperAnimation(Animation animation) {
		this.upperAnimationList.add(animation);
	}

	public void addLowerAnimation(Animation animation) {
		this.lowerAnimationList.add(animation);
	}

	public void createAndAddUpperAnimation(int xCoordinate, int yCoordinate, String animationType, boolean infiniteLoop,
			float scale) {
		this.upperAnimationList.add(createAnimation(xCoordinate, yCoordinate, animationType, infiniteLoop, scale));
	}

	public void createAnddAddLowerAnimation(int xCoordinate, int yCoordinate, String animationType,
			boolean infiniteLoop, float scale) {
		this.lowerAnimationList.add(createAnimation(xCoordinate, yCoordinate, animationType, infiniteLoop, scale));
	}

	public Animation createAnimation(int xCoordinate, int yCoordinate, String animationType, boolean infiniteLoop,
			float scale) {
		return new Animation(xCoordinate, yCoordinate, animationType, infiniteLoop, scale);
	}

	// Called by EnemyManager when an enemy gets deleted and the belonging
	// animations need to be removed
	public void deleteEnemyAnimations(Enemy enemy) {
		if (enemy.getExhaustAnimation() != null) {
			deleteAnimation(enemy.getExhaustAnimation());
		}
	}

	private void deleteAnimation(Animation animation) {
		if (animation == null) {
			return;
		}
		if (upperAnimationList.contains(animation)) {
			upperAnimationList.remove(animation);
		} else if (lowerAnimationList.contains(animation)) {
			lowerAnimationList.remove(animation);
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
	}

}
