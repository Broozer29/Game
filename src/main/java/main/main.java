package main;

import java.awt.EventQueue;

import VisualAndAudioData.audio.AudioDatabase;
import VisualAndAudioData.image.ImageDatabase;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyCreator;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.movement.Direction;
import game.movement.MovementPatternSize;
import guiboards.BoardManager;

public class main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				AudioDatabase loadingAudioInstance = AudioDatabase.getInstance();
				ImageDatabase loadingImageInstance = ImageDatabase.getInstance();
				BoardManager ex = BoardManager.getInstance();

				//Load a LONG animation so it doesnt lag
				preloadThings();
				ex.initMainMenu();
				ex.setVisible(true);
			}
		});
	}

	private static void preloadThings(){
		Enemy shuriken = EnemyCreator.createEnemy(EnemyEnums.Shuriken, 0, 0, Direction.LEFT,
				EnemyEnums.Shuriken.getDefaultScale(), EnemyEnums.Shuriken.getMovementSpeed(), EnemyEnums.Shuriken.getMovementSpeed(), MovementPatternSize.SMALL, false);

	}
}
