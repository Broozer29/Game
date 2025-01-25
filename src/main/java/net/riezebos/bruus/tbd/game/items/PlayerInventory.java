package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.items.*;
import net.riezebos.bruus.tbd.game.items.items.captain.*;
import net.riezebos.bruus.tbd.game.items.items.firefighter.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerInventory {
    private static PlayerInventory instance = new PlayerInventory();
    private Map<ItemEnums, Item> items = new HashMap<>();
    private float cashMoney = 999999999;
    private PlayerInventory () {
//        addItem(ItemEnums.ThornedPlates);
//        addItem(ItemEnums.EntanglingFlames);

//        addItem(ItemEnums.ModuleElectrify);
//        addItem(ItemEnums.ModuleCommand);

//        for(int i = 0; i < 4; i++){
//            addItem(ItemEnums.GuardianDrone);
//        }
        PlayerStats.getInstance().setShopRerollDiscount(99);
//        addItem(ItemEnums.PlatinumSponge);
    }



    public void resetInventory(){
        items.clear();
        cashMoney = 100f;
    }

    public static PlayerInventory getInstance () {
        return instance;
    }


    public void addItem (ItemEnums itemEnum) {
        Item item = items.compute(itemEnum, (key, existingItem) -> {
            if (existingItem == null) {
                Item newItem = createItemFromEnum(itemEnum);
                if (newItem != null) {
                    return newItem;
                }
            } else {
                existingItem.increaseQuantityOfItem(1);
            }
            return existingItem;
        });
        if(item != null) {
            activateUponPurchaseItemEffects(item);
        }
    }



    public Item createItemFromEnum (ItemEnums itemEnum) {
        switch (itemEnum) {
            case ModuleScorch:
                return new ModuleScorch();
            case PlasmaLauncher:
                return new PlasmaLauncher();
            case MoneyPrinter:
                return new MoneyPrinter();
            case ArmorPiercingRounds:
                return new ArmorPiercingRounds();
            case EnergySiphon:
                return new EnergySyphon();
            case StickyDynamite:
                return new StickyDynamite();
            case PlasmaCoatedBullets:
                return new PlasmaCoatedBullets();
            case PhotonPiercer:
                return new PhotonPiercer();
            case CannisterOfGasoline:
                return new CannisterOfGasoline();
            case SelfRepairingSteel:
                return new SelfRepairingSteel();
            case Battery:
                return new Battery();
            case FocusCrystal:
                return new FocusCrystal();
            case PrecisionAmplifier:
                return new PrecisionAmplifier();
            case PlatinumSponge:
                return new PlatinumSponge();
            case EmergencyRepairBot:
                return new EmergencyRepairBot();
            case Overclock:
                return new Overclock();
            case RepulsionArmorPlate:
                return new RepulsionArmorPlate();
            case GuardianDrone:
                return new GuardianDrones();
            case CriticalOverloadCapacitor:
                return new CriticalOverloadCapacitor();
            case BarrierSuperSizer:
                return new BarrierSupersizer();
            case DrillerModule:
                return new DrillerModule();
            case BouncingModuleAddon:
                return new BouncingModuleAddon();
            case VIPTicket:
                return new VIPTicket();
            case ElectricDestabilizer:
                return new ElectricDestabilizer();
            case ModuleAccuracy:
                return new ModuleAccuracy();
            case ElectricSupercharger:
                return new ElectricSupercharger();
            case ThornedPlates:
                return new ThornedPlates();
            case Thornweaver:
                return new Thornweaver();
            case BarbedAegis:
                return new BarbedAegis();
            case BarbedMissiles:
                return new BarbedMissiles();
            case ModuleElectrify:
                return new ModuleElectrify();
            case ModuleCommand:
                return new ModuleCommand();
            case Contract:
                return new Contract();
            case StickyOil:
                return new StickyOil();
            case CorrosiveOil:
                return new CorrosiveOil();
            case ScorchingFury:
                return new ScorchingFury();
            case FlameDetonation:
                return new FlameDetonation();
            case EscalatingFlames:
                return new EscalatingFlames();
            case EntanglingFlames:
                return new EntanglingFlames();
            case BargainBucket:
                return new BargainBucket();
            default:
                System.out.println("I tried to create: " + itemEnum + " but fell in default, did you forget to add it to the inventory creation?");
                return null;
        }
    }

    private void activateUponPurchaseItemEffects (Item item){
        if(item.getApplicationMethod().equals(ItemApplicationEnum.UponAcquiring)){
            item.applyEffectToObject(null);
        }
    }

    public List<Item> getItemsByApplicationMethod (ItemApplicationEnum applicationMethod) {
        return items.values().stream()
                .filter(item -> item.getApplicationMethod() == applicationMethod)
                .collect(Collectors.toList());
    }


    public Item getItemByName (ItemEnums itemName) {
        return items.get(itemName);
    }

    public Map<ItemEnums, Item> getItems () {
        return items;
    }

    public float getCashMoney () {
        return cashMoney;
    }

    public void setCashMoney (float cashMoney) {
        this.cashMoney = cashMoney;
    }

    public void addMinerals (float amount){
//        if(amount > 0){
//            OnScreenTextManager.getInstance().addMineralsGainedText(amount);
//        }
        this.cashMoney += amount;
    }

    public void spendCashMoney(float amount){
        this.cashMoney -= amount;
    }

    public void removeItemFromInventory (ItemEnums item) {
        if(this.items.containsKey(item)){
            items.remove(item);
        }
    }
}
