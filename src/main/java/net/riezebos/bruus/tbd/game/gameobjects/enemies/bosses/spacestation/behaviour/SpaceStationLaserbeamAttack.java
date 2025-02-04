package net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.spacestation.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.LinkedList;
import java.util.List;

public class SpaceStationLaserbeamAttack implements BossActionable {

    private Point centerPoint;
    private int attackCooldown = 17;
    private double lastAttackedTime = 0;
    private double startedFiringTime = 0;
    private int attackDuration = 5;
    private boolean isFiringLaserbeams = false;
    private int priority = 10;
    private List<Laserbeam> laserbeamList = new LinkedList<>();
    private List<SpriteAnimation> chargingAnimations = new LinkedList<>();
    private List<Point> laserBeamAimingPoints = new LinkedList<>();

    private int newFrameDelay = 0;
    private int oldFrameDelay = 0;

    // Define the original, unscaled points once
    private static final Point[] BASE_ORIGIN_POINTS = {
            new Point(209, 236),
            new Point(695, 332),
            new Point(367, 708)
    };

    public SpaceStationLaserbeamAttack () {
        centerPoint = EnemyCreator.calculateSpaceStationBossDestination();
        for (int i = 0; i < BASE_ORIGIN_POINTS.length; i++) {
            laserBeamAimingPoints.add(new Point(0, 0));
        }
        createChargingAnimations();
    }

    @Override
    public boolean activateBehaviour (Enemy enemy) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        int currentRotationAngle = getCurrentRotationAngle(enemy);


        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateChargingAnimationLocations(enemy, currentRotationAngle);

            //Charging up/preparing to attack phase
            SpriteAnimation firstChargingAnimation = chargingAnimations.get(0);
            if (!firstChargingAnimation.isPlaying() && !isFiringLaserbeams) {
                for (SpriteAnimation spriteanimation : chargingAnimations) {
                    spriteanimation.refreshAnimation();
                    AnimationManager.getInstance().addUpperAnimation(spriteanimation);
                }
                oldFrameDelay = enemy.getAnimation().getFrameDelay();
                enemy.getAnimation().setFrameDelay(newFrameDelay);
                enemy.setAttacking(true);
                AudioManager.getInstance().addAudio(AudioEnums.ChargingLaserbeam);
            }


            if (firstChargingAnimation.isPlaying() && !isFiringLaserbeams &&
                    firstChargingAnimation.getCurrentFrame() == firstChargingAnimation.getTotalFrames() - 1) {
                createLaserbeams(enemy);
                updateLaserbeamCoordinates(currentRotationAngle, enemy);
                isFiringLaserbeams = true;
                startedFiringTime = currentTime;
                for (Laserbeam laserbeam : laserbeamList) {
                    MissileManager.getInstance().addLaserBeam(laserbeam);
                }
            }

        }

        //Attacking phase
        if (isFiringLaserbeams) {
            updateLaserbeamCoordinates(getCurrentRotationAngle(enemy), enemy);
            if (attackIsFinished(currentTime)) {
                for (Laserbeam laserbeam : laserbeamList) {
                    laserbeam.setVisible(false);
                }
                laserbeamList.clear();
                enemy.setAttacking(false);
                isFiringLaserbeams = false;
                lastAttackedTime = currentTime;
                enemy.getAnimation().setFrameDelay(oldFrameDelay);
                return true;
            }
            return false;
        }

        return isFiringLaserbeams;
    }

    private boolean attackIsFinished (double currenttime) {
        if (currenttime >= startedFiringTime + attackDuration) {
            return true;
        }
        return false;
    }


    private void createLaserbeams (Enemy enemy) {
        float scale = enemy.getScale();

        for (int i = 0; i < BASE_ORIGIN_POINTS.length; i++) {
            int scaledX = (int) (BASE_ORIGIN_POINTS[i].getX() * scale);
            int scaledY = (int) (BASE_ORIGIN_POINTS[i].getY() * scale);

            int actualXCoordinate = enemy.getXCoordinate() + scaledX;
            int actualYCoordinate = enemy.getYCoordinate() + scaledY;

            laserBeamAimingPoints.get(i).setX(actualXCoordinate);
            laserBeamAimingPoints.get(i).setY(actualYCoordinate);

            LaserbeamConfiguration laserbeamConfiguration = new LaserbeamConfiguration(true, enemy.getDamage() / 2);
            laserbeamConfiguration.setAmountOfLaserbeamSegments(25);
            laserbeamConfiguration.setOriginPoint(laserBeamAimingPoints.get(i));
            laserbeamConfiguration.setAngleDegrees(calculateAngleFromCenter(laserBeamAimingPoints.get(i), enemy));

            Laserbeam laserbeam = new AngledLaserBeam(laserbeamConfiguration);
            laserbeamList.add(laserbeam);
            laserbeam.setOwner(enemy);
            MissileManager.getInstance().addLaserBeam(laserbeam);
        }
    }

    private void updateLaserbeamCoordinates (int angleDegrees, Enemy enemy) {
        // Update positions based on rotation
        for (int i = 0; i < BASE_ORIGIN_POINTS.length; i++) {
            Point newPoint = getCoordinatesBasedOnTheAngle(BASE_ORIGIN_POINTS[i], enemy, angleDegrees);

            // Update the laserbeam origin points
            laserBeamAimingPoints.set(i, new Point(newPoint.getX() - Laserbeam.bodyWidth / 2, newPoint.getY() - Laserbeam.bodyWidth / 2));
            laserbeamList.get(i).setAngleDegrees(calculateAngleFromCenter(laserBeamAimingPoints.get(i), enemy));
            laserbeamList.get(i).setOriginPoint(
                    new Point(enemy.getCenterXCoordinate() - Laserbeam.bodyWidth / 2,
                            enemy.getCenterYCoordinate() - Laserbeam.bodyWidth / 2));
        }
    }

    private void createChargingAnimations () {
        for (int i = 0; i < BASE_ORIGIN_POINTS.length; i++) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setScale(1);
            spriteConfiguration.setxCoordinate(BASE_ORIGIN_POINTS[i].getX());
            spriteConfiguration.setyCoordinate(BASE_ORIGIN_POINTS[i].getY());
            spriteConfiguration.setImageType(ImageEnums.LaserbeamCharging);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 10, false);
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

    private Point getCoordinatesBasedOnTheAngle (Point basePoint, Enemy enemy, int angleDegrees) {
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

    @Override
    public int getPriority () {
        return priority;
    }

    @Override
    public boolean isAvailable (Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameStateInfo.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)
                && enemy.getXCoordinate() == centerPoint.getX()
                && enemy.getYCoordinate() == centerPoint.getY();
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

    private double calculateAngleFromCenter (Point point, Enemy enemy) {
        // Get the center coordinates of the enemy
        int enemyCenterX = enemy.getCenterXCoordinate() - Laserbeam.bodyWidth / 2;
        int enemyCenterY = enemy.getCenterYCoordinate() - Laserbeam.bodyWidth / 2;

        // Calculate the difference in X and Y coordinates
        int dx = point.getX() - enemyCenterX;
        int dy = point.getY() - enemyCenterY;

        // Calculate the angle using Math.atan2 (returns result in radians)
        double angleRadians = Math.atan2(dy, dx);

        // Convert the angle to degrees
        double angleDegrees = Math.toDegrees(angleRadians);

        // Ensure the angle is positive and in the range [0, 360]
        if (angleDegrees < 0) {
            angleDegrees += 360;
        }

        return angleDegrees;
    }

}
