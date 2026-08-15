package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.mutalisk.BileTravelPlaceholder;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MutaliskMissile extends Missile {

    private boolean firstTimeMoved = false;
    private int stepsInPath = 0;
    private int stepsTaken = 0;
    private boolean isBileBit = false;
    private float flatHealValue = 0;
    public static float healAmount = 10f;
    private double gameSecondsSinceLastBileItemActivation = 0;
    private int amountOfBileBits = 20;

    public MutaliskMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        initDestructionAnimation(missileConfiguration);
        this.isDamageable = false;
        this.isDestructable = true;
        gameSecondsSinceLastBileItemActivation = GameState.getInstance().getGameSeconds() - 0.25f;
        BileTravelPlaceholder.timeBetweenExplosion = 0.35f;
    }

    private void initDestructionAnimation(MissileConfiguration missileConfiguration) {
        if (missileConfiguration.getDestructionType() != null) {
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);
        }
    }

    public void missileAction() {
        if (!firstTimeMoved) {
            if (movementConfiguration.getCurrentPath() == null) {
                return;
            }
            firstTimeMoved = true;
            stepsInPath = movementConfiguration.getCurrentPath().getWaypoints().size();

            if (!this.isBileBit) {
                this.flatHealValue = healAmount;
            }
        }
        stepsTaken++;
        float percent = 1.0f - ((float) stepsTaken / (float) stepsInPath);
        percent = Math.max(0.0f, Math.min(1.0f, percent)); // Clamp to [0, 1]

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BileTravelRange) != null &&
                !isBileBit &&
                GameState.getInstance().getGameSeconds() - gameSecondsSinceLastBileItemActivation > BileTravelPlaceholder.timeBetweenExplosion) {
            for (int i = 0; i < amountOfBileBits; i++) {
                fireMissile(this);
            }
            gameSecondsSinceLastBileItemActivation = GameState.getInstance().getGameSeconds();
        }


        if (!isBileBit && percent <= 0.15f) {
            detonate();
        } else {
            this.setTransparancyAlpha(false, percent, 0);
            if (this.getAnimation().getTransparancyAlpha() <= 0.05f) {
                this.setVisible(false);
            }
        }
    }

    @Override
    public void handleCollision(GameObject collidedObject) {
        if (!isBileBit) {
            this.ownerOrCreator.heal(flatHealValue, true);
            detonateMissile(collidedObject);
        } else {
            super.pierceAndBounce(collidedObject);
        }
        this.setVisible(false);
    }

    private void detonateMissile(GameObject collidedObject) {
        this.dealDamageToGameObject(collidedObject);
        playDetonationAnimation();
    }

    private void playDetonationAnimation() {
        SpriteConfiguration spriteConfig = new SpriteConfiguration();
        spriteConfig.setxCoordinate(-500);
        spriteConfig.setyCoordinate(-500);
        spriteConfig.setScale(0.35f);
        spriteConfig.setImageType(ImageEnums.MutaliskMissileExplosion);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfig, 1, false);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }

    private void detonate() {
        for (int i = 0; i < amountOfBileBits; i++) {
            fireMissile(this);
        }
        this.setVisible(false);
    }

    private void fireMissile(GameObject owner) {
        int centerXCoordinate = Math.toIntExact(Math.round(owner.getCenterXCoordinate() + (owner.getWidth() * 0.25)));
        int centerYCoordinate = Math.toIntExact(Math.round(owner.getCenterYCoordinate() + (owner.getHeight() * 0.25)));
        float scale = 0.25f;

        MissileCreator missileCreator1 = MissileCreator.getInstance();
        SpriteConfiguration spriteConfiguration = missileCreator1.createMissileSpriteConfig(owner.getCenterXCoordinate(), owner.getCenterYCoordinate(),
                MissileEnums.MutaliskMissile.getImageType(), scale);

        // Initial speed doesn't matter, will be recalculated after destination is set
        MovementConfiguration movementConfiguration = missileCreator1.createMissileMovementConfig(
                1, new DestinationPathFinder(), Direction.RIGHT
        );

        Point destination = calculateDestination();
        movementConfiguration.setDestination(destination);

        boolean isFriendly = true;

        SpaceShip spaceShip = (SpaceShip) owner.getOwnerOrCreator();
        float damage = spaceShip.getDamage();
        boolean isExplosive = false;
        MissileConfiguration missileConfiguration = missileCreator1.createMissileConfiguration(MissileEnums.MutaliskMissile, damage, MissileEnums.MutaliskMissile.getDeathOrExplosionImageEnum(), isFriendly, isExplosive,
                true, true);

        Missile missile = missileCreator1.createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        missile.setAllowedVisualsToRotate(false);
        missile.setOwnerOrCreator(owner);
        missile.resetMovementPath();
        missile.setCenterCoordinates(centerXCoordinate, centerYCoordinate);
        missile.getMovementConfiguration().setDestination(destination);

        // Calculate speed based on distance so all missiles take the same time to reach their destination
        double distance = Math.sqrt(Math.pow(destination.getX() - centerXCoordinate, 2) + Math.pow(destination.getY() - centerYCoordinate, 2));
        float normalizedSpeed = (float) (distance / maxDistanceRange) * baseMovementSpeed;
        missile.getMovementConfiguration().setMovementSpeed(normalizedSpeed);
        MutaliskMissile mutaliskMissile = (MutaliskMissile) missile;
        mutaliskMissile.isBileBit = true;

        MissileManager.getInstance().addExistingMissile(missile);
    }

    private int minDistanceRange = 80;
    private int maxDistanceRange = 170;
    private float baseMovementSpeed = 5.5f;

    private Point calculateDestination() {
        maxDistanceRange = 200;
        minDistanceRange = 90;
        double ownerCenterX = this.getCenterXCoordinate();
        double ownerCenterY = this.getCenterYCoordinate();

        // Random distance between min and max
        double distance = minDistanceRange + (Math.random() * (maxDistanceRange - minDistanceRange));

        // Random angle in 360 degrees (full circle for grenade explosion effect)
        double angle = Math.random() * 2 * Math.PI;

        // Calculate destination point using trigonometry
        double targetX = ownerCenterX + distance * Math.cos(angle);
        double targetY = ownerCenterY + distance * Math.sin(angle);

        return new Point(targetX, targetY);
    }
}