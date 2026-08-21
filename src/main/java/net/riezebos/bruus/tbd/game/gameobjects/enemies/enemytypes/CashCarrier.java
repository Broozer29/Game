package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.Interactable;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.InteractableManager;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable.RotatingCoins;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.SpawnCoinsOnDeath;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.Random;

public class CashCarrier extends Enemy {

    private Random random = new Random();
    public CashCarrier(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale * 1.5f);
        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.25f);


        SpawnCoinsOnDeath goldOnDeathEffect = new SpawnCoinsOnDeath(20, 3, 1.0f);
        this.addEffect(goldOnDeathEffect);
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;

        if (LevelManager.getInstance().getCurrentEnemyTribe().equals(EnemyTribes.Zerg)) {
            initAsOverlord();
        }

        this.hasAttack = false;
    }

    private void initAsOverlord() {
        this.getAnimation().changeImagetype(ImageEnums.Overlord);
        this.getAnimation().setFrameDelay(9);
        this.setAllowedVisualsToRotate(true);
        this.setDeathSound(AudioEnums.OverlordDeath);
        this.rotateObjectTowardsRotation(true);
        this.getDestructionAnimation().changeImagetype(ImageEnums.GuardianDeath);
        this.getDestructionAnimation().setFrameDelay(3);
    }



    private int coinDropCooldown = 5;
    private double lastSecondsCoinDropped = GameState.getInstance().getGameSeconds() + random.nextInt(0, 3); //start on 5 sec additional cooldown
    public void fireAction() {
        //Probably do nothing?

        if(GameState.getInstance().getGameSeconds() >= lastSecondsCoinDropped + coinDropCooldown &&
                WithinVisualBoundariesCalculator.isWithinBoundaries(this)){

            dropCoin();
            lastSecondsCoinDropped = GameState.getInstance().getGameSeconds() + random.nextDouble(0, 1.2);
        }
    }

    private int coinWorth = 10;
    private void dropCoin(){
        SpriteAnimationConfiguration animConfig = SpriteAnimation.createDefaultSpriteAnimationConfig(ImageEnums.RotatingCoins);
        animConfig.setInfiniteLoop(true);
        BouncingPathFinder bouncingPathFinder = new BouncingPathFinder();
        bouncingPathFinder.setMaxBounces(RotatingCoins.maxBounces);
        MovementConfiguration movementConfiguration = EnemyCreator.createMovementConfiguration(
                this.getCenterXCoordinate(), this.getCenterYCoordinate(), Direction.getRandomDiagonalDirection(),
                RotatingCoins.defaultMovementSpeed, bouncingPathFinder
        );

        Interactable coins = new RotatingCoins(animConfig, movementConfiguration, coinWorth);
        coins.setAllowedVisualsToRotate(false);
        coins.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        coins.resetMovementPath();

        InteractableManager.getInstance().addInteractable(coins);
    }

}
