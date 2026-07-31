package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ItemEnums {

    //Disabled/deprecated stuff
    ReflectiveShielding(ItemRarityEnums.Common, ImageEnums.Thornedplates, "Reflective Shielding", false),
    Thornweaver(ItemRarityEnums.Relic, ImageEnums.ThornWeaver, "Thornweaver", false),
    BarbedAegis(ItemRarityEnums.Rare, ImageEnums.BarbedAegis, "Barbed Aegis", false),
    BarbedMissiles(ItemRarityEnums.Rare, ImageEnums.BarbedMissiles, "Barbed Missiles", false),
    BarrierSuperSizer(ItemRarityEnums.Common, ImageEnums.Starcraft2_BatteryUpgrade, "Barrier Booster", false), //It's a boring item
    ProtossThorns(ItemRarityEnums.Rare, ImageEnums.Test_Image, "Thorned Ships", false),
    Locked(ItemRarityEnums.Locked, ImageEnums.LockedIcon, "Locked", false),
    RepulsionArmorPlate(ItemRarityEnums.Common, ImageEnums.Starcraft2_Health_Upgrade_2, "Armor Plate", false), //It's boring and never used
    MoneyPrinter(ItemRarityEnums.Rare, ImageEnums.MoneyPrinter, "Loot Box", false), //Completely fucks up the pacing & has broken implementation
    PyrrhicProtocol(ItemRarityEnums.Legendary, ImageEnums.PyrrhicProtocolIcon, "Pyrrhic Protocol", false),
    VengeanceProtocol(ItemRarityEnums.Rare, ImageEnums.VengeanceProtocolIcon, "Vengeance Protocol", false),
    EnergySiphon(ItemRarityEnums.Common, ImageEnums.Starcraft2_Energy_Siphon, "Energy Siphon", false),
    ModulePower(ItemRarityEnums.Legendary,  ImageEnums.Test_Image, "Module: Power", false), //disabled omdat drones al genoeg snowballen, dit zou extra op extra zijn
    PuncturingPierce(ItemRarityEnums.Legendary,  ImageEnums.PuncturingPierces, "Puncturing Pierce", false), //disabled want saai
    //Relics 2.0
    //generic relics
    Placeholder(ItemRarityEnums.Relic, ImageEnums.SmallDroneItemIcon, "Placeholder", true),
    BonusKaart(ItemRarityEnums.Relic, ImageEnums.BonusKaart, "Bonus Kaart", true),
    GreedIsGood(ItemRarityEnums.Relic, ImageEnums.Test_Image, "Cash Infusion", true),
    ShieldStabilizer(ItemRarityEnums.Relic,  ImageEnums.ShieldStabilizer, "Shield Stabilizer", true),
    GlassCannon(ItemRarityEnums.Relic,  ImageEnums.GlassCannon, "Glass Cannon", true),
    HelpRequested(ItemRarityEnums.Relic, ImageEnums.MoneyPrinter, "Help Requested", true),
    WisdomBall(ItemRarityEnums.Relic, ImageEnums.WondrousWisdomball, "Wondrous Wisdomball", true),
    Stuivie(ItemRarityEnums.Relic, ImageEnums.StuiversBestFriend, "Stuivie", true),

    //captain relics
    ModuleAccuracy(ItemRarityEnums.Relic, ImageEnums.Starcraft2LockOn, "Module: Accuracy", true),
    BouncingLasers(ItemRarityEnums.Relic,  ImageEnums.Starcraft2BouncingLaser, "Bouncing Lasers", true),
    ModuleElectrify(ItemRarityEnums.Relic,  ImageEnums.ModuleElectrify, "Module: Electric Razor", true),
    ModuleCommand(ItemRarityEnums.Relic,  ImageEnums.ModuleCommand, "Module: Synergize", true),
    ModuleFocusFire(ItemRarityEnums.Relic,  ImageEnums.ModuleFocusFire, "Module: Focus Fire", true),
    AnionInverter(ItemRarityEnums.Relic, ImageEnums.AnionInverter, "Anion Inverter", true),
    BigIron(ItemRarityEnums.Relic,  ImageEnums.BigIron, "Big Iron", false), //disabled want dit voelt buggy aan en ook niet echt fijn om mee te spelen
    OneShotOneKill(ItemRarityEnums.Relic, ImageEnums.OneShotOneKill, "One shot, one kill", true),
    ElectricDestabilizer(ItemRarityEnums.Relic, ImageEnums.Starcraft2_Protoss_Shield_Disintegrate, "Electric Destabilizer", true),

    //firefighter relics
    ModuleScorch(ItemRarityEnums.Relic,  ImageEnums.ModuleScorchIcon, "Module: Scorch", true),
    BeckoningFlames(ItemRarityEnums.Relic,  ImageEnums.EntanglingFlames, "Beckoning Flames", true),
    RingOfFire(ItemRarityEnums.Relic, ImageEnums.Test_Image, "Ring of Fire", true),
    FieryImplosion(ItemRarityEnums.Relic, ImageEnums.Test_Image, "Fiery Implosion", true),
    FireWithoutGasIsAss(ItemRarityEnums.Relic, ImageEnums.Test_Image, "Without gas it's ass", true),

    //carrier relics
    ArbiterDamage(ItemRarityEnums.Relic,  ImageEnums.ArbiterDamage, "Arbit Inversion", true),
    PulsingBeacon(ItemRarityEnums.Relic, ImageEnums.Test_Image, "Pulsing Beacon", true),
    InverseRetrieval(ItemRarityEnums.Relic,  ImageEnums.InversionRetrieval, "Inverse Retrieval", true),

    //-----------------------------
    ArmorPiercingRounds(ItemRarityEnums.Rare, ImageEnums.Starcraft2_Armor_Piercing, "Piercing Rounds", true),
    LeechingLasers(ItemRarityEnums.Rare,  ImageEnums.LeechingLasers, "Leeching Lasers", false),
    CalmInChaos(ItemRarityEnums.Legendary,  ImageEnums.CalmInChaos, "Calm in chaos", true),
    ElectroShedding(ItemRarityEnums.Legendary,  ImageEnums.Electroshedding, "Electro Shedding", true),
    PlasmaCoatedBullets(ItemRarityEnums.Common, ImageEnums.Starcraft2_Blue_Flame, "Plasma Bullets", true),

    PhotonPiercer(ItemRarityEnums.Common,  ImageEnums.Starcraft2_Concussive_Shells, "Photon Piercer", true),
    CannisterOfGasoline(ItemRarityEnums.Rare,  ImageEnums.CannisterOfGasoline, "Gasoline", true),
    SelfRepairingSteel(ItemRarityEnums.Common,  ImageEnums.Starcraft2_Heal, "Regenerative Steel", true),
    Battery(ItemRarityEnums.Rare,  ImageEnums.Starcraft2_Battery, "Battery", true),
    FocusCrystal(ItemRarityEnums.Common,  ImageEnums.Starcraft2Keystone, "Focus Crystal", true),
    PrecisionAmplifier(ItemRarityEnums.Common,  ImageEnums.Starcraft2_Advanced_Optics, "Advanced Optics", true),
    PlatinumSponge(ItemRarityEnums.Rare,  ImageEnums.Starcraft2_Platinum_Sponge, "Platinum Sponge", true),
    EmergencyRepairBot(ItemRarityEnums.Common,  ImageEnums.Starcraft2_Vespene_Drone, "Repair Bot", true),
    Overclock(ItemRarityEnums.Common,  ImageEnums.Starcraft2_Overclock, "Overclock", true),
    PlasmaLauncher(ItemRarityEnums.Rare,  ImageEnums.Starcraft2_Focused_Crystal, "Plasma Launcher", true),
    GuardianDrone(ItemRarityEnums.Common,  ImageEnums.Starcraft2_Seeker_Missile, "Guardian Drone", true),
    CriticalOverloadCapacitor(ItemRarityEnums.Rare,  ImageEnums.Starcraft2_Auto_Tracking, "Precision Overloader", true),
    PiercingMissiles(ItemRarityEnums.Legendary,  ImageEnums.PiercingLaser, "Piercing Lasers", true),
    VIPTicket(ItemRarityEnums.Legendary,  ImageEnums.VIPTicket, "VIP Ticket", true),
    ElectricSupercharger(ItemRarityEnums.Legendary,  ImageEnums.Starcraft2_Psi_Storm2, "Electric Supercharger", true),
    Adrenaline(ItemRarityEnums.Legendary,  ImageEnums.Adrenaline, "Adrenaline", true),
    ExplosiveLaserbeams(ItemRarityEnums.Legendary,  ImageEnums.ExplosiveLaserbeams, "Explosive Laserbeams", true),
    ExplosiveGreed(ItemRarityEnums.Legendary,  ImageEnums.ExplosiveGreed, "Explosive Greed", true),
    Guillotine(ItemRarityEnums.Legendary,  ImageEnums.Guillotine, "Guillotine", true),
    Contract(ItemRarityEnums.Common,  ImageEnums.Contract, "Contract", true),
    StickyOil(ItemRarityEnums.Rare,  ImageEnums.StickyOilIcon, "Sticky Oil", true),
    CorrosiveOil(ItemRarityEnums.Legendary,  ImageEnums.CorrosiveOil, "Corrosive Oil", true),
    ScorchingFury(ItemRarityEnums.Common,  ImageEnums.Starcraft2RepairBeam, "Scorching Fury", true),
    FlameDetonation(ItemRarityEnums.Legendary,  ImageEnums.Starcraft2_MineExplosion, "Flame Detonation", true),
    EscalatingFlames(ItemRarityEnums.Common,  ImageEnums.EscalatingFlames, "Escalating Flames", true),
    FuelCannister(ItemRarityEnums.Rare,  ImageEnums.FuelCannister, "Fuel Cannister", true),
    BargainBucket(ItemRarityEnums.Legendary,  ImageEnums.BargainBucket, "Bargain Bucket", true),
    ProtossScout(ItemRarityEnums.Common,  ImageEnums.ScoutItem, "Hangar Bay: Scout", true),
    ProtossArbiter(ItemRarityEnums.Rare,  ImageEnums.ArbiterItem, "Hangar Bay: Arbiter", true),
    ProtossShuttle(ItemRarityEnums.Common,  ImageEnums.ShuttleItem, "Hangar Bay: Shuttle", true),
    ProtossCorsair(ItemRarityEnums.Rare,  ImageEnums.CorsairItemIcon, "Hangar Bay: Corsair", true),
    HangarBayUpgrade(ItemRarityEnums.Legendary,  ImageEnums.HangarBayIcon, "Hangar Bay Upgrade", true),
    RallyTheFleet(ItemRarityEnums.Rare,  ImageEnums.RallyTheFleetIcon, "Rally the Fleet", true),
    Martyrdom(ItemRarityEnums.Legendary,  ImageEnums.FrenzyIcon, "Martyrdom", true),
    KineticDynamo(ItemRarityEnums.Legendary,  ImageEnums.KineticDynamoIcon, "Kinetic Dynamo", false),
    ArbiterMultiTargeting(ItemRarityEnums.Rare,  ImageEnums.ArbiterMultiTargetIcon, "Arbiter Multi Targeting", true),
    ConstructionKit(ItemRarityEnums.Legendary,  ImageEnums.ConstructionKitIcon, "Construction Kit", true),
    SynergeticLink(ItemRarityEnums.Legendary,  ImageEnums.SynergeticLinkIcon, "Synergetic Link", false),
    EmergencyRepairs(ItemRarityEnums.Rare,  ImageEnums.EmergencyRepairsIcon, "Emergency Repairs", true),
    StickyDynamite(ItemRarityEnums.Common,  ImageEnums.StickyDynamite, "Sticky Dynamite", true),
    EternaFlame(ItemRarityEnums.Legendary,  ImageEnums.Eternaflame, "EternaBurn", true),
    EphemeralBlaze(ItemRarityEnums.Rare,  ImageEnums.EphemeralBlaze, "Ephemeral Blaze", true),
    AimAssist(ItemRarityEnums.Legendary,  ImageEnums.AimAssist, "Aim Assist", true),
    HighVelocityLasers(ItemRarityEnums.Rare,  ImageEnums.HighVelocityLasers, "High Velocity Lasers", false),
    InfernalPreIgniter(ItemRarityEnums.Legendary,  ImageEnums.InfernalPreIgniter, "Infernal Pre-igniter", true);

    private ItemRarityEnums itemRarity;
    private ImageEnums itemIcon;
    private String itemName;
    private boolean enabled;

    ItemEnums(ItemRarityEnums itemRarity, ImageEnums imageEnums, String itemName, boolean enabled) {
        this.itemRarity = itemRarity;
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


    public ImageEnums getItemIcon() {
        return itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static boolean isRelicAvailable() {
        // Check every relic item
        for (ItemEnums item : ItemEnums.values()) {
            if (item.getItemRarity() == ItemRarityEnums.Relic) {
                // If this relic is not in the player's inventory,
                // then there is still a relic available to obtain.
                if (PlayerInventory.getInstance().getItemFromInventoryIfExists(item) == null) {
                    return true;
                }
            }
        }

        // All relics are already in the inventory.
        return false;
    }
}
