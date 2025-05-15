package net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable;

import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class RotatingCoins extends Interactable{

    public static float defaultMovementSpeed = 1.25f;
    public static int maxBounces = 2;
    private float amountOfMineralsGained;
    private boolean activated = false;

    public RotatingCoins(SpriteAnimationConfiguration spriteAnimationConfiguration, MovementConfiguration movementConfiguration, float amountOfMineralsGained) {
        super(spriteAnimationConfiguration, movementConfiguration);
        this.amountOfMineralsGained = amountOfMineralsGained;
    }

    @Override
    public void activateObject() {
        if(!activated){
            activated = true;
            PlayerInventory.getInstance().addMinerals(amountOfMineralsGained);
            AudioManager.getInstance().addAudio(AudioEnums.CoinCollected); //placeholder
            this.setVisible(false);
        }
    }

    public float getAmountOfMineralsGained() {
        return amountOfMineralsGained;
    }

    public void setAmountOfMineralsGained(float amountOfMineralsGained) {
        this.amountOfMineralsGained = amountOfMineralsGained;
    }
}
