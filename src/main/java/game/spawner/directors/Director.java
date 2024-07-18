package game.spawner.directors;

import VisualAndAudioData.DataClass;
import game.gamestate.GameStateInfo;
import game.movement.deprecatedpathfinderconfigs.MovementPatternSize;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyCreator;
import game.spawner.LevelManager;
import game.movement.Direction;
import game.gameobjects.enemies.enums.EnemyCategory;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.spawner.EnemyFormation;
import game.spawner.FormationCreator;
import game.spawner.enums.SpawnFormationEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Director {
    private float credits;
    private double lastSpawnTime;
    private long spawnInterval; // Interval for Slow and Fast directors
    private double lastCashCarrierSpawnTime;
    private double spawnCashCarrierChance;
    private double lastFormationSpawnTime;
    private List<MonsterCard> availableCards;
    private List<MonsterCard> affordableMonsters;

    private FormationCreator formationCreator;

    private double spawnWindowDuration = 3.0; // seconds, adjust as needed

    private DirectorType directorType;

    private Random random = new Random();
    private double currentTime;
    private boolean active;

    public Director (DirectorType directorType, List<MonsterCard> availableCards) {
        this.directorType = directorType;
        this.credits = 0;
        this.lastSpawnTime = 0;
        this.currentTime = 0;
        this.spawnInterval = calculateInitialSpawnInterval(directorType);
        this.spawnCashCarrierChance = calculateCashCarrierChance(directorType);
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

    private double calculateCashCarrierChance (DirectorType directorType) {
        switch (directorType) {
            case Slow:
                return 0.1f;
//                return 1f;
            case Fast:
//                return 1f;
                return 0.05f;
            default:
                return 0f; // instant directors should not spawn cash carriers
        }
    }

    public void update (double secondsPassed) {
        currentTime = secondsPassed;
        if (shouldAttemptSpawn(secondsPassed)) {
            updateAffordableMonsterList();
            attemptSpawn();
            lastSpawnTime = secondsPassed;


            if (directorType == DirectorType.Instant && credits < 50) {
                this.active = false;
            }

        }
    }

    private void attemptSpawn () {
        double randomNumber = random.nextDouble();  // Generate a random number between 0 and 1
        double timeSinceLastCashCarrier = currentTime - lastCashCarrierSpawnTime;

        // Check for cash carrier spawn conditions
        if (randomNumber <= spawnCashCarrierChance && timeSinceLastCashCarrier >= 20) {
            SpawnFormationEnums formationType = SpawnFormationEnums.getRandomFormation();
            spawnCashCarrier(formationType);  // Always a scout as of now
        }


        if (credits > minimumMonsterCost() && !affordableMonsters.isEmpty()) {
            SpawnFormationEnums formationType = SpawnFormationEnums.getRandomFormation();
            MonsterCard selectedCard = selectMonsterCard();

            if (selectedCard != null) {
                EnemyEnums enemyType = selectedCard.getEnemyType();
                float totalFormationCost = calculateFormationCost(formationType, enemyType);

                if (credits >= totalFormationCost && shouldSpawnFormation(enemyType)) {
                    spawnRegularFormation(formationType, enemyType);
                    credits -= totalFormationCost;
                } else if (credits >= selectedCard.getCreditCost()) {
                    spawnEnemy(enemyType);
                    credits -= selectedCard.getCreditCost();
                }
            }
        }

        resetSpawnTimer();
    }

    private boolean shouldSpawnFormation (EnemyEnums enemyType) {
        double time = currentTime - lastFormationSpawnTime;
        if (time < 3) {
            return false;
        }

        double randomDouble = random.nextDouble();
        double chanceThreshold = switch (enemyType) {
            case CashCarrier, Alien_Bomb -> -1f;
            case Needler, Scout -> 0.2f; //20% chance of spawning a formation
            case Bomba, Tazer, Flamer, Seeker, Bulldozer, Energizer -> 0.1f; //10% chance of spawning formation
        };


        return randomDouble < chanceThreshold;
    }


    private void updateAffordableMonsterList () {
        float totalDifficultyCoefficient = GameStateInfo.getInstance().getDifficultyCoefficient();
        affordableMonsters = adjustWeights(availableCards, totalDifficultyCoefficient);
    }

    private float minimumMonsterCost () {
        return availableCards.stream()
                .map(MonsterCard::getCreditCost)
                .min(Float::compare)
                .orElse(Float.MAX_VALUE);
    }

    private float calculateFormationCost (SpawnFormationEnums formationType, EnemyEnums enemyType) {
        float enemyCount = formationType.getEnemyCountInFormation();
        return enemyCount * (enemyType.getCreditCost() * 1.5f); //50% increased price for a formation!
    }

    private boolean shouldAttemptSpawn (double currentTime) {
        if (directorType == DirectorType.Instant) {
            return true;
        } else {
            double spawnWindowStart = lastSpawnTime + spawnInterval;
            double spawnWindowEnd = spawnWindowStart + spawnWindowDuration;
            return currentTime >= spawnWindowStart && currentTime <= spawnWindowEnd;
        }
    }


    private void spawnEnemy (EnemyEnums enemyType) {
        // Set parameters for spawning
        Direction direction = Direction.LEFT;
        float scale = enemyType.getDefaultScale();
        int xMovementSpeed = enemyType.getMovementSpeed();
        int yMovementSpeed = enemyType.getMovementSpeed();

        // Call LevelManager's spawnEnemy method
        LevelManager.getInstance().spawnEnemy(0, 0, enemyType, direction, scale, true, xMovementSpeed, yMovementSpeed, enemyType.isBoxCollision());
    }

    public void spawnRegularFormation (SpawnFormationEnums formationType, EnemyEnums enemyType) {
        spawnFormationWithParameters(formationType, enemyType, null, false);
    }

    private void spawnCashCarrier (SpawnFormationEnums formationType) {
        spawnEntourageFormation(formationType, EnemyEnums.Scout, EnemyEnums.CashCarrier);
        this.lastCashCarrierSpawnTime = currentTime;
    }

    private void spawnEntourageFormation (SpawnFormationEnums formationType, EnemyEnums primaryEnemy, EnemyEnums secondaryEnemy) {
        spawnFormationWithParameters(formationType, primaryEnemy, secondaryEnemy, true);
    }


    private void spawnFormationWithParameters (SpawnFormationEnums formationType, EnemyEnums primaryEnemyType, EnemyEnums secondaryEnemyType, boolean isEntourage) {
        Direction direction = Direction.LEFT;
        int xMovementSpeed = isEntourage ? secondaryEnemyType.getMovementSpeed() : primaryEnemyType.getMovementSpeed();
        int yMovementSpeed = xMovementSpeed;


        Enemy enemy = null;
        if (isEntourage) {
            enemy = EnemyCreator.createEnemy(secondaryEnemyType, 0, 0, direction, secondaryEnemyType.getDefaultScale(), 0, 0, MovementPatternSize.SMALL, false);
        } else {
            enemy = EnemyCreator.createEnemy(primaryEnemyType, 0, 0, direction, primaryEnemyType.getDefaultScale(), 0, 0, MovementPatternSize.SMALL, false);
        }

        int formationWDistance = Math.round(enemy.getWidth() * 1.1f);
        int formationHDistance = Math.round(enemy.getHeight() * 1.1f);

        EnemyFormation formation = formationCreator.createFormation(formationType, formationWDistance, formationHDistance);
        int totalFormationWidth = formation.getFormationWidth() * formation.getWidthDistance();
        int totalFormationHeight = formation.getFormationHeight() * formation.getHeightDistance();

        int baseX = calculateBaseX(totalFormationWidth, direction);
        int baseY = calculateBaseY(totalFormationHeight, direction);

        if (isEntourage) {
            formation.spawnFormation(baseX, baseY, primaryEnemyType, secondaryEnemyType, direction, xMovementSpeed, yMovementSpeed);
        } else {
            formation.spawnFormation(baseX, baseY, primaryEnemyType, primaryEnemyType, direction, xMovementSpeed, yMovementSpeed);
        }
        lastFormationSpawnTime = currentTime;
    }


    private int calculateBaseX(int totalFormationWidth, Direction direction) {
        DataClass instance = DataClass.getInstance();
        if (direction == Direction.LEFT) {
            // For LEFT direction, spawn at or beyond the right edge of the board
            int bound = instance.getWindowWidth() + (random.nextInt(totalFormationWidth) * 3);
            return instance.getWindowWidth() + Math.max(0, bound);
        } else if (direction == Direction.RIGHT) {
            // For RIGHT direction, spawn at or before the left edge of the board
            int bound = -(random.nextInt(totalFormationWidth) * 3);
            return Math.min(0, bound);
        } else if (direction == Direction.DOWN || direction == Direction.UP) {
            int min = 0;
            int max = instance.getWindowWidth() - totalFormationWidth;
            if (min > max) {
                return min; // Fallback to min if bounds are invalid
            }
            return random.nextInt(min, max + 1);
        } else if (direction == Direction.LEFT_UP || direction == Direction.LEFT_DOWN) {
            int randomXIncrease = random.nextInt(instance.getWindowWidth() / 2);
            int min = instance.getWindowWidth() / 2 + randomXIncrease;
            int max = Math.round(instance.getWindowWidth() + randomXIncrease) - totalFormationWidth;
            if (min > max) {
                return min; // Fallback to min if bounds are invalid
            }
            return random.nextInt(min, max + 1);
        } else if (direction == Direction.RIGHT_UP || direction == Direction.RIGHT_DOWN) {
            // Calculate 25% of the window width
            int quarterWindowWidth = instance.getWindowWidth() / 4;
            // Define the maximum possible value for the random range
            int maxRange = quarterWindowWidth;
            // Get a random number from -maxRange to maxRange
            int randomX = random.nextInt(maxRange + 1); // This will give a value from 0 to maxRange

            // Decide randomly to add or subtract this value from zero
            if (random.nextBoolean()) { // This randomly decides if the value is positive or negative
                randomX = -randomX;
            }

            return randomX;
        }
        return 0;
    }



    private int calculateBaseY(int totalFormationHeight, Direction direction) {
        DataClass instance = DataClass.getInstance();
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            if (totalFormationHeight > instance.getPlayableWindowMaxHeight()) {
                totalFormationHeight = instance.getPlayableWindowMaxHeight();
            }
            int min = instance.getPlayableWindowMinHeight();
            int max = instance.getPlayableWindowMaxHeight() - totalFormationHeight + 1;
            if (min > max) {
                return min; // Fallback to min if bounds are invalid
            }
            return random.nextInt(min, max);
        } else if (direction == Direction.DOWN || direction == Direction.LEFT_DOWN || direction == Direction.RIGHT_DOWN) {
            int min = instance.getPlayableWindowMinHeight() - Math.round(totalFormationHeight * 1.5f);
            int max = instance.getPlayableWindowMinHeight() - 10;
            if (min > max) {
                return min; // Fallback to min if bounds are invalid
            }
            return random.nextInt(min, max + 1);
        } else if (direction == Direction.UP || direction == Direction.LEFT_UP || direction == Direction.RIGHT_UP) {
            int min = instance.getPlayableWindowMaxHeight() + 10;
            int max = instance.getPlayableWindowMaxHeight() + Math.round(totalFormationHeight * 1.5f);
            if (min > max) {
                return min; // Fallback to min if bounds are invalid
            }
            return random.nextInt(min, max + 1);
        }

        return instance.getPlayableWindowMinHeight();
    }


    private boolean coinFlip(){
        double randomNumber = random.nextDouble();
        return randomNumber < 0.5;
    }



    public void resetSpawnTimer () {
        if (directorType != DirectorType.Instant) {
            lastSpawnTime = currentTime;  // Capture the current time when resetting
            spawnInterval = calculateInitialSpawnInterval(directorType);  // Recalculate the next interval
        }
    }

    public void receiveCredits (float amount) {
        this.credits += amount;
    }

    private MonsterCard selectMonsterCard () {
        if (!affordableMonsters.isEmpty()) {
//            for(MonsterCard card : affordableMonsters){
//                System.out.println(card.getEnemyType() + " with weight: " + card.getWeight());
//            }
//            System.out.println("");
            return weightedRandomSelection(affordableMonsters);
        }
        return null; // Return null if no affordable monsters are available
    }

    private List<MonsterCard> adjustWeights (List<MonsterCard> baseMonsterCards, float difficultyCoefficient) {
        // Adjust the weights based on difficulty coefficient
        return baseMonsterCards.stream().map(card -> {
            float adjustedWeight = calculateAdjustedWeight(card, difficultyCoefficient);
            return new MonsterCard(card.getEnemyType(), card.getCreditCost(), adjustedWeight);
        }).collect(Collectors.toList());
    }


    private float calculateAdjustedWeight (MonsterCard card, float difficultyCoefficient) {
        EnemyCategory category = card.getEnemyType().getEnemyCategory();
        float baseWeight = card.getWeight();
        // Adjust these values to finely control spawn behavior
        float basicIncreaseRate = 2f; // Increase basic enemy weight by a significant factor early on
        float decayRateForBasicEnemies = 0.2f; // Slower decay for Basic enemies (less aggressive than before)
        float growthRateForStrongEnemies = 0.05f; // Slower growth for stronger enemies

        switch (category) {
            case Basic:
                // Increase weight significantly for Basic enemies initially and reduce slowly
                return baseWeight * (basicIncreaseRate - difficultyCoefficient * decayRateForBasicEnemies);
            case Mercenary:
            case Boss:
                // Gradually increase weight for stronger enemies as difficulty increases
                return baseWeight * (1 + difficultyCoefficient * growthRateForStrongEnemies);
            case Summon:
                return 0; //Should never be spawned
            default:
                return baseWeight;  // Default case for any uncategorized types
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

        return null; // Implement a fallback in case no card is selected
    }

    public boolean isActive () {
        return this.active;
    }
}
