package menuscreens;

import javax.swing.JFrame;

import data.DataClass;
//import database.DatabaseConnection;
import menuscreens.boards.GameBoard;
import menuscreens.boards.MenuBoard;
import menuscreens.boards.UserSelectionBoard;

public class BoardManager extends JFrame {

	private DataClass data = DataClass.getInstance();
//	private DatabaseConnection connection;
	private MenuBoard mBoard;
	private GameBoard gBoard;
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
		add(mBoard);
	}

	public void initGame() {
		remove(mBoard);
		mBoard.revalidate();
		gBoard = new GameBoard();
		add(gBoard);
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

}
