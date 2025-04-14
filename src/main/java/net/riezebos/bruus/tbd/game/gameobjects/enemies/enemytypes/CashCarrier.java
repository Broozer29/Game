package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.GainGoldOnDeath;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class CashCarrier extends Enemy {
    public CashCarrier (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale * 1.5f);

        //Gold gain should be 0, as this effect is cosmetic ONLY!
        GainGoldOnDeath goldOnDeathEffect = new GainGoldOnDeath(EffectActivationTypes.OnObjectDeath, EffectIdentifiers.CashCarrierGoldGain, 0);
        this.addEffect(goldOnDeathEffect);
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;

        if(LevelManager.getInstance().getCurrentEnemyTribe().equals(EnemyTribes.Zerg)){
            initAsOverlord();
        }
    }

    private void initAsOverlord(){
        this.getAnimation().changeImagetype(ImageEnums.Overlord);
        this.getAnimation().setFrameDelay(9);
        this.setAllowedVisualsToRotate(true);
        this.setDeathSound(AudioEnums.OverlordDeath);
        this.rotateObjectTowardsRotation(true);
        this.getDestructionAnimation().changeImagetype(ImageEnums.GuardianDeath);
        this.getDestructionAnimation().setFrameDelay(3);
    }

    public void fireAction(){
        //Probably do nothing?
    }

}
