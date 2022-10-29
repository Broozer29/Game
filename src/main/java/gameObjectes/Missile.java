package gameObjectes;

import java.util.ArrayList;
import java.util.List;

import Data.DataClass;

public class Missile extends Sprite {

	private final int BOARD_WIDTH = 1280;
	private float missileDamage;
	private String missileType;

	public Missile(int x, int y, String missileType) {
		super(x, y);
		this.missileType = missileType;
		initMissile();
	}

	private void initMissile() {
		if (missileType.equals("laserblast")) {
			loadImage("laserbeam");
			this.missileDamage = (float) 7.5;
		}
		if (missileType.equals("AlienDefault")) {
			loadImage("alienlaserbeam");
			this.missileDamage = (float) 2.5;
		}

		getImageDimensions();
	}

	public void updateGameTick() {
		move(getCoordinatesDefaultMove(missileType));

	}

	private List<Float> getCoordinatesDefaultMove(String type) {
		List<Float> coordinatesList = new ArrayList<Float>(2);

		if (type.equals("laserblast")) {
			coordinatesList.add(0, (float) 7.5);
			coordinatesList.add(1, (float) 0);
		} else if (type.equals("AlienDefault")) {
			coordinatesList.add(0, (float) -3);
			coordinatesList.add(1, (float) 0);
		}
		return coordinatesList;
	}

	public void move(List<Float> coordinatesList) {
		xCoordinate += coordinatesList.get(0);
		yCoordinate += coordinatesList.get(1);
		if (xCoordinate > BOARD_WIDTH) {
			visible = false;
		}
		if (xCoordinate < 0) {
			visible = false;
		}
	}

	public float getMissileDamage() {
		return this.missileDamage;
	}
}