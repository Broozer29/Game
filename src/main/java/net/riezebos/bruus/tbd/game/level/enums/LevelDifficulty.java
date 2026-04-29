package net.riezebos.bruus.tbd.game.level.enums;

import java.util.Random;

public enum LevelDifficulty {
    Easy,
    Medium,
    Hard;

    public static LevelDifficulty getRandomDifficulty () {
        LevelDifficulty[] difficulties = LevelDifficulty.values();
        return difficulties[new Random().nextInt(difficulties.length)];
    }

    public float toCreditBonus() {
        switch (this) {
            case Easy: return 1f;
            case Medium: return 2f;
            case Hard: return 3f;
            default: return 1.0f;
        }
    }
}
