package net.riezebos.bruus.tbd.game.level.enums;

import java.util.Random;

public enum MiniBossConfig {
    Easy(0),
    Medium(1),
    Hard(2);


    private int miniBossesPerLevel;

    MiniBossConfig(int miniBossesPerLevel){
        this.miniBossesPerLevel = miniBossesPerLevel;
    }

    public int getMiniBossesPerLevel(){
        return this.miniBossesPerLevel;
    }

    public static MiniBossConfig getRandomMiniBossConfig() {
        MiniBossConfig[] lengths = MiniBossConfig.values();
        return lengths[new Random().nextInt(lengths.length)];
    }
}
