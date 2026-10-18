package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasethree;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.finalboss.FinalBossOrbitMissile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class straightLineMissileAttack implements BossActionable {
    private double attackCooldown = 7;
    private double lastAttackedTime = 0 - attackCooldown;
    private int priority = 2;

    private SpriteAnimation attackingAnimation;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();

        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            doTheThing(enemy);
            lastAttackedTime = currentTime;
            return true; //We finished
        }
        return true; //We still running this behaviour
    }


    private void doTheThing(Enemy enemy) {
        for (SpaceShip spaceShip : PlayerManager.getInstance().getAllSpaceShips()) {
            createLineOfMissiles(spaceShip, enemy);
        }
    }

    private static int numberOfMissiles = 6;
    private static float distanceFromPlayer = 140;
    private static float spaceBetweenMissiles = 50;
    private static float upwardsShift = 100; // Moves the whole line up by this many pixels, a negative value moves it down
    private Random random = new Random();

    private void createLineOfMissiles(SpaceShip spaceShip, Enemy enemy) {
        // -1 places a "/" to the left of the player, 1 places a "\" to the right of the player
        int side = random.nextBoolean() ? -1 : 1;
        float diagonalStepSize = spaceBetweenMissiles / (float) Math.sqrt(2);

        // The middle missile is level with the player, numberOfMissiles counts it plus the missiles on each half of the line (2 gives 3 missiles, 3 gives 5)
        List<FinalBossOrbitMissile> listOfMissiles = new ArrayList<>();
        listOfMissiles.add(createLineMissile(spaceShip, enemy, side, 0));
        for (int i = 1; i < numberOfMissiles; i++) {
            float step = i * diagonalStepSize;
            listOfMissiles.add(createLineMissile(spaceShip, enemy, side, step)); // upwards, towards the player
            listOfMissiles.add(createLineMissile(spaceShip, enemy, side, -step)); // downwards, away from the player
        }

        double seconds = GameState.getInstance().getGameSeconds();
        listOfMissiles.forEach(missile -> {
            missile.setTimeLastStepFinished(seconds);
            MissileManager.getInstance().addExistingMissile(missile);
        });
    }

    private FinalBossOrbitMissile createLineMissile(SpaceShip spaceShip, Enemy enemy, int side, float stepSize) {
        FinalBossOrbitMissile missile = createMissile(spaceShip, enemy);
        missile.followTargetWithOffset(side * (distanceFromPlayer - stepSize), -stepSize - upwardsShift);
        return missile;
    }

    private FinalBossOrbitMissile createMissile(SpaceShip spaceShip, Enemy enemy) {
        float moveSpeed = 2;
        float scale = 0.75f;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(0);
        spriteConfiguration.setyCoordinate(0);
        spriteConfiguration.setScale(scale);
        spriteConfiguration.setImageType(MissileEnums.FinalBossChargingMissile.getImageType()); //todo placeholder

        MissileConfiguration missileConfiguration = new MissileConfiguration();
        missileConfiguration.setDamage(enemy.getDamage()); //placeholder
        missileConfiguration.setMissileType(MissileEnums.FinalBossChargingMissile);

        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setMovementSpeed(moveSpeed);
        movementConfiguration.setPathFinder(new StraightLinePathFinder());
        movementConfiguration.setDirection(Direction.RIGHT);

        FinalBossOrbitMissile missile = (FinalBossOrbitMissile) MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(enemy);
        missile.setTarget(spaceShip);
        missile.setTransparancyAlpha(true, 0.2f, 0.05f);
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
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_3
                    && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }

    public double getLastAttackTime() {
        return lastAttackedTime;
    }

    public void setLastAttackTime(double lastAttackTime) {
        this.lastAttackedTime = lastAttackTime;
    }
}
