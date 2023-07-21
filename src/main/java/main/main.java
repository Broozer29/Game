package main;

import java.awt.EventQueue;

import gamedata.audio.AudioDatabase;
import gamedata.image.ImageDatabase;
import menuscreens.BoardManager;

public class main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				AudioDatabase loadingAudioInstance = AudioDatabase.getInstance();
				ImageDatabase loadingImageInstance = ImageDatabase.getInstance();
				BoardManager ex = BoardManager.getInstance();
				ex.initMainMenu();
				ex.setVisible(true);
			}
		});
	}
}
