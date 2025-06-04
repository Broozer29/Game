package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.ArmorModifierEffect;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.AttackSpeedModifierEffect;
import net.riezebos.bruus.tbd.game.items.items.carrier.PyrrhicProtocol;
import net.riezebos.bruus.tbd.game.items.items.carrier.RallyTheFleet;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.List;

public class CarrierDrone extends Drone {

    private boolean hasProccedRallyTheFleet = false;
    private double gameSecondsPlaced = 0;

    public CarrierDrone(SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
        this.setAllowedVisualsToRotate(false);
        this.setAllowedToMove(false);
        this.droneType = DroneTypes.CarrierDrone;
        this.maxHitPoints = 100;
        this.currentHitpoints = maxHitPoints;
        this.isProtoss = true;
    }

    public void activateObject () {
        if(gameSecondsPlaced <= 0.02){
            gameSecondsPlaced = GameState.getInstance().getGameSeconds();
        }


        if(!hasProccedRallyTheFleet && (GameState.getInstance().getGameSeconds() >= gameSecondsPlaced + 2)) {
            RallyTheFleet rallyTheFleet = (RallyTheFleet) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.RallyTheFleet);
            if (rallyTheFleet != null) {
                rallyTheFleet.applyEffectToObject(this);
            }
            hasProccedRallyTheFleet = true;
        }
    }

    public void fireAction (){
        //to do
    }

    public void triggerOnDeathActions(){
        PyrrhicProtocol protocol = (PyrrhicProtocol) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.PyrrhicProtocol);
        if(protocol != null){
            protocol.applyEffectToObject(this);
        }
    }
}
