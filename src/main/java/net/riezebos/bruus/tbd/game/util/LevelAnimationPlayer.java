package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.managers.AnimationManager;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

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
