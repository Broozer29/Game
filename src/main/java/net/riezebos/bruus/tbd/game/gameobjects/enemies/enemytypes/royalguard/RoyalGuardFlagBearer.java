package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.royalguard;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FlagBearerPulse;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class RoyalGuardFlagBearer extends Enemy {

    /*
        Support rol, op het het huidige moment vliegt hij permanent rond op het veld en vuurt een cirkel aanval. De cirkel healed fellow enemies en damaged de spelers
            - Healing vervangen met attack speed bonus?
            - Of een 360 graden cirkel projectielen schieten?
        Maximaal 2 op het scherm
     */

    private int lastRegisteredBoardBlock = 0;
    private int rotationAngleDegrees = 0;
    public RoyalGuardFlagBearer(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1f);
        this.attackSpeed = 0.025f;
        this.knockbackStrength = 8;

        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder) {
            hoverPathFinder.setSecondsToHoverStill(0);
            hoverPathFinder.setShouldDecreaseBoardBlock(true);
        }
    }

    @Override
    public void fireAction() {
        attackSpeed = 0.095f;
        // Check if the attack cooldown has been reached
        double currentTime = GameState.getInstance().getGameSeconds();

        if (this.getCurrentBoardBlock() != lastRegisteredBoardBlock) {
            lastRegisteredBoardBlock = this.getCurrentBoardBlock();
        }


        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder) {
            if (this.getCurrentBoardBlock() <= 1) { // if its reaches 0 it will move out of bounds
                hoverPathFinder.setDecreaseBoardBlockAmountBy(-1);
            } else if (this.getCurrentBoardBlock() >= 7) {
                hoverPathFinder.setDecreaseBoardBlockAmountBy(1);
            }
        }

        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire) {
//            doMyThing();
            shootMissiles(rotationAngleDegrees);
            this.rotationAngleDegrees = increaseRotationAngle(this.rotationAngleDegrees);
            lastAttackTime = currentTime; // Update the last attack time after firing
        }
    }

    private void doMyThing() {
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setScale(1);
        missileSpriteConfiguration.setImageType(ImageEnums.EnergyCircle);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(missileSpriteConfiguration, 2, false);

        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(this.getDamage(), false, true, false, false, false, false);
        SpecialAttack specialAttack = new FlagBearerPulse(spriteAnimationConfiguration, specialAttackConfiguration);
        specialAttack.setOwnerOrCreator(this);
        specialAttack.setObjectType("Flamer Special Attack");

        specialAttack.setObjectToCenterAround(this);
        specialAttack.setCenteredAroundObject(true);

        specialAttack.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        specialAttack.getAnimation().setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        specialAttack.setTransparancyAlpha(false, 0.5f, 0f);
        specialAttack.setNeutral(true);
        this.objectsFollowingThis.add(specialAttack);

        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }

    private Point calculateBulletDestination(int angleDegrees, int distance, int centerX, int centerY) {
        // Convert the angle from degrees to radians because Math functions use radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }

    private int increaseRotationAngle(int rotationAngle) {
        int newAngle = rotationAngle + 16;
        if (newAngle > 360) {
            newAngle = 0;
        }

        return newAngle;
    }


    private void shootMissiles(int angleDegrees) {
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        // The charging up attack animation has finished, create and fire the missile
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                missileType.getImageType(), 0.5f);


        int movementSpeed = 4;
        //Create missile movement attributes and create a movement configuration

        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, this.movementRotation
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;
        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly,
                false, true, true);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        //get the coordinates for rotation of the missile
        Point rotationCoordinates = calculateBulletDestination(angleDegrees, 600, this.getCenterXCoordinate(), this.getCenterYCoordinate());

        missile.resetMovementPath();

        missile.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(rotationCoordinates); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }
}