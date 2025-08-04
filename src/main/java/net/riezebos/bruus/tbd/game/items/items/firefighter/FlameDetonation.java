package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships.AlienBomb;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.DormentExplosionActivationMethods;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DormentExplosion;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public class FlameDetonation extends Item {

    private float explosionDamage;
    private float burningDamage;
    public static float duration = 3;

    public FlameDetonation () {
        super(ItemEnums.FlameDetonation, 1, ItemApplicationEnum.BeforeCollision); //Before collision to ensure the explosion goes off even if the target already has 0 HP
        this.explosionDamage = 0;
        this.burningDamage = calculateBurningDamage(quantity);
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        this.burningDamage = calculateBurningDamage(quantity);
    }

    private float calculateBurningDamage (int quantity) {
        PlayerClass currentPlayerClass = PlayerStats.getInstance().getPlayerClass();
        switch (currentPlayerClass) {
            case FireFighter:
                return PlayerStats.getInstance().getIgniteDamage();
            default:
                return PlayerStats.getInstance().getBaseDamage() * 0.01f * quantity;
        }
    }




    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if (gameObject instanceof AlienBomb) { //AlienBombs should be immune to this
            return;
        }

        DormentExplosion dormentExplosion = new DormentExplosion(explosionDamage, ImageEnums.LingeringFlameLooping,
                DormentExplosionActivationMethods.OnDeath, false, EffectIdentifiers.FlameDetonationDormentExplosion,
                0, EffectActivationTypes.OnObjectDeath, true);
        dormentExplosion.setBurningDamage(burningDamage);
        dormentExplosion.setBurningDuration(duration * quantity);
        gameObject.addEffect(dormentExplosion);
    }

    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter);
    }

    public float getDuration () {
        return duration;
    }
}