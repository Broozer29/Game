package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone;

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
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class FinalBossCreateRotatingReflectingBlocks implements BossActionable {
    private double lastAttackedTime = -50;
    private double attackCooldown = 0;
    private int priority = 99999;
    private boolean hasInitialized = false;
    private boolean hasRecenteredMissiles = false;

    private SpriteAnimation attackingAnimation;

    private static float firstRingDistance = 200;
    private static float secondRingDistance = 275;
    private static float thirdRingDistance = 350;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        if (hasInitialized) {
            if (!hasRecenteredMissiles) {
                OrbitingObjectsFormatter.reformatOrbitingObjects(enemy, firstRingDistance, 0, false);
                OrbitingObjectsFormatter.reformatOrbitingObjects(enemy, secondRingDistance, 1, true);
                OrbitingObjectsFormatter.reformatOrbitingObjects(enemy, thirdRingDistance, 2, false);
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

    private void teleportBoss(Enemy enemy) {
        enemy.resetMovementPath();
        enemy.setAllowedToMove(false);
        enemy.setAllowedVisualsToRotate(false);
        int xCoordinate = Math.round((DataClass.getInstance().getWindowWidth() * 0.5f));
        int yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() / 2;
        enemy.setCenterCoordinates(xCoordinate, yCoordinate);
        enemy.move();
    }

    private void doTheThing(Enemy enemy) {
        teleportBoss(enemy);
        createMissiles(enemy);
        hasInitialized = true;
    }


    private void createMissiles(Enemy enemy) {
        createMissileRing(enemy, firstRingDistance, false, 20, 0);
        createMissileRing(enemy, secondRingDistance, true, 25, 1);
        createMissileRing(enemy, thirdRingDistance, false, 30, 2);
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
        int maxHitPoints = 300;
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
                    && !hasRecenteredMissiles
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_1
                    && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }

}
