package net.riezebos.bruus.tbd.game.items;

public class ItemDescriptionRetriever {

    private ItemDescriptionRetriever(){

    }

    public static String getDescriptionOfItem(ItemEnums itemEnums){
        switch (itemEnums){
            case PlasmaCoatedBullets -> {
                return "Attacks now apply damage over time. Enemies continuously take 10% (+10%) damage for 1.5 (+1.5) seconds";
            }
            case PhotonPiercer -> {
                return "Attacks against enemies with 90% or more health deal 100% (+100%) additional damage.";
            }
            case CannisterOfGasoline -> {
                return "Enemies explode on death, creating an explosion that ignites enemies for 15% (+15%) damage that lasts for 2 (+2) seconds.";
            }
            case SelfRepairingSteel -> {
                return "After not taking damage for 3 seconds, rapidly regenerate 0.5 (+0.5) hitpoints every 15 milliseconds. Additionally, increases shield regeneration rate by 100% (+100%)";
            }
            case Battery -> {
                return "Your special attack gains 1 (+1) additional charge.";
            }
            case FocusCrystal -> {
                return "Your attacks that hit enemies in close range deal 15% (+15%) additional damage";
            }
            case PrecisionAmplifier -> {
                return "Gain 10% (+10%) critical strike chance. Attacks that critically strike deal double damage.";
            }
            case PlatinumSponge -> {
                return "Gain 3 (+3) armor after taking damage from an enemy. This bonus is lost after not taking damage for 2 seconds.";
            }
            case EmergencyRepairBot -> {
                return "When dropping below 25% health, instantly heals you for 75% max hitpoints. Consumed on use.";
            }
            case Overclock -> {
                return "Increases attack speed by 15% (+15%)";
            }
            case RepulsionArmorPlate -> {
                return "Gain 10 armor. Armor increases damage reduction.";
            }
            case ArmorPiercingRounds -> {
                return "Increases damage dealt to Medium sized enemies by 20% (+20%)";
            }
            case EnergySiphon -> {
                return "Killing an enemy grants a 25 (+25) hitpoints barrier that decays";
            }
            case MoneyPrinter -> {
                return "Killing an enemy has a 10% chance to grant 4 (+4) additional minerals";
            }
            case StickyDynamite -> {
                return "Every attack has 10% chance to explode for 100% (+100%) additional damage";
            }
            case PlasmaLauncher -> {
                return "10% Chance on hitting an enemy to fire a plasma shot towards the closest enemy dealing 200% (+200%) damage";
            }
            case GuardianDrone -> {
                return "Gain 1 (+1) invincible drone. Drones deal 20 damage";
            }
            case CriticalOverloadCapacitor -> {
                return "Increases damage dealt by critical strikes by 100% (+100%)";
            }
            case BarrierSuperSizer -> {
                return "Inceases maximum shield by 20% (+20%)";
            }
            case DrillerModule -> {
                return "Allows missiles to pierce 1 (+1) additional time";
            }
            case BouncingModuleAddon -> {
                return "Piercing missiles bounce towards enemies instead. Bouncing missiles deal +25% damage (+25%) after each bounce";
            }
            case VIPTicket -> {
                return "When entering the shop, grants 1 (+1) free shop refresh.";
            }
            case ElectricDestabilizer -> {
                return "Your Electro Shred ability now freezes enemies, preventing them from moving and shooting for 2 (+2) seconds";
            }
            case ModuleAccuracy -> {
                return "Drones now fire towards the enemy closest to you. Drones deal 25% (+25%) increased damage.";
            }
            case ElectricSupercharger -> {
                return "Your Electro Shred ability is now improved. Electro Shred gains an additional +20% (+20%) damage.";
            }
            case ThornedPlates -> {
                return "Gain 5 (+5) armor. Additionally, grants Thorns and 20% (+20%) increased damage to Thorns damage.";
            }
            case Thornweaver -> {
                return "Colliding with enemies now applies Thorns damage to them. You take 50% reduced damage from colliding with enemies.";
            }
            case BarbedAegis -> {
                return "Your Thorns damage is increased by 20% (+20%) of your armor.";
            }
            case BarbedMissiles -> {
                return "Your missiles have a 10% (+10%) chance to apply your Thorns damage once.";
            }
            case ModuleElectrify -> {
                return "Your drones fire Electro Shred instead of missiles. Drones now orbit 20 yards further from you.";
            }
            case ModuleCommand -> {
                return "Your drones no longer fire automatically. You no longer fire missiles. Your drones now fire missiles whenever you fire. Maximum drone capacity is increased to 12.";
            }
            case Contract -> {
                return "After killing 100 enemies, this item transforms into a random rare or legendary item when entering the shop.";
            }
            case StickyOil -> {
                return "Your Ignite effects last 33% (+33%) longer.";
            }
            case CorrosiveOil -> {
                return "Your Ignite effects reduce the armor of targets by 1 (+1) per stack of Ignite.";
            }
            case ScorchingFury -> {
                return "Your Ignite effects deal 20% (+20%) more damage.";
            }
            case FlameDetonation -> {
                return "Your Ignite effects now cause enemies to explode in fire. When an enemy dies, it leaves a flame behind for 2 (+2) seconds that ignites enemies that pass through it.";
            }
            case EscalatingFlames -> {
                return "Your Ignite can now stack 1 (+1) additional time.";
            }
            case EntanglingFlames -> {
                return "Your Ignite effect applies your thorns damage 1 (+1) times every 1,5 seconds";
            }
            case ModuleScorch -> {
                return "Your drones no longer attack. Your drones orbit 30 yards further. Your drones are transformed into fireballs that damage and apply Ignite when colliding with enemies.";
            }


            default -> {
                return "This item has no description yet";
            }
        }
    }
}
