package net.riezebos.bruus.tbd.guiboards;

import net.riezebos.bruus.tbd.guiboards.boards.GameBoard;
import net.riezebos.bruus.tbd.guiboards.boards.MenuBoard;
import net.riezebos.bruus.tbd.guiboards.boards.ShopBoard;
import net.riezebos.bruus.tbd.visuals.audiodata.DataClass;
import net.riezebos.bruus.tbd.visuals.audiodata.audio.AudioManager;
import net.riezebos.bruus.tbd.visuals.audiodata.audio.enums.AudioEnums;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class BoardManager extends JFrame {

    private DataClass data = DataClass.getInstance();
    private MenuBoard menuBoard;
    private GameBoard gameBoard;
    private ShopBoard shopBoard;
    private static BoardManager instance = new BoardManager();
    private AudioManager audioManager = AudioManager.getInstance();

    private JPanel currentBoard = null;

    private BoardManager () {
        setResizable(false);
        setTitle("Alpha version 0.1");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(data.getWindowWidth(), data.getWindowHeight());
        menuBoard = new MenuBoard();
        gameBoard = new GameBoard();
        shopBoard = new ShopBoard();
    }

    public static BoardManager getInstance () {
        return instance;
    }

    private void playMenuMusic () {
        audioManager.stopMusicAudio();
        try {
            audioManager.playDefaultBackgroundMusic(AudioEnums.mainmenu, true);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

    }

    private void playShopMenuMusic () {
        audioManager.stopMusicAudio();
        try {
            audioManager.playDefaultBackgroundMusic(AudioEnums.Lemmino_Firecracker, true);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void initMainMenu () {
        audioManager.stopMusicAudio();
        playMenuMusic();
        changeMenuScreen(menuBoard);
        menuBoard.recreateWindow();
        menuBoard.getTimer().restart();
    }

    public void initGame () {
        audioManager.stopMusicAudio();
        changeMenuScreen(gameBoard);
        menuBoard.getTimer().stop();
        gameBoard.startGame();
    }


    public void gameToMainMenu () {
        audioManager.stopMusicAudio();
        gameBoard.resetGame();
        gameBoard.getTimer().stop();
        changeMenuScreen(menuBoard);
        menuBoard.recreateWindow();
        menuBoard.getTimer().restart();
        playMenuMusic();
    }

    public void openShopWindow () {
        audioManager.stopMusicAudio();
        playShopMenuMusic();
        changeMenuScreen(shopBoard);
        gameBoard.getTimer().stop();
        menuBoard.getTimer().stop();
        shopBoard.createWindow();
        shopBoard.getTimer().restart();
    }

    private void changeMenuScreen (JPanel newBoard) {
        if (currentBoard != null) {
            remove(currentBoard);
            currentBoard.revalidate();
        }
        currentBoard = newBoard;
        add(currentBoard);
        currentBoard.revalidate();
        currentBoard.requestFocus();
        repaint();
    }

    public ShopBoard getShopBoard () {
        return shopBoard;
    }
}
