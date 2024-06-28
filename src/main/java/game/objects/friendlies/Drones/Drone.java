package game.objects.friendlies.Drones;

import game.gamestate.GameStateInfo;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.friendlies.FriendlyObject;
import game.objects.friendlies.FriendlyObjectConfiguration;
import game.objects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class Drone extends FriendlyObject {

    private double lastAttackTime = 0.0;
    public Drone (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
    }

    public void activateObjectAction () {
        switch (this.friendlyObjectType) {
            case Missile_Drone:
                fireAction();
                break;
            default:
                break;
        }
    }

    private void fireAction () {

        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
//            if (!chargingUpAttackAnimation.isPlaying()) {
//                chargingUpAttackAnimation.refreshAnimation();
//                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
//            }
//            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                fireMissile();
                lastAttackTime = currentTime; // Update the last attack time after firing
//            }
        }
    }


    private void fireMissile(){
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.animation.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.animation.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(ImageEnums.Player_Laserbeam);


        int xMovementSpeed = 5;
        int yMovementSpeed = 5;
        float damage = 15f;
        Direction rotation = Direction.RIGHT;
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        PathFinder pathFinder = new RegularPathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                xMovementSpeed, yMovementSpeed, pathFinder, movementPatternSize, rotation
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        boolean isFriendly = true;
        if (pathFinder instanceof HomingPathFinder) {
            movementConfiguration.setTargetToChase(((HomingPathFinder) pathFinder).getTarget(isFriendly, this.xCoordinate, this.yCoordinate));
        }

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(MissileEnums.PlayerLaserbeam
                , 100, 100, null, damage, ImageEnums.Impact_Explosion_One, isFriendly, allowedToDealDamage, objectType,
                MissileEnums.PlayerLaserbeam.isBoxCollision(), false, false);


        Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);
        newMissile.setOwnerOrCreator(this);
        newMissile.setObjectType("Drone Missile");
        newMissile.resetMovementPath();
        newMissile.setCenterCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());
        MissileManager.getInstance().addExistingMissile(newMissile);
    }

}