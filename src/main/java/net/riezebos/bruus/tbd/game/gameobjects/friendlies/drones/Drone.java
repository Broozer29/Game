package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectEnums;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.Martyrdom;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;


public abstract class Drone extends GameObject {
    protected double lastAttackTime = 0.0;
    protected DroneTypes droneType;
    protected boolean isProtoss = false;
    protected FriendlyObjectEnums friendlyObjectType;

    protected Drone(SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration);
        this.friendlyObjectType = droneConfiguration.getFriendlyType();
        this.attackSpeed = droneConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        this.boxCollision = droneConfiguration.isBoxCollision();
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
    }

    protected void initProtossDeathExplosion() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(this.getXCoordinate());
        spriteConfiguration.setyCoordinate(this.getYCoordinate());
        spriteConfiguration.setScale(0.75f);
        spriteConfiguration.setImageType(ImageEnums.ProtossDestroyedExplosion);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        this.destructionAnimation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    public void activateObject() {
        //Must be overriden


    }

    public void fireAction() {
        //Must be overriden
    }

    public boolean isProtoss() {
        return isProtoss;
    }

    public DroneTypes getDroneType() {
        return droneType;
    }

    public double getLastAttackTime() {
        return lastAttackTime;
    }

    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();

        Martyrdom martyrdom = (Martyrdom) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.Martyrdom);
        if (this.isProtoss && martyrdom != null) {
            martyrdom.applyEffectToObject(null);
        }

    }

}