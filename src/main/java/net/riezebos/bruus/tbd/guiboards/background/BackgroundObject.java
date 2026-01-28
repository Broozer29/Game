
package net.riezebos.bruus.tbd.guiboards.background;

import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.image.BufferedImage;

public class BackgroundObject extends Sprite {

    private int depthLevel;
    private BGOEnums bgoType;

    public BackgroundObject(SpriteConfiguration spriteConfiguration, BackgroundObjectConfiguration bgoConfiguration){
        super(spriteConfiguration);
        this.bgoType = bgoConfiguration.getBgoType();
        this.depthLevel = bgoConfiguration.getDepthLevel();
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

    public int getDepthLevel () {
        return depthLevel;
    }

}