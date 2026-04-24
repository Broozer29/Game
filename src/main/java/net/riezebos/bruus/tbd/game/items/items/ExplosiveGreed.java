package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ExplosiveGreed extends Item {

    public static float damageModifier = 15;

    public ExplosiveGreed() {
        super(ItemEnums.ExplosiveGreed, 1, ItemApplicationEnum.CustomActivation);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target, CollisionInfo collisionInfo) {
        //Applies an effect to an object, with the applier provided for certain conditions
        if(collisionInfo != null && target != null){
            ExplosionManager.getInstance().addExplosion(createExplosion(target, collisionInfo));
        }
    }


    private Explosion createExplosion(GameObject target, CollisionInfo collisionInfo){
        float damage = target.getDamage() * (damageModifier * quantity);
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setImageType(ImageEnums.CarrierWarpExplosion); //todo placeholder
        spriteConfiguration.setxCoordinate(target.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(target.getCenterYCoordinate());
        spriteConfiguration.setScale(2);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, damage, true, false);
        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setCenterCoordinates(collisionInfo.getCollisionPoint().getX(), collisionInfo.getCollisionPoint().getY());
        return explosion;
    }


    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return true;
    }
}
