package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemTypeEnums;
import net.riezebos.bruus.tbd.game.settings.ShopkeeperFilters;

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

    //Replaces the switched-on filters with the saved ones; names that are not valid enum values are skipped
    public void load (ShopkeeperFilters saved) {
        clear();
        for (String name : saved.getClasses()) {
            try {
                classes.add(PlayerClass.valueOf(name));
            } catch (IllegalArgumentException e) {
                //Not a class any more
            }
        }
        for (String name : saved.getRarities()) {
            try {
                rarities.add(ItemRarityEnums.valueOf(name));
            } catch (IllegalArgumentException e) {
                //Not a rarity any more
            }
        }
        for (String name : saved.getTypes()) {
            try {
                types.add(ItemTypeEnums.valueOf(name));
            } catch (IllegalArgumentException e) {
                //Not a type any more
            }
        }
    }

    //The switched-on filters by name, the form settings.json holds
    public ShopkeeperFilters toSaved () {
        ShopkeeperFilters saved = new ShopkeeperFilters();
        for (PlayerClass playerClass : classes) {
            saved.getClasses().add(playerClass.name());
        }
        for (ItemRarityEnums rarity : rarities) {
            saved.getRarities().add(rarity.name());
        }
        for (ItemTypeEnums type : types) {
            saved.getTypes().add(type.name());
        }
        return saved;
    }

    public void clear () {
        classes.clear();
        rarities.clear();
        types.clear();
    }
}
