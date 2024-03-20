package game.objects.player.specialAttacks;

import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class ElectroShred extends SpecialAttack {
    public ElectroShred (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
//        this.xOffset = -(this.getWidth() / 2);
//        this.yOffset = -(this.getHeight() / 2);
//        this.animation.addXOffset(-(animation.getWidth() / 2));
//        this.animation.addYOffset(-(animation.getHeight() / 2));
        this.setObjectType("ElectroShred");
    }
}