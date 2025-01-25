package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
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

    private float procChance;
    private float damageMultiplier;
    private Random rand;

    public PlasmaLauncher () {
        super(ItemEnums.PlasmaLauncher, 1, ItemApplicationEnum.AfterCollision);
        procChance = 0.1f;
        calculateDamage();
        rand = new Random();
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateDamage();
    }

    private void calculateDamage () {
        damageMultiplier = quantity * 2;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        float chance = rand.nextFloat(0.0f, 1.01f); // Generates a float between 0.0 (inclusive) and 1.0 (exclusive)
        if (chance <= procChance) {
            createPlasmaMissile();
        }
    }

    private void createPlasmaMissile () {
        GameObject player = PlayerManager.getInstance().getSpaceship();

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
        Enemy target = EnemyManager.getInstance().getClosestEnemy(player.getCenterXCoordinate(), player.getCenterYCoordinate());

        movementConfiguration.setDestination(
                new Point(
                        target.getCenterXCoordinate(),
                        target.getCenterYCoordinate()
                ));


        MissileConfiguration missileConfiguration = getMissileConfiguration(isFriendly);

        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.resetMovementPath();
        missile.getAnimation().changeImagetype(ImageEnums.PlasmaLauncherMissile);

        missile.getMovementConfiguration().setDestination(
                new Point(
                        target.getCenterXCoordinate() - (missile.getWidth() / 2),
                        target.getCenterYCoordinate() - (missile.getHeight() / 2)
                ));

        missile.setOwnerOrCreator(player);
        missile.setAllowedVisualsToRotate(false);
        missile.getAnimation().setAnimationScale(0.6f);
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private MissileConfiguration getMissileConfiguration (boolean isFriendly) {
        ImageEnums impactType = PlayerStats.getInstance().getPlayerMissileImpactImage();
        int maxHitPoints = 1000;
        int maxShields = 0;
        AudioEnums deathSound = null;

        boolean allowedToDealDamage = true;
        String objectType = "Plasma Launcher Missile";
        float damage = PlayerStats.getInstance().getBaseDamage() * damageMultiplier;

        return MissileCreator.getInstance().createMissileConfiguration(MissileEnums.DefaultAnimatedBullet,
                maxHitPoints, maxShields, deathSound, damage, impactType, isFriendly, allowedToDealDamage,
                objectType, false, false, false, false);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}
