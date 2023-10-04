package game.movement;

import java.util.ArrayList;
import java.util.List;

import game.movement.pathfinderconfigs.RegularPathFinderConfig;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.friendlies.powerups.PowerUp;
import gamedata.DataClass;

public class PowerUpDirectionBouncer {

	private final int windowWidth;
	private final int windowHeight;
	private static PowerUpDirectionBouncer instance = new PowerUpDirectionBouncer();

	private PowerUpDirectionBouncer() {
		this.windowWidth = DataClass.getInstance().getWindowWidth();
		this.windowHeight = DataClass.getInstance().getWindowHeight();
	}

	public static PowerUpDirectionBouncer getInstance() {
		return instance;
	}


	
}