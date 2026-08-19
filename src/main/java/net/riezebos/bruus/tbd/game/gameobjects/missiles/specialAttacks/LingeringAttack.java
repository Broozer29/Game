package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg.Mutalisk;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.items.items.firefighter.FlameDetonation;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class LingeringAttack extends SpecialAttack{
    private double gamesecondsStarted;
    private double duration;

    public LingeringAttack(SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration specialAttackConfiguration) {
        super(spriteAnimationConfiguration, specialAttackConfiguration);
        this.setObjectType("Lingering Attack");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        this.appliesItemEffects = false;
        this.animation.setInfiniteLoop(true);
        super.showDamage = false;

        //Its the lingering flame version
        if(this.imageEnum.equals(ImageEnums.LingeringFlameLooping)) {
            this.addYOffset(-Math.round(this.getScale() * 35)); //To offset the large empty space in the spritesheet
            if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.FlameDetonation) != null){
                FlameDetonation flameDetonation = (FlameDetonation) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.FlameDetonation);
                this.duration = flameDetonation.getDuration();
            }
            initIgniteEffect();
        } else if(this.imageEnum.equals(ImageEnums.PoisonCloud)){
            super.internalTickCooldown = 0.45; //roughly every 0.5 seconds it applies damage
            this.damage = specialAttackConfiguration.getDamage();
            this.duration = Mutalisk.cloudDuration + (EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.2f);
        }

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
        //todo code smell en ranzig, dit is nodig voor flame detonation maar waarom niet gewoon in de constructor?
        if(this.ownerOrCreator != null && this.ownerOrCreator instanceof SpaceShip){
            super.internalTickCooldown = this.ownerOrCreator.getAttackSpeed();
        }
        if(GameState.getInstance().getGameSeconds() > (gamesecondsStarted + duration) && !isDissipating){
            this.setTransparancyAlpha(true, 1, -0.035f);
            super.isDissipating = true;
        }
    }


    public void setDuration(double duration){
        this.duration = duration;
    }
}
