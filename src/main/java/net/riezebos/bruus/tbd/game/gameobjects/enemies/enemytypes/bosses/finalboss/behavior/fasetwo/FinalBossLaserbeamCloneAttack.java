package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FinalBossLaserbeamCloneAttack implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 30;
    private int priority = 20;


    private int currentPhase = 0;
    private static int clonesAreFiringLaserbeams = 2;
    private static int clonesHaveFinishedFiringLaserbeams = 3;
    private static int finishedBehaviour = 4;
    public static float damageRatio = 0.5f;

    //step 1 stuff
    private int angleBetweenClones = 15;
    private float distanceFromCenter = 400 * DataClass.getInstance().getResolutionFactor();
    private int currentSpawnCloneAngle = 0;
    private double lastGameSecondsCloneSpawned = 0;
    private float cloneSpawnCooldown = 0.05f;
    private boolean hasTeleportedPlayer = false;
    private List<FinalBossLaserbeamClone> clones = new ArrayList<>();


    //step 2 stuff
    private double timeBetweenLaserStart = 0.35f;
    private double lastTimeLaserStarted = 0;
    private int laserbeamFireIndex = 0;
    private int laserbeamFireDirection = 1; // 1 = clockwise, -1 = counter clockwise
    private int clonesFired = 0;
    private boolean isLastCloneFiringLaserbeam = false;
    private Random random = new Random();
    public static double laserbeamFiringTime = 4;


    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();

        if (currentPhase == 0 && enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            //step 1: teleport player to the center, play a smoke animation at the destination and players current position
            if (currentSpawnCloneAngle >= 360) {
                currentPhase = clonesAreFiringLaserbeams;
            }
            if (currentSpawnCloneAngle <= 360 && currentTime >= lastGameSecondsCloneSpawned + cloneSpawnCooldown) {
                spawnClone(currentSpawnCloneAngle, enemy);

                if (!hasTeleportedPlayer) {
                    teleportPlayersToCenter(enemy);
                    hasTeleportedPlayer = true;
                }
                currentSpawnCloneAngle += angleBetweenClones;
                lastGameSecondsCloneSpawned = currentTime;
                lastTimeLaserStarted = currentTime;
            }
            return false; //We still running this behaviour
        } else if (currentPhase == clonesAreFiringLaserbeams && enemy.isAllowedToFire()) {
            //step 2: create finalbosslaserbeamclones in a circle around the player, set their allowedToMove to false and rotate them
            // towards the center (where the player is teleported)
            if(currentTime - lastTimeLaserStarted > timeBetweenLaserStart){
                fireCloneLaserbeam(enemy);
                lastTimeLaserStarted = currentTime;
            }
            if(isLastCloneFiringLaserbeam && currentTime < lastTimeLaserStarted + (laserbeamFiringTime - 1)){
                currentPhase = clonesHaveFinishedFiringLaserbeams;
            }
            return false;
        } else if (currentPhase == clonesHaveFinishedFiringLaserbeams) {
            //step 3: fire the finalbosslaserbeamclones in the order that they were spawned in
            currentPhase = finishedBehaviour;
            return false;
        } else if (currentPhase == finishedBehaviour) {
            //step 4: if the final clone has fired their laserbeam, despawn them all
            resetAttack();
            lastAttackedTime = currentTime;
            enemy.setAttacking(false);
            return true;
        }

        return true; //We still running this behaviour

    }

    private void spawnClone(int angleDegrees, Enemy enemy) {
        Point spawningPoint = getPointOnCircle(angleDegrees, distanceFromCenter, getCenterPoint(enemy));
        SpriteAnimation smokeAnimation = createSmokeAnim();
        FinalBossLaserbeamClone clone = createClone(angleDegrees, spawningPoint, enemy);
        clones.add(clone);
        smokeAnimation.setCenterCoordinates(clone.getCenterXCoordinate(), clone.getCenterYCoordinate());
        clone.setAllowedToMove(false);

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

        float laserbeamAngleDegrees = angleDegrees - 180;
        if (laserbeamAngleDegrees < 0) {
            laserbeamAngleDegrees += 360;
        }

        finalBossLaserbeamClone.rotateGameObjectTowards(getCenterPoint(enemy).getX(), getCenterPoint(enemy).getY(), false);

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

    private Point getCenterPoint(Enemy enemy) {
        return new Point(
                DataClass.getInstance().getWindowWidth() * 0.5 - (enemy.getWidth() * 0.5),
                DataClass.getInstance().getPlayableWindowMaxHeight() * 0.5 - (enemy.getHeight() * 0.5)
        );
    }

    private void teleportPlayersToCenter(Enemy enemy) {
        Point centerPoint = getCenterPoint(enemy);
        for (SpaceShip spaceShip : PlayerManager.getInstance().getAllSpaceShips()) {
            spaceShip.setCenterCoordinates(centerPoint.getX(), centerPoint.getY());
            SpriteAnimation smokeAnimation = createSmokeAnim();
            smokeAnimation.setCenterCoordinates(centerPoint.getX(), centerPoint.getY());
            AnimationManager.getInstance().addUpperAnimation(smokeAnimation);
        }
    }

    //AI slopped all over it, starts at a random index and goes in a random direction
    private void fireCloneLaserbeam(Enemy enemy){
        if (clones.isEmpty() || isLastCloneFiringLaserbeam) {
            return;
        }

        if (clonesFired == 0) {
            //Randomize the starting clone and the direction so the pattern isn't predictable
            laserbeamFireIndex = random.nextInt(clones.size());
            laserbeamFireDirection = random.nextBoolean() ? 1 : -1;
        }

        clones.get(laserbeamFireIndex).setAllowedToFire(true);
        clonesFired++;
        //Wrap around in both directions, e.g. start at 15 going up ends at 14
        laserbeamFireIndex = Math.floorMod(laserbeamFireIndex + laserbeamFireDirection, clones.size());

        if(clonesFired >= clones.size()){
            isLastCloneFiringLaserbeam = true;
        }
    }

    private void resetAttack(){
        laserbeamFireIndex = 0;
        laserbeamFireDirection = 1;
        clonesFired = 0;
        isLastCloneFiringLaserbeam = false;
        currentSpawnCloneAngle = 0;
        currentPhase = 0;
        hasTeleportedPlayer = false;
        clones.forEach(FinalBossLaserbeamClone::detonateClone);
        clones.clear();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getLastAttackedTime() {
        return lastAttackedTime;
    }

    public void setLastAttackedTime(double lastAttackedTime) {
        this.lastAttackedTime = lastAttackedTime;
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
