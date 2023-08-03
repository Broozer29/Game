package game.objects.enemies;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;

public class AlienBomb extends Enemy {

	public AlienBomb(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, rotation, EnemyEnums.Alien_Bomb, scale, pathFinder, xMovementSpeed,yMovementSpeed);
		loadImage(ImageEnums.Alien_Bomb);
//		animation
		//Create animated enemies lol, kopieer missile
		setDeathAnimation(ImageEnums.Alien_Bomb_Explosion);
		this.deathAnimation.setFrameDelay(2);
		this.hitPoints = 10;
		this.maxHitPoints = 10;
		this.attackSpeedFrameCount = 999999;
		this.deathSound = AudioEnums.Destroyed_Explosion;
		this.hasAttack = false;
		this.showHealthBar = false;
		this.setVisible(true);
		this.setRotation(rotation);	
	}


}