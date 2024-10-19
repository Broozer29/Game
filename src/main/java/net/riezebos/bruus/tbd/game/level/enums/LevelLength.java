package net.riezebos.bruus.tbd.game.level.enums;

import java.util.Random;

public enum LevelLength {
    Short, // <3 minutes
    Medium, // 3-5 minutes
    Long; // >5 minutes

    public static LevelLength getRandomLength () {
        LevelLength[] lengths = LevelLength.values();
        return lengths[new Random().nextInt(lengths.length)];
    }

    public static LevelLength getLevelLengthByDuration(double durationInSeconds){
        if(durationInSeconds <= 180){
            return LevelLength.Short;
        } else if(durationInSeconds <=300){
            return LevelLength.Medium;
        }
        return LevelLength.Long;
    }
}
