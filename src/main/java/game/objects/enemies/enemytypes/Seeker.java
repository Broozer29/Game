package game.objects.enemies.enemytypes;

import VisualAndAudioData.audio.enums.AudioEnums;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.PathFinderEnums;
import game.movement.Point;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.*;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import game.objects.player.PlayerManager;
import game.objects.player.spaceship.SpaceShip;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Seeker extends Enemy {


    public Seeker (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.missileTypePathFinders = PathFinderEnums.StraightLine;
        this.damage = MissileEnums.SeekerProjectile.getDamage();

//        this.attackSpeed = 1;
    }

    public Seeker (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.missileTypePathFinders = PathFinderEnums.StraightLine;
        this.damage = MissileEnums.SeekerProjectile.getDamage();

    }


    @Override
    public void fireAction () {
        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder){
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
        }
        // Check if the attack cooldown has been reached
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
        && allowedToFire) {
            if (!chargingUpAttackAnimation.isPlaying()) {
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }

            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                shootMissile();
                lastAttackTime = currentTime; // Update the last attack time after firing
            }
        }
    }


    private void shootMissile () {
        // The charging up attack animation has finished, create and fire the missile
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate, ImageEnums.Seeker_Missile
                , this.scale);


        //Create missile movement attributes and create a movement configuration
        MissileEnums missileType = MissileEnums.SeekerProjectile;
        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                missileType.getxMovementSpeed(), missileType.getyMovementSpeed(), missilePathFinder, movementPatternSize, this.movementRotation
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
                false, false, true);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        //get the coordinates for rotation of the missile
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        Point rotationCoordinates = new Point(spaceship.getCenterXCoordinate(),
                spaceship.getCenterYCoordinate());

        // Any visual resizing BEFORE replacing starting coordinates and rotation
//        missile.setScale(0.3f);

        //Place the missile at the correct location, then rotate it and disable further rotations
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.getAnimation().setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.rotateGameObjectTowards(rotationCoordinates.getX(), rotationCoordinates.getY(), false);
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        //Get the coordinates required for the pathfinders
        Point movementDestinationCoordinates = new Point(rotationCoordinates.getX() - missile.getWidth() / 2, rotationCoordinates.getY() - missile.getHeight() / 2);

        //Resets the movement configuration and sets the new desination, adjusted for the dimensions of the missile
        missile.resetMovementPath();
        missile.getMovementConfiguration().setDestination(movementDestinationCoordinates); // again because reset removes it
        missile.setOwnerOrCreator(this);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }




}