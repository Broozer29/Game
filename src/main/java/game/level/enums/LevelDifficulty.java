package game.level.enums;

import java.util.Random;

public enum LevelDifficulty {
    Easy, Medium, Hard;

    public static LevelDifficulty getRandomDifficulty () {
        LevelDifficulty[] difficulties = LevelDifficulty.values();
        return difficulties[new Random().nextInt(difficulties.length)];
    }
}
