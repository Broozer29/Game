package net.riezebos.bruus.tbd.guiboards.util;

public class WeaponDescription {

    private String name;
    private String description;


    public WeaponDescription (String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }
}
