package net.riezebos.bruus.tbd.game.level.directors;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;

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
    private PerformanceLogger performanceLogger = null;


    private DirectorManager () {
        createMonsterCards();
        this.performanceLogger = new PerformanceLogger("Director Manager");
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
        instantDirector.receiveCredits(Math.min(150 * GameState.getInstance().getDifficultyCoefficient(), 650));
        directorList.add(instantDirector);
        lastCashCarrierSpawnTime = 0;
    }

    public Director getTestDirector () {
        return new Director(DirectorType.Fast, baseMonsterCards);
    }

    public void resetManager () {
        directorList = new ArrayList<>();
        performanceLogger.reset();
    }

    public void createMonsterCards () {
        if (!baseMonsterCards.isEmpty()) {
            baseMonsterCards = new ArrayList<>();
        }

        EnemyTribes enemyTribes = LevelManager.getInstance().getCurrentEnemyTribe();

        List<EnemyEnums> availableMonsters = Arrays.stream(EnemyEnums.values())
                .filter(enemyEnums -> GameState.getInstance().getStagesCompleted() >= enemyEnums.getMinimumStageLevelRequired())
                .filter(enemyEnums -> enemyEnums.getEnemyCategory() != EnemyCategory.Special
                        && enemyEnums.getEnemyCategory() != EnemyCategory.Summon
                        && enemyEnums.getEnemyCategory() != EnemyCategory.Boss
                        && enemyEnums.getEnemyTribe().equals(enemyTribes))
                .toList();

        for (EnemyEnums enemy : availableMonsters) {
            MonsterCard card = new MonsterCard(enemy, enemy.getCreditCost(), enemy.getWeight());
            baseMonsterCards.add(card);
        }
    }


    public void updateGameTick () {
//        PerformanceLoggerManager.timeAndLog(performanceLogger, "Total", () -> {
            if (enabled) {
                PerformanceLoggerManager.timeAndLog(performanceLogger, "Distribute Credits", this::distributeCredits);
                PerformanceLoggerManager.timeAndLog(performanceLogger, "Update Difficulty Coefficient", this::updateDifficultyCoefficient);
                for (Director director : directorList) {
                    if (director.isActive()) {
                        PerformanceLoggerManager.timeAndLog(performanceLogger, "Update Director", director::update);
                    }
                }
            }
//        });
    }

    public void distributeCredits () {
        GameState gameStateInfo = GameState.getInstance();
        float creditAmount = (float) ((1 + 0.025 * gameStateInfo.getDifficultyCoefficient())) + (LevelManager.getInstance().getCurrentLevelDifficultyScore() * 0.35f); // Determine the amount of credits to distribute

        if (testingRichMode) {
            creditAmount = creditAmount * this.testingCreditsBonus;
        }

        for (Director director : directorList) {
            director.receiveCredits(creditAmount);
        }
    }

    private void updateDifficultyCoefficient () {
        GameState.getInstance().updateDifficultyCoefficient();
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

    public PerformanceLogger getPerformanceLogger () {
        return this.performanceLogger;
    }
}
