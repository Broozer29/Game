package main;

import java.awt.EventQueue;

import VisualAndAudioData.audio.AudioDatabase;
import VisualAndAudioData.image.ImageDatabase;
import controllerInput.ConnectedControllers;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyCreator;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.movement.Direction;
import game.movement.MovementPatternSize;
import guiboards.BoardManager;
import javafx.application.Platform;

public class main {
	public static void main(String[] args) {
		ConnectedControllers.getInstance().initController();

		Platform.startup(() -> {
			// This initializes the JavaFX application thread, which is needed for MediaPlayer
		});

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				AudioDatabase loadingAudioInstance = AudioDatabase.getInstance();
				ImageDatabase loadingImageInstance = ImageDatabase.getInstance();
				BoardManager ex = BoardManager.getInstance();

				preloadThings();
				ex.initMainMenu();
				ex.setVisible(true);
			}
		});
	}
	private static void preloadThings(){
		// Load a LARGE animation so it doesn't lag
		Enemy shuriken = EnemyCreator.createEnemy(EnemyEnums.Shuriken, 0, 0, Direction.LEFT,
				EnemyEnums.Shuriken.getDefaultScale(), EnemyEnums.Shuriken.getMovementSpeed(), EnemyEnums.Shuriken.getMovementSpeed(), MovementPatternSize.SMALL, false);

//		Enemy spaceStation = EnemyCreator.createEnemy(EnemyEnums.SpaceStationBoss, 0, 0, Direction.LEFT,
//				EnemyEnums.SpaceStationBoss.getDefaultScale(), EnemyEnums.SpaceStationBoss.getMovementSpeed(), EnemyEnums.SpaceStationBoss.getMovementSpeed(), MovementPatternSize.SMALL, false);

	}
}
