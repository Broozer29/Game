package game.util;

import VisualAndAudioData.image.ImageEnums;
import game.managers.AnimationManager;
import game.objects.GameObject;
import game.objects.player.PlayerManager;
import game.objects.player.spaceship.SpaceShip;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class LevelAnimationPlayer {

    public static void playLevelUpAnimation(GameObject gameObject){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.LevelUpAnimation);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, false);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.setAnimationScale(0.3f);
        if(gameObject instanceof SpaceShip) {
            PlayerManager.getInstance().getSpaceship().addPlayerFollowingAnimation(spriteAnimation);
            AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
        } else {
            //unimplemented, do we need to level enemies? maybe later
        }

    }
}
