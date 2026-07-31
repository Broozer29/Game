package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossArbiter;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossScout;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossShuttle;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.items.items.*;
import net.riezebos.bruus.tbd.game.items.items.captain.*;
import net.riezebos.bruus.tbd.game.items.items.carrier.*;
import net.riezebos.bruus.tbd.game.items.items.deprecated.ArmorPiercingRounds;
import net.riezebos.bruus.tbd.game.items.items.firefighter.*;

public class ItemDescriptionRetriever {

    private ItemDescriptionRetriever() {

    }


    /*
            ITEM DISTRIBUTION AS OF 01-07-2026 (shared items are included)
                                (generics + class specific + shared = total amount)
            CAPTAIN:
                Relics:         7 + 6 + 1   = 14
                Legendaries:    4 + 3 + 1   = 8
                Rares:          4 + 2       = 6
                Commons:        6 + 3 + 1   = 10
                TOTAL:                      = 37 (24 can be rolled in the shop)

            FIREFIGHTER:
                Relics:         7 + 5 + 1   = 13
                Legendaries:    4 + 5 + 1   = 10
                Rares:          4 + 3       = 7
                Commons:        6 + 2       = 8
                TOTAL:                      = 37 (25 can be rolled in the shop)

            CARRIER:
                Relics:         7 + 3       = 10
                Legendaries:    4 + 4 + 1   = 9
                Rares:          5 + 6       = 11
                Commons:        5 + 2 + 1   = 8
                TOTAL:                      = 38 (29 can be rolled in the shop)



            PLANS:
                CAPTAIN: Leave as is and continue testing
                FIREFIGHTER: Move some legendaries to relics. Introduce some new commons.
                    Idea: move the items that determine wether you play ignite DoT or flamethrower to the relics, add commons that support these archetypes
                CARRIER: Move legendaries to relics. Either add some commons or move some rares to commons for better distribution
                    Problem: no clear build archetype, it's just "random bullshit go!", but maybe its fine to have 1 such class?
     */


