package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MenuCursor extends GUIComponent {

    private GUIComponent selectedMenuTile;

    public MenuCursor(SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
        if (this.spriteConfiguration.getImageType().equals(ImageEnums.ProtossCarrier)) {
            super.setImage(ImageEnums.ProtossCarrierWithoutEngine);
        }
    }

    public GUIComponent getSelectedMenuTile() {
        return selectedMenuTile;
    }

    public void setSelectedMenuTile(GUIComponent selectedMenuTile) {
        this.selectedMenuTile = selectedMenuTile;
    }

    public int getxDistanceToKeep() {
        return Math.round(this.getWidth());
    }

    public int getYDistanceModification() {
        if (this.imageEnum.equals(ImageEnums.ProtossCarrierWithoutEngine)) {
            return Math.round(this.scale * 15);
        } else return 0;
    }

}
