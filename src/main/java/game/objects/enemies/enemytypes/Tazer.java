package game.objects.enemies.enemytypes;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.BouncingPathFinder;
import game.movement.pathfinders.HoverPathFinder;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyConfiguration;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileEnums;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tazer extends Enemy {

    private List<Direction> missileDirections = new ArrayList<>();
    private Direction randomDirection = null;

    public Tazer (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

//        SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
//        exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Tazer_Normal_Exhaust);
//        this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Tazer_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

        this.damage = MissileEnums.TazerProjectile.getDamage();
        initDirectionFromRotation();

    }


    public void fireAction () {
        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder){
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
        }
        // Check if the attack cooldown has been reached
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
        && allowedToFire) {

            if(randomDirection == null) {
                randomDirection = selectRandomMissileDirection();
            }
            this.rotateGameObjectTowards(randomDirection, true);
            this.allowedVisualsToRotate = false;

            if (!chargingUpAttackAnimation.isPlaying()) {
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }

            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                shootMissile(randomDirection);
                lastAttackTime = currentTime; // Update the last attack time after firing
                randomDirection = null;
            }
        }

        if(!allowedToFire){
            this.allowedVisualsToRotate = true;
        }
    }


    private void shootMissile (Direction randomDirection) {
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate, MissileEnums.TazerProjectile.getImageType()
                ,1);



        //Create missile movement attributes and create a movement configuration
        MissileEnums missileType = MissileEnums.TazerProjectile;
        BouncingPathFinder missilePathFinder = new BouncingPathFinder();
        missilePathFinder.setMaxBounces(1);
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                2,2, missilePathFinder, movementPatternSize, randomDirection
        );

        this.rotateGameObjectTowards(randomDirection, false);
        this.allowedVisualsToRotate = false;

        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Tazer Projectile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
//        missile.getDestructionAnimation().setFrameDelay(1);
//        missile.getDestructionAnimation().setAnimationScale(2f);
        missile.setOwnerOrCreator(this);
        missile.getAnimation().setAnimationScale(1f);
        missile.setAllowedVisualsToRotate(false);
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.resetMovementPath();

        this.missileManager.addExistingMissile(missile);
    }


    private void initDirectionFromRotation() {
        switch (this.movementConfiguration.getRotation()) {
            case DOWN:
                missileDirections.add(Direction.LEFT_DOWN);
                missileDirections.add(Direction.DOWN);
                missileDirections.add(Direction.RIGHT_DOWN);
                break;
            case LEFT:
            case LEFT_DOWN:
            case LEFT_UP:
                missileDirections.add(Direction.LEFT_DOWN);
                missileDirections.add(Direction.LEFT);
                missileDirections.add(Direction.LEFT_UP);
                break;
            case NONE:
                missileDirections.add(Direction.LEFT);
                break;
            case RIGHT:
            case RIGHT_DOWN:
            case RIGHT_UP:
                missileDirections.add(Direction.RIGHT_UP);
                missileDirections.add(Direction.RIGHT);
                missileDirections.add(Direction.RIGHT_DOWN);
                break;
            case UP:
                missileDirections.add(Direction.LEFT_UP);
                missileDirections.add(Direction.UP);
                missileDirections.add(Direction.RIGHT_UP);
                break;
            default:
                missileDirections.add(Direction.LEFT);
                break;

        }
    }

    private Direction selectRandomMissileDirection(){
        Random rand = new Random();
        return missileDirections.get(random.nextInt(missileDirections.size()));
    }
}