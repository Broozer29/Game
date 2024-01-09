package main;

import java.awt.EventQueue;

import VisualAndAudioData.audio.AudioDatabase;
import VisualAndAudioData.image.ImageDatabase;
import guiboards.BoardManager;

public class main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				AudioDatabase loadingAudioInstance = AudioDatabase.getInstance();
				ImageDatabase loadingImageInstance = ImageDatabase.getInstance();
				BoardManager ex = BoardManager.getInstance();
				ex.initMainMenu();
				ex.setVisible(true);
				System.out.println("java.library.path: " + System.getProperty("java.library.path"));
//				System.out.println(System.getProperty("java.library.path"));
			}
		});
	}
}
