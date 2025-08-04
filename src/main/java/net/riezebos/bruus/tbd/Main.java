package net.riezebos.bruus.tbd;

import javafx.application.Platform;
import net.riezebos.bruus.tbd.controllerInput.ConnectedControllersManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.redboss.behaviour.CrossingLaserbeamsAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;

import java.awt.*;

public class Main {
	public static void main(String[] args) {
		ConnectedControllersManager.getInstance().initController();

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
				ex.getMainMenuBoard().requestFocus();
			}
		});
	}
	private static void preloadThings(){
		// Load LARGE animations so it doesn't lag upon creation of them
		EnemyEnums enemyEnum = EnemyEnums.Shuriken;
		Enemy shuriken = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
				enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed(), enemyEnum.getMovementSpeed(), MovementPatternSize.SMALL, false);
		shuriken.deleteObject();

		enemyEnum = EnemyEnums.SpaceStationBoss;
		Enemy spaceStation = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
				enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed(), enemyEnum.getMovementSpeed(), MovementPatternSize.SMALL, false);
		spaceStation.deleteObject();

		enemyEnum = EnemyEnums.ShurikenMiniBoss;
		Enemy shurikenMiniBoss = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
				enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed(), enemyEnum.getMovementSpeed(), MovementPatternSize.SMALL, false);
		shurikenMiniBoss.deleteObject();

		enemyEnum = EnemyEnums.CashCarrier;
		Enemy cashCarrier = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
				enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed(), enemyEnum.getMovementSpeed(), MovementPatternSize.SMALL, false);
		cashCarrier.deleteObject();

		enemyEnum = EnemyEnums.RedBoss;
		Enemy redBoss = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
				enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed(), enemyEnum.getMovementSpeed(), MovementPatternSize.SMALL, false);
		redBoss.deleteObject();

		enemyEnum = EnemyEnums.FourDirectionalDrone;
		Enemy fDrone = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
				enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed(), enemyEnum.getMovementSpeed(), MovementPatternSize.SMALL, false);
		fDrone.deleteObject();

		enemyEnum = EnemyEnums.YellowBoss;
		Enemy yellowBoss = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
				enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed(), enemyEnum.getMovementSpeed(), MovementPatternSize.SMALL, false);
		yellowBoss.deleteObject();



		simulateAttackAngles(true);
		simulateAttackAngles(false);

	}

	private static void simulateAttackAngles(boolean inwards) {
		// Use the centralized static values from CrossingLaserbeamsAttack
		int lowerLaserbeamLowestAngle = CrossingLaserbeamsAttack.lowerLaserbeamLowestAngle;
		int lowerLaserbeamHighestAngle = CrossingLaserbeamsAttack.lowerLaserbeamHighestAngle;
		int upperLaserbeamLowestAngle = CrossingLaserbeamsAttack.upperLaserbeamLowestAngle;
		int upperLaserbeamHighestAngle = CrossingLaserbeamsAttack.upperLaserbeamHighestAngle;
		float angleStepSize = CrossingLaserbeamsAttack.angleStepSize;

		// Adjust angles like the CrossingLaserbeamsAttack settings
		if (inwards) {
			lowerLaserbeamHighestAngle = 185;
			upperLaserbeamLowestAngle = 175;
		} else {
			lowerLaserbeamLowestAngle = 160;
			lowerLaserbeamHighestAngle = 190;
			upperLaserbeamLowestAngle = 170;
			upperLaserbeamHighestAngle = 200;
			angleStepSize = 0.25f;
		}

		LaserbeamConfiguration lowerConfig = new LaserbeamConfiguration(false, 0);
		lowerConfig.setAmountOfLaserbeamSegments(20);
		lowerConfig.setOriginPoint(new Point(0, 0));
		Laserbeam lowerLaserbeam = new AngledLaserBeam(lowerConfig);

		LaserbeamConfiguration upperConfig = new LaserbeamConfiguration(false, 0);
		upperConfig.setAmountOfLaserbeamSegments(20);
		upperConfig.setOriginPoint(new Point(0, 0));
		Laserbeam upperLaserbeam = new AngledLaserBeam(upperConfig);

		// Simulate lower laserbeam movement
		for (float angle = lowerLaserbeamLowestAngle; angle <= lowerLaserbeamHighestAngle; angle += angleStepSize) {
			lowerLaserbeam.setAngleDegrees(angle);
			lowerLaserbeam.update(); // Caching automatically happens here
		}

		// Simulate upper laserbeam movement
		for (float angle = upperLaserbeamLowestAngle; angle <= upperLaserbeamHighestAngle; angle += angleStepSize) {
			upperLaserbeam.setAngleDegrees(angle);
			upperLaserbeam.update(); // Caching automatically happens here
		}
	}

}
