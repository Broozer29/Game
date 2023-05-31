package game.objects.friendlies;

import image.objects.SpriteAnimation;
import image.objects.Sprite;

public class FriendlyObject extends Sprite {
	
	private SpriteAnimation animation;
	private int damage;
	
	public FriendlyObject(int x, int y, float scale) {
		super(x, y, scale);
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public void setAnimation(SpriteAnimation animation) {
		this.animation = animation;
	}
	
	public SpriteAnimation getAnimation() {
		return this.animation;
	}

}
