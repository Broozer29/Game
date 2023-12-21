package game.objects.friendlies.spaceship.specialAttacks;

import java.util.ArrayList;
import java.util.List;

import game.objects.GameObject;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class SpecialAttack extends GameObject {
	protected List<Missile> specialAttackMissiles = new ArrayList<Missile>();
	protected boolean centeredAroundPlayer = false;
	

	public SpecialAttack(SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteAnimationConfiguration);
		this.damage = missileConfiguration.getDamage();
		this.friendly = missileConfiguration.isFriendly();
		this.allowedToDealDamage = missileConfiguration.isAllowedToDealDamage();
		this.updateCurrentBoardBlock();
	}

	
	public SpriteAnimation getAnimation() {
		return this.animation;
	}
	
	public void setScale(float newScale) {
		this.animation.setAnimationScale(newScale);
	}
	
	public boolean centeredAroundPlayer() {
		return centeredAroundPlayer;
	}
	
	public void setCenteredAroundPlayer(Boolean bool) {
		this.centeredAroundPlayer = bool;
	}
	
	public void moveSpecialAttackMissiles() {
		for(Missile missile : specialAttackMissiles) {
			if(missile.isVisible()) {
				missile.move();
				missile.updateCurrentBoardBlock();
			}
		}
	}
	
	public List<Missile> getSpecialAttackMissiles() {
		return this.specialAttackMissiles;
	}
}