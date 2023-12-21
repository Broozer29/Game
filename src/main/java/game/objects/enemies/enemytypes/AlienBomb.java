package game.objects.enemies.enemytypes;

import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.SpriteAnimation;

public class AlienBomb extends Enemy {

	public AlienBomb(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
		super(spriteConfiguration, enemyConfiguration);
		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Alien_Bomb_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
	}


}