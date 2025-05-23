package net.riezebos.bruus.tbd.guiboards.util;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerPrimaryAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerSpecialAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public class WeaponDescription {

    private String name;
    private String description;

    private PlayerPrimaryAttackTypes attackType;
    private PlayerSpecialAttackTypes specialAttackType;


    public WeaponDescription (String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setAttackType (PlayerPrimaryAttackTypes attackType) {
        this.attackType = attackType;
    }

    public void setSpecialAttackType (PlayerSpecialAttackTypes specialAttackType) {
        this.specialAttackType = specialAttackType;
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
