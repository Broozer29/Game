package game.gameobjects.enemies.enemytypes;

import game.movement.MovementConfiguration;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gameobjects.enemies.Enemy;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class AlienBomb extends Enemy {

	public AlienBomb(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, enemyConfiguration, movementConfiguration);
		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Alien_Bomb_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
		this.detonateOnCollision = true;
	}


}