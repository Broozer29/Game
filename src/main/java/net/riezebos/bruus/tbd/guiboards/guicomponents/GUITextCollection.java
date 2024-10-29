package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visuals.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GUITextCollection implements Actionable {

    private String textToDisplayAsGUIComponents;
    private List<GUIComponent> componentsBelongingToCollection = new ArrayList<>();

    private MenuFunctionEnums menuFunctionEnums;
    private int startingXCoordinate;
    private int startingYCoordinate;

    private float scale;

    public GUITextCollection (int startingXCoordinate, int startingYCoordinate, String textToDisplayAsGUIComponents){
        this.startingXCoordinate = startingXCoordinate;
        this.startingYCoordinate = startingYCoordinate;
        this.textToDisplayAsGUIComponents = textToDisplayAsGUIComponents;
        initMenuText();
    }

    private void initMenuText() {
        int kernelDistance = (int) Math.ceil(10);
        for (int i = 0; i < textToDisplayAsGUIComponents.length(); i++) {
            char stringChar = textToDisplayAsGUIComponents.charAt(i);
            if (stringChar != ' ') {
                ImageEnums imageType = ImageEnums.fromChar(stringChar); // convert char to Letter enum
                SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
                spriteConfiguration.setImageType(imageType);
                spriteConfiguration.setxCoordinate(startingXCoordinate + (kernelDistance * i));
                spriteConfiguration.setyCoordinate(startingYCoordinate);
                spriteConfiguration.setScale(1);

                DisplayOnly letter = new DisplayOnly(spriteConfiguration);
                componentsBelongingToCollection.add(letter);
            }
        }
    }

    public void setText (String text) {
        this.textToDisplayAsGUIComponents = text;
        initMenuText();
    }


    public void addComponentToCollection(GUIComponent component){
        if(!this.componentsBelongingToCollection.contains(component)){
            componentsBelongingToCollection.add(component);
        }
    }

    public void removeComponentFromCollection(GUIComponent component){
        if(this.componentsBelongingToCollection.contains(component)){
            this.componentsBelongingToCollection.remove(component);
        }
    }

    public List<GUIComponent> getComponents () {
        return componentsBelongingToCollection;
    }

    @Override
    public void activateComponent () {
        for(GUIComponent component : componentsBelongingToCollection){
            component.activateComponent();
        }
    }

    public void setMenuFunctionality (MenuFunctionEnums menuFunctionalityToImplement){
        this.menuFunctionEnums = menuFunctionalityToImplement;
        int lastComponentIndex = 0;

        GUIComponent firstComponent = componentsBelongingToCollection.get(lastComponentIndex);
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(firstComponent.getXCoordinate());
        spriteConfiguration.setyCoordinate(firstComponent.getYCoordinate());
        spriteConfiguration.setScale(firstComponent.getScale());
        spriteConfiguration.setImageType(firstComponent.getSpriteConfiguration().getImageType());


        MenuButton newFirstcomponent = new MenuButton(spriteConfiguration);
        newFirstcomponent.setDescriptionOfComponent(firstComponent.getDescriptionOfComponent());
        newFirstcomponent.setMenuFunctionality(menuFunctionalityToImplement);

        componentsBelongingToCollection.set(lastComponentIndex, newFirstcomponent);
    }

    public float getScale () {
        return scale;
    }

    public void setScale (float scale) {
        if(scale != this.scale) {
            this.scale = scale;
            initMenuText();
        }
    }

    public MenuFunctionEnums getMenuFunctionEnums () {
        return menuFunctionEnums;
    }
}
