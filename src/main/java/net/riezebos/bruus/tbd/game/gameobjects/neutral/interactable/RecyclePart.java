package net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable;

import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class RecyclePart extends Interactable {

    public static float defaultMovementSpeed = 0.5f;
    public static int maxBounces = 2;
    private boolean activated = false;
    private double gameSecondsSpawned = -1;
    private boolean isDespawning = false;

    public RecyclePart(SpriteAnimationConfiguration spriteAnimationConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, movementConfiguration);
    }

    @Override
    public void updateObject() {
        if(gameSecondsSpawned <= 0){
            this.gameSecondsSpawned = GameState.getInstance().getGameSeconds();
        }

        if(!isDespawning && GameState.getInstance().getGameSeconds() - gameSecondsSpawned > 2){
            this.isDespawning = true;
            this.setTransparancyAlpha(true, 1, -0.01f);
        }
    }

    @Override
    public void activateObject(SpaceShip activator) {

        if (!activated) {
            activated = true;
            activator.getSecondaryGun().addSpecialCharge(activator);
            this.setVisible(false);
        }
    }
}