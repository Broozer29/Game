package game.objects.player.spaceship;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.UnsupportedAudioFileException;

import controllerInput.ControllerInputEnums;
import controllerInput.ControllerInputReader;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.objects.neutral.Explosion;
import game.objects.GameObject;
import game.objects.player.specialAttacks.SpecialAttack;
import game.objects.player.BoostsUpgradesAndBuffsSettings;
import game.objects.player.PlayerStats;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class SpaceShip extends GameObject {

    private int directionx;
    private int directiony;

    private float currentShieldRegenDelayFrame;
    private boolean controlledByKeyboard = true;
    private Set<Integer> pressedKeys = new HashSet<>();

    private SpriteAnimation deathAnimation = null;  //inherit from gameobject
    private SpriteAnimation exhaustAnimation = null;  //inherit from gameobject

    private PlayerStats playerStats = PlayerStats.getInstance();
    private BoostsUpgradesAndBuffsSettings powerUpEffects = BoostsUpgradesAndBuffsSettings.getInstance();

    private List<SpriteAnimation> playerFollowingAnimations = new ArrayList<SpriteAnimation>();  //inherit from gameobject?
    private List<Explosion> playerFollowingExplosions = new ArrayList<Explosion>();  //inherit from gameobject?
    private List<SpaceShipRegularGun> spaceShipGuns = new ArrayList<SpaceShipRegularGun>();
    private List<SpaceShipSpecialGun> spaceShipSpecialGuns = new ArrayList<SpaceShipSpecialGun>();
    private List<SpecialAttack> playerFollowingSpecialAttacks = new ArrayList<SpecialAttack>();

    public SpaceShip (SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
        playerStats = PlayerStats.getInstance();
        powerUpEffects = BoostsUpgradesAndBuffsSettings.getInstance();
        initShip();
    }

    // Called when managers need to be reset.
    public void resetSpaceship () {
        initShip();
    }

    //Used for going from level A to level B
    public void resetSpaceshipFollowingObjects () {
        playerFollowingSpecialAttacks = new ArrayList<SpecialAttack>();
        playerFollowingExplosions = new ArrayList<Explosion>();
        playerFollowingAnimations = new ArrayList<SpriteAnimation>();
        powerUpEffects.initDefaultSettings();
    }

    private void initShip () {
        directionx = 0;
        directiony = 0;
        pressedKeys = new HashSet<>();
        loadImage(playerStats.getSpaceShipImage());
        currentShieldRegenDelayFrame = 0;
        powerUpEffects.initDefaultSettings();
        spaceShipGuns = new ArrayList<SpaceShipRegularGun>();
        spaceShipSpecialGuns = new ArrayList<SpaceShipSpecialGun>();
        SpaceShipRegularGun gun = new SpaceShipRegularGun();
        this.spaceShipGuns.add(gun);
        SpaceShipSpecialGun specialGun = new SpaceShipSpecialGun();
        this.spaceShipSpecialGuns.add(specialGun);
        initExhaustAnimation(playerStats.getExhaustImage());
        initDeathAnimation(ImageEnums.Destroyed_Explosion);
        this.exhaustAnimation.setAnimationScale(0.3f);
        this.setObjectType("Player spaceship");
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
        deathAnimation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    public void addShieldDamageAnimation () {
        if (playerFollowingAnimations.size() < 10) {


            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(this.spriteConfiguration, 1, false);
            spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Default_Player_Shield_Damage);

            SpriteAnimation shieldAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            shieldAnimation.setOriginCoordinates(this.xCoordinate, this.yCoordinate);
            int yDist = 5;
            int xDist = 30;
            shieldAnimation.addXOffset(xDist);
            shieldAnimation.addYOffset(yDist);
            playerFollowingAnimations.add(shieldAnimation);

            AnimationManager.getInstance().addUpperAnimation(shieldAnimation);
        }

    }

    //Might not be needed
//    private void removeInvisibleAnimations () {
//        for (int i = 0; i < playerFollowingAnimations.size(); i++) {
//            if (!playerFollowingAnimations.get(i).isVisible()) {
//                playerFollowingAnimations.remove(i);
//            }
//        }
//        for (int i = 0; i < playerFollowingExplosions.size(); i++) {
//            if (!playerFollowingExplosions.get(i).getAnimation().isVisible()) {
//                playerFollowingExplosions.remove(i);
//            }
//        }
//
//        for (int i = 0; i < playerFollowingSpecialAttacks.size(); i++) {
//            if (!playerFollowingSpecialAttacks.get(i).getAnimation().isVisible()) {
//                playerFollowingSpecialAttacks.remove(i);
//            }
//        }
//    }

    public void takeDamage (float damage) {

        System.out.println("Damage taken/healed: " + damage);
        if (damage > 0) {
            lastGameSecondDamageTaken = GameStateInfo.getInstance().getGameSeconds();
            this.currentShieldRegenDelayFrame = 0;
            float shieldPiercingDamage = playerStats.getShieldHitpoints() - damage;

            if (shieldPiercingDamage < 0) {
                playerStats.changeHitPoints(shieldPiercingDamage);
                playerStats.setShieldHitpoints(0);
            } else {
                playerStats.changeShieldHitpoints(-damage);
                addShieldDamageAnimation();
            }
        }

        if (damage < 0) {
            playerStats.changeHitPoints(damage);
        }

    }

    public void updateGameTick () {
        this.currentShieldRegenDelayFrame++;

        for (SpaceShipRegularGun gun : spaceShipGuns) {
            gun.updateFrameCount();
        }

        for (SpaceShipSpecialGun specialGun : spaceShipSpecialGuns) {
            specialGun.updateFrameCount();
        }

        movePlayerAnimations();
        moveExplosions();
        moveSpecialAttacks();
//        removeInvisibleAnimations();

        if (currentShieldRegenDelayFrame >= playerStats.getShieldRegenDelay()) {
            if (playerStats.getShieldHitpoints() < playerStats.getMaxShieldHitPoints()) {
                repairShields((float) 0.4);
                repairHealth((float) 0.4);
            }
        }
    }

    private void movePlayerAnimations () {
        for (SpriteAnimation anim : playerFollowingAnimations) {
            anim.setOriginCoordinates(xCoordinate, yCoordinate);
        }
    }

    private void moveExplosions () {
        for (Explosion explosion : playerFollowingExplosions) {
            explosion.setX(this.getXCoordinate());
            explosion.setY(this.getYCoordinate());
            explosion.getAnimation().setX(this.getXCoordinate());
            explosion.getAnimation().setY(this.getYCoordinate());
            explosion.updateBoardBlock();
            explosion.getAnimation().setAnimationBounds(explosion.getAnimation().getXCoordinate(), explosion.getAnimation().getYCoordinate());
        }
    }

    private void moveSpecialAttacks () {
        for (SpecialAttack specialAttack : playerFollowingSpecialAttacks) {
            if (specialAttack.centeredAroundPlayer()) {
                specialAttack.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
                specialAttack.getAnimation().setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
            } else {
                specialAttack.setX(this.getXCoordinate());
                specialAttack.setY(this.getYCoordinate());
                specialAttack.getAnimation().setX(this.getXCoordinate());
                specialAttack.getAnimation().setY(this.getYCoordinate());
            }
            specialAttack.updateBoardBlock();
            specialAttack.getAnimation().setAnimationBounds(specialAttack.getAnimation().getXCoordinate(), specialAttack.getAnimation().getYCoordinate());
//			specialAttack.getAnimation().updateCurrentBoardBlock();
        }
    }

    // Moves the spaceship, constantly called
    public void move () {
        xCoordinate += directionx;
        yCoordinate += directiony;

        if (!controlledByKeyboard) {
            haltMoveDown();
            haltMoveLeft();
            haltMoveRight();
            haltMoveUp();
        }

        playerStats.setHomingRectangleYCoordinate(
                (int) (yCoordinate - (height * playerStats.getHomingRectangleResizeScale())));
        playerStats.setHomingRectangleXCoordinate(
                (int) (xCoordinate - (width * playerStats.getHomingRectangleResizeScale())));
        playerStats.setHomingRectangleWidth((int) (width * (playerStats.getHomingRectangleResizeScale() * 2)));
        playerStats.setHomingRectangleHeight((int) (height * (playerStats.getHomingRectangleResizeScale() * 2.25)));

        bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);

        if (this.exhaustAnimation != null) {
            this.exhaustAnimation.setX(this.getCenterXCoordinate() - ((this.getWidth() + 5)));
            this.exhaustAnimation.setY(this.getCenterYCoordinate() - (this.exhaustAnimation.getHeight() / 2) + 5);
        }

        if (this.deathAnimation != null) {
            deathAnimation.setCenterCoordinates(this.xCoordinate, this.yCoordinate);
        }

        updateBoardBlock();
    }

    // Launch a missile from the center point of the spaceship
    private void fire () throws UnsupportedAudioFileException, IOException {
        for (SpaceShipRegularGun gun : spaceShipGuns) {
            gun.fire(this.xCoordinate + this.width, this.yCoordinate + (this.height / 2),
                    playerStats.getAttackType());
        }
    }

    private void fireSpecialAttack () throws UnsupportedAudioFileException, IOException {
        for (SpaceShipSpecialGun gun : spaceShipSpecialGuns) {
            gun.fire(this.xCoordinate - (this.width / 2), this.yCoordinate - (this.height / 2), this.getWidth(), this.getHeight(),
                    playerStats.getPlayerSpecialAttackType());
        }
    }

    public void repairHealth (float healAmount) {
        playerStats.changeHitPoints(healAmount);
    }

    public void repairShields (float healAmount) {
        playerStats.changeShieldHitpoints(healAmount);
    }

    public List<SpaceShipSpecialGun> getSpecialGuns () {
        return this.spaceShipSpecialGuns;
    }

    public void addFollowingSpecialAttack (SpecialAttack specialAttack) {
        this.playerFollowingSpecialAttacks.add(specialAttack);
        AnimationManager.getInstance().addUpperAnimation(specialAttack.getAnimation());
    }

    public SpriteAnimation getExhaustAnimation () {
        return this.exhaustAnimation;
    }

    public SpriteAnimation getDeathAnimation () {
        return this.deathAnimation;
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
                        moveLeftQuick();
                        break;
                    case (KeyEvent.VK_D):
                    case (KeyEvent.VK_RIGHT):
                        moveRightQuick();
                        break;
                    case (KeyEvent.VK_W):
                    case (KeyEvent.VK_UP):
                        moveUpQuick();
                        break;
                    case (KeyEvent.VK_S):
                    case (KeyEvent.VK_DOWN):
                        moveDownQuick();
                        break;
                    case (KeyEvent.VK_Q):
                    case (KeyEvent.VK_ENTER):
                        try {
                            fireSpecialAttack();
                        } catch (UnsupportedAudioFileException | IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case (KeyEvent.VK_SHIFT):
                    case (KeyEvent.VK_E):
                        takeDamage(500);
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
        if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_SLOW)) {
            moveLeftSlow();
        } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_QUICK)) {
            moveLeftQuick();
        }

        if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_SLOW)) {
            moveRightSlow();
        } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_QUICK)) {
            moveRightQuick();
        }

        if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_QUICK)) {
            moveUpQuick();
        } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_SLOW)) {
            moveUpSlow();
        }

        if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_SLOW)) {
            moveDownSlow();
        } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_QUICK)) {
            moveDownQuick();
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

    private void moveLeftSlow () {
        directionx = -Math.round(playerStats.getCurrentMovementSpeed() / 2);
    }

    private void moveLeftQuick () {
        directionx = -playerStats.getCurrentMovementSpeed();
    }

    private void haltMoveLeft () {
        directionx = 0;
    }

    private void moveRightSlow () {
        directionx = Math.round(playerStats.getCurrentMovementSpeed() / 2);
    }

    private void moveRightQuick () {
        directionx = playerStats.getCurrentMovementSpeed();
    }

    private void haltMoveRight () {
        directionx = 0;
    }

    private void moveUpSlow () {
        directiony = -Math.round(playerStats.getCurrentMovementSpeed() / 2);
    }

    private void moveUpQuick () {
        directiony = -playerStats.getCurrentMovementSpeed();
    }

    private void haltMoveUp () {
        directiony = 0;
    }

    private void moveDownSlow () {
        directiony = Math.round(playerStats.getCurrentMovementSpeed() / 2);
    }

    private void moveDownQuick () {
        directiony = playerStats.getCurrentMovementSpeed();
    }

    private void haltMoveDown () {
        directiony = 0;
    }

}