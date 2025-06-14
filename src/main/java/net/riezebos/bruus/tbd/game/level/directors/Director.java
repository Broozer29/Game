package net.riezebos.bruus.tbd.game.level.directors;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.level.EnemyFormation;
import net.riezebos.bruus.tbd.game.level.FormationCreator;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.SpawnFormationEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Director {
    private float credits;
    private double lastSpawnTime = 0;
    private long spawnInterval; // Interval for Slow and Fast directors

    private double spawnCashCarrierChance;
    private double lastFormationSpawnTime;
    private List<MonsterCard> availableCards;

    private FormationCreator formationCreator;
    private boolean isInSpawnWindow = false; // New flag to track if we're in the spawn window

    private double spawnWindowDuration = 3.0; // seconds, adjust as needed

    private DirectorType directorType;

    private Random random = new Random();
    private double currentTime;
    private boolean active;

    public Director(DirectorType directorType, List<MonsterCard> availableCards) {
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

    private long calculateInitialSpawnInterval(DirectorType directorType) {
        return switch (directorType) {
            case Slow -> 10 + (long) (Math.random() * 5); // 10-15 seconds
            case Fast -> 5 + (long) (Math.random() * 5); // 5-10 seconds
            default -> 0; // Instant directors don't use intervals
        };
    }

    private double calculateCashCarrierChance(DirectorType directorType) {
        return switch (directorType) {
            case Slow -> 0.1f;
            case Fast -> 0.05f;
            default -> 0f; // instant directors should not spawn cash carriers
        };
    }

    public void update() {
        currentTime = GameState.getInstance().getGameSeconds();

        // Check if we should spawn enemies
        if (shouldAttemptSpawn(currentTime)) {
            attemptSpawn(); // Spawn enemies
        }

        // Only update lastSpawnTime after the entire window has passed
        if (currentTime > lastSpawnTime + spawnInterval + spawnWindowDuration) {
            lastSpawnTime = currentTime; // Move to cooldown period after the window ends
        }

        // Check for conditions to deactivate the Director
        if (directorType == DirectorType.Instant && credits < 15) {
            this.active = false;
        }
        if (directorType == DirectorType.Boss) {
            this.active = false;
        }
    }

    private void attemptSpawn() {
        if (directorType == DirectorType.Boss) {
            spawnBoss();
            return; //We dont want to do anything else but spawning the boss at this time, subject to change
        }

        double randomNumber = random.nextDouble();  // Generate a random number between 0 and 1
        double timeSinceLastCashCarrier = currentTime - DirectorManager.getInstance().getLastCashCarrierSpawnTime();

        // Check for cash carrier spawn conditions
        if (randomNumber <= spawnCashCarrierChance && timeSinceLastCashCarrier >= 45) {
            spawnCashCarrier();  // Always a scout as of now
            DirectorManager.getInstance().setLastCashCarrierSpawnTime(currentTime);
        }

        if (credits > minimumMonsterCost()) {
            SpawnFormationEnums formationType = SpawnFormationEnums.getRandomFormation();
            MonsterCard selectedCard = selectMonsterCard();

            if (selectedCard != null && canSpawnMoreOfThisEnemy(selectedCard.getEnemyType())) {
                EnemyEnums enemyType = selectedCard.getEnemyType();
                float totalFormationCost = calculateFormationCost(formationType, enemyType);

                if (credits >= totalFormationCost && canSpawnInFormation(enemyType) && shouldSpawnFormation(enemyType)) {
                    spawnRegularFormation(formationType, enemyType);
                    credits -= totalFormationCost;
                } else if (credits >= selectedCard.getCreditCost()) {
                    spawnEnemy(enemyType, true);
                    credits -= selectedCard.getCreditCost();
                }
            }
        }
    }

    private boolean canSpawnMoreOfThisEnemy(EnemyEnums enemyEnums) {
        if (enemyEnums.equals(EnemyEnums.ZergQueen)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.ZergQueen) < 2;
        }

        if (enemyEnums.equals(EnemyEnums.ZergDevourer)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.ZergDevourer) < 8;
        }

        if (enemyEnums.equals(EnemyEnums.ZergGuardian)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.ZergGuardian) < 14;
        }

        if (enemyEnums.equals(EnemyEnums.Tazer)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Tazer) < 3;
        }

        if (enemyEnums.equals(EnemyEnums.Bulldozer)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Bulldozer) < 5;
        }
        if (enemyEnums.equals(EnemyEnums.Energizer)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Energizer) < 8;
        }

        if (enemyEnums.equals(EnemyEnums.Seeker)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Seeker) < 12;
        }
        if (enemyEnums.equals(EnemyEnums.Bomba)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Bomba) < 6;
        }

        return true;
    }

    private boolean canSpawnInFormation(EnemyEnums enemyEnums) {
        if (enemyEnums.equals(EnemyEnums.ZergQueen) || enemyEnums.equals(EnemyEnums.Tazer) || enemyEnums.equals(EnemyEnums.Bomba)) {
            return false;
        }
        return true;
    }

    private void spawnBoss() {
        spawnBoss(LevelManager.getInstance().getNextBoss());
        EnemyManager.getInstance().setHasSpawnedABoss(true);
    }

    private void spawnBoss(EnemyEnums enemyEnums) {
        // Set parameters for spawning
        Direction direction = Direction.LEFT;
        float scale = enemyEnums.getDefaultScale();
        float xMovementSpeed = enemyEnums.getMovementSpeed();
        float yMovementSpeed = enemyEnums.getMovementSpeed();

        // Call LevelManager's spawnEnemy method
        LevelManager.getInstance().spawnEnemy(
                DataClass.getInstance().getWindowWidth() + Math.round(enemyEnums.getBaseWidth() * 0.88f),
                DataClass.getInstance().getPlayableWindowMaxHeight() / 2,
                enemyEnums, direction, scale, false, xMovementSpeed, yMovementSpeed, false);
    }


    private boolean shouldSpawnFormation(EnemyEnums enemyType) {
        double time = currentTime - lastFormationSpawnTime;
        if (time < 3) {
            return false;
        }

        double randomDouble = random.nextDouble();
        double chanceThreshold = switch (enemyType.getEnemyCategory()) {
            case Summon, Special, Boss -> -1f;
            case Small -> 0.3f; //20% chance of spawning a formation
            case Medium -> 0.2f; //10% chance of spawning formation
        };

        return randomDouble < chanceThreshold;
    }

    private float minimumMonsterCost() {
        return availableCards.stream()
                .map(MonsterCard::getCreditCost)
                .min(Float::compare)
                .orElse(Float.MAX_VALUE);
    }

    private float calculateFormationCost(SpawnFormationEnums formationType, EnemyEnums enemyType) {
        float enemyCount = formationType.getEnemyCountInFormation();
        return enemyCount * enemyType.getCreditCost();
    }

    private boolean shouldAttemptSpawn(double currentTime) {
        if (directorType == DirectorType.Instant || directorType == DirectorType.Boss) {
            return true; // Always spawn for Instant or Boss types
        } else {
            double spawnWindowStart = lastSpawnTime + spawnInterval;
            double spawnWindowEnd = spawnWindowStart + spawnWindowDuration;

            // Determine if we're in the spawn window
            isInSpawnWindow = currentTime >= spawnWindowStart && currentTime <= spawnWindowEnd;
            return isInSpawnWindow;
        }
    }

    private void spawnEnemy(EnemyEnums enemyType, boolean randomLocation) {
        // Set parameters for spawning
        Direction direction = Direction.LEFT;
        float scale = enemyType.getDefaultScale();
        float xMovementSpeed = enemyType.getMovementSpeed();
        float yMovementSpeed = enemyType.getMovementSpeed();

        // Call LevelManager's spawnEnemy method
        LevelManager.getInstance().spawnEnemy(
                DataClass.getInstance().getWindowWidth() + Math.round(enemyType.getBaseWidth() * scale),
                DataClass.getInstance().getPlayableWindowMaxHeight() / 2 - Math.round((enemyType.getBaseHeight() * scale) / 2),
                enemyType, direction, scale, randomLocation, xMovementSpeed, yMovementSpeed, false);
    }

    public void spawnRegularFormation(SpawnFormationEnums formationType, EnemyEnums enemyType) {
        spawnFormationWithParameters(formationType, enemyType, null, false);
    }

    private void spawnCashCarrier() {
//        spawnEntourageFormation(formationType, EnemyEnums.getRandomEnemy(EnemyCategory.Mercenary), EnemyEnums.CashCarrier);
//        this.lastCashCarrierSpawnTime = currentTime;
        spawnEnemy(EnemyEnums.CashCarrier, true);
    }

    private void spawnEntourageFormation(SpawnFormationEnums formationType, EnemyEnums primaryEnemy, EnemyEnums secondaryEnemy) {
        spawnFormationWithParameters(formationType, primaryEnemy, secondaryEnemy, true);
    }

    private void spawnFormationWithParameters(SpawnFormationEnums formationType, EnemyEnums primaryEnemyType, EnemyEnums secondaryEnemyType, boolean isEntourage) {
        Direction direction = Direction.LEFT;
        float xMovementSpeed = isEntourage ? secondaryEnemyType.getMovementSpeed() : primaryEnemyType.getMovementSpeed();
        float yMovementSpeed = xMovementSpeed;

        int formationWDistance;
        int formationHDistance;

        if (isEntourage) {
            formationWDistance = Math.round(secondaryEnemyType.getBaseWidth() * 1.1f);
            formationHDistance = Math.round(secondaryEnemyType.getBaseHeight() * 1.1f);
        } else {
            formationWDistance = Math.round(primaryEnemyType.getBaseWidth() * 1.1f);
            formationHDistance = Math.round(primaryEnemyType.getBaseHeight() * 1.1f);
        }

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


    public void receiveCredits(float amount) {
        this.credits += amount;
    }

    private MonsterCard selectMonsterCard() {
        List<MonsterCard> adjustedCards = adjustWeights(availableCards, GameState.getInstance().getDifficultyCoefficient());
        if (!adjustedCards.isEmpty()) {
            return weightedRandomSelection(adjustedCards);
        }
        return null; // Return null if no adjusted monsters are available
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
        float growthRateForStrongEnemies = 0.15f; // Slower growth for stronger enemies

        switch (category) {
            case Small:
                // Increase weight significantly for Basic enemies initially and reduce slowly
                return baseWeight * (basicIncreaseRate - difficultyCoefficient * decayRateForBasicEnemies);
            case Medium:
            case Boss:
                // Gradually increase weight for stronger enemies as difficulty increases
                return baseWeight * (1 + difficultyCoefficient * growthRateForStrongEnemies);
            case Summon:
                return 0; // Should never be spawned
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

    public boolean isActive() {
        return this.active;
    }

    public void setIsActive(boolean active) {
        this.active = active;
    }
}
