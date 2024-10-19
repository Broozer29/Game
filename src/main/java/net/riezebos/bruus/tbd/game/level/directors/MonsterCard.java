package net.riezebos.bruus.tbd.game.level.directors;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;

public class MonsterCard {
    private EnemyEnums enemyType;
    private float creditCost;
    private float weight;
    // Other relevant attributes like stage conditions

    public MonsterCard(EnemyEnums enemyType, float creditCost, float weight) {
        this.enemyType = enemyType;
        this.creditCost = creditCost;
        this.weight = weight;
    }

    public EnemyEnums getEnemyType () {
        return enemyType;
    }

    public void setEnemyType (EnemyEnums enemyType) {
        this.enemyType = enemyType;
    }

    public float getCreditCost () {
        return creditCost;
    }

    public void setCreditCost (int creditCost) {
        this.creditCost = creditCost;
    }

    public float getWeight () {
        return weight;
    }

    public void setWeight (int weight) {
        this.weight = weight;
    }

}