package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker.StrikerBossLaserbeamClone;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.TrackingLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrikerBossCloneLaserbeamAttack implements BossActionable {

    public static float damageRatio = 0.2f;
    public static float maxLaserbeamRotationsPerUpdate = 0.2f;
    public static int laserbeamBodySegmentLength = 10;

    private double attackCooldown = 20;
    private int priority = 15;
    private double lastFiredTime = GameState.getInstance().getGameSeconds() + 4;
    private boolean isFiringLaserbeams;
    private double startedFiringTime = 0;
    private double duration = 4 + (LevelManager.getInstance().getBossDifficultyLevel() * 2);
    private SpriteAnimation chargingAnimation;
    private TrackingLaserBeam trackingLaserbeam;

    private List<StrikerBossLaserbeamClone> laserbeamCloneEnemies = new ArrayList<>();
    private List<Integer> usedBoardBlocks = new ArrayList<>();
    private int boardBlockForTheRealStriker = 0;

    public StrikerBossCloneLaserbeamAttack() {
    }

    private void recreateClones(GameObject parent) {
        usedBoardBlocks.clear();
        Random random = new Random();
        int boardBlockAmount = DataClass.getInstance().getBoardBlockAmount();

        // Generate random boardBlocks for clones
        while (usedBoardBlocks.size() < 4) {
            int boardBlock = random.nextInt(0, boardBlockAmount);
            if (!usedBoardBlocks.contains(boardBlock)) {
                usedBoardBlocks.add(boardBlock);
                laserbeamCloneEnemies.add(createEnemy(boardBlock, parent));
            }
        }

        do {
            boardBlockForTheRealStriker = random.nextInt(0, boardBlockAmount);
        } while (usedBoardBlocks.contains(boardBlockForTheRealStriker));
    }

    private StrikerBossLaserbeamClone createEnemy(int boardBlock, GameObject parent) {
        Point spawnCoordinates = BoardBlockUpdater.getRandomCoordinateInBlock(boardBlock, parent.getWidth(), parent.getHeight());
        StrikerBossLaserbeamClone strikerBossLaserbeamClone = (StrikerBossLaserbeamClone) EnemyCreator.createEnemy(EnemyEnums.StrikerBossLaserbeamClone, spawnCoordinates.getX(), spawnCoordinates.getY(), Direction.LEFT,
                parent.getScale(), 1);
        strikerBossLaserbeamClone.setOwnerOrCreator(parent);
        strikerBossLaserbeamClone.setDamage(parent.getDamage());
        strikerBossLaserbeamClone.setCurrentHitpoints(1000000);
        strikerBossLaserbeamClone.setMaxHitPoints(1000000);
        return strikerBossLaserbeamClone;
    }


    @Override
    public boolean activateBehaviour(Enemy boss) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (chargingAnimation == null) {
            chargingAnimation = createChargingAnimation(boss);
            updateLaserbeamOriginAnimation(boss);
        }


        if (!isFiringLaserbeams) {
            boss.setAllowedVisualsToRotate(true);
            rotateBossToPlayer(boss);
        }

        if (boss.isAllowedToFire() && currentTime >= lastFiredTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(boss) && !isFiringLaserbeams) {
            updateLaserbeamOriginAnimation(boss);
            boss.setAllowedToMove(false);
            if (laserbeamCloneEnemies.isEmpty()) {
                recreateClones(boss);
                laserbeamCloneEnemies.forEach(clone -> {
                    EnemyManager.getInstance().addEnemy(clone);
                    playSmokeAnimation(clone);
                    clone.setAllowedToMove(false);
                    clone.setAllowedToFire(true);
                });
                playSmokeAnimation(boss);
                teleportBoss(boss);
                playSmokeAnimation(boss);
            }

            if (!chargingAnimation.isPlaying()) {
                chargingAnimation.refreshAnimation();
                boss.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(chargingAnimation);
                AudioManager.getInstance().addAudio(AudioEnums.ChargingLaserbeam);
            }

            if (chargingAnimation.isPlaying() &&
                    chargingAnimation.getCurrentFrame() == chargingAnimation.getTotalFrames() - 1) {
                createLaserbeams(boss);
                boss.setAllowedVisualsToRotate(false);
                trackingLaserbeam.update(); //Prevents the laserbeams from "jumping" to the right position by doing it before adding them to missilemanager
                chargingAnimation.setVisible(false);
                MissileManager.getInstance().addLaserBeam(trackingLaserbeam);
                startedFiringTime = currentTime;
                isFiringLaserbeams = true;
            }
            return false; //waiting for the laserbeams to finish
        }

        if (isFiringLaserbeams) {
            updateLaserbeamOriginPoints(boss);
            updateLaserbeamVisibility();
            if (!trackingLaserbeam.isVisible()) {
                trackingLaserbeam = null;
                boss.setAttacking(false);
                boss.setAllowedToMove(true);
                boss.setAllowedVisualsToRotate(true);
                rotateBossToPlayer(boss);
                playSmokeAnimation(boss); //Place it at the current location
                playSmokeAnimationAtFirstStep(boss); //Place it at the location the boss teleports back to
                isFiringLaserbeams = false;
                lastFiredTime = currentTime;
                return true;
            }
            return false;
        }

        return true; //Laserbeams should removed and this attack is finished
    }

    private void updateLaserbeamVisibility() {
        if (trackingLaserbeam != null && startedFiringTime + duration < GameState.getInstance().getGameSeconds()) {
            trackingLaserbeam.setVisible(false);
            laserbeamCloneEnemies.stream().forEach(clone -> clone.detonateClone());
            laserbeamCloneEnemies.clear();
        }
    }

    private void rotateBossToPlayer(GameObject boss){
        GameObject closestPlayer = PlayerManager.getInstance().getClosestSpaceShip(boss);
        boss.rotateGameObjectTowards(closestPlayer.getCenterXCoordinate(), closestPlayer.getCenterYCoordinate(), false);
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

    private void teleportBoss(Enemy boss) {
        Point teleportPoint = BoardBlockUpdater.getRandomCoordinateInBlock(boardBlockForTheRealStriker, boss.getWidth(), boss.getHeight());
        boss.setCenterCoordinates(teleportPoint.getX(), teleportPoint.getY());
    }

    private SpriteAnimation createChargingAnimation(Enemy boss) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(boss.getChargingUpAttackAnimation().getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(boss.getChargingUpAttackAnimation().getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.PinkLaserbeamCharging);

        SpriteAnimation chargingAnimation = new SpriteAnimation(new SpriteAnimationConfiguration(spriteConfiguration, 1, false));
        chargingAnimation.setAnimationScale(2f);
        chargingAnimation.setFrameDelay(10);
        return chargingAnimation;
    }

    private void updateLaserbeamOriginAnimation(Enemy boss) {
        chargingAnimation.setCenterCoordinates(
                boss.getChargingUpAttackAnimation().getCenterXCoordinate(),
                boss.getChargingUpAttackAnimation().getCenterYCoordinate()
        );
    }

    private void updateLaserbeamOriginPoints(Enemy boss) {
        if (trackingLaserbeam != null) {
            trackingLaserbeam.setOriginPoint(new Point(
                    boss.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4,
                    boss.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12
            ));
        }
    }

    private void createLaserbeams(Enemy boss) {
        float damage = boss.getDamage() * damageRatio;
        LaserbeamConfiguration upperLaserbeamConfiguration = new LaserbeamConfiguration(false, damage);
        upperLaserbeamConfiguration.setAmountOfLaserbeamSegments(laserbeamBodySegmentLength);
        upperLaserbeamConfiguration.setTargetToAimAt(PlayerManager.getInstance().getClosestSpaceShip(boss));
        upperLaserbeamConfiguration.setOriginPoint(new Point(
                boss.getCenterXCoordinate(),
                boss.getCenterYCoordinate()
        ));

        trackingLaserbeam = new TrackingLaserBeam(upperLaserbeamConfiguration);
        updateLaserbeamOriginPoints(boss);
        trackingLaserbeam.setOwner(boss);
        trackingLaserbeam.setMaxRotationPerUpdate(maxLaserbeamRotationsPerUpdate);
    }

    @Override
    public int getPriority() {
        return priority;
    }


    @Override
    public boolean isAvailable(Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= lastFiredTime + attackCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy);
    }
}
