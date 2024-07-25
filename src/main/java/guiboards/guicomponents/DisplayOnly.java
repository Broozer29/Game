package guiboards.guicomponents;

import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class DisplayOnly extends GUIComponent{

    public DisplayOnly (SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
    }

    public DisplayOnly(SpriteAnimationConfiguration spriteAnimationConfiguration){
        super(spriteAnimationConfiguration.getSpriteConfiguration());
        this.animation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    public void activateComponent(){
        //Does nothing as it's display only, use button for activates
    }


}
