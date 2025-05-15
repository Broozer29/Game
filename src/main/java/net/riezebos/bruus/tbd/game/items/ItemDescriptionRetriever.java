package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossArbiter;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossScout;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossShuttle;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.items.items.*;
import net.riezebos.bruus.tbd.game.items.items.captain.*;
import net.riezebos.bruus.tbd.game.items.items.carrier.*;
import net.riezebos.bruus.tbd.game.items.items.firefighter.*;

public class ItemDescriptionRetriever {

    private ItemDescriptionRetriever() {

    }

    public static String getDescriptionOfItem(ItemEnums itemEnums) {
        switch (itemEnums) {
            case PlasmaCoatedBullets -> {
                return "Your missiles apply damage over time. Enemies take " +
                        Math.round(Math.round(PlasmaCoatedBullets.burningDamage * 100) * (PlasmaCoatedBullets.duration / DamageOverTime.damageInterval)) +
                        "(+" +
                        Math.round(Math.round(PlasmaCoatedBullets.burningDamage * 100) * (PlasmaCoatedBullets.duration / DamageOverTime.damageInterval)) + "%) damage over " +
                        PlasmaCoatedBullets.duration + " seconds (+" + PlasmaCoatedBullets.duration + ")";
            }
            case PhotonPiercer -> {
                return "Attacks against enemies with " + Math.round(PhotonPiercer.hpRequirement * 100) + "% or more health deal " +
                        Math.round(PhotonPiercer.damageAmplificationModifier * 100) + "% (+" + Math.round(PhotonPiercer.damageAmplificationModifier * 100) +
                        " %) additional damage.";
            }
            case CannisterOfGasoline -> {
                return "Enemies explode on death, creating an explosion that applies Ignite to enemies dealing " +
                        Math.round(Math.round(PlayerStats.igniteDamageMultiplier * 100) * (PlayerStats.igniteDuration / DamageOverTime.damageInterval)) + "% damage during " +
                        PlayerStats.igniteDuration + " (+" + Math.round(CannisterOfGasoline.igniteDurationBonus * 100) + "%) seconds";
            }
            case SelfRepairingSteel -> {
                double value = SelfRepairingSteel.repairAmount * (1000f / 15f);
                return "Regenerate " + String.format("%.1f", value) +
                        "(+" +
                        String.format("%.1f", value) +
                        ") hitpoints per second.";
            }
            case Battery -> {
                return "Your special attack gains 1 additional charge.";
            }
            case FocusCrystal -> {
                return "Your missiles deal " + Math.round(FocusCrystal.damageAmplificationModifier * 100) +
                        "% (+" +
                        Math.round(FocusCrystal.damageAmplificationModifier * 100) +
                        "%) additional damage to nearby enemies.";
            }
            case PrecisionAmplifier -> {
                return "Gain " + Math.round(PrecisionAmplifier.critChance * 100) + "% (+" + Math.round(PrecisionAmplifier.critChance * 100) + "%) critical strike chance. Critical strikes deal double damage.";
            }
            case PlatinumSponge -> {
                return "After taking damage, gain " +
                        Math.round(PlatinumSponge.armorBonus) +
                        " (+" +
                        Math.round(PlatinumSponge.armorBonus) +
                        ") armor for 2 seconds up to a maximum of " +
                        Math.round(PlatinumSponge.maxArmorBonus) +
                        " (+" +
                        Math.round(PlatinumSponge.maxArmorBonus) +
                        ")";
            }
            case EmergencyRepairBot -> {
                return "When dropping below " + Math.round(EmergencyRepairBot.healthActivationRatio * 100) + "% health, instantly heals you for " +
                        Math.round(EmergencyRepairBot.healingFactor * 100) + "% max hitpoints. Consumed on use.";
            }
            case Overclock -> {
                return "Increases attack speed by " + Math.round(Overclock.attackSpeedBonus) + "%";
            }
            case RepulsionArmorPlate -> {
                return "Gain 10 armor. Armor increases damage reduction.";  //disabled
            }
            case ArmorPiercingRounds -> {
                return "Increases damage dealt to Medium sized enemies by 20% (+20%)";  //disabled
            }
            case EnergySiphon -> {
                return "Gain " + Math.round(EnergySyphon.barrierAmount) + " (+" + Math.round(EnergySyphon.barrierAmount) + ") shields when killing an enemy.";
            }
            case MoneyPrinter -> {
                return "Killing an enemy has a 10% chance to grant 2 (+2) additional minerals";  //disabled
            }
            case StickyDynamite -> {
                return "Your missiles have " + Math.round(StickyDynamite.chanceToProc * 100) + "% chance to cause an explosion for " +
                        Math.round(StickyDynamite.explosionDamage * 100) + "% (+" + Math.round(StickyDynamite.explosionDamage * 100) + "%) additional damage";
            }
            case PlasmaLauncher -> {
                return Math.round(PlasmaLauncher.procChance * 100) + "% Chance on hitting an enemy to fire a piercing plasma shot for " + Math.round(PlasmaLauncher.damageMultiplier * 100) + "% (+" + Math.round(PlasmaLauncher.damageMultiplier * 100) + "%) damage";
            }
            case GuardianDrone -> {
                return "Gain 1 invincible drone.";
            }
            case CriticalOverloadCapacitor -> {
                return "Critical strikes deal an additional " + Math.round(CriticalOverloadCapacitor.damageMultiplier * 100) + "% (+" + Math.round(CriticalOverloadCapacitor.damageMultiplier * 100) + "%) damage.";
            }
            case BarrierSuperSizer -> {
                return "Inceases maximum shield by 20% (+20%)"; //disabled
            }
            case PiercingMissiles -> {
                return "Missiles pierce 1 additional time";
            }
            case BouncingModuleAddon -> {
                return "Piercing missiles bounce towards enemies instead. Missiles deal +" +
                        Math.round(BouncingModuleAddon.bonusDamagePercentage * 100) +
                        "% additional damage after each bounce..";
            }
            case VIPTicket -> {
                return "When entering the shop, grants 1 free shop refresh.";
            }
            case ElectricDestabilizer -> {
                return "Your Electro Shred ability now stuns non-boss enemies for " + ElectricDestabilizer.duration + " +(" + ElectricDestabilizer.duration + ") seconds.";
            }
            case ModuleAccuracy -> {
                return "Drones now fire towards the closest enemy. Drones deal " + Math.round(ModuleAccuracy.damageBonus * 100) + "% (+" + Math.round(ModuleAccuracy.damageBonus * 100) + "%) damage.";
            }
            case ElectricSupercharger -> {
                return "Your Electro Shred area of effect is improved. Electro Shred deals +" +
                        Math.round(ElectricSupercharger.buffAmount * 100) +
                        "% (+%" + Math.round(ElectricSupercharger.buffAmount * 100) + "%) damage.";
            }
            case ThornedPlates -> {
                return "Gain" + ThornedPlates.armorAmount + " (+" + ThornedPlates.armorAmount + ") armor. Upon taking damage, you return 100% Thorns damage to the attacker. Increases all Thorns damage by " + Math.round(ThornedPlates.buffAmount * 100) + "% (+ " + Math.round(ThornedPlates.buffAmount * 100) + "%)";
            }
            case Thornweaver -> {
                return "Colliding with enemies now applies 100% Thorns damage to them. You take " + Math.round(Thornweaver.collisionDamageReduction * 100) +
                        "% reduced damage from colliding with enemies.";
            }
            case BarbedAegis -> {
                return "All Thorns damage is increased by " + Math.round(BarbedAegis.ratio * 100) + "% (+" + Math.round(BarbedAegis.ratio * 100) + "%) of your armor."; //disabled
            }
            case BarbedMissiles -> {
                return "Your missiles have a " + Math.round(BarbedMissiles.procChance * 100) + "% (+" + Math.round(BarbedMissiles.procChance * 100) + "%) chance to deal an additional 100% Thorns damage.";
            }
            case ModuleElectrify -> {
                return "Drones fire Electro Shred instead of missiles. Drones now orbit " +
                        ModuleElectrify.orbitrangeBonus +
                        " yards further away";
            }
            case ModuleCommand -> {
                return "Your drones now fire whenever you fire. Maximum drone capacity is increased to " + ModuleCommand.maxDronesCapacity + ".";
            }
            case Contract -> {
                return "After killing " + Contract.killCountRequired + " enemies, transform into a random rare or legendary item upon entering the shop.";
            }
            case StickyOil -> {
                return "Your Ignite effects last" +
                        Math.round(StickyOil.bonusDurationMultiplier * 100) +
                        "% (+ " +
                        Math.round(StickyOil.bonusDurationMultiplier * 100) +
                        "%) longer. ";
            }
            case CorrosiveOil -> {
                return "Ignite reduces armor by " + CorrosiveOil.amountPerStack + " (+" + CorrosiveOil.amountPerStack + ") per stack of Ignite.";
            }
            case ScorchingFury -> {
                return "Ignite deals " + Math.round(ScorchingFury.bonusDamageMultiplier * 100) + "% (+" + Math.round(ScorchingFury.bonusDamageMultiplier * 100) + "%) more damage.";
            }
            case FlameDetonation -> {
                return "Ignite now causes enemies to explode leaving behind a flame for " + Math.round(FlameDetonation.duration) + " (+" + Math.round(FlameDetonation.duration) + ") seconds that applies Ignite.";
            }
            case EscalatingFlames -> {
                return "Ignite can stack 1 additional time.";
            }
            case EntanglingFlames -> {
                return "Ignite applies 100% Thorns damage 1 (+1) times every 0.75 seconds";
            }
            case ModuleScorch -> {
                return "Drones are transformed into fireballs that damage and apply Ignite.";
            }
            case BargainBucket -> {
                return "Gain 1 Scorching Fury, 1 Sticky Oil and 1 Escalating Flames.";
            }
            case ShieldStabilizer -> {
                return "Taking damage no longer halts shield regeneration. Reduces shield regeneration rate by " + Math.round(ShieldStabilizer.shieldRegenMultiplier * 100) + "%.";
            }
            case ProtossScout -> {
                return "Gain 1 Protoss Scout dealing " + Math.round(ProtossScout.scoutDamageFactor * 100) + "% damage. Takes up 1 Hangar Bay slot.";
            }
            case ProtossShuttle -> {
                return "Gain 1 Protoss Shuttle dealing " + Math.round(ProtossShuttle.shuttleDamageRatio * 100)
                        + "% damage. Takes up 1 Hangar Bay slot.";
            }
            case ProtossArbiter -> {
                return "Adds a Protoss Arbiter to your fleet. Arbiters heal damaged allies for " + Math.round(ProtossArbiter.healingRate * 66f) +
                        " (+" + Math.round(ProtossArbiterItem.healingBonusMultiplier * 100) + "%) hitpoints per second. " +
                        "Takes up 1 Hangar Bay slot";
            }
            case HangarBayUpgrade -> {
                return "Maximum amount of available Hangar Bay slots increased by " + HangarBayUpgrade.additionalShipsPerItem;
            }
            case PyrrhicProtocol -> {
                return "Beacons explode upon dying dealing " + Math.round(PyrrhicProtocol.explosionDamageRatio * 100) + "% " +
                        "+(" + Math.round(PyrrhicProtocol.explosionDamageRatio * 100) + "%) damage.";
            }
            case Martyrdom -> {
                return "When a Protoss Ship dies, remaining Protoss Ships become frenzied for " + Martyrdom.duration + " seconds. Gaining " +
                        Math.round(Martyrdom.attackSpeedIncrease * 100) + "% " + "(+" + Math.round(Martyrdom.attackSpeedIncrease * 100) + "%) attack speed.";
            }
            case RallyTheFleet -> {
                return "2 seconds after placement, beacons boost nearby Protoss Ships with " +
                        Math.round(RallyTheFleet.attackSpeedModifier * 100) + " (+" + Math.round(RallyTheFleet.attackSpeedModifier * 100) + " %) attack speed and " +
                        Math.round(RallyTheFleet.armorBonus) + " (+" + Math.round(RallyTheFleet.armorBonus) + " ) armor for 3 seconds.";
            }
            case InverseRetrieval -> {
                return "Instead of recalling beacons, teleport on top of them. After teleporting release a shockwave dealing " +
                        Math.round(InverseRetrieval.explosionDamageRatio * 100) + "% (+" + Math.round(InverseRetrieval.explosionDamageRatio * 100) + ") damage that stuns enemies for " +
                        InverseRetrieval.disableDuration + " seconds.";
            }
            case KineticDynamo -> {
                return "While moving in fast mode, you build up a charge. When switching to slow mode, release the charge in an explosion dealing up to " + Math.round(KineticDynamo.damageRatio * 100) + "% (+" + Math.round(KineticDynamo.damageRatio * 100) + "%) damage.";
            }
            case ProtossThorns -> {
                return "Your Protoss Ships return 100% Thorns damage upon being hit. Thorns damage dealt by Protoss Ships is increased by " + Math.round(ProtossThorns.thornsBonusDamageRatio * 100) + "% (+ " + Math.round(ProtossThorns.thornsBonusDamageRatio * 100) + "%).";
            }
            case ArbiterMultiTargeting -> {
                return "Arbiters can heal 1 additional target simultaneously.";
            }
            case SynergeticLink -> {
                return "Your Protoss Scouts gain " + Math.round(100 * SynergeticLink.scoutBonusDamagePerShip) + "% (+" + Math.round(100 * SynergeticLink.scoutBonusDamagePerShip) + "%) damage per Protoss Shuttle that is alive." +
                        "Your Protoss Shuttles gain 5% (+5%) base attack speed per Protoss Scout that is alive."; //This is WILDLY oversimplified, as it changes the base attack speed
            }
            case InfernalPreIgniter -> {
                return "Flamethrowers damage scales with remaining fuel. Dealing up to " + Math.round(InfernalPreIgniter.maxDamageBonnus * 100) +"% damage when full and 0% damage when empty.";
            }

            default -> {
                return "This item has no description yet";
            }
        }
    }
}
