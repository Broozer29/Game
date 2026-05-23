package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.util.VisualLayer;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FrontShield extends SpecialAttack {

    public FrontShield (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("FrontShield");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        this.destroysMissiles = true;
        this.damagesMissiles = true;
        this.visualLayer = VisualLayer.Upper;
        this.knockbackStrength = 14;
        super.internalTickCooldown = 0.15f;
    }


    @Override
    public void updateSpecialAttack() {
        //empty for now
    }

    public void startDissipating(){
        super.isDissipating = true;
        this.setTransparancyAlpha(true, 1, -0.035f);
    }

    public boolean isCompletelyDissipated(){
        return !this.visible;
    }
}

