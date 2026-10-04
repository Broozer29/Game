package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class FinalBossLaserbeamCloneAttack implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 10;
    private int priority = 2;


    private int currentPhase = 0;
    private static int isSpawningClones = 1;
    private static int clonesAreFiringLaserbeams = 2;
    private static int clonesHaveFinishedFiringLaserbeams = 3;
    private static int finishedBehaviour = 4;
    public static float damageRatio = 0.5f;


    private int angleBetweenClones = 15;
    private int currentSpawnCloneAngle = 0;
    private double lastGameSecondsCloneSpawned = 0;
    private float cloneSpawnCooldown = 0.15f;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();

        if (currentPhase == 0 && enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            //step 1: teleport player to the center, play a smoke animation at the destination and players current position
            if (currentSpawnCloneAngle >= 360) {
                currentPhase = isSpawningClones;
            }
            if (currentSpawnCloneAngle <= 360 && currentTime >= lastGameSecondsCloneSpawned + cloneSpawnCooldown) {
//                spawnClone(currentSpawnCloneAngle);
                currentSpawnCloneAngle += angleBetweenClones;
                lastGameSecondsCloneSpawned = currentTime;
            }
            return false; //We still running this behaviour
        } else if (currentPhase == isSpawningClones) {
            //step 2: create finalbosslaserbeamclones in a circle around the player, set their allowedToMove to false and rotate them
            // towards the center (where the player is teleported)
            currentPhase = clonesAreFiringLaserbeams;
            return false;
        } else if (currentPhase == clonesAreFiringLaserbeams) {
            //step 3: fire the finalbosslaserbeamclones in the order that they were spawned in
            currentPhase = clonesHaveFinishedFiringLaserbeams;
            return false;
        } else if (currentPhase == clonesHaveFinishedFiringLaserbeams) {
            //step 4: if the final clone has fired their laserbeam, despawn them all
            currentPhase = finishedBehaviour;
            return false;
        }

        /*
            Clones should never show healthbar and be indestructible
            Player should be boxed in by the clones, he should not be able to escape the surround
         */

        if (currentPhase == finishedBehaviour) {
            lastAttackedTime = currentTime;
            enemy.setAttacking(false);
            return true;
        }
        return true; //We still running this behaviour

    }

    private void spawnClone(int angleDegrees, Enemy enemy) {
        Point spawningPoint = getPointOnCircle(angleDegrees, 300, getCenterPoint());
        SpriteAnimation smokeAnimation = createSmokeAnim();
        FinalBossLaserbeamClone clone = createClone(angleDegrees, spawningPoint, enemy);
        smokeAnimation.setCenterCoordinates(clone.getCenterXCoordinate(), clone.getCenterYCoordinate());

        AnimationManager.getInstance().addUpperAnimation(smokeAnimation);
        EnemyManager.getInstance().addEnemy(clone);
    }

    private FinalBossLaserbeamClone createClone(int angleDegrees, Point spawningPoint, Enemy enemy) {
        FinalBossLaserbeamClone finalBossLaserbeamClone = (FinalBossLaserbeamClone) EnemyCreator.createEnemy(EnemyEnums.FinalBossLaserbeamClone, spawningPoint.getX(), spawningPoint.getY(), Direction.LEFT,
                enemy.getScale(), 1);
        finalBossLaserbeamClone.setOwnerOrCreator(enemy);
        finalBossLaserbeamClone.setDamage(enemy.getDamage());
        finalBossLaserbeamClone.setCurrentHitpoints(1000000);
        finalBossLaserbeamClone.setMaxHitPoints(1000000);

        int laserbeamAngleDegrees = angleDegrees - 360;
        if (laserbeamAngleDegrees < 0) {
            laserbeamAngleDegrees += 360;
        }

        finalBossLaserbeamClone.setLaserbeamAngle(laserbeamAngleDegrees);
        return finalBossLaserbeamClone;
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

    private Point getPointOnCircle(int angleDegrees, float distance, Point centerPoint) {
        double angleRadians = Math.toRadians(angleDegrees);
        double x = centerPoint.getX() + distance * Math.cos(angleRadians);
        double y = centerPoint.getY() + distance * Math.sin(angleRadians);
        return new Point(x, y);
    }

    private Point getCenterPoint() {
        return new Point(DataClass.getInstance().getWindowWidth() / 2, DataClass.getInstance().getPlayableWindowMaxHeight() / 2);
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
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_2
                    && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }
}
