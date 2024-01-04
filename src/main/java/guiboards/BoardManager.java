package guiboards;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

import VisualAndAudioData.DataClass;
import VisualAndAudioData.audio.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import guiboards.boards.GameBoard;
import guiboards.boards.LevelSelectionBoard;
import guiboards.boards.MenuBoard;
import guiboards.boards.TalentSelectionBoard;
import guiboards.boards.UserSelectionBoard;

public class BoardManager extends JFrame {

	private DataClass data = DataClass.getInstance();
	private MenuBoard menuBoard;
	private GameBoard gameBoard;
	private UserSelectionBoard usBoard;
	private TalentSelectionBoard talentBoard;
	private LevelSelectionBoard levelSelectionBoard;
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
		talentBoard = new TalentSelectionBoard();
		levelSelectionBoard = new LevelSelectionBoard();
	}

	public static BoardManager getInstance() {
		return instance;
	}

	private void playMenuMusic() {
		if (audioManager.getBackgroundMusic() == null) {
			try {
				audioManager.playMusicAudio(AudioEnums.mainmenu);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void initMainMenu() {
		playMenuMusic();
		changeMenuScreen(menuBoard);
		menuBoard.recreateWindow();

	}

	public void initGame() {
		audioManager.stopMusicAudio();
		changeMenuScreen(gameBoard);
		gameBoard.startGame();
	}

	public void initLevelSelectionBoard() {
		playMenuMusic();
		changeMenuScreen(talentBoard);
		talentBoard.recreateWindow();
	}

	public void initTalentSelectionBoard() {
		playMenuMusic();
		changeMenuScreen(levelSelectionBoard);
		levelSelectionBoard.recreateWindow();
	}

	public void gameToMainMenu() {
		gameBoard.resetGame();
		changeMenuScreen(menuBoard);
		menuBoard.recreateWindow();
		menuBoard.getTimer().restart();
		playMenuMusic();
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

}
