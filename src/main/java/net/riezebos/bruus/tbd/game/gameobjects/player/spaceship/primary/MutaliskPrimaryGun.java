package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.primary;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerPrimaryAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.PrimaryPlayerGun;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.mutalisk.BileTravelPlaceholder;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MutaliskPrimaryGun extends PrimaryPlayerGun {

    public static float damageRatio = 1f;

    @Override
    public void fire(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType, SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return;
        }
        if (attackOffCooldown(owner)) {
            fireGenericBehaviour(owner);
            fireMissile(owner);

            if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.MutaliskSideCannons) != null){
                fireSideCannonMissiles(owner, true);
                fireSideCannonMissiles(owner, false);
            }
        }
    }

    private void fireMissile(SpaceShip owner) {
        int centerXCoordinate = Math.toIntExact(Math.round(owner.getCenterXCoordinate() + (owner.getWidth() * 0.25)));
        int centerYCoordinate = Math.toIntExact(Math.round(owner.getCenterYCoordinate() + (owner.getHeight() * 0.25)));

        Missile missile = createBaseMissile(owner.getCenterXCoordinate(), owner.getCenterYCoordinate(),
                MissileEnums.MutaliskMissile.getImageType(), 0.75f, new DestinationPathFinder(),
                MissileEnums.MutaliskMissile, owner);

        missile.setAllowedVisualsToRotate(false);
        missile.setOwnerOrCreator(owner);
        missile.resetMovementPath();
        missile.setCenterCoordinates(centerXCoordinate, centerYCoordinate);
        missile.getMovementConfiguration().setDestination(calculateDestination(owner));

        MissileManager.getInstance().addExistingMissile(missile);
    }

    private void fireSideCannonMissiles(SpaceShip owner, boolean upOrDown) {
        Missile missile = createBaseMissile(owner.getCenterXCoordinate(), owner.getCenterYCoordinate(),
                MissileEnums.MutaliskMissile.getImageType(), 0.75f, new DestinationPathFinder(),
                MissileEnums.MutaliskMissile, owner);

        missile.setAllowedVisualsToRotate(false);
        missile.resetMovementPath();
        double angle = upOrDown ? Direction.RIGHT.toAngle() + 30 : Direction.RIGHT.toAngle() - 30;
        Point bulletDestination = calculateBulletDestination(angle, owner.getCenterXCoordinate(), owner.getCenterYCoordinate());
        missile.setOwnerOrCreator(owner);
        missile.setCenterCoordinates(owner.getXCoordinate() + owner.getWidth() - 10, owner.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(bulletDestination);

        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Missile createBaseMissile(int xCoordinate, int yCoordinate, ImageEnums playerMissileType,
                                      float missileScale, PathFinder missilePathFinder, MissileEnums attackType, SpaceShip owner) {
        float movementSpeed = 5.3f;
        MissileCreator missileCreator = MissileCreator.getInstance();

        SpriteConfiguration spriteConfiguration = missileCreator.createMissileSpriteConfig(xCoordinate, yCoordinate,
                playerMissileType, missileScale);

        MovementConfiguration movementConfiguration = missileCreator.createMissileMovementConfig(
                movementSpeed, missilePathFinder, Direction.RIGHT);

        boolean isFriendly = true;
        float damage = owner.getDamage() * damageRatio;
        boolean isExplosive = false;

        MissileConfiguration missileConfiguration = missileCreator.createMissileConfiguration(attackType, damage,
                attackType.getDeathOrExplosionImageEnum(), isFriendly, isExplosive, true, true);

        Missile missile = missileCreator.createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        return missile;
    }

    private Point calculateBulletDestination(double angleDegrees, int centerX, int centerY) {
        double angleRadians = Math.toRadians(angleDegrees);

        float horizontalDistance = 150;
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BileTravelRange) != null){
            horizontalDistance *= BileTravelPlaceholder.bonusTravelRange;
        }

        // Calculate destination such that horizontal distance matches the main missile
        int targetX = centerX + Math.round(horizontalDistance);
        int targetY = centerY + (int) (Math.tan(angleRadians) * horizontalDistance);
        return new Point(targetX, targetY);
    }

    private Point calculateDestination(SpaceShip owner) {
        double ownerCenterX = owner.getCenterXCoordinate();
        double ownerCenterY = owner.getCenterYCoordinate();

        float distanceToTravel = 150;
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BileTravelRange) != null){
            distanceToTravel *= BileTravelPlaceholder.bonusTravelRange;
        }

        return new Point(ownerCenterX + distanceToTravel, ownerCenterY);
    }
}
