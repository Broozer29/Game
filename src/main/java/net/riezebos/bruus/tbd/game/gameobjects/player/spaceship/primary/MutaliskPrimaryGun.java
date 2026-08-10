package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.primary;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerPrimaryAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.PrimaryPlayerGun;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MutaliskPrimaryGun extends PrimaryPlayerGun {

    @Override
    public void fire(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType, SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return;
        }
        if (attackOffCooldown(owner)) {
            fireGenericBehaviour(owner);
            fireMissile(owner);
        }
    }

    private void fireMissile(SpaceShip owner) {
        float movementSpeed = 5.3f;

        int centerXCoordinate = Math.toIntExact(Math.round(owner.getCenterXCoordinate() + (owner.getWidth() * 0.25)));
        int centerYCoordinate = Math.toIntExact(Math.round(owner.getCenterYCoordinate() + (owner.getHeight() * 0.25)));
        float scale = 0.75f;


        MissileCreator missileCreator1 = MissileCreator.getInstance();
        SpriteConfiguration spriteConfiguration = missileCreator1.createMissileSpriteConfig(owner.getCenterXCoordinate(), owner.getCenterYCoordinate(),
                MissileEnums.MutaliskMissile.getImageType(), scale);


        MovementConfiguration movementConfiguration = missileCreator1.createMissileMovementConfig(
                movementSpeed, new DestinationPathFinder(), Direction.RIGHT
        );

        movementConfiguration.setDestination(calculateDestination(owner));


        boolean isFriendly = true;
        float damage = owner.getDamage() * 1f;
        boolean isExplosive = false;
        MissileConfiguration missileConfiguration = missileCreator1.createMissileConfiguration(MissileEnums.MutaliskMissile, damage, MissileEnums.MutaliskMissile.getDeathOrExplosionImageEnum(), isFriendly, isExplosive,
                true, true);

        Missile missile = missileCreator1.createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        missile.setAllowedVisualsToRotate(false);
        missile.setOwnerOrCreator(owner);
        missile.resetMovementPath();
        missile.setCenterCoordinates(centerXCoordinate, centerYCoordinate);
        missile.getMovementConfiguration().setDestination(calculateDestination(owner));
        missile.setOwnerOrCreator(owner);

        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Point calculateDestination(SpaceShip owner) {
        double ownerCenterX = owner.getCenterXCoordinate();
        double ownerCenterY = owner.getCenterYCoordinate();

        return new Point(ownerCenterX + 150, ownerCenterY);
    }
}
