package game.gameobjects.missiles.missiletypes;

import game.movement.MovementConfiguration;
import game.gameobjects.missiles.Missile;
import game.gameobjects.missiles.MissileConfiguration;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

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
