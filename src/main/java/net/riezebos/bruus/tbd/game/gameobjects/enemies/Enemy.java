package net.riezebos.bruus.tbd.game.gameobjects.enemies;

import net.riezebos.bruus.tbd.game.UI.GameUICreator;
import net.riezebos.bruus.tbd.game.UI.UIObject;
import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.PathFinderEnums;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class Enemy extends GameObject {

    protected MissileManager missileManager = MissileManager.getInstance();
    protected Random random = new Random();
    protected boolean allowedToFire;
    protected boolean isAttacking;

    protected EnemyEnums enemyType;
    protected PathFinderEnums missileTypePathFinders;
    protected double lastAttackTime = 0.0;

    protected boolean detonateOnCollision;


    public Enemy(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration);
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
        configureEnemy(enemyConfiguration);
        modifyStatsBasedOnLevelAndDifficulty();
        initChargingUpAnimation(spriteConfiguration);
    }

    public Enemy(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion);
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }

        configureEnemy(enemyConfiguration);
        modifyStatsBasedOnLevelAndDifficulty();
        initChargingUpAnimation(spriteAnimationConfigurationion.getSpriteConfiguration());

    }

    private void configureEnemy(EnemyConfiguration enemyConfiguration) {
        this.enemyType = enemyConfiguration.getEnemyType();
        this.maxHitPoints = enemyConfiguration.getMaxHitPoints();
        this.currentHitpoints = maxHitPoints;
        this.maxShieldPoints = enemyConfiguration.getMaxShields();
        this.currentShieldPoints = maxShieldPoints;
        this.hasAttack = enemyConfiguration.isHasAttack();
        this.showHealthBar = enemyConfiguration.isShowHealthBar();
        this.deathSound = enemyConfiguration.getDeathSound();
        this.currentLocation = new Point(xCoordinate, yCoordinate);
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        this.boxCollision = enemyConfiguration.isBoxCollision();
        this.baseArmor = enemyConfiguration.getBaseArmor();
        this.cashMoneyWorth = enemyConfiguration.getCashMoneyWorth();
        this.xpOnDeath = enemyConfiguration.getXpOnDeath();
        this.isAttacking = false;
        this.setVisible(true);
        this.setFriendly(false);

        this.rotateGameObjectTowards(movementRotation, true);

        this.objectType = enemyConfiguration.getObjectType();
        this.allowedToFire = true;
    }

    private void modifyStatsBasedOnLevelAndDifficulty() {
        int enemyLevel = GameState.getInstance().getMonsterLevel();
        int difficultyScore = LevelManager.getInstance().getCurrentLevelDifficultyScore(); // Ranges between 2 and 6 (inclusive)

        // Calculate scaling factor (1.0 at Easy, up to 1.5 at Hard)
        float difficultyScalingFactor = 1 + ((difficultyScore - 2) / 4.0f) * 0.5f;

        if (enemyLevel > 1) {
            // Apply both enemy level scaling and difficulty scaling
            this.maxHitPoints *= Math.pow(getScalingFactor(), enemyLevel) * difficultyScalingFactor;
            this.currentHitpoints = maxHitPoints;

            this.maxShieldPoints *= Math.pow(getScalingFactor(), enemyLevel) * difficultyScalingFactor;
            this.currentShieldPoints = maxShieldPoints;

            this.damage *= Math.pow(getScalingFactor(), enemyLevel) * difficultyScalingFactor;

            // XP on death is multiplied by 50% of difficulty coefficient
//            this.xpOnDeath *= (1 + (0.5 * difficultyCoeff));

            // Cash money worth scaling for bosses
            if (this.enemyType.getEnemyCategory().equals(EnemyCategory.Boss)) {
                this.cashMoneyWorth *= Math.pow(getScalingFactor(), enemyLevel);
            }

            this.cashMoneyWorth *= PlayerStats.getInstance().getMineralModifier();
        }
    }

    private float getScalingFactor() {
//        if (this.enemyType.getEnemyTribe().equals(EnemyTribes.Zerg)) {
//            return 1.15f;
//        }
        return 1.125f;
    }

    private void initChargingUpAnimation(SpriteConfiguration spriteConfiguration) {
        SpriteConfiguration newSpriteConfig = spriteConfiguration;
        if (spriteConfiguration.getScale() < 0.85f) {
            newSpriteConfig.setScale(0.85f);
        }
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(newSpriteConfig, 2, false);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Charging);
        this.chargingUpAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    @Override
    public void triggerOnDeathActions() {
        if (LevelManager.getInstance().isNextLevelABossLevel() && this.enemyType.getEnemyCategory().equals(EnemyCategory.Boss)) {
            GameStatsTracker.getInstance().addEnemyKilled(1);
            PlayerProfileManager.getInstance().getLoadedProfile().addEmeralds(1);
            PlayerProfileManager.getInstance().exportCurrentProfile();
            GUIComponent emeraldIcon = GameUICreator.getInstance().createEmeraldObtainedIcon(this.getCenterXCoordinate(), this.getCenterYCoordinate());
            BoardManager.getInstance().getGameBoard().addGUIAnimation(emeraldIcon);
        } else if (!LevelManager.getInstance().isNextLevelABossLevel() && !this.enemyType.getEnemyCategory().equals(EnemyCategory.Summon)) {
            //seperate if statement because i might want to handle something else here
            if (!this.hasEffect(EffectIdentifiers.EndOfLevelBurn)) {
                GameStatsTracker.getInstance().addEnemyKilled(1);
            }
        }

    }

    public EnemyEnums getEnemyType() {
        return this.enemyType;
    }

    public void fireAction() {
        // This could contain default behaviour but SHOULD be overriden by specific enemytype
        // classes.
    }

    public void rotateAfterMovement() {
//        updateChargingAttackAnimationCoordination();
        if (!this.allowedVisualsToRotate) {
            return;
        }

        // Check for specific missile type pathfinders
        if (this.getMissileTypePathFinders() == PathFinderEnums.Homing
                || this.getMissileTypePathFinders() == PathFinderEnums.StraightLine) {
            // Rotate towards the player, assuming these are only used for enemies that aim
            Point point = new Point(
                    PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(),
                    PlayerManager.getInstance().getSpaceship().getCenterYCoordinate());
            rotateObjectTowardsPoint(point, true);
//            updateChargingAttackAnimationCoordination();
            return;
        }

        if (this.getEnemyType().equals(EnemyEnums.Alien_Bomb)) {
            if (this.getOwnerOrCreator() != null) {
                this.rotateGameObjectTowards(this.getOwnerOrCreator().getMovementConfiguration().getRotation(), true);
                this.setAllowedVisualsToRotate(false);
            }
            return;
        }


        if (!movementConfiguration.getCurrentPath().getWaypoints().isEmpty()) {
            rotateObjectTowardsDestination(true);
            setAllowedVisualsToRotate(false);
        } else if (!(movementConfiguration.getPathFinder() instanceof DestinationPathFinder)) {
            setAllowedVisualsToRotate(false);
        }
    }

    public void deleteObject() {
        if (this.animation != null) {
            this.animation.setVisible(false);
        }

        for (SpriteAnimation spriteAnimation : effectAnimations) {
            spriteAnimation.setVisible(false);
        }

        if (this.exhaustAnimation != null) {
            this.exhaustAnimation.setVisible(false);
        }

        if (this.shieldDamagedAnimation != null) {
            this.shieldDamagedAnimation.setVisible(false);
        }

        if (this.destructionAnimation != null &&
                this.currentHitpoints > 1 &&
                !this.destructionAnimation.isPlaying()) {
            this.destructionAnimation.setVisible(false);
        }

        if (this.chargingUpAttackAnimation != null) {
            this.chargingUpAttackAnimation.setVisible(false);
        }

        for (GameObject object : objectsFollowingThis) {
            object.deleteObject();
        }

        for (GameObject object : objectOrbitingThis) {
            object.deleteObject();
        }

        this.objectToFollow = null;

        if (movementConfiguration != null) {
            this.movementConfiguration.deleteConfiguration();
        }
        this.movementConfiguration = null;
        this.ownerOrCreator = null;
        this.visible = false;
    }

    public SpriteAnimation getChargingUpAttackAnimation() {
        return chargingUpAttackAnimation;
    }

    public void setChargingUpAttackAnimation(SpriteAnimation chargingUpAttackAnimation) {
        this.chargingUpAttackAnimation = chargingUpAttackAnimation;
    }

    public PathFinderEnums getMissileTypePathFinders() {
        return missileTypePathFinders;
    }

    public void setMissileTypePathFinders(PathFinderEnums missileTypePathFinders) {
        this.missileTypePathFinders = missileTypePathFinders;
    }

    public boolean isAllowedToFire() {
        return allowedToFire;
    }

    public void setAllowedToFire(boolean allowedToFire) {
        this.allowedToFire = allowedToFire;
    }

    public double getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(double lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public boolean isDetonateOnCollision() {
        return detonateOnCollision;
    }

    public void setDetonateOnCollision(boolean detonateOnCollision) {
        this.detonateOnCollision = detonateOnCollision;
    }


}