package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo;

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
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;
import net.riezebos.bruus.tbd.game.util.OrbitingObjectsFormatter;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class FinalBossOrbitMissileAttack implements BossActionable {
    private double attackCooldown = 10;
    private double lastAttackedTime = 0 - attackCooldown;
    private int priority = 10;

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
            createRingOfMissiles(spaceShip, enemy);
        }
    }

    private static int layerIndex = 80085;
    private void createRingOfMissiles(SpaceShip spaceShip, Enemy enemy) {
        List<FinalBossOrbitMissile> listOfMissiles = new ArrayList<>();
        for (int i = 0; i < 360; i += 35) {
            FinalBossOrbitMissile missile = createMissile(spaceShip, enemy);
            listOfMissiles.add(missile);
            spaceShip.addOrbitingObject(missile, layerIndex); //magic number, make it a constant so only finalboss missiles are on this layer
        }

        OrbitingObjectsFormatter.reformatOrbitingObjects(spaceShip, 175f, layerIndex, true);
        double seconds = GameState.getInstance().getGameSeconds();
        listOfMissiles.forEach(missile -> {
            missile.setTimeLastStepFinished(seconds);
            MissileManager.getInstance().addExistingMissile(missile);});

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
        movementConfiguration.setPathFinder(new OrbitPathFinder(spaceShip));
        movementConfiguration.setLastKnownTargetX(spaceShip.getCenterXCoordinate());
        movementConfiguration.setLastKnownTargetY(spaceShip.getCenterYCoordinate());
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
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_2
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
