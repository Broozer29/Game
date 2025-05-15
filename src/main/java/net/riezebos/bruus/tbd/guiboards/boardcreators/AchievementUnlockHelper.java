package net.riezebos.bruus.tbd.guiboards.boardcreators;

import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class AchievementUnlockHelper {
    public static GUIComponent createUnlockGUIComponent(ImageEnums imageEnums) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(DataClass.getInstance().getWindowWidth() / 2);
        spriteConfiguration.setyCoordinate(DataClass.getInstance().getWindowHeight() * 0.8f);
        spriteConfiguration.setScale(1.3f * DataClass.getInstance().getResolutionFactor());
        spriteConfiguration.setImageType(imageEnums);

        GUIComponent boonComponent = new GUIComponent(spriteConfiguration);
        boonComponent.setTransparancyAlpha(true, 1, -0.002f);
        boonComponent.setCenterCoordinates(DataClass.getInstance().getWindowWidth() / 2,
                (DataClass.getInstance().getWindowHeight() * 0.8f));
        return boonComponent;
    }
}
