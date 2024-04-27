package game.objects.missiles.missiletypes;

import game.movement.MovementConfiguration;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class BarrierProjectile extends Missile {
    public BarrierProjectile (SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration, movementConfiguration);
//        this.animation.setFrameDelay(2);
//        this.animation.rotateAnimation(movementConfiguration.getRotation());
        this.destroysMissiles = true;

    }

    public void missileAction() {

    }

}
