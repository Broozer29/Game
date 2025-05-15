package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.ElectroShred;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.LingeringFlame;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.captain.ModuleElectrify;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class SpecialAttackDrone extends Drone {

    public SpecialAttackDrone (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration, DroneTypes droneType) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
        super.droneType = droneType;
    }

    @Override
    public void activateObject () {
        PlayerInventory playerInventory = PlayerInventory.getInstance();
        if (playerInventory.getItemFromInventoryIfExists(ItemEnums.ModuleCommand) != null) {
            return;
        }

        double currentTime = GameState.getInstance().getGameSeconds();

        //No attack cooldown for channeled attacks
        if (this.droneType.equals(DroneTypes.Missile) || this.droneType.equals(DroneTypes.ElectroShred)) {
            if (currentTime >= lastAttackTime + this.getAttackSpeed()) {
                fireAction();
                lastAttackTime = currentTime;
            }
        }
        if (this.droneType == DroneTypes.FireBall) {
            fireAction(); //Might not be needed
        }

    }



    @Override
    public void fireAction () {
        switch (droneType) {
            case ElectroShred -> fireElectroShred();
            case FireBall -> activateFlamingBalls();
            default -> fireElectroShred();
        }
    }

    private void fireElectroShred () {
        float bonusScale = 0.0f;
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleElectrify) != null) {
            ModuleElectrify electrify = (ModuleElectrify) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleElectrify);
            bonusScale = electrify.getDroneSpecialScale();
        }

        float scale = 0.75f * (1 + bonusScale);
        SpriteConfiguration electroShredSpriteConfig = new SpriteConfiguration();
        electroShredSpriteConfig.setxCoordinate(this.getCenterXCoordinate());
        electroShredSpriteConfig.setyCoordinate(this.getCenterYCoordinate());
        electroShredSpriteConfig.setImageType(ImageEnums.ElectroShredImproved);
        electroShredSpriteConfig.setScale(scale);

        float damage = PlayerStats.getInstance().getDroneDamage() * 0.25f; //25% of drone base damage

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(electroShredSpriteConfig, 2, false);
        SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, false, false);

        SpriteAnimation specialAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        specialAttackAnimation.setAnimationScale(scale);

        SpecialAttack specialAttack = new ElectroShred(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundObject(true);
        specialAttack.setScale(scale);
        specialAttack.setOwnerOrCreator(this);
        specialAttack.setCenterCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());

        this.addFollowingGameObject(specialAttack);
        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }

    private boolean initialized = false;
    private void activateFlamingBalls () {
        if(!initialized) {
            SpriteConfiguration orbitingLingeringFlame = new SpriteConfiguration();
            float scale = 0.4f;
            orbitingLingeringFlame.setxCoordinate(this.getCenterXCoordinate());
            orbitingLingeringFlame.setyCoordinate(this.getCenterYCoordinate());
            orbitingLingeringFlame.setImageType(ImageEnums.ModuleScorchFlames);
            orbitingLingeringFlame.setScale(scale);

            float damage = PlayerStats.getInstance().getDroneDamage() * 0.2f; //20% of drone base damage

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(orbitingLingeringFlame, 0, true);
            SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, false, false);

            LingeringFlame lingeringFlame = new LingeringFlame(spriteAnimationConfiguration, missileConfiguration);
            lingeringFlame.setCenteredAroundObject(true);
            lingeringFlame.setOwnerOrCreator(this);
            lingeringFlame.setDuration(999999999); //practically infinite

            this.addFollowingGameObject(lingeringFlame);
            MissileManager.getInstance().addSpecialAttack(lingeringFlame);
            this.initialized = true;
        }
    }
}
