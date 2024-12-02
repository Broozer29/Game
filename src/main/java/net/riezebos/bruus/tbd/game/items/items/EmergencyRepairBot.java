package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class EmergencyRepairBot extends Item {

    private float healingFactor;
    //Untested

    public EmergencyRepairBot () {
        super(ItemEnums.EmergencyRepairBot, 1, ItemApplicationEnum.ApplyOnCreation);
        healingFactor = 0.75f;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        float currentHp = gameObject.getCurrentHitpoints();
        float maxHp = gameObject.getMaxHitPoints();

        // Check if current HP is 25% or lower of the max HP and if more than 1 quantity is left
        if (currentHp <= maxHp * 0.25 && this.quantity >= 1) {
            float healingAmount = maxHp * healingFactor; // Calculate 75% of max HP
            gameObject.takeDamage(-healingAmount); // Apply healing
            // Reduce the quantity of the item by 1
            this.decreaseQuantityOfItem(1);
            AudioManager.getInstance().addAudio(AudioEnums.Power_Up_Acquired);
        }
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantityOfItem (int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }

}
