package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.SpawnProjectileOnDeath;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class Mutalisk extends Enemy {

    public Mutalisk (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 15;
        this.hasAttack = false;
        this.attackSpeed = 2;
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
        initOnDeathEffect();
    }

    private void initOnDeathEffect(){
        SpawnProjectileOnDeath spawnProjectileOnDeath = new SpawnProjectileOnDeath(EffectActivationTypes.OnObjectDeath,
                EffectIdentifiers.MutaliskDeathMissiles,
                3, MissileEnums.DefaultAnimatedBullet, ImageEnums.MutaliskMissile, ImageEnums.MutaliskMissileImpact);

        this.effects.add(spawnProjectileOnDeath);
    }


    @Override
    public void fireAction () {
        if(this.rotationAngle == 180){
            //This is a very shoddy fix to prevent bad cropping, probably required for 90, 270 & 0 too, requires test
            this.setAllowedVisualsToRotate(true);
            this.rotateObjectTowardsAngle(181, true);
            this.setAllowedVisualsToRotate(false);
        }
    }



}
