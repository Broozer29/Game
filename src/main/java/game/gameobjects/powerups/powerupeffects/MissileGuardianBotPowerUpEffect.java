package game.gameobjects.powerups.powerupeffects;

import game.gameobjects.friendlies.*;
import game.gameobjects.player.spaceship.SpaceShip;
import game.gameobjects.powerups.creation.PowerUpEffect;
import game.gameobjects.powerups.PowerUpEnums;
import game.gameobjects.player.PlayerManager;
import game.util.OrbitingObjectsFormatter;

public class MissileGuardianBotPowerUpEffect extends PowerUpEffect {

    public MissileGuardianBotPowerUpEffect (PowerUpEnums powerUpType) {
        super(powerUpType);
    }

    @Override
    public void activatePower () {
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        if (spaceship.getObjectOrbitingThis().size() < 8) {
            FriendlyManager.getInstance().addDrone();
            OrbitingObjectsFormatter.reformatOrbitingObjects(spaceship, 85);
        }
    }

    @Override
    public void deactivatePower () {

    }
}
