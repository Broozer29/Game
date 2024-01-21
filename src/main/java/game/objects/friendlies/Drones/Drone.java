package game.objects.friendlies.Drones;

import game.movement.Direction;
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
    public Drone (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration);
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
            PathFinder pathFinder = new RegularPathFinder();

            int xMovementSpeed = 5;
            int yMovementSpeed = 5;

            SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
            missileSpriteConfiguration.setxCoordinate(xCoordinate + this.width / 2);
            missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
            missileSpriteConfiguration.setImageType(ImageEnums.Player_Laserbeam);

            MissileConfiguration missileConfiguration = new MissileConfiguration(MissileTypeEnums.DefaultPlayerLaserbeam,
                    100, 100, null, null, isFriendly()
                    , pathFinder, Direction.RIGHT, xMovementSpeed, yMovementSpeed, true
                    , "Drone regular missile", (float) 15, MovementPatternSize.SMALL, MissileTypeEnums.DefaultPlayerLaserbeam.isBoxCollision());

            if (pathFinder instanceof HomingPathFinder) {
                missileConfiguration.setTargetToChase(((HomingPathFinder) pathFinder).getTarget(true));
            }

            Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);
            newMissile.setOwnerOrCreator(this);
            MissileManager.getInstance().addExistingMissile(newMissile);
            attackSpeedCurrentFrameCount = 0;

        } else {
            attackSpeedCurrentFrameCount++;
        }
    }

}