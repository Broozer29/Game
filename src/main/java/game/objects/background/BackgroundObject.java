
package game.objects.background;

import java.awt.image.BufferedImage;
import java.util.Random;

import game.movement.Direction;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.Sprite;

public class BackgroundObject extends Sprite {

    private int depthLevel;
    private BGOEnums bgoType;
    private Random random;

//    public BackgroundObject (int x, int y, BufferedImage planetImage, float scale, BGOEnums bgoType, int depthLevel) {
//        super(x, y, scale);
//        setImage(planetImage);
//        this.bgoType = bgoType;
//        this.depthLevel = depthLevel;
//    }

    public BackgroundObject(SpriteConfiguration spriteConfiguration, BackgroundObjectConfiguration bgoConfiguration){
        super(spriteConfiguration);
        this.bgoType = bgoConfiguration.getBgoType();
        this.depthLevel = bgoConfiguration.getDepthLevel();
        this.random = new Random();
    }

    public void setNewPlanetImage (BufferedImage image) {
        setImage(image);
    }

    public BGOEnums getBGOtype () {
        return this.bgoType;
    }

    public void BGOEnums (BGOEnums bgoType) {
        this.bgoType = bgoType;
    }

    public void rotateRandomDegrees () {
        rotateImage(selectRandomDirection(), false);
    }

    private Direction selectRandomDirection () {
        Direction[] enums = Direction.values();
        Direction randomValue = enums[random.nextInt(enums.length)];
        return randomValue;
    }

    public int getDepthLevel () {
        return depthLevel;
    }

}