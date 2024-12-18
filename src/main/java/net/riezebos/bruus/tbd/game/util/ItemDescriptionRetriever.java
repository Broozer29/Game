package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class ItemDescriptionRetriever {

    private ItemDescriptionRetriever(){

    }

    public static String getDescriptionOfItem(ItemEnums itemEnums){
        switch (itemEnums){
            case PlasmaCoatedBullets -> {
                return "Attacks deal additional 10% (+10%) damage every 1.5 (+1.5) seconds.";
            }
            case PhotonPiercer -> {
                return "Attacks against enemies with 90% or more health deals 75% (+75%) additional damage.";
            }
            case CannisterOfGasoline -> {
                return "Enemies explode on death, creating an explosion that ignites enemies for 15% (+15%) damage that lasts for 2 (+2) seconds.";
            }
            case SelfRepairingSteel -> {
                return "After not taking damage for 3 seconds, rapidly regenerate 0.5 (+0.5) hitpoints";
            }
            case Battery -> {
                return "Your special attack gains 1 (+1) additional charge.";
            }
            case FocusCrystal -> {
                return "Attacks that hit enemies in close range deal 15% (+15%) additional damage";
            }
            case PrecisionAmplifier -> {
                return "Gain 10% critical strike chance.";
            }
            case PlatinumSponge -> {
                return "After not taking damage for 3 seconds, gain +100 (+100) additional armor.";
            }
            case EmergencyRepairBot -> {
                return "When dropping below 25% health, heals you for 75% max hitpoints. Consumed on use.";
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
                return "Killing an enemy grants 25 (+25) temporary barrier";
            }
            case MoneyPrinter -> {
                return "Killing an enemy has a 10% chance to grant 5 (+5) additional minerals";
            }
            case StickyDynamite -> {
                return "Every attack has 10% chance to explode for 200% (+200%) additional damage, increases the size of the explosion by 10% (+10%)";
            }
            case PlasmaLauncher -> {
                return "10% Chance on hitting an enemy to fire a plasma shot towards the closest enemy dealing 150% (+150%) damage";
            }
            case GuardianDrone -> {
                return "Gain 1 (+1) invincible drone at the start of the round. The drone deals 100% damage";
            }
            case CriticalOverloadCapacitor -> {
                return "Increases critical damage by 100% (+100%)";
            }
            case BarrierSuperSizer -> {
                return "Inceases maximum shield and maximum overloaded shield by 25 (+25)";
            }
            case DrillerModule -> {
                return "Allows missiles to pierce 1 (+1) additional time";
            }
            case BouncingModuleAddon -> {
                return "Piercing missiles bounce towards enemies instead. Bouncing missiles deal +25% damage (+25%) after each bounce";
            }
            case VIPTicket -> {
                return "Reduces the cost of refreshing the shop by 25% (+25%)";
            }
            case ElectricDestabilizer -> {
                return "Your Electro Shred now freezes enemies, preventing them from moving and shooting for 2 (+2) seconds";
            }
            case ModuleAccuracy -> {
                return "Drones now fire towards the enemy closest to you. Drones deal 25% (+25%) increased damage.";
            }
            case ElectricSupercharger -> {
                return "Your Electro Shred ability is now improved. Electro Shred gains an additional +20% (+20%) damage.";
            }
            case ThornedPlates -> {
                return "Gain 20% (+20%) thorns damage. When taking damage, you deal thorns damage to the opponent that damaged you.";
            }
            case Thornweaver -> {
                return "Your thorns damage now has 10% (+10%) chance of applying your on-hit effects.";
            }
            case BarbedAegis -> {
                return "Your thorns damage is increased by 25% (+25%) of your armor. When taking damage, you deal thorns damage to the opponent that damaged you.";
            }
            case BarbedMissiles -> {
                return "Your attacks now additionally apply your thorns damage 1 (+1) time.";
            }
            default -> {
                return "This item has no description yet";
            }
        }
    }
}
