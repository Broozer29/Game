package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.items.firefighter.FlameDetonation;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class LingeringFlame extends SpecialAttack{
    private double gamesecondsStarted;
    private double duration;

    public LingeringFlame (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("Lingering Flame");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        this.appliesItemEffects = false;
        this.animation.setInfiniteLoop(true);
        super.showDamage = false;
        super.internalTickCooldown = PlayerStats.getInstance().getAttackSpeed();

        if(this.imageEnum.equals(ImageEnums.LingeringFlameLooping)) {
            this.addYOffset(-Math.round(this.getScale() * 35)); //To offset the large empty space in the spritesheet
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.FlameDetonation) != null){
            FlameDetonation flameDetonation = (FlameDetonation) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.FlameDetonation);
            this.duration = flameDetonation.getDuration();
        }

        initIgniteEffect();
        gamesecondsStarted = GameStateInfo.getInstance().getGameSeconds();
    }

    private void initIgniteEffect(){
        float duration = PlayerStats.getInstance().getFireFighterIgniteDuration();
        float damage = PlayerStats.getInstance().getFireFighterIgniteDamage();
        EffectInterface ignite = new DamageOverTime(damage, duration, EffectIdentifiers.Ignite);
        this.effectsToApply.add(ignite);
    }

    @Override
    public void startDissipating(){
    }


    @Override
    public void updateSpecialAttack() {
//        if (this.animation.getImageEnum().equals(ImageEnums.LingeringFlameLooping) &&
//                this.animation.getCurrentFrame() >= this.animation.getTotalFrames()) {
//            this.animation.changeImagetype(ImageEnums.LingeringFlameLoopingDissipating);
//            this.animation.setCurrentFrame(0);
//            this.animation.setAnimationScale(this.scale);
//        }

        if(GameStateInfo.getInstance().getGameSeconds() > (gamesecondsStarted + duration) && !isDissipating){
            this.setTransparancyAlpha(true, 1, -0.035f);
            super.isDissipating = true;
        }
    }


    public void setDuration(double duration){
        this.duration = duration;
    }
}
