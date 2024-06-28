package game.items;

import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.items.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerInventory {
    private static PlayerInventory instance = new PlayerInventory();
    private Map<ItemEnums, Item> items = new HashMap<>();
    private float cashMoney = 2000;
    private PlayerInventory () {
//        addItem(ItemEnums.DrillerModule);
//        addItem(ItemEnums.BouncingModuleAddon);
//        addItem(ItemEnums.PlasmaCoatedBullets);
    }

    public void resetInventory(){
        items.clear();
        cashMoney = 0f;
    }

    public static PlayerInventory getInstance () {
        return instance;
    }


    public void addItem (ItemEnums itemEnum) {
        items.compute(itemEnum, (key, existingItem) -> {
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
    }

    private Item createItemFromEnum (ItemEnums itemEnum) {
        switch (itemEnum) {
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
            default:
                System.out.println("I tried to create: " + itemEnum + " but fell in default, did you forget to add it to the inventory creation?");
                return null;
        }
    }

    public List<Item> getItemsByApplicationMethod (ItemApplicationEnum applicationMethod) {
        return items.values().stream()
                .filter(item -> item.getApplicationMethod() == applicationMethod)
                .collect(Collectors.toList());
    }

    public List<Item> getItemByActivationTypes(EffectActivationTypes activationType){
        return items.values().stream()
                .filter(item -> item.getEffectType() == activationType)
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

    public void gainCashMoney(float amount){
        this.cashMoney += amount;
    }

    public void spendCashMoney(float amount){
        this.cashMoney -= amount;
    }

}
