package game.objects.missiles.missiletypes;

import com.badlogic.gdx.Game;
import game.movement.MovementConfiguration;
import game.objects.GameObject;
import game.objects.enemies.Enemy;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class TazerProjectile extends Missile {

	public TazerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
//		this.animation.rotateAnimation(movementConfiguration.getRotation());
//		setAnimation();
//		this.animation.setFrameDelay(1);
	}

	private boolean bool = true;
	public void missileAction() {
//		if(bool) {
//			this.animation.rotateAnimation(this.rotationAngle + 10, false);
//			if(this.rotationAngle > 360){
//				this.rotationAngle = 0;
//			}
//			bool = false;
//		}
	}

	public void applyDebuffToObject(GameObject gameObject){
		//This should be a debuff, not permanent

	}

	public void applyBuffToObject(GameObject gameObject){
		//This should be a buff effect, not permanent


		float damage = gameObject.getDamage();
		gameObject.setDamage(damage * 1.5f); //50% damage bonus

		if(gameObject.isHasAttack() && gameObject.getAttackSpeed() > 0){
			int newAttackSpeed = Math.round(gameObject.getAttackSpeed() / 2f);
			gameObject.setAttackSpeed(newAttackSpeed);
		}
	}

}