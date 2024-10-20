package game.gameobjects.missiles.specialAttacks;

import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class ElectroShred extends SpecialAttack {
    public ElectroShred (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("ElectroShred");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        setOnHitInterval(0.3f);
    }
}