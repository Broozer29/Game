package guiboards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import VisualAndAudioData.audio.AudioDatabase;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.image.ImageEnums;
import game.items.enums.ItemRarityEnums;
import game.items.PlayerInventory;
import game.items.enums.ItemEnums;
import game.spawner.LevelManager;
import game.spawner.enums.LevelDifficulty;
import game.spawner.enums.LevelLength;
import game.util.ItemDescriptionRetriever;
import guiboards.boardEnums.MenuFunctionEnums;
import guiboards.boardEnums.MenuObjectEnums;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class MenuObjectCollection {

    private List<MenuObjectPart> menuImages = new ArrayList<>();
    private List<MenuObjectPart> menuTextImages = new ArrayList<>();
    private int xCoordinate;
    private int yCoordinate;
    private String text;
    private MenuObjectEnums menuObjectType;
    private MenuFunctionEnums menuFunctionality;
    private ImageEnums imageType;
    private float scale;
    private MenuItemInformation menuItemInformation;

    private LevelDifficulty levelDifficulty;

    private LevelLength levelLength;

    public MenuObjectCollection (int xCoordinate, int yCoordinate, float scale, String text,
                                 MenuObjectEnums menuObjectType, MenuFunctionEnums menuFunctionality) {
        setXCoordinate(xCoordinate);
        setYCoordinate(yCoordinate);
        this.text = text;
        this.menuObjectType = menuObjectType;
        this.menuFunctionality = menuFunctionality;
        this.scale = scale;
        this.imageType = MenuFunctionImageCoupler.getInstance().getImageByMenuType(menuObjectType);
        initMenuImages();
    }

    private void initMenuImages () {
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration();

        if (menuObjectType == MenuObjectEnums.Text) {
            initMenuText();
        } else {
            if (menuObjectType == MenuObjectEnums.Highlight_Animation) {
                spriteConfiguration.setImageType(ImageEnums.Invisible);
                addNewTile(spriteConfiguration, true);
            } else if (isShopItemType(menuObjectType)) {
                initShopItem(spriteConfiguration);
            } else {
                addNewTile(spriteConfiguration, false);
            }
        }
    }

    private boolean isShopItemType (MenuObjectEnums type) {
        return type == MenuObjectEnums.CommonItem
                || type == MenuObjectEnums.RareItem
                || type == MenuObjectEnums.LegendaryItem;
    }

    private SpriteConfiguration createSpriteConfiguration () {
        SpriteConfiguration config = new SpriteConfiguration();
        config.setxCoordinate(xCoordinate);
        config.setyCoordinate(yCoordinate);
        config.setScale(scale);
        config.setImageType(imageType);
        return config;
    }

    private void addNewTile (SpriteConfiguration config, boolean isAnimation) {
        MenuObjectPart newTile = new MenuObjectPart(config);
        if (isAnimation) {
            newTile.setTileAnimation(imageType);
        }
        menuImages.add(newTile);
    }

    public void setNewImage (ItemEnums item) {
        this.imageType = item.getItemIcon();
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration();
        this.menuImages.clear();
        MenuObjectPart newTile = new MenuObjectPart(spriteConfiguration);
        newTile.setImageDimensions(50, 50);
        this.menuImages.add(newTile);
    }


    private void initShopItem (SpriteConfiguration spriteConfiguration) {
        ItemRarityEnums randomItemRarity = null;
        switch (this.menuObjectType) {
            case CommonItem -> randomItemRarity = ItemRarityEnums.getRandomCommonItemSlot();
            case RareItem -> randomItemRarity = ItemRarityEnums.getRandomRareItemSlot();
            case LegendaryItem -> randomItemRarity = ItemRarityEnums.Legendary;
        }

        ItemEnums item = ItemEnums.getRandomItemByRarity(randomItemRarity);
        this.imageType = item.getItemIcon();
        spriteConfiguration.setImageType(imageType);
        String itemDesc = ItemDescriptionRetriever.getDescriptionOfItem(item);
        this.menuItemInformation = new MenuItemInformation(item, randomItemRarity, itemDesc, true, 50);
        MenuObjectPart newTile = new MenuObjectPart(spriteConfiguration);
        newTile.setImageDimensions(50, 50);
        this.menuImages.add(newTile);
    }

    private void lockItemInShop () {
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration();
        spriteConfiguration.setImageType(ImageEnums.Test_Image);
        this.imageType = ImageEnums.Test_Image;
        this.menuImages.clear();
        MenuObjectPart newTile = new MenuObjectPart(spriteConfiguration);
        newTile.setImageDimensions(50, 50);
        this.menuImages.add(newTile);

    }

    public void menuTileAction () throws UnsupportedAudioFileException, IOException {
        AudioDatabase.getInstance().updateGameTick();
        switch (this.menuFunctionality) {
            case Start_Game:
                BoardManager.getInstance().initGame();
                break;
            case Select_Talent_Selection_Board:
                BoardManager.getInstance().initTalentSelectionBoard();
                break;
            case Return_To_Main_Menu:
                BoardManager.getInstance().initMainMenu();
                break;
            case NONE:
                break;
            case Open_Inventory:
                if (BoardManager.getInstance().getShopBoard().isShowInventory()) {
                    BoardManager.getInstance().getShopBoard().setShowInventory(false);
                } else {
                    BoardManager.getInstance().getShopBoard().setShowInventory(true);
                }
                break;
            case Open_Shop_Window:
                BoardManager.getInstance().openShopWindow();
                break;
            case PurchaseItem:
                if (menuItemInformation.isAvailable() && menuItemInformation.canAfford()) {
                    AudioManager.getInstance().addAudio(AudioEnums.Power_Up_Acquired);
                    PlayerInventory.getInstance().addItem(menuItemInformation.getItem());
                    menuItemInformation.setAvailable(false);
                    PlayerInventory.getInstance().spendCashMoney(menuItemInformation.getCost());
                    lockItemInShop();
                } else {
                    AudioManager.getInstance().addAudio(AudioEnums.Firewall);
                }
                break;
            case SelectSongDifficulty:
                changeLevelDifficulty();
                break;
            case SelectSongLength:
                changeLevelLength();
                break;
            default:
                System.out.println("Unimplemented MenuObject behaviour was attempted!");
                break;
        }
    }


    private void initMenuText () {
        int startingXCoordinate = this.xCoordinate;
        int startingYCoordinate = this.yCoordinate;
        int kernelDistance = (int) Math.ceil(10 * scale);

        for (int i = 0; i < text.length(); i++) {
            char stringChar = text.charAt(i);
            if (stringChar != ' ') {
                ImageEnums imageType = ImageEnums.fromChar(stringChar); // convert char to Letter enum
                SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
                spriteConfiguration.setImageType(imageType);
                spriteConfiguration.setxCoordinate(startingXCoordinate + (kernelDistance * i));
                spriteConfiguration.setyCoordinate(startingYCoordinate);
                spriteConfiguration.setScale(scale);

                MenuObjectPart newTile = new MenuObjectPart(spriteConfiguration);
                menuTextImages.add(newTile);
            }
        }
    }

    private void changeLevelDifficulty () {
        if (this.levelDifficulty != null) {
            LevelManager.getInstance().setCurrentLevelDifficulty(this.levelDifficulty);
        }
    }

    private void changeLevelLength () {
        if (this.levelLength != null) {
            LevelManager.getInstance().setCurrentLevelLength(this.levelLength);
        }
    }

    public void addMenuPart (MenuObjectPart menuObjectPart) {
        this.menuImages.add(menuObjectPart);
    }

    public int getXCoordinate () {
        return xCoordinate;
    }

    public void setXCoordinate (int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate () {
        return yCoordinate;
    }

    public void setYCoordinate (int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public List<MenuObjectPart> getMenuImages () {
        return menuImages;
    }

    public MenuFunctionEnums getMenuFunction () {
        return this.menuFunctionality;
    }

    public MenuObjectEnums getMenuObjectType () {
        return this.menuObjectType;
    }

    public void changeImage (ImageEnums newImage) {
        this.imageType = newImage;
        menuImages = new ArrayList<MenuObjectPart>();
        initMenuImages();
    }

    @Override
    public String toString () {
        return "MenuObject{" +
                "menuTiles=" + menuImages +
                ", xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", text='" + text + '\'' +
                ", menuObjectType=" + menuObjectType +
                ", menuFunctionality=" + menuFunctionality +
                ", imageType=" + imageType +
                ", scale=" + scale +
                '}';
    }

    public String getText () {
        return text;
    }

    public float getScale () {
        return scale;
    }

    public MenuItemInformation getMenuItemInformation () {
        return menuItemInformation;
    }

    public LevelDifficulty getLevelDifficulty () {
        return levelDifficulty;
    }

    public void setLevelDifficulty (LevelDifficulty levelDifficulty) {
        if (this.menuFunctionality == MenuFunctionEnums.SelectSongDifficulty) {
            this.levelDifficulty = levelDifficulty;
        }
    }

    public LevelLength getLevelLength () {
        return levelLength;
    }

    public void setLevelLength (LevelLength levelLength) {
        if (this.menuFunctionality == MenuFunctionEnums.SelectSongLength) {
            this.levelLength = levelLength;
        }
    }

    public List<MenuObjectPart> getMenuTextImages () {
        return menuTextImages;
    }
}