package net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class ScrapMetal extends Interactable {

    public static float defaultMovementSpeed = 0.7f;
    public static int maxBounces = 2;
    private float repairAmount;
    private boolean activated = false;

    public ScrapMetal(SpriteAnimationConfiguration spriteAnimationConfiguration, MovementConfiguration movementConfiguration, float repairProgressionAmount) {
        super(spriteAnimationConfiguration, movementConfiguration);
        this.repairAmount = repairProgressionAmount;
    }

    @Override
    public void activateObject() {
        if (!activated) {
            activated = true;
            this.setVisible(false);
            ProtossUtils.getInstance().handleScrapMetalPickUp(this.repairAmount);
        }
    }

    public float getRepairAmount() {
        return repairAmount;
    }

    public void setRepairAmount(float repairAmount) {
        this.repairAmount = repairAmount;
    }
}