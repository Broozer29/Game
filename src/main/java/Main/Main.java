package Main;

import java.awt.EventQueue;

import javax.swing.JFrame;

import Main.Data.DataClass;
import database.DatabaseConnection;

public class Main extends JFrame {

	private DataClass data;
	private DatabaseConnection connection;
	private MenuBoard mBoard;
	
	public Main() {
		
		data = DataClass.getInstance();
		initMainMenu();
		//initGame();
	}
	
	private void initMainMenu() {
		mBoard = new MenuBoard();
		setResizable(false);

		setTitle("Test");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(data.getWindowWidth(), data.getWindowHeight());
		add(mBoard);

		//initGame();
	}

	private void initGame() {
		remove(mBoard);
		add(new GameBoard());

	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Main ex = new Main();
				ex.setVisible(true);
			}
		});
	}
}
