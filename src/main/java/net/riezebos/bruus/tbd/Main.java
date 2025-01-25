package net.riezebos.bruus.tbd;

import javafx.application.Platform;
import net.riezebos.bruus.tbd.controllerInput.ConnectedControllersManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
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
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

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



		//Rotate a laserbeam 360 degrees so it doesnt lag when they are used
		LaserbeamConfiguration laserbeamConfiguration = new LaserbeamConfiguration(false, 0);
		laserbeamConfiguration.setAmountOfLaserbeamSegments(3);
		laserbeamConfiguration.setAngleDegrees(Direction.RIGHT.toAngle());
		laserbeamConfiguration.setDirection(Direction.RIGHT);
		laserbeamConfiguration.setOriginPoint(new Point(0,0));
		Laserbeam laserbeam = new AngledLaserBeam(laserbeamConfiguration);

		for(int i = 0; i < 360; i++){
			laserbeam.setAngleDegrees(i);
			laserbeam.update();
		}


	}
}
