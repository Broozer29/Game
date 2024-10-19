package net.riezebos.bruus.tbd;

import javafx.application.Platform;
import net.riezebos.bruus.tbd.controllerInput.ConnectedControllers;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.visuals.audiodata.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageDatabase;

import java.awt.*;

public class Main {
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
		// Load LARGE animations so it doesn't lag upon creation of them
		Enemy shuriken = EnemyCreator.createEnemy(EnemyEnums.Shuriken, 0, 0, Direction.LEFT,
				EnemyEnums.Shuriken.getDefaultScale(), EnemyEnums.Shuriken.getMovementSpeed(), EnemyEnums.Shuriken.getMovementSpeed(), MovementPatternSize.SMALL, false);

		Enemy spaceStation = EnemyCreator.createEnemy(EnemyEnums.SpaceStationBoss, 0, 0, Direction.LEFT,
				EnemyEnums.SpaceStationBoss.getDefaultScale(), EnemyEnums.SpaceStationBoss.getMovementSpeed(), EnemyEnums.SpaceStationBoss.getMovementSpeed(), MovementPatternSize.SMALL, false);

	}
}
