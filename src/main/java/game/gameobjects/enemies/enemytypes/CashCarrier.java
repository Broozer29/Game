package game.gameobjects.enemies.enemytypes;

import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.effectimplementations.GainGoldOnDeath;
import game.movement.MovementConfiguration;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gameobjects.enemies.enums.EnemyEnums;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

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
