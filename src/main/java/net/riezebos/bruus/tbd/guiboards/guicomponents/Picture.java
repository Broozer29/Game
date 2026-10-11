package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;

//An image component that can be given any picture, such as the tab underline and the parts of a volume bar
public class Picture extends DisplayOnly {

    public Picture (ImageEnums imageEnum) {
        super(configurationFor(imageEnum));
    }

    public void setPicture (BufferedImage picture) {
        setImage(picture);
    }

    public static BufferedImage scaled (BufferedImage image, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return result;
    }

    private static SpriteConfiguration configurationFor (ImageEnums imageEnum) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(imageEnum);
        return spriteConfiguration;
    }
}
