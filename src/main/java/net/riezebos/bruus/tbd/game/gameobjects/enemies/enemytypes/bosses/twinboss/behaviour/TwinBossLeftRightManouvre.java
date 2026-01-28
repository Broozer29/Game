package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBossManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.StationaryExplodingBomb;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.*;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.CustomAudioClip;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class TwinBossLeftRightManouvre implements BossActionable {

    public static double lastAttackTime = GameState.getInstance().getGameSeconds();
    private static double attackCooldown = 15;
    private static double lastBombDroppedTime = 0;
    private static double bombDropCooldown = 0.3f;
    private static int priority = 2;
    private static float speedModifier = 5.75f;

    private static CustomAudioClip chargingUpMovement = null;
    private static CustomAudioClip boostingAway = null;


    private static int twinsPlacedInPosition = 0;
    private static int twinsMoving = 0;
    private static int twinsWaitingToMove = 0;
    private static int twinsReset = 0;

    public TwinBossLeftRightManouvre() {
        chargingUpMovement = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationChargingUpMovement);
        boostingAway = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationBlastingOff);
    }

    private static float audioDurationInSecondsDelay = 1.75f;
    private static double timeStartedCharging = 0;


    private static float firstIndexHeight = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.2f;
    private static float secondIndexHeight = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.4f;
    private static float thirdIndexHeight = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.6f;
    private static float fourthIndexHeight = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.8f;

    public static void resetBehaviour(){
        twinsPlacedInPosition = 0;
        twinsMoving = 0;
        timeStartedCharging = 0;
        twinsWaitingToMove = 0;
        twinsReset = 0;
        bombDropIndexCounter = 0;

        //Recalculating them should the dimensions of the window EVER change during runtime, as of writing this comment, its not implemented yet
//        firstIndexHeight = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.2f;
//        secondIndexHeight = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.4f;
//        thirdIndexHeight = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.6f;
//        fourthIndexHeight = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.8f;


        if(boostingAway != null) {
            boostingAway.setMediaPlayerFinished(false);
            boostingAway.setMediaPlayerPlaying(false);
            boostingAway.setPlaybackPosition(0);
        }

        if(chargingUpMovement != null) {
            chargingUpMovement.setMediaPlayerFinished(false);
            chargingUpMovement.setMediaPlayerPlaying(false);
            chargingUpMovement.setPlaybackPosition(0);
        }
    }

    @Override
    public boolean activateBehaviour(Enemy enemy1) {
        TwinBoss enemy = (TwinBoss) enemy1; //we can safely cast since this behaviour is never added to other enemy types
        if (twinsWaitingToMove < TwinBossManager.twinCount) { //if not all 4 are ready to move
            if (chargingUpMovement == null || chargingUpMovement.getMediaPlayer() == null) {
                chargingUpMovement = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationChargingUpMovement);
            }

            //if first time here, play this stuff
            if (twinsWaitingToMove == 0) {
                chargingUpMovement.setPlaybackPosition(0);
                chargingUpMovement.startClip();
                timeStartedCharging = GameState.getInstance().getGameSeconds();
            }
            teleportBossToStartingSpot(enemy);
            twinsWaitingToMove++;
            return false;
        }

        if (GameState.getInstance().getGameSeconds() >= timeStartedCharging + audioDurationInSecondsDelay &&
                twinsWaitingToMove == TwinBossManager.twinCount && //if all twins are waiting to move
                twinsMoving < TwinBossManager.twinCount) { //if not all twins are moving
            startCharging(enemy, twinsMoving);
            twinsMoving++;
            return false;
        }

        if (twinsMoving == TwinBossManager.twinCount) {
            activateAttack(enemy); //Now that it's moving, do something, currently empty
        }

        if (twinsMoving == TwinBossManager.twinCount && enemy.getMovementConfiguration().getCurrentPath() != null && enemy.getMovementConfiguration().getCurrentPath().getWaypoints().size() <= 1 ) { //or another check wether to see if we reached the destination
            revertToOldMovement(enemy);
            twinsReset++;
            if (twinsReset == TwinBossManager.twinCount) {
                lastAttackTime = GameState.getInstance().getGameSeconds();
                TwinBossManager.setCooldownToPreventBackToBackTeleportBehaviour(this);
                boostingAway.setMediaPlayerFinished(false);
                boostingAway.setMediaPlayerPlaying(false);
                boostingAway.setPlaybackPosition(0);

                chargingUpMovement.setMediaPlayerFinished(false);
                chargingUpMovement.setMediaPlayerPlaying(false);
                chargingUpMovement.setPlaybackPosition(0);
                twinsWaitingToMove = 0;
                twinsMoving = 0;
                twinsReset = 0;
                twinsPlacedInPosition = 0;
                return true; //we reset all the twins and finish behaviour
            }
            return false; //finished but do not return true as it sends a signal to the manager to wipe remaining behaviour
        }
        return false;
    }

    private void revertToOldMovement(TwinBoss enemy) {
        playSmokeAnimation(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate()); //Place it at the current location
        teleportBossToOriginalPosition(enemy);
        enemy.setPointToTeleportBackTo(null);
        playSmokeAnimation(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate()); //Place it at the current location
//        playSmokeAnimationAtFirstStep(enemy); //Place it at the location the boss teleports back to

        enemy.getMovementConfiguration().setLastUsedYMovementSpeed(enemy.getEnemyType().getMovementSpeed() * speedModifier);
        enemy.getMovementConfiguration().setLastUsedXMovementSpeed(enemy.getEnemyType().getMovementSpeed() * speedModifier);
        enemy.getMovementConfiguration().setYMovementSpeed(enemy.getEnemyType().getMovementSpeed());
        enemy.getMovementConfiguration().setXMovementSpeed(enemy.getEnemyType().getMovementSpeed());
        enemy.setAllowedVisualsToRotate(true);


        HoverPathFinder hoverPathFinder = new HoverPathFinder();
        hoverPathFinder.setSecondsToHoverStill(0);
        hoverPathFinder.setShouldDecreaseBoardBlock(true);
        hoverPathFinder.setDecreaseBoardBlockAmountBy(twinsReset >= 2 ? -1 : 1);

        enemy.getMovementConfiguration().setBoardBlockToHoverIn(BoardBlockUpdater.getBoardBlock(enemy.getCenterXCoordinate()));

        enemy.getMovementConfiguration().setPathFinder(hoverPathFinder);
        enemy.resetMovementPath();
        enemy.move();
    }

    private void startCharging(Enemy enemy, int currentTwinsMovingIndex) {
        enemy.setAllowedVisualsToRotate(true);
        enemy.setAllowedToMove(true);
        enemy.getMovementConfiguration().setPathFinder(new DestinationPathFinder());


        Point destination = calculateDestinationBasedOnIndex(enemy, currentTwinsMovingIndex);
        enemy.getMovementConfiguration().setDestination(destination);
        enemy.resetMovementPath();

        enemy.getMovementConfiguration().setDestination(destination);
        enemy.getMovementConfiguration().setLastUsedYMovementSpeed(enemy.getEnemyType().getMovementSpeed());
        enemy.getMovementConfiguration().setYMovementSpeed(enemy.getEnemyType().getMovementSpeed() * speedModifier);
        enemy.getMovementConfiguration().setLastUsedXMovementSpeed(enemy.getEnemyType().getMovementSpeed());
        enemy.getMovementConfiguration().setXMovementSpeed(enemy.getEnemyType().getMovementSpeed() * speedModifier);
        enemy.rotateObjectTowardsDestination(false);

        enemy.setAllowedVisualsToRotate(false);
        enemy.move();
        if (boostingAway == null && boostingAway.getMediaPlayer() == null) {
            boostingAway = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationBlastingOff);
        }
        boostingAway.setPlaybackPosition(0);
        boostingAway.startClip();
    }

    private Point calculateDestinationBasedOnIndex(Enemy enemy, int currentTwinsMovingIndex){
        //centerXCoordinate might need to be replaced with enemy.getXCoordinate, requires runtime test to know for sure
        Point point = new Point(-100, enemy.getYCoordinate());
        switch(currentTwinsMovingIndex){
            case 0:
                point.setX(DataClass.getInstance().getWindowWidth());
                break;
            case 1:
                point.setX(-(enemy.getWidth() / 2));
                break;
            case 2:
                point.setX(DataClass.getInstance().getWindowWidth());
                break;
            case 3:
                point.setX(-(enemy.getWidth() / 2));
                break;
        }
        return point;
    }



    private static int bombDropIndexCounter = 0;
    private void activateAttack(TwinBoss boss) {
        if (GameState.getInstance().getGameSeconds() >= lastBombDroppedTime + bombDropCooldown) {
            bombDropIndexCounter++; //This works so long as the bosses call this method in a consistent order
            createExplosion(boss);
            if(bombDropIndexCounter == TwinBossManager.twinCount) {
                lastBombDroppedTime = GameState.getInstance().getGameSeconds();
                bombDropIndexCounter = 0;
            }
        }
    }

    private static void createExplosion(Enemy enemy) {
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate(),
                ImageEnums.Bomba_Missile, 0.6f);

        float movementSpeed = 1f;


        //Create missile movement attributes and create a movement configuration
        MissileEnums missileType = MissileEnums.StationaryExplodingBomb;
        PathFinder missilePathFinder = new RegularPathFinder();
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, Direction.LEFT
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;
        int maxHitPoints = 100000;
        int maxShields = 0;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Bomba Missile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, enemy.getDamage() * 2f, missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, false,
                true, false, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        StationaryExplodingBomb missile = (StationaryExplodingBomb) MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(enemy);
        missile.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
        missile.setAllowedToMove(false);
        missile.setAllowedVisualsToRotate(false);
        missile.setExplosionSize(5);
        missile.getAnimation().setFrameDelay(1);
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private void playSmokeAnimation(float xCoordinate, float yCoordinate) {
        SpriteAnimation spriteAnimation = createSmokeAnim();
        spriteAnimation.setCenterCoordinates(xCoordinate, yCoordinate);
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


    private void teleportBossToStartingSpot(TwinBoss boss) {
        if (TwinBossManager.twinCount > 4) {
            OnScreenTextManager.getInstance().addText("TwinBossLeftRightManouvre expects 4 enemies but got " + TwinBossManager.twinCount + "!");
        }
        if(boss.getPointToTeleportBackTo() == null) {
            playSmokeAnimation(boss.getCenterXCoordinate(), boss.getCenterYCoordinate()); //Place it at the current location
            boss.setPointToTeleportBackTo(new Point(boss.getCenterXCoordinate(), boss.getCenterYCoordinate()));
            switch (twinsPlacedInPosition) {
                case 0:
                    boss.setCenterCoordinates(Math.round(0 + boss.getWidth() * 0.5f), Math.round(firstIndexHeight));
                    break;
                case 1:
                    boss.setCenterCoordinates(Math.round(DataClass.getInstance().getWindowWidth() * 0.95f), Math.round(secondIndexHeight));
                    break;
                case 2:
                    boss.setCenterCoordinates(Math.round(0 + boss.getWidth() * 0.5f), Math.round(thirdIndexHeight));
                    break;
                case 3:
                    boss.setCenterCoordinates(Math.round(DataClass.getInstance().getWindowWidth() * 0.95f), Math.round(fourthIndexHeight));
                    break;
            }

            boss.setAllowedVisualsToRotate(true);
            Point destination = calculateDestinationBasedOnIndex(boss, twinsPlacedInPosition);
            boss.rotateGameObjectTowards(destination.getX(), boss.getCenterYCoordinate(), false); //centerYCoordinate is viable since we go left/right only
            boss.setAllowedVisualsToRotate(false);

            boss.setAllowedToMove(false);
            playSmokeAnimation(boss.getCenterXCoordinate(), boss.getCenterYCoordinate());
            twinsPlacedInPosition++;
        }
    }

    private void teleportBossToOriginalPosition(TwinBoss boss) {
        boss.setCenterCoordinates(boss.getPointToTeleportBackTo().getX(), boss.getPointToTeleportBackTo().getY());
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return GameState.getInstance().getGameSeconds() >= lastAttackTime + attackCooldown;
    }
}
