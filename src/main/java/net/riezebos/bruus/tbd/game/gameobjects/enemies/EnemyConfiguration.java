package net.riezebos.bruus.tbd.game.gameobjects.enemies;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class EnemyConfiguration {
    private EnemyEnums enemyType;
    private float baseDamage;

    private int maxHitPoints;
    private AudioEnums deathSound;

    private float baseArmor;
    private float xpOnDeath;

    private float cashMoneyWorth;

    public EnemyConfiguration (EnemyEnums enemyType, int maxHitPoints, float baseDamage, AudioEnums deathSound,float baseArmor, float xpOnDeath, float cashMoneyWorth) {
        this.enemyType = enemyType;
        this.baseDamage = baseDamage;
        this.maxHitPoints = maxHitPoints;
        this.deathSound = deathSound;
        this.baseArmor = baseArmor;
        this.xpOnDeath = xpOnDeath;
        this.cashMoneyWorth = cashMoneyWorth;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public EnemyEnums getEnemyType () {
        return enemyType;
    }

    public void setEnemyType (EnemyEnums enemyType) {
        this.enemyType = enemyType;
    }

    public int getMaxHitPoints () {
        return maxHitPoints;
    }

    public void setMaxHitPoints (int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public AudioEnums getDeathSound () {
        return deathSound;
    }

    public void setDeathSound (AudioEnums deathSound) {
        this.deathSound = deathSound;
    }

    public float getBaseArmor () {
        return baseArmor;
    }

    public void setBaseArmor (float baseArmor) {
        this.baseArmor = baseArmor;
    }

    public float getXpOnDeath () {
        return xpOnDeath;
    }

    public void setXpOnDeath (float xpOnDeath) {
        this.xpOnDeath = xpOnDeath;
    }

    public float getCashMoneyWorth () {
        return cashMoneyWorth;
    }

    public void setCashMoneyWorth (float cashMoneyWorth) {
        this.cashMoneyWorth = cashMoneyWorth;
    }
}
