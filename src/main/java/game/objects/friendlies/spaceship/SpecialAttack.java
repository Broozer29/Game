package game.objects.friendlies.spaceship;

import java.util.ArrayList;
import java.util.List;

import game.objects.missiles.Missile;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class SpecialAttack extends Sprite {
	protected SpriteAnimation animation;
	protected float damage;
	protected boolean friendly;
	protected List<Missile> specialAttackMissiles = new ArrayList<Missile>();
	

	public SpecialAttack(int x, int y, float scale, SpriteAnimation animation, float damage, boolean friendly) {
		super(x, y, scale);
		this.animation = animation;
		this.damage = damage;
		this.friendly = friendly;
	}

	
	public SpriteAnimation getAnimation() {
		return this.animation;
	}
	
	public void setScale(float newScale) {
		this.animation.setAnimationScale(newScale);
	}
	
	public float getDamage() {
		return this.damage;
	}
	
	public boolean isFriendly() {
		return friendly;
	}
	
	public void moveSpecialAttackMissiles() {
		for(Missile missile : specialAttackMissiles) {
			if(missile.isVisible()) {
				missile.move();
			}
		}
	}
	
	public List<Missile> getSpecialAttackMissiles() {
		return this.specialAttackMissiles;
	}
}