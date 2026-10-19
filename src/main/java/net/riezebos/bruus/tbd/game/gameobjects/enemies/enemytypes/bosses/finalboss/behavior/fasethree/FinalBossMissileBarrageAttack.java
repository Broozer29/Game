package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasethree;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;


/*
 *  Description of what this attack should do:
 *  The boss is always on the RIGHT side of the screen, meaning we effectively need to fire to the left side of the boss (in clockwise direction, between 18:00 and 24:00). See Direction.java for the angles that match directions
 *  In a for-loop, iterate over the angles between 18:00 and 24:00, firing missiles at each angle
 *  But, crucially, leave a gap between the angles that the player must use to dodge. So, for example, fire between 18:00 and 24:00, but leave a gap between 20:00 and 21:00
 *  And, equally crucially, this gap must increase with each iteration of the for-loop so that the player has to move "with" the wave of missiles.
 *  Additionally, the gap has to be randomized somewhat. Meaning, it cannot ALWAYS start at the same angle. So, to add complexity to the attack:
 *         - The for-loop should increment or decrement the "gap" so the player has to move up or down, depending on the random direction
 *         - The starting and ending angles in the for-loop should keep this direction in mind. No point in starting at 23:00 if we go up and effectively end at 24:00.
 *
 *  I want the following variables for configuring the attack:
 *  double attackCooldown (self-explanator)
 *  int angleIncrement (the amount by which the angle should increase with each iteration of the for-loop)
 *  int angleGapSize (the size of the gap between the angles that the player must use to dodge)
 *  double timeBetweenMissileWave (the time between each for-loop iteration, effectively controlling the speed of the attack)
 *
 *
 */


public class FinalBossMissileBarrageAttack implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 22;
    private int priority = 3;

    private float angleIncrement = 1.5f; // How many degrees the gap moves with each wave
    private int angleGapSize = 17; // Width of the gap in degrees
    private double timeBetweenMissileWave = 0.06;
    private int angleBetweenMissiles = 2; // Degrees between two missiles of the same wave

    // Angles of the left half of the screen as seen from the boss (Direction.java): 90 is DOWN, 180 is LEFT, 270 is UP
    private static double lowestAngle = 140;
    private static double highestAngle = 220;

    private boolean barrageActive = false;
    private double gapStartAngle;
    private int gapDirection; // 1 moves the gap towards UP, -1 moves it towards DOWN
    private long wavesRemaining;
    private double lastWaveTime;
    private Random random = new Random();

    private SpriteAnimation attackingAnimation;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        angleGapSize = 20;
        double currentTime = GameState.getInstance().getGameSeconds();
        if (attackingAnimation == null) {
            initAttackAnimation(enemy);
        }

        if (barrageActive) {
            if (currentTime >= lastWaveTime + timeBetweenMissileWave) {
                fireWave(enemy);
            }

            if (wavesRemaining <= 0) {
                barrageActive = false;
                lastAttackedTime = currentTime;
                enemy.setAttacking(false);
                return true; //We finished
            }
            return false; //We still running this behaviour
        }


        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateAttackAnimationLocation(enemy);
            if (!attackingAnimation.isPlaying()) {
                attackingAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(attackingAnimation);
            }


            if (attackingAnimation.isPlaying() && attackingAnimation.getCurrentFrame() == attackingAnimation.getTotalFrames() - 1) {
                doTheThing(enemy);
                return false; //The waves are fired over the next frames, see the barrageActive check above
            }
            return false; //We still running this behaviour
        }
        return true; //We still running this behaviour

    }


    private void initAttackAnimation(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn); //vervangen met de daadwerkelijke anim

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        attackingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        attackingAnimation.setAnimationScale(0.5f);
        attackingAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
        attackingAnimation.addXOffset(Math.round(attackingAnimation.getWidth() * 0.1f));
    }

    private void updateAttackAnimationLocation(Enemy enemy) {
        attackingAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
    }

    private void doTheThing(Enemy enemy) {
        gapDirection = random.nextBoolean() ? 1 : -1;

        // The gap starts near the edge it moves away from, with a random offset so it doesn't always start at the same angle
        double travelRange = (highestAngle - lowestAngle) - angleGapSize;
        double randomOffset = random.nextDouble() * (travelRange / 3);
        if (gapDirection == 1) {
            gapStartAngle = lowestAngle + randomOffset;
        } else {
            gapStartAngle = highestAngle - angleGapSize - randomOffset;
        }

        // Keep firing waves until the gap reaches the edge it moves towards
        wavesRemaining = Math.round((travelRange - randomOffset) / angleIncrement) + 1;
        barrageActive = true;
        fireWave(enemy);
    }

    private void fireWave(Enemy enemy) {
        double gapEndAngle = gapStartAngle + angleGapSize;
        for (double angle = lowestAngle; angle <= highestAngle; angle += angleBetweenMissiles) {
            if (angle > gapStartAngle && angle < gapEndAngle) {
                continue; // Leave the gap open for the player to dodge through
            }
            shootMissiles(enemy, angle);
        }

        gapStartAngle += gapDirection * angleIncrement;
        wavesRemaining--;
        lastWaveTime = GameState.getInstance().getGameSeconds();
    }

    private void shootMissiles(Enemy enemy ,double angleDegrees) {
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate(),
                ImageEnums.LaserBullet, 0.5f);


        float movementSpeed = 3.25f;
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, Direction.LEFT
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                enemy.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly,
                false, true, true);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);


        //Calculate the angle based on the current chargingAnimation. Because we want to fire from 4 directions, we also need to keep
        //track of the angle that the given chargingAnimation has in this method
        Point bulletOrigin = new Point(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
        Point bulletDestination = calculateBulletDestination(angleDegrees, 800, enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());

        missile.resetMovementPath();

        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.getMovementConfiguration().setDestination(bulletDestination); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setSpeedsUp(true);
        missile.setOwnerOrCreator(enemy);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Point calculateBulletDestination(double angleDegrees, int distance, int centerX, int centerY) {
        // Convert the angle from degrees to radians because Math functions use radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
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
        if (enemy instanceof FinalBoss finalBoss) {
            return finalBoss.isAllowedToFire()
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_3
                    && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }

    public void setLastAttackedTime(double lastAttackedTime) {
        this.lastAttackedTime = lastAttackedTime;
    }
}
