package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.items.enums.ItemClassAvailability;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemTypeEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ItemEnums {

    //Disabled/deprecated stuff
    Thornweaver(ItemRarityEnums.Relic, ImageEnums.ThornWeaver, "Thornweaver", false, ItemClassAvailability.All, ItemTypeEnums.Defensive),
    BarbedAegis(ItemRarityEnums.Rare, ImageEnums.BarbedAegis, "Barbed Aegis", false, ItemClassAvailability.All, ItemTypeEnums.Defensive),
    BarbedMissiles(ItemRarityEnums.Rare, ImageEnums.BarbedMissiles, "Barbed Missiles", false, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    ProtossThorns(ItemRarityEnums.Rare, ImageEnums.Test_Image, "Thorned Ships", false, ItemClassAvailability.Carrier, ItemTypeEnums.Defensive),
    Locked(ItemRarityEnums.Locked, ImageEnums.LockedIcon, "Locked", false, ItemClassAvailability.All, ItemTypeEnums.Utility),
    RepulsionArmorPlate(ItemRarityEnums.Common, ImageEnums.Starcraft2_Health_Upgrade_2, "Armor Plate", false, ItemClassAvailability.All, ItemTypeEnums.Defensive), //It's boring and never used
    MoneyPrinter(ItemRarityEnums.Rare, ImageEnums.MoneyPrinter, "Loot Box", false, ItemClassAvailability.All, ItemTypeEnums.Economy), //Completely fucks up the pacing & has broken implementation
    PyrrhicProtocol(ItemRarityEnums.Legendary, ImageEnums.PyrrhicProtocolIcon, "Pyrrhic Protocol", false, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive), //you dont want your beacon to die (unless, beacon build???)
    VengeanceProtocol(ItemRarityEnums.Rare, ImageEnums.VengeanceProtocolIcon, "Vengeance Protocol", false, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive),
    PuncturingPierce(ItemRarityEnums.Legendary, ImageEnums.PuncturingPierces, "Puncturing Pierce", false, ItemClassAvailability.Captain, ItemTypeEnums.Offensive), //disabled want saai
    Guillotine(ItemRarityEnums.Legendary, ImageEnums.Guillotine, "Guillotine", false, ItemClassAvailability.All, ItemTypeEnums.Offensive), //disabled want fundamenteel niet eens met de spirit van deze item, saaie item en een nachtmerrie om omheen te designen
    RocketLauncher(ItemRarityEnums.Rare, ImageEnums.Starcraft2_Dual_Rockets, "Rocket Launcher", false, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    ArmorPiercingRounds(ItemRarityEnums.Rare, ImageEnums.Starcraft2_Armor_Piercing, "Piercing Rounds", false, ItemClassAvailability.All, ItemTypeEnums.Offensive),
    KineticDynamo(ItemRarityEnums.Legendary, ImageEnums.KineticDynamoIcon, "Kinetic Dynamo", false, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive), //todo multiplayer update broke it and I never fixed it
    EnergySiphon(ItemRarityEnums.Rare, ImageEnums.Starcraft2_Energy_Siphon, "Energy Siphon", false, ItemClassAvailability.All, ItemTypeEnums.Defensive),

    //Shield package: remove this comment before pushing
    BombasticBarrier(ItemRarityEnums.Rare, ImageEnums.BombasticBarrier, "Bombastic Barrier", true, ItemClassAvailability.All, ItemTypeEnums.Offensive, ItemTypeEnums.Defensive),
    BarrierBooster(ItemRarityEnums.Common, ImageEnums.Starcraft2_BatteryUpgrade, "Barrier Booster", true, ItemClassAvailability.All, ItemTypeEnums.Defensive),
    ReflectiveShielding(ItemRarityEnums.Legendary, ImageEnums.ReflectiveShielding, "Barrier Backfire", true, ItemClassAvailability.All, ItemTypeEnums.Offensive, ItemTypeEnums.Defensive),
    CalmInChaos(ItemRarityEnums.Legendary, ImageEnums.CalmInChaos, "Blizzard Barrier", true, ItemClassAvailability.All, ItemTypeEnums.Offensive),

    //Relics 2.0
    //generic relics
    Placeholder(ItemRarityEnums.Relic, ImageEnums.SmallDroneItemIcon, "Placeholder", true, ItemClassAvailability.All, ItemTypeEnums.Offensive),
    BonusKaart(ItemRarityEnums.Relic, ImageEnums.BonusKaart, "Bonus Kaart", true, ItemClassAvailability.All, ItemTypeEnums.Utility),
    CashInfusion(ItemRarityEnums.Relic, ImageEnums.CashInfusion, "Cash Infusion", true, ItemClassAvailability.All, ItemTypeEnums.Economy),
    ShieldStabilizer(ItemRarityEnums.Relic, ImageEnums.ShieldStabilizer, "Shield Stabilizer", true, ItemClassAvailability.All, ItemTypeEnums.Defensive),
    GlassCannon(ItemRarityEnums.Relic, ImageEnums.GlassCannon, "Glass Cannon", true, ItemClassAvailability.All, ItemTypeEnums.Offensive),
    HelpRequested(ItemRarityEnums.Relic, ImageEnums.MoneyPrinter, "Help Requested", true, ItemClassAvailability.All, ItemTypeEnums.Utility),
    WisdomBall(ItemRarityEnums.Relic, ImageEnums.WondrousWisdomball, "Wondrous Wisdomball", true, ItemClassAvailability.All, ItemTypeEnums.Utility),
    Stuivie(ItemRarityEnums.Relic, ImageEnums.StuiversBestFriend, "Stuivie", true, ItemClassAvailability.All, ItemTypeEnums.Defensive),

    //captain relics
    ModuleAccuracy(ItemRarityEnums.Relic, ImageEnums.Starcraft2LockOn, "Module: Accuracy", true, ItemClassAvailability.CaptainFireFighter, ItemTypeEnums.Offensive),
    BouncingLasers(ItemRarityEnums.Relic, ImageEnums.Starcraft2BouncingLaser, "Bouncing Lasers", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    ModuleElectrify(ItemRarityEnums.Relic, ImageEnums.ModuleElectrify, "Module: Electric Razor", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    ModuleCommand(ItemRarityEnums.Relic, ImageEnums.ModuleCommand, "Module: Synergize", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive, ItemTypeEnums.Utility),
    UpgradeFocusFire(ItemRarityEnums.Relic, ImageEnums.ModuleFocusFire, "Upgrade: Focus Fire", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    AnionInverter(ItemRarityEnums.Relic, ImageEnums.AnionInverter, "Anion Inverter", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive, ItemTypeEnums.Utility),
    BigIron(ItemRarityEnums.Relic, ImageEnums.BigIron, "Big Iron", false, ItemClassAvailability.Captain, ItemTypeEnums.Offensive), //disabled want dit voelt buggy aan en ook niet echt fijn om mee te spelen
    OneShotOneKill(ItemRarityEnums.Relic, ImageEnums.OneShotOneKill, "One shot, one kill", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    ElectricDestabilizer(ItemRarityEnums.Relic, ImageEnums.Starcraft2_Protoss_Shield_Disintegrate, "Electric Destabilizer", true, ItemClassAvailability.Captain, ItemTypeEnums.Defensive),
    SideCannons(ItemRarityEnums.Relic, ImageEnums.SideCannons, "Side Cannons", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),

    //firefighter relics
    ModuleScorch(ItemRarityEnums.Relic, ImageEnums.ModuleScorchIcon, "Module: Scorch", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    BeckoningFlames(ItemRarityEnums.Relic, ImageEnums.EntanglingFlames, "Beckoning Flames", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    RingOfFire(ItemRarityEnums.Relic, ImageEnums.RingOfFire, "Ring of Fire", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    FieryImplosion(ItemRarityEnums.Relic, ImageEnums.FieryImplosion, "Fiery Implosion", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    FireWithoutGasIsAss(ItemRarityEnums.Relic, ImageEnums.WithoutGasItsAss, "Without gas it's ass", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),

    //carrier relics
    ArbiterDamage(ItemRarityEnums.Relic, ImageEnums.ArbiterDamage, "Arbit Inversion", true, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive),
    PulsingBeacon(ItemRarityEnums.Relic, ImageEnums.PulsingBeacon, "Pulsing Beacon", true, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive),
    InverseRetrieval(ItemRarityEnums.Relic, ImageEnums.InversionRetrieval, "Inverse Retrieval", true, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive, ItemTypeEnums.Utility),


    //Mutalisk relics
    Consume(ItemRarityEnums.Relic, ImageEnums.Consume, "Consume", false, ItemClassAvailability.Mutalisk, ItemTypeEnums.Defensive),
    ThickHide(ItemRarityEnums.Common, ImageEnums.Test_Image, "Thick Hide", false, ItemClassAvailability.Mutalisk, ItemTypeEnums.Defensive),
    InstantViralEruption(ItemRarityEnums.Relic, ImageEnums.Test_Image, "InstantViralEruption", false, ItemClassAvailability.Mutalisk, ItemTypeEnums.Offensive),
    BileTravelRange(ItemRarityEnums.Relic, ImageEnums.Test_Image, "BileTravelTRange", false, ItemClassAvailability.Mutalisk, ItemTypeEnums.Utility),
    MutaliskSideCannons(ItemRarityEnums.Relic, ImageEnums.Test_Image, "MutaliskSideCannons", false, ItemClassAvailability.Mutalisk, ItemTypeEnums.Offensive),

    //Mutalisk items
    MutaliskHealingBonus(ItemRarityEnums.Common, ImageEnums.Test_Image, "MutaliskHealingBonus", false, ItemClassAvailability.Mutalisk, ItemTypeEnums.Defensive),
    MutaliskAttackSpeed(ItemRarityEnums.Common, ImageEnums.Test_Image, "MutaliskAttackSpeed", false, ItemClassAvailability.Mutalisk, ItemTypeEnums.Offensive),
    PoisonedNeedles(ItemRarityEnums.Common, ImageEnums.Test_Image, "Poison Needles", false, ItemClassAvailability.Mutalisk, ItemTypeEnums.Offensive),
    FragmentationSacs(ItemRarityEnums.Common, ImageEnums.Test_Image, "Fragmentation Sacs", false, ItemClassAvailability.Mutalisk, ItemTypeEnums.Offensive),


    //-----------------------------
    LeechingLasers(ItemRarityEnums.Rare, ImageEnums.LeechingLasers, "Leeching Lasers", false, ItemClassAvailability.Captain, ItemTypeEnums.Defensive),
    ElectroShredding(ItemRarityEnums.Legendary, ImageEnums.Electroshedding, "Electro Shredding", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    PlasmaCoatedBullets(ItemRarityEnums.Common, ImageEnums.Starcraft2_Blue_Flame, "Plasma Bullets", true, ItemClassAvailability.CaptainCarrier, ItemTypeEnums.Offensive),
    Recycler(ItemRarityEnums.Rare, ImageEnums.Recycler, "Recycler", true, ItemClassAvailability.Captain, ItemTypeEnums.Utility),
    PhotonPiercer(ItemRarityEnums.Common, ImageEnums.Starcraft2_Concussive_Shells, "Photon Piercer", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    CannisterOfGasoline(ItemRarityEnums.Rare, ImageEnums.CannisterOfGasoline, "Gasoline", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    RegenerativeSteel(ItemRarityEnums.Common, ImageEnums.Starcraft2_Heal, "Regenerative Steel", true, ItemClassAvailability.All, ItemTypeEnums.Defensive),
    Battery(ItemRarityEnums.Rare, ImageEnums.Starcraft2_Battery, "Battery", true, ItemClassAvailability.CaptainFireFighter, ItemTypeEnums.Utility),
    FocusCrystal(ItemRarityEnums.Common, ImageEnums.Starcraft2Keystone, "Focus Crystal", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    AdvancedOptics(ItemRarityEnums.Common, ImageEnums.Starcraft2_Advanced_Optics, "Advanced Optics", true, ItemClassAvailability.All, ItemTypeEnums.Offensive),
    PlatinumSponge(ItemRarityEnums.Rare, ImageEnums.Starcraft2_Platinum_Sponge, "Platinum Sponge", true, ItemClassAvailability.All, ItemTypeEnums.Defensive),
    EmergencyRepairBot(ItemRarityEnums.Common, ImageEnums.Starcraft2_Vespene_Drone, "Repair Bot", true, ItemClassAvailability.All, ItemTypeEnums.Defensive),
    Overclock(ItemRarityEnums.Rare, ImageEnums.Starcraft2_Overclock, "Overclock", true, ItemClassAvailability.CaptainFireFighter, ItemTypeEnums.Offensive),
    PlasmaLauncher(ItemRarityEnums.Rare, ImageEnums.Starcraft2_Focused_Crystal, "Plasma Launcher", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    GuardianDrone(ItemRarityEnums.Common, ImageEnums.Starcraft2_Seeker_Missile, "Guardian Drone", true, ItemClassAvailability.CaptainFireFighter, ItemTypeEnums.Offensive),
    UpgradeOverload(ItemRarityEnums.Rare, ImageEnums.Starcraft2_Auto_Tracking, "Upgrade: Overload", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    UpgradePrecision(ItemRarityEnums.Legendary, ImageEnums.UpgradePrecision, "Upgrade: Precision", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    UpgradeSpeed(ItemRarityEnums.Rare, ImageEnums.UpgradeSpeed, "Upgrade: Speed", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    PiercingMissiles(ItemRarityEnums.Legendary, ImageEnums.PiercingLaser, "Piercing Lasers", true, ItemClassAvailability.CaptainCarrier, ItemTypeEnums.Offensive),
    VIPTicket(ItemRarityEnums.Legendary, ImageEnums.VIPTicket, "VIP Ticket", true, ItemClassAvailability.All, ItemTypeEnums.Economy),
    ElectricSupercharger(ItemRarityEnums.Legendary, ImageEnums.Starcraft2_Psi_Storm2, "Electric Supercharger", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    Adrenaline(ItemRarityEnums.Legendary, ImageEnums.Adrenaline, "Adrenaline", true, ItemClassAvailability.CaptainFireFighter, ItemTypeEnums.Offensive),
    ExplosiveLaserbeams(ItemRarityEnums.Legendary, ImageEnums.ExplosiveLaserbeams, "Explosive Laserbeams", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    ExplosiveGreed(ItemRarityEnums.Legendary, ImageEnums.ExplosiveGreed, "Explosive Greed", true, ItemClassAvailability.All, ItemTypeEnums.Offensive),
    Contract(ItemRarityEnums.Common, ImageEnums.Contract, "Contract", true, ItemClassAvailability.All, ItemTypeEnums.Utility),
    StickyOil(ItemRarityEnums.Rare, ImageEnums.StickyOilIcon, "Sticky Oil", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    CorrosiveOil(ItemRarityEnums.Legendary, ImageEnums.CorrosiveOil, "Corrosive Oil", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    ScorchingFury(ItemRarityEnums.Common, ImageEnums.Starcraft2RepairBeam, "Scorching Fury", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    FlameDetonation(ItemRarityEnums.Legendary, ImageEnums.Starcraft2_MineExplosion, "Flame Detonation", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    EscalatingFlames(ItemRarityEnums.Common, ImageEnums.EscalatingFlames, "Escalating Flames", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    FuelCannister(ItemRarityEnums.Legendary, ImageEnums.FuelCannister, "Fuel Cannister", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Utility),
    BargainBucket(ItemRarityEnums.Legendary, ImageEnums.BargainBucket, "Bargain Bucket", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    ProtossScout(ItemRarityEnums.Common, ImageEnums.ScoutItem, "Hangar Bay: Scout", true, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive),
    ProtossArbiter(ItemRarityEnums.Rare, ImageEnums.ArbiterItem, "Hangar Bay: Arbiter", true, ItemClassAvailability.Carrier, ItemTypeEnums.Defensive),
    ProtossShuttle(ItemRarityEnums.Common, ImageEnums.ShuttleItem, "Hangar Bay: Shuttle", true, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive),
    ProtossCorsair(ItemRarityEnums.Rare, ImageEnums.CorsairItemIcon, "Hangar Bay: Corsair", true, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive),
    HangarBayUpgrade(ItemRarityEnums.Legendary, ImageEnums.HangarBayIcon, "Hangar Bay Upgrade", true, ItemClassAvailability.Carrier, ItemTypeEnums.Utility),
    RallyTheFleet(ItemRarityEnums.Rare, ImageEnums.RallyTheFleetIcon, "Rally the Fleet", true, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive),
    Martyrdom(ItemRarityEnums.Legendary, ImageEnums.FrenzyIcon, "Martyrdom", true, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive),
    ArbiterMultiTargeting(ItemRarityEnums.Rare, ImageEnums.ArbiterMultiTargetIcon, "Arbiter Multi Targeting", true, ItemClassAvailability.Carrier, ItemTypeEnums.Defensive),
    ConstructionKit(ItemRarityEnums.Legendary, ImageEnums.ConstructionKitIcon, "Construction Kit", true, ItemClassAvailability.Carrier, ItemTypeEnums.Utility),
    SynergeticLink(ItemRarityEnums.Legendary, ImageEnums.SynergeticLinkIcon, "Synergetic Link", false, ItemClassAvailability.Carrier, ItemTypeEnums.Utility),
    EmergencyRepairs(ItemRarityEnums.Rare, ImageEnums.EmergencyRepairsIcon, "Emergency Repairs", true, ItemClassAvailability.Carrier, ItemTypeEnums.Utility),
    StickyDynamite(ItemRarityEnums.Common, ImageEnums.StickyDynamite, "Sticky Dynamite", true, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    EternaFlame(ItemRarityEnums.Legendary, ImageEnums.Eternaflame, "EternaBurn", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Utility),
    EphemeralBlaze(ItemRarityEnums.Rare, ImageEnums.EphemeralBlaze, "Ephemeral Blaze", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    AimAssist(ItemRarityEnums.Legendary, ImageEnums.AimAssist, "Aim Assist", true, ItemClassAvailability.Carrier, ItemTypeEnums.Offensive),
    HighVelocityLasers(ItemRarityEnums.Rare, ImageEnums.HighVelocityLasers, "High Velocity Lasers", false, ItemClassAvailability.Captain, ItemTypeEnums.Offensive),
    InfernalPreIgniter(ItemRarityEnums.Legendary, ImageEnums.InfernalPreIgniter, "Infernal Pre-igniter", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive),
    DistributedHeat(ItemRarityEnums.Relic, ImageEnums.DistributedHeatIcon, "Distributed Heat", true, ItemClassAvailability.FireFighter, ItemTypeEnums.Offensive);

    private ItemRarityEnums itemRarity;
    private ImageEnums itemIcon;
    private String itemName;
    private boolean enabled;
    private ItemClassAvailability classAvailability;
    private List<ItemTypeEnums> itemTypes;

    ItemEnums(ItemRarityEnums itemRarity, ImageEnums imageEnums, String itemName, boolean enabled, ItemClassAvailability classAvailability, ItemTypeEnums firstType, ItemTypeEnums... otherTypes) {
        this.itemRarity = itemRarity;
        this.itemIcon = imageEnums;
        this.itemName = itemName;
        this.enabled = enabled;
        this.classAvailability = classAvailability;
        List<ItemTypeEnums> types = new ArrayList<>();
        types.add(firstType);
        types.addAll(List.of(otherTypes));
        this.itemTypes = List.copyOf(types);
    }

    public static ItemEnums getRandomItemByRarity(ItemRarityEnums category) {
        List<ItemEnums> matchingItems = new ArrayList<>();
        if(category.equals(ItemRarityEnums.Locked)) {
            return ItemEnums.Locked;
        }

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

    public ItemClassAvailability getClassAvailability() {
        return classAvailability;
    }

    public List<ItemTypeEnums> getItemTypes() {
        return itemTypes;
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
