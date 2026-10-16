package net.riezebos.bruus.tbd.game.gameobjects.enemies;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.util.List;

/**
 * How one boss, mini boss or summon is spawned: its scale rule and the directions its spawn code can actually pick.
 * Built once in {@link BossSummons}; the spawn call sites and the preload read from it.
 * The directions are the allowed set only: a call site that picks at run time keeps its own pick logic over this set.
 */
public record EnemySpawnSettings(EnemyEnums enemy, ScaleRule scaleRule, float fixedScale, boolean resolutionScaled,
                                 List<Direction> directions) {

    public enum ScaleRule {
        DEFAULT, FIXED, PARENT
    }

    public EnemySpawnSettings {
        directions = List.copyOf(directions);
    }

    public static EnemySpawnSettings of(EnemyEnums enemy, Direction... directions) {
        return new EnemySpawnSettings(enemy, ScaleRule.DEFAULT, 0, false, List.of(directions));
    }

    /** A boss itself: default scale, reduced by 0.7 below resolution factor 1. */
    public static EnemySpawnSettings boss(EnemyEnums enemy, Direction... directions) {
        return new EnemySpawnSettings(enemy, ScaleRule.DEFAULT, 0, true, List.of(directions));
    }

    public static EnemySpawnSettings fixed(EnemyEnums enemy, float scale, Direction... directions) {
        return new EnemySpawnSettings(enemy, ScaleRule.FIXED, scale, false, List.of(directions));
    }

    public static EnemySpawnSettings parent(EnemyEnums enemy, Direction... directions) {
        return new EnemySpawnSettings(enemy, ScaleRule.PARENT, 0, false, List.of(directions));
    }

    /** The scale to spawn with. parentScale is only read by the PARENT rule. */
    public float scale(float parentScale) {
        switch (scaleRule) {
            case FIXED:
                return fixedScale;
            case PARENT:
                return parentScale;
            default:
                float scale = enemy.getDefaultScale();
                if (resolutionScaled && DataClass.getInstance().getResolutionFactor() < 1) {
                    scale *= 0.7f;
                }
                return scale;
        }
    }
}
