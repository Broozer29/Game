package game.items;

import game.items.items.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerInventory {
    private static PlayerInventory instance = new PlayerInventory();
    private Map<ItemEnums, Item> items = new HashMap<>();

    private PlayerInventory () {
        addItem(ItemEnums.Overclock);
        addItem(ItemEnums.EmergencyRepairBot);
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
                existingItem.increaseQuantityOfItem(existingItem.getQuantity() + 1);
            }
            return existingItem;
        });
    }

    private Item createItemFromEnum (ItemEnums itemEnum) {
        switch (itemEnum) {
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

    public Item getItemByName (ItemEnums itemName) {
        return items.get(itemName);
    }


}
