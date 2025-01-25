package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.guiboards.GUIComponentItemInformation;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuObjectEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class GUIComponent extends Sprite implements Actionable{

    protected SpriteAnimation animation;
    protected MenuFunctionEnums menuFunctionality;
    private MenuObjectEnums menuObjectType;
    protected String descriptionOfComponent; //The description that gets shown in the description box
    protected GUIComponentItemInformation shopItemInformation;


    public GUIComponent (SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
    }

    public GUIComponent (SpriteAnimationConfiguration spriteAnimationConfiguration) {
        super(spriteAnimationConfiguration.getSpriteConfiguration());
        this.animation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    public void activateComponent(){
        //Exists to be overwritten
    }

    public SpriteAnimation getAnimation(){
        return animation;
    }

    public String getDescriptionOfComponent () {
        return descriptionOfComponent;
    }

    public void setDescriptionOfComponent (String descriptionOfComponent) {
        this.descriptionOfComponent = descriptionOfComponent;
    }

    public void setNewImage(ImageEnums newImage){
        this.imageEnum = newImage;
        this.spriteConfiguration.setImageType(newImage);
        super.setImage(imageEnum);
    }

    public MenuFunctionEnums getMenuFunctionality () {
        return menuFunctionality;
    }

    public void setMenuFunctionality (MenuFunctionEnums menuFunctionality) {
        this.menuFunctionality = menuFunctionality;
    }

    public MenuObjectEnums getMenuObjectType () {
        return menuObjectType;
    }

    public void setMenuObjectType (MenuObjectEnums menuObjectType) {
        this.menuObjectType = menuObjectType;
    }

    public GUIComponentItemInformation getShopItemInformation () {
        return shopItemInformation;
    }

    public void setShopItemInformation (GUIComponentItemInformation shopItemInformation) {
        this.shopItemInformation = shopItemInformation;
    }
}
