package game.spawner.enums;

import java.util.Random;

public enum LevelLength {
    Short, // <4 minutes
    Medium, // 4-6 minutes
    Long; // >6 minutes

    public static LevelLength getRandomLength () {
        LevelLength[] lengths = LevelLength.values();
        return lengths[new Random().nextInt(lengths.length)];
    }
}
