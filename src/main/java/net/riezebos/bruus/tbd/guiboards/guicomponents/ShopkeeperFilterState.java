package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemTypeEnums;

import java.util.EnumSet;
import java.util.Set;

//Which filters are switched on in the SHOPKEEPER page, kept as one object so it can be loaded and saved as a whole
//A group with nothing switched on does not filter
public class ShopkeeperFilterState {

    private final Set<PlayerClass> classes = EnumSet.noneOf(PlayerClass.class);
    private final Set<ItemRarityEnums> rarities = EnumSet.noneOf(ItemRarityEnums.class);
    private final Set<ItemTypeEnums> types = EnumSet.noneOf(ItemTypeEnums.class);

    public Set<PlayerClass> getClasses () {
        return classes;
    }

    public Set<ItemRarityEnums> getRarities () {
        return rarities;
    }

    public Set<ItemTypeEnums> getTypes () {
        return types;
    }

    public boolean isEmpty () {
        return classes.isEmpty() && rarities.isEmpty() && types.isEmpty();
    }

    public void clear () {
        classes.clear();
        rarities.clear();
        types.clear();
    }
}
