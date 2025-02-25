package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GUITextCollection implements Actionable {

    private String textToDisplayAsGUIComponents;
    private List<GUIComponent> componentsBelongingToCollection = new ArrayList<>();

    protected MenuFunctionEnums menuFunctionEnums;
    private int startingXCoordinate;
    private int startingYCoordinate;

    private float scale;

    public GUITextCollection (int startingXCoordinate, int startingYCoordinate, String textToDisplayAsGUIComponents) {
        this.startingXCoordinate = startingXCoordinate;
        this.startingYCoordinate = startingYCoordinate;
        this.textToDisplayAsGUIComponents = textToDisplayAsGUIComponents;
        this.scale = 1;
        initMenuText();
    }

    public GUITextCollection (float startingXCoordinate, float startingYCoordinate, String textToDisplayAsGUIComponents) {
        this.startingXCoordinate = Math.round(startingXCoordinate);
        this.startingYCoordinate = Math.round(startingYCoordinate);
        this.textToDisplayAsGUIComponents = textToDisplayAsGUIComponents;
        this.scale = 1;
        initMenuText();
    }

    private void initMenuText () {
        int kernelDistance = 10;
        for (int i = 0; i < textToDisplayAsGUIComponents.length(); i++) {
            char stringChar = textToDisplayAsGUIComponents.charAt(i);
            if (stringChar != ' ') {
                ImageEnums imageType = ImageEnums.fromChar(stringChar); // convert char to Letter enum
                SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
                spriteConfiguration.setImageType(imageType);
                spriteConfiguration.setxCoordinate(startingXCoordinate + Math.round(((kernelDistance * i) * scale)));
                spriteConfiguration.setyCoordinate(startingYCoordinate);
                spriteConfiguration.setScale(scale);

                DisplayOnly letter = new DisplayOnly(spriteConfiguration);
                componentsBelongingToCollection.add(letter);
            }
        }
    }

    public void setText (String text) {
        this.componentsBelongingToCollection.clear();
        this.textToDisplayAsGUIComponents = text;
        initMenuText();
    }


    public void addComponentToCollection (GUIComponent component) {
        if (!this.componentsBelongingToCollection.contains(component)) {
            componentsBelongingToCollection.add(component);
        }
    }

    public void removeComponentFromCollection (GUIComponent component) {
        if (this.componentsBelongingToCollection.contains(component)) {
            this.componentsBelongingToCollection.remove(component);
        }
    }

    public List<GUIComponent> getComponents () {
        return componentsBelongingToCollection;
    }

    @Override
    public void activateComponent () {
        //add an animation here?
        for (GUIComponent component : componentsBelongingToCollection) {
            component.activateComponent();
        }
    }

    public void setMenuFunctionality (MenuFunctionEnums menuFunctionalityToImplement) {
        this.menuFunctionEnums = menuFunctionalityToImplement;
        int firstComponentIndex = 0;

        GUIComponent firstComponent = componentsBelongingToCollection.get(firstComponentIndex);
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(firstComponent.getXCoordinate());
        spriteConfiguration.setyCoordinate(firstComponent.getYCoordinate());
        spriteConfiguration.setScale(firstComponent.getScale());
        spriteConfiguration.setImageType(firstComponent.getSpriteConfiguration().getImageType());


        MenuButton newFirstcomponent = new MenuButton(spriteConfiguration);
        newFirstcomponent.setDescriptionOfComponent(firstComponent.getDescriptionOfComponent());
        newFirstcomponent.setMenuFunctionality(menuFunctionalityToImplement);

        componentsBelongingToCollection.set(firstComponentIndex, newFirstcomponent);
    }

    public float getScale () {
        return scale;
    }

    public void setScale (float scale) {
        this.componentsBelongingToCollection.clear();
        this.scale = scale;
        initMenuText();
    }

    public int getStartingXCoordinate () {
        return startingXCoordinate;
    }

    public void setStartingXCoordinate (int startingXCoordinate) {
        this.startingXCoordinate = startingXCoordinate;
        this.componentsBelongingToCollection.clear();
        initMenuText();
    }

    public int getWidth(){
        GUIComponent firstComponent = componentsBelongingToCollection.get(0);
        GUIComponent lastComponent = componentsBelongingToCollection.get(componentsBelongingToCollection.size() - 1);
        return (lastComponent.getXCoordinate() + lastComponent.getWidth()) - firstComponent.getXCoordinate();
    }

    public int getHeight(){
        return componentsBelongingToCollection.get(0).getHeight();
    }

    public void setCenterXCoordinate(int xCoordinate) {
        if (componentsBelongingToCollection.isEmpty()) {
            return; // No components to reposition
        }

        // Calculate the current width of the text collection
        int collectionWidth = getWidth();

        // Calculate the new starting X coordinate so that the collection is centered around xCoordinate
        int newStartingX = xCoordinate - (collectionWidth / 2);

        // Find the current left-most X position of the first component
        int currentFirstX = componentsBelongingToCollection.get(0).getXCoordinate();

        // Calculate the shift amount
        int shiftAmount = newStartingX - currentFirstX;

        // Apply the shift to all components
        for (GUIComponent component : componentsBelongingToCollection) {
            component.setXCoordinate(component.getXCoordinate() + shiftAmount);
        }

        // Update the starting X coordinate
        this.startingXCoordinate = newStartingX;
    }

}
