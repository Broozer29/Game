package game.utils;

import java.util.Random;

public class WeaponOffsetCalculator {

    public static int calculateRandomWeaponHeightOffset (int height) {
        //Remakes a random everytime this is called
        //sloppy temporary implementation that needs fixing but in the middle of a refactor so cant do it now
        Random random = new Random();

        int upOrDown = random.nextInt((1 - 0) + 1) + 1;
        int yOffset = random.nextInt(((height / 2) - 0) + 1) + 0;
        if (upOrDown == 1) {
            return yOffset;
        } else {
            return -yOffset;
        }
    }
}
