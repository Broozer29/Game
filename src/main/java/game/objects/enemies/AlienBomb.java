package game.objects.enemies;

import data.audio.AudioEnums;
import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class AlienBomb extends Enemy {

	public AlienBomb(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, rotation, EnemyEnums.Alien_Bomb, scale, pathFinder);
		loadImage(ImageEnums.Alien_Bomb);
		setDeathAnimation(ImageEnums.Alien_Bomb_Explosion);
		this.deathAnimation.setFrameDelay(2);
		this.initBoardBlockSpeeds();
		this.hitPoints = 10;
		this.maxHitPoints = 10;
		this.attackSpeedFrameCount = 999999;
		this.XMovementSpeed = 1;
		this.YMovementSpeed = 1;
		this.deathSound = AudioEnums.Destroyed_Explosion;
		this.hasAttack = false;
		this.showHealthBar = false;
		this.setVisible(true);
		this.setRotation(rotation);	
	}

	private void initBoardBlockSpeeds() {
		this.boardBlockSpeeds.add(0, 1);
		this.boardBlockSpeeds.add(1, 1);
		this.boardBlockSpeeds.add(2, 1);
		this.boardBlockSpeeds.add(3, 1);
		this.boardBlockSpeeds.add(4, 1);
		this.boardBlockSpeeds.add(5, 1);
		this.boardBlockSpeeds.add(6, 1);
		this.boardBlockSpeeds.add(7, 1);
	}

}