package net.riezebos.bruus.tbd.game.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemTypeEnums;

import java.util.ArrayList;
import java.util.List;

//The SHOPKEEPER filters that are switched on, by enum name, as stored in settings.json
//The lists hold whatever the file says; repair() drops the names that are not valid values
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopkeeperFilters {

    private List<String> classes = new ArrayList<>();
    private List<String> rarities = new ArrayList<>();
    private List<String> types = new ArrayList<>();

    public ShopkeeperFilters() {
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes != null ? classes : new ArrayList<>();
    }

    public List<String> getRarities() {
        return rarities;
    }

    public void setRarities(List<String> rarities) {
        this.rarities = rarities != null ? rarities : new ArrayList<>();
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types != null ? types : new ArrayList<>();
    }

    //A separate object with its own lists
    public ShopkeeperFilters copyOf() {
        ShopkeeperFilters copy = new ShopkeeperFilters();
        copy.classes = new ArrayList<>(this.classes);
        copy.rarities = new ArrayList<>(this.rarities);
        copy.types = new ArrayList<>(this.types);
        return copy;
    }

    //Drops the names that are not a valid value of their enum, and repeated names
    public void repair() {
        classes = keepValidNames(classes, PlayerClass.class);
        rarities = keepValidNames(rarities, ItemRarityEnums.class);
        types = keepValidNames(types, ItemTypeEnums.class);
    }

    private static <E extends Enum<E>> List<String> keepValidNames(List<String> names, Class<E> enumClass) {
        List<String> valid = new ArrayList<>();
        if (names == null) {
            return valid;
        }
        for (String name : names) {
            try {
                Enum.valueOf(enumClass, name);
                if (!valid.contains(name)) {
                    valid.add(name);
                }
            } catch (IllegalArgumentException | NullPointerException e) {
                //Not a value of this enum, so it is dropped
            }
        }
        return valid;
    }
}
