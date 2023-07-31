package game.objects;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class Explosion extends Sprite {

	private SpriteAnimation animation;
	private float damage;
	private boolean dealtDamage;
	private boolean friendly;
	private boolean centeredAroundPlayer = false;
	private List<Sprite> skipCollision = new ArrayList<Sprite>();

	public Explosion(int x, int y, float scale, SpriteAnimation animation, float damage, boolean friendly) {
		super(x, y, scale);
		this.friendly = friendly;
		this.animation = animation;
		this.damage = damage;
		this.dealtDamage = false;
	}

	public SpriteAnimation getAnimation() {
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

	public boolean isFriendly() {
		return friendly;
	}

	public boolean centeredAroundPlayer() {
		return centeredAroundPlayer;
	}

	public void setCenteredAroundPlayer(Boolean bool) {
		this.centeredAroundPlayer = bool;
	}

	public void addCollidedSprite(Sprite sprite) {
		if (!this.skipCollision.contains(sprite)) {
			this.skipCollision.add(sprite);
		}
	}
	
	public List<Sprite> getCollidedSprites() {
		return this.skipCollision;
	}

	public boolean shouldDealDamage() {
		if (this.animation.getCurrentFrame() < 5 || this.damage == 0) {
			return true;
		} else
			return false;
	}

}