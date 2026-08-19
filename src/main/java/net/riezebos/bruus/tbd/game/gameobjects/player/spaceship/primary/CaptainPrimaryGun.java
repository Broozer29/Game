package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.primary;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerPrimaryAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.PrimaryPlayerGun;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.captain.BigIron;
import net.riezebos.bruus.tbd.game.items.items.captain.HighVelocityLasers;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.CustomAudioClip;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class CaptainPrimaryGun extends PrimaryPlayerGun {

    @Override
    public void fire(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType, SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return;
        }
        if (attackOffCooldown(owner)) {
            fireGenericBehaviour(owner);
            handleRegularMissile(xCoordinate, yCoordinate, playerAttackType, owner);
        }
    }

    private void handleRegularMissile(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType, SpaceShip owner) {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BigIron) != null) {
            handleChargingLaserbeam(owner);
        } else {
            ImageEnums visualImage = playerStats.getPlayerMissileImage();
            PathFinder pathFinder = new RegularPathFinder();
            fireMissile(xCoordinate, yCoordinate, visualImage, 1, pathFinder, playerAttackType.getCorrespondingMissileEnum(), owner);

            if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.SideCannons) != null){
                fireSideCannonMissiles(owner, playerAttackType.getCorrespondingMissileEnum().getImageType(),true);
                fireSideCannonMissiles(owner, playerAttackType.getCorrespondingMissileEnum().getImageType(),false);
            }
            playFiringAudio(playerAttackType);
        }
        orangeBarCurrentValue = -1;
        orangeBarMaxValue = -1;
    }

    private void playFiringAudio(PlayerPrimaryAttackTypes playerAttackType) {
//        playMissileAudio(AudioEnums.NewPlayerLaserbeam); //turned it off since it became tedious to listen to and breaks the music
    }

    @Override
    public void stopFiring(SpaceShip owner) {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BigIron) != null) {
            releaseChargingLaserbeam(owner);
        }
    }


    private void playMissileAudio(AudioEnums audioEnum) {
        this.audioManager.addAudio(audioEnum);
    }


    private void fireMissile(int xCoordinate, int yCoordinate, ImageEnums playerMissileType,
                             float missileScale, PathFinder missilePathFinder, MissileEnums attackType, SpaceShip owner) {
        int movementSpeed = 6;
        MissileCreator missileCreator1 = MissileCreator.getInstance();
        SpriteConfiguration spriteConfiguration = missileCreator1.createMissileSpriteConfig(xCoordinate, yCoordinate,
                playerMissileType, missileScale);


        MovementConfiguration movementConfiguration = missileCreator1.createMissileMovementConfig(
                movementSpeed, missilePathFinder, Direction.RIGHT
        );


        boolean isFriendly = true;
        float damage = owner.getDamage() * 2;
        damage *= 1 + (totalDamageBonus); //bigiron dmg bonus
        boolean isExplosive = false;
        MissileConfiguration missileConfiguration = missileCreator1.createMissileConfiguration(attackType, damage, attackType.getDeathOrExplosionImageEnum(), isFriendly, isExplosive,
                true, true);

        PlayerStats instance = PlayerStats.getInstance();
        if (!isExplosive) {
            missileConfiguration.setPiercesMissiles(instance.getPiercingMissilesAmount() > 0);
            missileConfiguration.setAmountOfPierces(instance.getPiercingMissilesAmount());
        }
        Missile missile = missileCreator1.createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.HighVelocityLasers) != null) {
            HighVelocityLasers lasers = (HighVelocityLasers) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.HighVelocityLasers);
            lasers.applyEffectToObject(missile);
        }

        missile.setOwnerOrCreator(owner);
        missile.setCenterCoordinates(missile.getCenterXCoordinate(), owner.getCenterYCoordinate());
        missile.resetMovementPath();

        missile.setCanBounce(true);

        this.missileManager.addExistingMissile(missile);
    }

    private void fireSideCannonMissiles(SpaceShip owner, ImageEnums playerMissileType, boolean upOrDown) {
        int movementSpeed = 6;
        MissileCreator missileCreator1 = MissileCreator.getInstance();
        SpriteConfiguration spriteConfiguration = missileCreator1.createMissileSpriteConfig(owner.getXCoordinate(), owner.getYCoordinate(),
                playerMissileType, 1);


        MovementConfiguration movementConfiguration = missileCreator1.createMissileMovementConfig(
                movementSpeed, new StraightLinePathFinder(), Direction.RIGHT
        );


        boolean isFriendly = true;
        float damage = owner.getDamage() * 2;
        damage *= 1 + (totalDamageBonus); //bigiron dmg bonus
        boolean isExplosive = false;
        MissileConfiguration missileConfiguration = missileCreator1.createMissileConfiguration(MissileEnums.DefaultAnimatedBullet, damage, MissileEnums.DefaultAnimatedBullet.getDeathOrExplosionImageEnum(), isFriendly, isExplosive,
                true, true);
        if (!isExplosive) {
            missileConfiguration.setPiercesMissiles(PlayerStats.getInstance().getPiercingMissilesAmount() > 0);
            missileConfiguration.setAmountOfPierces(PlayerStats.getInstance().getPiercingMissilesAmount());
        }

        Missile missile = missileCreator1.createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.HighVelocityLasers) != null) {
            HighVelocityLasers lasers = (HighVelocityLasers) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.HighVelocityLasers);
            lasers.applyEffectToObject(missile);
        }

        missile.resetMovementPath();
        double angle = upOrDown ? Direction.RIGHT.toAngle() + 10 : Direction.RIGHT.toAngle() - 20;
        Point bulletDestination = calculateBulletDestination(angle, 200, owner.getCenterXCoordinate() + 200, owner.getCenterYCoordinate());
        missile.setOwnerOrCreator(owner);
        missile.setCenterCoordinates(owner.getXCoordinate() + owner.getWidth() - 10, owner.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(bulletDestination);

        missile.setCanBounce(true);

        this.missileManager.addExistingMissile(missile);
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

    private float totalScaleBonus = 0f;
    private float totalDamageBonus = 0f;
    private double secondsStartedCharging = 0.0d;
    private boolean isCharging = false;
    private SpriteAnimation chargingAnimation = this.initBigIronChargingAnimation();
    private CustomAudioClip chargingAudioClip = AudioDatabase.getInstance().getAudioClip(AudioEnums.ChargingBigIronLaserbeam);

    private void handleChargingLaserbeam(SpaceShip owner) {
        if (!isCharging) {
            isCharging = true;
            secondsStartedCharging = GameState.getInstance().getGameSeconds();
            chargingAnimation = this.initBigIronChargingAnimation(); //reset animation
            owner.addPlayerFollowingAnimation(chargingAnimation);
            AnimationManager.getInstance().addUpperAnimation(chargingAnimation);
            chargingAudioClip = AudioDatabase.getInstance().getAudioClip(AudioEnums.ChargingBigIronLaserbeam);
            chargingAudioClip.setLoop(true);
            totalScaleBonus = 0f;
            totalDamageBonus = 0f;
        }
        if (isCharging) {
            double timeCharged = GameState.getInstance().getGameSeconds() - secondsStartedCharging;
            int intervals = Math.round((float) (timeCharged / BigIron.maxChargeSeconds * BigIron.amountOfIntervals)); //how many intervals have passed since we started charging
            totalScaleBonus = intervals * (BigIron.scaleGrowthPerInterval + 0.025f); //klein beetje extra scaling op de charge anim
            totalDamageBonus = intervals * (BigIron.damagePerInterval * (1 + owner.getAttackSpeedModifier()));
            chargingAnimation.setAnimationScale(1 + totalScaleBonus);

            if (timeCharged >= 2 && !chargingAudioClip.isRunning()) {
                chargingAudioClip.startClip();
                chargingAudioClip.setLoop(true);
            }
//            chargingAnimation.setCenterCoordinates(owner.getCenterXCoordinate(), owner.getCenterYCoordinate()); //recenter the animation, might not be need
        }
        if (isCharging && secondsStartedCharging + BigIron.maxChargeSeconds < GameState.getInstance().getGameSeconds()) {
            releaseChargingLaserbeam(owner);
        }
    }

    public void releaseChargingLaserbeam(SpaceShip owner) {
        //release it
        if (chargingAnimation != null) {
            chargingAnimation.setVisible(false);
        }
        if (chargingAudioClip != null) {
            chargingAudioClip.setLoop(false);
            chargingAudioClip.stopClip();
        }

        if (!isCharging) {
            return; //if we werent charging, dont fire anything
        }
        ImageEnums visualImage = playerStats.getPlayerMissileImage();
        PathFinder pathFinder = new RegularPathFinder();
        fireMissile(owner.getXCoordinate() + owner.getWidth(), owner.getCenterYCoordinate(), visualImage, 1 * (1 + totalScaleBonus), pathFinder, PlayerPrimaryAttackTypes.Laserbeam.getCorrespondingMissileEnum(), owner);
        playFiringAudio(PlayerPrimaryAttackTypes.Laserbeam);
        totalScaleBonus = 0f;
        totalDamageBonus = 0f;
        lastAttackTime = GameState.getInstance().getGameSeconds();
        isCharging = false;
    }

    private SpriteAnimation initBigIronChargingAnimation() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(-300);
        spriteConfiguration.setyCoordinate(-300);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Charging);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, true);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.addXOffset(60);
        return spriteAnimation;
    }

}
