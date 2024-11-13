package net.riezebos.bruus.tbd.game.level.directors;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirectorManager {

    private static DirectorManager instance = new DirectorManager();
    private List<Director> directorList = new ArrayList<>();
    private List<MonsterCard> baseMonsterCards = new ArrayList<>();
    private boolean enabled;
    public boolean testingRichMode; //used for testing only

    private double lastCashCarrierSpawnTime;
    private int testingCreditsBonus = 3;


    private DirectorManager () {
        createMonsterCards();
    }


    public void createDirectors (LevelTypes levelType) {
        switch (levelType) {
            case Regular -> {
                createDirectorsForRegularLevel();
            }
            case Boss -> {
                createBossDirectors();
            }
            case Special -> {
                //to do
            }
        }
    }

    private void createBossDirectors () {
        directorList = new ArrayList<>();
        Director bossDirector = new Director(DirectorType.Boss, baseMonsterCards);
        directorList.add(bossDirector);
    }

    private void createDirectorsForRegularLevel () {
        directorList = new ArrayList<>();

//        Create fast & slow director(s)
        Director slowDirector = new Director(DirectorType.Slow, baseMonsterCards);
        directorList.add(slowDirector);

        Director fastDirector = new Director(DirectorType.Fast, baseMonsterCards);
        directorList.add(fastDirector);

        Director instantDirector = new Director(DirectorType.Instant, baseMonsterCards);
        instantDirector.receiveCredits(Math.min(75 * GameStateInfo.getInstance().getDifficultyCoefficient(), 650));
        directorList.add(instantDirector);
        lastCashCarrierSpawnTime = 0;
    }

    public Director getTestDirector () {
        return new Director(DirectorType.Fast, baseMonsterCards);
    }

    public void resetManager () {
        directorList = new ArrayList<>();
    }

    public void createMonsterCards () {
        if (!baseMonsterCards.isEmpty()) {
            baseMonsterCards = new ArrayList<>();
        }

        List<EnemyEnums> availableMonsters = Arrays.stream(EnemyEnums.values())
                .filter(enemyEnums -> GameStateInfo.getInstance().getStagesCompleted() >= enemyEnums.getMinimumStageLevelRequired())
                .filter(enemyEnums -> enemyEnums.getEnemyCategory() != EnemyCategory.Special
                        && enemyEnums.getEnemyCategory() != EnemyCategory.Summon
                        && enemyEnums.getEnemyCategory() != EnemyCategory.Boss)
                .toList();

        for (EnemyEnums enemy : availableMonsters) {
            float creditCost = determineCreditCostBasedOnEnemy(enemy);
            MonsterCard card = new MonsterCard(enemy, creditCost, enemy.getWeight());
            baseMonsterCards.add(card);
        }
    }

    private float determineCreditCostBasedOnEnemy (EnemyEnums enemy) {
        // Implement logic to determine the credit cost of each enemy
        //Needs to be multiplied by the stats of the enemy to some degree as the stats grow
        return enemy.getCreditCost();
    }

    public void updateGameTick () {
        if (enabled) {
            distributeCredits();
            updateDifficultyCoefficient();
            for (Director director : directorList) {
                if (director.isActive()) {
                    director.update(GameStateInfo.getInstance().getGameSeconds()); // Update each director per game tick
                }
            }
        }
    }

    public void distributeCredits () {
        GameStateInfo gameStateInfo = GameStateInfo.getInstance();
        float creditAmount = (float) ((1 + 0.05 * gameStateInfo.getDifficultyCoefficient()) * 0.5); // Determine the amount of credits to distribute

        if (testingRichMode) {
            creditAmount = creditAmount * this.testingCreditsBonus;
        }

        for (Director director : directorList) {
            director.receiveCredits(creditAmount);
        }
    }

    private void updateDifficultyCoefficient () {
        GameStateInfo.getInstance().updateDifficultyCoefficient();
    }

    public static DirectorManager getInstance () {
        return instance;
    }


    public List<Director> getDirectorList () {
        return directorList;
    }

    public List<MonsterCard> getBaseMonsterCards () {
        return baseMonsterCards;
    }

    public boolean isEnabled () {
        return enabled;
    }

    public void setEnabled (boolean enabled) {
        this.enabled = enabled;
        if(!this.enabled){ //Disable all active directors if the directorManager is disabled
            for(Director director : directorList){
                director.setIsActive(false);
            }
        }
    }

    public double getLastCashCarrierSpawnTime () {
        return lastCashCarrierSpawnTime;
    }

    public void setLastCashCarrierSpawnTime (double lastCashCarrierSpawnTime) {
        this.lastCashCarrierSpawnTime = lastCashCarrierSpawnTime;
    }
}
