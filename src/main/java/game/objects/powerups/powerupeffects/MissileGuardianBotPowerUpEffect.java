package game.objects.powerups.powerupeffects;

import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import game.objects.friendlies.*;
import game.objects.player.spaceship.SpaceShip;
import game.objects.powerups.creation.PowerUpEffect;
import game.objects.powerups.PowerUpEnums;
import game.objects.player.PlayerManager;
import game.util.OrbitingObjectsFormatter;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
