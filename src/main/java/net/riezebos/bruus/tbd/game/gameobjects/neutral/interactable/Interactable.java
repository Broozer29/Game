package net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public abstract class Interactable extends GameObject {

    public Interactable(SpriteAnimationConfiguration spriteAnimationConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration);
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
    }

    public void activateObject() {
        //supposed to be overriden
    }


}
