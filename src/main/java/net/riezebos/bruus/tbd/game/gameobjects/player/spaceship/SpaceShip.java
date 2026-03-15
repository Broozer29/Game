package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputEnums;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputReader;
import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.items.carrier.KineticDynamo;
import net.riezebos.bruus.tbd.game.level.directors.DirectorManager;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.OrbitingObjectsFormatter;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpaceShip extends GameObject {

    private float directionx;
    private float directiony;

    private float accumulatedXCoordinate;
    private float accumulatedYCoordinate;
    private boolean overridePlayerControls = false;
    private ArrayList<Point> movementDirectionsToExecute = new ArrayList<>();
    private float knockbackDamping = 0.9f;  // How quickly knockback reduces each update

    private float currentShieldRegenDelayFrame;
    private boolean controlledByKeyboard = true;
    private Set<Integer> pressedKeys = new HashSet<>();
    private boolean isImmune;

    private SpriteAnimation exhaustAnimation = null;  //inherit from gameobject

    private PlayerStats playerStats = PlayerStats.getInstance();

    private List<SpriteAnimation> playerFollowingAnimations = new ArrayList<SpriteAnimation>();  //inherit from gameobject?
    private PrimaryPlayerGun primaryPlayerGun = null;
    private SpaceShipSpecialGun spaceShipSpecialGun = null;
    private List<SpecialAttack> playerFollowingSpecialAttacks = new ArrayList<SpecialAttack>();
    public boolean allowMovementBeyondBoundaries = false;
    private double lastTimeCollisionDamageTaken = 0;


    private float attackSpeedModifier = 1f;
    private float critDamageModifier = 2f;
    private float movementSpeedModifier = 1f;
    private float specialAttackRechargeCooldownBonusModifier = 0;
    private float specialAttackDamageModifier = 1f;
    private float protossConstructionSpeedModifier = 1f;
    private float thornsDamageModifier = 0f; //starting at 0 causes thorns to be disabled by default
    private float igniteDamageModifier = 1f;
    private float droneDamageModifier = 1f;
    private float maxShieldModifier = 1;
    private float maxOverloadedShieldModifier = 2;
    private int maxSpecialAttackCharges = 1;
    private float specialAttackRechargeCooldownModifier = 1f;
    private float shieldRegenModifier = 1;
    private boolean continueShieldRegenThroughDamage = false;

    private float igniteDurationModifier = 1;
    private float fuelCannisterUsageModifier = 1;
    private float fuelCannisterMaxCapacityModifier = 1;
    private float fuelCannisterRegenModifier = 1;

    private int droneOrbitRadius = 85;
    private DroneTypes droneTypes = DroneTypes.Missile;

    private ControllerInputReader controllerInputReader;

    public SpaceShip(SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
        playerStats = PlayerStats.getInstance();
        initShip();
    }

    public SpaceShip(SpriteConfiguration spriteConfiguration, ControllerInputReader controllerInputReader) {
        super(spriteConfiguration);
        playerStats = PlayerStats.getInstance();
        this.controllerInputReader = controllerInputReader;
        initShip();
    }

    //This method should only be used to bring the player back after dying, not as a general "reset"
    // currently disabled because stuivie is disabled
    public void reviveSpaceShip() {
        boolean shouldLoadEngineAnim = (this.exhaustAnimation != null && this.exhaustAnimation.isVisible()) ? false : true;
        if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setImageType(ImageEnums.ProtossCarrier);
            spriteConfiguration.setxCoordinate(this.xCoordinate);
            spriteConfiguration.setyCoordinate(this.yCoordinate);
            spriteConfiguration.setScale(0.9f);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
            setAnimation(new SpriteAnimation(spriteAnimationConfiguration));
            shouldLoadEngineAnim = false;
        } else {
            setImage(playerStats.getSpaceShipImage());
        }
        this.setVisible(true);
        initDeathAnimation(ImageEnums.Destroyed_Explosion);

        currentShieldRegenDelayFrame = 0;
        if (shouldLoadEngineAnim) {
            initExhaustAnimation(ImageEnums.Default_Player_Engine);
            this.exhaustAnimation.setAnimationScale(0.3f);
        }
        this.currentShieldPoints = playerStats.getMaxShieldHitPoints();
        this.currentHitpoints = playerStats.getMaxHitPoints();
        this.setImmune(false);
    }

    private void initShip() {
        this.isImmune = false;
        directionx = 0;
        directiony = 0;
        this.friendly = true;
        this.playerFollowingAnimations.clear();
        this.playerFollowingSpecialAttacks.clear();
        this.accumulatedYCoordinate = this.yCoordinate;
        this.accumulatedXCoordinate = this.xCoordinate;
        this.knockbackDamping = playerStats.getKnockBackDamping();
        pressedKeys = new HashSet<>();
        this.baseArmor = 0 + PlayerStats.baseArmor;
        this.attackSpeed = PlayerStats.getInstance().getBaseAttackSpeed();

        boolean shouldLoadEngineAnim = true;
        if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setImageType(ImageEnums.ProtossCarrier);
            spriteConfiguration.setxCoordinate(this.xCoordinate);
            spriteConfiguration.setyCoordinate(this.yCoordinate);
            spriteConfiguration.setScale(0.9f);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
            setAnimation(new SpriteAnimation(spriteAnimationConfiguration));
            shouldLoadEngineAnim = false;
        } else {
            setImage(playerStats.getSpaceShipImage());
        }


        currentShieldRegenDelayFrame = 0;
        this.primaryPlayerGun = new PrimaryPlayerGun();
        this.spaceShipSpecialGun = new SpaceShipSpecialGun();
        if (shouldLoadEngineAnim) {
            initExhaustAnimation(ImageEnums.Default_Player_Engine);
            this.exhaustAnimation.setAnimationScale(0.3f);
        }

        initDeathAnimation(ImageEnums.Destroyed_Explosion);
        this.setObjectType("Player spaceship");
        this.effects = new CopyOnWriteArrayList<>();
        this.hasAttack = true;

        this.currentHitpoints = playerStats.getMaxHitPoints();
        this.currentShieldPoints = playerStats.getMaxShieldHitPoints();
        applyOnCreationEffects();

        //Again cause Glass Cannon modifies this, so we need to recalculate it if this item exists. Obvious code smell
        this.currentHitpoints = playerStats.getMaxHitPoints();
        this.currentShieldPoints = playerStats.getMaxShieldHitPoints();
    }

    private void applyOnCreationEffects() {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.FocusCrystal) != null) {
            SpriteConfiguration focusCrystalConfig = new SpriteConfiguration();
            focusCrystalConfig.setxCoordinate(getXCoordinate());
            focusCrystalConfig.setyCoordinate(getYCoordinate());
            focusCrystalConfig.setScale(1);
            focusCrystalConfig.setTransparancyAlpha(0.15f);
            focusCrystalConfig.setImageType(ImageEnums.Highlight);

            SpriteAnimationConfiguration focusCrystalAnimConfig = new SpriteAnimationConfiguration(focusCrystalConfig, 10, true);
            SpriteAnimation focusCrystalAnimation = new SpriteAnimation(focusCrystalAnimConfig);

            focusCrystalAnimation.setAnimationScale(5.714f);

            addPlayerFollowingAnimation(focusCrystalAnimation);
            AnimationManager.getInstance().addUpperAnimation(focusCrystalAnimation);
        }
    }

    private boolean firedPostCreationEffects = false;

    private void postCreationActivities() {
        //This method exists because some managers or methods REQUIRE the spaceship to have finished initializing
        if (!firedPostCreationEffects) {
            //todo deze is verplaatst uit applyOnCreationEffects dus dit kan buggy gevolgen hebben maar multiplayer he....
            for (Item item : PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.ApplyOnCreation)) {
                item.applyEffectToObject(this);
            }

            for (int i = 0; i < playerStats.getAmountOfDrones(); i++) {
                FriendlyManager.getInstance().addDrone(this);
            }

            if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)) {
                for (int i = 0; i < 2; i++) {
                    FriendlyManager.getInstance().addProtossShip(DroneTypes.ProtossScout, this);
                }
            }

            OrbitingObjectsFormatter.reformatOrbitingObjects(this, this.getDroneOrbitRadius());
            firedPostCreationEffects = true;
        }
    }


    private void initExhaustAnimation(ImageEnums imageType) {
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, true);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(imageType);
        exhaustAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        AnimationManager.getInstance().addLowerAnimation(exhaustAnimation);
    }

    private void initDeathAnimation(ImageEnums imageType) {
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(imageType);
        destructionAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        destructionAnimation.setAnimationScale(2.5f);
    }

    public void addShieldDamageAnimation() {
        long shieldAnimationCount = playerFollowingAnimations.stream()
                .filter(spriteAnimation -> spriteAnimation.getImageEnum().equals(ImageEnums.Default_Player_Shield_Damage)
                ).count();

        if (shieldAnimationCount < 10) {
            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(this.spriteConfiguration, 1, false);
            spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Default_Player_Shield_Damage);

            SpriteAnimation shieldAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            shieldAnimation.setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
            playerFollowingAnimations.add(shieldAnimation);

            AnimationManager.getInstance().addUpperAnimation(shieldAnimation);
        }
    }

    @Override
    public void takeDamage(float damageTaken) {
        if (this.isImmune || DevTestSettings.playerIsImmune) {
            return; //The player is immune, we don't want to do anything here
        }

        if (damageTaken > 0) {
            lastGameSecondDamageTaken = GameState.getInstance().getGameSeconds();
            this.currentShieldRegenDelayFrame = 0;

            if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.GlassCannon) != null) {
                damageTaken *= 2f;
            }

            damageTaken = Math.max(damageTaken - PlayerStats.getInstance().getFlatDamageReduction(), Math.min(1, damageTaken));

            AudioManager.getInstance().addAudio(AudioEnums.PlayerTakesDamage);

            // Check if the damage pierces the shield
            float shieldPiercingDamage = currentShieldPoints - damageTaken;
            if (shieldPiercingDamage < 0) {
                // Apply the damage that pierced through the shield to hit points
                changeHitPoints(shieldPiercingDamage); //Remove the "-" because shieldPiercingDamage is already a negative value
                //If there were any shields left, show the animation
                if (currentShieldPoints > 0) {
                    addShieldDamageAnimation();
                }
                currentShieldPoints = 0;

            } else {
                // Shield absorbs all damage
                changeShieldHitpoints(-damageTaken);
                addShieldDamageAnimation();
            }
        } else if (damageTaken < 0) {
            // Apply healing
            changeHitPoints(-damageTaken); //Negative to change the negative back to a positive
        }
    }

    private void changeHitPoints(float change) {
        this.currentHitpoints += change;
        float maxHitPoints = playerStats.getMaxHitPoints();
        if (this.currentHitpoints > maxHitPoints) {
            this.currentHitpoints = maxHitPoints;
        }
    }

    public void changeShieldHitpoints(float change) {
        this.currentShieldPoints += change;
        float maxShieldPoints = playerStats.getMaxShieldHitPoints() * this.maxShieldModifier;
        if (currentShieldPoints > (maxShieldPoints)) {
            currentShieldPoints = maxShieldPoints;
        }
    }

    private void reduceOverloadedShieldPoints() {
        if (currentShieldPoints > playerStats.getMaxShieldHitPoints()) {
            currentShieldPoints -= playerStats.getOverloadedShieldDiminishAmount();
        }
    }



    public boolean isAllowedToBuildProtoss = true;
    public float protossShipBuilderTimer = 0.0f;
    public float protossShipBuildTime = 5;
    public float overBuildResidu = 0;
    private int scoutCount = PlayerStats.getDefaultCarrierStartingScouts();
    private int corsairCount = 0;
    private int arbiterCount = 0;
    private int shuttleCount = 0;

    public void updateGameTick() {
        this.currentShieldRegenDelayFrame++;
        postCreationActivities();
        primaryPlayerGun.updateFrameCount(this);
        spaceShipSpecialGun.updateFrameCount(this);

        movePlayerAnimations();
        moveSpecialAttacks();
        removeInvisibleAnimations();
        updateGameObjectEffects();
        reduceOverloadedShieldPoints();

        if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)) {
            ProtossUtils.getInstance().buildProtossShips(this);
        }

        boolean shouldRegenShields = false;

        if (this.isContinueShieldRegenThroughDamage() && currentShieldPoints < playerStats.getMaxShieldHitPoints()) {
            shouldRegenShields = true;
        } else if (currentShieldRegenDelayFrame >= playerStats.getShieldRegenDelay() && currentShieldPoints < playerStats.getMaxShieldHitPoints()) {
            shouldRegenShields = true;
        }

        if (shouldRegenShields) {
            repairShields(PlayerStats.getInstance().getShieldRegenPerTick() * shieldRegenModifier);
        }
    }


    private void movePlayerAnimations() {
        for (SpriteAnimation anim : playerFollowingAnimations) {
            anim.setOriginCoordinates(getCenterXCoordinate(), getCenterYCoordinate());
        }
    }

    private void removeInvisibleAnimations() {
        playerFollowingAnimations.removeIf(animation -> !animation.isVisible());
    }


    private void moveSpecialAttacks() {
        Iterator<SpecialAttack> iterator = playerFollowingSpecialAttacks.iterator();
        while (iterator.hasNext()) {
            SpecialAttack specialAttack = iterator.next();
            if (!specialAttack.isVisible()) {
                iterator.remove();
            } else {
                if (specialAttack.centeredAroundPlayer()) {
                    specialAttack.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
                }
                specialAttack.updateBoardBlock();
                specialAttack.getAnimation().setAnimationBounds(specialAttack.getAnimation().getXCoordinate(), specialAttack.getAnimation().getYCoordinate());
            }
        }
    }


    // Moves the spaceship, constantly called
    private float knockbackVelocityX = 0;
    private float knockbackVelocityY = 0;
    private boolean knockbackActive = false;

    private float previousAccumulatedX;
    private float previousAccumulatedY;
    private int previousXCoordinate;
    private int previousYCoordinate;

    public void move() {
        if (overridePlayerControls) {
            return;  // Ignore movement if controls are overridden
        }

        // Update queued movements with player input
        if (directionx != 0 || directiony != 0) {
            movementDirectionsToExecute.add(new Point(directionx, directiony));
        }


        KineticDynamo kineticDynamo = (KineticDynamo) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.KineticDynamo);
        if (kineticDynamo != null) {
            kineticDynamo.buildUpEnergy(directionx, directiony);
        }

        // Process movement every frame
        Point playerInputMovement = new Point(0, 0); // Default to zero movement

        if (!movementDirectionsToExecute.isEmpty()) {
            playerInputMovement = movementDirectionsToExecute.get(0);
            // Remove player input movement from the queue after processing it
            movementDirectionsToExecute.remove(0);
        }

        // Combine player input with knockback velocities
        float totalMovementX = playerInputMovement.getX();
        float totalMovementY = playerInputMovement.getY();

        if (knockbackActive) {
            totalMovementX += knockbackVelocityX;
            totalMovementY += knockbackVelocityY;

            // Apply damping to knockback velocities
            knockbackVelocityX *= knockbackDamping;
            knockbackVelocityY *= knockbackDamping;

            // If knockback velocities are very small, stop knockback
            if (Math.abs(knockbackVelocityX) < 0.01f && Math.abs(knockbackVelocityY) < 0.01f) {
                knockbackVelocityX = 0;
                knockbackVelocityY = 0;
                knockbackActive = false;
            }
        }

        // Store previous coordinates
        previousAccumulatedX = accumulatedXCoordinate;
        previousAccumulatedY = accumulatedYCoordinate;
        previousXCoordinate = xCoordinate;
        previousYCoordinate = yCoordinate;

        // Update coordinates
        accumulatedXCoordinate += totalMovementX;
        accumulatedYCoordinate += totalMovementY;
        xCoordinate = Math.round(accumulatedXCoordinate);
        yCoordinate = Math.round(accumulatedYCoordinate);


        if (!allowMovementBeyondBoundaries) {
            // Get boundary limits
            int windowWidth = DataClass.getInstance().getWindowWidth();
            int playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();

            // Ensure X coordinate is within bounds
            if (xCoordinate < 0) {
                xCoordinate = 0;
                accumulatedXCoordinate = 0;  // Also reset accumulated to avoid future inconsistencies
            } else if (xCoordinate > windowWidth - this.getWidth()) {
                xCoordinate = windowWidth - this.getWidth();
                accumulatedXCoordinate = xCoordinate;  // Ensure accumulated matches new clamped position
            }

            // Ensure Y coordinate is within bounds
            if (yCoordinate < 0) {
                yCoordinate = 0;
                accumulatedYCoordinate = 0;  // Also reset accumulated to avoid future inconsistencies
            } else if (yCoordinate > playableWindowMaxHeight - this.getHeight()) {
                yCoordinate = playableWindowMaxHeight - this.getHeight();
                accumulatedYCoordinate = yCoordinate;  // Ensure accumulated matches new clamped position
            }
        }


        // Update bounds and other properties
        bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
        this.currentLocation = new Point(this.xCoordinate, this.yCoordinate);

        // Reset directions if controlled by keyboard
        if (!controlledByKeyboard) {
            haltMoveDown();
            haltMoveLeft();
            haltMoveRight();
            haltMoveUp();
        }

        if (this.exhaustAnimation != null) {
            this.exhaustAnimation.setXCoordinate(this.xCoordinate - (exhaustAnimation.getWidth() / 2));
            this.exhaustAnimation.setYCoordinate(this.getCenterYCoordinate() - (this.exhaustAnimation.getHeight() / 2) + 3);
        }

        if (this.destructionAnimation != null) {
            destructionAnimation.setCenterCoordinates(this.xCoordinate, this.yCoordinate);
        }

        if (this.animation != null) {
            this.animation.setXCoordinate(this.xCoordinate);
            this.animation.setYCoordinate(this.yCoordinate);
        }

        updateBoardBlock();
    }

    @Override
    public void setCenterCoordinates(int newXCoordinate, int newYCoordinate) {
        super.setCenterCoordinates(newXCoordinate, newYCoordinate);
        this.previousAccumulatedY = this.yCoordinate;
        this.previousAccumulatedX = this.xCoordinate;
        this.previousXCoordinate = this.xCoordinate;
        this.previousYCoordinate = this.yCoordinate;
        this.accumulatedXCoordinate = this.xCoordinate;
        this.accumulatedYCoordinate = this.yCoordinate;
        this.movementDirectionsToExecute.clear();
    }

    public void applyKnockback(CollisionInfo collisionInfo, float knockbackStrength) {
        Point collisionPoint = collisionInfo.getCollisionPoint();

        // Determine the knockback direction based on collision point
        float knockbackDirectionX = this.getCenterXCoordinate() - collisionPoint.getX();
        float knockbackDirectionY = this.getCenterYCoordinate() - collisionPoint.getY();

        // Normalize the knockback direction
        float length = (float) Math.sqrt(knockbackDirectionX * knockbackDirectionX + knockbackDirectionY * knockbackDirectionY);
        if (length != 0) {
            knockbackDirectionX /= length;
            knockbackDirectionY /= length;
        } else {
            // If length is zero, set a default knockback direction
            knockbackDirectionX = -1;
            knockbackDirectionY = 0; // Arbitrary direction upwards
        }

        // Set initial knockback velocities
        knockbackVelocityX = knockbackDirectionX * knockbackStrength;
        knockbackVelocityY = knockbackDirectionY * knockbackStrength;

        knockbackActive = true;
    }


    private LinkedList<Point> resetPositions = new LinkedList<>();

    public void resetToPreviousPosition() {
        // Reset to previous position
        accumulatedXCoordinate = previousAccumulatedX;
        accumulatedYCoordinate = previousAccumulatedY;
        xCoordinate = previousXCoordinate;
        yCoordinate = previousYCoordinate;

        // Update bounds and other properties
        bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
        this.currentLocation = new Point(this.xCoordinate, this.yCoordinate);

        // Add the new position to resetPositions
        Point newPosition = new Point(xCoordinate, yCoordinate);
        resetPositions.add(newPosition);

        // Keep the list size up to 4
        if (resetPositions.size() > 4) {
            resetPositions.removeFirst();
        }

        // Check if we are bouncing between two positions
        if (resetPositions.size() == 4) {
            boolean isStuck = resetPositions.get(0).equals(resetPositions.get(1)) &&
                    resetPositions.get(1).equals(resetPositions.get(2)) &&
                    resetPositions.get(2).equals(resetPositions.get(3));

            if (isStuck) {
                // Player is stuck
                // Move the player to a safe position
                float addedDirectionX = -6;
                float addedDirectionY = -3;
                if (directionx != 0) {
                    addedDirectionX = directionx;
                }
                if (directiony != 0) {
                    addedDirectionY = directiony;
                }

                accumulatedXCoordinate += (addedDirectionX * 3);
                accumulatedYCoordinate += (addedDirectionY * 3);
                xCoordinate = Math.round(accumulatedXCoordinate);
                yCoordinate = Math.round(accumulatedYCoordinate);

                // Clear the resetPositions list
                resetPositions.clear();

                // Update bounds and other properties
                bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
                this.currentLocation = new Point(this.xCoordinate, this.yCoordinate);
            }
        }
    }


    // Launch a missile from the center point of the spaceship
    private void startPrimaryFiring() {
        primaryPlayerGun.fire(this.xCoordinate + this.width, this.getCenterYCoordinate(), playerStats.getAttackType(), this);
    }

    public float getDamage() {
        float attackDamage = PlayerStats.getInstance().getBaseDamage() * this.bonusDamageMultiplier;
        if (isACrit && this.isFriendly()) {
            attackDamage *= critDamageModifier;
        }
        if (attackDamage < 0.05) {
            return 0.05f;
        } else {
            return attackDamage;
        }
    }

    private void haltPrimaryFiring() {
        primaryPlayerGun.stopFiring();
    }

    private void fireSpecialAttack() {
        spaceShipSpecialGun.fire(this.getCenterXCoordinate(), this.getCenterYCoordinate(),
                playerStats.getPlayerSpecialAttackType(), this);
    }

    public void repairHealth(float healAmount) {
        changeHitPoints(healAmount);
    }

    public void repairShields(float healAmount) {
        changeShieldHitpoints(healAmount);
    }

    public SpaceShipSpecialGun getSpecialGun() {
        return this.spaceShipSpecialGun;
    }

    public void addFollowingSpecialAttack(SpecialAttack specialAttack) {
        this.playerFollowingSpecialAttacks.add(specialAttack);
    }

    public SpriteAnimation getExhaustAnimation() {
        return this.exhaustAnimation;
    }


    public synchronized void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (!pressedKeys.isEmpty()) {
            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext(); ) {
                switch (it.next()) {
                    case (KeyEvent.VK_SPACE):
                        startPrimaryFiring();
                        break;
                    case (KeyEvent.VK_A):
                    case (KeyEvent.VK_LEFT):
                        moveLeftQuick(1);
                        break;
                    case (KeyEvent.VK_D):
                    case (KeyEvent.VK_RIGHT):
                        moveRightQuick(1);
                        break;
                    case (KeyEvent.VK_W):
                    case (KeyEvent.VK_UP):
                        moveUpQuick(1);
                        break;
                    case (KeyEvent.VK_S):
                    case (KeyEvent.VK_DOWN):
                        moveDownQuick(1);
                        break;
                    case (KeyEvent.VK_Q):
                    case (KeyEvent.VK_ENTER):
                        fireSpecialAttack();
                        break;
                    case (KeyEvent.VK_SHIFT):
                        if (DevTestSettings.alloweSuicidebutton && GameState.getInstance().getGameState() == GameStatusEnums.Playing) {
                            this.takeDamage(1000000000f);
                        }
                        break;
                    case (KeyEvent.VK_Y):
                        spawnPortal();
                        break;
                }
            }
        }
    }

    //Cheat code method
    private void spawnPortal() {
        DirectorManager.getInstance().setEnabled(false);
        GameState.getInstance().setGameState(GameStatusEnums.Level_Finished);
    }

    public synchronized void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_Q) {
            haltPrimaryFiring();
        }
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            haltMoveLeft();
        }
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            haltMoveRight();
        }
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            haltMoveUp();
        }
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            haltMoveDown();
        }
    }

    // Called by GameBoard every loop if a controller is connected
    private boolean isFiringPrimary = false;

    public void update() {
        controlledByKeyboard = false;
        controllerInputReader.pollController();

        if (GameState.getInstance().getGameState() != GameStatusEnums.Paused && GameState.getInstance().getGameState() != GameStatusEnums.Dying) {
            if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT)) {
                moveLeftQuick(controllerInputReader.getxAxisValue());
            }

            if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT)) {
                moveRightQuick(controllerInputReader.getxAxisValue());
            }

            if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP)) {
                moveUpQuick(controllerInputReader.getyAxisValue());
            }

            if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN)) {
                moveDownQuick(controllerInputReader.getyAxisValue());
            }

            if (controllerInputReader.isInputActive(ControllerInputEnums.FIRE)) {
                startPrimaryFiring();
                isFiringPrimary = true;
            }
            if (controllerInputReader.isInputActive(ControllerInputEnums.SPECIAL_ATTACK)) {
                fireSpecialAttack();
            }
        }


        if (isFiringPrimary && !controllerInputReader.isInputActive(ControllerInputEnums.FIRE)) {
            haltPrimaryFiring();
            isFiringPrimary = false;
        }
    }

    private void moveLeftQuick(float multiplier) {
        directionx = -(Math.abs(multiplier) * this.getMovementSpeed());
    }

    private void haltMoveLeft() {
        directionx = 0;
    }


    private void moveRightQuick(float multiplier) {
        directionx = Math.abs(multiplier) * this.getMovementSpeed();
    }

    private void haltMoveRight() {
        directionx = 0;
    }


    private void moveUpQuick(float multiplier) {
        directiony = -(Math.abs(multiplier) * this.getMovementSpeed());
    }

    private void haltMoveUp() {
        directiony = 0;
    }


    private void moveDownQuick(float multiplier) {
        directiony = Math.abs(multiplier) * this.getMovementSpeed();
    }

    private void haltMoveDown() {
        directiony = 0;
    }

    public void addPlayerFollowingAnimation(SpriteAnimation spriteAnimation) {
        if (!this.playerFollowingAnimations.contains(spriteAnimation)) {
            playerFollowingAnimations.add(spriteAnimation);
        }
    }

    public boolean isImmune() {
        return isImmune;
    }

    public void setImmune(boolean immune) {
        isImmune = immune;
    }

    @Override
    public float getCurrentHitpoints() {
        return currentHitpoints;
    }

    @Override
    public float getMaxHitPoints() {
        return PlayerStats.getInstance().getMaxHitPoints();
    }

    public PrimaryPlayerGun getSpaceShipRegularGun() {
        return primaryPlayerGun;
    }

    public SpaceShipSpecialGun getSpaceShipSpecialGun() {
        return spaceShipSpecialGun;
    }

    public double getLastTimeCollisionDamageTaken() {
        return lastTimeCollisionDamageTaken;
    }

    public void setLastTimeCollisionDamageTaken(double lastTimeCollisionDamageTaken) {
        this.lastTimeCollisionDamageTaken = lastTimeCollisionDamageTaken;
    }

    public float getAttackSpeedModifier() {
        return attackSpeedModifier;
    }

    public void modifyAttackSpeedModifier(float attackSpeedModifier) {
        this.attackSpeedModifier += attackSpeedModifier;
    }

    public float getMovementSpeedModifier() {
        return movementSpeedModifier;
    }

    public void modifyMovementSpeedModifier(float movementSpeedModifier) {
        this.movementSpeedModifier += movementSpeedModifier;
    }

    public float getProtossConstructionSpeedModifier() {
        return protossConstructionSpeedModifier;
    }

    public void modifyProtossConstructionSpeedModifier(float protossConstructionSpeedModifier) {
        this.protossConstructionSpeedModifier += protossConstructionSpeedModifier;
    }

    public float getSpecialAttackRechargeCooldownBonusModifier() {
        return specialAttackRechargeCooldownBonusModifier;
    }

    public void modifySpecialAttackRechargeCooldownBonusModifier(float specialAttackRechargeCooldownBonusModifier) {
        this.specialAttackRechargeCooldownBonusModifier += specialAttackRechargeCooldownBonusModifier;
    }

    public float getThornsDamageModifier() {
        return thornsDamageModifier;
    }

    public void modifyThornsDamageModifier(float thornsDamageModifier) {
        this.thornsDamageModifier += thornsDamageModifier;
    }

    public float getIgniteDamageModifier() {
        return igniteDamageModifier;
    }

    public void modifyIgniteDamageModifier(float igniteDamageModifier) {
        this.igniteDamageModifier += igniteDamageModifier;
    }

    public float getDroneDamageModifier() {
        return this.droneDamageModifier;
    }

    public void modifyDroneDamageModifier(float droneDamageModifier) {
        this.droneDamageModifier += droneDamageModifier;
    }

    public float getMovementSpeed() {
        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)){
            return 2.5f * this.movementSpeedModifier; //todo voor de carrier move speed is dit een hele dirty hack
        }

        return PlayerStats.baseMoveSpeed * this.movementSpeedModifier;
    }

    public void modifyMaxShieldMultiplier(float modifier) {
        this.maxShieldModifier += modifier;
    }

    //deprecated
    public void modifyMaxOverloadingShieldMultiplier(float modifier) {
        this.maxOverloadedShieldModifier += modifier;
    }

    public int getMaxSpecialAttackCharges() {
        return maxSpecialAttackCharges;
    }

    public void setMaxSpecialAttackCharges(int maxSpecialAttackCharges) {
        this.maxSpecialAttackCharges = maxSpecialAttackCharges;
    }

    public void modifySpecialAttackRechargeCooldown(float v) {
        this.specialAttackRechargeCooldownModifier += v;
    }

    public float getSpecialAttackRechargeCooldownModifier() {
        return specialAttackRechargeCooldownModifier;
    }

    public float getSpecialAttackDamage() {
        return ((PlayerStats.getInstance().getBaseDamage() * this.specialAttackDamageModifier) * this.bonusDamageMultiplier);
    }

    public void modifySpecialAttackDamageModifier(float modifier) {
        this.specialAttackDamageModifier += modifier;
    }

    public void modifyFuelCannisterUsageMultiplier(float modifier) {
        this.fuelCannisterUsageModifier += modifier;
    }

    public float getFuelCannisterUsageModifier() {
        return fuelCannisterUsageModifier;
    }

    public void modifyFuelCannisterMaxCapacityModifier(float modifier) {
        this.fuelCannisterMaxCapacityModifier += modifier;
    }

    public float getFuelCannisterMaxCapacityModifier() {
        return fuelCannisterMaxCapacityModifier;
    }

    public void modifyFuelCannisterRegenMultiplier(float modifier) {
        this.fuelCannisterRegenModifier += modifier;
    }

    public float getFuelCannisterRegenModifier() {
        return fuelCannisterRegenModifier;
    }

    public void modifyDroneOrbitRadius(float modifier){
        this.droneOrbitRadius += modifier;
    }

    public float getDroneOrbitRadius(){
        return droneOrbitRadius;
    }

    public void setDroneType(DroneTypes droneTypes){
        this.droneTypes = droneTypes;
    }

    public DroneTypes getDroneType(){
        return droneTypes;
    }

    public void modifyShieldRegenModifier(float modifier){
        this.shieldRegenModifier += modifier;
    }

    public float getShieldRegenModifier(){
        return shieldRegenModifier;
    }

    public boolean isContinueShieldRegenThroughDamage() {
        return continueShieldRegenThroughDamage;
    }

    public void setContinueShieldRegenThroughDamage(boolean continueShieldRegenThroughDamage) {
        this.continueShieldRegenThroughDamage = continueShieldRegenThroughDamage;
    }

    public void modifyIgniteDurationModifier(float modifier) {
        this.igniteDurationModifier += modifier;
    }

    public float getIgniteDurationModifier(){
        return this.igniteDurationModifier;
    }

    public void modifyCritDamageModifier(float modifier){
        this.critDamageModifier += modifier;
    }

    public float getCritDamageModifier() {
        return this.critDamageModifier;
    }

    public float getProtossShipBuilderTimer() {
        return protossShipBuilderTimer;
    }

    public float getProtossShipBuildTime() {
        return protossShipBuildTime;
    }
    public int getShuttleCount() {
        return shuttleCount;
    }

    public void setShuttleCount(int shuttleCount) {
        this.shuttleCount = shuttleCount;
    }

    public int getArbiterCount() {
        return arbiterCount;
    }

    public void setArbiterCount(int arbiterCount) {
        this.arbiterCount = arbiterCount;
    }

    public int getCorsairCount() {
        return corsairCount;
    }

    public void setCorsairCount(int corsairCount) {
        this.corsairCount = corsairCount;
    }

    public int getScoutCount() {
        return scoutCount;
    }

    public void setScoutCount(int scoutCount) {
        this.scoutCount = scoutCount;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpaceShip spaceShip = (SpaceShip) o;
        return Objects.equals(controllerInputReader, spaceShip.controllerInputReader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), controllerInputReader);
    }
}