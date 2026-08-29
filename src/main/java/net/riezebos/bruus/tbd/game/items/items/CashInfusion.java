package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.Interactable;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.InteractableManager;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.RotatingCoins;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.SpawnCoinsOnDeath;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class CashInfusion extends Item {
    public static int mineralsPerPickup = 15;
    public static float spawnChance = 0.05f;
    private static Random random = new Random();

    public CashInfusion() {
        super(ItemEnums.CashInfusion, 1, ItemApplicationEnum.AfterCollision);
    }


    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target) {
        //Applies an effect to an object, with the applier provided for certain conditions
//        if(applier.isFriendly() && target instanceof Enemy  && ((Enemy) target).getEnemyType().getEnemyCategory().equals(EnemyCategory.Medium)){
//            target.addEffect(createCoinEffect());
//        }
    }

    private SpawnCoinsOnDeath createCoinEffect(){
        return new SpawnCoinsOnDeath(mineralsPerPickup, 1, spawnChance);
    }

    public static void spawnCoin(GameObject applier, GameObject target){

        if(applier.getOwnerOrCreator() != null && (applier.getOwnerOrCreator() instanceof SpaceShip || (applier.getOwnerOrCreator() instanceof Drone drone && drone.isProtoss()))  && random.nextFloat(0, 1.0f) < spawnChance){
            //alleen toepassen als de applier van de speler komt, e.g. geen drones/lingering flames etc etc
            InteractableManager.getInstance().addInteractable(getRotatingCoin(target));
        }
    }

    private static Interactable getRotatingCoin(GameObject gameObject) {

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setyCoordinate(gameObject.getCenterYCoordinate());
        spriteConfiguration.setxCoordinate(gameObject.getCenterXCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.RotatingCoins);

        BouncingPathFinder bouncingPathFinder = new BouncingPathFinder();
        bouncingPathFinder.setMaxBounces(RotatingCoins.maxBounces);
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        MovementConfiguration movementConfiguration = EnemyCreator.createMovementConfiguration(
                spriteConfiguration.getxCoordinate(), spriteAnimationConfiguration.getSpriteConfiguration().getyCoordinate(), Direction.getRandomDiagonalDirection(),
                RotatingCoins.defaultMovementSpeed, bouncingPathFinder
        );

        Interactable coins = new RotatingCoins(spriteAnimationConfiguration, movementConfiguration, mineralsPerPickup);
        coins.setAllowedVisualsToRotate(false);
        coins.resetMovementPath();
        coins.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());

        return coins;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }
        return true;
    }
}