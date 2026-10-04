package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.finalboss.FinalBossMine;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.CustomAudioClip;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class FinalBossDropMineCharge implements BossActionable {
    private double lastAttackTime = GameState.getInstance().getGameSeconds() - 32;
    private double attackCooldown = 35;
    private double lastBombDroppedTime = 0;
    private double bombDropCooldown = 0.1f;
    private int priority = 13;

    private CustomAudioClip chargingUpMovement = null;
    private CustomAudioClip boostingAway = null;


    private boolean isCharging = false;
    private boolean chargingUp = false;
    private Point selectedEndDestination;

    public FinalBossDropMineCharge() {
        chargingUpMovement = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationChargingUpMovement);
        boostingAway = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationBlastingOff);
    }


    private Point calculateEndDestination(Enemy enemy) {
        float minXCoordinate = 0;
        float maxXCoordinate = DataClass.getInstance().getWindowWidth();
        float minYCoordinate = 0;
        float maxYCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight();


        float playerXCoordinate = 0;
        float playerYCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.5f;

        float bossXCoordinate = enemy.getCenterXCoordinate();
        float bossYCoordinate = enemy.getCenterYCoordinate();

        float deltaX = playerXCoordinate - bossXCoordinate;
        float deltaY = playerYCoordinate - bossYCoordinate;
        float angle = (float) Math.atan2(deltaY, deltaX);

        float xEnd = 0;
        float yEnd = 0;

        if (deltaX > 0) { // Charging to the right
            xEnd = maxXCoordinate;
        } else { // Charging to the left
            xEnd = minXCoordinate;
        }
        yEnd = bossYCoordinate + (xEnd - bossXCoordinate) * (float) Math.tan(angle);

        if (yEnd < minYCoordinate || yEnd > maxYCoordinate) {
            if (deltaY > 0) { // Charging downwards
                yEnd = maxYCoordinate;
            } else { // Charging upwards
                yEnd = minYCoordinate;
            }
            xEnd = bossXCoordinate + (yEnd - bossYCoordinate) / (float) Math.tan(angle);
        }

        xEnd = Math.max(minXCoordinate, Math.min(maxXCoordinate, xEnd)) - enemy.getWidth() / 2; //final offset so the charge is centered on the player
        yEnd = Math.max(minYCoordinate, Math.min(maxYCoordinate, yEnd)) - enemy.getHeight() / 2;//final offset so the charge is centered on the player

        return new Point(xEnd, yEnd);
    }


    private static float audioDurationInSecondsDelay = 2.75f;
    private double timeStartedCharging = 0;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        if (!isCharging && !chargingUp) {

            if (chargingUpMovement == null || chargingUpMovement.getMediaPlayer() == null) {
                chargingUpMovement = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationChargingUpMovement);
            }
            timeStartedCharging = GameState.getInstance().getGameSeconds();
            chargingUpMovement.setPlaybackPosition(0);
            chargingUpMovement.startClip();
            enemy.setAllowedToMove(false);
            chargingUp = true;
            playSmokeAnimation(enemy);
            teleportBossToStartingLocation(enemy);
            playSmokeAnimation(enemy);
            return false;
        }

        if (GameState.getInstance().getGameSeconds() >= timeStartedCharging + audioDurationInSecondsDelay && chargingUp && !isCharging) {
            startCharging(enemy);
            return false;
        }

        if (isCharging) {
            enemy.setAllowedToMove(true);
            dropBomb(enemy); //Now that it's moving, periodically drop bombs behind him
        }

        if (enemy.getMovementConfiguration().getCurrentPath() != null && enemy.getMovementConfiguration().getCurrentPath().getWaypoints().isEmpty() && isCharging && !chargingUp) { //or another check wether to see if we reached the destination
            revertToOldMovement(enemy);


            //These manual intervention in CustomAudioClip lifecycle management is required to guarantee playback. I do NOT understand why it's required
            boostingAway.setMediaPlayerFinished(false);
            boostingAway.setMediaPlayerPlaying(false);
            boostingAway.setPlaybackPosition(0);

            chargingUpMovement.setMediaPlayerFinished(false);
            chargingUpMovement.setMediaPlayerPlaying(false);
            chargingUpMovement.setPlaybackPosition(0);

            this.isCharging = false; //reset for next use
            this.chargingUp = false; //reset for next use
            return true; //finished behaviour
        }

        return false;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    private void revertToOldMovement(Enemy enemy){
        lastAttackTime = GameState.getInstance().getGameSeconds();
        teleportBossToBackLine(enemy);
        playSmokeAnimation(enemy); //Place it at the current location
        playSmokeAnimationAtFirstStep(enemy); //Place it at the location the boss teleports back to

        enemy.getMovementConfiguration().setLastUsedMovementSpeed(enemy.getEnemyType().getMovementSpeed() * 7.5f);
        enemy.getMovementConfiguration().setMovementSpeed(enemy.getEnemyType().getMovementSpeed());
        enemy.setAllowedVisualsToRotate(true);
        HoverPathFinder hoverPathFinder = new HoverPathFinder();
        hoverPathFinder.setSecondsToHoverStill(0);
        hoverPathFinder.setShouldDecreaseBoardBlock(false);
        hoverPathFinder.setShouldChangeBoardBlockEverXHover(2);
        enemy.getMovementConfiguration().setBoardBlockToHoverIn(7);

        enemy.getMovementConfiguration().setPathFinder(hoverPathFinder);
        enemy.resetMovementPath();
        enemy.move();
    }

    private void startCharging(Enemy enemy){
        selectedEndDestination = calculateEndDestination(enemy);
        enemy.setAllowedVisualsToRotate(true);
        enemy.getMovementConfiguration().setPathFinder(new DestinationPathFinder());
        enemy.getMovementConfiguration().setDestination(selectedEndDestination);
        enemy.resetMovementPath();

        enemy.getMovementConfiguration().setDestination(selectedEndDestination);
        enemy.getMovementConfiguration().setLastUsedMovementSpeed(enemy.getEnemyType().getMovementSpeed());
        enemy.getMovementConfiguration().setMovementSpeed(enemy.getEnemyType().getMovementSpeed() * 7.5f);
        enemy.rotateObjectTowardsDestination(false);

        enemy.setAllowedVisualsToRotate(false);
        enemy.move();
        isCharging = true;
        chargingUp = false;

        if (boostingAway == null && boostingAway.getMediaPlayer() == null) {
            boostingAway = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationBlastingOff);
        }
        boostingAway.setPlaybackPosition(0);
        boostingAway.startClip();
    }

    private void dropBomb(Enemy enemy) {
        if (GameState.getInstance().getGameSeconds() >= lastBombDroppedTime + bombDropCooldown) {
            lastBombDroppedTime = GameState.getInstance().getGameSeconds();
            createFinalBossMine(enemy, Direction.UP);
            createFinalBossMine(enemy, Direction.DOWN);
        }
    }

    private void createFinalBossMine(Enemy enemy, Direction direction) {
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate(),
                MissileEnums.FinalBossMine.getImageType(), 0.5f);

        Random random = new Random();
        float movementSpeed = 2f + random.nextFloat() * 2f;


        //Create missile movement attributes and create a movement configuration
        MissileEnums missileType = MissileEnums.FinalBossMine;
        PathFinder missilePathFinder = new RegularPathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, direction
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                enemy.getDamage() * 2f, missileType.getDeathOrExplosionImageEnum(), isFriendly, true,
                true, true);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        FinalBossMine missile = (FinalBossMine) MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(enemy);
        missile.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false);
        missile.getAnimation().setFrameDelay(4);
        missile.setCurrentHitpoints(50);
        missile.setMaxHitPoints(50);
        MissileManager.getInstance().addExistingMissile(missile);
    }


    private void playSmokeAnimation(GameObject target) {
        SpriteAnimation spriteAnimation = createSmokeAnim();
        spriteAnimation.setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }

    private SpriteAnimation createSmokeAnim() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(-100);
        spriteConfiguration.setyCoordinate(-100);
        spriteConfiguration.setScale(1.15f);
        spriteConfiguration.setImageType(ImageEnums.SmokeExplosion);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        return new SpriteAnimation(spriteAnimationConfiguration);
    }

    //Dirty patch solution for playing smoke at the correct place: as it plays before the boss is teleported to the next step
    private void playSmokeAnimationAtFirstStep(GameObject target) {
        SpriteAnimation spriteAnimation = createSmokeAnim();

        Point teleportPoint = target.getMovementConfiguration().getCurrentLocation();
        spriteAnimation.setCenterCoordinates(
                teleportPoint.getX() + target.getWidth() / 2,
                teleportPoint.getY() + target.getHeight() / 2
        );
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }


    private void teleportBossToStartingLocation(Enemy boss) {
        boss.setCenterCoordinates(DataClass.getInstance().getWindowWidth() * 0.9f, DataClass.getInstance().getPlayableWindowMaxHeight() * 0.5f);
        boss.setAllowedToMove(false);
        boss.setAllowedVisualsToRotate(true);
        boss.rotateGameObjectTowards(Direction.LEFT, false);
        boss.setAllowedVisualsToRotate(false);
    }

    private void teleportBossToBackLine(Enemy boss) {
        Point teleportPoint = BoardBlockUpdater.getRandomCoordinateInBlock(7, boss.getWidth(), boss.getHeight());
        boss.setCenterCoordinates(teleportPoint.getX(), teleportPoint.getY());
    }


    @Override
    public boolean isAvailable(Enemy enemy) {
        if (enemy instanceof FinalBoss finalBoss) {
            return finalBoss.isAllowedToFire()
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_2
                    && GameState.getInstance().getGameSeconds() >= lastAttackTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }


    public double getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(double lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }
}

