package game.util;

import game.items.enums.ItemEnums;

public class ItemDescriptionRetriever {

    public static String getDescriptionOfItem(ItemEnums itemEnums){
        switch (itemEnums){
            case PlasmaCoatedBullets -> {
                return "Attacks deal additional damage over time.";
            }
            case PhotonPiercer -> {
                return "Attacks against enemies with 90% or more health deals additional damage.";
            }
            case CannisterOfGasoline -> {
                return "Enemies explode on death, creating an explosion that deals damage and ignites enemies.";
            }
            case SelfRepairingSteel -> {
                return "After not taking damage for a few seconds, start regenerating hitpoints.";
            }
            case Battery -> {
                return "Your special attack has an additional charge.";
            }
            case FocusCrystal -> {
                return "Enemies in close range take additional damage";
            }
            case PrecisionAmplifier -> {
                return "Gain 10% crit chance.";
            }
            case PlatinumSponge -> {
                return "After not taking damage for a few seconds, gain additional armor.";
            }
            case EmergencyRepairBot -> {
                return "When dropping below 25% health, heals you for 75% max hitpoints. Consumed on use.";
            }
            case Overclock -> {
                return "Increases attack speed";
            }
            case RepulsionArmorPlate -> {
                return "Increases armor";
            }
            case ArmorPiercingRounds -> {
                return "Increases damage dealt to bosses";
            }
            case EnergySiphon -> {
                return "Killing an enemy grants a temporary barrier";
            }
            case MoneyPrinter -> {
                return "Taking damage grants money";
            }
            case StickyDynamite -> {
                return "Attacks apply a delayed explosion";
            }
            case PlasmaLauncher -> {
                return "Chance on hitting an enemy to fire a homing plasma shot";
            }
        }
        return "This item has no description yet";
    }
}