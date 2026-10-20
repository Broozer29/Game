package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone.FinalBossCreateRotatingReflectingBlocks;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone.FinalBossPeriodicMissileBarrage;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone.FinalBossPhaseOneLaserbeamAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone.FinalBossSpawnMine;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasethree.FinalBossMissileBarrageAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasethree.FinalBossPhase3LaserbeamCloneAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasethree.FinalBossSpawnFourDirectionalDrones;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasethree.FinalBossStraightLineMissileAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo.FinalBossDropMineCharge;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo.FinalBossOrbitMissileAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo.FinalBossPhase1LaserbeamCloneAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo.FinalBossSpreadMissileAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.ReflectiveBlocks;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.finalboss.FinalBossMine;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
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

    //Share of the total hitpoints each phase lasts, phase 3 gets whatever remains (60% by default)
    private float phaseOneHitpointsRatio = 0.1f;
    private float phaseTwoHitpointsRatio = 0.3f;
    //Multiplies the level scaled maxHitPoints from Enemy. Every phase used to refill to full hitpoints, so 3 keeps the whole fight as long as before
    private float totalHitpointsMultiplier = 3f;


    private AudioEnums firstPhaseAudioEnum = AudioEnums.FinalBossPhase1;
    private AudioEnums secondPhaseAudioEnum = AudioEnums.FinalBossPhase2;
    private AudioEnums thirdPhaseAudioEnum = AudioEnums.FinalBossPhase3;
    private static FinalBossBarker bossBarker = new FinalBossBarker(); //static so attemptMockingBark can be called without a boss instance
    private FinalBossPeriodicMissileBarrage finalBossPeriodicMissileBarrage = new FinalBossPeriodicMissileBarrage();
    private FinalBossSpawnMine phase1FinalBossSpawnMine = new FinalBossSpawnMine();

    private FinalBossOrbitMissileAttack phase2OrbitMissileAttack;
    private FinalBossDropMineCharge phase2DropMineCharge;
    private FinalBossSpreadMissileAttack phase2SpreadMissileAttack;
    private FinalBossPhase1LaserbeamCloneAttack phase2LaserbeamCloneAttack;

    private FinalBossStraightLineMissileAttack phase3FinalBossStraightLineMissileAttack;
    private FinalBossMissileBarrageAttack phase3MissileBarrageAttack;
    private FinalBossCreateRotatingReflectingBlocks phase3CreateRotatingReflectingBlocks;
    private FinalBossSpawnFourDirectionalDrones phase3SpawnFourDirectionalDrones;
    private FinalBossPhase3LaserbeamCloneAttack phase3LaserbeamCloneAttack;

    public FinalBoss(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        this.maxHitPoints *= totalHitpointsMultiplier;
        this.currentHitpoints = this.maxHitPoints;

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 2, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.BossExplosion);
        destroyedExplosionfiguration.getSpriteConfiguration().setScale(4);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.knockbackStrength = 9;
        this.setAllowedToMove(false);
        this.bossPhase = BOSSPHASE_1;

        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.15f);

        //phase 1 abilities
        FinalBossCreateRotatingReflectingBlocks finalBossCreateRotatingReflectingBlocks = new FinalBossCreateRotatingReflectingBlocks(4, BOSSPHASE_1);
        FinalBossPhaseOneLaserbeamAttack finalBossPhaseOneLaserbeamAttack = new FinalBossPhaseOneLaserbeamAttack();

        bossBehaviourList.add(finalBossCreateRotatingReflectingBlocks);
        bossBehaviourList.add(finalBossPhaseOneLaserbeamAttack);
        bossBehaviourList.add(phase1FinalBossSpawnMine);

        //phase 2 abilities
        phase2OrbitMissileAttack = new FinalBossOrbitMissileAttack();
        phase2DropMineCharge = new FinalBossDropMineCharge();
        phase2SpreadMissileAttack = new FinalBossSpreadMissileAttack();
        phase2LaserbeamCloneAttack = new FinalBossPhase1LaserbeamCloneAttack();

        bossBehaviourList.add(phase2OrbitMissileAttack);
        bossBehaviourList.add(phase2DropMineCharge);
        bossBehaviourList.add(phase2SpreadMissileAttack);
        bossBehaviourList.add(phase2LaserbeamCloneAttack);


        //phase 3 abilities
        phase3FinalBossStraightLineMissileAttack = new FinalBossStraightLineMissileAttack();
        phase3MissileBarrageAttack = new FinalBossMissileBarrageAttack();
        phase3CreateRotatingReflectingBlocks = new FinalBossCreateRotatingReflectingBlocks(2, BOSSPHASE_3);
        phase3SpawnFourDirectionalDrones = new FinalBossSpawnFourDirectionalDrones();
        phase3LaserbeamCloneAttack = new FinalBossPhase3LaserbeamCloneAttack();

        bossBehaviourList.add(phase3FinalBossStraightLineMissileAttack);
        bossBehaviourList.add(phase3MissileBarrageAttack);
        bossBehaviourList.add(phase3CreateRotatingReflectingBlocks);
        bossBehaviourList.add(phase3SpawnFourDirectionalDrones);
        bossBehaviourList.add(phase3LaserbeamCloneAttack);


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
//            changePhaseToNextPhase();
//            changePhaseToNextPhase();
            firePhaseOnePassiveAbilities();
        }

        if (getBossPhase() == BOSSPHASE_3) {
            firePhaseThreePassiveAbilities();
        }

        attemptBark();


        if (!(this.currentActiveBehavior instanceof FinalBossDropMineCharge)) {
            this.setAllowedVisualsToRotate(true);
            this.rotateGameObjectTowards(PlayerManager.getInstance().getClosestSpaceShip(this));
            this.setAllowedVisualsToRotate(false);
        }

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


        //Only change phases between attacks, so the boss never teleports away in the middle of one
        if (shouldChangePhase()) {
            changePhaseToNextPhase();
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

    private void firePhaseThreePassiveAbilities() {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastGameSecondsMissileSpawned + spawnCornerReflectingBlocksCooldown) {
            firePhaseOneReflectingBarriers();
            lastGameSecondsMissileSpawned = currentTime;
        }
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
        float maxHitPoints = Math.min(60 * (GameState.getInstance().getDifficultyCoefficient()), 500); //max 500 hp
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

        if (this.getBossPhase() != BOSSPHASE_3 && currentHitpoints <= getEndOfPhaseHitpoints()) {
            //Clamp to the threshold so one big hit can't skip a phase. The phase change itself happens in fireAction once the current attack is finished
            currentHitpoints = getEndOfPhaseHitpoints();
            return;
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

    //Phase 1 ends on hitpoints, phase 2 ends when its music has finished playing
    private boolean shouldChangePhase() {
        if (bossPhase == BOSSPHASE_1) {
            return currentHitpoints <= getEndOfPhaseHitpoints();
        } else if (bossPhase == BOSSPHASE_2) {
            return AudioManager.getInstance().isBackgroundMusicFinished(secondPhaseAudioEnum);
        }
        return false;
    }

    //The hitpoints at which the current phase ends and the next one starts. In phase 2 this is only a floor, the phase ends when its music finishes
    private float getEndOfPhaseHitpoints() {
        if (bossPhase == BOSSPHASE_1) {
            return maxHitPoints * (1 - phaseOneHitpointsRatio);
        } else if (bossPhase == BOSSPHASE_2) {
            return maxHitPoints * (1 - phaseOneHitpointsRatio - phaseTwoHitpointsRatio);
        }
        return 0;
    }

    //Detonate the mines placed by FinalBossSpawnMine
    private void detonatePhaseOneMines() {
        MissileManager.getInstance().getMissiles().stream()
                .filter(missile -> missile instanceof FinalBossMine && missile.isVisible() && missile.getOwnerOrCreator() == this)
                .toList()
                .forEach(missile -> ((FinalBossMine) missile).detonateMissile());
    }

    private void changePhaseToNextPhase() {
        if (bossPhase == BOSSPHASE_1) {
            bossPhase = BOSSPHASE_2;
            //Clear all hostile missiles to make room for then next phase
            attemptPhaseTransitionBark();
            MissileManager.getInstance().getMissiles().stream().filter(missile -> !missile.isFriendly()).forEach(
                    missile -> missile.setTransparancyAlpha(true, missile.getTransparancyAlpha(), -0.05f)
            );
            detonatePhaseOneMines(); //After the fade-out, so the bullets from the mines stay for a chaotic transition
            AudioManager.getInstance().playDefaultBackgroundMusicForALevel(secondPhaseAudioEnum, false); //No loop, phase 3 starts when this song ends
            setPhaseTwoCooldowns();
            phase2Teleport();

        } else if (bossPhase == BOSSPHASE_2) {
            attemptPhaseTransitionBark();
            bossPhase = BOSSPHASE_3;
            setPhaseThreeCooldowns();
            phase3Teleport();
            AudioManager.getInstance().playDefaultBackgroundMusicForALevel(thirdPhaseAudioEnum, true);
        }
    }

    private void phase3Teleport() {
        //todo dit is een sloppy copy-paste van phase2teleport met alleen de bestemming als verschil
        lastAttackTime = GameState.getInstance().getGameSeconds();
        Point teleportPoint = new Point(DataClass.getInstance().getWindowWidth() - (this.getWidth() * 2), DataClass.getInstance().getWindowHeight() * 0.5f);
        this.setCenterCoordinates(teleportPoint.getX(), teleportPoint.getY());
        playSmokeAnimation(this); //Place it at the current location
        playSmokeAnimationAtFirstStep(this); //Place it at the location the boss teleports back to

        HoverPathFinder hoverPathFinder = new HoverPathFinder();
        hoverPathFinder.setSecondsToHoverStill(0);
        hoverPathFinder.setShouldDecreaseBoardBlock(false);
        hoverPathFinder.setShouldChangeBoardBlockEverXHover(2);
        this.getMovementConfiguration().setBoardBlockToHoverIn(7);
        this.getMovementConfiguration().setPathFinder(hoverPathFinder);
        this.resetMovementPath();
        this.setAllowedToMove(false);
    }

    private void phase2Teleport() {
        lastAttackTime = GameState.getInstance().getGameSeconds();
        Point teleportPoint = BoardBlockUpdater.getRandomCoordinateInBlock(7, this.getWidth(), this.getHeight());
        this.setCenterCoordinates(teleportPoint.getX(), teleportPoint.getY());
        playSmokeAnimation(this); //Place it at the current location
        playSmokeAnimationAtFirstStep(this); //Place it at the location the boss teleports back to

        this.getMovementConfiguration().setLastUsedMovementSpeed(this.getEnemyType().getMovementSpeed() * 7.5f);
        this.getMovementConfiguration().setMovementSpeed(this.getEnemyType().getMovementSpeed());
        this.setAllowedVisualsToRotate(true);
        HoverPathFinder hoverPathFinder = new HoverPathFinder();
        hoverPathFinder.setSecondsToHoverStill(0);
        hoverPathFinder.setShouldDecreaseBoardBlock(false);
        hoverPathFinder.setShouldChangeBoardBlockEverXHover(2);
        this.getMovementConfiguration().setBoardBlockToHoverIn(7);

        this.getMovementConfiguration().setPathFinder(hoverPathFinder);
        this.resetMovementPath();
        this.setAllowedToMove(true);
        this.move();
    }

    private void playSmokeAnimationAtFirstStep(GameObject target) {
        SpriteAnimation spriteAnimation = createSmokeAnim();

        Point teleportPoint = target.getMovementConfiguration().getCurrentLocation();
        spriteAnimation.setCenterCoordinates(
                teleportPoint.getX() + target.getWidth() / 2,
                teleportPoint.getY() + target.getHeight() / 2
        );
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }

    private void playSmokeAnimation(GameObject target) {
        SpriteAnimation spriteAnimation = createSmokeAnim();
        spriteAnimation.setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
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


    //Called from GameBoard when the player dies and the game-over screen is shown
    public static void attemptMockingBark() {
        if (!AudioManager.getInstance().bossIsBarking(bossBarker)) {
            AudioManager.getInstance().addAudio(bossBarker.getRandomMockBark());
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

    private void setPhaseTwoCooldowns() {
        phase2DropMineCharge.setLastAttackTime(GameState.getInstance().getGameSeconds() - 60); //instantly do this one
        phase2OrbitMissileAttack.setLastAttackTime(GameState.getInstance().getGameSeconds() - 3);
        phase2SpreadMissileAttack.setLastAttackTime(GameState.getInstance().getGameSeconds() - 15);
        phase2LaserbeamCloneAttack.setLastAttackedTime(GameState.getInstance().getGameSeconds() - 10);
    }

    private void setPhaseThreeCooldowns() {
        //tbd
        phase3MissileBarrageAttack.setLastAttackedTime(GameState.getInstance().getGameSeconds() - 5);
        phase3FinalBossStraightLineMissileAttack.setLastAttackTime(GameState.getInstance().getGameSeconds() - 10);
        phase3SpawnFourDirectionalDrones.setLastAttackTime(GameState.getInstance().getGameSeconds() - 15);
    }
}
