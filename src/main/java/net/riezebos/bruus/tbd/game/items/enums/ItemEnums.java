package net.riezebos.bruus.tbd.game.items.enums;

import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ItemEnums {
    Locked(ItemRarityEnums.Locked, ItemCategory.Utility, ImageEnums.Test_Image, "Locked"),
    PlasmaCoatedBullets(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Blue_Flame, "Plasma Coated Bullets"),
    PhotonPiercer(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Concussive_Shells, "Photon Piercer"),
    CannisterOfGasoline(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.CannisterOfGasoline, "Cannister of Gasoline"),
    SelfRepairingSteel(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Heal, "Self Repairing Steel"),
    Battery(ItemRarityEnums.Common, ItemCategory.Utility, ImageEnums.Starcraft2_Battery, "Battery"),
    FocusCrystal(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2Keystone, "Focused Crystal"),
    PrecisionAmplifier(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Advanced_Optics, "Advanced Optics"),
    PlatinumSponge(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Platinum_Sponge, "Platinum Sponge"),
    EmergencyRepairBot(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Vespene_Drone, "Emergency Repair Bot"),
    Overclock(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Overclock, "Overclock"),
    RepulsionArmorPlate(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Health_Upgrade_2, "Armor Plate"),
    EnergySiphon(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Energy_Siphon, "Energy Siphon"),
    ArmorPiercingRounds(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Armor_Piercing, "Armor Piercing Rounds"),
    MoneyPrinter(ItemRarityEnums.Rare, ItemCategory.Utility, ImageEnums.MoneyPrinter, "Loot Box"),
    PlasmaLauncher(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.Starcraft2_Focused_Crystal, "Plasma Launcher"),
    GuardianDrone(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.Starcraft2_Seeker_Missile, "Guardian Drone"),
    CriticalOverloadCapacitor(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2_Auto_Tracking, "Critical Overload Capacitor"),
    BarrierSuperSizer(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Fire_Hardened_Shields, "Barrier Super Sizer"),
    DrillerModule(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.PiercingLaser, "Laser Addon: Piercing"),
    BouncingModuleAddon(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2BouncingLaser, "Laser Addon: Bouncing"),
    VIPTicket(ItemRarityEnums.Legendary, ItemCategory.Utility, ImageEnums.VIPTicket, "VIP Ticket"),
    ElectricDestabilizer(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2_Protoss_Shield_Disintegrate, "Electric Destabilizer"),
    ModuleAccuracy(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2LockOn, "Module: Accuracy"),
    ElectricSupercharger(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2_Psi_Storm2, "Electric Supercharger"),
    ThornedPlates(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.Thornedplates, "Thorned Armor Plate"),
    Thornweaver(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.ThornWeaver, "Thornweaver"),
    BarbedAegis(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.BarbedAegis, "Barbed Aegis"),
    BarbedMissiles(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.BarbedMissiles, "Barbed Missiles"),
    StickyDynamite(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.StickyDynamite, "Sticky Dynamite");


    private ItemRarityEnums itemRarity;
    private ItemCategory itemCategory;
    private ImageEnums itemIcon;
    private String itemName;

    ItemEnums(ItemRarityEnums itemRarity, ItemCategory itemCategory, ImageEnums imageEnums, String itemName){
        this.itemRarity = itemRarity;
        this.itemCategory = itemCategory;
        this.itemIcon = imageEnums;
        this.itemName = itemName;
    }

    public static ItemEnums getRandomItemByRarity (ItemRarityEnums category) {
        List<ItemEnums> matchingItems = new ArrayList<>();

        // Loop through all ItemEnums and add those that match the given category
        for (ItemEnums item : ItemEnums.values()) {
            if (!item.equals(ItemEnums.Locked) && item.getItemRarity() == category) {
                matchingItems.add(item);
            }
        }

        // If there are no items in the category, return null or throw an exception
        if (matchingItems.isEmpty()) {
            return getRandomItemByRarity(ItemRarityEnums.Common); // or throw new IllegalArgumentException("No items found for the given category");
        }

        // Select a random item from the list
        Random rand = new Random();
        return matchingItems.get(rand.nextInt(matchingItems.size()));
    }

    public ItemRarityEnums getItemRarity () {
        return this.itemRarity;
    }

    public ItemCategory getItemCategory () {
        return itemCategory;
    }

    public ImageEnums getItemIcon () {
        return itemIcon;
    }

    public String getItemName () {
        return itemName;
    }
}
