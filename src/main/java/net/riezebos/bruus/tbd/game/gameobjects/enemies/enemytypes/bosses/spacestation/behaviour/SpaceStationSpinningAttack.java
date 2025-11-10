package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.spacestation.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.CustomAudioClip;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import java.util.Random;

public class SpaceStationSpinningAttack implements BossActionable {

    private double lastAttackedTime = 0;
    private int attackCooldown = 50;
    private Point centerPoint;
    private CustomAudioClip chargingUpMovement = null;
    private CustomAudioClip boostingAway = null;
    private boolean allowedToBlastAway = false;
    private boolean chargingUp = false;
    private boolean isMoving = false;
    private boolean isBouncing = false;
    private boolean isGoingBackToCenter = false;

    private float oldMoveSpeed = 0.0f;
    private float newMoveSpeedModifier = 2.75f;

    public SpaceStationSpinningAttack () {
//        chargingUpMovement = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationChargingUpMovement);
//        boostingAway = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationBlastingOff);
        centerPoint = EnemyCreator.calculateSpaceStationBossDestination(EnemyEnums.SpaceStationBoss);
    }


    @Override
    public boolean activateBehaviour (Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            if (!chargingUp) {
                chargingUpMovement = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationChargingUpMovement);
                boostingAway = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationBlastingOff);
                chargingUpMovement.startClip();
                enemy.getAnimation().setFrameDelay(0);
                chargingUp = true;
            }
            if (chargingUpMovement.isFinished() && chargingUp) {
                allowedToBlastAway = true;
            }
        }

        if (allowedToBlastAway && !isMoving) {
            BouncingPathFinder pathFinder = new BouncingPathFinder();
            pathFinder.setUseCenteredCoordinatesInstead(true);
            enemy.getMovementConfiguration().setDirection(getRandomDirection());
            enemy.setPathFinder(pathFinder);
            oldMoveSpeed = enemy.getMovementConfiguration().getXMovementSpeed();
            enemy.getMovementConfiguration().setXMovementSpeed(oldMoveSpeed * newMoveSpeedModifier);
            enemy.getMovementConfiguration().setYMovementSpeed(oldMoveSpeed * newMoveSpeedModifier);
            isMoving = true;
            isBouncing = true;
            if(boostingAway != null) {
                boostingAway.startClip();
            }
        }

        if (isMoving) {
            if (isBouncing) {
                BouncingPathFinder pathFinder = (BouncingPathFinder) enemy.getMovementConfiguration().getPathFinder();
                if (pathFinder.getCurrentBounces() > 5) {
                    DestinationPathFinder destinationPathFinder = new DestinationPathFinder();
                    enemy.getMovementConfiguration().setDestination(centerPoint);
                    enemy.setPathFinder(destinationPathFinder);
                    isBouncing = false;
                    isGoingBackToCenter = true;
                }
            }

            if (isGoingBackToCenter) {
                if (enemy.getMovementConfiguration().getCurrentPath().getWaypoints().isEmpty()) {
                    isGoingBackToCenter = false;
                    isMoving = false;
                    chargingUp = false;
                    allowedToBlastAway = false;
                    isBouncing = false;
                    lastAttackedTime = currentTime;
                    enemy.getAnimation().setFrameDelay(2);
                    enemy.getMovementConfiguration().setXMovementSpeed(oldMoveSpeed);
                    enemy.getMovementConfiguration().setYMovementSpeed(oldMoveSpeed);
                    return true;
                }
            }
        }

        return false;
    }

    private Direction[] directions = {Direction.LEFT_UP, Direction.LEFT_DOWN, Direction.RIGHT_UP, Direction.RIGHT_DOWN};
    Random random  = new Random();
    private Direction getRandomDirection () {
        int i = random.nextInt(0, (directions.length - 1));
        return directions[i];

    }


    @Override
    public int getPriority () {
        return 3;
    }

    @Override
    public boolean isAvailable (Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)
                && Math.abs(enemy.getXCoordinate() - centerPoint.getX()) <= 1
                && Math.abs(enemy.getYCoordinate() - centerPoint.getY()) <= 1;
    }
}
