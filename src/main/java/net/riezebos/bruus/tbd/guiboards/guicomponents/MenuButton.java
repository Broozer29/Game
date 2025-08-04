package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameMode;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.MiniBossConfig;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.Boon;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.boardcreators.BoonSelectionBoardCreator;
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
    private MiniBossConfig miniBossConfig;

    public MenuButton(SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
    }

    public MenuButton(SpriteAnimationConfiguration spriteAnimationConfiguration) {
        super(spriteAnimationConfiguration.getSpriteConfiguration());
        this.animation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    @Override
    public void activateComponent() {
        AudioDatabase.getInstance().updateGameTick();
        BoardManager boardManager = BoardManager.getInstance();
        if (this.menuFunctionality == null) {
            OnScreenTextManager.getInstance().addText("No menu functionality found!", DataClass.getInstance().getWindowWidth() / 2, DataClass.getInstance().getWindowHeight() / 2);
            return;
        }
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
            case OpenBoonSelectionBoard:
                boardManager.openUpgradeSelectionScreen();
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
                SaveManager.getInstance().exportCurrentSave();
                break;
            case SelectCaptainClass:
                PlayerStats.getInstance().setPlayerClass(PlayerClass.Captain);
                BoardManager.getInstance().getClassSelectionBoard().recreateCursor();
                BoardManager.getInstance().getClassSelectionBoard().addCursorAnimation();
                AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
                AudioManager.getInstance().addAudio(AudioEnums.getSelectClassAudioByClass(PlayerClass.Captain));
                break;
            case SelectFireFighterClass:
                PlayerStats.getInstance().setPlayerClass(PlayerClass.FireFighter);
                BoardManager.getInstance().getClassSelectionBoard().recreateCursor();
                BoardManager.getInstance().getClassSelectionBoard().addCursorAnimation();
                AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
                AudioManager.getInstance().addAudio(AudioEnums.getSelectClassAudioByClass(PlayerClass.FireFighter));
                break;
            case SelectCarrierClass:
                PlayerStats.getInstance().setPlayerClass(PlayerClass.Carrier);
                BoardManager.getInstance().getClassSelectionBoard().recreateCursor();
                BoardManager.getInstance().getClassSelectionBoard().addCursorAnimation();
                AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
                AudioManager.getInstance().addAudio(AudioEnums.getSelectClassAudioByClass(PlayerClass.Carrier));
                break;
            case ContinueSaveFile:
                SaveManager.getInstance().loadSaveFile();
                boardManager.openShopWindow();
                break;
            case SelectNepotism:
            case SelectClubAccess:
            case SelectCompoundWealth:
            case SelectTreasureHunter:
            case SelectBountyHunter:
            case SelectThickHide:
                if (BoonSelectionBoardCreator.getBoonByMenuFunctionality(menuFunctionality).isUnlocked()) {
                    BoonManager.getInstance().setUtilityBoon(BoonSelectionBoardCreator.getBoonByMenuFunctionality(menuFunctionality));
                }
                break;
            case UpgradeNepotism:
            case UpgradeClubAccess:
            case UpgradeCompoundWealth:
            case UpgradeTreasureHunter:
            case UpgradeBountyHunter:
            case UpgradeThickHide:
                Boon boon = BoonSelectionBoardCreator.getBoonByMenuFunctionality(menuFunctionality);
                boon.upgradeBoon();
                BoardManager.getInstance().getUpgradeSelectionBoard().recreateList();
                BoardManager.getInstance().getUpgradeSelectionBoard().recreateEmeraldText();
                break;
            case PurchaseItem:
                break; //Handled in a different class
            case SelectBossMode:
                if (GameState.getInstance().getGameMode().equals(GameMode.ManMode)) {
                    GameState.getInstance().setGameMode(GameMode.Default);
                    OnScreenTextManager.getInstance().addText("Changed to DEFAULT mode",
                            DataClass.getInstance().getWindowWidth() / 2,
                            DataClass.getInstance().getWindowHeight() / 2);
                } else {
                    GameState.getInstance().setGameMode(GameMode.ManMode);
                    OnScreenTextManager.getInstance().addText("Changed to MAN MODE",
                            DataClass.getInstance().getWindowWidth() / 2,
                            DataClass.getInstance().getWindowHeight() / 2);
                }
                break;
            default:
                System.out.println("Unimplemented functionality");
                OnScreenTextManager.getInstance().addText("Unimplemented menu functionality in menubutton", 400, 400);
                break;
        }
    }


    private void handleShopRefresh() {
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

    private void changeLevelDifficulty() {
        if (this.levelDifficulty != null) {
            LevelManager.getInstance().setCurrentLevelDifficulty(this.levelDifficulty);
        }
    }

    private void changeLevelLength() {
        if (this.miniBossConfig != null) {
            LevelManager.getInstance().setCurrentMiniBossConfig(this.miniBossConfig);
        }
    }

    public LevelDifficulty getLevelDifficulty() {
        return levelDifficulty;
    }

    public void setLevelDifficulty(LevelDifficulty levelDifficulty) {
        this.levelDifficulty = levelDifficulty;
    }

    public MiniBossConfig getMiniBossConfig() {
        return miniBossConfig;
    }

    public void setMiniBossConfig(MiniBossConfig miniBossConfig) {
        this.miniBossConfig = miniBossConfig;
    }
}
