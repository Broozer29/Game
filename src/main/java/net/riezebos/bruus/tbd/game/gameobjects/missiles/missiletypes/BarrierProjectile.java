package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class BarrierProjectile extends Missile {
    public BarrierProjectile (SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration, movementConfiguration);
//        this.animation.setFrameDelay(2);
//        this.animation.rotateAnimation(movementConfiguration.getRotation());
        this.destroysMissiles = true;

        if(missileConfiguration.getDestructionType() != null){
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);
        }
    }

    public void missileAction() {

    }

}
