package game.items.items;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.gameobjects.GameObject;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class EmergencyRepairBot extends Item {

    private float healingFactor;
    //Untested

    public EmergencyRepairBot () {
        super(ItemEnums.EmergencyRepairBot, 1, ItemApplicationEnum.ApplyOnCreation);
        healingFactor = 0.75f;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        float currentHp = gameObject.getCurrentHitpoints();
        float maxHp = gameObject.getMaxHitPoints();

        // Check if current HP is 25% or lower of the max HP and if more than 1 quantity is left
        if (currentHp <= maxHp * 0.25 && this.quantity >= 1) {
            float healingAmount = maxHp * healingFactor; // Calculate 75% of max HP
            gameObject.takeDamage(-healingAmount); // Apply healing
            // Reduce the quantity of the item by 1
            this.decreaseQuantityOfItem(1);
            try {
                AudioManager.getInstance().addAudio(AudioEnums.Power_Up_Acquired);
            } catch (UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantityOfItem(int amount) {
        this.quantity = Math.max(0, this.quantity - amount); // Ensure quantity doesn't go below 0
    }

}
