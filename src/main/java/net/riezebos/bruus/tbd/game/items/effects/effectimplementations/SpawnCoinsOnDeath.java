package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.Interactable;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.InteractableManager;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.RotatingCoins;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SpawnCoinsOnDeath implements EffectInterface {

    private float goldAmount;
    private EffectIdentifiers effectIdentifier;
    private int coinAmount;

    private EffectActivationTypes effectActivationType;
    private boolean activated;

    private SpriteAnimation additionalAnimation;
    private List<Direction> usedRotations = new ArrayList<>();

    public SpawnCoinsOnDeath(float goldAmount, int coinAmount) {
        this.effectActivationType = EffectActivationTypes.OnObjectDeath;
        this.effectIdentifier = EffectIdentifiers.SpawnCoinsOnDeath;
        this.goldAmount = goldAmount;
        this.activated = false;
        this.coinAmount = coinAmount;
    }


    @Override
    public void activateEffect(GameObject gameObject) {
        if (gameObject.getCurrentHitpoints() <= 0 && !activated) {
            activated = true;

            if(coinAmount > 4){
                coinAmount = 4; //Only 4 diagonal directions
            }

            for (int i = 0; i < coinAmount; i++) {
                InteractableManager.getInstance().addInteractable(getRotatingCoin(gameObject));
            }
        }
    }

    private Interactable getRotatingCoin(GameObject gameObject) {

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setyCoordinate(gameObject.getCenterYCoordinate());
        spriteConfiguration.setxCoordinate(gameObject.getCenterXCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.RotatingCoins);

        BouncingPathFinder bouncingPathFinder = new BouncingPathFinder();
        bouncingPathFinder.setMaxBounces(RotatingCoins.maxBounces);
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        MovementConfiguration movementConfiguration = EnemyCreator.createMovementConfiguration(
                spriteConfiguration.getxCoordinate(), spriteAnimationConfiguration.getSpriteConfiguration().getyCoordinate(), getDirection(),
                RotatingCoins.defaultMovementSpeed, RotatingCoins.defaultMovementSpeed, MovementPatternSize.SMALL, bouncingPathFinder
        );

        Interactable coins = new RotatingCoins(spriteAnimationConfiguration, movementConfiguration, goldAmount);
        coins.setAllowedVisualsToRotate(false);
        coins.resetMovementPath();
        coins.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());

        return coins;
    }

    private Direction getDirection(){
        Direction rotation = Direction.getRandomDiagonalDirection();

        if(usedRotations.contains(rotation)){
            return getDirection();
        } else {
            usedRotations.add(rotation);
        }
        return rotation;
    }

    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        return false;
    }

    @Override
    public SpriteAnimation getAnimation() {
        return null;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums() {
        return this.effectActivationType;
    }

    @Override
    public void resetDuration() {
        //Shouldnt expire anyway
    }

    @Override
    public void increaseEffectStrength(GameObject gameObject) {
        //Maybe do something later?
    }

    @Override
    public EffectInterface copy() {
        //Shouldn't be used
        return null;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier() {
        return effectIdentifier;
    }

    @Override
    public void removeEffect(GameObject gameObject) {
        if (additionalAnimation != null) {
            additionalAnimation.setInfiniteLoop(false);
            additionalAnimation.setVisible(false);
        }
        additionalAnimation = null;
    }
}
