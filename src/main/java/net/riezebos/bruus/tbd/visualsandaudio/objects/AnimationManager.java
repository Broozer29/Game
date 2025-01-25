package net.riezebos.bruus.tbd.visualsandaudio.objects;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {

	private static AnimationManager instance = new AnimationManager();
	private List<SpriteAnimation> upperAnimationList = new ArrayList<>();
	private List<SpriteAnimation> lowerAnimationList = new ArrayList<>();

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
		recenterAnimations(); //Required for cropped animations that have different sizes, this might lead to weird interactions ir origin coords are used when the shouldnt
	}

	// Use for animations that have different sizes
	private void recenterAnimations() {
		for (SpriteAnimation anim : upperAnimationList) {
			if (anim.isVisible() && anim.getOriginXCoordinate() != 0 && anim.getOriginYCoordinate() != 0) {
					anim.setCenterCoordinates(anim.getOriginXCoordinate(), anim.getOriginYCoordinate());
				}

		}
		for (SpriteAnimation anim : lowerAnimationList) {
			if (anim.isVisible() && anim.getOriginXCoordinate() != 0 && anim.getOriginYCoordinate() != 0) {
					anim.setCenterCoordinates(anim.getOriginXCoordinate(), anim.getOriginYCoordinate());
				}

		}
	}

	public void playLevelUpAnimation(GameObject gameObject){
		SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
		spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
		spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
		spriteConfiguration.setScale(1);
		spriteConfiguration.setImageType(ImageEnums.LevelUpAnimation);

		SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, false);
		SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
		spriteAnimation.setAnimationScale(0.3f);
		if(gameObject instanceof SpaceShip) {
			PlayerManager.getInstance().getSpaceship().addPlayerFollowingAnimation(spriteAnimation);
			addUpperAnimation(spriteAnimation);
		}
	}

}