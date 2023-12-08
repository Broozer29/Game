package game.objects.enemies.enemytypes;

import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyEnums;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;
import visual.objects.SpriteAnimation;

public class AlienBomb extends Enemy {

	public AlienBomb(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, rotation, EnemyEnums.Alien_Bomb, scale, pathFinder, xMovementSpeed,yMovementSpeed);
		loadImage(ImageEnums.Alien_Bomb);
		this.destructionAnimation = new SpriteAnimation(x, y, ImageEnums.Alien_Bomb_Explosion, false, scale);
		this.destructionAnimation.setFrameDelay(2);
		this.currentHitpoints = 10;
		this.maxHitPoints = 10;
		this.attackSpeed = 999999;
		this.deathSound = AudioEnums.Destroyed_Explosion;
		this.hasAttack = false;
		this.showHealthBar = false;
		this.setVisible(true);
		rotateGameObject(rotation);
	}


}