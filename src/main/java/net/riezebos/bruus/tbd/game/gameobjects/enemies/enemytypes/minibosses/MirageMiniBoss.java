package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.minibosses;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MirageMiniBoss extends Enemy {

    private double lastTimeCloned;
    private int cloneCooldown = 20;

    private List<Direction> availableDirections = new ArrayList<>();

    public MirageMiniBoss(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteAnimationConfigurationion.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(3f);
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
        this.damage = 10;
        this.attackSpeed = 5;

        initializeDirections();

        if (this.movementConfiguration.getPathFinder() instanceof BouncingPathFinder pathFinder) {
            pathFinder.setMaxBounces(1000);
            pathFinder.setUseCenteredCoordinatesInstead(true);
        }
        lastTimeCloned = GameState.getInstance().getGameSeconds(); //spawn with clones on cooldown
    }

    private void initializeDirections() {
        availableDirections = new ArrayList<>();
        availableDirections.add(Direction.LEFT_UP);
        availableDirections.add(Direction.LEFT_DOWN);
        availableDirections.add(Direction.RIGHT_UP);
        availableDirections.add(Direction.RIGHT_DOWN);

        Collections.shuffle(availableDirections);
    }

    private Direction getRandomDirection() {
        if (availableDirections.isEmpty()) {
            initializeDirections();
        }
        // Remove and return the first direction from the shuffled list
        return availableDirections.remove(0);
    }



    public void fireAction() {

        if (this.ownerOrCreator instanceof MirageMiniBoss && (this.ownerOrCreator.getCurrentHitpoints() <= 0 || !this.ownerOrCreator.isVisible())) {
            //the original died
            this.takeDamage(9999);
        }

        double currentTime = GameState.getInstance().getGameSeconds();

        if (currentTime >= lastTimeCloned + cloneCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire && !(this.ownerOrCreator instanceof MirageMiniBoss)) {
            spawnCloneAnimation();
            super.cleanseAllEffects();

            for (int i = 0; i < 3; i++) {
                createClone();
            }

            this.resetMovementPath();
            this.movementConfiguration.setDirection(getRandomDirection());
            lastTimeCloned = currentTime;
        }


        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire) {
            updateChargingAttackAnimationCoordination();
            if (!chargingUpAttackAnimation.isPlaying()) {
                this.isAttacking = true;
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }

            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                shootMissiles();
                this.isAttacking = false;
                lastAttackTime = currentTime; // Update the last attack time after firing
            }
        }

        this.setAllowedVisualsToRotate(true);
        this.rotateGameObjectTowards(PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(), PlayerManager.getInstance().getSpaceship().getCenterYCoordinate(), true);
        this.setAllowedVisualsToRotate(false);
    }

    private void shootMissiles() {
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        // The charging up attack animation has finished, create and fire the missile
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                missileType.getImageType(), 0.75f);


        int movementSpeed = 5;
        //Create missile movement attributes and create a movement configuration

        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, this.movementRotation
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Mirage Mini Boss projectile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        //get the coordinates for rotation of the missile
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        Point rotationCoordinates = new Point(
                spaceship.getCenterXCoordinate() - missile.getWidth() / 2,
                spaceship.getCenterYCoordinate() - missile.getHeight() / 2
        );

        missile.resetMovementPath();

        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(rotationCoordinates); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private void spawnCloneAnimation() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        spriteConfiguration.setImageType(ImageEnums.SmokeExplosion); //placeholder
        spriteConfiguration.setScale(1);

        SpriteAnimationConfiguration cloneAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        SpriteAnimation cloneAnimation = new SpriteAnimation(cloneAnimationConfiguration);
        cloneAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        AnimationManager.getInstance().addUpperAnimation(cloneAnimation);
    }

    private void createClone() {
        Enemy clonedEnemy = EnemyCreator.createEnemy(EnemyEnums.MirageMiniBoss, this.xCoordinate, this.yCoordinate, Direction.LEFT,
                this.scale, EnemyEnums.MirageMiniBoss.getMovementSpeed(), EnemyEnums.MirageMiniBoss.getMovementSpeed(), MovementPatternSize.SMALL, false);
        clonedEnemy.setDamage(0); //overwrite the damage to 0
        clonedEnemy.setOwnerOrCreator(this);
        clonedEnemy.setMaxHitPoints(this.maxHitPoints);
        clonedEnemy.setCurrentHitpoints(this.currentHitpoints);
        clonedEnemy.setBaseArmor(-75); // massively increased damage taken
        clonedEnemy.getDestructionAnimation().changeImagetype(ImageEnums.SmokeExplosion); //placeholder
        clonedEnemy.getDestructionAnimation().setAnimationScale(0.65f);
        clonedEnemy.getDestructionAnimation().setFrameDelay(2);
        //this doesnt copy status effects, easy solution: cleanse status effects when it makes clones

        clonedEnemy.resetMovementPath();
        float newSpeed = getDifferentMovementSpeed(getDifferentMovementSpeed(this.getMovementConfiguration().getXMovementSpeed()));

        clonedEnemy.getMovementConfiguration().setXMovementSpeed(newSpeed);
        clonedEnemy.getMovementConfiguration().setYMovementSpeed(newSpeed);
        clonedEnemy.getMovementConfiguration().setDirection(getRandomDirection());
        //give it a random direction for the bouncing pathfinder
        clonedEnemy.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());

        EnemyManager.getInstance().addEnemy(clonedEnemy);
    }

    private float getDifferentMovementSpeed(float currentMoveSpeed){
        float returnSpeed = currentMoveSpeed * 0.95f;
        float bonusSpeed = random.nextFloat(0, (currentMoveSpeed * 0.1f));

        return returnSpeed + bonusSpeed;
    }

}
