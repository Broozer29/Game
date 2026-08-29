package net.riezebos.bruus.tbd.game.level.directors;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.gamestate.GameMode;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.level.EnemyFormation;
import net.riezebos.bruus.tbd.game.level.FormationCreator;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.MiniBossConfig;
import net.riezebos.bruus.tbd.game.level.enums.SpawnFormationEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

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

    private double defaultSpawnWindowDuration = 2; // in secondes
    private double spawnWindowDuration = defaultSpawnWindowDuration;

    private DirectorType directorType;

    private Random random = new Random();
    private double currentTime;
    private boolean active;

    private int miniBossessSpawned = 0;

    public Director(DirectorType directorType, List<MonsterCard> availableCards) {
        this.directorType = directorType;
        this.credits = 0;
        this.lastSpawnTime = 0;
        this.currentTime = 0;
        this.spawnInterval = updateSpawnInterval(directorType);
        this.spawnCashCarrierChance = calculateCashCarrierChance(directorType);
        this.availableCards = new ArrayList<>(availableCards);
        this.active = true;
        this.formationCreator = new FormationCreator();
    }

    private long updateSpawnInterval(DirectorType directorType) {
        spawnWindowDuration = defaultSpawnWindowDuration + (GodRunDetector.getInstance().getGodRunScore() * 0.25f); //spawn voor 0.25 sec langer per "wave" des te hoger de godrunscore is om te helpen altijd iets op het scherm te hebben
        int defaultTime = 0;
        switch (directorType) {
            case Slow -> {
                defaultTime = 7 + random.nextInt(3);
            }
            case Fast -> {
                defaultTime = 3 + random.nextInt(2);
            }
            default -> {
                defaultTime = 0;
            } // Instant directors don't use intervals
        }

        //If godrun detected, spawn more often
        if (GodRunDetector.getInstance().getGodRunScore() >= 1) {
            defaultTime = Math.round(defaultTime * 0.65f);
        }

        return defaultTime; // 10-15 seconds
    }

    private double calculateCashCarrierChance(DirectorType directorType) {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BonusKaart) != null) {
            return 0f; //bonuskaart disables cash carriers
        }


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

        if (directorType == DirectorType.Fast || directorType == DirectorType.Slow) {
            handleRegularDirectorSpawn();
        } else if (directorType == DirectorType.MiniBoss) {
            handleMiniBossDirectorSpawn();
        }
    }

    private void handleMiniBossDirectorSpawn() {
        if (miniBossessSpawned < LevelManager.getInstance().getCurrentMiniBossConfig().getMiniBossesPerLevel()) {
            spawnMiniBoss();
            this.miniBossessSpawned++;
        }
    }

    private void spawnMiniBoss() {
        // Set parameters for spawning
        MonsterCard selectedCard = selectMonsterCard();
        if (selectedCard == null) {
            //failed to select a boss
            return;
        }

        EnemyEnums miniBossType = selectedCard.getEnemyType();
        Direction direction = getMiniBossDirection(miniBossType);
        float scale = miniBossType.getDefaultScale();
        float xMovementSpeed = miniBossType.getMovementSpeed();

        // Call LevelManager's spawnEnemy method
        LevelManager.getInstance().spawnEnemy(
                DataClass.getInstance().getWindowWidth() + Math.round(miniBossType.getBaseWidth() * 0.88f),
                DataClass.getInstance().getPlayableWindowMaxHeight() / 2,
                miniBossType, direction, scale, false, xMovementSpeed);
    }

    private Direction getMiniBossDirection(EnemyEnums enemyEnums) {
        Direction direction = Direction.LEFT;
        if (enemyEnums.equals(EnemyEnums.ShurikenMiniBoss) || enemyEnums.equals(EnemyEnums.MirageMiniBoss)) {
            switch (random.nextInt(2)) {
                case 0:
                    direction = Direction.LEFT_UP;
                    break;
                case 1:
                    direction = Direction.LEFT_DOWN;
                    break;
                default:
                    direction = Direction.LEFT_UP;
            }
        }
        return direction;
    }


    private void handleRegularDirectorSpawn() {
        double randomNumber = random.nextDouble();  // Generate a random number between 0 and 1
        double timeSinceLastCashCarrier = currentTime - DirectorManager.getInstance().getLastCashCarrierSpawnTime();

        // Check for cash carrier spawn conditions
        if (randomNumber <= spawnCashCarrierChance && timeSinceLastCashCarrier >= 45) {
            int amount = GameState.getInstance().getGameMode().equals(GameMode.DoubleTrouble) ? 2 : 1;
            for (int i = 0; i < amount; i++) {
                spawnCashCarrier();
            }
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
                } else if (credits >= selectedCard.getCreditCost() && !GameState.getInstance().getGameMode().equals(GameMode.Formatted)) {
                    spawnEnemy(enemyType);
                    credits -= selectedCard.getCreditCost();
                }
            }
        }
    }

    private boolean canSpawnMoreOfThisEnemy(EnemyEnums enemyEnums) {

        if (enemyEnums.equals(EnemyEnums.RoyalGuardFlagbearer)) {
            if (EnemyManager.getInstance().getEnemies().stream().filter(enemy -> !enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Summon)).count() <= 8) {
                return false; //mag niet spawnen als er niet genoeg non-summoned enemies zijn
            }

            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.RoyalGuardFlagbearer) < 2;
        }

        if (enemyEnums.equals(EnemyEnums.RoyalGuardCaptain)) {
            if (this.directorType.equals(DirectorType.Instant) || GameState.getInstance().getCurrentLevelProgression() < 0.35f) { //we dont immediatly want to spawn these bad boys
                return false;
            }
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.RoyalGuardCaptain) < 1;
        }

        if (enemyEnums.equals(EnemyEnums.RoyalGuardBarricade)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.RoyalGuardBarricade) < 6;
        }

        if (enemyEnums.equals(EnemyEnums.RoyalGuardShieldbearer)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.RoyalGuardBarricade) < 10;
        }

        if (enemyEnums.equals(EnemyEnums.ZergQueen)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.ZergQueen) < 2;
        }

        if (enemyEnums.equals(EnemyEnums.ZergDevourer)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.ZergDevourer) < 6;
        }

        if (enemyEnums.equals(EnemyEnums.ZergGuardian)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.ZergGuardian) < 12;
        }

        if (enemyEnums.equals(EnemyEnums.Tazer)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Tazer) < 3;
        }

        if (enemyEnums.equals(EnemyEnums.Bulldozer)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Bulldozer) < 7;
        }
        if (enemyEnums.equals(EnemyEnums.Energizer)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Energizer) < 7;
        }

        if (enemyEnums.equals(EnemyEnums.Seeker)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Seeker) < 10;
        }
        if (enemyEnums.equals(EnemyEnums.Bomba)) {
            return EnemyManager.getInstance().getAmountOfEnemyTypesAlive(EnemyEnums.Bomba) < 8;
        }

        return true;
    }

    private List<EnemyEnums> getEnemiesThatCanSpawnFromUpOrDown() {
        List<EnemyEnums> eligibleEnemies = new ArrayList<>();

        switch (LevelManager.getInstance().getCurrentEnemyTribe()) {
            case Zerg -> {
                eligibleEnemies.add(EnemyEnums.ZergGuardian);
                eligibleEnemies.add(EnemyEnums.ZergScourge);
            }
            case Pirates -> {
                eligibleEnemies.add(EnemyEnums.Bulldozer);
                eligibleEnemies.add(EnemyEnums.Needler);
            }
            case RoyalGuard -> {
                eligibleEnemies.add(EnemyEnums.RoyalGuardShieldbearer);
                eligibleEnemies.add(EnemyEnums.RoyalGuardBarricade);
            }
        }

        return eligibleEnemies;
    }

    private boolean canSpawnInFormation(EnemyEnums enemyEnums) {
        if (enemyEnums.equals(EnemyEnums.ZergQueen) || enemyEnums.equals(EnemyEnums.Tazer) || enemyEnums.equals(EnemyEnums.RoyalGuardFlagbearer) || enemyEnums.equals(EnemyEnums.RoyalGuardCaptain)) {
            return false;
        }
        return true;
    }

    private void spawnBoss() {
        spawnBoss(LevelManager.getInstance().getSelectedBoss());
        EnemyManager.getInstance().setHasSpawnedABoss(true);
    }

    private void spawnBoss(EnemyEnums enemyEnums) {
        // Set parameters for spawning
        Direction direction = Direction.LEFT;
        float scale = enemyEnums.getDefaultScale();
//        float scale = 0.35f;
        float xMovementSpeed = enemyEnums.getMovementSpeed();
//        float xMovementSpeed = 3f;

        int xCoordinate = DataClass.getInstance().getWindowWidth() + Math.round(enemyEnums.getBaseWidth() * 0.88f);
        int yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() / 2;

        if (enemyEnums.equals(EnemyEnums.BlueBoss)) {
            xCoordinate = Math.round((DataClass.getInstance().getWindowWidth() * 0.5f));
            AnimationManager.getInstance().addUpperAnimation(createWarpInAnimation(xCoordinate, yCoordinate));
        } else if (enemyEnums.equals(EnemyEnums.TwinBoss)) {
            //spawn right twins
            xCoordinate = DataClass.getInstance().getWindowWidth() + Math.round(EnemyEnums.TwinBoss.getBaseWidth() * scale);
            LevelManager.getInstance().spawnEnemy(
                    xCoordinate,
                    Math.round(DataClass.getInstance().getPlayableWindowMaxHeight() * 0.25f),
                    enemyEnums, direction, scale, false, xMovementSpeed);

            LevelManager.getInstance().spawnEnemy(
                    xCoordinate,
                    Math.round(DataClass.getInstance().getPlayableWindowMaxHeight() * 0.75f),
                    enemyEnums, direction, scale, false, xMovementSpeed);


            //spawn left twins
            int leftXCoordinate = Math.round(-(EnemyEnums.TwinBoss.getBaseWidth() * scale));
            Direction rightDirection = Direction.RIGHT;
            LevelManager.getInstance().spawnEnemy(
                    leftXCoordinate,
                    Math.round(DataClass.getInstance().getPlayableWindowMaxHeight() * 0.25f),
                    enemyEnums, rightDirection, scale, false, xMovementSpeed);


            LevelManager.getInstance().spawnEnemy(
                    leftXCoordinate,
                    Math.round(DataClass.getInstance().getPlayableWindowMaxHeight() * 0.75f),
                    enemyEnums, rightDirection, scale, false, xMovementSpeed);
            return; //bypass the normal spawning;
        }

        // Call LevelManager's spawnEnemy method
        LevelManager.getInstance().spawnEnemy(
                xCoordinate,
                yCoordinate,
                enemyEnums, direction, scale, false, xMovementSpeed);
    }

    private SpriteAnimation createWarpInAnimation(int xCoordinate, int yCoordinate) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.setCenterCoordinates(xCoordinate, yCoordinate);
        return spriteAnimation;
    }


    private boolean shouldSpawnFormation(EnemyEnums enemyType) {
        float smallFormationChance = GameState.getInstance().getGameMode().equals(GameMode.Formatted) ? 1 : 0.3f;
        float mediumFormationChance = GameState.getInstance().getGameMode().equals(GameMode.Formatted) ? 1 : 0.2f;

        double time = currentTime - lastFormationSpawnTime;
        if (time < 3 && !GameState.getInstance().getGameMode().equals(GameMode.Formatted)) {
            return false;
        }

        double randomDouble = random.nextDouble();
        double chanceThreshold = switch (enemyType.getEnemyCategory()) {
            case Summon, Special, Boss, MiniBoss -> -1f;
            case Small -> smallFormationChance;
            case Medium -> mediumFormationChance;
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
        float enemyCreditCost = enemyType.getCreditCost();

        if (GameState.getInstance().getGameMode().equals(GameMode.Formatted)) {
            enemyCreditCost *= 1.5f;
        } else {
            enemyCreditCost *= 3f;
        }

        if (enemyType.getEnemyTribe().equals(EnemyTribes.RoyalGuard)) {
            enemyCreditCost *= 1 - (EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.05f);
        }

        float enemyCount = formationType.getEnemyCountInFormation();
        return enemyCount * enemyCreditCost;
    }

    private boolean shouldAttemptSpawn(double currentTime) {
        if (directorType == DirectorType.Instant || directorType == DirectorType.Boss) {
            return true; // Always spawn for Instant or Boss types
        } else if (directorType == DirectorType.MiniBoss) {
            return isWindowForMiniBossSpawn(LevelManager.getInstance().getCurrentMiniBossConfig());
        }

        //regular directors
        double spawnWindowStart = lastSpawnTime + spawnInterval;
        double spawnWindowEnd = spawnWindowStart + spawnWindowDuration;

        // Determine if we're in the spawn window
        isInSpawnWindow = currentTime >= spawnWindowStart && currentTime <= spawnWindowEnd;
        return isInSpawnWindow;
    }


    private boolean isWindowForMiniBossSpawn(MiniBossConfig miniBossConfig) {
        double levelProgression = calculateLevelProgression(GameState.getInstance().getCurrentLevelProgression(), GameState.getInstance().getPredictedFinishSeconds() - GameState.getInstance().getLevelStartTime());

        switch (miniBossConfig) {
            case Medium:
                return (levelProgression >= 0.5f && miniBossessSpawned < 1);
            case Hard:
                boolean returnValue = false;
                if (levelProgression >= 0.25f && miniBossessSpawned < 1) {
                    returnValue = true;
                } else if (levelProgression >= 0.75f && miniBossessSpawned < 2) {
                    returnValue = true;
                }
                return returnValue;
        }
        return false;
    }

    public double calculateLevelProgression(double currentSeconds, double maxSeconds) {
        if (currentSeconds < 0 || maxSeconds < 0) {
            return -1;
        }
        return (currentSeconds / maxSeconds);
    }

    private void spawnEnemy(EnemyEnums enemyType) {
        // Set parameters for spawning
        Direction direction = getSpawnDirection(enemyType);
        float scale = enemyType.getDefaultScale();
        float xMovementSpeed = enemyType.getMovementSpeed();

        // Call LevelManager's spawnEnemy method
        LevelManager.getInstance().spawnEnemy(
                0, 0, //actual coordinates will be recalculated because of random = true
                enemyType, direction, scale, true, xMovementSpeed);
    }

    public void spawnRegularFormation(SpawnFormationEnums formationType, EnemyEnums enemyType) {
        spawnFormationWithParameters(formationType, enemyType);
    }

    private void spawnCashCarrier() {
        spawnEnemy(EnemyEnums.CashCarrier);
    }

    private Direction getSpawnDirection(EnemyEnums enemyType) {
        int godRunScore = GodRunDetector.getInstance().getGodRunScore();
        Random random = new Random();

        // Score >= 4: enemies that can spawn from any direction do so
        if (godRunScore >= 4 && this.getEnemiesThatCanSpawnFromUpOrDown().contains(enemyType)) {
            Direction[] cardinalDirections = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
            return cardinalDirections[random.nextInt(4)];
        }

        // Score >= 2: spawn from left or right randomly
        if (godRunScore >= 2) {
            return random.nextBoolean() ? Direction.RIGHT : Direction.LEFT;
        }

        // Default: spawn from left
        return Direction.LEFT;
    }

    private void spawnFormationWithParameters(SpawnFormationEnums formationType, EnemyEnums enemyType) {
        Direction direction = getSpawnDirection(enemyType);
        float xMovementSpeed = enemyType.getMovementSpeed();
        float yMovementSpeed = xMovementSpeed;

        int formationWDistance;
        int formationHDistance;

        formationWDistance = Math.round((enemyType.getBaseWidth() * enemyType.getDefaultScale()));
        formationHDistance = Math.round((enemyType.getBaseWidth() * enemyType.getDefaultScale()));

        EnemyFormation formation = formationCreator.createFormation(formationType, formationWDistance, formationHDistance);
        int totalFormationWidth = formation.getFormationWidth() * formation.getWidthDistance();
        int totalFormationHeight = formation.getFormationHeight() * formation.getHeightDistance();

        int baseX = calculateBaseX(totalFormationWidth, direction);
        int baseY = calculateBaseY(totalFormationHeight, direction);

        formation.spawnFormation(baseX, baseY, enemyType, enemyType, direction, xMovementSpeed, yMovementSpeed);
        lastFormationSpawnTime = currentTime;
    }

    private int calculateBaseX(int totalFormationWidth, Direction direction) {
        DataClass instance = DataClass.getInstance();
        if (direction == Direction.LEFT) {
            // For LEFT direction, spawn at or beyond the right edge of the board
            int bound = instance.getWindowWidth() + (random.nextInt((int) Math.round(totalFormationWidth * 0.5)));
            return instance.getWindowWidth() + Math.max(0, bound);
        } else if (direction == Direction.RIGHT) {
            // For RIGHT direction, spawn at or before the left edge of the board
            int bound = -(totalFormationWidth + random.nextInt(totalFormationWidth));
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
        if (this.directorType == DirectorType.MiniBoss) {
            return availableCards.get(random.nextInt(availableCards.size()));
        }

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
        float growthRateForStrongEnemies = 0.175f; // Slower growth for stronger enemies

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
