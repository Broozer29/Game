package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.PathFinderEnums;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class Guardian extends Enemy {


    public Guardian (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.missileTypePathFinders = PathFinderEnums.StraightLine;
        this.damage = 15;
        this.attackSpeed = 3;
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
        this.chargingUpAttackAnimation.changeImagetype(ImageEnums.GuardianChargingAnimation);
        this.chargingUpAttackAnimation.cropAnimation();
        this.chargingUpAttackAnimation.setFrameDelay(3);

        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder){
            HoverPathFinder pathFinder = (HoverPathFinder) this.movementConfiguration.getPathFinder();
//            movementConfiguration.setBoardBlockToHoverIn(7);
            pathFinder.setShouldDecreaseBoardBlock(true);
            pathFinder.setDecreaseBoardBlockAmountBy(1);
        }
    }

    @Override
    protected void updateChargingAttackAnimationCoordination () {
        if (this.chargingUpAttackAnimation != null) {
            double baseDistance = (this.getWidth() + this.getHeight()) / 4.0;
            Point chargingUpLocation = calculateFrontPosition(this.getCenterXCoordinate(), this.getCenterYCoordinate(), rotationAngle, baseDistance);
            this.chargingUpAttackAnimation.setCenterCoordinates(chargingUpLocation.getX(), chargingUpLocation.getY());
        }
    }

    private Point calculateFrontPosition (int centerX, int centerY, double angleDegrees, double distanceToFront) {
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the new position using trigonometry
        int newX = centerX + (int) (Math.cos(angleRadians) * distanceToFront);
        int newY = centerY + (int) (Math.sin(angleRadians) * distanceToFront);

        return new Point(newX, newY);
    }

    @Override
    public void fireAction () {
        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder) {
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

            shootMissile();
            lastAttackTime = currentTime;
        }
    }

    private void shootMissile () {
        float scale = 0.5f;
        MissileEnums missileType = MissileEnums.DefaultAnimatedBullet;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                missileType.getImageType(), scale);


        float movementSpeed = 6.5f;
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
        String objectType = "Guardian Missile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.getAnimation().changeImagetype(ImageEnums.GuardianMissile);
        missile.getDestructionAnimation().changeImagetype(ImageEnums.GuardianMissileImpact);
        missile.getAnimation().setFrameDelay(1);
        missile.setKnockbackStrength(5);

        //get the coordinates for rotation of the missile
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        Point rotationCoordinates = new Point(
                spaceship.getCenterXCoordinate() - (missile.getAnimation().getWidth() / 2),
                spaceship.getCenterYCoordinate() - (missile.getHeight() * 0.15f) //Small offset because it aims too low, probably because of non-cropped sprites
        );
        missile.getAnimation().setAnimationScale(scale);

        // Any visual resizing BEFORE replacing starting coordinates and rotation
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
