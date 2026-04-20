package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.CircularPulseAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.PulsingBeacon;
import net.riezebos.bruus.tbd.game.items.items.carrier.PyrrhicProtocol;
import net.riezebos.bruus.tbd.game.items.items.carrier.RallyTheFleet;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class CarrierBeacon extends Drone {

    private boolean hasProccedRallyTheFleet = false;
    private double gameSecondsPlaced = 0;

    public CarrierBeacon(SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
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
        //Als de eigenaar null is of dood, self-destruct
        if(this.ownerOrCreator == null || (!this.ownerOrCreator.isVisible() || this.ownerOrCreator.getCurrentHitpoints() <= 0)){
            this.takeDamage(this.maxHitPoints * 200);
        }

        if(GameState.getInstance().getGameSeconds() >= lastTimeCastPulsingBeacon + PulsingBeacon.cooldown){
            MissileManager.getInstance().addSpecialAttack(createSpecialAttack());
            lastTimeCastPulsingBeacon = GameState.getInstance().getGameSeconds();
            timesCast++;
        }

    }

    public void triggerOnDeathActions(){
        PyrrhicProtocol protocol = (PyrrhicProtocol) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.PyrrhicProtocol);
        if(protocol != null){
            protocol.applyEffectToObject(this);
        }
    }

    private int timesCast = 0;
    private double lastTimeCastPulsingBeacon = GameState.getInstance().getGameSeconds();
    private SpecialAttack createSpecialAttack(){
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setScale(1);
        missileSpriteConfiguration.setImageType(ImageEnums.EnergyCircle);

        float damage = this.ownerOrCreator.getDamage() * PulsingBeacon.damageModifier * (1 + PulsingBeacon.damageBonusPerCast * timesCast);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(missileSpriteConfiguration, 2, false);

        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(damage, true, true, false, false, false, false);
        SpecialAttack specialAttack = new CircularPulseAttack(spriteAnimationConfiguration, specialAttackConfiguration);
        specialAttack.setOwnerOrCreator(this);
        specialAttack.setObjectToCenterAround(this);
        specialAttack.setCenteredAroundObject(true);

        specialAttack.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        specialAttack.getAnimation().setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        this.getObjectsFollowingThis().add(specialAttack);
        return specialAttack;
    }
}
