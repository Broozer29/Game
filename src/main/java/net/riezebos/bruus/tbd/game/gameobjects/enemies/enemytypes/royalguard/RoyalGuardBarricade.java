package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.royalguard;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoyalGuardBarricade extends Enemy {

    private List<RoyalGuardBarricadeMinion> minions = new ArrayList<>();
    private int maxMinionCount = 6;
    private double lastTimeMinionSpawned = 0;
    private double minionSpawnCooldown = 3;
    private int attackRange = 350;
    private GameObject target = null;
    private SpriteAnimation warpInAnimation = null;

    public RoyalGuardBarricade(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1f);
        this.damage = 12;
        this.attackSpeed = 2;
        this.knockbackStrength = 8;
        this.warpInAnimation = initSpawnAnimation(this);

    }

    private int gameTickPerformanceSaver = 5; //every 5 gameticks allow to search for a target
    private int gameTickPerformanceCounter = 0;
    private boolean spawnMinion = false;

    @Override
    public void fireAction() {
        // Check if the attack cooldown has been reached
        double currentTime = GameState.getInstance().getGameSeconds();
        if (gameTickPerformanceCounter % gameTickPerformanceSaver == 0 || (target == null || !target.isVisible())) {
            target = PlayerManager.getInstance().getClosestSpaceShip(this); //relock to the closest target
        }
        gameTickPerformanceCounter++;

        if (isTooFarAway()) { //if the target is too far away, lose the target lock
            target = null;
        }

        minions.removeIf(minion -> !minion.isVisible() || minion.getCurrentHitpoints() <= 0);

        //spawn een minion als er ruimte is
        if (currentTime >= lastTimeMinionSpawned + minionSpawnCooldown && minions.size() < maxMinionCount && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            this.warpInAnimation.refreshAnimation();
            this.warpInAnimation.setCenterCoordinates(this.movementConfiguration.getRotation().equals(Direction.LEFT) ? this.getXCoordinate() - 10 : this.getXCoordinate() + this.getWidth() + 15, this.getCenterYCoordinate());
            AnimationManager.getInstance().addUpperAnimation(this.warpInAnimation);
            spawnMinion = true;
            lastTimeMinionSpawned = currentTime;
        }

        if (spawnMinion && warpInAnimation != null && warpInAnimation.isPlaying() && warpInAnimation.getCurrentFrame() >= warpInAnimation.getTotalFrames() - 4) {
            RoyalGuardBarricadeMinion minion = createMinion(this);
            this.minions.add(minion);
            spawnMinion = false;
            EnemyManager.getInstance().addEnemy(minion);
        }

        //throw een minion als er een minion is EN een speler dichtbij genoeg is
        if (allowedToFire && target != null && !minions.isEmpty() && currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            RoyalGuardBarricadeMinion minionToThrow = getClosestMinionToTarget(target);
            if (minionToThrow != null) {
                minionToThrow.throwAtTarget(target);
                minions.remove(minionToThrow); //thrown it so we lose it now
                lastAttackTime = currentTime;
            }
        }

    }

    private RoyalGuardBarricadeMinion getClosestMinionToTarget(GameObject target) {
        RoyalGuardBarricadeMinion closestMinion = null;
        double minDistance = Double.MAX_VALUE;

        for (RoyalGuardBarricadeMinion minion : minions) {
            if (minion.isWarmingUp()) {
                continue;
            }
            double distance = ProtossUtils.getDistanceToRectangle(minion.getCenterXCoordinate(), minion.getCenterYCoordinate(), target.getBounds());
            if (distance < minDistance) {
                minDistance = distance;
                closestMinion = minion;
            }
        }

        return closestMinion;
    }

    private boolean isTooFarAway() {
        int attackRangeToCheck = attackRange;
        if (target != null) {
            attackRangeToCheck += 35; //voorkomt het constant roteren van locking/losing lock
        }

        Rectangle targetBounds = target.getBounds();
        double distance = ProtossUtils.getDistanceToRectangle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetBounds);
        return distance > attackRangeToCheck;
    }


    private SpriteAnimation initSpawnAnimation(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(this.movementConfiguration.getRotation().equals(Direction.LEFT) ? enemy.getXCoordinate() : enemy.getXCoordinate() + enemy.getWidth() + 15);
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        SpriteAnimation spawnAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spawnAnimation.setAnimationScale(0.3f);
        spawnAnimation.setCenterCoordinates(this.movementConfiguration.getRotation().equals(Direction.LEFT) ? enemy.getXCoordinate() : enemy.getXCoordinate() + enemy.getWidth() + 15, enemy.getCenterYCoordinate());
        return spawnAnimation;
    }

    private RoyalGuardBarricadeMinion createMinion(Enemy enemy) {
        EnemyEnums enemyEnums = EnemyEnums.RoyalGuardBarricadeMinion;
        RoyalGuardBarricadeMinion minion = (RoyalGuardBarricadeMinion) EnemyCreator.createEnemy(enemyEnums, this.getChargingUpAttackAnimation().getCenterXCoordinate(), this.getChargingUpAttackAnimation().getCenterYCoordinate(), Direction.LEFT,
                enemyEnums.getDefaultScale(), enemyEnums.getMovementSpeed());

        //Scout should immediatly change its move config upon spawning, so its responsible itself for surrounding the carrier
        minion.setOwnerOrCreator(enemy);
        updateChargingAttackAnimationCoordination();
        minion.setCenterCoordinates(this.movementConfiguration.getRotation().equals(Direction.LEFT) ? enemy.getXCoordinate() : enemy.getXCoordinate() + enemy.getWidth() + 5, enemy.getCenterYCoordinate());
        minion.getMovementConfiguration().setPathFinder(new DestinationPathFinder());
        minion.updateMovementPath();
        return minion;
    }

}