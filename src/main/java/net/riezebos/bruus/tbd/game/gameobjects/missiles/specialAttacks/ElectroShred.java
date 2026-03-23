package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class ElectroShred extends SpecialAttack {
    public ElectroShred (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("ElectroShred");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = true;
        super.internalTickCooldown = 0.15f;
        super.showDamage = false;
    }

    @Override
    public boolean isDestroysMissiles() {
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.AnionInverter) != null){
            return false; //overwrite it in any case if the item exists
        }
        return destroysMissiles;
    }
}