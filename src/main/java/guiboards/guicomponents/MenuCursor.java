package guiboards.guicomponents;

import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class MenuCursor extends GUIComponent{

    private GUIComponent selectedMenuTile;
    private int xDistanceToKeep = 75;

    public MenuCursor (SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
    }

    public MenuCursor (SpriteAnimationConfiguration spriteAnimationConfiguration) {
        super(spriteAnimationConfiguration);
    }


    public GUIComponent getSelectedMenuTile() {
        return selectedMenuTile;
    }

    public void setSelectedMenuTile(GUIComponent selectedMenuTile) {
        this.selectedMenuTile = selectedMenuTile;
    }

    public int getxDistanceToKeep() {
        return xDistanceToKeep;
    }

}
