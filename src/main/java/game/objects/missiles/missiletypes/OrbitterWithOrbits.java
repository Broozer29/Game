package game.objects.missiles.missiletypes;

import VisualAndAudioData.image.ImageEnums;
import com.badlogic.gdx.Game;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.OrbitPathFinder;
import game.objects.GameObject;
import game.objects.missiles.*;
import game.objects.neutral.Explosion;
import game.objects.neutral.ExplosionConfiguration;
import game.objects.neutral.ExplosionManager;
import game.util.OrbitingObjectsFormatter;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class OrbitterWithOrbits extends Missile {
    private List<GameObject> firstWaveOfOrbits = new ArrayList<>();

    public OrbitterWithOrbits (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        this.rotateImage(movementConfiguration.getRotation());
        initDestructionAnimation(missileConfiguration, movementConfiguration);
        this.isDestructable = true;
    }

    public OrbitterWithOrbits(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration){
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
        initDestructionAnimation(missileConfiguration, movementConfiguration);
        this.isDestructable = true;
    }

    private void initOrbits() {
        int orbitRadius = 150;
        for (int i = 0; i < 8; i++) {
            Missile missile = createOrbitingMissile(this, orbitRadius);
            firstWaveOfOrbits.add(missile);
            MissileManager.getInstance().addExistingMissile(missile);
        }

        OrbitingObjectsFormatter.reformatOrbitingObjects(this, orbitRadius);
        for (GameObject object : objectOrbitingThis) {
            object.resetMovementPath();
        }
    }

    private void createSecondWaveOfOrbits() {
        int orbitRadius = 100;
        for (GameObject object : firstWaveOfOrbits) {
            for (int i = 0; i < 8; i++) {
                Missile missile = createOrbitingMissile(object, orbitRadius);
                MissileManager.getInstance().addExistingMissile(missile);
            }

            OrbitingObjectsFormatter.reformatOrbitingObjects(object, orbitRadius);
            for (GameObject orbitingMissile : object.getObjectOrbitingThis()) {
                orbitingMissile.resetMovementPath();
            }
        }
    }

    private Missile createOrbitingMissile(GameObject target, int orbitRadius) {
        SpriteConfiguration spriteConfig = MissileCreator.getInstance().createMissileSpriteConfig(
                target.getXCoordinate(), target.getYCoordinate(),
                MissileEnums.Orbitter.getImageType(), 0.2f);

        MovementConfiguration movementConfiguration1 = target.getMovementConfiguration();
        MovementConfiguration moveConfig = MissileCreator.getInstance().createMissileMovementConfig(
                movementConfiguration1.getXMovementSpeed(), movementConfiguration1.getYMovementSpeed(),
                new OrbitPathFinder(target, 85, 300, 0), MovementPatternSize.SMALL,
                movementConfiguration1.getRotation());
        moveConfig.initDefaultSettingsForSpecializedPathFinders();
        moveConfig.setLastKnownTargetX(target.getCenterXCoordinate());
        moveConfig.setLastKnownTargetY(target.getCenterYCoordinate());
        moveConfig.setLastUsedXMovementSpeed(movementConfiguration1.getXMovementSpeed());
        moveConfig.setLastUsedYMovementSpeed(movementConfiguration1.getYMovementSpeed());
        moveConfig.setOrbitRadius(orbitRadius);

        MissileConfiguration missileConfig = MissileCreator.getInstance().createMissileConfiguration(
                MissileEnums.Orbitter, 100, 0, null, MissileEnums.Orbitter.getDamage(),
                MissileEnums.Orbitter.getDeathOrExplosionImageEnum(),
                target.isFriendly(), true, "OrbitterMissile", false, false, true);

        Missile orbitingMissile = MissileCreator.getInstance().createMissile(spriteConfig, missileConfig, moveConfig);
        orbitingMissile.setAllowedVisualsToRotate(false);
        target.getObjectOrbitingThis().add(orbitingMissile);
        return orbitingMissile;
    }


    private void initDestructionAnimation(MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration){
        if(missileConfiguration.getDestructionType() != null){
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);

            if(this.destructionAnimation.getImageType().equals(ImageEnums.LaserBulletDestruction)){
                this.destructionAnimation.rotateAnimation(movementConfiguration.getRotation(), false);
                this.destructionAnimation.setFrameDelay(1);
            }
        }
    }


    boolean hasFired = false;
    public void missileAction () {
        if(!hasFired) {
            initOrbits();
//            createSecondWaveOfOrbits();
            hasFired = true;
        }
    }

}