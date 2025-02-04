package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.util.VisualLayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FlameThrower extends SpecialAttack {

    public FlameThrower (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("FlameThrower");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        super.internalTickCooldown = PlayerStats.getInstance().getAttackSpeed();
        initIgniteEffect();
        super.damagesMissiles = true;
        super.maxHPDamagePercentage = 0.095f;
        super.visualLayer = VisualLayer.Lower;
    }

    private void initIgniteEffect(){
        float duration = PlayerStats.getInstance().getFireFighterIgniteDuration();
        float damage = PlayerStats.getInstance().getFireFighterIgniteDamage();
        EffectInterface ignite = new DamageOverTime(damage, duration, EffectIdentifiers.Ignite);
        this.effectsToApply.add(ignite);
    }

    @Override
    public void startDissipating(){
        this.animation.changeImagetype(ImageEnums.FireFighterFlameThrowerDissipating);
        this.animation.setCurrentFrame(0);
        this.animation.setAnimationScale(this.scale);
        this.animation.setInfiniteLoop(false);
        super.isDissipating = true;
    }


    @Override
    public void updateSpecialAttack() {
        if (this.animation.getImageEnum().equals(ImageEnums.FireFighterFlameThrowerAppearing) &&
                this.animation.getCurrentFrame() >= this.animation.getTotalFrames()) {
            this.animation.changeImagetype(ImageEnums.FireFighterFlameThrowerLooping);
            this.animation.setCurrentFrame(0);
            this.animation.setAnimationScale(this.scale);
        }

        if (this.animation.getImageEnum().equals(ImageEnums.FireFighterFlameThrowerDissipating) &&
                this.animation.getCurrentFrame() >= this.animation.getTotalFrames() - 1) {
            this.visible = false;
        }
    }
}
