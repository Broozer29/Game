package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBossManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
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

public class TwinBossLaserbeamCentreManouvre implements BossActionable {

    public static double lastAttackTime = GameState.getInstance().getGameSeconds();
    private static double attackCooldown = 20;
    private static int priority = 3;

    private static int twinsChargingUpLaser = 0;
    private static int twinsFiringLaser = 0;
    private static int twinsTeleportedToMiddle = 0;
    private static int twinsReset = 0;
    private static double duration = 5;

    private static double gameSecondsLaserbeamStartedFiring = 0;

    private static CustomAudioClip chargingLaserbeamSoundEffect = null;

    public TwinBossLaserbeamCentreManouvre() {
        chargingLaserbeamSoundEffect = AudioDatabase.getInstance().getAudioClip(AudioEnums.ChargingLaserbeam);
    }

    public static void resetBehaviour() {
        twinsChargingUpLaser = 0;
        twinsFiringLaser = 0;
        twinsTeleportedToMiddle = 0;
        twinsReset = 0;

        chargingLaserbeamSoundEffect.setMediaPlayerFinished(false);
        chargingLaserbeamSoundEffect.setMediaPlayerPlaying(false);
        chargingLaserbeamSoundEffect.setPlaybackPosition(0);
    }

    @Override
    public boolean activateBehaviour(Enemy enemy1) {
        TwinBoss enemy = (TwinBoss) enemy1; //we can safely cast since this behaviour is never added to other enemy types
        if (twinsTeleportedToMiddle < TwinBossManager.twinCount) {
            if (chargingLaserbeamSoundEffect == null || chargingLaserbeamSoundEffect.getMediaPlayer() == null) {
                chargingLaserbeamSoundEffect = AudioDatabase.getInstance().getAudioClip(AudioEnums.SpaceStationChargingUpMovement);
            }

            //if first time here, play this stuff
            teleportBossToStartingSpot(enemy);
            twinsTeleportedToMiddle++;
            return false;
        }

        if (twinsTeleportedToMiddle == TwinBossManager.twinCount && twinsChargingUpLaser < TwinBossManager.twinCount) {
            addChargingUpLaserAnim(enemy);

            if (!chargingLaserbeamSoundEffect.isRunning()) {
                chargingLaserbeamSoundEffect.startClip();
            }
            twinsChargingUpLaser++;
            return false;
        }

        if (chargingLaserbeamSoundEffect.isFinished() && twinsChargingUpLaser == TwinBossManager.twinCount && twinsFiringLaser < TwinBossManager.twinCount) {
            activateLaserbeams(enemy); //start firing the real laser
            twinsFiringLaser++;

            if (twinsFiringLaser == TwinBossManager.twinCount) {
                gameSecondsLaserbeamStartedFiring = GameState.getInstance().getGameSeconds();
            }
        }

        //0.5 seconds after laserbeams exist, start updating it
        if (twinsFiringLaser == TwinBossManager.twinCount
//                && GameState.getInstance().getGameSeconds() >= gameSecondsLaserbeamStartedFiring + delayBeforeUpdatingLaserbeams
        ) {
            updateLaserbeams(enemy);
        }

        if (twinsFiringLaser == TwinBossManager.twinCount && GameState.getInstance().getGameSeconds() >= gameSecondsLaserbeamStartedFiring + duration) {
            enemy.clearLaserbeams();
            revertToOldMovement(enemy);
            twinsReset++;
            if (twinsReset == TwinBossManager.twinCount) {
                lastAttackTime = GameState.getInstance().getGameSeconds();

                TwinBossManager.setCooldownToPreventBackToBackTeleportBehaviour(this);

                chargingLaserbeamSoundEffect.setMediaPlayerFinished(false);
                chargingLaserbeamSoundEffect.setMediaPlayerPlaying(false);
                chargingLaserbeamSoundEffect.setPlaybackPosition(0);
                gameSecondsLaserbeamStartedFiring = 0;
                twinsTeleportedToMiddle = 0;
                twinsChargingUpLaser = 0;
                twinsReset = 0;
                twinsFiringLaser = 0;
                return true; //we reset all the twins and finish behaviour
            }
            return false; //finished but do not return true as it sends a signal to the manager to wipe remaining behaviour
        }
        return false;
    }

    private void revertToOldMovement(TwinBoss enemy) {
        HoverPathFinder hoverPathFinder = new HoverPathFinder();
        hoverPathFinder.setSecondsToHoverStill(0);
        hoverPathFinder.setShouldDecreaseBoardBlock(true);
        hoverPathFinder.setDecreaseBoardBlockAmountBy(twinsReset >= 2 ? -1 : 1);
        enemy.getMovementConfiguration().setBoardBlockToHoverIn(BoardBlockUpdater.getBoardBlock(enemy.getCenterXCoordinate()));
        enemy.getMovementConfiguration().setPathFinder(hoverPathFinder);
        enemy.resetMovementPath();
        enemy.setAllowedVisualsToRotate(true);
        enemy.setAllowedToMove(true);
        enemy.move();
    }


    private SpriteAnimationConfiguration createChargingAnimationConfig(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.LaserbeamCharging);

