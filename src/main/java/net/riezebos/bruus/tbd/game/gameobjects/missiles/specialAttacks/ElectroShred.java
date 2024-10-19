package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class ElectroShred extends SpecialAttack {
    public ElectroShred (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("ElectroShred");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        setOnHitInterval(0.3f);
    }
}