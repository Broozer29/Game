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
            if (directorType == DirectorType.Instant && credits < 50) {
                this.active = false;
            }
        }
    }

    private void attemptSpawn() {
        if (credits > minimumMonsterCost() && !affordableMonsters.isEmpty()) {
            SpawnFormationEnums formationType = SpawnFormationEnums.getRandomFormation();
            MonsterCard selectedCard = selectMonsterCard();

            if (selectedCard != null) {
                EnemyEnums enemyType = selectedCard.getEnemyType();
                float totalFormationCost = calculateFormationCost(formationType, enemyType);

                if (credits >= totalFormationCost) {
                    spawnFormation(formationType, enemyType);
                    credits -= totalFormationCost;
                } else if (credits >= selectedCard.getCreditCost()) {
                    spawnEnemy(selectedCard.getEnemyType());
                    credits -= selectedCard.getCreditCost();
                }
            }
        }

        resetSpawnTimer();
    }

    private void updateAffordableMonsterList() {
        float totalDifficultyCoefficient = GameStateInfo.getInstance().getDifficultyCoefficient();
        affordableMonsters = adjustWeights(availableCards, totalDifficultyCoefficient);
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



    private boolean isMonsterTooCheap(MonsterCard card) {
        // Define a threshold below which a monster is considered too cheap
        float tooCheapThreshold = TOO_CHEAP_MULTIPLIER * card.getCreditCost();

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

    private double spawnWindowDuration = 3.0; // seconds, adjust as needed

    private boolean shouldAttemptSpawn(double currentTime) {
        if (directorType == DirectorType.Instant) {
            return true;
        } else {
            double spawnWindowStart = lastSpawnTime + spawnInterval;
            double spawnWindowEnd = spawnWindowStart + spawnWindowDuration;
            return currentTime >= spawnWindowStart && currentTime <= spawnWindowEnd;
        }
    }



    private void spawnEnemy(EnemyEnums enemyType) {
        // Set parameters for spawning
        int xCoordinate = 0; // Will be ignored as random is true
        int yCoordinate = 0; // Will be ignored as random is true
        Direction direction = Direction.LEFT;
        float scale = enemyType.getDefaultScale();
        int xMovementSpeed = enemyType.getMovementSpeed();
        int yMovementSpeed = enemyType.getMovementSpeed();
        int amountOfAttempts = 1;

        // Call LevelManager's spawnEnemy method
        LevelManager.getInstance().spawnEnemy(xCoordinate, yCoordinate, enemyType, amountOfAttempts, direction, scale, true, xMovementSpeed, yMovementSpeed, enemyType.isBoxCollision());
    }

    private void spawnFormation(SpawnFormationEnums formationType, EnemyEnums enemyType) {
        Direction direction = Direction.LEFT;
        float scale = enemyType.getDefaultScale();
        int xMovementSpeed = enemyType.getMovementSpeed();
        int yMovementSpeed = enemyType.getMovementSpeed();

        int formationWDistance = Math.round(enemyType.getFormationWidthDistance() * scale);
        int formationHDistance = Math.round(enemyType.getFormationHeightDistance() * scale);

        EnemyFormation formation = formationCreator.createFormation(formationType,
                formationWDistance,
                formationHDistance);

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


    public void resetSpawnTimer() {
        if (directorType != DirectorType.Instant) {
            lastSpawnTime = GameStateInfo.getInstance().getGameSeconds();  // Capture the current time when resetting
            spawnInterval = calculateInitialSpawnInterval(directorType);  // Recalculate the next interval
        }
    }

    public void receiveCredits (float amount) {
        this.credits += amount;
    }

    private MonsterCard selectMonsterCard() {
        if (!affordableMonsters.isEmpty()) {
//            for(MonsterCard card : affordableMonsters){
//                System.out.println(card.getEnemyType() + " with weight: " + card.getWeight());
//            }
//            System.out.println("");
            return weightedRandomSelection(affordableMonsters);
        }
        return null; // Return null if no affordable monsters are available
    }
    private List<MonsterCard> adjustWeights(List<MonsterCard> baseMonsterCards, float difficultyCoefficient) {
        // Adjust the weights based on difficulty coefficient
        return baseMonsterCards.stream().map(card -> {
            float adjustedWeight = calculateAdjustedWeight(card, difficultyCoefficient);
            return new MonsterCard(card.getEnemyType(), card.getCreditCost(), adjustedWeight);
        }).collect(Collectors.toList());
    }


    private float calculateAdjustedWeight(MonsterCard card, float difficultyCoefficient) {
        EnemyCategory category = card.getEnemyType().getEnemyCategory();
        float baseWeight = card.getWeight();
        // Adjust these values to finely control spawn behavior
        float basicIncreaseRate = 2f; // Increase basic enemy weight by a significant factor early on
        float decayRateForBasicEnemies = 0.2f; // Slower decay for Basic enemies (less aggressive than before)
        float growthRateForStrongEnemies = 0.1f; // Slower growth for stronger enemies

        switch (category) {
            case Basic:
                // Increase weight significantly for Basic enemies initially and reduce slowly
                return baseWeight * (basicIncreaseRate - difficultyCoefficient * decayRateForBasicEnemies);
            case Mercenary:
            case Boss:
                // Gradually increase weight for stronger enemies as difficulty increases
                return baseWeight * (1 + difficultyCoefficient * growthRateForStrongEnemies);
            default:
                return baseWeight;  // Default case for any uncategorized types
        }
    }




    private MonsterCard weightedRandomSelection(List<MonsterCard> adjustedCards) {
        double totalWeight = adjustedCards.stream().mapToDouble(MonsterCard::getWeight).sum();
        double randomValue = totalWeight * random.nextDouble();

        double cumulativeWeight = 0.0;
        for (MonsterCard card : adjustedCards) {
            cumulativeWeight += card.getWeight();
            if (cumulativeWeight >= randomValue) {
                return card;
            }
        }

        return null; // Implement a fallback in case no card is selected
    }

    public boolean isActive () {
        return this.active;
    }
}
