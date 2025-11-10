package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.minibosses;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class ShurikenMiniBoss extends Enemy {
    public ShurikenMiniBoss(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        this.setAllowedVisualsToRotate(false);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteAnimationConfigurationion.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(3f);
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;

        if(this.movementConfiguration.getPathFinder() instanceof BouncingPathFinder bouncingPathFinder){
            bouncingPathFinder.setMaxBounces(100);
            bouncingPathFinder.setUseCenteredCoordinatesInstead(true);
        }
    }


    public void fireAction () {
        //idk of deze gedrag moet hebben
    }
}
