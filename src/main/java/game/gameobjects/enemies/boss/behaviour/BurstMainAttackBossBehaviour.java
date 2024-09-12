package game.gameobjects.enemies.boss.behaviour;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.missiles.*;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.player.spaceship.SpaceShip;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.MovementPatternSize;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.StraightLinePathFinder;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class BurstMainAttackBossBehaviour implements BossActionable {

    private double lastAttackTime = 0;
    private double lastSingleShotAttackTime = 0;
    private boolean allowedToFireBurst = false;
    private double intermittenAttackCooldown = 0.25f;
    private int burstShotsFired = 0; // Track the number of shots fired in the burst
    private int amountOfShotsPerBurst = 8;
    private double attackCooldown = 3;

    private SpriteAnimation chargingAttackAnimation = null;


    @Override
    public boolean activateBehaviour (Enemy enemy) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();

        if (chargingAttackAnimation == null) {
            initChargingAttackAnimation(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastAttackTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateChargingAttackAnimationLocation(enemy);

            if (!chargingAttackAnimation.isPlaying() && !allowedToFireBurst) {
                chargingAttackAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(chargingAttackAnimation);
                burstShotsFired = 0; // Reset the burst shot counter, so that it is ready for the actual firing
            }

            if (chargingAttackAnimation.getCurrentFrame() >= chargingAttackAnimation.getTotalFrames() - 1) {
                // The "charging up" animation has finished, missiles are now allowed to be fired
                allowedToFireBurst = true;
            }
        }

        if (allowedToFireBurst) {
            boolean canFireAgain = currentTime >= lastSingleShotAttackTime + intermittenAttackCooldown;
            if (burstShotsFired < amountOfShotsPerBurst && canFireAgain) {
                // Fire a missile and update lastAttackTime
                fireMissile(enemy);
                burstShotsFired++;
                lastSingleShotAttackTime = currentTime;

                if (burstShotsFired >= amountOfShotsPerBurst) {
                    // After the 4th shot, the burst is finished
                    lastAttackTime = currentTime;
                    allowedToFireBurst = false;
                    enemy.setAttacking(false);
                    return true; //We finished
                }
            }
            return false; //We still running this behaviour
        }
        return true; //We dont have anything to do at this point
    }

    private void initChargingAttackAnimation (Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setImageType(ImageEnums.Charging);
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getYCoordinate());
        spriteConfiguration.setScale(1);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        chargingAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        chargingAttackAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
        chargingAttackAnimation.addXOffset(-5);

    }

    private void updateChargingAttackAnimationLocation (Enemy enemy) {
        chargingAttackAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
    }

    private void fireMissile (Enemy enemy) {
        Missile missile = createMissile(enemy);
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Missile createMissile (Enemy enemy) {
        MissileEnums missileType = MissileEnums.ScoutLaserBullet;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(
                enemy.getXCoordinate(), enemy.getCenterYCoordinate(),
                missileType.getImageType(), enemy.getScale());


        int movementSpeed = 5;
        //Create missile movement attributes and create a movement configuration
        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, enemy.getMovementConfiguration().getRotation()
        );


        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Boss Burst Missile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, enemy.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        //get the coordinates for rotation of the missile
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        Point rotationCoordinates = new Point(
                spaceship.getCenterXCoordinate() - missile.getWidth() / 2,
                spaceship.getCenterYCoordinate() - missile.getHeight() / 2
        );

        // Any visual resizing BEFORE replacing starting coordinates and rotation

        missile.resetMovementPath();

        Point chargingCenterCoords = new Point(enemy.getChargingUpAttackAnimation().getCenterXCoordinate(), enemy.getCenterYCoordinate());

        missile.setCenterCoordinates(chargingCenterCoords.getX(), chargingCenterCoords.getY());
        missile.getMovementConfiguration().setDestination(rotationCoordinates); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(chargingCenterCoords.getX(), chargingCenterCoords.getY());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(enemy);

        return missile;
    }

    public double getAttackCooldown () {
        return attackCooldown;
    }

    public void setAttackCooldown (double attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public int getAmountOfShotsPerBurst () {
        return amountOfShotsPerBurst;
    }

    public void setAmountOfShotsPerBurst (int amountOfShotsPerBurst) {
        this.amountOfShotsPerBurst = amountOfShotsPerBurst;
    }

    public double getIntermittenAttackCooldown () {
        return intermittenAttackCooldown;
    }

    public void setIntermittenAttackCooldown (double intermittenAttackCooldown) {
        this.intermittenAttackCooldown = intermittenAttackCooldown;
    }
}
