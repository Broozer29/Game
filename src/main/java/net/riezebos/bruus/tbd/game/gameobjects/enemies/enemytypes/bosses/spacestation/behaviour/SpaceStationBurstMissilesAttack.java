package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.spacestation.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.LinkedList;
import java.util.List;

public class SpaceStationBurstMissilesAttack implements BossActionable {

    private double attackCooldown = 3f;
    private int priority = 1;
    private double lastAttackedTime = 0;
    private List<SpriteAnimation> chargingAnimations = new LinkedList<>();
    private boolean allowedToFireBurst = false;
    private int burstShotsFired = 0;
    private double lastSingleShotAttackTime = 0;
    private double intermittenAttackCooldown = 0.2;
    private int amountOfShotsPerBurst = 12;



    private static final Point[] BASE_ORIGIN_POINTS = {
            new Point(209, 236),
            new Point(695, 332),
            new Point(367, 708)
    };

    public SpaceStationBurstMissilesAttack (){
        createChargingAnimations();
    }

    private void createChargingAnimations () {
        for (int i = 0; i < BASE_ORIGIN_POINTS.length; i++) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setScale(1.5f);
            spriteConfiguration.setxCoordinate(BASE_ORIGIN_POINTS[i].getX());
            spriteConfiguration.setyCoordinate(BASE_ORIGIN_POINTS[i].getY());
            spriteConfiguration.setImageType(ImageEnums.Charging);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, false);
            SpriteAnimation chargingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            chargingAnimations.add(chargingAnimation);
        }
    }

    private void updateChargingAnimationLocations (Enemy enemy, int angleDegrees) {
        for (int i = 0; i < BASE_ORIGIN_POINTS.length; i++) {
            Point newPoint = getCoordinatesBasedOnTheAngle(BASE_ORIGIN_POINTS[i], enemy, angleDegrees);
            chargingAnimations.get(i).setCenterCoordinates(newPoint.getX(), newPoint.getY());
        }
    }

    private Point getCoordinatesBasedOnTheAngle(Point basePoint, Enemy enemy, int angleDegrees){
        double angleRadians = Math.toRadians(angleDegrees);
        int scaledX = (int) (basePoint.getX() * enemy.getScale());
        int scaledY = (int) (basePoint.getY() * enemy.getScale());

        // Calculate the actual screen position of the point (relative to enemy's top-left corner)
        int relativeX = enemy.getXCoordinate() + scaledX;
        int relativeY = enemy.getYCoordinate() + scaledY;

        // Apply rotation around the enemy's center
        int newX = (int) (enemy.getCenterXCoordinate() +
                (relativeX - enemy.getCenterXCoordinate()) * Math.cos(angleRadians) -
                (relativeY - enemy.getCenterYCoordinate()) * Math.sin(angleRadians));

        int newY = (int) (enemy.getCenterYCoordinate() +
                (relativeX - enemy.getCenterXCoordinate()) * Math.sin(angleRadians) +
                (relativeY - enemy.getCenterYCoordinate()) * Math.cos(angleRadians));

        return new Point(newX, newY);
    }

    private int counterForRotationAngle = 0;
    private int getCurrentRotationAngle (Enemy enemy) {
        if (enemy.getAnimation().getCurrentFrame() == 120) {
            counterForRotationAngle += 1;
        }

        if (counterForRotationAngle > 3) {
            counterForRotationAngle = 0;
        }

        return enemy.getAnimation().getCurrentFrame() - 1 + (counterForRotationAngle * 120);
    }



    @Override
    public boolean activateBehaviour (Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        int currentRotationAngle = getCurrentRotationAngle(enemy);


        //Charging up/preparing to attack phase
        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateChargingAnimationLocations(enemy, currentRotationAngle);
            if (!chargingAnimations.get(0).isPlaying() && !allowedToFireBurst) {
                for (SpriteAnimation spriteanimation : chargingAnimations) {
                    spriteanimation.refreshAnimation();
                    AnimationManager.getInstance().addUpperAnimation(spriteanimation);
                }
                enemy.setAttacking(true);
                burstShotsFired = 0;
            }

            if (chargingAnimations.get(0).getCurrentFrame() >= chargingAnimations.get(0).getTotalFrames() - 1) {
                allowedToFireBurst = true;
            }
        }


        //Attacking phase
        if (allowedToFireBurst) {
            boolean canFireAgain = currentTime >= lastSingleShotAttackTime + intermittenAttackCooldown;
            if (burstShotsFired < amountOfShotsPerBurst && canFireAgain) {
                // Fire a missile and update lastAttackTime
                for(SpriteAnimation spriteAnimation : chargingAnimations){
                    fireMissile(enemy, spriteAnimation.getCenterXCoordinate(), spriteAnimation.getCenterYCoordinate());
                }

                burstShotsFired++;
                lastSingleShotAttackTime = currentTime;

                if (burstShotsFired >= amountOfShotsPerBurst) {
                    lastAttackedTime = currentTime;
                    allowedToFireBurst = false;
                    enemy.setAttacking(false);
                    return true; //We finished
                }
            }
            return false; //We still running this behaviour
        }
        return true; //We dont have anything to do at this point
    }

    private void fireMissile (Enemy enemy, int xCoordinate, int yCoordinate) {
        Missile missile = createMissile(enemy, xCoordinate, yCoordinate);
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Missile createMissile (Enemy enemy, int xCoordinate, int yCoordinate) {
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(
                enemy.getXCoordinate(), enemy.getCenterYCoordinate(),
                missileType.getImageType(), enemy.getScale() * 0.75f);


        int movementSpeed = 6;
        //Create missile movement attributes and create a movement configuration
        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, movementSpeed, missilePathFinder, MovementPatternSize.SMALL, enemy.getMovementConfiguration().getRotation()
        );


        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Boss Burst Missile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, enemy.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        //get the coordinates for rotation of the missile
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        Point destinationCoordinates = new Point(
                spaceship.getCenterXCoordinate() - missile.getWidth() / 2,
                spaceship.getCenterYCoordinate() - missile.getHeight() / 2 + 4
        );

        // Any visual resizing BEFORE replacing starting coordinates and rotation
        missile.resetMovementPath();

        Point chargingCenterCoords = new Point(xCoordinate, yCoordinate);

        missile.setCenterCoordinates(chargingCenterCoords.getX(), chargingCenterCoords.getY());
        missile.getMovementConfiguration().setDestination(destinationCoordinates); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(chargingCenterCoords.getX(), chargingCenterCoords.getY());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(enemy);

        return missile;
    }


    @Override
    public int getPriority () {
        return priority;
    }

    @Override
    public boolean isAvailable (Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy);
    }
}
