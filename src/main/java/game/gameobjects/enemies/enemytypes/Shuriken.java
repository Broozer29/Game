package game.gameobjects.enemies.enemytypes;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.movement.MovementConfiguration;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class Shuriken extends Enemy {
    public Shuriken (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        initObject();
    }

    public Shuriken (SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        initObject();
    }

    private void initObject(){
        SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
        spriteConfiguration1.setScale(1);
        spriteConfiguration1.setxCoordinate(this.xCoordinate);
        spriteConfiguration1.setyCoordinate(this.yCoordinate);
        spriteConfiguration1.setImageType(this.enemyType.getDestructionType());
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.deathSound = AudioEnums.Alien_Bomb_Impact;
        this.detonateOnCollision = true;
        this.damage = 40;
    }

}
