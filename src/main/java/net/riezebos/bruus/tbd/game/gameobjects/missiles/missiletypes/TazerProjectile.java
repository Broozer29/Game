package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.AttackSpeedModifierEffect;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageModifierEffect;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class TazerProjectile extends Missile {

    private int amountOfCollisions = 0;
    private int maximumAmountOfCollisions = 2;

    public TazerProjectile (SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
//		this.animation.rotateAnimation(movementConfiguration.getRotation());
//		setAnimation();
//		this.animation.setFrameDelay(1);

        if (missileConfiguration.getDestructionType() != null) {
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);
        }
    }

    public void missileAction () {
    }

    public void applyTazerMissileEffect (GameObject target) {
        if (!target.isHasAttack() || this.hasCollidedBeforeWith(target)) {
            return;
        }
        SpriteConfiguration spriteConfig = new SpriteConfiguration(target.getXCoordinate(), target.getYCoordinate()
                , target.getScale(), ImageEnums.SuperChargedBuff, 0, 0, 1,
                false, 0);
        SpriteAnimationConfiguration spriteAnimConfig = new SpriteAnimationConfiguration(spriteConfig, 2, true);
        SpriteAnimation superChargedAnimation = new SpriteAnimation(spriteAnimConfig);
        DamageModifierEffect damageModifierEffect = null;
        AttackSpeedModifierEffect attackSpeedModifierEffect = null;

        if (target.isFriendly()) {
            //Debuff the player or player friendly objects
            attackSpeedModifierEffect = new AttackSpeedModifierEffect(-0.35f, 3, null, EffectIdentifiers.TazerAttackSpeedModifier);
            damageModifierEffect = new DamageModifierEffect(-0.25f, 3, superChargedAnimation, EffectIdentifiers.TazerDamageModifier);
            this.setVisible(false);
        } else {
            //Buff the fellow enemies
            attackSpeedModifierEffect = new AttackSpeedModifierEffect(0.25f, 5, null, EffectIdentifiers.TazerAttackSpeedModifier);
            damageModifierEffect = new DamageModifierEffect(0.25f, 6, superChargedAnimation, EffectIdentifiers.TazerDamageModifier);
        }


        if (attackSpeedModifierEffect != null) {
            target.addEffect(attackSpeedModifierEffect);
        }
        if (damageModifierEffect != null) {
            target.addEffect(damageModifierEffect);
        }

        this.addCollidedObject(target);
        amountOfCollisions++;
        if (amountOfCollisions >= maximumAmountOfCollisions) {
            this.setVisible(false);
        }
    }

}