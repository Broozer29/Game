package menuscreens;

import javax.swing.JFrame;

import database.DatabaseConnection;
import gamedata.DataClass;
import gamedata.image.ImageDatabase;
//import database.DatabaseConnection;
import menuscreens.boards.GameBoard;
import menuscreens.boards.MenuBoard;
import menuscreens.boards.UserSelectionBoard;

public class BoardManager extends JFrame {

	private DataClass data = DataClass.getInstance();
	private DatabaseConnection connection;
	private MenuBoard mBoard;
	private GameBoard gBoard = new GameBoard();
	private UserSelectionBoard usBoard;
	private static BoardManager instance = new BoardManager();

	private BoardManager() {
		setResizable(false);
		setTitle("Test");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(data.getWindowWidth(), data.getWindowHeight());
	}

	public static BoardManager getInstance() {
		return instance;
	}

	public void initMainMenu() {
		mBoard = new MenuBoard();
		ImageDatabase memorizer = ImageDatabase.getInstance();
		add(mBoard);
	}

	public void initGame() {
		remove(mBoard);
		mBoard.revalidate();
		add(gBoard);
		gBoard.startGame();
		gBoard.revalidate();
		gBoard.requestFocus();
		repaint();
	}

	public void initUserSelection() {
		remove(mBoard);
		mBoard.revalidate();
		usBoard = new UserSelectionBoard();
		add(usBoard);
		usBoard.revalidate();
		usBoard.requestFocus();
		repaint();
	}

	public void userSelectionToMainMenu() {
		remove(usBoard);
		usBoard.revalidate();
		add(mBoard);
		mBoard.revalidate();
		mBoard.requestFocus();
		repaint();
	}
	
	public void gameToMainMenu() {
		gBoard.resetGame();
		gBoard.revalidate();
		remove(gBoard);
		add(mBoard);
		mBoard.revalidate();
		mBoard.requestFocus();
		mBoard.getTimer().restart();
		repaint();
	}

}
