package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class StrikerCornerDrone extends Enemy {

    public StrikerCornerDrone(SpriteAnimationConfiguration spriteConfig, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfig, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfig.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 5;
        this.allowedToMove = false;
        this.attackSpeed = 1.5f;
        this.detonateOnCollision = false;
        this.knockbackStrength = 9;
        this.allowedToFire = true;
        this.baseArmor = 999999;
    }

    @Override
    public boolean isShowHealthBar() {
        return false;
    }

    @Override
    public void fireAction() {
        this.allowedToMove = false; //Manually overwrite this cause I don't want these to move
        rotateTowardsPlayer();
        super.updateChargingAttackAnimationCoordination();
        if (this.ownerOrCreator != null && this.ownerOrCreator.getCurrentHitpoints() <= 0) {
            this.takeDamage(99999);
        }



//        double currentTime = GameState.getInstance().getGameSeconds();
//        if (allowedToFire && currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
//            shootMissiles();
//            this.lastAttackTime = GameState.getInstance().getGameSeconds();
//        }
    }

    private void rotateTowardsPlayer() {
        this.rotateGameObjectTowards(
                PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(),
                PlayerManager.getInstance().getSpaceship().getCenterYCoordinate(),
                false
        );
    }

    @Override
    protected void updateChargingAttackAnimationCoordination() {
        if (this.chargingUpAttackAnimation != null) {
            double baseDistance = (this.getWidth() + this.getHeight()) / 3.0; //shorter distance
            Point chargingUpLocation = calculateFrontPosition(this.getCenterXCoordinate(), this.getCenterYCoordinate(), rotationAngle, baseDistance);
            this.chargingUpAttackAnimation.setCenterCoordinates(chargingUpLocation.getX(), chargingUpLocation.getY());
        }
    }


    //Called by StrikerBoss
    public void shootMissile() {
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        // The charging up attack animation has finished, create and fire the missile
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                missileType.getImageType(), 0.55f);


        float movementSpeed = 3.75f;
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
        String objectType = "Seeker Missile";

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

//        missile.setScale(0.3f);

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

}
