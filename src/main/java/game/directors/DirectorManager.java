package game.directors;

import game.gamestate.GameStateInfo;
import game.objects.enemies.enums.EnemyEnums;

import java.util.ArrayList;
import java.util.List;

public class DirectorManager {

    //What it can do as of now:
    //Calculate difficulty coeff based on gameticks passed
    //Create a population of monstercards
    //Adjust weight of monstercards by difficulty (has to be expanded, see Director itself)
    //select monstercard based on their respective weight.

    private static DirectorManager instance = new DirectorManager();
    private List<Director> directorList = new ArrayList<>();
    private List<MonsterCard> baseMonsterCards = new ArrayList<>();

    private float difficultyCoefficient;

    private DirectorManager () {
        createMonsterCards();
    }

    public void createDirectors(){
        directorList = new ArrayList<>();

//        Create fast & slow director(s)
        Director slowDirector = new Director(DirectorType.Slow, baseMonsterCards);
        directorList.add(slowDirector);

        Director fastDirector = new Director(DirectorType.Fast, baseMonsterCards);
        directorList.add(fastDirector);
    }


    public void createMonsterCards () {
        if(!baseMonsterCards.isEmpty()){
            baseMonsterCards = new ArrayList<>();
        }

        for (EnemyEnums enemy : EnemyEnums.values()) {
            float creditCost = determineCreditCostBasedOnEnemy(enemy);
            MonsterCard card = new MonsterCard(enemy, creditCost, 1.0F);
            baseMonsterCards.add(card);
        }
    }

    private float determineCreditCostBasedOnEnemy(EnemyEnums enemy) {
        // Implement logic to determine the credit cost of each enemy
        //Needs to be multiplied by the stats of the enemy to some degree as the stats grow

        return enemy.getCreditCost();
    }

    public void updateGameTick() {
        distributeCredits();
        updateDifficultyCoefficient();
        for (Director director : directorList) {
            director.update(GameStateInfo.getInstance().getGameSeconds()); // Update each director per game tick
        }
    }

    public void distributeCredits() {
        float creditAmount = (float) ((1 + 0.2 * difficultyCoefficient) * 0.5); // Determine the amount of credits to distribute
        for (Director director : directorList) {
            director.receiveCredits(creditAmount);
        }
    }

    private void updateDifficultyCoefficient() {
        long secondsPassed = GameStateInfo.getInstance().getGameSeconds();
        int stagesCompleted = GameStateInfo.getInstance().getStagesCompleted();

        float playerFactor = 1;
        float timeFactor = 0.0506f; // The factor for time, increase value to increase difficulty scaling
        float difficultyValue = 1; // This can be a static value or change based on game settings
        float stageFactor = (float) Math.pow(1.15, stagesCompleted); // Exponential growth for each stage completed

        float timeInMinutes = secondsPassed / 60.0f; // Convert seconds to minutes

        difficultyCoefficient = (playerFactor + timeInMinutes * timeFactor) * stageFactor;
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

    public float getDifficultyCoefficient () {
        return difficultyCoefficient;
    }
}
