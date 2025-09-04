package net.riezebos.bruus.tbd.guiboards.util;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;

import java.util.HashMap;
import java.util.Map;

public class ClassDescription {

    private static final Map<PlayerClass, ClassDescription> cache = new HashMap<>();  // Static map to store instances

    private String title;
    private String description;
    private float baseDamage;
    private float baseAttackSpeed;
    private int maxHitpoints;
    private int bonusArmor;
    private String difficulty;
    private String unlockCondition;

    private ClassDescription(PlayerClass playerClass) {
        updateClassDescriptionBox(playerClass);
    }

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
                description = "The Fire Fighter utilizes a flamethrower to burn enemies nearby. The attacks of the Fire Fighter apply Ignite up to 3 times which damages enemies over time. " +
                        "The Fire Fighter specializes in applying damage over time effects but has a very short attack range.";
                baseDamage = PlayerStats.fireFighterBaseDamage;
                baseAttackSpeed = PlayerStats.fireFighterAttackSpeed;
                maxHitpoints = PlayerStats.fireFighterHitpoints;
                bonusArmor = 0;
                difficulty = "Hard";
                unlockCondition = "Defeat the first boss to unlock this class.";
                break;
            case Captain:
                title = "Captain";
                description = "The Captain shoots laserbeams and uses a rapidly recharging EMP to destroy any nearby missiles or enemies. " +
                        "";
                baseDamage = PlayerStats.captainBaseDamage;
                baseAttackSpeed = PlayerStats.captainAttackSpeed;
                maxHitpoints = PlayerStats.captainBaseHitpoints;
                bonusArmor = 0;
                difficulty = "Medium";
                break;
            case Carrier:
                title = "Carrier";
                description = "The Carrier cannot attack. The Carrier creates an armada of Protoss ships to fight around him. " +
                        "The Carrier can deploy a destructable beacon, Protoss ships will prioritize flying around the beacon. Switching gears allows the Carrier to rebuild destroyed Protoss ships or " +
                        "rapidly move away from danger.";
                baseDamage = PlayerStats.carrierBaseDamage;
                baseAttackSpeed = PlayerStats.carrierAttackSpeed;
                maxHitpoints = PlayerStats.carrierHitpoints;
                bonusArmor = Math.round(PlayerStats.carrierBaseArmor);
                difficulty = "Easy";
                unlockCondition = "Defeat the third boss to unlock this class.";
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

    public int getMaxHitpoints() {
        return maxHitpoints;
    }

    public int getBonusArmor() {
        return bonusArmor;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getUnlockCondition() {
        return unlockCondition;
    }
}
