package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FireShield extends SpecialAttack {
    private double gamesecondsStarted;
    private double duration;
    public FireShield (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("FireShield");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        this.destroysMissiles = true;
        super.internalTickCooldown = PlayerStats.getInstance().getAttackSpeed();
        this.duration = 4;
        initIgniteEffect();
        gamesecondsStarted = GameState.getInstance().getGameSeconds();
    }

    private void initIgniteEffect(){
        float duration = PlayerStats.getInstance().getIgniteDuration();
        float damage = PlayerStats.getInstance().getIgniteDamage();
        EffectInterface ignite = new DamageOverTime(damage, duration, EffectIdentifiers.Ignite);
        this.effectsToApply.add(ignite);
    }

    @Override
    public void startDissipating(){
    }


    @Override
    public void updateSpecialAttack() {
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


    }
}
