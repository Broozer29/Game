package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.util.VisualLayer;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FrontShield extends SpecialAttack {

    public FrontShield (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("FireShield");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        this.destroysMissiles = true;
        this.damagesMissiles = true;
        this.visualLayer = VisualLayer.Upper;
        this.knockbackStrength = 14;
    }


    @Override
    public void updateSpecialAttack() {
        super.internalTickCooldown = this.ownerOrCreator.getAttackSpeed(); //todo dit is eigenlijk een initialize variabele maar owner word gezet na de constructor, code smell
    }

    public void startDissipating(){
        super.isDissipating = true;
        this.setTransparancyAlpha(true, 1, -0.035f);
    }

    public boolean isCompletelyDissipated(){
        return !this.visible;
    }
}

