package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class StationaryExplodingBomb extends Missile {


    private int angleIncrement = 45;
    private double amountOfTimesAnimHasSpedUp = 0;
    private float explosionSize = 2.5f;
    private boolean createMissilesOnExplosion = false;

    public StationaryExplodingBomb(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        this.animation.setFrameDelay(2); //default
        this.isDamageable = false;
        this.isDestructable = true;
        super.setAllowedToMove(false);

        if (missileConfiguration.getDestructionType() != null) {
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 1, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);
            this.destructionAnimation.setAnimationScale(0.75f);
        }
    }

    public void missileAction() {
        if (this.animation.getCurrentFrame() == this.animation.getTotalFrames()) {
            amountOfTimesAnimHasSpedUp++;
            int newFrameDelay = Math.max(0, this.animation.getFrameDelay() - 1);
            this.animation.setFrameDelay(newFrameDelay);
        }

        if (amountOfTimesAnimHasSpedUp > 4) {
            this.detonateMissile();
        }
    }

    public void detonateMissile() {
        createExplosion();
    }

    private void createExplosion() {
        if(createMissilesOnExplosion){
            createMissileRing();
        }

        SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
        spriteConfiguration1.setxCoordinate(this.xCoordinate);
        spriteConfiguration1.setyCoordinate(this.yCoordinate);
        spriteConfiguration1.setScale(explosionSize);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 1, false);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Missile_Explosion);

        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(this.isFriendly(), damage, true, false);
        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setOwnerOrCreator(this.ownerOrCreator);
        explosion.setScale(explosionSize);
        explosion.setCenterCoordinates(this.animation.getCenterXCoordinate(), this.animation.getCenterYCoordinate());
        ExplosionManager.getInstance().addExplosion(explosion);

        this.setVisible(false);
    }

    private void createMissileRing(){
        for (int angle = 0; angle < (361 - angleIncrement); angle += angleIncrement) {
            // Directly call shootMissiles using current angle
            shootMissiles(angle);
        }
    }

    private void shootMissiles(double angleDegrees) {
        MissileEnums missileType = MissileEnums.DefaultAnimatedBullet;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(ImageEnums.MotherShipDroneMissile);
        missileSpriteConfiguration.setScale(0.2f);


        float movementSpeed = 1.75f;
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
        String objectType = "Enemy Protoss Beacon";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage() * 0.25f, missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);


        //Calculate the angle based on the current chargingAnimation. Because we want to fire from 4 directions, we also need to keep
        //track of the angle that the given chargingAnimation has in this method
        Point bulletOrigin = new Point(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        Point bulletDestination = calculateBulletDestination(angleDegrees, 400, this.getCenterXCoordinate(), this.getCenterYCoordinate());

        missile.resetMovementPath();

        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.getMovementConfiguration().setDestination(bulletDestination); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Point calculateBulletDestination(double angleDegrees, int distance, int centerX, int centerY) {
        // Convert the angle from degrees to radians because Math functions use radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }

    public void setCreateMissilesOnExplosion(boolean createMissilesOnExplosion) {
        this.createMissilesOnExplosion = createMissilesOnExplosion;
    }

    public float getExplosionSize() {
        return explosionSize;
    }

    public void setExplosionSize(float explosionSize) {
        this.explosionSize = explosionSize;
    }

    public int getAngleIncrement() {
        return angleIncrement;
    }

    public void setAngleIncrement(int angleIncrement) {
        this.angleIncrement = angleIncrement;
    }
}