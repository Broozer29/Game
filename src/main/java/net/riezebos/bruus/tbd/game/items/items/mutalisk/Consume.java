package net.riezebos.bruus.tbd.game.items.items.mutalisk;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class Consume extends Item {

    public static int hitsPointsGrantedPerKill = 1;
    public static int killsRequiredPerHitPoint = 10;
    private int killsAcquiredBeforeCounting = -100;
    public static int currentKillCount = 0;
    public static float currentMaxHpBonus = 0;

    public Consume () {
        super(ItemEnums.Consume, 1, ItemApplicationEnum.CustomActivation);
        killsAcquiredBeforeCounting = GameStatsTracker.getInstance().getEnemiesKilled(); //theoretically this should work, as it only gets constructed once in a run and deleted when the playerinventory is reset
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //should be called on each enemy kill
        currentKillCount++;
        if((currentKillCount - killsAcquiredBeforeCounting) % killsRequiredPerHitPoint == 0){
            currentMaxHpBonus += hitsPointsGrantedPerKill;
        }
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }


    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk);
    }

}
