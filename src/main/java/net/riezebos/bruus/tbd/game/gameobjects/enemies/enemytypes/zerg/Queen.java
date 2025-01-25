package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.movement.*;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;


public class Queen extends Enemy {

    private boolean isAttackingRightNow = false;
    private boolean laidEggDuringCurrentHover = false;

    public Queen (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.missileTypePathFinders = PathFinderEnums.StraightLine;
        this.damage = 15;
        this.attackSpeed = 4;
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
    }

    @Override
    public void fireAction() {
        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder) {
            if (!this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty()) {
                laidEggDuringCurrentHover = false;
            }

            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
        }

        // If the enemy is attacking, check if the animation has finished.
        if (isAttackingRightNow) {
            if (this.animation.getCurrentFrame() == this.animation.getTotalFrames() - 1) {
                isAttackingRightNow = false; // Attack is complete.
                changeAnimationToIdle(); // Return to idle state.
            }
            return; // Exit to ensure no new actions are taken while attacking.
        }

        // Egg-laying logic (independent of cooldown).
        if (determineAction()) {
            isAttackingRightNow = true;
            changeAnimationToAttacking();
            layEgg(); // Lay an egg immediately.
            laidEggDuringCurrentHover = true;
            return; // Exit early to prevent other actions during egg-laying.
        }

        // Check if the attack cooldown has been reached for shooting a missile.
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire) {
            isAttackingRightNow = true;
            changeAnimationToAttacking();
            rotateTowardsPlayer(); // Rotate towards the player before shooting.
            shootMissile();
            lastAttackTime = currentTime; // Reset cooldown after shooting.
        }

        // Reset animation to idle if no action was taken.
        if (!isAttackingRightNow) {
            changeAnimationToIdle();
        }
    }

    private void changeAnimationToAttacking () {
        if (!this.animation.getImageEnum().equals(ImageEnums.QueenAttacking)) {
            this.animation.changeImagetype(ImageEnums.QueenAttacking);
            this.animation.setCurrentFrame(0);
            this.animation.setFrameDelay(4);
            rotateTowardsPlayer();
        }

    }

    private void changeAnimationToIdle () {
        if (!this.animation.getImageEnum().equals(ImageEnums.QueenIdle)) {
            this.animation.changeImagetype(ImageEnums.QueenIdle);
            this.animation.setCurrentFrame(0);
            this.animation.setFrameDelay(4);
            rotateTowardsPlayer();
        }
    }


    private boolean determineAction () {
        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder &&
                !laidEggDuringCurrentHover && hoverPathFinder.isHovering() &&
                    (GameStateInfo.getInstance().getGameSeconds() > hoverPathFinder.getGameSecondsSinceEmptyList() + (hoverPathFinder.getSecondsToHoverStill() - 1))
//                &&
//                    GameStateInfo.getInstance().getGameSeconds() > lastTimeEggLaid + eggCooldown
        ) {
                return true;
            }
        return false;
    }

    private void rotateTowardsPlayer () {
        this.rotationAngle = 0;
        this.rotateGameObjectTowards(
                PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(),
                PlayerManager.getInstance().getSpaceship().getCenterYCoordinate(),
                true);
    }

    private void layEgg () {
        this.rotateGameObjectTowards(Direction.LEFT, false);
        int randomNumber = random.nextInt(0, 2);
        EnemyEnums selectedEnemy = null;
        if (randomNumber == 0) {
            selectedEnemy = EnemyEnums.DevourerCocoon;
        } else {
            selectedEnemy = EnemyEnums.MutaGuardianCocoon;
        }


        float scale = 1;
        Enemy enemy2 = EnemyCreator.createEnemy(selectedEnemy, this.getCenterXCoordinate(), this.getCenterYCoordinate(), Direction.LEFT, scale
                , selectedEnemy.getMovementSpeed(), selectedEnemy.getMovementSpeed(), MovementPatternSize.SMALL, false);

        enemy2.setCenterCoordinates(this.getCenterXCoordinate() - enemy2.getWidth(), this.getCenterYCoordinate());
        enemy2.move();
        EnemyManager.getInstance().addEnemy(enemy2);
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
        missile.getAnimation().changeImagetype(ImageEnums.GuardianMissile);
        missile.getDestructionAnimation().changeImagetype(ImageEnums.GuardianMissileImpact);
        missile.getAnimation().setFrameDelay(1);
        missile.setKnockbackStrength(5);

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

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }


}