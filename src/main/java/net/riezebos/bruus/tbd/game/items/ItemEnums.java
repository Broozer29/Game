package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.items.enums.ItemCategory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ItemEnums {

    //Disabled/deprecated stuff
    ReflectiveShielding(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Thornedplates, "Reflective Shielding", false),
    Thornweaver(ItemRarityEnums.Relic, ItemCategory.Offense, ImageEnums.ThornWeaver, "Thornweaver", false),
    BarbedAegis(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.BarbedAegis, "Barbed Aegis", false),
    BarbedMissiles(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.BarbedMissiles, "Barbed Missiles", false),
    BarrierSuperSizer(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_BatteryUpgrade, "Barrier Booster", false), //It's a boring item
    ProtossThorns(ItemRarityEnums.Rare, ItemCategory.Defense, ImageEnums.Test_Image, "Thorned Ships", false),
    Locked(ItemRarityEnums.Locked, ItemCategory.Utility, ImageEnums.LockedIcon, "Locked", false),
    RepulsionArmorPlate(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Health_Upgrade_2, "Armor Plate", false), //It's boring and never used
    ArmorPiercingRounds(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Armor_Piercing, "Piercing Rounds", false), //It's boring and deprecated
    MoneyPrinter(ItemRarityEnums.Rare, ItemCategory.Utility, ImageEnums.MoneyPrinter, "Loot Box", false), //Completely fucks up the pacing & has broken implementation
    PyrrhicProtocol(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.PyrrhicProtocolIcon, "Pyrrhic Protocol", false),
    VengeanceProtocol(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.VengeanceProtocolIcon, "Vengeance Protocol", false),
    EnergySiphon(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Energy_Siphon, "Energy Siphon", false),
    //-----------------------------


    BeckoningFlames(ItemRarityEnums.Relic, ItemCategory.Offense, ImageEnums.EntanglingFlames, "Beckoning Flames", true),
    PlasmaCoatedBullets(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Blue_Flame, "Plasma Bullets", true),
    PhotonPiercer(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Concussive_Shells, "Photon Piercer", true),
    CannisterOfGasoline(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.CannisterOfGasoline, "Gasoline", true),
    SelfRepairingSteel(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Heal, "Regenerative Steel", true),
    Battery(ItemRarityEnums.Common, ItemCategory.Utility, ImageEnums.Starcraft2_Battery, "Battery", true),
    FocusCrystal(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2Keystone, "Focus Crystal", true),
    PrecisionAmplifier(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Advanced_Optics, "Advanced Optics", true),
    PlatinumSponge(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Platinum_Sponge, "Platinum Sponge", true),
    EmergencyRepairBot(ItemRarityEnums.Common, ItemCategory.Defense, ImageEnums.Starcraft2_Vespene_Drone, "Repair Bot", true),
    Overclock(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2_Overclock, "Overclock", true),
    PlasmaLauncher(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.Starcraft2_Focused_Crystal, "Plasma Launcher", true),
    GuardianDrone(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.Starcraft2_Seeker_Missile, "Guardian Drone", true),
    CriticalOverloadCapacitor(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2_Auto_Tracking, "Precision Overloader", true),
    PiercingMissiles(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.PiercingLaser, "Piercing Lasers", true),
    BouncingModuleAddon(ItemRarityEnums.Relic, ItemCategory.Offense, ImageEnums.Starcraft2BouncingLaser, "Bouncing Lasers", true),
    VIPTicket(ItemRarityEnums.Legendary, ItemCategory.Utility, ImageEnums.VIPTicket, "VIP Ticket", true),
    ElectricDestabilizer(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2_Protoss_Shield_Disintegrate, "Electric Destabilizer", true),
    ModuleAccuracy(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2LockOn, "Module: Accuracy", true),
    ElectricSupercharger(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2_Psi_Storm2, "Electric Supercharger", true),
    ModuleElectrify(ItemRarityEnums.Relic, ItemCategory.Offense, ImageEnums.ModuleElectrify, "Module: Electric Razor", true),
    ModuleCommand(ItemRarityEnums.Relic, ItemCategory.Offense, ImageEnums.ModuleCommand, "Module: Synergize", true),
    Contract(ItemRarityEnums.Common, ItemCategory.Utility, ImageEnums.Contract, "Contract", true),
    StickyOil(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.StickyOilIcon, "Sticky Oil", true),
    CorrosiveOil(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.CorrosiveOil, "Corrosive Oil", true),
    ScorchingFury(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Starcraft2RepairBeam, "Scorching Fury", true),
    FlameDetonation(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Starcraft2_MineExplosion, "Flame Detonation", true),
    EscalatingFlames(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.EscalatingFlames, "Escalating Flames", true),
    FuelCannister(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.FuelCannister, "Fuel Cannister", true),
    BargainBucket(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.BargainBucket, "Bargain Bucket", true),
    ModuleScorch(ItemRarityEnums.Relic, ItemCategory.Offense, ImageEnums.ModuleScorchIcon, "Module: Scorch", true),
    ShieldStabilizer(ItemRarityEnums.Relic, ItemCategory.Defense, ImageEnums.ShieldStabilizer, "Shield Stabilizer", true),
    ProtossScout(ItemRarityEnums.Common, ItemCategory.Utility, ImageEnums.ScoutItem, "Hangar Bay: Scout", true),
    ProtossArbiter(ItemRarityEnums.Rare, ItemCategory.Utility, ImageEnums.ArbiterItem, "Hangar Bay: Arbiter", true),
    ProtossShuttle(ItemRarityEnums.Common, ItemCategory.Utility, ImageEnums.ShuttleItem, "Hangar Bay: Shuttle", true),
    ProtossCorsair(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.CorsairItemIcon, "Hangar Bay: Corsair", true),
    HangarBayUpgrade(ItemRarityEnums.Legendary, ItemCategory.Utility, ImageEnums.HangarBayIcon, "Hangar Bay Upgrade", true),
    RallyTheFleet(ItemRarityEnums.Rare, ItemCategory.Utility, ImageEnums.RallyTheFleetIcon, "Rally the Fleet", true),
    InverseRetrieval(ItemRarityEnums.Legendary, ItemCategory.Utility, ImageEnums.InversionRetrieval, "Inverse Retrieval", true),
    Martyrdom(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.FrenzyIcon, "Martyrdom", true),
    KineticDynamo(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.KineticDynamoIcon, "Kinetic Dynamo", true),
    ArbiterMultiTargeting(ItemRarityEnums.Rare, ItemCategory.Utility, ImageEnums.ArbiterMultiTargetIcon, "Arbiter Multi Targeting", true),
    ConstructionKit(ItemRarityEnums.Legendary, ItemCategory.Utility, ImageEnums.ConstructionKitIcon, "Construction Kit", true),
    SynergeticLink(ItemRarityEnums.Legendary, ItemCategory.Utility, ImageEnums.SynergeticLinkIcon, "Synergetic Link", true),
    EmergencyRepairs(ItemRarityEnums.Rare, ItemCategory.Utility, ImageEnums.EmergencyRepairsIcon, "Emergency Repairs", true),
    ArbiterDamage(ItemRarityEnums.Relic, ItemCategory.Offense, ImageEnums.ArbiterDamage, "Arbit Inversion", true),
    StickyDynamite(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.StickyDynamite, "Sticky Dynamite", true),
    EternaFlame(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.Eternaflame, "EternaBurn", true),
    EphemeralBlaze(ItemRarityEnums.Rare, ItemCategory.Offense, ImageEnums.EphemeralBlaze, "Ephemeral Blaze", true),
    Stuivie(ItemRarityEnums.Relic, ItemCategory.Defense, ImageEnums.StuiversBestFriend, "Stuivie", true),
    GlassCannon(ItemRarityEnums.Relic, ItemCategory.Offense, ImageEnums.GlassCannon, "Glass Cannon", true),
    AimAssist(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.AimAssist, "Aim Assist", true),
    HighVelocityLasers(ItemRarityEnums.Common, ItemCategory.Offense, ImageEnums.Test_Image, "High Velocity Lasers", true),
    InfernalPreIgniter(ItemRarityEnums.Legendary, ItemCategory.Offense, ImageEnums.InfernalPreIgniter, "Infernal Pre-igniter", true);

    private ItemRarityEnums itemRarity;
    private ItemCategory itemCategory;
    private ImageEnums itemIcon;
    private String itemName;
    private boolean enabled;

    ItemEnums(ItemRarityEnums itemRarity, ItemCategory itemCategory, ImageEnums imageEnums, String itemName, boolean enabled) {
        this.itemRarity = itemRarity;
        this.itemCategory = itemCategory;
        this.itemIcon = imageEnums;
        this.itemName = itemName;
        this.enabled = enabled;
    }

    public static ItemEnums getRandomItemByRarity(ItemRarityEnums category) {
        List<ItemEnums> matchingItems = new ArrayList<>();

        // Loop through all ItemEnums and add those that match the given category
        for (ItemEnums item : ItemEnums.values()) {
            if (!item.equals(ItemEnums.Locked) && item.getItemRarity() == category) {
                matchingItems.add(item);
            }
        }

        // If there are no items in the category, return null or throw an exception
        if (matchingItems.isEmpty()) {
            return getRandomItemByRarity(ItemRarityEnums.Common);
        }

        // Select a random item from the list
        Random rand = new Random();
        return matchingItems.get(rand.nextInt(matchingItems.size()));
    }

    public ItemRarityEnums getItemRarity() {
        return this.itemRarity;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public ImageEnums getItemIcon() {
        return itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
