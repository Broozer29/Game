package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.Interactable;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.InteractableManager;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.RecyclePart;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.items.Recycler;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawnRecyclePartOnDeath implements EffectInterface {

    private EffectIdentifiers effectIdentifier;

    private EffectActivationTypes effectActivationType;
    private boolean activated;

    private List<SpriteAnimation> animationList = new ArrayList<>();

    public SpawnRecyclePartOnDeath() {
        this.effectActivationType = EffectActivationTypes.OnObjectDeath;
        this.effectIdentifier = EffectIdentifiers.SpawnRecyclePartOnDeath;
        this.activated = false;
    }


    @Override
    public void activateEffect(GameObject gameObject) {
        if (gameObject.getCurrentHitpoints() <= 0 && !activated) {
            activated = true;

            Random random = new Random();
            //should be null safe, since this effect cannot be created but from the recycler class
            Recycler recycler = (Recycler) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.Recycler);
            if(random.nextInt(0, 1) < (recycler.getQuantity() * Recycler.spawnChance)){
                InteractableManager.getInstance().addInteractable(getRecyclerPart(gameObject));
            }
        }
    }

    private Interactable getRecyclerPart(GameObject gameObject) {

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setyCoordinate(gameObject.getCenterYCoordinate());
        spriteConfiguration.setxCoordinate(gameObject.getCenterXCoordinate());
        spriteConfiguration.setScale(0.65f);
        spriteConfiguration.setImageType(ImageEnums.ScrapMetalAnim);

        BouncingPathFinder bouncingPathFinder = new BouncingPathFinder();
        bouncingPathFinder.setMaxBounces(RecyclePart.maxBounces);
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
        MovementConfiguration movementConfiguration = EnemyCreator.createMovementConfiguration(
                spriteConfiguration.getxCoordinate(), spriteAnimationConfiguration.getSpriteConfiguration().getyCoordinate(), Direction.getRandomDiagonalDirection(),
                RecyclePart.defaultMovementSpeed, bouncingPathFinder
        );

        Interactable scrapMetal = new RecyclePart(spriteAnimationConfiguration, movementConfiguration);
        scrapMetal.setAllowedVisualsToRotate(false);
        scrapMetal.setAllowedToMove(false);
        scrapMetal.resetMovementPath();
        scrapMetal.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());

        return scrapMetal;
    }

    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        return false;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return animationList;
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
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            animationList.get(0).setInfiniteLoop(false);
            animationList.get(0).setVisible(false);
        }
        animationList.clear();
    }
}
