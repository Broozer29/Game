package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class FinalBossPeriodicMissileBarrage implements BossActionable {
    private double lastAttackedTime = GameState.getInstance().getGameSeconds() + 2; //Allow reflecting block spawn to resolve first
    private double attackCooldown = 0.1f;
    private int priority = 1;


    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();

        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            doTheThing(enemy);
            lastAttackedTime = currentTime;
        }
        return true; //return value is ignored
    }



    private void doTheThing(Enemy enemy) {
        Point destination = calculateDestination(enemy);
        MissileManager.getInstance().addExistingMissile(createMissile(enemy, destination));
    }

    private Missile createMissile(Enemy enemy, Point destination) {
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        // The charging up attack animation has finished, create and fire the missile
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate(),
                missileType.getImageType(), 0.8f);


        float movementSpeed = 2f;
        //Create missile movement attributes and create a movement configuration

        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, enemy.getMovementConfiguration().getRotation()
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;
        float damage = enemy.getDamage();

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                damage, missileType.getDeathOrExplosionImageEnum(), isFriendly,
                false, true, true);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        missile.resetMovementPath();
        missile.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(destination); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover
        missile.setOwnerOrCreator(enemy);
        return missile;
    }

    private Point calculateDestination(Enemy enemy) {
        double ownerCenterX = enemy.getCenterXCoordinate();
        double ownerCenterY = enemy.getCenterYCoordinate();

        // Random distance between min and max
        double distance = 2000;

        // Random angle in 360 degrees (full circle for grenade explosion effect)
        double angle = Math.random() * 2 * Math.PI;

        // Calculate destination point using trigonometry
        double targetX = ownerCenterX + distance * Math.cos(angle);
        double targetY = ownerCenterY + distance * Math.sin(angle);

        return new Point(targetX, targetY);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return false; //This behaviour is not supposed to be randomly selected
    }


}
