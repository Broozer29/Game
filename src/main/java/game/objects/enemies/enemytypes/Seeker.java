package game.objects.enemies.enemytypes;

import VisualAndAudioData.audio.enums.AudioEnums;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.Direction;
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
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Seeker extends Enemy {


    public Seeker (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

//        SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
//        exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Normal_Exhaust);
//        this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.missileTypePathFinders = PathFinderEnums.StraightLine;
        this.damage = MissileTypeEnums.SeekerProjectile.getDamage();


//        this.attackSpeed = 20;
//		this.attackSpeed= 9999999;
//		this.attackSpeedCurrentFrameCount= 9999999;
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
        MissileTypeEnums missileType = MissileTypeEnums.SeekerProjectile;
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
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        //Set destination required for the StraightLinePathFinder
        Point spaceShipCenter = new Point(PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(),
                PlayerManager.getInstance().getSpaceship().getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(spaceShipCenter);

        //Rotate the object towards it's destination
        missile.rotateGameObjectTowards(missile.getMovementConfiguration().getDestination().getX(), missile.getMovementConfiguration().getDestination().getY(), true);
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.getAnimation().setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover
        missile.resetMovementPath();
        missile.getMovementConfiguration().setDestination(spaceShipCenter); // again because reset removes it
        missile.setOwnerOrCreator(this);
        MissileManager.getInstance().addExistingMissile(missile);
    }




}