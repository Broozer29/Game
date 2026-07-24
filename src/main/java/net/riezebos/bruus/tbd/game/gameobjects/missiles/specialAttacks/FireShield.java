package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.items.items.firefighter.RingOfFire;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.VisualLayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class FireShield extends SpecialAttack {
    private double gamesecondsStarted;
    private double duration;
    public FireShield (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("FireShield");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        this.destroysMissiles = true;
        this.damagesMissiles = true;
        this.duration = 4;
        this.effectsToApply.add(initIgniteEffect());
        gamesecondsStarted = GameState.getInstance().getGameSeconds();
        this.visualLayer = VisualLayer.Lower;
    }

    private EffectInterface initIgniteEffect(){
        float duration = PlayerStats.getInstance().getIgniteDuration();
        float damage = PlayerStats.getInstance().getIgniteDamage();
        EffectInterface ignite = new DamageOverTime(damage, duration, EffectIdentifiers.Ignite);
        return ignite;
    }

    @Override
    public void updateSpecialAttack() {
        super.internalTickCooldown = this.ownerOrCreator.getAttackSpeed(); //todo dit is eigenlijk een initialize variabele maar owner word gezet na de constructor, code smell

        if (this.animation.getImageEnum().equals(ImageEnums.FireFighterFireShieldAppearing) &&
                this.animation.getCurrentFrame() >= this.animation.getTotalFrames()) {
            this.animation.changeImagetype(ImageEnums.FireFighterFireShield);
            this.animation.setCurrentFrame(0);
            this.animation.setAnimationScale(this.scale);
        }

        if(GameState.getInstance().getGameSeconds() > (gamesecondsStarted + duration) && !isDissipating){
            this.setTransparancyAlpha(true, 1, -0.035f);
            super.isDissipating = true;
        }
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.RingOfFire) != null && !super.isDissipating){
            handleRingOfFireRelic();
        }
    }


    //----------------------------------Ring of Fire----------------------------------
    private double lastSecondsFired = GameState.getInstance().getGameSeconds();
    private int angleIncrement = 60;
    private void handleRingOfFireRelic(){
        if(GameState.getInstance().getGameSeconds() > lastSecondsFired + RingOfFire.projectileCooldown){
            createMissileRing();
            lastSecondsFired = GameState.getInstance().getGameSeconds();

        }
    }

    private void createMissileRing(){
        angleIncrement = 6;
        for (int angle = 0; angle < (361 - angleIncrement); angle += angleIncrement) {
            // Directly call shootMissiles using current angle
            shootMissiles(angle);
        }
    }

    private void shootMissiles(double angleDegrees) {
        MissileEnums missileType = MissileEnums.DefaultAnimatedBullet;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(ImageEnums.AlienLaserBeamAnimatedOrange);
        missileSpriteConfiguration.setScale(1);


        float movementSpeed = 6;
        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, Direction.RIGHT
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = this.isFriendly();
        float damage = PlayerStats.fireFighterBaseDamage * RingOfFire.projectileDamage;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                damage, missileType.getDeathOrExplosionImageEnum(), isFriendly,
                false, true, true);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);


        //Calculate the angle based on the current chargingAnimation. Because we want to fire from 4 directions, we also need to keep
        //track of the angle that the given chargingAnimation has in this method
        Point bulletOrigin = calculateBulletDestination(angleDegrees, 60, this.getCenterXCoordinate(), this.getCenterYCoordinate());
        Point bulletDestination = calculateBulletDestination(angleDegrees, 900, this.getCenterXCoordinate(), this.getCenterYCoordinate());

        missile.setTransparancyAlpha(false, 0.85f, 0);
        missile.resetMovementPath();

        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.getMovementConfiguration().setDestination(bulletDestination); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this.getOwnerOrCreator());
        missile.addEffectToApply(initIgniteEffect());
        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Point calculateBulletDestination(double angleDegrees, int distance, int centerX, int centerY) {
        // Convert the angle from degrees to radians because Math functions use radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }
    //----------------------------------End of Ring of Fire----------------------------------
}
