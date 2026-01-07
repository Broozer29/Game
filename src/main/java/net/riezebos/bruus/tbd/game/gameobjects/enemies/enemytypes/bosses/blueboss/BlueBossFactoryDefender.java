package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;

public class BlueBossFactoryDefender extends Enemy {
    private GameObject target;
    private int attackRange = 525;
    private boolean wasFiringAtTarget = false;

    public BlueBossFactoryDefender(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        target = PlayerManager.getInstance().getSpaceship();
        this.setAllowedVisualsToRotate(true);
        this.attackSpeed = 0.65f;
        this.knockbackStrength = 9;
    }

    @Override
    public void fireAction() {
        this.setAllowedVisualsToRotate(true);
        this.rotateGameObjectTowards(target.getCenterXCoordinate(), target.getCenterYCoordinate(), false);
        if(this.ownerOrCreator != null && this.ownerOrCreator.getCurrentHitpoints() <= 0 || !this.ownerOrCreator.isVisible()){
            this.takeDamage(this.getMaxHitPoints() * 100);
        }

        //todo, actual firing


        double currentTime = GameState.getInstance().getGameSeconds();
        super.updateChargingAttackAnimationCoordination();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && !isTooFarAway()) {
            shootMissile();
            lastAttackTime = currentTime;
        }
    }

    @Override
    public boolean isShowHealthBar(){
        return false;
    }

    private boolean isTooFarAway() {
        int attackRangeToCheck = attackRange;
        if(wasFiringAtTarget){
            attackRangeToCheck += 25; //voorkomt het constant roteren van locking/losing lock
        }

        Rectangle targetBounds = target.getBounds();
        double distance = ProtossUtils.getDistanceToRectangle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetBounds);
        return distance > attackRangeToCheck;
    }

    private void shootMissile() {
        MissileEnums missileType = MissileEnums.PlayerLaserbeam;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getChargingUpAttackAnimation().getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getChargingUpAttackAnimation().getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(ImageEnums.MotherShipDroneMissile);
        missileSpriteConfiguration.setScale(0.125f);

        float xMovementSpeed = 1.5f;
        float yMovementSpeed = 1.5f;
        Direction rotation = Direction.RIGHT;
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        PathFinder pathFinder = new StraightLinePathFinder();

        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                xMovementSpeed, yMovementSpeed, pathFinder, movementPatternSize, rotation
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        boolean isFriendly = false;
        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType
                , 100, 100, null, this.ownerOrCreator.getDamage() / 2, ImageEnums.Impact_Explosion_One, isFriendly, allowedToDealDamage, objectType,
                missileType.isUsesBoxCollision(), false, false, false);

        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(this);
        missile.setObjectType("Enemy Factory defender Missile");
        missile.resetMovementPath();

        Point point = new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        point.setX(point.getX() - missile.getWidth() / 2);
        point.setY(point.getY() - missile.getHeight() / 2);
        movementConfiguration.setDestination(point);
        missile.setCenterCoordinates(this.getChargingUpAttackAnimation().getCenterXCoordinate(), this.getChargingUpAttackAnimation().getCenterYCoordinate());
        missile.rotateObjectTowardsDestination(true);
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover
        missile.setOwnerOrCreator(this);
        MissileManager.getInstance().addExistingMissile(missile);
    }

}
