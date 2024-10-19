package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.GainGoldOnDeath;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

public class CashCarrier extends Enemy {
    public CashCarrier (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale * 1.5f);

        //Gold gain should be 0, as this effect is cosmetic ONLY!
        GainGoldOnDeath goldOnDeathEffect = new GainGoldOnDeath(EffectActivationTypes.OnObjectDeath, EffectIdentifiers.CashCarrierGoldGain, 0);
        this.addEffect(goldOnDeathEffect);
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
    }

    public void fireAction(){
        //Probably do nothing?
    }

}
