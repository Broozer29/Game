package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class PlasmaLauncher extends Item {

    public static float procChance = 0.1f;
    public static float damageMultiplier = 2;
    private Random rand;

    public PlasmaLauncher() {
        super(ItemEnums.PlasmaLauncher, 1, ItemApplicationEnum.AfterCollision);
        rand = new Random();
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        float chance = rand.nextFloat(0.0f, 1.01f); // Generates a float between 0.0 (inclusive) and 1.0 (exclusive)
        if (chance <= procChance) {
            createPlasmaMissile(gameObject);
        }
    }



    private void createPlasmaMissile(GameObject target) {
        GameObject player = PlayerManager.getInstance().getSpaceship();

        if (target == null) {
            return; //There is no target to fire at
        }

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(player.getXCoordinate());
        spriteConfiguration.setyCoordinate(player.getYCoordinate());
        spriteConfiguration.setImageType(ImageEnums.PlasmaLauncherMissile);
        spriteConfiguration.setScale(0.6f);

        int movementSpeed = 6;
        StraightLinePathFinder pathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, movementSpeed, pathFinder, MovementPatternSize.SMALL, Direction.RIGHT
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        boolean isFriendly = true;

        movementConfiguration.setDestination(
                new Point(
                        target.getCenterXCoordinate(),
                        target.getCenterYCoordinate()
                ));


        MissileConfiguration missileConfiguration = getMissileConfiguration(isFriendly);

        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.resetMovementPath();
        missile.getAnimation().changeImagetype(ImageEnums.PlasmaLauncherMissile);
        missile.getAnimation().setAnimationScale(0.6f);
        missile.setCenterCoordinates(PlayerManager.getInstance().getSpaceship().getXCoordinate() + PlayerManager.getInstance().getSpaceship().getWidth(),
                PlayerManager.getInstance().getSpaceship().getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(
                new Point(
                        target.getCenterXCoordinate() - (missile.getWidth() / 2),
                        target.getCenterYCoordinate() - (missile.getHeight() / 2)
                ));
        missile.setPiercesThroughObjects(true);
        missile.setAmountOfPiercesLeft(99999);
        missile.setOwnerOrCreator(player);
        missile.rotateObjectTowardsDestination(false);
        missile.setAllowedVisualsToRotate(false);
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private MissileConfiguration getMissileConfiguration(boolean isFriendly) {
        ImageEnums impactType = PlayerStats.getInstance().getPlayerMissileImpactImage();
        int maxHitPoints = 1000;
        int maxShields = 0;
        AudioEnums deathSound = null;

        boolean allowedToDealDamage = true;
        String objectType = "Plasma Launcher Missile";
        float damage = PlayerStats.getInstance().getBaseDamage() * (damageMultiplier * quantity);

        return MissileCreator.getInstance().createMissileConfiguration(MissileEnums.DefaultAnimatedBullet,
                maxHitPoints, maxShields, deathSound, damage, impactType, isFriendly, allowedToDealDamage,
                objectType, false, false, false, false);
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}
