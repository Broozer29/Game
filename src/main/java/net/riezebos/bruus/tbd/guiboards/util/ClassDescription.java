package net.riezebos.bruus.tbd.guiboards.util;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;

import java.util.HashMap;
import java.util.Map;

public class ClassDescription {

    private static final Map<PlayerClass, ClassDescription> cache = new HashMap<>();  // Static map to store instances

    private String title;
    private String description;
    private float baseDamage;
    private float baseAttackSpeed;

    // Private constructor to ensure instances are only created through the getInstance method
    private ClassDescription(PlayerClass playerClass) {
        updateClassDescriptionBox(playerClass);
    }

    // Static method to return an existing instance or create a new one if needed
    public static ClassDescription getInstance(PlayerClass playerClass) {
        if (!cache.containsKey(playerClass)) {
            cache.put(playerClass, new ClassDescription(playerClass));
        }
        return cache.get(playerClass);
    }

    private void updateClassDescriptionBox(PlayerClass playerClass) {
        title = "";
        description = "";
        switch (playerClass) {
            case FireFighter:
                title = "Fire Fighter";
                description = "The Fire Fighter utilizes a flamethrower to burn enemies. The attacks of the Fire Fighter apply Ignite which damages enemies over time. " +
                        "The Fire Fighter specializes in applying damage over time effects but has a very short attack range.";
                baseDamage = PlayerStats.fireFighterBaseDamage;
                baseAttackSpeed = PlayerStats.fireFighterAttackSpeed;
                break;
            case Captain:
                title = "Captain";
                description = "The Captain resembles traditional values. Shooting laserbeams from a long distance and using a rapidly recharging EMP to destroy any enemies or missiles near him. " +
                        "The Captain specializes in upgrading his laserbeams and Guardian Drones whilst staying away from enemies with a long attack range.";
                baseDamage = PlayerStats.captainBaseDamage;
                baseAttackSpeed = PlayerStats.captainAttackSpeed;
                break;
            default:
                break;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public float getBaseDamage () {
        return baseDamage;
    }

    public float getBaseAttackSpeed () {
        return baseAttackSpeed;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