        return new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
    }

    private void addChargingUpLaserAnim(TwinBoss boss) {
        SpriteAnimation chargingAnimation = new SpriteAnimation(createChargingAnimationConfig(boss));
        chargingAnimation.setAnimationScale(2f);
        chargingAnimation.setFrameDelay(10);
        chargingAnimation.setCenterCoordinates(
                boss.getChargingUpAttackAnimation().getCenterXCoordinate(),
                boss.getChargingUpAttackAnimation().getCenterYCoordinate()
        );
        //Origin coordinates forces it to constantly recenter itself on this point
        chargingAnimation.setOriginCoordinates(
                boss.getChargingUpAttackAnimation().getCenterXCoordinate(),
                boss.getChargingUpAttackAnimation().getCenterYCoordinate()
        );
        AnimationManager.getInstance().addUpperAnimation(chargingAnimation);
    }

    private void activateLaserbeams(TwinBoss boss) {
        double angle = 0;
        switch (twinsFiringLaser) {
            case 0:
                angle = Direction.LEFT.toAngle();
                break;
            case 1:
                angle = Direction.UP.toAngle();
                break;
            case 2:
                angle = Direction.RIGHT.toAngle();
                break;
            case 3:
                angle = Direction.DOWN.toAngle();
                break;
        }
        for (int i = 0; i < 2; i++) {
            Laserbeam laserbeam = createLaserbeam(boss, angle);
            boss.addLaserbeam(laserbeam);
            MissileManager.getInstance().addLaserBeam(laserbeam);
        }
    }

    private void updateLaserbeams(TwinBoss boss) {
        for (int i = 0; i < boss.getLaserbeamList().size(); i++) {
            Laserbeam laserbeam = boss.getLaserbeamList().get(i);
            laserbeam.setOriginPoint(
                    new Point(
                            boss.getCenterXCoordinate() - Laserbeam.getXOffsetForCentering(),
                            boss.getCenterYCoordinate() - Laserbeam.getYOffsetForCentering()
                    )
            );

            laserbeam.setAngleDegrees(i == 0 ? laserbeam.getAngleDegrees() + 0.3f : laserbeam.getAngleDegrees() - 0.3f);

            if (laserbeam.getAngleDegrees() < 0) {
                laserbeam.setAngleDegrees(360);
            } else if (laserbeam.getAngleDegrees() > 360) {
                laserbeam.setAngleDegrees(0);
            }
        }
    }

    private Laserbeam createLaserbeam(Enemy enemy, double angle) {
        float damage = enemy.getDamage();
        LaserbeamConfiguration upperLaserbeamConfiguration = new LaserbeamConfiguration(true, damage);
        upperLaserbeamConfiguration.setAmountOfLaserbeamSegments(15);

        //Not setting the origin point causes a nullpointer exception, it's mandatory
        //todo fix this nullpointer exception by either checking for an existing origin point or make it mandatory for the laserbeam config or smth
        upperLaserbeamConfiguration.setOriginPoint(
                new Point(
                        enemy.getCenterXCoordinate(),
                        enemy.getCenterYCoordinate()
                )
        );

        upperLaserbeamConfiguration.setAngleDegrees(angle);

        Laserbeam laserbeam = new AngledLaserBeam(upperLaserbeamConfiguration);
        laserbeam.setAngleDegrees(angle);
        laserbeam.setOwner(enemy);
        laserbeam.setOriginPoint(
                new Point(
                        enemy.getCenterXCoordinate() - Laserbeam.getXOffsetForCentering(),
                        enemy.getCenterYCoordinate() - Laserbeam.getYOffsetForCentering()
                )
        );
        laserbeam.update();
        return laserbeam;
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
        spriteConfiguration.setScale(1f);
        spriteConfiguration.setImageType(ImageEnums.SmokeExplosion);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        return new SpriteAnimation(spriteAnimationConfiguration);
    }


    private void teleportBossToStartingSpot(TwinBoss boss) {
        if (TwinBossManager.twinCount > 4) {
            OnScreenTextManager.getInstance().addText("TwinBossLeftRightManouvre expects 4 enemies but got " + TwinBossManager.twinCount + "!");
        }
        if (boss.getPointToTeleportBackTo() == null) {
            playSmokeAnimation(boss.getCenterXCoordinate(), boss.getCenterYCoordinate()); //Place it at the current location
            boss.setAllowedVisualsToRotate(true);
            switch (twinsTeleportedToMiddle) {
                case 0:
                    boss.rotateGameObjectTowards(Direction.LEFT, false);
                    boss.setCenterCoordinates(
                            (DataClass.getInstance().getWindowWidth() / 2) - (boss.getWidth()),
                            (DataClass.getInstance().getPlayableWindowMaxHeight() / 2));
                    break;
                case 1:
                    boss.rotateGameObjectTowards(Direction.UP, false);
                    boss.setCenterCoordinates(
                            (DataClass.getInstance().getWindowWidth() / 2) - (boss.getWidth() * 0.5f),
                            (DataClass.getInstance().getPlayableWindowMaxHeight() / 2) - (boss.getHeight()));
                    break;
                case 2:
                    boss.rotateGameObjectTowards(Direction.RIGHT, false);
                    boss.setCenterCoordinates(
                            (DataClass.getInstance().getWindowWidth() / 2) + (boss.getWidth()),
                            (DataClass.getInstance().getPlayableWindowMaxHeight() / 2));
                    break;
                case 3:
                    boss.rotateGameObjectTowards(Direction.DOWN, false);
                    boss.setCenterCoordinates(
                            (DataClass.getInstance().getWindowWidth() / 2) - (boss.getWidth() * 0.5f),
                            (DataClass.getInstance().getPlayableWindowMaxHeight() / 2) + (boss.getHeight() * 0.25f));
                    break;
            }

            boss.setAllowedVisualsToRotate(false);
            boss.setAllowedToMove(false);
            playSmokeAnimation(boss.getCenterXCoordinate(), boss.getCenterYCoordinate());
        }
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
