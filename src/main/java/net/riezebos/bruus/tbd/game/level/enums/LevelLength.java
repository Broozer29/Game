package net.riezebos.bruus.tbd.game.level.enums;

import java.util.Random;

public enum LevelLength {
    Short, // <2 minutes
    Medium, // 2-4 minutes
    Long; // >4 minutes

    public static LevelLength getRandomLength () {
        LevelLength[] lengths = LevelLength.values();
        return lengths[new Random().nextInt(lengths.length)];
    }

    public static LevelLength getLevelLengthByDuration(double durationInSeconds){
        if(durationInSeconds <= 120){
            return LevelLength.Short;
        } else if(durationInSeconds <=240){
            return LevelLength.Medium;
        }
        return LevelLength.Long;
    }
}
