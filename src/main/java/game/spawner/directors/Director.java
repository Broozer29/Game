package game.spawner.directors;

import VisualAndAudioData.DataClass;
import game.gamestate.GameStateInfo;
import game.spawner.LevelManager;
import game.movement.Direction;
import game.objects.enemies.enums.EnemyCategory;
import game.objects.enemies.enums.EnemyEnums;
import game.spawner.EnemyFormation;
import game.spawner.FormationCreator;
import game.spawner.enums.SpawnFormationEnums;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Director {
    private float credits;
    private double lastSpawnTime;
    private long spawnInterval; // Interval for Slow and Fast directors
    private List<MonsterCard> availableCards;
    private List<MonsterCard> affordableMonsters;

    private FormationCreator formationCreator;

    private static final float TOO_CHEAP_MULTIPLIER = 20.0f; //increase over time to remove cheaper enemies

    private DirectorType directorType;

    private Random random = new Random();

    private boolean active;

    public Director (DirectorType directorType, List<MonsterCard> availableCards) {
        this.directorType = directorType;
        this.credits = 0;
        this.lastSpawnTime = 0;
        this.spawnInterval = calculateInitialSpawnInterval(directorType);
        this.availableCards = new ArrayList<>(availableCards);
        this.active = true;
        this.formationCreator = new FormationCreator();
    }

    private long calculateInitialSpawnInterval (DirectorType directorType) {
        switch (directorType) {
            case Slow:
                return 15 + (long) (Math.random() * 10); // 15-25 seconds
            case Fast:
                return 5 + (long) (Math.random() * 8); // 5-13 seconds
            default:
                return 0; // Instant directors don't use intervals
        }
    }

    public void update (double secondsPassed) {
        if (shouldAttemptSpawn(secondsPassed)) {
            updateAffordableMonsterList();
            attemptSpawn();
            lastSpawnTime = secondsPassed;
            if (directorType == DirectorType.Instant) {
                this.active = false;
            }
        }
    }

    private void attemptSpawn() {
        int loopCount = 0;

        while (credits > minimumMonsterCost() && !affordableMonsters.isEmpty()) {
            // Randomly select a formation type
            loopCount++;
            SpawnFormationEnums formationType = SpawnFormationEnums.getRandomFormation();
            MonsterCard selectedCard = selectMonsterCard(affordableMonsters);

            if (selectedCard != null) {
                EnemyEnums enemyType = selectedCard.getEnemyType();

                // Calculate the total credit cost of the formation
                float totalFormationCost = calculateFormationCost(formationType, enemyType);

                if (credits >= totalFormationCost) {
                    // If enough credits, spawn formation
                    spawnFormation(formationType, enemyType);
                    credits -= totalFormationCost;
                } else if (credits >= selectedCard.getCreditCost()) {
                    // Otherwise, attempt to spawn a singular enemy
                    spawnEnemy(selectedCard.getEnemyType());
                    credits -= selectedCard.getCreditCost();
                }
            } else {
                break;
            }

            if(loopCount > 30){
                break;
            }
        }

        if (credits > minimumMonsterCost()) {
            spawnMostExpensiveAffordableMonster();
        }

        resetSpawnTimer();
    }

    private void spawnMostExpensiveAffordableMonster() {
        MonsterCard expensiveCard = selectMostExpensiveAffordableMonsterCard(availableCards);
        if (expensiveCard != null && credits >= expensiveCard.getCreditCost()) {
            spawnEnemy(expensiveCard.getEnemyType());
            credits -= expensiveCard.getCreditCost();
        }
    }

    private float minimumMonsterCost() {
        return availableCards.stream()
                .map(MonsterCard::getCreditCost)
                .min(Float::compare)
                .orElse(Float.MAX_VALUE);
    }

    private float calculateFormationCost(SpawnFormationEnums formationType, EnemyEnums enemyType) {
        EnemyFormation formation = formationCreator.createFormation(formationType, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
        float enemyCount = countEnemiesInFormation(formation.getFormationPattern());
        return enemyCount * enemyType.getCreditCost();
    }

    private int countEnemiesInFormation(boolean[][] formationPattern) {
        int count = 0;
        for (boolean[] row : formationPattern) {
            for (boolean hasEnemy : row) {
                if (hasEnemy) {
                    count++;
                }
            }
        }
        return count;
    }

    private void updateAffordableMonsterList() {
        // Update the list of monsters that are not considered too cheap
        affordableMonsters = availableCards.stream()
                .filter(card -> !isMonsterTooCheap(card))
                .collect(Collectors.toList());

        if (affordableMonsters.isEmpty()) {
            // If all monsters are deemed too cheap, add the three most expensive ones
            List<MonsterCard> topThreeExpensiveCards = availableCards.stream()
                    .sorted(Comparator.comparing(MonsterCard::getCreditCost).reversed())
                    .limit(3) // Limit to the top three
                    .toList();

            affordableMonsters.addAll(topThreeExpensiveCards);
        }
//        for(MonsterCard monsterCard : affordableMonsters){
//            System.out.println(monsterCard.getEnemyType());
//        }
    }

    private boolean isMonsterTooCheap(MonsterCard card) {
        // Define a threshold below which a monster is considered too cheap
        float tooCheapThreshold = TOO_CHEAP_MULTIPLIER * card.getCreditCost();

//        System.out.println("cheap threshold: " + tooCheapThreshold);
//        System.out.println("20% of current credits: " + credits / 5);
        return (credits / 5) > tooCheapThreshold;
    }

    private float getMaxMonsterCost() {
        return availableCards.stream()
                .map(MonsterCard::getCreditCost)
                .max(Float::compare)
                .orElse(0.0f);
    }

    private MonsterCard selectMostExpensiveAffordableMonsterCard(List<MonsterCard> availableCards) {
        // Sort the cards in descending order of credit cost and return the first one that can be afforded
        return availableCards.stream()
                .filter(card -> card.getCreditCost() <= credits)
                .max(Comparator.comparing(MonsterCard::getCreditCost))
                .orElse(null);
    }

    private boolean shouldAttemptSpawn(double timeInSeconds) {
        if (directorType == DirectorType.Instant) {
            return true;
        } else {
            return (lastSpawnTime + spawnInterval) <= timeInSeconds;
        }
    }



    private void spawnEnemy(EnemyEnums enemyType) {
        // Set parameters for spawning
        int xCoordinate = 0; // Will be ignored as random is true
        int yCoordinate = 0; // Will be ignored as random is true
        Direction direction = Direction.LEFT;
        float scale = 1;
        int xMovementSpeed = 2;
        int yMovementSpeed = 2;
        int amountOfAttempts = 1;

        // Call LevelManager's spawnEnemy method
        LevelManager.getInstance().spawnEnemy(xCoordinate, yCoordinate, enemyType, amountOfAttempts, direction, scale, true, xMovementSpeed, yMovementSpeed, enemyType.isBoxCollision());
    }

    private void spawnFormation(SpawnFormationEnums formationType, EnemyEnums enemyType) {
        Direction direction = Direction.LEFT;
        float scale = 1.0f;
        int xMovementSpeed = 2;
        int yMovementSpeed = 2;

        EnemyFormation formation = formationCreator.createFormation(formationType,
                enemyType.getFormationWidthDistance(),
                enemyType.getFormationHeightDistance());

        int totalFormationWidth = (formation.getFormationWidth() * formation.getWidthDistance());
        int totalFormationHeight = (formation.getFormationHeight() * formation.getHeightDistance());

        int baseX = calculateBaseX(totalFormationWidth, Direction.LEFT); // Assuming direction is LEFT for now
        int baseY = calculateBaseY(totalFormationHeight);

        formation.spawnFormation(baseX, baseY, enemyType, direction, scale, xMovementSpeed, yMovementSpeed);
    }

    private int calculateBaseX(int totalFormationWidth, Direction direction) {
        if (direction == Direction.LEFT) {
            // For LEFT direction, spawn at or beyond the right edge of the board
            return DataClass.getInstance().getWindowWidth() + (random.nextInt(totalFormationWidth) * 3);
        } else if (direction == Direction.RIGHT) {
            // For RIGHT direction, spawn at or before the left edge of the board
            return -(random.nextInt(totalFormationWidth) * 3);
        }
        return 0;
    }

    private int calculateBaseY(int totalFormationHeight) {
        return random.nextInt(DataClass.getInstance().getPlayableWindowMinHeight(),DataClass.getInstance().getPlayableWindowMaxHeight() - totalFormationHeight + 1); // +1 to include the upper limit
    }


    public void resetSpawnTimer () {
        if (directorType != DirectorType.Instant) {
            spawnInterval = calculateInitialSpawnInterval(directorType);
        }
    }

    public void receiveCredits (float amount) {
        this.credits += amount;
    }

    private MonsterCard selectMonsterCard (List<MonsterCard> baseMonsterCards) {
        List<MonsterCard> adjustedCards = adjustWeights(baseMonsterCards);
        return weightedRandomSelection(adjustedCards);
    }

    private List<MonsterCard> adjustWeights (List<MonsterCard> baseMonsterCards) {
        float totalDifficultyCoefficient = GameStateInfo.getInstance().getDifficultyCoefficient();

        // Adjust the weights based on difficulty coefficient
        return baseMonsterCards.stream().map(card -> {
            float adjustedWeight = calculateAdjustedWeight(card, totalDifficultyCoefficient);
            return new MonsterCard(card.getEnemyType(), card.getCreditCost(), adjustedWeight);
        }).collect(Collectors.toList());
    }

    private float calculateAdjustedWeight (MonsterCard card, float difficultyCoefficient) {
        EnemyCategory category = card.getEnemyType().getEnemyCategory();
        float baseWeight = card.getWeight();

        // Constants to control the rate of change
        float basicDecayRate = 0.9f; // Controls how quickly the weight of basic enemies decreases
        float minibossGrowthRate = 0.2f; // Controls the growth rate for minibosses
        float bossGrowthStart = 1.5f; // Point at which bosses start getting a non-zero weight
        float bossGrowthRate = 0.2f;

        switch (category) {
            case Basic:
                // Decrease weight for Basic enemies as difficulty increases
                return (float) Math.max(baseWeight * (1 - difficultyCoefficient * basicDecayRate), 0.05);
            case MiniBoss:
                // Increase weight for MiniBoss enemies up to a point, then decrease
                return (float) (baseWeight * (1 + Math.sin(difficultyCoefficient * minibossGrowthRate)));
            case Boss:
                // Bosses start getting weight past a certain difficulty point
                if (difficultyCoefficient > bossGrowthStart) {
                    return (float) (baseWeight * (1 + Math.sin(difficultyCoefficient * bossGrowthRate)));
                } else {
                    return 0;
                }
            default:
                return baseWeight;
        }
    }

    private MonsterCard weightedRandomSelection (List<MonsterCard> adjustedCards) {
        double totalWeight = adjustedCards.stream().mapToDouble(MonsterCard::getWeight).sum();
        double randomValue = totalWeight * random.nextDouble();

        double cumulativeWeight = 0.0;
        for (MonsterCard card : adjustedCards) {
            cumulativeWeight += card.getWeight();
            if (cumulativeWeight >= randomValue) {
                return card;
            }
        }

        return null; // Fallback in case no card is selected
    }

    public boolean isActive () {
        return this.active;
    }


}
