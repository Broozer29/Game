package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossArbiter;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossScout;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossShuttle;
import net.riezebos.bruus.tbd.game.items.items.carrier.*;
import net.riezebos.bruus.tbd.game.items.items.firefighter.CorrosiveOil;

public class ItemDescriptionRetriever {

    private ItemDescriptionRetriever() {

    }

    public static String getDescriptionOfItem(ItemEnums itemEnums) {
        switch (itemEnums) {
            case PlasmaCoatedBullets -> {
                return "Attacks now apply damage over time. Enemies rapidly and continously take 1% (+1%) damage for 1.5 (+1.5) seconds";
            }
            case PhotonPiercer -> {
                return "Attacks against enemies with 90% or more health deal 100% (+100%) additional damage.";
            }
            case CannisterOfGasoline -> {
                return "Enemies explode on death, creating an explosion that applies Ignite to enemies. Increases Ignite duration & damage by 20% (+20%)";
            }
            case SelfRepairingSteel -> {
                return "After not taking damage for 3 seconds, regenerate 5 (+5) hitpoints per second. Increases shield regeneration rate by 20% (+20%)";
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
                return "Killing an enemy regenerates a 5 (+5) shield hitpoints. When you are at maximum shields gain it as a temporary Barrier instead.";
            }
            case MoneyPrinter -> {
                return "Killing an enemy has a 10% chance to grant 2 (+2) additional minerals";
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
            case PiercingMissiles -> {
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
                return "Your Electro Shred ability is now improved. Electro Shred gains an additional +50% (+50%) damage.";
            }
            case ThornedPlates -> {
                return "Gain 5 (+5) armor. Additionally, grants Thorns and 20% (+20%) increase to Thorns damage.";
            }
            case Thornweaver -> {
                return "Grants Thorns. Colliding with enemies now applies Thorns damage to them. You take 50% reduced damage from colliding with enemies.";
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
                return "Your Ignite effects reduce the armor of targets by " + CorrosiveOil.amountPerStack + " (+" + CorrosiveOil.amountPerStack + ") per stack of Ignite.";
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
                return "Your Ignite effect applies your thorns damage 1 (+1) times every 0.75 seconds";
            }
            case ModuleScorch -> {
                return "Your drones no longer attack. Your drones orbit 30 yards further. Your drones are transformed into fireballs that damage and apply Ignite when colliding with enemies.";
            }
            case BargainBucket -> {
                return "Gain 1 Scorching Fury, 1 Sticky Oil and 1 Escalating Flames.";
            }
            case ShieldStabilizer -> {
                return "Taking damage no longer halts shield regeneration. Reduces shield regeneration rate by 50%.";
            }
            case ProtossScout -> {
                return "Increases the amount of available Protoss Scouts by 1 (+1). Scouts deal " + Math.round(ProtossScout.scoutDamageFactor * 100) + "% damage. Takes up 1 Hangar Bay slot for each Scout.";
            }
            case ProtossShuttle -> {
                return "Increases the amount of available Protoss Shuttles by 1 (+1). Shuttles shoot explosive projectiles that deal " + Math.round(ProtossShuttle.shuttleDamageRatio * 100)
                        + "% damage. Takes up 1 Hangar Bay slot for each Shuttle.";
            }
            case ProtossArbiter -> {
                return "Adds a Protoss Arbiter to your fleet. Arbiters heals a damaged ally for " + Math.round(ProtossArbiter.healingRate * 66f) +
                        " (+" + Math.round(ProtossArbiterItem.healingBonusMultiplier * 100) + "%) health per second. " +
                        "Arbiters prioritize healing the player. Takes up 1 Hangar Bay slot";
            }
            case HangarBayUpgrade -> {
                return "Increases the maximum amount of available Hangar Bay slots by " + HangarBayUpgrade.additionalShipsPerItem + " allowing for more Hangar Bay items.";
            }
            case PyrrhicProtocol -> {
                return "Whenever your beacon dies, it creates an explosion that deals " + Math.round(PyrrhicProtocol.explosionDamageRatio * 100) + "% " +
                        "+(" + Math.round(PyrrhicProtocol.explosionDamageRatio * 100) + "%) damage.";
            }
            case Martyrdom -> {
                return "Whenever a Protoss Ship dies, other Protoss Ships become frenzied for " + Martyrdom.duration + " seconds. Gaining " +
                        Math.round(Martyrdom.attackSpeedIncrease * 100) + "% " + "(+" + Math.round(Martyrdom.attackSpeedIncrease * 100) + "%) attack speed.";
            }
            case RallyTheFleet -> {
                return "2 seconds after placement, beacons now emit a singular pulse. Protoss Ships that are hit by this pulse gain " +
                        Math.round(RallyTheFleet.attackSpeedModifier * 100) + " (+" + Math.round(RallyTheFleet.attackSpeedModifier * 100) + " %) attack speed and " +
                        Math.round(RallyTheFleet.armorBonus) + " (+" + Math.round(RallyTheFleet.armorBonus) + " ) armor for 3 seconds.";
            }
            case InverseRetrieval -> {
                return "Instead or recalling beacons, you now teleport on top of them. Teleporting to a beacon releases a shockwave that deals " +
                        Math.round(InverseRetrieval.explosionDamageRatio * 100) + "% (+" + Math.round(InverseRetrieval.explosionDamageRatio * 100) + ") damage and stuns enemies for " +
                        InverseRetrieval.disableDuration + " seconds.";
            }
            case KineticDynamo -> {
                return "While moving in fast mode, you build up a charge. When you switch gears, this charge is released in an explosion. The damage of the explosion is based on the accumulated charge. Additional stacks increase the maximum charge by 100%.";
            }
            case ProtossThorns -> {
                return "Your Protoss Ships now deal Thorns damage upon being hit. Thorns damage dealt by Protoss Ships is increased by " + Math.round(ProtossThorns.thornsBonusDamageRatio * 100) + "% (+ " + Math.round(ProtossThorns.thornsBonusDamageRatio * 100) + "%).";
            }
            case ArbiterMultiTargeting -> {
                return "Arbiters can heal 1 (+1) additional target simultaneously.";
            }
            case SynergeticLink -> {
                return "Your Protoss Scouts gain " + Math.round(100 * SynergeticLink.scoutBonusDamagePerShip) + "% (+" + Math.round(100 * SynergeticLink.scoutBonusDamagePerShip) + "%) damage per Protoss Shuttle that is alive." +
                        "Your Protoss Shuttles gain 5% (+5%) base attack speed per Protoss Scout that is alive."; //This is WILDLY oversimplified, as it changes the base attack speed
            }

            default -> {
                return "This item has no description yet";
            }
        }
    }
}
