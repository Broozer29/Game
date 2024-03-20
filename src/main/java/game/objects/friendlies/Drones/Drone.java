package game.objects.friendlies.Drones;

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
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class Drone extends FriendlyObject {
    public Drone (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
    }

    public void activateObjectAction () {
        switch (this.friendlyObjectType) {
            case Missile_Drone:
                fireMissile();
                break;
            default:
                break;
        }
    }

    private void fireMissile () {
        if (attackSpeedCurrentFrameCount >= attackSpeed) {
            SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
            missileSpriteConfiguration.setxCoordinate(xCoordinate + this.width / 2);
            missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
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

            MissileConfiguration missileConfiguration = new MissileConfiguration(MissileTypeEnums.DefaultPlayerLaserbeam
                    , 100, 100, null, damage, ImageEnums.Impact_Explosion_One, isFriendly, allowedToDealDamage, objectType,
                    MissileTypeEnums.DefaultPlayerLaserbeam.isBoxCollision());


            Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);
            newMissile.setOwnerOrCreator(this);
            MissileManager.getInstance().addExistingMissile(newMissile);
            attackSpeedCurrentFrameCount = 0;

        } else {
            attackSpeedCurrentFrameCount++;
        }
    }

}