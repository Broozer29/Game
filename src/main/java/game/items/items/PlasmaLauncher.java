package game.items.items;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.movement.Direction;
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

        ImageEnums impactType = PlayerStats.getInstance().getPlayerMissileImpactImage();
        PathFinder pathFinder = new HomingPathFinder();

        int maxHitPoints = 1000;
        int maxShields = 0;
        AudioEnums deathSound = null;
        boolean isFriendly = true;
        boolean allowedToDealDamage = true;
        String objectType = "Plasma Launcher Missile";
        float damage = PlayerStats.getInstance().getNormalAttackDamage() * damageMultiplier;
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;

        MissileConfiguration missileConfiguration = new MissileConfiguration(MissileTypeEnums.PlasmaLauncherMissile,
                maxHitPoints, maxShields, deathSound, impactType, isFriendly, pathFinder, Direction.RIGHT, xMovementSpeed,
                yMovementSpeed, allowedToDealDamage, objectType, damage, movementPatternSize, false);


        if (pathFinder instanceof HomingPathFinder) {
            missileConfiguration.setTargetToChase(((HomingPathFinder) pathFinder).getTarget(isFriendly, spriteConfiguration.getxCoordinate(), spriteConfiguration.getyCoordinate()));
        }

        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration);
        missile.setOwnerOrCreator(player);
        MissileManager.getInstance().addExistingMissile(missile);
    }

}
