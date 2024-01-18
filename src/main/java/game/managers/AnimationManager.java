package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.objects.enemies.Enemy;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

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
		for(SpriteAnimation animation : upperAnimationList){
			animation.setVisible(false);
		}

		for(SpriteAnimation animation : lowerAnimationList){
			animation.setVisible(false);
		}

		removeInvisibleAnimations();

	}

	private AnimationManager() {
	}

	public void addDestroyedExplosion(SpriteAnimation animation) {
		addUpperAnimation(animation);
	}

	public void addExhaustAnimation(SpriteAnimation animation) {
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

	public void createAndAddUpperAnimation(int xCoordinate, int yCoordinate, ImageEnums animationType,
			boolean infiniteLoop, float scale) {
		this.upperAnimationList.add(createAnimation(xCoordinate, yCoordinate, animationType, infiniteLoop, scale));
	}

	public void createAnddAddLowerAnimation(int xCoordinate, int yCoordinate, ImageEnums animationType,
			boolean infiniteLoop, float scale) {
		this.lowerAnimationList.add(createAnimation(xCoordinate, yCoordinate, animationType, infiniteLoop, scale));
	}

	public SpriteAnimation createAnimation(int xCoordinate, int yCoordinate, ImageEnums animationType,
			boolean infiniteLoop, float scale) {

		SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
		spriteConfiguration.setxCoordinate(xCoordinate);
		spriteConfiguration.setyCoordinate(yCoordinate);
		spriteConfiguration.setScale(scale);
		spriteConfiguration.setImageType(animationType);
		SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, infiniteLoop);
		return new SpriteAnimation(spriteAnimationConfiguration);
	}

	// Called by EnemyManager when an enemy gets deleted and the belonging
	// animations need to be removed
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
		recenterAnimations();
	}

	// Use for animations that have different sizes
	private void recenterAnimations() {
		for (SpriteAnimation anim : upperAnimationList) {
			if (anim.isVisible()) {
				if (anim.getOriginXCoordinate() != 0 && anim.getOriginYCoordinate() != 0) {
					anim.setCenterCoordinates(anim.getOriginXCoordinate(), anim.getOriginYCoordinate());
				}
			}
		}
		for (SpriteAnimation anim : lowerAnimationList) {
			if (anim.isVisible()) {
				if (anim.getOriginXCoordinate() != 0 && anim.getOriginYCoordinate() != 0) {
					anim.setCenterCoordinates(anim.getOriginXCoordinate(), anim.getOriginYCoordinate());
				}
			}
		}
	}

}