package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.items.*;
import net.riezebos.bruus.tbd.game.items.items.captain.*;
import net.riezebos.bruus.tbd.game.items.items.carrier.*;
import net.riezebos.bruus.tbd.game.items.items.deprecated.ArmorPiercingRounds;
import net.riezebos.bruus.tbd.game.items.items.deprecated.MoneyPrinter;
import net.riezebos.bruus.tbd.game.items.items.deprecated.RepulsionArmorPlate;
import net.riezebos.bruus.tbd.game.items.items.firefighter.*;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.boardcreators.AchievementUnlockHelper;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerInventory {
    private static PlayerInventory instance = new PlayerInventory();
    private Map<ItemEnums, Item> items = new HashMap<>();
    private float cashMoney = 0;

    private PlayerInventory() {
        resetInventory();
    }


    public void resetInventory() {
        items.clear();

        if(DevTestSettings.infiniteMoney){
            cashMoney = 999999999;
        } else {
            cashMoney = 0;
        }
    }

    public static PlayerInventory getInstance() {
        return instance;
    }


    public void addItem(ItemEnums itemEnum) {
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
        if (item != null) {
            activateUponPurchaseItemEffects(item);
        }

        checkClubAccessUnlockCondition();
        checkTreasureHunterUnlockCondition();
    }

    private void checkClubAccessUnlockCondition() {
        Item ticket = getItemFromInventoryIfExists(ItemEnums.VIPTicket);
        if (ticket != null && ticket.getQuantity() >= 5 && PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel() <= 0) {
            PlayerProfileManager.getInstance().getLoadedProfile().setClubAccessLevel(1);
            PlayerProfileManager.getInstance().exportCurrentProfile();
            BoardManager.getInstance().getShopBoard().addGUIAnimation(AchievementUnlockHelper.createUnlockGUIComponent(BoonEnums.CLUB_ACCESS.getUnlockImage()));
            AudioManager.getInstance().addAudio(AudioEnums.AchievementUnlocked);
        }
    }

    private void checkTreasureHunterUnlockCondition() {
        int amount = items.values().stream()
                .filter(item -> item.getItemEnum().getItemRarity().equals(ItemRarityEnums.Relic))
                .toList()
                .size();
        if (amount >= 3 && PlayerProfileManager.getInstance().getLoadedProfile().getTreasureHunterLevel() <= 0) {
            PlayerProfileManager.getInstance().getLoadedProfile().setTreasureHunterLevel(1);
            PlayerProfileManager.getInstance().exportCurrentProfile();
            BoardManager.getInstance().getShopBoard().addGUIAnimation(AchievementUnlockHelper.createUnlockGUIComponent(BoonEnums.TREASURE_HUNTER.getUnlockImage()));
            AudioManager.getInstance().addAudio(AudioEnums.AchievementUnlocked);
        }
    }


    public Item createItemFromEnum(ItemEnums itemEnum) {
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
            case PiercingMissiles:
                return new PiercingMissiles();
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
            case ReflectiveShielding:
                return new ReflectiveShielding();
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
            case BeckoningFlames:
                return new EntanglingFlames();
            case BargainBucket:
                return new BargainBucket();
            case ShieldStabilizer:
                return new ShieldStabilizer();
            case ProtossScout:
                return new ProtossScoutItem();
            case ProtossArbiter:
                return new ProtossArbiterItem();
            case ProtossShuttle:
                return new ProtossShuttleItem();
            case HangarBayUpgrade:
                return new HangarBayUpgrade();
            case PyrrhicProtocol:
                return new PyrrhicProtocol();
            case RallyTheFleet:
                return new RallyTheFleet();
            case InverseRetrieval:
                return new InverseRetrieval();
            case Martyrdom:
                return new Martyrdom();
            case KineticDynamo:
                return new KineticDynamo();
            case ProtossThorns:
                return new ProtossThorns();
            case ArbiterMultiTargeting:
                return new ArbiterMultiTargeting();
            case SynergeticLink:
                return new SynergeticLink();
            case FuelCannister:
                return new FuelCannister();
            case InfernalPreIgniter:
                return new InfernalPreIgniter();
            case ConstructionKit:
                return new ConstructionKit();
            case EmergencyRepairs:
                return new EmergencyRepairs();
            case VengeanceProtocol:
                return new VengeanceProtocol();
            case ArbiterDamage:
                return new ArbiterDamage();
            case EternaFlame:
                return new EternaBurn();
            case EphemeralBlaze:
                return new EphemeralBlaze();
            case Stuivie:
                return new StuiversBestFriend();
            case GlassCannon:
                return new GlassCannon();
            case AimAssist:
                return new AimAssist();
            case ProtossCorsair:
                return new ProtossCorsairItem();
            case HighVelocityLasers:
                return new HighVelocityLasers();
            case Locked:
                return null;
            default:
                System.out.println("I tried to create: " + itemEnum + " but fell in default, did you forget to add it to the inventory creation?");
                return null;
        }
    }

    private void activateUponPurchaseItemEffects(Item item) {
        if (item.getApplicationMethod().equals(ItemApplicationEnum.UponAcquiring)) {
            item.applyEffectToObject(null);
        }
    }

    public List<Item> getItemsByApplicationMethod(ItemApplicationEnum applicationMethod) {
        return items.values().stream()
                .filter(item -> item.getApplicationMethod() == applicationMethod)
                .collect(Collectors.toList());
    }


    public Item getItemFromInventoryIfExists(ItemEnums itemName) {
        return items.get(itemName);
    }

    public Map<ItemEnums, Item> getItems() {
        return items;
    }

    public float getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(float cashMoney) {
        this.cashMoney = cashMoney;
    }

    public void addMinerals(float amount) {
//        if(amount > 0){
//            OnScreenTextManager.getInstance().addMineralsGainedText(amount);
//        }
        this.cashMoney += amount;
    }

    public void spendCashMoney(float amount) {
        this.cashMoney -= amount;
    }

    public void removeItemFromInventory(ItemEnums item) {
        if (this.items.containsKey(item)) {
            items.remove(item);
        }
    }
}
