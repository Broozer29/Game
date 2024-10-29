package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.AlienBomb;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.AttackSpeedModifierEffect;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageModifierEffect;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visuals.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

public class TazerProjectile extends Missile {

	public TazerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
//		this.animation.rotateAnimation(movementConfiguration.getRotation());
//		setAnimation();
//		this.animation.setFrameDelay(1);

		if(missileConfiguration.getDestructionType() != null){
			SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
			destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
			this.destructionAnimation = new SpriteAnimation(destructionAnimation);
		}
	}

	public void missileAction() {
		//Applies an effect but this is handled through the Missile Manager because it's a collision effect.
		//Should refactor so it's behaviour from this class though, doesn't really belong in the manager
	}

	public void handleTazerMissile(GameObject missile, GameObject target){
		if(target instanceof AlienBomb){
			return; //AlienBombs should be immune to this and be ignored
		}
		SpriteConfiguration spriteConfig = new SpriteConfiguration(target.getXCoordinate(), target.getYCoordinate()
				,target.getScale(), ImageEnums.SuperChargedBuff, 0,0,1,
				false,0);
		SpriteAnimationConfiguration spriteAnimConfig = new SpriteAnimationConfiguration(spriteConfig, 2, true);
		SpriteAnimation superChargedAnimation = new SpriteAnimation(spriteAnimConfig);
		DamageModifierEffect damageModifierEffect = null;
		AttackSpeedModifierEffect attackSpeedModifierEffect = null;

		if(target.isFriendly() || target instanceof SpaceShip){
			//Debuff the player or player friendly objects
			attackSpeedModifierEffect = new AttackSpeedModifierEffect(0.75f, 3, null, EffectIdentifiers.TazerAttackSpeedModifier);
			damageModifierEffect = new DamageModifierEffect(0.75f, 3, superChargedAnimation, EffectIdentifiers.TazerDamageModifier);
			missile.setVisible(false);
		} else {
			//Buff the fellow enemies
			attackSpeedModifierEffect = new AttackSpeedModifierEffect(3.0f, 6, null, EffectIdentifiers.TazerAttackSpeedModifier);
			damageModifierEffect = new DamageModifierEffect(1.5f, 6, superChargedAnimation, EffectIdentifiers.TazerDamageModifier);
		}
		target.addEffect(attackSpeedModifierEffect);
		target.addEffect(damageModifierEffect);
	}

}