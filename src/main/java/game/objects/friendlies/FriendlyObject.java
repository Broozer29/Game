package game.objects.friendlies;

import image.objects.Animation;
import image.objects.Sprite;

public class FriendlyObject extends Sprite {
	
	private Animation animation;
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
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}

}
