package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.SpawnSpecialAttackOnDeath;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class Mutalisk extends Enemy {


    public static float cloudDuration = 3;
    public Mutalisk(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.hasAttack = false;
        this.attackSpeed = 2;
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.15f);
        initOnDeathEffect();
    }

    private void initOnDeathEffect() {
        SpawnSpecialAttackOnDeath spawnSpecialAttackOnDeath = new SpawnSpecialAttackOnDeath(EffectActivationTypes.OnObjectDeath, EffectIdentifiers.MutaliskDeathMissiles, ImageEnums.PoisonCloud);
        this.effects.add(spawnSpecialAttackOnDeath);
        //        SpawnProjectileOnDeath spawnProjectileOnDeath = new SpawnProjectileOnDeath(EffectActivationTypes.OnObjectDeath,
//                EffectIdentifiers.MutaliskDeathMissiles,
//                3, MissileEnums.DefaultAnimatedBullet, ImageEnums.MutaliskMissile, ImageEnums.MutaliskMissileImpact);
//
//        this.effects.add(spawnProjectileOnDeath);
    }


    @Override
    public void fireAction() {
        if (this.rotationAngle == 180) {
            //todo This is a very shoddy fix to prevent bad cropping, probably required for 90, 270 & 0 too, requires test
            this.setAllowedVisualsToRotate(true);
            this.rotateObjectTowardsAngle(181, true);
            this.setAllowedVisualsToRotate(false);
        } else if (rotationAngle == 0){
            this.setAllowedVisualsToRotate(true);
            this.rotateObjectTowardsAngle(1, true);
            this.setAllowedVisualsToRotate(false);
        }
    }


}
