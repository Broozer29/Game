package game.items.items;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import game.objects.missiles.*;
import game.objects.player.PlayerManager;
import game.objects.player.PlayerStats;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class PlasmaLauncher extends Item {

    private float procChance;
    private float damageMultiplier;
    private Random rand;

    public PlasmaLauncher () {
        super(ItemEnums.PlasmaLauncher, 1, EffectActivationTypes.AfterAHit, ItemApplicationEnum.AfterCollision);
        procChance = 0.2f;
        calculateDamage();
        rand = new Random();
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateDamage();
    }

    private void calculateDamage () {
        damageMultiplier = quantity * 3f;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        float chance = rand.nextFloat(); // Generates a float between 0.0 (inclusive) and 1.0 (exclusive)
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

        int xMovementSpeed = 3;
        int yMovementSpeed = 3;
        Direction rotation = Direction.RIGHT;
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        PathFinder pathFinder = new HomingPathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                xMovementSpeed, yMovementSpeed, pathFinder, movementPatternSize, rotation
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();


        boolean isFriendly = true;
        if (pathFinder instanceof HomingPathFinder) {
            movementConfiguration.setTargetToChase(((HomingPathFinder) pathFinder).getTarget(isFriendly, spriteConfiguration.getxCoordinate(), spriteConfiguration.getyCoordinate()));
        }

        MissileConfiguration missileConfiguration = getMissileConfiguration(isFriendly);

        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(player);
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private MissileConfiguration getMissileConfiguration (boolean isFriendly) {
        ImageEnums impactType = PlayerStats.getInstance().getPlayerMissileImpactImage();
        int maxHitPoints = 1000;
        int maxShields = 0;
        AudioEnums deathSound = null;

        boolean allowedToDealDamage = true;
        String objectType = "Plasma Launcher Missile";
        float damage = PlayerStats.getInstance().getNormalAttackDamage() * damageMultiplier;

        MissileConfiguration missileConfiguration = new MissileConfiguration(MissileTypeEnums.PlasmaLauncherMissile,
                maxHitPoints, maxShields, deathSound, damage, impactType, isFriendly, allowedToDealDamage,
                objectType, false);
        return missileConfiguration;
    }

}