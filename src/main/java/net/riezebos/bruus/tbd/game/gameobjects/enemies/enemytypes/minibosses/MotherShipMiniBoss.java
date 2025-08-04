package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.minibosses;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MotherShipMiniBoss extends Enemy {


    private List<Enemy> droneList = new ArrayList<>();
    private int lastRegisteredBoardBlock = 0;

    public MotherShipMiniBoss(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteAnimationConfigurationion.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(3f);
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
        this.updateBoardBlock();
        this.lastRegisteredBoardBlock = this.getCurrentBoardBlock();
        this.damage = 10;

        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder){
            hoverPathFinder.setSecondsToHoverStill(0);
            hoverPathFinder.setShouldDecreaseBoardBlock(true);
        }

    }


    public void fireAction () {
        //idk of deze gedrag moet hebben
        if(this.getCurrentBoardBlock() != lastRegisteredBoardBlock && droneList.size() < 4){
            addDrone();
//            OnScreenTextManager.getInstance().addText("Adding a drone");
            lastRegisteredBoardBlock = this.getCurrentBoardBlock();
        }

        droneList.removeIf(drone -> !drone.isVisible() || drone.getCurrentHitpoints() <= 0);

        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder){
            if(this.getCurrentBoardBlock() <= 1){ // if its reaches 0 it will move out of bounds
                hoverPathFinder.setDecreaseBoardBlockAmountBy(-1);
            } else if (this.getCurrentBoardBlock() >= 7){
                hoverPathFinder.setDecreaseBoardBlockAmountBy(1);
            }
        }

    }


    private void addDrone(){
        Enemy drone = EnemyCreator.createEnemy(EnemyEnums.MotherShipDrone, this.getCenterXCoordinate(), this.getCenterYCoordinate(), Direction.LEFT, 0.25f, EnemyEnums.MotherShipDrone.getMovementSpeed(), EnemyEnums.MotherShipDrone.getMovementSpeed(),
        MovementPatternSize.SMALL, false);
        drone.setOwnerOrCreator(this);
        drone.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        droneList.add(drone);
        EnemyManager.getInstance().addEnemy(drone);
    }

    public void triggerOnDeathActions() {
        for(Enemy drone :droneList){
            drone.takeDamage(9999);
        }
    }

}
