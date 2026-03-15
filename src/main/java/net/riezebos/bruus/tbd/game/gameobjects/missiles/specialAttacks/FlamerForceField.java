package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.ArmorCalculator;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FlamerForceField extends SpecialAttack {
    public FlamerForceField(SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("FlamerForceField");
        this.allowRepeatedDamage = false;
        this.destroysMissiles = false;
        this.appliesOnHitEffects = false;
        super.internalTickCooldown = 0.15f;
        super.showDamage = false;
    }

    @Override
    public void dealDamageToGameObject(GameObject target){
        if(target.getCurrentHitpoints() <= 0.000f || !target.isVisible()){
            return; //if it's dead, skip this operation
        }
        for (EffectInterface effectInterface : effectsToApply) {
            target.addEffect(effectInterface);
        }

        if(getDamage() > 0.000f) {
            float damage = ArmorCalculator.calculateDamage(getDamage(), target);
            target.takeDamage(damage);
        }

        if(target instanceof SpaceShip spaceShip){
            CollisionInfo collisionInfo = new CollisionInfo(true,
                    new Point(this.getCenterXCoordinate(), this.getCenterYCoordinate()));
            spaceShip.applyKnockback(collisionInfo, 15);
        }
    }
}