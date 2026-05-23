package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.LingeringAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
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
        if (this.droneType == DroneTypes.FireBall) {
            fireAction(); //Might not be needed
        }

    }



    @Override
    public void fireAction () {
        switch (droneType) {
            case FireBall -> activateFlamingBalls();
        }
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

            SpaceShip spaceship = (SpaceShip) this.ownerOrCreator;
            float damage = (PlayerStats.getInstance().getBaseDroneDamage() * 0.5f) + spaceship.getDroneDamageModifier();

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(orbitingLingeringFlame, 0, true);
            SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, false, false);

            LingeringAttack lingeringFlame = new LingeringAttack(spriteAnimationConfiguration, missileConfiguration);
            lingeringFlame.setAllowedVisualsToRotate(false);
            lingeringFlame.setOwnerOrCreator(this);
            lingeringFlame.setDuration(999999999); //practically infinite

            this.addFollowingGameObject(lingeringFlame);
            MissileManager.getInstance().addSpecialAttack(lingeringFlame);
            this.initialized = true;
        }
    }
}
