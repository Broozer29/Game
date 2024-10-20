package guiboards.guicomponents;

import VisualAndAudioData.DataClass;
import VisualAndAudioData.audio.AudioDatabase;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.enums.MusicMediaPlayer;
import game.items.PlayerInventory;
import game.managers.OnScreenTextManager;
import game.managers.ShopManager;
import game.level.LevelManager;
import game.level.enums.LevelDifficulty;
import game.level.enums.LevelLength;
import guiboards.BoardManager;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class MenuButton extends GUIComponent {

    private LevelDifficulty levelDifficulty;

    private LevelLength levelLength;

    public MenuButton (SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
    }

    public MenuButton (SpriteAnimationConfiguration spriteAnimationConfiguration) {
        super(spriteAnimationConfiguration.getSpriteConfiguration());
        this.animation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    public void activateComponent () {
        AudioDatabase.getInstance().updateGameTick();
        BoardManager boardManager = BoardManager.getInstance();
        switch (this.menuFunctionality) {
            case Start_Game:
                boardManager.initGame();
                break;
            case Return_To_Main_Menu:
                boardManager.initMainMenu();
                break;
            case Open_Inventory:
                boardManager.getShopBoard().setShowInventory(!boardManager.getShopBoard().isShowInventory());
                break;
            case Open_Shop_Window:
                boardManager.openShopWindow();
                break;
            case SelectSongDifficulty:
                changeLevelDifficulty();
                break;
            case SelectSongLength:
                changeLevelLength();
                break;
            case SelectMacOSMediaPlayer:
                AudioManager.getInstance().setMusicMediaPlayer(MusicMediaPlayer.MacOS);
                OnScreenTextManager.getInstance().addText("Changed to using APPLE MUSIC for gameplay",
                        DataClass.getInstance().getWindowWidth() / 2,
                        DataClass.getInstance().getWindowHeight() / 2);
                break;
            case SelectDefaultMediaPlayer:
                AudioManager.getInstance().setMusicMediaPlayer(MusicMediaPlayer.Default);
                OnScreenTextManager.getInstance().addText("Changed to using LOCALLY DOWNLOADED MUSIC for gameplay",
                        DataClass.getInstance().getWindowWidth() / 2,
                        DataClass.getInstance().getWindowHeight() / 2);
                break;
            case RerollShop:
                PlayerInventory inventory = PlayerInventory.getInstance();
                int rerollCost = ShopManager.getInstance().getRerollCost();
                if(inventory.getCashMoney() >= rerollCost) {
                    boardManager.getShopBoard().rerollShop();
                    inventory.spendCashMoney(rerollCost);
                } else {
                    try {
                        AudioManager.getInstance().addAudio(AudioEnums.NotEnoughMinerals);
                    } catch (UnsupportedAudioFileException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
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

    public LevelDifficulty getLevelDifficulty () {
        return levelDifficulty;
    }

    public void setLevelDifficulty (LevelDifficulty levelDifficulty) {
        this.levelDifficulty = levelDifficulty;
    }

    public LevelLength getLevelLength () {
        return levelLength;
    }

    public void setLevelLength (LevelLength levelLength) {
        this.levelLength = levelLength;
    }
}
