package guiboards;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

import VisualAndAudioData.DataClass;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.audio.enums.AudioEnums;
import guiboards.boards.*;

import java.io.IOException;

public class BoardManager extends JFrame {

	private DataClass data = DataClass.getInstance();
	private MenuBoard menuBoard;
	private GameBoard gameBoard;
	private ShopBoard shopBoard;
	private static BoardManager instance = new BoardManager();
	private AudioManager audioManager = AudioManager.getInstance();

	private JPanel currentBoard = null;

	private BoardManager() {
		setResizable(false);
		setTitle("Alpha version 0.1");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(data.getWindowWidth(), data.getWindowHeight());
		menuBoard = new MenuBoard();
		gameBoard = new GameBoard();
		shopBoard = new ShopBoard();
	}

	public static BoardManager getInstance() {
		return instance;
	}

	private void playMenuMusic() {
		if (audioManager.getBackgroundMusic() == null || audioManager.getBackgroundMusic().getAudioType() != AudioEnums.mainmenu) {
			audioManager.stopMusicAudio();
//			System.out.println("Muted background music");
			try {
				audioManager.playBackgroundMusic(AudioEnums.mainmenu, true);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void playShopMenuMusic(){
		if (audioManager.getBackgroundMusic() == null || audioManager.getBackgroundMusic().getAudioType().equals(AudioEnums.Lemmino_Firecracker)) {
			audioManager.stopMusicAudio();
//			System.out.println("Muted background music");
			try {
				audioManager.playBackgroundMusic(AudioEnums.Lemmino_Firecracker, true);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void initMainMenu() {
		audioManager.stopMusicAudio();
		playMenuMusic();
		changeMenuScreen(menuBoard);
		menuBoard.recreateWindow();
		menuBoard.getTimer().restart();
	}

	public void initGame() {
		audioManager.stopMusicAudio();
		changeMenuScreen(gameBoard);
		menuBoard.getTimer().stop();
		gameBoard.startGame();
	}


	public void gameToMainMenu() {
		audioManager.stopMusicAudio();
		gameBoard.resetGame();
		gameBoard.getTimer().stop();
		changeMenuScreen(menuBoard);
		menuBoard.recreateWindow();
		menuBoard.getTimer().restart();
		menuBoard.resetLastMoveTime();
		playMenuMusic();
	}

	public void openShopWindow(){
		audioManager.stopMusicAudio();
		playShopMenuMusic();
		changeMenuScreen(shopBoard);
		gameBoard.getTimer().stop();
		menuBoard.getTimer().stop();
		shopBoard.createWindow();
		shopBoard.getTimer().restart();
	}

	private void changeMenuScreen(JPanel newBoard) {
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
