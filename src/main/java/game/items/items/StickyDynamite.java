package game.items.items;

import VisualAndAudioData.image.ImageEnums;
import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.effectimplementations.DormentExplosion;
import game.items.effects.DormentExplosionActivationMethods;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.objects.GameObject;

public class StickyDynamite extends Item {

    private float explosionDamage;

    public StickyDynamite(){
        super(ItemEnums.StickyDynamite, 1, EffectActivationTypes.CheckEveryGameTick, ItemApplicationEnum.AfterCollision);
        this.explosionDamage = calculateExplosionDamage(quantity);
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        this.explosionDamage = calculateExplosionDamage(quantity);
    }

    private float calculateExplosionDamage (int quantity) {
        return 75f * quantity;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        DormentExplosion dormentExplosion = new DormentExplosion(explosionDamage, ImageEnums.StickyDynamiteExplosion, DormentExplosionActivationMethods.Timed, false
        , EffectIdentifiers.StickyDynamiteDormantExplosion, 2, effectType, false);
        gameObject.addEffect(dormentExplosion);
    }

}
