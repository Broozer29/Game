package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class EnemyProtossBeacon extends Enemy {
    private boolean canAttractProtoss;

    public EnemyProtossBeacon(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteAnimationConfigurationion.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.allowedVisualsToRotate = false;
        this.detonateOnCollision = false;
        this.knockbackStrength = 9;
        this.allowedToFire = false;
        canAttractProtoss = true;
    }

    public void fireAction() {

        if(this.ownerOrCreator != null && this.ownerOrCreator.getCurrentHitpoints() <= 0){
            this.takeDamage(99999);
        }

        if (this.movementConfiguration.getPathFinder() instanceof DestinationPathFinder) {
            if (this.movementConfiguration.getCurrentPath() != null && this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty()) {
                this.setAllowedToMove(true);
                this.getMovementConfiguration().setPathFinder(new DestinationPathFinder());

                SpaceShip closestSpaceShip = PlayerManager.getInstance().getClosestSpaceShip(this.getCenterXCoordinate(), this.getCenterYCoordinate());
                this.getMovementConfiguration().setDestination(new Point(
                        closestSpaceShip.getCenterXCoordinate() - (this.getWidth() / 2),
                        closestSpaceShip.getCenterYCoordinate() - (this.getHeight() / 2)
                ));
            }
        }
    }

    public boolean isCanAttractProtoss() {
        return canAttractProtoss;
    }
}
