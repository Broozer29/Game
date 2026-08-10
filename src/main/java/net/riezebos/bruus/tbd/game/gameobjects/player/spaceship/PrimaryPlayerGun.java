package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerPrimaryAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;

public abstract class PrimaryPlayerGun {

    protected MissileManager missileManager = MissileManager.getInstance();
    protected AudioManager audioManager = AudioManager.getInstance();
    protected PlayerStats playerStats = PlayerStats.getInstance();
    protected SpecialAttack channeledAttack = null;

    protected double lastAttackTime = 0.0;
    protected double timeChannelAttackGetsCleared = 0.0;

    protected float orangeBarMaxValue = -10;
    protected float orangeBarCurrentValue = -10;

    public PrimaryPlayerGun() {
        //shouldnt be able to directly instantiated
    }

    public void fire(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType, SpaceShip owner) {
        // To be overridden
    }

    protected void fireGenericBehaviour(SpaceShip owner) {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleCommand) != null) {
            for (Drone drone : FriendlyManager.getInstance().getAllPlayerDrones(owner)) {
                drone.fireAction();
            }
        }
    }

    protected boolean attackOffCooldown(SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return false;
        }

        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + owner.getAttackSpeed()) {
            lastAttackTime = currentTime;  // Update the last attack time
            return true;
        }

        return false;
    }


    public void stopFiring(SpaceShip owner) {
        //to be overridden
    }

    public void updateFrameCount(SpaceShip owner) {
        if (channeledAttack != null && channeledAttack.isDissipating()) {
            if ((timeChannelAttackGetsCleared + 0.5d) < GameState.getInstance().getGameSeconds()) {
                channeledAttack = null;
            }
        }
    }

    public float getOrangeBarMaxValue(SpaceShip owner) {
        return orangeBarMaxValue;
    }

    public float getOrangeBarCurrentValue(SpaceShip owner) {
        return orangeBarCurrentValue;
    }

}