package game.objects.missiles.specialAttacks;

import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class ElectroShred extends SpecialAttack {
    public ElectroShred (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("ElectroShred");
        this.allowRepeatedDamage = true;
        this.allowOnHitEffects = true;
        this.onHitInterval = 0.3f;
    }
}