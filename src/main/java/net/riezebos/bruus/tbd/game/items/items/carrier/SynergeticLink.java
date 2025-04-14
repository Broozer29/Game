package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class SynergeticLink extends Item {

    public static float shuttleAttackSpeedBonusPerShip = 0.075f;
    public static float scoutBonusDamagePerShip = 0.1f;
    private float currentShuttleAttackSpeedBonus;
    private float currentScoutBonusDamage;

    public SynergeticLink() {
        super(ItemEnums.SynergeticLink, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        //Protoss Drones should continuesly update their base stats
        //This method should update the amounts by constantly checking to see how many are alive
        recalculateScoutAttackDamage();
        recalculateShuttleAttackSpeed();
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    private void recalculateShuttleAttackSpeed(){
        this.currentShuttleAttackSpeedBonus = (this.quantity * shuttleAttackSpeedBonusPerShip) * FriendlyManager.getInstance().getDronesByDroneType(DroneTypes.ProtossScout).size();
    }

    private void recalculateScoutAttackDamage(){
        this.currentScoutBonusDamage = (this.quantity * scoutBonusDamagePerShip) * FriendlyManager.getInstance().getDronesByDroneType(DroneTypes.ProtossShuttle).size();
    }

    public float getCurrentShuttleAttackSpeedBonus() {
        return currentShuttleAttackSpeedBonus;
    }

    public float getCurrentScoutBonusDamage() {
        return currentScoutBonusDamage;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}