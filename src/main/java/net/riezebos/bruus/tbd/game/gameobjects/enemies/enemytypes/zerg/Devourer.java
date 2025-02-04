package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.AttackSpeedModifierEffect;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.ModifyMovementSpeedEffect;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.PathFinderEnums;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class Devourer extends Enemy {

    private boolean isAttackingRightNow = false;

    public Devourer (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.missileTypePathFinders = PathFinderEnums.StraightLine;
        this.damage = 15;
        this.attackSpeed = 4;
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
//        this.attackSpeed = 1;
    }

    @Override
    public void fireAction () {
        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder) {
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
            if (!allowedToFire && this.animation.getImageEnum().equals(ImageEnums.DevourerAttacking)) {
                this.animation.changeImagetype(ImageEnums.DevourerIdle);
                this.animation.setCurrentFrame(0);
                this.animation.setFrameDelay(4);
                rotateTowardsPlayer();
            }
        }
        // Check if the attack cooldown has been reached
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire) {
            if (!isAttackingRightNow) {
                isAttackingRightNow = true;
                this.animation.changeImagetype(ImageEnums.DevourerAttacking);
                this.animation.setCurrentFrame(0);
                this.animation.setFrameDelay(4);
                rotateTowardsPlayer();
                this.isAttacking = true;
            }


            if (isAttackingRightNow && this.animation.getCurrentFrame() == this.animation.getTotalFrames() &&
                    this.animation.getImageEnum().equals(ImageEnums.DevourerAttacking)) {
                shootMissile();
                this.isAttacking = false;
                isAttackingRightNow = false;
                lastAttackTime = currentTime;
                this.animation.changeImagetype(ImageEnums.DevourerIdle);
                this.animation.setCurrentFrame(0);
                this.animation.setFrameDelay(2);
                rotateTowardsPlayer();
            }
        }
    }

    private void rotateTowardsPlayer(){
        this.rotationAngle = 0;
        this.rotateGameObjectTowards(
                PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(),
                PlayerManager.getInstance().getSpaceship().getCenterYCoordinate(),
                true);
    }


    private void shootMissile () {
        MissileEnums missileType = MissileEnums.DefaultAnimatedBullet;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                missileType.getImageType(), 1);


        int movementSpeed = 5;
        //Create missile movement attributes and create a movement configuration

        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, this.movementRotation
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Devourer Missile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.getAnimation().changeImagetype(ImageEnums.DevourerMissile);
        missile.getDestructionAnimation().changeImagetype(ImageEnums.DevourerMissileExplosion);
        missile.getAnimation().setFrameDelay(1);

        //get the coordinates for rotation of the missile
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        Point rotationCoordinates = new Point(
                spaceship.getCenterXCoordinate() - (missile.getAnimation().getWidth() / 2),
                spaceship.getCenterYCoordinate() - (missile.getAnimation().getHeight() / 2) + missile.getHeight() * 0.3f //Small offset because it aims too high, probably because of non-cropped
        );

        // Any visual resizing BEFORE replacing starting coordinates and rotation

        missile.resetMovementPath();

        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(rotationCoordinates); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);
        missile.addEffectToApply(getDevourerSlowEffect());
        missile.addEffectToApply(getDevourerAttackSpeedEffect());

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private EffectInterface getDevourerSlowEffect(){
        SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
        spriteConfiguration1.setImageType(ImageEnums.DevourerDebuffStage1);
        spriteConfiguration1.setxCoordinate(-100);
        spriteConfiguration1.setyCoordinate(-100);
        spriteConfiguration1.setScale(1);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 2, true);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);

        return new ModifyMovementSpeedEffect(-0.15f, 4, spriteAnimation, EffectIdentifiers.DevourerMoveSpeedDebuff);
    }

    private EffectInterface getDevourerAttackSpeedEffect(){

        SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
        spriteConfiguration1.setImageType(ImageEnums.DevourerDebuffStage1);
        spriteConfiguration1.setxCoordinate(-100);
        spriteConfiguration1.setyCoordinate(-100);
        spriteConfiguration1.setScale(1);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 2, true);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        return new AttackSpeedModifierEffect(-0.3f, 4, spriteAnimation, EffectIdentifiers.DevourerAttackSpeedDebuff);
    }

}
