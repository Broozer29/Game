package net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable;

import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class RecyclePart extends Interactable {

    public static float defaultMovementSpeed = 0.5f;
    public static int maxBounces = 2;
    private boolean activated = false;

    public RecyclePart(SpriteAnimationConfiguration spriteAnimationConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, movementConfiguration);
    }

    @Override
    public void activateObject(SpaceShip activator) {
        if (!activated) {
            activated = true;
            activator.getSecondaryGun().addSpecialCharge(activator);
            this.setVisible(false);
        }
    }
}