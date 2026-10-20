package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasethree;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.FinalBossLaserbeamClone;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
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


/*
   Claude instructions.
   See FinalBossPhase2LaserbeamCloneAttack.java and FinalBossLaserbeamClone.java for an example of how to implement this.

   Key differences:
   - Do NOT teleport the player anywhere.
   - Do NOT spawn in a circle, instead in this formation (X are spawns, dots are empty space
            X.....X.....X.....X.....X.....X
            ...............................
            ...............................
            ...............................
            ...............................
            ...............................
            ...X.....X.....X.....X.....X...
   - The laserbeamclones are configured to attack themselves, so we only need to spawn them in the right position.
   I want the following variables to configure:
   1.) int xCoordinate startingCoordinate, int xCoordinate endingCoordinate
   2.) float xCoordinatedistanceBetweenEachSpawn = -boss.getWidth()/2f
   3.) int upperYCoordinate = 0 + 30;
   4.) int lowerYCoordinate = DataClass.getinstance.getplayablewindowheigt() - 30;

   Every spawn should share the same Y coordinate base on wether it is spawned up or down
   Every spawn should rotate to the other side of the screen (up spawns should look down, down spawns should look up)
   Every spawn should be between the startingXCoordinate which should be DataClass.getInstance.getWindowWidth() * 0.9f, endingStartingCoordinate should be DataClass.getInstance.getWindowWidth() * 0.15f
   Spawning should be randomized a bit, simply do this by making it a 50/50 flip wether to start spawning topside or bottomside
 */


public class FinalBossPhase3LaserbeamCloneAttack implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 15;
    private int priority = 2;

    private SpriteAnimation attackingAnimation;

    private int currentPhase = 0;
    private static int spawningClones = 1;
    private static int clonesAreFiringLaserbeams = 2;
    private static int finishedBehaviour = 3;

    //spawn formation
    private int startingXCoordinate;
    private int endingXCoordinate;
    private float xCoordinateDistanceBetweenEachSpawn;
    private int upperYCoordinate;
    private int lowerYCoordinate;

    private float currentSpawnXCoordinate;
    private boolean currentSpawnIsTopside;
    private double lastGameSecondsCloneSpawned = 0;
    private float cloneSpawnCooldown = 0.1f;
    private List<FinalBossLaserbeamClone> clones = new ArrayList<>();
    private Random random = new Random();

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (attackingAnimation == null) {
            initAttackAnimation(enemy);
        }


        if (currentPhase == 0 && enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            //step 1: play the attacking animation, once it finishes start spawning clones
            updateAttackAnimationLocation(enemy);
            if (!attackingAnimation.isPlaying()) {
                attackingAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(attackingAnimation);
            }


            if (attackingAnimation.isPlaying() && attackingAnimation.getCurrentFrame() == attackingAnimation.getTotalFrames() - 1) {
                initSpawnFormation(enemy);
                currentPhase = spawningClones;
            }
            return false; //We still running this behaviour
        } else if (currentPhase == spawningClones) {
            //step 2: spawn clones from right to left, alternating between the top and bottom of the screen.
            //The clones fire their laserbeam by themselves once spawned
            if (currentSpawnXCoordinate < endingXCoordinate) {
                currentPhase = clonesAreFiringLaserbeams;
            } else if (currentTime >= lastGameSecondsCloneSpawned + cloneSpawnCooldown) {
                spawnClone(enemy);
                currentSpawnXCoordinate += xCoordinateDistanceBetweenEachSpawn;
                currentSpawnIsTopside = !currentSpawnIsTopside;
                lastGameSecondsCloneSpawned = currentTime;
            }
            return false;
        } else if (currentPhase == clonesAreFiringLaserbeams) {
            //step 3: wait until every clone has finished firing its laserbeam
            if (clones.stream().noneMatch(clone -> clone.isAllowedToFire() || clone.isAttacking())) {
                currentPhase = finishedBehaviour;
            }
            return false;
        } else if (currentPhase == finishedBehaviour) {
            //step 4: despawn all clones
            resetAttack();
            lastAttackedTime = currentTime;
            enemy.setAttacking(false);
            return true; //We finished
        }
        return true; //We still running this behaviour

    }

    private void initSpawnFormation(Enemy enemy) {
        startingXCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.9f);
        endingXCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.1f);
        xCoordinateDistanceBetweenEachSpawn = -enemy.getWidth() * 0.825f;
        upperYCoordinate = 0 + 15;
        lowerYCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() - 15;
        cloneSpawnCooldown = 0.25f;

        currentSpawnXCoordinate = startingXCoordinate;
        currentSpawnIsTopside = random.nextBoolean();
    }

    private void spawnClone(Enemy enemy) {
        FinalBossLaserbeamClone clone = createClone(enemy, currentSpawnIsTopside);
        clones.add(clone);

        SpriteAnimation smokeAnimation = createSmokeAnim();
        smokeAnimation.setCenterCoordinates(clone.getCenterXCoordinate(), clone.getCenterYCoordinate());
        AnimationManager.getInstance().addUpperAnimation(smokeAnimation);
        EnemyManager.getInstance().addEnemy(clone);
    }

    private FinalBossLaserbeamClone createClone(Enemy enemy, boolean isTopside) {
        FinalBossLaserbeamClone clone = (FinalBossLaserbeamClone) EnemyCreator.createEnemy(EnemyEnums.FinalBossLaserbeamClone,
                Math.round(currentSpawnXCoordinate), upperYCoordinate, Direction.LEFT, enemy.getScale(), 1);
        clone.setOwnerOrCreator(enemy);
        clone.setDamage(enemy.getDamage());
        clone.setCurrentHitpoints(1000000);
        clone.setMaxHitPoints(1000000);
        clone.setAllowedToMove(false);

        //Top clones keep their top edge 30 pixels from the top, bottom clones keep their bottom edge 30 pixels from the bottom
        if (!isTopside) {
            clone.setCenterCoordinates(clone.getCenterXCoordinate(), lowerYCoordinate - clone.getHeight() / 2);
        }

        //Look at the other side of the screen: 90 degrees is straight down, 270 straight up
        int targetYCoordinate = isTopside ? DataClass.getInstance().getPlayableWindowMaxHeight() : 0;
        clone.rotateGameObjectTowards(clone.getCenterXCoordinate(), targetYCoordinate, false);
        clone.setLaserbeamAngle(isTopside ? 90 : 270);

        clone.setAllowedToFire(true);
        return clone;
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

    private void resetAttack() {
        currentPhase = 0;
        clones.forEach(FinalBossLaserbeamClone::detonateClone);
        clones.clear();
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

}
