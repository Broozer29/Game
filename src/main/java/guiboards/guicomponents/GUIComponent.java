package guiboards.guicomponents;

import VisualAndAudioData.image.ImageEnums;
import guiboards.boardEnums.MenuFunctionEnums;
import guiboards.boardEnums.MenuObjectEnums;
import visualobjects.Sprite;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class GUIComponent extends Sprite implements Actionable{

    protected SpriteAnimation animation;
    protected MenuFunctionEnums menuFunctionality;
    private MenuObjectEnums menuObjectType;
    protected String descriptionOfComponent; //The description that gets shown in the description box


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
        this.imageType = newImage;
        this.spriteConfiguration.setImageType(newImage);
        super.loadImage(imageType);
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

}
