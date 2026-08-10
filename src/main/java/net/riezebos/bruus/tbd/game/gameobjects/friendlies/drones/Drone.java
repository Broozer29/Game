package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectEnums;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.ElectroShred;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.secondary.CaptainSecondaryGun;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.captain.AnionInverter;
import net.riezebos.bruus.tbd.game.items.items.captain.ModuleElectrify;
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

    public void fireAction(GameObject target) {
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
            martyrdom.applyEffectToObject(this.ownerOrCreator);
        }

    }

    private double lastTimeElectroShredAttack = -100;

    public void spawnElectroShredAttack() {
        if (lastTimeElectroShredAttack + ModuleElectrify.cooldown <= GameState.getInstance().getGameSeconds()) {
            fireElectroShred();
            lastTimeElectroShredAttack = GameState.getInstance().getGameSeconds();
        }
    }

    private void fireElectroShred() {
        float scale = 1.5f;
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.AnionInverter) != null) {
            scale *= (1 + AnionInverter.scaleBonus);
        }
        SpriteConfiguration electroShredSpriteConfig = new SpriteConfiguration();
        electroShredSpriteConfig.setxCoordinate(this.getCenterXCoordinate());
        electroShredSpriteConfig.setyCoordinate(this.getCenterYCoordinate());
        SpaceShip owner = (SpaceShip) this.ownerOrCreator;
        electroShredSpriteConfig.setImageType(owner.getElectroShredImageEnum());
        electroShredSpriteConfig.setScale(scale);
        float damage = (owner.getSpecialAttackDamage() * CaptainSecondaryGun.electroShredBonusDamageModifier) * owner.getDroneDamageModifier();

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(electroShredSpriteConfig, 2, false);
        SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, true, true);

        SpriteAnimation specialAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        specialAttackAnimation.setAnimationScale(scale);

        SpecialAttack specialAttack = new ElectroShred(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundObject(true);
        specialAttack.setScale(scale);
        specialAttack.setOwnerOrCreator(this.ownerOrCreator);
        specialAttack.setCenterCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());
        specialAttack.setTransparancyAlpha(false, 0.3f, 0);
        this.addFollowingGameObject(specialAttack);
        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }

}