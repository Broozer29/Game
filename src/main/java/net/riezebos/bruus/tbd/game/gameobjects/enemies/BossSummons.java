package net.riezebos.bruus.tbd.game.gameobjects.enemies;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static net.riezebos.bruus.tbd.game.movement.Direction.*;

/**
 * The one list of how every boss and mini boss, and everything they spawn, is spawned.
 * Entry 0 of forEnemy(x) is x itself, the rest are the things x spawns (also through its own summons).
 */
public final class BossSummons {

    private static final Map<EnemyEnums, List<EnemySpawnSettings>> SETTINGS = new EnumMap<>(EnemyEnums.class);

    static {
        add(EnemyEnums.RedBoss,
                EnemySpawnSettings.boss(EnemyEnums.RedBoss, LEFT),
                EnemySpawnSettings.of(EnemyEnums.FourDirectionalDrone, LEFT),
                EnemySpawnSettings.of(EnemyEnums.Shuriken, LEFT_UP, LEFT_DOWN)); //Red never picks LEFT: nextInt(0, 2)
        add(EnemyEnums.SpaceStationBoss,
                EnemySpawnSettings.boss(EnemyEnums.SpaceStationBoss, LEFT),
                EnemySpawnSettings.of(EnemyEnums.FourDirectionalDrone, LEFT),
                EnemySpawnSettings.of(EnemyEnums.Needler, LEFT),
                EnemySpawnSettings.of(EnemyEnums.Shuriken, LEFT_UP, LEFT_DOWN, RIGHT_UP)); //never RIGHT_DOWN: nextInt(0, 3)
        add(EnemyEnums.CarrierBoss,
                EnemySpawnSettings.boss(EnemyEnums.CarrierBoss, LEFT),
                EnemySpawnSettings.of(EnemyEnums.EnemyCarrierBeacon, LEFT),
                EnemySpawnSettings.of(EnemyEnums.EnemyProtossScout, LEFT),
                EnemySpawnSettings.of(EnemyEnums.EnemyProtossShuttle, LEFT),
                EnemySpawnSettings.of(EnemyEnums.CarrierPulsingDrone, LEFT));
        add(EnemyEnums.YellowBoss,
                EnemySpawnSettings.boss(EnemyEnums.YellowBoss, LEFT),
                EnemySpawnSettings.of(EnemyEnums.Shuriken, LEFT_UP, LEFT_DOWN, RIGHT_UP, RIGHT_DOWN));
        add(EnemyEnums.TwinBoss,
                EnemySpawnSettings.boss(EnemyEnums.TwinBoss, LEFT, RIGHT)); //right twins LEFT, left twins RIGHT
        add(EnemyEnums.StrikerBoss,
                EnemySpawnSettings.boss(EnemyEnums.StrikerBoss, LEFT),
                EnemySpawnSettings.of(EnemyEnums.Needler, RIGHT, LEFT), //order matters: first side, second side
                EnemySpawnSettings.of(EnemyEnums.Seeker, RIGHT, LEFT),
                EnemySpawnSettings.parent(EnemyEnums.StrikerBossLaserbeamClone, LEFT), //rotated toward the player at run time
                EnemySpawnSettings.of(EnemyEnums.StrikerBossCornerDrone, LEFT));
        add(EnemyEnums.FinalBoss,
                EnemySpawnSettings.boss(EnemyEnums.FinalBoss, LEFT),
                EnemySpawnSettings.parent(EnemyEnums.FinalBossLaserbeamClone, LEFT)); //rotated toward the boss at run time
        add(EnemyEnums.BlueBoss,
                EnemySpawnSettings.boss(EnemyEnums.BlueBoss, LEFT),
                EnemySpawnSettings.of(EnemyEnums.BlueBossFactory, LEFT),
                EnemySpawnSettings.of(EnemyEnums.Needler, LEFT),
                EnemySpawnSettings.of(EnemyEnums.BlueBossFactoryDefender, LEFT));

        add(EnemyEnums.ShurikenMiniBoss, EnemySpawnSettings.of(EnemyEnums.ShurikenMiniBoss, LEFT_UP, LEFT_DOWN));
        add(EnemyEnums.MirageMiniBoss,
                EnemySpawnSettings.of(EnemyEnums.MirageMiniBoss, LEFT_UP, LEFT_DOWN),
                EnemySpawnSettings.parent(EnemyEnums.MirageMiniBoss, LEFT)); //the clone
        add(EnemyEnums.MotherShipMiniBoss,
                EnemySpawnSettings.of(EnemyEnums.MotherShipMiniBoss, LEFT),
                EnemySpawnSettings.fixed(EnemyEnums.MotherShipDrone, 0.25f, LEFT));
        add(EnemyEnums.DefenderMiniBoss, EnemySpawnSettings.of(EnemyEnums.DefenderMiniBoss, LEFT));
        add(EnemyEnums.LaserbeamMiniBoss, EnemySpawnSettings.of(EnemyEnums.LaserbeamMiniBoss, LEFT));
    }

    private BossSummons() {
    }

    private static void add(EnemyEnums enemy, EnemySpawnSettings... settings) {
        SETTINGS.put(enemy, List.of(settings));
    }

    /** Entry 0 is the enemy itself, the rest is everything it spawns. Empty for any other enemy. */
    public static List<EnemySpawnSettings> forEnemy(EnemyEnums enemy) {
        return SETTINGS.getOrDefault(enemy, List.of());
    }

    /** The settings of a child as spawned by this parent (a child type differs per parent). Skips entry 0, the parent itself, so a parent that spawns its own type (the Mirage clone) gets the child's settings. */
    public static EnemySpawnSettings summon(EnemyEnums parent, EnemyEnums child) {
        List<EnemySpawnSettings> list = forEnemy(parent);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).enemy() == child) {
                return list.get(i);
            }
        }
        throw new IllegalArgumentException(child + " is not in the spawn list of " + parent);
    }
}
