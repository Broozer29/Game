package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone.FinalBossCreateRotatingReflectingBlocks;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone.FinalBossPeriodicMissileBarrage;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone.FinalBossPhaseOneLaserbeamAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.ReflectiveBlocks;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.util.ArmorCalculator;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FinalBoss extends Enemy {

    private List<BossActionable> bossBehaviourList = new ArrayList<>();
    private BossActionable currentActiveBehavior = null;
    private double finishedAttackTime = -1;
    private int bossPhase = BOSSPHASE_1;

    public static int BOSSPHASE_1 = 1;
    public static int BOSSPHASE_2 = 2;
    public static int BOSSPHASE_3 = 3;

    private AudioEnums firstPhaseAudioEnum = AudioEnums.FinalBossPhase1;
    private AudioEnums secondPhaseAudioEnum = AudioEnums.FinalBossPhase2;
    private AudioEnums thirdPhaseAudioEnum = AudioEnums.FinalBossPhase3;
    private FinalBossBarker bossBarker = new FinalBossBarker();
    private FinalBossPeriodicMissileBarrage finalBossPeriodicMissileBarrage = new FinalBossPeriodicMissileBarrage();

    public FinalBoss(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 2, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.BossExplosion);
        destroyedExplosionfiguration.getSpriteConfiguration().setScale(4);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.knockbackStrength = 9;

        this.setAllowedToMove(false);

        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.15f);

        //phase 1 abilities
        FinalBossCreateRotatingReflectingBlocks finalBossCreateRotatingReflectingBlocks = new FinalBossCreateRotatingReflectingBlocks();
        bossBehaviourList.add(finalBossCreateRotatingReflectingBlocks);

        FinalBossPhaseOneLaserbeamAttack finalBossPhaseOneLaserbeamAttack = new FinalBossPhaseOneLaserbeamAttack();
        bossBehaviourList.add(finalBossPhaseOneLaserbeamAttack);

        //phase 2 abilities
        //phase 3 abilities

        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder) {
            hoverPathFinder.setSecondsToHoverStill(0);
            hoverPathFinder.setShouldDecreaseBoardBlock(true);
        }


        bossBehaviourList = bossBehaviourList.stream()
                .sorted(Comparator.comparingInt(BossActionable::getPriority).reversed())
                .collect(Collectors.toList());

    }

    @Override
    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();
    }


    @Override
    protected void updateChargingAttackAnimationCoordination() {
        if (this.chargingUpAttackAnimation != null) {
            this.chargingUpAttackAnimation.setCenterCoordinates(this.getXCoordinate() - (chargingUpAttackAnimation.getWidth() / 2), this.getCenterYCoordinate());
        }
    }

    @Override
    public void fireAction() {
        if (!allowedToFire && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            this.allowedToFire = true; // Boss is allowed to fire
        }

        if (getBossPhase() == BOSSPHASE_1) {
            firePhaseOnePassiveAbilities();
        }

        attemptBark();


        //todo dit elke call weer op true zetten is een mega code smell en ik snap niet waarom deze baas het nodig heeft en mothershipminiboss niet
        this.setAllowedVisualsToRotate(true);
        this.rotateGameObjectTowards(PlayerManager.getInstance().getClosestSpaceShip(this));
        this.setAllowedVisualsToRotate(false);

        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder) {
            if (this.getCurrentBoardBlock() <= 2) { // if its reaches 0 it will move out of bounds
                hoverPathFinder.setDecreaseBoardBlockAmountBy(-2);
            } else if (this.getCurrentBoardBlock() >= 6) {
                hoverPathFinder.setDecreaseBoardBlockAmountBy(2);
            }
        }

        updateChargingAttackAnimationCoordination();

        // If there's an active behavior, try to execute it
        if (currentActiveBehavior != null) {
            boolean isCompleted = currentActiveBehavior.activateBehaviour(this);
            if (isCompleted) {
                finishedAttackTime = GameState.getInstance().getGameSeconds();
                currentActiveBehavior = null; // Clear the completed behavior
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


    public int getBossPhase() {
        return bossPhase;
    }

    private float spawnCornerReflectingBlocksCooldown = 0.5f;
    private double lastGameSecondsMissileSpawned = -1;

    private void firePhaseOnePassiveAbilities() {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastGameSecondsMissileSpawned + spawnCornerReflectingBlocksCooldown) {
            firePhaseOneReflectingBarriers();
            lastGameSecondsMissileSpawned = currentTime;
        }
        finalBossPeriodicMissileBarrage.activateBehaviour(this);
    }

    private void firePhaseOneReflectingBarriers() {
        MissileManager.getInstance().addExistingMissile(createMissile(10, 20, Direction.RIGHT, Direction.UP));
        MissileManager.getInstance().addExistingMissile(createMissile(DataClass.getInstance().getWindowWidth() - 50, -10, Direction.DOWN, Direction.RIGHT));
        MissileManager.getInstance().addExistingMissile(createMissile(DataClass.getInstance().getWindowWidth() + 15, DataClass.getInstance().getPlayableWindowMaxHeight() - 50, Direction.LEFT, Direction.UP));
        MissileManager.getInstance().addExistingMissile(createMissile(0, DataClass.getInstance().getPlayableWindowMaxHeight() + 15, Direction.UP, Direction.RIGHT));
    }

    private Missile createMissile(int xCoordinate, int yCoordinate, Direction directionToMoveIn, Direction directionToRotateIn) {
        MissileEnums missileType = MissileEnums.ReflectiveBlocks;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(
                xCoordinate, yCoordinate,
                missileType.getImageType(), 0.1f);


        float movementSpeed = 1.75f;
        //Create missile movement attributes and create a movement configuration
        PathFinder missilePathFinder = new RegularPathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, directionToMoveIn
        );


        boolean isFriendly = false;
        int maxHitPoints = 300;
        float damage = this.getDamage() * 0.4f;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                damage, missileType.getDeathOrExplosionImageEnum(), isFriendly,
                false, false, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        ReflectiveBlocks missile = (ReflectiveBlocks) MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setMaxHitPoints(maxHitPoints);
        missile.setCurrentHitpoints(maxHitPoints);
        missile.setDamageable(true);
        missile.setDestructable(false);
        Point destination = new Point(missile.getMovementConfiguration().getDestination().getX(), missile.getMovementConfiguration().getDestination().getY());
        missile.resetMovementPath();

        missile.setAllowedVisualsToRotate(true);
        missile.rotateGameObjectTowards(directionToRotateIn, true);
        missile.setAllowedVisualsToRotate(false);
        missile.setCenterCoordinates(xCoordinate, yCoordinate);

        missile.setOwnerOrCreator(this);
        return missile;
    }

    public void takeDamage(float damageTaken) {
        if (currentHitpoints - damageTaken <= 0) {
            if (this.getBossPhase() == BOSSPHASE_1 || this.getBossPhase() == BOSSPHASE_2) {
                changePhaseToNextPhase();
                this.currentHitpoints = this.maxHitPoints;
                return;
            }
        }

        if (damageTaken > 0) {
            damageTaken = (ArmorCalculator.calculateDamage(damageTaken, this) * damageReductionMultiplier);
        }

        if (damageTaken > 0) {
            lastGameSecondDamageTaken = GameState.getInstance().getGameSeconds();
        }

        this.currentHitpoints -= damageTaken;
        if (currentHitpoints > maxHitPoints) {
            currentHitpoints = maxHitPoints;
        }

        if (this.currentHitpoints <= 0) {
            if (this.xpOnDeath > 0) {
                PlayerStats.getInstance().addXP(xpOnDeath);
            }
            if (this.destructionAnimation != null) {
                this.destructionAnimation.setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
//                this.destructionAnimation.rotateAnimation(this.rotationAngle, false);
                AnimationManager.getInstance().addUpperAnimation(destructionAnimation);
            }

            for (GameObject object : objectsFollowingThis) {
                if (object.isVisible()) {
                    object.setCashMoneyWorth(0);
                    object.takeDamage(object.getMaxHitPoints() * 5);
                }
            }

            for (GameObject object : getAllOrbitingObjects()) {
                if (object.isVisible()) {
                    object.setCashMoneyWorth(0);
                    object.takeDamage(object.getMaxHitPoints() * 5);
                }
            }

            AudioManager.getInstance().addAudio(deathSound);

            triggerOnDeathActions();

            this.setVisible(false);
            activateOnDeathEffects();
            PlayerInventory.getInstance().addMinerals(this.cashMoneyWorth);

            //todo end the game and return to main menu

        }
    }

    private void changePhaseToNextPhase() {
        if (bossPhase == BOSSPHASE_1) {
            bossPhase = BOSSPHASE_2;
            //Clear all hostile missiles to make room for then next phase
            attemptPhaseTransitionBark();
            MissileManager.getInstance().getMissiles().stream().filter(missile -> !missile.isFriendly()).forEach(
                    missile -> missile.setTransparancyAlpha(true, missile.getTransparancyAlpha(), -0.05f)
            );
            AudioManager.getInstance().playDefaultBackgroundMusicForALevel(secondPhaseAudioEnum, true);
        } else if (bossPhase == BOSSPHASE_2) {
            attemptPhaseTransitionBark();
            bossPhase = BOSSPHASE_3;
            AudioManager.getInstance().playDefaultBackgroundMusicForALevel(thirdPhaseAudioEnum, true);
        }
    }

    private void attemptBark() {
        attemptGreetingBark();
        attemptLowHealthBark();
        attemptTauntBark();
    }

    private double barkCooldown = 30;
    private double lastGameSecondsBarked = 0;

    private void attemptTauntBark() {
        if (lastGameSecondsBarked + barkCooldown < GameState.getInstance().getGameSeconds() && !AudioManager.getInstance().bossIsBarking(this)) {
            AudioManager.getInstance().addAudio(bossBarker.getRandomTauntBark());
            lastGameSecondsBarked = GameState.getInstance().getGameSeconds();
        }
    }

    private void attemptPhaseTransitionBark() {
        if (!AudioManager.getInstance().bossIsBarking(this)) {
            AudioManager.getInstance().addAudio(bossBarker.getRandomPhaseTransitionBark());
            lastGameSecondsBarked = GameState.getInstance().getGameSeconds();
        }
    }

    private void attemptLowHealthBark() {
        //tl;dr, if there is a player with 0 shield and <= 30% hitpoints, bark low health
        if (!AudioManager.getInstance().bossIsBarking(this) && PlayerManager.getInstance().getAllSpaceShips().stream().filter(spaceShip -> spaceShip.getCurrentShieldPoints() <= 0 && spaceShip.getCurrentHitpoints() < (spaceShip.getMaxHitPoints() * 0.3f)).count() > 0) {
            AudioManager.getInstance().addAudio(bossBarker.getLowHealthBark());
            lastGameSecondsBarked = GameState.getInstance().getGameSeconds();
        }
    }


    //should be called from gameboard
    public void attemptMockingBark() {
        if (!AudioManager.getInstance().bossIsBarking(this)) {
            AudioManager.getInstance().addAudio(bossBarker.getRandomMockBark());
            //should this have a cooldown? its supposed to be played when the player dies, so a cooldown makes no sense right?
        }
    }

    private boolean hasGreeted = false;
    public void attemptGreetingBark() {
        if (!AudioManager.getInstance().bossIsBarking(this) && !hasGreeted) {
            AudioManager.getInstance().addAudio(bossBarker.getRandomGreetingBark());
            lastGameSecondsBarked = GameState.getInstance().getGameSeconds();
            hasGreeted = true;
        }
    }

    public static void initBossAssets() {
        preloadReflectingBarrier();
    }

    private static void preloadReflectingBarrier() {
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
        float damage = 0;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                damage, missileType.getDeathOrExplosionImageEnum(), isFriendly,
                false, false, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        ReflectiveBlocks missile = (ReflectiveBlocks) MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setMaxHitPoints(maxHitPoints);
        missile.setCurrentHitpoints(maxHitPoints);
        missile.setDamageable(true);
        missile.setDestructable(false);

        for (int i = 0; i < 360; i++) {
            missile.rotateObjectTowardsAngle(i, true);
        }
    }

    public FinalBossBarker getBossBarker() {
        return bossBarker;
    }
}
