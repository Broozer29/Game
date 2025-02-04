package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.LevelLength;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.boards.ShopBoard;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicMediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;


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

    @Override
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
            case OpenClassSelectWindow:
                boardManager.menuToClassSelection();
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
                AudioManager.getInstance().setMusicMediaPlayer(MusicMediaPlayer.iTunesMacOS);
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
                handleShopRefresh();
                break;
            case SelectCaptainClass:
                PlayerStats.getInstance().setPlayerClass(PlayerClass.Captain);
                BoardManager.getInstance().getClassSelectionBoard().recreateCursor();
                BoardManager.getInstance().getClassSelectionBoard().addCursorAnimation();
                break;
            case SelectFireFighterClass:
                PlayerStats.getInstance().setPlayerClass(PlayerClass.FireFighter);
                BoardManager.getInstance().getClassSelectionBoard().recreateCursor();
                BoardManager.getInstance().getClassSelectionBoard().addCursorAnimation();
                break;
            default:
                System.out.println("Unimplemented functionality");
                OnScreenTextManager.getInstance().addText("Unimplemented menu functionality in menubutton",400,400);
                break;
        }

    }

    private void handleShopRefresh () {
        PlayerInventory inventory = PlayerInventory.getInstance();
        ShopManager shopManager = ShopManager.getInstance();
        ShopBoard shopBoard = BoardManager.getInstance().getShopBoard();

        if (shopManager.getFreeRefreshessLeft() > 0) {
            shopBoard.rerollShop();
            shopManager.spendFreeReroll();
            if (shopManager.getFreeRefreshessLeft() < 1) {
                shopBoard.remakeShopRerollText(); //Only refresh the "FREE" text with the actual cost once we run out of free refreshes
            }
        } else {
            int rerollCost = shopManager.getRerollCost();
            if (inventory.getCashMoney() >= rerollCost) {
                shopBoard.rerollShop();
                inventory.spendCashMoney(rerollCost);
            } else {
                AudioManager.getInstance().addAudio(AudioEnums.NotEnoughMinerals);
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
