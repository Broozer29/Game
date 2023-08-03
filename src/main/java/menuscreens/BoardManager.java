package menuscreens;

import javax.swing.JFrame;
import javax.swing.JPanel;

import database.DatabaseConnection;
import gamedata.DataClass;
//import database.DatabaseConnection;
import menuscreens.boards.GameBoard;
import menuscreens.boards.LevelSelectionBoard;
import menuscreens.boards.MenuBoard;
import menuscreens.boards.TalentSelectionBoard;
import menuscreens.boards.UserSelectionBoard;

public class BoardManager extends JFrame {

	private DataClass data = DataClass.getInstance();
	private DatabaseConnection connection;
	private MenuBoard menuBoard;
	private GameBoard gameBoard;
	private UserSelectionBoard usBoard;
	private TalentSelectionBoard talentBoard;
	private LevelSelectionBoard levelSelectionBoard;
	private static BoardManager instance = new BoardManager();

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

	public void initMainMenu() {
		changeMenuScreen(menuBoard);
		menuBoard.recreateWindow();
	}

	public void initGame() {
		changeMenuScreen(gameBoard);
		gameBoard.startGame();
	}
	
	public void initLevelSelectionBoard() {
		changeMenuScreen(talentBoard);
		talentBoard.recreateWindow();
	}

	public void initTalentSelectionBoard() {
		changeMenuScreen(levelSelectionBoard);
		levelSelectionBoard.recreateWindow();
	}

	public void gameToMainMenu() {
		gameBoard.resetGame();
		changeMenuScreen(menuBoard);
		menuBoard.recreateWindow();
		menuBoard.getTimer().restart();
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
