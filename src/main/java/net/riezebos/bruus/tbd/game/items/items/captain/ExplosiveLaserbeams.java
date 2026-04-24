package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ExplosiveLaserbeams extends Item {

    public static float damageModifier = 1;

    public ExplosiveLaserbeams() {
        super(ItemEnums.ExplosiveLaserbeams, 1, ItemApplicationEnum.AfterCollision);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target, CollisionInfo collisionInfo) {
        //Applies an effect to an object, with the applier provided for certain conditions

        if(applier.getOwnerOrCreator() != null && applier.getOwnerOrCreator() instanceof SpaceShip && collisionInfo != null && applier instanceof Missile){
            ExplosionManager.getInstance().addExplosion(createExplosion(applier, target, collisionInfo));
        }
    }


    private Explosion createExplosion(GameObject applier, GameObject target, CollisionInfo collisionInfo){
        float damage = target.getDamage() * quantity;
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setImageType(ImageEnums.Explosion4);
        spriteConfiguration.setxCoordinate(applier.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(applier.getCenterYCoordinate());
        spriteConfiguration.setScale(1f);

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

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}
