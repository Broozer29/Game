package game.spawner.directors;

import game.gamestate.GameStateInfo;
import game.objects.enemies.enums.EnemyEnums;

import java.util.ArrayList;
import java.util.List;

public class DirectorManager {

    private static DirectorManager instance = new DirectorManager();
    private List<Director> directorList = new ArrayList<>();
    private List<MonsterCard> baseMonsterCards = new ArrayList<>();
    private boolean enabled;



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

        Director instantDirector = new Director(DirectorType.Instant, baseMonsterCards);
        instantDirector.receiveCredits(200);
        directorList.add(instantDirector);

    }

    public void resetManager(){
        directorList = new ArrayList<>();
    }

    public void createMonsterCards () {
        if(!baseMonsterCards.isEmpty()){
            baseMonsterCards = new ArrayList<>();
        }

        for (EnemyEnums enemy : EnemyEnums.values()) {
            if(enemy != EnemyEnums.Alien_Bomb) {
                float creditCost = determineCreditCostBasedOnEnemy(enemy);
                MonsterCard card = new MonsterCard(enemy, creditCost, enemy.getWeight());
                baseMonsterCards.add(card);
            }
        }
    }

    private float determineCreditCostBasedOnEnemy(EnemyEnums enemy) {
        // Implement logic to determine the credit cost of each enemy
        //Needs to be multiplied by the stats of the enemy to some degree as the stats grow

        return enemy.getCreditCost();
    }

    public void updateGameTick() {
        if(enabled) {
            distributeCredits();
            updateDifficultyCoefficient();
            for (Director director : directorList) {
                director.update(GameStateInfo.getInstance().getGameSeconds()); // Update each director per game tick
            }
        }
    }

    public void distributeCredits() {
        float creditAmount = (float) ((1 + 0.2 * GameStateInfo.getInstance().getDifficultyCoefficient()) * 0.5); // Determine the amount of credits to distribute
        for (Director director : directorList) {
            director.receiveCredits(creditAmount);
        }
    }

    private void updateDifficultyCoefficient() {
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
    }
}
