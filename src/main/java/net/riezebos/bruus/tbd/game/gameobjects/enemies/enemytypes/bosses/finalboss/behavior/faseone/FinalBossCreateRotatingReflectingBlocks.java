package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.ReflectiveBlocks;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.util.OrbitingObjectsFormatter;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class FinalBossCreateRotatingReflectingBlocks implements BossActionable {
    private double lastAttackedTime = -500;
    private double attackCooldown = 0;
    private int priority = 99999;
    private boolean hasInitialized = false;
    private boolean hasRecenteredMissiles = false;
    //Phase 3 only: rebuilds the rings every refreshCooldown seconds. Phase 1 rings are never replenished
    private double refreshCooldown = 90;

    private SpriteAnimation attackingAnimation;

    // Preset values for ring formations of 2 and 3 wide. >4 is not supported in this implementation
    private static float[] ringDistances = {200, 275, 350, 425};
    private static boolean[] ringReversed = {false, true, false, true};
    private static int[] ringMissileAmounts = {20, 25, 30, 35};

    private int amountOfRings;
    private int bossPhase;

    public FinalBossCreateRotatingReflectingBlocks(int amountOfRings, int bossPhase) {
        this.amountOfRings = Math.min(amountOfRings, ringDistances.length);
        this.bossPhase = bossPhase;
    }

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        if (hasInitialized && isRefreshDue()) {
            removeRemainingRings(enemy);
            hasInitialized = false;
            hasRecenteredMissiles = false;
        }

        if (hasInitialized) {
            if (!hasRecenteredMissiles) {
                for (int i = 0; i < amountOfRings; i++) {
                    OrbitingObjectsFormatter.reformatOrbitingObjects(enemy, ringDistances[i], i, ringReversed[i]);
                }
                hasRecenteredMissiles = true;
            }
            return true; //early exit
        }


        double currentTime = GameState.getInstance().getGameSeconds();
        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            doTheThing(enemy);
            lastAttackedTime = currentTime;
            enemy.setAttacking(false);
            return true; //We finished
        }
        return true; //We still running this behaviour

    }


    private boolean isRefreshDue() {
        return bossPhase == FinalBoss.BOSSPHASE_3
                && GameState.getInstance().getGameSeconds() >= lastAttackedTime + refreshCooldown;
    }

    //Removes the blocks left from the previous rings, so the new rings are full and evenly spaced instead of stacked on the old ones
    private void removeRemainingRings(Enemy enemy) {
        for (int i = 0; i < amountOfRings; i++) {
            List<GameObject> ring = enemy.getOrbitingObjectsAtLayer(i);
            ring.forEach(block -> block.setVisible(false));
            ring.clear();
        }
    }

    private void teleportBoss(Enemy enemy) {
        if(bossPhase == FinalBoss.BOSSPHASE_1) {
            enemy.resetMovementPath();
            enemy.setAllowedToMove(false);
            enemy.setAllowedVisualsToRotate(false);
            int xCoordinate = Math.round((DataClass.getInstance().getWindowWidth() * 0.5f));
            int yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() / 2;
            enemy.setCenterCoordinates(xCoordinate, yCoordinate);
            enemy.move();
        }
    }

    private void doTheThing(Enemy enemy) {
        teleportBoss(enemy);
        createMissiles(enemy);
        hasInitialized = true;
    }


    private void createMissiles(Enemy enemy) {
        for (int i = 0; i < amountOfRings; i++) {
            createMissileRing(enemy, ringDistances[i], ringReversed[i], ringMissileAmounts[i], i);
        }
    }


    private void createMissileRing(Enemy enemy, float distance, boolean reverse, int amountOfMissiles, int missileLayerIndex) {
        List<ReflectiveBlocks> missilesToAdd = new ArrayList<>();
        for (int i = 0; i < amountOfMissiles; i++) {
            ReflectiveBlocks missile = createReflectiveMissiles(enemy);
            missile.resetMovementPath();
            OrbitPathFinder orbitPathFinder = new OrbitPathFinder(enemy);
            missile.getMovementConfiguration().setOrbitRadius(distance);
            orbitPathFinder.setReverse(reverse);
            missile.getMovementConfiguration().setPathFinder(orbitPathFinder);
            missile.setFinalBossProtectiveReflectiveBlock(true);
            missile.setOwnerOrCreator(enemy);
            missilesToAdd.add(missile);
        }

        missilesToAdd.forEach(missile -> enemy.addOrbitingObject(missile, missileLayerIndex));
        missilesToAdd.forEach(missile -> MissileManager.getInstance().addExistingMissile(missile));
    }

    private ReflectiveBlocks createReflectiveMissiles(Enemy enemy) {
        MissileEnums missileType = MissileEnums.ReflectiveBlocks;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(
                -500, -500,
                missileType.getImageType(), 0.1f);


        float movementSpeed = 1.75f;
        //Create missile movement attributes and create a movement configuration
        PathFinder missilePathFinder = new RegularPathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, Direction.LEFT
        );


        boolean isFriendly = false;
        float maxHitPoints = Math.min(60 * (GameState.getInstance().getDifficultyCoefficient()), 300); //max 500 hp
        float damage = enemy.getDamage() * 0.4f;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                damage, missileType.getDeathOrExplosionImageEnum(), isFriendly,
                false, false, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        ReflectiveBlocks missile = (ReflectiveBlocks) MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setMaxHitPoints(maxHitPoints);
        missile.setCurrentHitpoints(maxHitPoints);
        missile.setDamageable(true);
        missile.setDestructable(false);

        missile.setOwnerOrCreator(enemy);
        return missile;
    }

    //Distance of the widest ring that can exist, used by other attacks to stay clear of the rings
    public static float getOutermostRingDistance() {
        return ringDistances[ringDistances.length - 1];
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
                    && (!hasRecenteredMissiles || isRefreshDue())
                    && finalBoss.getBossPhase() == bossPhase
                    && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }

}
