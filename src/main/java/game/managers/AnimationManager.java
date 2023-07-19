package game.managers;

import java.util.ArrayList;
import java.util.List;

import data.image.ImageEnums;
import game.objects.enemies.Enemy;
import visual.objects.SpriteAnimation;

public class AnimationManager {

	private static AnimationManager instance = new AnimationManager();
	private List<SpriteAnimation> upperAnimationList = new ArrayList<SpriteAnimation>();
	private List<SpriteAnimation> lowerAnimationList = new ArrayList<SpriteAnimation>();


	public static AnimationManager getInstance() {
		return instance;
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		upperAnimationList = new ArrayList<SpriteAnimation>();
		lowerAnimationList = new ArrayList<SpriteAnimation>();

	}

	private AnimationManager() {
	}

	public void addDestroyedExplosion(SpriteAnimation animation) {
		addUpperAnimation(animation);
	}

	public void addExhaustAnimation(SpriteAnimation animation) {
		System.out.println(animation.getAnimationBounds());
		if (animation != null) {
			this.lowerAnimationList.add(animation);
		}
	}

	public void addUpperAnimation(SpriteAnimation animation) {
		this.upperAnimationList.add(animation);
	}

	public void addLowerAnimation(SpriteAnimation animation) {
		this.lowerAnimationList.add(animation);
	}

	public void createAndAddUpperAnimation(int xCoordinate, int yCoordinate, ImageEnums animationType, boolean infiniteLoop,
			float scale) {
		this.upperAnimationList.add(createAnimation(xCoordinate, yCoordinate, animationType, infiniteLoop, scale));
	}

	public void createAnddAddLowerAnimation(int xCoordinate, int yCoordinate, ImageEnums animationType,
			boolean infiniteLoop, float scale) {
		this.lowerAnimationList.add(createAnimation(xCoordinate, yCoordinate, animationType, infiniteLoop, scale));
	}

	public SpriteAnimation createAnimation(int xCoordinate, int yCoordinate, ImageEnums animationType, boolean infiniteLoop,
			float scale) {
		return new SpriteAnimation(xCoordinate, yCoordinate, animationType, infiniteLoop, scale);
	}

	// Called by EnemyManager when an enemy gets deleted and the belonging
	// animations need to be removed
	public void deleteEnemyAnimations(Enemy enemy) {
		if (enemy.getExhaustAnimation() != null) {
			deleteAnimation(enemy.getExhaustAnimation());
		}
	}

	private void deleteAnimation(SpriteAnimation animation) {
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


	public List<SpriteAnimation> getUpperAnimations() {
		return this.upperAnimationList;
	}

	public List<SpriteAnimation> getLowerAnimations() {
		return this.lowerAnimationList;
	}

	public void updateGameTick() {
		removeInvisibleAnimations();
	}

}