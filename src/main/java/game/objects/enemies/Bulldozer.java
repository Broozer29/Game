package game.objects.enemies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.OrbitPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;
import game.objects.friendlies.FriendlyObject;
import game.objects.friendlies.GuardianDrone;
import game.objects.missiles.MissileManager;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;
import visual.objects.SpriteAnimation;

public class Bulldozer extends Enemy {

	public Bulldozer(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder,
			int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, rotation, EnemyEnums.Bulldozer, scale, pathFinder, xMovementSpeed, yMovementSpeed);
		loadImage(ImageEnums.Bulldozer);
		setExhaustanimation(ImageEnums.Bulldozer_Normal_Exhaust);
		setDeathAnimation(ImageEnums.Bulldozer_Destroyed_Explosion);
		this.exhaustAnimation.setFrameDelay(1);
		this.hitPoints = 50;
		this.maxHitPoints = 50;
		this.attackSpeedFrameCount = 200;
		this.hasAttack = true;
		this.showHealthBar = true;
		this.deathSound = AudioEnums.Large_Ship_Destroyed;
		this.setVisible(true);
		this.setRotation(rotation);
		this.deathAnimation.rotateAnimetion(rotation);
		createRotatingBombs();
	}

	private void createRotatingBombs() {
	    // The center around which the AlienBombs will orbit
	    double meanX = this.getCenterXCoordinate();
	    double meanY = this.getCenterYCoordinate();

	    // Calculate the angle increment based on how many bombs you want
	    double angleIncrement = 2 * Math.PI / 8; // 8 is the total number of bombs

	    for (int iterator = 0; iterator < 8; iterator++) {
	        // 2. Find the next angle
	        double nextAngle = angleIncrement * iterator;

	        // 3. Place the new drone
	        int radius = 75; // Example radius
	        int x = (int) (meanX + Math.cos(nextAngle) * radius);
	        int y = (int) (meanY + Math.sin(nextAngle) * radius);

	        PathFinder pathFinder = new OrbitPathFinder(this, radius, 300, nextAngle);
	        Enemy alienBomb = new AlienBomb(x, y, null, Direction.LEFT, scale, pathFinder, 1, 1);
	        this.followingEnemies.add(alienBomb);
	        EnemyManager.getInstance().addEnemy(alienBomb);
	    }
	}
	

	
	public void fireAction() {
		if (missileManager == null) {
			missileManager = MissileManager.getInstance();
		}
	}

}