    public static String getDescriptionOfItem(ItemEnums itemEnums) {
        switch (itemEnums) {
            //--------------------------------------generic items--------------------------------------
            //7
            case GlassCannon -> {
                return "You deal double damage. You take double damage.";} //relic
            case HelpRequested -> {
                return "Contracts sold in the shop are now free and always reward Legendary quality items. Contracts now require " + Math.round((Contract.killCountRequired * (1 + HelpRequested.additionalKillsRequiredModifier))) + " kills to be completed.";
            } //relic
            case ElectricDestabilizer -> {
                return "Your Electroshred ability now stuns non-boss enemies for " + Math.round(ElectricDestabilizer.duration) + " seconds.";
            } //relic
            case ShieldStabilizer -> {
                return "Taking damage no longer halts shield regeneration. Reduces shield regeneration rate by " + Math.round(ShieldStabilizer.shieldRegenMultiplier * 100) + "%.";
            } //relic
            case BonusKaart -> {
                return "Gain an additional copy of the first item you purchase in the shop. Can be used multiple times.";
            } //relic
            case GreedIsGood -> {
                return "Cash carriers no longer spawn. Instead, medium sized enemies have a " + Math.round(GreedIsGood.mineralsPerPickupChance * 100) + "% chance to drop a coin worth " + GreedIsGood.mineralsPerPickup + " minerals.";
            } //relic
            case Placeholder -> {
                return "Killing an enemy spawns a friendly stationary drone that attacks nearby enemies. Up to 4 drones per player can be spawned at a time.";
            } //relic
            case WisdomBall -> {return "Refreshes in the shop have a chance to be wondrous.";}
            case Stuivie -> {
                return "Once per round you get revived after dying. Upon reviving unleash an explosion dealing " + Math.round(StuiversBestFriend.explosionDamageAmount * 100) + "% damage.";
            } //relic / disabled in multiplayer


            //4
            case VIPTicket -> {
                return "When entering the shop, grants 1 FREE shop reroll.";
            } //legendary
            case Guillotine -> {
                return "Enemies that are damaged below " + Math.round(Guillotine.hitpointsThreshold * 100) + " % (+" + Math.round(Guillotine.hitpointsThreshold * 100) + "%) of their hitpoints are instantly killed.";
            } //legendary
            case ExplosiveGreed -> {
                return "Picking up coins releases a large explosion dealing " + Math.round(ExplosiveGreed.damageModifier * 100) + "% (+" + Math.round(ExplosiveGreed.damageModifier * 100) + "%) damage.";
            } //legendary
            case CalmInChaos -> {
                return "Deal " + Math.round(CalmInChaos.damageBonus * 100) + "% (+" + Math.round(CalmInChaos.damageBonus * 100) + "%) additional damage. This bonus is lost for " + CalmInChaos.cooldown + " seconds after taking damage.";
            } //legendary

            //4
            case CannisterOfGasoline -> {
                return "Enemies explode on death, creating an explosion that applies Ignite to enemies dealing " +
                        Math.round(Math.round(PlayerStats.igniteDamageMultiplier * 100) * (PlayerStats.igniteDuration / DamageOverTime.damageInterval)) + "% (+" + Math.round(CannisterOfGasoline.igniteDamageBonus * 100) + "%) damage over " +
                        PlayerStats.igniteDuration + " seconds.";
            } //rare
            case Battery -> {
                return "Your special attack gains 1 (+1) additional charge. Your special attack recharges " + Math.round(Battery.cooldownReduction * 100) + "% (+ " + Math.round(Battery.cooldownReduction * 100) + "%) faster.";
            } //rare
            case PlatinumSponge -> {
                return "Reduces all damage taken by " + Math.round(PlatinumSponge.damageReduction) + " (+" + Math.round(PlatinumSponge.damageReduction) + "). Damage taken cannot be reduced below 1";
            } //rare
            case CriticalOverloadCapacitor -> {
                return "Critical strikes deal an additional " + Math.round(CriticalOverloadCapacitor.damageMultiplier * 100) + "% (+" + Math.round(CriticalOverloadCapacitor.damageMultiplier * 100) + "%) damage.";
            } //rare

            //6
            case EmergencyRepairBot -> {
                return "When dropping below " + Math.round(EmergencyRepairBot.healthActivationRatio * 100) + "% health, instantly heals you for " +
                        Math.round(EmergencyRepairBot.healingFactor * 100) + "% max hitpoints. Consumed on use.";
            } //common
            case Overclock -> {
                return "Increases attack speed by " + Math.round(Overclock.attackSpeedBonus) + "%";
            } //common
            case PrecisionAmplifier -> {
                return "Gain " + Math.round(PrecisionAmplifier.critChance * 100) + "% (+" + Math.round(PrecisionAmplifier.critChance * 100) + "%) critical strike chance. Critical strikes deal double damage.";
            } //common
            case SelfRepairingSteel -> {
                double value = SelfRepairingSteel.repairAmount * (1000f / 15f);
                return "Regenerate " + String.format("%.1f", value) +
                        " (+" +
                        String.format("%.1f", value) +
                        ") hitpoints per second.";
            } //commmon
            case GuardianDrone -> {
                return "Gain 1 orbiting drone. It attacks automatically dealing 100% damage. Drones do not apply item effects.";
            } //common
            case Contract -> {
                return "After killing " + Contract.killCountRequired + " enemies, transform into a random rare or legendary item upon entering the shop.";
            } //common
            //--------------------------------------captain items--------------------------------------
            //6
            case BouncingLasers -> {
                return "Piercing missiles bounce towards enemies instead. Missiles deal +" +
                        Math.round(BouncingLasers.bonusDamagePercentage * 100) +
                        "% additional damage after each bounce. Missiles pierce 1 additional time.";
            } //relic
            case ModuleElectrify -> {
                return "Whenever you fire Electroshred, your drones fire a copy of it. Can occur once every " +
                        String.format("%.1f", ModuleElectrify.cooldown) +
                        " seconds. Electroshred fired from drones destroy enemy projectiles.";
            } //relic
            case ModuleCommand -> {
                return "Your drones now fire whenever you fire. Maximum drone capacity is increased to " + ModuleCommand.maxDronesCapacity + ".";
            } //relic
            case AnionInverter -> {
                return "Electroshred cooldown decreased by " + Math.abs(Math.round(AnionInverter.cooldownModifier * 100)) + "%. Electroshred damage increased by " + Math.round(AnionInverter.damageModifier * 100) + "%. Electroshred does NOT destroy enemy projectiles.";
            } //relic
            case ModuleFocusFire -> {
                return "Your drones no longer fire automatically. After your laserbeam hits a target, all drones immediately fire 1 shot towards the target.";
            } //relic
            case OneShotOneKill -> {
                return "Your missiles that strike enemies with " + Math.round(OneShotOneKill.hpRequirement * 100) + "% hp will always critically strike and deal " + Math.round(OneShotOneKill.damageAmplificationModifier * 100) + "% damage.";
            } //relic

            //3
            case ElectricSupercharger -> {
                return "Your Electro Shred area of effect is improved. Electro Shred deals +" +
                        Math.round(ElectricSupercharger.buffAmount * 100) +
                        "% (+" + Math.round(ElectricSupercharger.buffAmount * 100) + "%) damage.";
            } //legendary
            case ExplosiveLaserbeams -> {
                return "Your missiles cause an explosion when colliding with enemies dealing " + Math.round(ExplosiveLaserbeams.damageModifier * 100) + "% (+" + Math.round(ExplosiveLaserbeams.damageModifier * 100) + "%) damage.";
            } //legendary
            case ElectroShedding -> {
                return "Electroshred now permanently reduces enemy armor by " + Math.round(ElectroShedding.armorReduction) + " (+" + Math.round(ElectroShedding.armorReduction) + ") whenever it deals damage. Losing armor increases ALL damage taken.";
            } //legendary

            //2
            case HighVelocityLasers -> {
                return "Your missiles gain " + (Math.round(HighVelocityLasers.moveSpeedModifier * 100) + "% movement speed.");
            } //rare
            case PlasmaLauncher -> {
                return Math.round(PlasmaLauncher.procChance * 100) + "% Chance on hitting an enemy to fire a piercing plasma shot for " + Math.round(PlasmaLauncher.damageMultiplier * 100) + "% (+" + Math.round(PlasmaLauncher.damageMultiplier * 100) + "%) damage";
            } //rare

            //3
            case PhotonPiercer -> {
                return "Your missiles that hit enemies with " + Math.round(PhotonPiercer.hpRequirement * 100) + "% or more health deal " +
                        Math.round(PhotonPiercer.damageAmplificationModifier * 100) + "% (+" + Math.round(PhotonPiercer.damageAmplificationModifier * 100) +
                        "%) additional damage.";
            } //common
            case FocusCrystal -> {
                return "Your missiles deal " + Math.round(FocusCrystal.damageAmplificationModifier * 100) +
                        "% (+" +
                        Math.round(FocusCrystal.damageAmplificationModifier * 100) +
                        "%) additional damage to nearby enemies.";
            } //common
            case StickyDynamite -> {
                return "Your missiles have " + Math.round(StickyDynamite.chanceToProc * 100) + "% chance to cause an explosion for " +
                        Math.round(StickyDynamite.explosionDamage * 100) + "% (+" + Math.round(StickyDynamite.explosionDamage * 100) + "%) additional damage";
            } //common
            //--------------------------------------firefighter items--------------------------------------
            //2
            case BeckoningFlames -> {
                return "Automatically fire a missile dealing " + Math.round(BeckoningFlames.damageBonus * 100) + "% damage to Ignited targets every 0.75 seconds they are affected by Ignite.";
            } //relic
            case ModuleScorch -> {
                return "Drones are transformed into fireballs that damage and apply Ignite.";
            } //relic
            case RingOfFire -> {
                return "While your Fire Shield is active, shoot missiles in all directions dealing " + Math.round(RingOfFire.projectileDamage * 100) + "% damage and apply ignite.";
            } //relic
            case FieryImplosion -> {
                return "When ignite reaches maximum stacks it instantly explodes dealing the full damage in a large explosion.";
            } //relic
            case FireWithoutGasIsAss -> {
                return "Ignite damage is reduced by " + Math.round(FireWithoutGasIsAss.reduction * 100) + "%. Enemies taking damage from your Flamethrower take "+ Math.round(FireWithoutGasIsAss.increase * 100) +"% increased damage from Ignite.";
            } //relic

            //6
            case CorrosiveOil -> {
                return "Ignite reduces armor by " + CorrosiveOil.amountPerStack + " (+" + CorrosiveOil.amountPerStack + ") per stack of Ignite.";
            } //legendary        (ignite build)
            case FlameDetonation -> {
                return "Ignite now causes enemies to explode leaving behind a flame for " + Math.round(FlameDetonation.duration) + " (+" + Math.round(FlameDetonation.duration) + ") seconds that applies Ignite.";
            } //legendary (make it a relic?)
            case BargainBucket -> {
                return "Gain 1 Scorching Fury, 1 Sticky Oil and 1 Escalating Flames.";
            } //legendary       (flamethrower build)
            case InfernalPreIgniter -> {
                double value = InfernalPreIgniter.scalingFactor * (1000f / 15f);
                return "While your Flamethrower is active, it's damage exponentially increases with  " + String.format("%.1f", value) +
                        "% (+" +
                        String.format("%.1f", value) +
                        "%) per second.";
            } //legendary  (flamethrower build)
            case EternaFlame -> {
                return "Your ignite deals " + Math.round(EternaBurn.igniteDamageReduction * 100) + "% reduced damage. Flamethrower requires " + Math.round(EternaBurn.fuelUsagereduction * 100) + "% less fuel.";
            } //legendary,  (flamethrower build) make it a common -> Remove ignite damage reduction and keep flamethrower fuel consumption reduction?

            //3
            case FuelCannister -> {
                return "Increases maximum fuel capacity and fuel regeneration by " + Math.round(FuelCannister.bonusFuelMultiplier * 100) + "%.";
            } //Rare (flamethrower build)
            case StickyOil -> {
                return "Ignite duration increased by " +
                        Math.round(StickyOil.bonusDurationMultiplier * 100) +
                        "%. ";
            } //rare           (ignite build)
            case EphemeralBlaze -> {
                return "Your ignite deals " + Math.round(EphemeralBlaze.igniteDamageReduction * 100) + "% (+" + Math.round(EphemeralBlaze.igniteDamageReduction * 100) + "%) reduced damage. " +
                        "Your flamethrower deals " + Math.round(EphemeralBlaze.primaryDamagePerIgniteStack * 100) + "% (+" + Math.round(EphemeralBlaze.primaryDamagePerIgniteStack * 100) + "%) increased damage per stack of ignite on the target. ";
            } //rare      (flamethrower build)

            //2
            case ScorchingFury -> {
                return "Ignite deals " + Math.round(ScorchingFury.bonusDamageMultiplier * 100) + "% more damage.";
            } //common     (ignite build)
            case EscalatingFlames -> {
                return "Ignite can stack 1 additional time.";
            } //common    (ignite build)
            //--------------------------------------carrier items--------------------------------------
            //3
            case PulsingBeacon -> {
                return "Your beacon now casts a damaging pulse every " + Math.round(PulsingBeacon.cooldown) + " second dealing " + Math.round(PulsingBeacon.damageModifier * 100) + "% damage. Every pulse increases the damage of subsequent pulses by " + Math.round(PulsingBeacon.damageBonusPerCast * 100) + "%";
            } //relic
            case ArbiterDamage -> {
                return "Protoss Arbiters no longer heal allies. Protoss Arbiters gain " + Math.round(ArbiterDamage.damageIncreaseMultiplier * 100) + "% increased effectiveness and damage random enemies.";
            } //relic
            case InverseRetrieval -> {
                return "Instead of recalling beacons, teleport on top of them. After teleporting release a shockwave dealing " +
                        Math.round(InverseRetrieval.explosionDamageRatio * 100) + "% (+" + Math.round(InverseRetrieval.explosionDamageRatio * 100) + ") damage that stuns enemies for " +
                        InverseRetrieval.disableDuration + " seconds.";
            } //Relic

            //4
            case HangarBayUpgrade -> {
                return "Maximum amount of available Hangar Bay slots increased by " + HangarBayUpgrade.additionalShipsPerItem;
            } //legendary
            case Martyrdom -> {
                return "When a Protoss Ship dies, remaining Protoss Ships become frenzied for " + Martyrdom.duration + " seconds. Gaining " +
                        Math.round(Martyrdom.attackSpeedIncrease * 100) + "% " + "(+" + Math.round(Martyrdom.attackSpeedIncrease * 100) + "%) attack speed.";
            } //legendary
            case AimAssist -> {
                return "Protoss ships gain " + Math.round(AimAssist.protossAttackRangeBonus * 100) + "% attack range.";
            }  //legendary
            case ConstructionKit -> {
                return "Increases Protoss Ship construction speed by " + Math.round(ConstructionKit.additionalConstructionSpeed * 100) + "%.";
            } //legendary

            //6
            case ProtossArbiter -> {
                return "Adds a Protoss Arbiter to your fleet. Arbiters heal damaged allies for " + Math.round(ProtossArbiter.healingRate * 66f) +
                        " (+" + Math.round(ProtossArbiterItem.healingBonusMultiplier * 100) + "%) hitpoints per second. " +
                        "Takes up 1 Hangar Bay slot";
            } //rare
            case ArbiterMultiTargeting -> {
                return "Arbiters can heal 1 additional target simultaneously.";
            } //rare
            case RallyTheFleet -> {
                return "2 seconds after placement, beacons boost nearby Protoss Ships with " +
                        Math.round(RallyTheFleet.attackSpeedModifier * 100) + " (+" + Math.round(RallyTheFleet.attackSpeedModifier * 100) + " %) attack speed and " +
                        Math.round(RallyTheFleet.armorBonus) + " (+" + Math.round(RallyTheFleet.armorBonus) + " ) armor for 3 seconds.";
            } //rare
            case EmergencyRepairs -> {
                return "Increases Protoss Ship construction speed by " + Math.round(EmergencyRepairs.constructionSpeedBonusMultiplier * 100) + "% (+" + Math.round(EmergencyRepairs.constructionSpeedBonusMultiplier * 100) + "%). For " + Math.round(EmergencyRepairs.duration) + " seconds after a Protoss Ship dies.";
            } //rare
            case VengeanceProtocol -> {
                return "Protoss Ships explode upon death, dealing " + Math.round(VengeanceProtocol.explosionDamageMultiplier * 100) + "% (+" + Math.round(VengeanceProtocol.explosionDamageMultiplier * 100) + "%) damage";
            } //rare
            case ProtossCorsair -> {
                return "Gain 1 Protoss Corsair. Corsairs are suicide bombers that deal " + Math.round(ProtossCorsairItem.hitpointsDamage * 100 ) + "% of the enemies maximum hitpoints as damage. Corsairs deal a maximum of " + Math.round(ProtossCorsairItem.maxDamageBase) + " (+" + Math.round(ProtossCorsairItem.maxDamageIncrease) + ") damage.";
            } //rare

            //2
            case ProtossScout -> {
                return "Gain 1 Protoss Scout dealing " + Math.round(ProtossScout.scoutDamageFactor * 100) + "% damage. Takes up 1 Hangar Bay slot.";
            } //common
            case ProtossShuttle -> {
                return "Gain 1 Protoss Shuttle dealing " + Math.round(ProtossShuttle.shuttleDamageRatio * 100)
                        + "% damage. Takes up 1 Hangar Bay slot.";
            } //common
            //--------------------------------------shared between some classes--------------------------------------
            case ModuleAccuracy -> {
                return "Drones now aim towards the enemy closest to you.";
            } //relic -> captain/firefighter

            case PiercingMissiles -> {
                return "Your missiles pierce 1 additional time";
            } //legendary -> carrier/captain
            case Adrenaline -> {
                return "Taking damage increases your attack speed by " + Math.round(Adrenaline.attackSpeedIncrease * 100) + "% (+" + Math.round(Adrenaline.attackSpeedIncrease * 100) + "%) for " + Math.round(Adrenaline.duration) + " seconds. Taking damage again refreshes the duration of the effect.";
            } //legendary -> captain/firefighter

            case PlasmaCoatedBullets -> {
                return "Your attacks apply damage over time. Enemies take " +
                        Math.round(Math.round(PlasmaCoatedBullets.burningDamage * 100) * (PlasmaCoatedBullets.duration / DamageOverTime.damageInterval)) +
                        "% (+" +
                        Math.round(Math.round(PlasmaCoatedBullets.burningDamage * 100) * (PlasmaCoatedBullets.duration / DamageOverTime.damageInterval)) + "%) damage every " +
                        PlasmaCoatedBullets.duration + " seconds.";
            } //common -> carrier/captain



            //--------------------------------------Uncounted--------------------------------------
            case ModulePower -> {
                return "Drones deal " + Math.round(ModulePower.damageBonus * 100) + "% (+" + Math.round(ModulePower.damageBonus * 100) + "%) damage.";
            }
            case ArmorPiercingRounds -> {
                return "Increases damage dealt to Mini Bosses by " + Math.round(ArmorPiercingRounds.damageModifier * 100) + "%";  //disabled
            }

            //--------------------------------------Disabled or deprecated items--------------------------------------
            case PyrrhicProtocol -> {
                return "Beacons explode upon dying dealing " + Math.round(PyrrhicProtocol.explosionDamageRatio * 100) + "% " +
                        "+(" + Math.round(PyrrhicProtocol.explosionDamageRatio * 100) + "%) damage.";
            } //legendary, marked for possible removal as this just doesn't fit any playstyle/archetype?
            case RepulsionArmorPlate -> {
                return "Gain 10 armor. Armor increases damage reduction.";  //disabled
            }
            case EnergySiphon -> {
                return "Gain " + Math.round(EnergySyphon.barrierAmount) + " (+" + Math.round(EnergySyphon.barrierAmount) + ") shields when killing an enemy.";
            }
            case MoneyPrinter -> {
                return "Killing an enemy has a 10% chance to grant 2 (+2) additional minerals";  //disabled
            }
            case BarrierSuperSizer -> {
                return "Inceases maximum shield by " + Math.round(BarrierSupersizer.modifierBonus * 100) + "%";
            }
            case PuncturingPierce -> { //different formula of *10 factor intead of 100, mathmatically no difference but this math perspective suits the user better
                return "Your missiles now deal an additional " + Math.round(PuncturingPierce.damageIncreasePerMoveSpeedIncreaseModifier * 10) + "% additional damage for every 10% increased move speed.";
            }
            case ReflectiveShielding -> {
                return "Whilst your shield is up, colliding with enemy missiles returns a missile dealing " +
                        Math.round(ReflectiveShielding.buffAmount * 100) + "% (+" + Math.round(ReflectiveShielding.buffAmount * 100) + "%) damage";
            }
            case Thornweaver -> {
                return "Colliding with enemies now applies 100% Thorns damage to them. You take " + Math.round(Thornweaver.collisionDamageReduction * 100) +
                        "% reduced damage from colliding with enemies.";
            }
            case BarbedAegis -> {
                return "Attacks that are Reflected have a " + Math.round(BarbedAegis.procChance * 100) + " % +(" + Math.round(BarbedAegis.procChanceIncrease) + "%) chance to deal " + Math.round(BarbedAegis.damageReduction * 100) + "% reduced damage."; //disabled
            }
            case BarbedMissiles -> {
                return "Your missiles have a " + Math.round(BarbedMissiles.procChance * 100) + "% (+" + Math.round(BarbedMissiles.procChance * 100) + "%) chance to deal an additional 100% Thorns damage.";
            }
            case LeechingLasers -> {
                return "Missiles that critically strike heal for " + LeechingLasers.healAmount + " (+" + LeechingLasers.healAmount + ") damage.";
            }
            case KineticDynamo -> {
                return "While moving in fast mode, you build up a charge. When switching to slow mode, release the charge in an explosion dealing up to " + Math.round(KineticDynamo.damageRatio * 100) + "% (+" + Math.round(KineticDynamo.damageRatio * 100) + "%) damage.";
            } //disabled due to multiplayer
            case ProtossThorns -> {
                return "Your Protoss Ships return 100% Thorns damage upon being hit. Thorns damage dealt by Protoss Ships is increased by " + Math.round(ProtossThorns.thornsBonusDamageRatio * 100) + "% (+ " + Math.round(ProtossThorns.thornsBonusDamageRatio * 100) + "%).";
            }
            case SynergeticLink -> {
                return "Your Protoss Scouts gain " + Math.round(100 * SynergeticLink.scoutBonusDamagePerShip) + "% (+" + Math.round(100 * SynergeticLink.scoutBonusDamagePerShip) + "%) damage per Protoss Shuttle that is alive." +
                        "Your Protoss Shuttles gain " + Math.round(100 * SynergeticLink.shuttleMissileSpeedPerStack) + "% (+" + Math.round(100 * SynergeticLink.shuttleMissileSpeedPerStack) + "%) missile speed per Protoss Scout that is alive."; //This is WILDLY oversimplified, as it changes the base attack speed
            } //disabled because it needs a refactor, implementation doesn't live up to intended fantasy
            case BigIron -> {
                return "You now charge your laserbeam over " + Math.round(BigIron.maxChargeSeconds) + " seconds. Fully charged laserbeams deal " + Math.round((BigIron.damagePerInterval * BigIron.amountOfIntervals) * 100) + "% bonus damage and are " + Math.round((BigIron.scaleGrowthPerInterval * BigIron.amountOfIntervals) * 100) + "% larger. Bonus attack speed now increases the damage dealt.";
            } //relic / disabled due to it feeling bad and being buggy
            default -> {
                return "This item has no description yet";
            }
        }
    }
}
