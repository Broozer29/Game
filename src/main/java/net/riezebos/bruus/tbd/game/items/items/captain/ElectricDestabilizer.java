package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.ElectroShred;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.FreezeEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ElectricDestabilizer extends Item {
    public static float duration = 2;

    public ElectricDestabilizer () {
        super(ItemEnums.ElectricDestabilizer, 1, ItemApplicationEnum.AfterCollision);
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target) {
        if (target instanceof Enemy enemy && enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Boss)) {
            return;
        }

        if(applier.getOwnerOrCreator() != null && !applier.getOwnerOrCreator().equals(PlayerManager.getInstance().getSpaceship())){
            return; //If the electroshreds owner is NOT the player, return
        }


        if (applier instanceof ElectroShred) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(target.getXCoordinate());
            spriteConfiguration.setyCoordinate(target.getYCoordinate());
            spriteConfiguration.setScale(1);
            spriteConfiguration.setImageType(ImageEnums.FreezeEffect);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
            SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);

            FreezeEffect freezeEffect = new FreezeEffect(duration * quantity, spriteAnimation);

            target.addEffect(freezeEffect);
        }
    }


    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }


    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}
