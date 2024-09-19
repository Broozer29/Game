package game.gameobjects.player.spaceship;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.UnsupportedAudioFileException;

import controllerInput.ControllerInputEnums;
import controllerInput.ControllerInputReader;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatsTracker;
import game.items.Item;
import game.items.PlayerInventory;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.managers.AnimationManager;
import game.movement.Point;
import game.gameobjects.friendlies.FriendlyManager;
import game.gameobjects.GameObject;
import game.gameobjects.missiles.specialAttacks.SpecialAttack;
import game.gameobjects.player.PlayerStats;
import VisualAndAudioData.image.ImageEnums;
import game.util.OrbitingObjectsFormatter;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class SpaceShip extends GameObject {

    private float directionx;
    private float directiony;

    private float accumulatedXCoordinate;
    private float accumulatedYCoordinate;

    private float currentShieldRegenDelayFrame;
    private boolean controlledByKeyboard = true;
    private Set<Integer> pressedKeys = new HashSet<>();

    private SpriteAnimation exhaustAnimation = null;  //inherit from gameobject

    private PlayerStats playerStats = PlayerStats.getInstance();

    private List<SpriteAnimation> playerFollowingAnimations = new ArrayList<SpriteAnimation>();  //inherit from gameobject?
    private SpaceShipRegularGun spaceShipRegularGun = null;
    private SpaceShipSpecialGun spaceShipSpecialGun = null;
    private List<SpecialAttack> playerFollowingSpecialAttacks = new ArrayList<SpecialAttack>();

    public SpaceShip (SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration, null);
        playerStats = PlayerStats.getInstance();
        initShip();
    }

    // Called when managers need to be reset.
    public void resetSpaceship () {
        initShip();
    }

    private void initShip () {
        directionx = 0;
        directiony = 0;
        this.currentShieldPoints = playerStats.getMaxShieldHitPoints();
        this.currentHitpoints = playerStats.getMaxHitPoints();
        this.playerFollowingAnimations.clear();
        this.playerFollowingSpecialAttacks.clear();
        this.accumulatedYCoordinate = this.yCoordinate;
        this.accumulatedXCoordinate = this.xCoordinate;
        pressedKeys = new HashSet<>();
        loadImage(playerStats.getSpaceShipImage());
        currentShieldRegenDelayFrame = 0;
        this.spaceShipRegularGun = new SpaceShipRegularGun();
        this.spaceShipSpecialGun = new SpaceShipSpecialGun();
        initExhaustAnimation(playerStats.getExhaustImage());
        initDeathAnimation(ImageEnums.Destroyed_Explosion);
        this.exhaustAnimation.setAnimationScale(0.3f);
        this.setObjectType("Player spaceship");
        this.effects = new CopyOnWriteArrayList<>();
        applyOnCreationEffects();
    }

    private void applyOnCreationEffects () {
        for (Item item : PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.ApplyOnCreation)) {
            item.applyEffectToObject(this);
        }


        if (PlayerInventory.getInstance().getItemByName(ItemEnums.FocusCrystal) != null) {
            SpriteConfiguration focusCrystalConfig = new SpriteConfiguration();
            focusCrystalConfig.setxCoordinate(getXCoordinate());
            focusCrystalConfig.setyCoordinate(getYCoordinate());
            focusCrystalConfig.setScale(1);
            focusCrystalConfig.setTransparancyAlpha(0.1f);
            focusCrystalConfig.setImageType(ImageEnums.Highlight);

            SpriteAnimationConfiguration focusCrystalAnimConfig = new SpriteAnimationConfiguration(focusCrystalConfig, 10, true);
            SpriteAnimation focusCrystalAnimation = new SpriteAnimation(focusCrystalAnimConfig);

//            FocusCrystal focusCrystal = (FocusCrystal) PlayerInventory.getInstance().getItemByName(ItemEnums.FocusCrystal);
//            focusCrystalAnimation.setImageDimensions(focusCrystal.getDistance() * 2, focusCrystal.getDistance() * 2);
            focusCrystalAnimation.setAnimationScale(5.714f);


            addPlayerFollowingAnimation(focusCrystalAnimation);
            AnimationManager.getInstance().addUpperAnimation(focusCrystalAnimation);
        }
    }

    private boolean firedPostCreationEffects = false;

    private void postCreationActivities () {
        //This method exists because some managers or methods REQUIRE the spaceship to have finished initializing
        if (!firedPostCreationEffects) {
            for (int i = 0; i < playerStats.getAmountOfDrones(); i++) {
                FriendlyManager.getInstance().addDrone();
            }
            OrbitingObjectsFormatter.reformatOrbitingObjects(this, 85);
            firedPostCreationEffects = true;
        }
    }


    private void initExhaustAnimation (ImageEnums imageType) {
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, true);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(imageType);
        exhaustAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        playerStats.setCurrentExhaust(imageType);
        AnimationManager.getInstance().addLowerAnimation(exhaustAnimation);
    }

    private void initDeathAnimation (ImageEnums imageType) {
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(imageType);
        destructionAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        destructionAnimation.setAnimationScale(2f);
    }

    public void addShieldDamageAnimation () {
        long shieldAnimationCount = playerFollowingAnimations.stream()
                .filter(spriteAnimation -> spriteAnimation.getImageType().equals(ImageEnums.Default_Player_Shield_Damage)
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

    public void takeDamage (float damageTaken) {
        // Armor calculation should only apply if damage is being dealt, not for healing
        if (damageTaken > 0) {
            lastGameSecondDamageTaken = GameStateInfo.getInstance().getGameSeconds();
            this.currentShieldRegenDelayFrame = 0;

            GameStatsTracker.getInstance().addDamageTaken(damageTaken);

            // Check if the damage pierces the shield
            float shieldPiercingDamage = currentShieldPoints - damageTaken;
            if (shieldPiercingDamage < 0) {
                // Apply the damage that pierced through the shield to hit points
                changeHitPoints(shieldPiercingDamage);
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
            changeHitPoints(-damageTaken);
        }
    }

    public void changeHitPoints (float change) {
        this.currentHitpoints += change;
        float maxHitPoints = playerStats.getMaxHitPoints();
        if (this.currentHitpoints > maxHitPoints) {
            this.currentHitpoints = maxHitPoints;
        }
    }

    public void changeShieldHitpoints (float change) {
        this.currentShieldPoints += change;
        float maxShieldPoints = playerStats.getMaxShieldHitPoints();
        float maxOverloadingShieldMultiplier = playerStats.getMaxOverloadingShieldMultiplier();
        if (currentShieldPoints > (maxShieldPoints * maxOverloadingShieldMultiplier)) {
            currentShieldPoints = maxShieldPoints * maxOverloadingShieldMultiplier;
        }
    }

    private void reduceOverloadedShieldPoints () {
        float maxShieldPoints = playerStats.getMaxShieldHitPoints();
        float maxShieldMultiplier = playerStats.getMaxShieldMultiplier();

        if (currentShieldPoints > maxShieldPoints * maxShieldMultiplier) {
            currentShieldPoints -= playerStats.getOverloadedShieldDiminishAmount();
        }
    }

    public void updateGameTick () {
        this.currentShieldRegenDelayFrame++;
        postCreationActivities();
        spaceShipSpecialGun.updateFrameCount();

        movePlayerAnimations();
        moveSpecialAttacks();
        removeInvisibleAnimations();
        updateGameObjectEffects();
        reduceOverloadedShieldPoints();

        if (currentShieldRegenDelayFrame >= playerStats.getShieldRegenDelay()) {
            if (currentShieldPoints < playerStats.getMaxShieldHitPoints()) {
                repairShields((float) 0.4);
//                repairHealth((float) 0.4);
            }
        }
    }


    private void movePlayerAnimations () {
        for (SpriteAnimation anim : playerFollowingAnimations) {
            anim.setOriginCoordinates(getCenterXCoordinate(), getCenterYCoordinate());
//            anim.setCenterCoordinates(gsetCenterXCoordinate(), getCenterYCoordinate());
        }
    }

    private void removeInvisibleAnimations () {
        Iterator<SpriteAnimation> iterator = playerFollowingAnimations.iterator();
        while (iterator.hasNext()) {
            SpriteAnimation animation = iterator.next();
            if (!animation.isVisible()) {
                iterator.remove();
            }
        }

    }


    private void moveSpecialAttacks () {
        for (SpecialAttack specialAttack : playerFollowingSpecialAttacks) {
            if (specialAttack.centeredAroundPlayer()) {
                specialAttack.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
//                specialAttack.getAnimation().setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
            }
            specialAttack.updateBoardBlock();
            specialAttack.getAnimation().setAnimationBounds(specialAttack.getAnimation().getXCoordinate(), specialAttack.getAnimation().getYCoordinate());
        }
    }

    // Moves the spaceship, constantly called
    public void move () {
        accumulatedXCoordinate += directionx;
        accumulatedYCoordinate += directiony;

        // Convert the accumulated float coordinates to integer coordinates
        xCoordinate = Math.round(accumulatedXCoordinate);
        yCoordinate = Math.round(accumulatedYCoordinate);

        if (!controlledByKeyboard) {
            haltMoveDown();
            haltMoveLeft();
            haltMoveRight();
            haltMoveUp();
        }

        bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);

        this.currentLocation = new Point(this.xCoordinate, this.yCoordinate);
        if (this.exhaustAnimation != null) {
            this.exhaustAnimation.setXCoordinate(this.xCoordinate - (exhaustAnimation.getWidth() / 2));
            this.exhaustAnimation.setYCoordinate(this.getCenterYCoordinate() - (this.exhaustAnimation.getHeight() / 2) + 3);
        }

        if (this.destructionAnimation != null) {
            destructionAnimation.setCenterCoordinates(this.xCoordinate, this.yCoordinate);
        }

        updateBoardBlock();
    }

    // Launch a missile from the center point of the spaceship
    private void fire () throws UnsupportedAudioFileException, IOException {
        spaceShipRegularGun.fire(this.xCoordinate + this.width, this.getCenterYCoordinate(),
                playerStats.getAttackType());
    }

    private void fireSpecialAttack () throws UnsupportedAudioFileException, IOException {
        spaceShipSpecialGun.fire(this.getCenterXCoordinate(), this.getCenterYCoordinate(),
                playerStats.getPlayerSpecialAttackType());
    }

    public void repairHealth (float healAmount) {
        changeHitPoints(healAmount);
    }

    public void repairShields (float healAmount) {
        changeShieldHitpoints(healAmount);
    }

    public SpaceShipSpecialGun getSpecialGun () {
        return this.spaceShipSpecialGun;
    }

    public void addFollowingSpecialAttack (SpecialAttack specialAttack) {
        this.playerFollowingSpecialAttacks.add(specialAttack);
        AnimationManager.getInstance().addUpperAnimation(specialAttack.getAnimation());
    }

    public SpriteAnimation getExhaustAnimation () {
        return this.exhaustAnimation;
    }


    public synchronized void keyPressed (KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (!pressedKeys.isEmpty()) {
            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext(); ) {
                switch (it.next()) {
                    case (KeyEvent.VK_SPACE):
                        try {
                            fire();
                        } catch (UnsupportedAudioFileException | IOException e1) {
                            e1.printStackTrace();
                        }
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
                        try {
                            fireSpecialAttack();
                        } catch (UnsupportedAudioFileException | IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
//                    case (KeyEvent.VK_SHIFT):
                    case (KeyEvent.VK_E):
                        break;
                }
            }
        }
    }

    public synchronized void keyReleased (KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_Q) {
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
        if (key == KeyEvent.VK_SHIFT || key == KeyEvent.VK_E) {
        }
    }

    // Called by GameBoard every loop if a controller is connected
    public void update (ControllerInputReader controllerInputReader) {
        controlledByKeyboard = false;
        controllerInputReader.pollController();
        if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_QUICK)) {
            moveLeftQuick(controllerInputReader.getxAxisValue());
        }

        if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_QUICK)) {
            moveRightQuick(controllerInputReader.getxAxisValue());
        }

        if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_QUICK)) {
            moveUpQuick(controllerInputReader.getyAxisValue());
        }

        if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_QUICK)) {
            moveDownQuick(controllerInputReader.getyAxisValue());
        }

        if (controllerInputReader.isInputActive(ControllerInputEnums.FIRE)) {
            try {
                fire();
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }
        if (controllerInputReader.isInputActive(ControllerInputEnums.SPECIAL_ATTACK)) {
            try {
                fireSpecialAttack();
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveLeftQuick (float multiplier) {
        directionx = -(Math.abs(multiplier) * playerStats.getCurrentMovementSpeed());
    }

    private void haltMoveLeft () {
        directionx = 0;
    }


    private void moveRightQuick (float multiplier) {
        directionx = Math.abs(multiplier) * playerStats.getCurrentMovementSpeed();
    }

    private void haltMoveRight () {
        directionx = 0;
    }


    private void moveUpQuick (float multiplier) {
        directiony = -(Math.abs(multiplier) * playerStats.getCurrentMovementSpeed());
    }

    private void haltMoveUp () {
        directiony = 0;
    }


    private void moveDownQuick (float multiplier) {
        directiony = Math.abs(multiplier) * playerStats.getCurrentMovementSpeed();
    }

    private void haltMoveDown () {
        directiony = 0;
    }

    public void addPlayerFollowingAnimation (SpriteAnimation spriteAnimation) {
        if (!this.playerFollowingAnimations.contains(spriteAnimation)) {
            playerFollowingAnimations.add(spriteAnimation);
        }
    }

}