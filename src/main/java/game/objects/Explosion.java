package game.objects;

import java.awt.Rectangle;

import image.objects.Animation;
import image.objects.Sprite;

public class Explosion extends Sprite{

	private Animation animation;
	private float damage;
	private boolean dealtDamage;
	
	public Explosion(int x, int y, float scale, Animation animation, float damage) {
		super(x, y, scale);
		this.animation = animation;
		this.damage = damage;
		this.dealtDamage = false;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
	
	public void setScale(float newScale) {
		this.animation.setAnimationScale(newScale);
	}
	
	public Rectangle getAnimationBounds() {
		return animation.getBounds();
	}
	
	public float getDamage() {
		return this.damage;
	}
	
	public void setDealtDamage(boolean dealtDamage) {
		this.dealtDamage = dealtDamage;
	}
	
	public boolean getDealtDamage() {
		return this.dealtDamage;
	}
	
	
}
