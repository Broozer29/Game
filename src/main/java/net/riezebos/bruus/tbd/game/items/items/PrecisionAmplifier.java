package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

import java.util.Random;

public class PrecisionAmplifier extends Item {

    private float critChance;
    private Random random = new Random();

    public PrecisionAmplifier () {
        super(ItemEnums.PrecisionAmplifier, 1,  ItemApplicationEnum.BeforeCollision);
        calculateCritChance();
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateCritChance();
    }

    private void calculateCritChance(){
        this.critChance = this.quantity * 10;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Not needed as critical strikes are no buffs/debuffs
    }

    @Override
    public void modifyAttackingObject (GameObject attack, GameObject target) {
        float roll = random.nextFloat() * 100; // Roll a number between 0 and 100

        if (roll < critChance) {
            attack.setACrit(true);
        }
    }

    @Override
    public boolean isAvailable(){
        boolean isAvailable = false;

        if(!this.itemEnum.isEnabled()){
            return isAvailable;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum).getQuantity() < 10; //Check if player already has 10 of them
        } else {
            return true; //Player has 0 stacks, so we return true
        }
    }
}
