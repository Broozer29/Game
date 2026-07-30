package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.yellowboss;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.yellowboss.behaviour.YellowBossLaserbeamMissileAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.yellowboss.behaviour.YellowBossMissileWaveAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.yellowboss.behaviour.YellowBossSpawnReflectingBarrier;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.YellowBossOrb;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class YellowBoss extends Enemy {

    private List<BossActionable> bossBehaviourList = new ArrayList<>();
    private BossActionable currentActiveBehavior = null;
    private double finishedAttackTime = 0;

    public YellowBoss(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 2, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.BossExplosion);
        destroyedExplosionfiguration.getSpriteConfiguration().setScale(1);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 9;
        this.allowedVisualsToRotate = false;
        this.knockbackStrength = 9;
        this.allowedToFire = false;


        YellowBossLaserbeamMissileAttack laserbeamMissileAttack = new YellowBossLaserbeamMissileAttack();
        bossBehaviourList.add(laserbeamMissileAttack);

        BossActionable missileWaveAttack = new YellowBossMissileWaveAttack();
        bossBehaviourList.add(missileWaveAttack);

        BossActionable reflectingBarrier = new YellowBossSpawnReflectingBarrier();
        bossBehaviourList.add(reflectingBarrier);

//        BossActionable spawnShurikens = new YellowBossSpawnShurikens();
//        bossBehaviourList.add(spawnShurikens);

        bossBehaviourList = bossBehaviourList.stream()
                .sorted(Comparator.comparingInt(BossActionable::getPriority).reversed())
                .collect(Collectors.toList());

        finishedAttackTime = GameState.getInstance().getGameSeconds();

    }


    @Override
    protected void updateChargingAttackAnimationCoordination() {
        if (this.chargingUpAttackAnimation != null) {
            this.chargingUpAttackAnimation.setCenterCoordinates(this.getXCoordinate() - (chargingUpAttackAnimation.getWidth() / 2), this.getCenterYCoordinate());
        }
    }


    @Override
    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();
    }

    @Override
    public void fireAction() {
        if (!allowedToFire && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            this.allowedToFire = true; // Boss is allowed to fire
            this.orbCreationAllowed = true;
        }

        updateChargingAttackAnimationCoordination();

        if (orbCreationAllowed && GameState.getInstance().getGameSeconds() >= lastGameSecondsOrbSpawned + orbCreationCooldown) {
            lastGameSecondsOrbSpawned = GameState.getInstance().getGameSeconds();
//            for(int i = 0; i < 2; i++) {
                createRandomOrb();
//            }
        }

        // If there's an active behavior, try to execute it
        if (currentActiveBehavior != null) {
            boolean isCompleted = currentActiveBehavior.activateBehaviour(this);
            if (isCompleted) {
                currentActiveBehavior = null; // Behavior finished, reset for next one
                finishedAttackTime = GameState.getInstance().getGameSeconds();
            } else {
                return; // If current behavior is still ongoing, stop further actions
            }
        }


        //Wait 0.5 seconds between attacks
        if (finishedAttackTime + 0.5 <= GameState.getInstance().getGameSeconds()) {
            // If no current behavior is active, find the next behavior to execute
            for (BossActionable bossActionable : bossBehaviourList) {
                // Attempt to execute the behavior, if available
                if (bossActionable.isAvailable(this)) {
                    boolean isCompleted = bossActionable.activateBehaviour(this);
                    if (!isCompleted) {
                        currentActiveBehavior = bossActionable; // Set this as the current active behavior
                        break; // Stop looking at other behaviors, only execute one at a time
                    }
                }
            }
        }
    }


    private double lastGameSecondsOrbSpawned = GameState.getInstance().getGameSeconds() + 8; //immediatly on cooldown
    private double orbCreationCooldown = 1;
    private boolean orbCreationAllowed = false;
    private void createRandomOrb() {
        MissileEnums missileType = MissileEnums.YellowBossOrb;
        Point spawnPoint = new Point(-50, random.nextInt(0, DataClass.getInstance().getPlayableWindowMaxHeight()));

        float rolledValue = random.nextFloat();
        boolean isHealOrb = false;
        if (rolledValue < 0.125f) {
            isHealOrb = true;
        }
        float scale = isHealOrb ? 0.7f : 0.65f;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(
                spawnPoint.getX(),
                spawnPoint.getY(),
                isHealOrb ? ImageEnums.YellowBossHealMissile : ImageEnums.YellowBossVoidMisisle, scale);


        float movementSpeed = isHealOrb ? 1.5f : 1.95f;
        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, Direction.RIGHT
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, this.getDamage(),
                isHealOrb ? null : ImageEnums.YellowBossVoidCollision,
                isFriendly,
                false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        YellowBossOrb yellowBossOrb = (YellowBossOrb) MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        yellowBossOrb.setScale(scale);
        yellowBossOrb.resetMovementPath();
        yellowBossOrb.setCenterCoordinates(spawnPoint.getX(), spawnPoint.getY());
        yellowBossOrb.getMovementConfiguration().setDestination(new Point(
                this.getCenterXCoordinate() - (yellowBossOrb.getWidth() / 2),
                this.getCenterYCoordinate() - (yellowBossOrb.getHeight() / 2)));
        yellowBossOrb.setAllowedVisualsToRotate(true);
        yellowBossOrb.rotateGameObjectTowards(yellowBossOrb.getCenterXCoordinate(), yellowBossOrb.getCenterYCoordinate(), false);
        yellowBossOrb.setOwnerOrCreator(this);
        yellowBossOrb.initOrbVersion(isHealOrb);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(yellowBossOrb);
    }
}
