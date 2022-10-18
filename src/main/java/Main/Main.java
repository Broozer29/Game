package Main;

import java.awt.EventQueue;

import menuscreens.BoardManager;

public class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				BoardManager ex = BoardManager.getInstance();
				ex.initMainMenu();
				ex.setVisible(true);
			}
		});
	}
}
