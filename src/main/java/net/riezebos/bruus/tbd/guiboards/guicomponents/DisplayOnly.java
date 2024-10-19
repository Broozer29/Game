package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

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
