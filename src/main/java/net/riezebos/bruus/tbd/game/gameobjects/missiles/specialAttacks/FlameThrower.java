package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.WithoutGasItsAss;
import net.riezebos.bruus.tbd.game.util.VisualLayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FlameThrower extends SpecialAttack {


    public FlameThrower (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("FlameThrower");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        initIgniteEffect();
        super.damagesMissiles = true;
        super.maxHPDamagePercentageForMissiles = 0.045f;
        super.visualLayer = VisualLayer.Lower;

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.FireWithoutGasIsAss) != null){
            initIgniteMagnificationEffect();
        }
    }

    private void initIgniteEffect(){
        float duration = PlayerStats.getInstance().getIgniteDuration();
        float damage = PlayerStats.getInstance().getIgniteDamage();
        EffectInterface ignite = new DamageOverTime(damage, duration, EffectIdentifiers.Ignite);
        this.effectsToApply.add(ignite);
    }

    private void initIgniteMagnificationEffect(){
        float duration = 0.05f;
        EffectInterface igniteModifier = new WithoutGasItsAss(duration, EffectIdentifiers.WithoutGasItsAssDamageBonus);
        this.effectsToApply.add(igniteModifier);
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
        if(this.ownerOrCreator instanceof SpaceShip && PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.InfernalPreIgniter) != null){
            PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.InfernalPreIgniter).applyEffectToObject(this);
        }

        if(this.ownerOrCreator instanceof SpaceShip && (ownerOrCreator.getCurrentHitpoints() <= 0 || !ownerOrCreator.isVisible())){
            this.setVisible(false);
        }
        super.internalTickCooldown = this.ownerOrCreator.getAttackSpeed(); //todo dit is eigenlijk een initialize variabele maar owner word gezet na de constructor, code smell


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
