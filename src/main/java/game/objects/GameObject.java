package game.objects;

import game.gamestate.GameStateInfo;
import game.items.PlayerInventory;
import game.items.effects.EffectActivationTypes;
import game.managers.AnimationManager;
import game.movement.*;
import game.movement.pathfinders.PathFinder;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.image.ImageResizer;
import VisualAndAudioData.image.ImageRotator;
import game.items.effects.EffectInterface;
import game.objects.player.PlayerStats;
import game.spawner.LevelManager;
import game.util.ArmorCalculator;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.Sprite;
import visualobjects.SpriteAnimation;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameObject extends Sprite {

    protected SpriteAnimation animation; //This is a code smell, because spriteanimation is already extension of sprite, need to untangle this
    protected SpriteAnimation destructionAnimation;
    protected SpriteAnimation shieldDamagedAnimation;
    protected SpriteAnimation exhaustAnimation;
    protected SpriteAnimation chargingUpAttackAnimation;
    protected boolean showHealthBar;
    protected boolean allowedToMove;

    protected float originalScale;

    //Audio variables
    protected AudioEnums deathSound;

    //Hitpoint variables
    protected float currentHitpoints;
    protected float maxHitPoints;
    protected float currentShieldPoints;
    protected float maxShieldPoints;
    protected float baseArmor;
    protected float armorBonus;


    //Damage variables
    protected boolean hasAttack;
    protected float damage;
    protected boolean allowedToDealDamage; //Set to false for explosions that hit a target
    protected int attackSpeed;
    protected int attackSpeedCurrentFrameCount;

    //Game logic variables
    protected boolean friendly;


    //Movement variables
    protected GameObject objectToCenterAround;
    protected GameObject objectToFollow;
    protected MovementConfiguration movementConfiguration;

    protected MovementTracker movementTracker;

    protected int lastBoardBlock;
    protected Point currentLocation;
    protected int currentBoardBlock;


    //Objects following this object
    protected List<GameObject> objectsFollowingThis = new ArrayList<GameObject>();

    protected List<GameObject> objectOrbitingThis = new ArrayList<GameObject>();

    //Other
    protected String objectType;
    protected Direction movementDirection;
    protected boolean boxCollision;
    protected double lastGameSecondDamageTaken;
    protected GameObject ownerOrCreator;

    private int movementCounter = 0;
    protected float cashMoneyWorth;
    protected double rotationAngle;
    protected boolean allowedVisualsToRotate;

    protected CopyOnWriteArrayList<EffectInterface> effects = new CopyOnWriteArrayList<>();
    protected List<SpriteAnimation> effectAnimations = new ArrayList<>();

    protected float xpOnDeath = 0;

    public GameObject (SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
        initGameObject();
        if (spriteConfiguration.getImageType() != null) {
            this.loadImage(spriteConfiguration.getImageType());
        }
    }

    public GameObject (SpriteAnimationConfiguration spriteAnimationConfiguration) {
        super(spriteAnimationConfiguration.getSpriteConfiguration());
        this.animation = new SpriteAnimation(spriteAnimationConfiguration);
        initGameObject();
    }

    private void initGameObject () {
        this.currentLocation = new Point(xCoordinate, yCoordinate);
        this.visible = true;
        this.movementTracker = new MovementTracker();
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        this.allowedToMove = true;
        this.allowedVisualsToRotate = true;
    }


    //*****************EFFECTS*******************************
    public void addEffect (EffectInterface effect) {
        if (visible && currentHitpoints > 0) {
            boolean effectFound = false;

            // Iterate over the effects to check if an instance of the same type exists
            for (EffectInterface existingEffect : effects) {
                if (existingEffect.getClass().equals(effect.getClass())) {
                    effectFound = true;
                    break; // Exit the loop as we found an instance of the effect
                }
            }

            if (!effectFound) {
                effects.add(effect);
            } else {
                refreshEffect(effect);
            }

            if (effect.getAnimation() != null) {
                boolean effectAnimationExists = false;
                for (SpriteAnimation effectAnim : effectAnimations) {
                    if (effectAnim.getImageType().equals(effect.getAnimation().getImageType())) {
                        effectAnimationExists = true;
                    }
                }

                if (!effectAnimationExists) {
                    effectAnimations.add(effect.getAnimation());
                    AnimationManager.getInstance().addUpperAnimation(effect.getAnimation());
                }
            }
        }
    }

    private void activateOnDeathEffects () {
        activateEffects(EffectActivationTypes.DormentExplosion);
        activateEffects(EffectActivationTypes.OnDeath);
        cleanseAllEffects();
    }

    public void updateGameObjectEffects () {
        activateEffects(EffectActivationTypes.DamageOverTime);
        activateEffects(EffectActivationTypes.HealthRegeneration);
        activateEffects(EffectActivationTypes.OutOfCombatArmorBonus);
        activateEffects(EffectActivationTypes.DormentExplosion);
    }


    private void activateEffects (EffectActivationTypes providedActivationType) {
        List<EffectInterface> toRemove = new ArrayList<>();
        for (EffectInterface effect : effects) {
            if (effect.getEffectTypesEnums() == providedActivationType) {
                effect.activateEffect(this);
                if (effect.shouldBeRemoved()) {
                    if (effect.getAnimation() != null) {
                        effect.getAnimation().setVisible(false);
                        if (effectAnimations.contains(effect.getAnimation())) {
                            effectAnimations.remove(effect.getAnimation());
                        }
                    }
                    toRemove.add(effect);
                }
            }
        }

        effects.removeAll(toRemove);
    }

    private void refreshEffect (EffectInterface effect) {
        if (effect.getEffectTypesEnums().equals(EffectActivationTypes.DamageOverTime)) {
            effect.resetDuration();
            effect.increaseEffectStrength();
        }
    }

    private void cleanseAllEffects () {
        //Does NOT set all effects to invisible, so might lead to problems later on
        //For example, effects that trigger after or during the objects deletion
        effects.clear();
    }

    //*****************DELETION/DAMAGE*******************************

    public void deleteObject () {
        if (this.animation != null) {
            this.animation.setVisible(false);
        }

        for (SpriteAnimation spriteAnimation : effectAnimations) {
            spriteAnimation.setVisible(false);
        }

        if (this.exhaustAnimation != null) {
            this.exhaustAnimation.setVisible(false);
        }

        if (this.shieldDamagedAnimation != null) {
            this.shieldDamagedAnimation.setVisible(false);
        }

        if (this.destructionAnimation != null &&
                this.currentHitpoints > 1) {
            this.destructionAnimation.setVisible(false);
        }

        if (this.chargingUpAttackAnimation != null) {
            this.chargingUpAttackAnimation.setVisible(false);
        }

        for (GameObject object : objectsFollowingThis) {
            object.deleteObject();
        }

        for (GameObject object : objectOrbitingThis) {
            object.deleteObject();
        }

        this.objectToFollow = null;
        this.movementConfiguration.deleteConfiguration();
        this.movementConfiguration = null; //could be dangerous and break shit
        this.movementTracker = null;
        this.ownerOrCreator = null;

        this.visible = false;
    }

    public void takeDamage (float damageTaken) {
        if (damageTaken > 0) {
            damageTaken = ArmorCalculator.calculateDamage(damageTaken, this);
        }
        this.currentHitpoints -= damageTaken;
        if (damageTaken > 0) {
            lastGameSecondDamageTaken = GameStateInfo.getInstance().getGameSeconds();
        }

        if (currentHitpoints > maxHitPoints) {
            currentHitpoints = maxHitPoints;
        }

        if (this.currentHitpoints <= 0) {
            if (this.xpOnDeath > 0) {
                PlayerStats.getInstance().addXP(xpOnDeath);
            }
            if (this.destructionAnimation != null) {
                this.destructionAnimation.setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
                AnimationManager.getInstance().addUpperAnimation(destructionAnimation);
            }

            for (GameObject object : objectsFollowingThis) {
                object.setCashMoneyWorth(0);
                object.takeDamage(9999999);
            }

            try {
                AudioManager.getInstance().addAudio(deathSound);
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
            LevelManager.getInstance().setEnemiesKilled(1);
            this.setVisible(false);
            activateOnDeathEffects();
            PlayerInventory.getInstance().gainCashMoney(this.cashMoneyWorth);
        }
    }


    //*****************MOVEMENT*******************************
    public void move () {
        SpriteMover.getInstance().moveSprite(this, movementConfiguration);
        moveAnimations();
        this.bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
        updateBoardBlock();
        updateVisibility();

        if (animation != null) {
            animation.setAnimationBounds(xCoordinate, yCoordinate);
        }
    }

    private void moveAnimations () {
        if (this.animation != null) {
            moveAnimations(animation);
        }

        if (this.destructionAnimation != null) {
            moveAnimations(destructionAnimation);
        }

        for (SpriteAnimation spriteAnimation : effectAnimations) {
            moveAnimationsToCenter(spriteAnimation);
        }

        updateChargingAttackAnimationCoordination();
    }

    private void moveAnimations (SpriteAnimation animation) {
        animation.setX(this.getXCoordinate());
        animation.setY(this.getYCoordinate());
        animation.setAnimationBounds(animation.getXCoordinate(), animation.getYCoordinate());
    }

    private void moveAnimationsToCenter (SpriteAnimation animation) {
        animation.setCenterCoordinates(this.getCenterXCoordinate(), this.getYCoordinate());
    }


    private void updateVisibility () {
        if (SpriteRemover.getInstance().shouldRemoveVisibility(this)) {
            this.visible = false;
        }

        if (!this.visible && this.animation != null) {
            this.animation.setVisible(false);
        }
    }


    //*****************VISUAL ALTERATION*******************************
    public void rotateGameObjectTowards (Direction direction) {
        if (this.animation != null) {
            rotateGameObjectSpriteAnimations(animation, direction);
        }
        if (this.exhaustAnimation != null) {
            rotateGameObjectSpriteAnimations(animation, direction);
        }

        if (this.destructionAnimation != null) {
            rotateGameObjectSpriteAnimations(animation, direction);
        }
        if (this.image != null) {
            this.image = ImageRotator.getInstance().rotate(image, direction);
        }
    }

    private void updateChargingAttackAnimationCoordination () {
        if (this.chargingUpAttackAnimation != null) {
            double baseDistance = (this.getWidth() + this.getHeight()) / 2.0;
            Point chargingUpLocation = ImageRotator.getInstance().calculateFrontPosition(this.getCenterXCoordinate(), this.getCenterYCoordinate(), rotationAngle, baseDistance);
            this.chargingUpAttackAnimation.setCenterCoordinates(chargingUpLocation.getX(), chargingUpLocation.getY());
        }
    }

    public void rotateGameObjectTowards (int targetXCoordinate, int targetYCoordinate) {
        double calculatedAngle = ImageRotator.getInstance().calculateAngle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetXCoordinate, targetYCoordinate);
        if (this.rotationAngle != calculatedAngle) {
            if (this.image != null) {
                this.image = ImageRotator.getInstance().rotateOrFlip(this.originalImage, calculatedAngle);
            }

            if (this.animation != null) {
                this.animation.rotateAnimation(calculatedAngle);
            }

            this.rotationAngle = calculatedAngle;

            updateChargingAttackAnimationCoordination();

            //Exhausts are better added to the sprites themselves by doing some GIMP photoshop and making the enemies an animation
//            if (this.exhaustAnimation != null) {
//                double baseDistance = (this.getWidth() + this.getHeight()) / 2.0;
//                Point exhaustLocation = ImageRotator.getInstance().calculateFrontPosition(this.getCenterXCoordinate(), this.getCenterYCoordinate(), calculatedAngle, baseDistance);
//                this.exhaustAnimation.setCenterCoordinates(exhaustLocation.getX(), exhaustLocation.getY());
//                Point exhaustLocation = ExhaustAnimationRotator.calculateExhaustPosition(this, this.rotationAngle);
//                this.exhaustAnimation.setX(exhaustLocation.getX());
//                this.exhaustAnimation.setY(exhaustLocation.getY());
//                double exhaustAngle = ImageRotator.getInstance().calculateAngle(exhaustAnimation.getCenterXCoordinate(), exhaustAnimation.getCenterYCoordinate(), targetXCoordinate, targetYCoordinate);
//                this.exhaustAnimation.rotateAnimation(exhaustAngle);
//            }
        }
    }

    private void rotateGameObjectSpriteAnimations (SpriteAnimation sprite, Direction direction) {
        if (sprite != null) {
            sprite.rotateAnimation(direction);
        }
    }

    //*****************SPECIFIC ENEMY BEHAVIOURS*******************************
    public void onCreationEffects () {
        //Exist to be overriden
    }

    public void onDeathEffects () {
        //exist to be overriden
    }


    //-------------------------------------getters and setters below------------

    public SpriteAnimation getDestructionAnimation () {
        return destructionAnimation;
    }

    public void setDestructionAnimation (SpriteAnimation destructionAnimation) {
        this.destructionAnimation = destructionAnimation;
    }

    public Point getCurrentLocation () {
        if (this.movementConfiguration == null) {
            return new Point(this.xCoordinate, this.yCoordinate);
        }
        return this.movementConfiguration.getCurrentLocation();
    }

    public int getCurrentBoardBlock () {
        return this.currentBoardBlock;
    }

    public void updateCurrentLocation (Point newLocation) {
        this.movementConfiguration.setCurrentLocation(newLocation);
    }

    public SpriteAnimation getShieldDamagedAnimation () {
        return shieldDamagedAnimation;
    }

    public void setShieldDamagedAnimation (SpriteAnimation shieldDamagedAnimation) {
        this.shieldDamagedAnimation = shieldDamagedAnimation;
    }

    public SpriteAnimation getExhaustAnimation () {
        return exhaustAnimation;
    }

    public void setExhaustAnimation (SpriteAnimation exhaustAnimation) {
        this.exhaustAnimation = exhaustAnimation;
    }

    public boolean isShowHealthBar () {
        return showHealthBar;
    }

    public void setShowHealthBar (boolean showHealthBar) {
        this.showHealthBar = showHealthBar;
    }

    public AudioEnums getDeathSound () {
        return deathSound;
    }

    public void setDeathSound (AudioEnums deathSound) {
        this.deathSound = deathSound;
    }

    public float getCurrentHitpoints () {
        return currentHitpoints;
    }

    public void setCurrentHitpoints (float currentHitpoints) {
        this.currentHitpoints = currentHitpoints;
    }

    public float getMaxHitPoints () {
        return maxHitPoints;
    }

    public void setMaxHitPoints (float maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public float getCurrentShieldPoints () {
        return currentShieldPoints;
    }

    public void setCurrentShieldPoints (float currentShieldPoints) {
        this.currentShieldPoints = currentShieldPoints;
    }

    public float getMaxShieldPoints () {
        return maxShieldPoints;
    }

    public void setMaxShieldPoints (float maxShieldPoints) {
        this.maxShieldPoints = maxShieldPoints;
    }

    public boolean isHasAttack () {
        return hasAttack;
    }

    public void setHasAttack (boolean hasAttack) {
        this.hasAttack = hasAttack;
    }

    public float getDamage () {
        return damage;
    }

    public void setDamage (float damage) {
        this.damage = damage;
    }

    public boolean isAllowedToDealDamage () {
        return allowedToDealDamage;
    }

    public void setAllowedToDealDamage (boolean allowedToDealDamage) {
        this.allowedToDealDamage = allowedToDealDamage;
    }

    public boolean isFriendly () {
        return friendly;
    }

    public void setFriendly (boolean friendly) {
        this.friendly = friendly;
    }

    public GameObject getObjectToCenterAround () {
        return objectToCenterAround;
    }

    public void setObjectToCenterAround (GameObject objectToCenterAround) {
        this.objectToCenterAround = objectToCenterAround;
    }

    public GameObject getObjectToFollow () {
        return objectToFollow;
    }

    public void setObjectToFollow (GameObject objectToFollow) {
        this.objectToFollow = objectToFollow;
        this.movementConfiguration.setTarget(objectToFollow);
    }

    public MovementConfiguration getMovementConfiguration () {
        return movementConfiguration;
    }

    public void setMovementConfiguration (MovementConfiguration movementConfiguration) {
        this.movementConfiguration = movementConfiguration;
    }

    public List<GameObject> getObjectsFollowingThis () {
        return objectsFollowingThis;
    }

    public void addFollowingGameObject (GameObject followingObject) {
        this.objectsFollowingThis.add(followingObject);
    }

    public SpriteAnimation getAnimation () {
        return this.animation;
    }

    public void setAnimation (SpriteAnimation spriteAnimation) {
        this.animation = spriteAnimation;
    }

    public float getScale () {
        return scale;
    }

    public float getOriginalScale () {
        return originalScale;
    }

    public String getObjectType () {
        return this.objectType;
    }

    public void setObjectType (String objectType) {
        this.objectType = objectType;
    }

    public void setScale (float newScale) {
        this.scale = newScale;
        if (this.animation != null) {
            this.animation.setAnimationScale(scale);
        } else {
            this.image = ImageResizer.getInstance().getScaledImage(image, scale);
            configureImageDimensions();
        }
    }

    public void setPathFinder (PathFinder newPathFinder) {
        if (this.movementConfiguration != null) {
            this.movementConfiguration.setPathFinder(newPathFinder);
        } else {
            System.out.println("Tried to adjust a PathFinder without having an existing MovementConfiguration");
        }
    }

    public void updateBoardBlock () {
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
    }

    public List<GameObject> getObjectOrbitingThis () {
        return objectOrbitingThis;
    }


    public boolean isBoxCollision () {
        return boxCollision;
    }

    public void setBoxCollision (boolean boxCollision) {
        this.boxCollision = boxCollision;
    }

    public double getLastGameSecondDamageTaken () {
        return lastGameSecondDamageTaken;
    }

    public void setLastGameSecondDamageTaken (long lastGameSecondDamageTaken) {
        this.lastGameSecondDamageTaken = lastGameSecondDamageTaken;
    }

    public GameObject getOwnerOrCreator () {
        return ownerOrCreator;
    }

    public void setOwnerOrCreator (GameObject ownerOrCreator) {
        this.ownerOrCreator = ownerOrCreator;
    }

    public float getBaseArmor () {
        return baseArmor;
    }

    public void setBaseArmor (float baseArmor) {
        this.baseArmor = baseArmor;
    }

    public float getArmorBonus () {
        return armorBonus;
    }

    public void adjustArmorBonus (float amount) {
        this.armorBonus += amount; // Modify the armor bonus by the given amount
    }

    public float getCashMoneyWorth () {
        return cashMoneyWorth;
    }

    public void setCashMoneyWorth (float cashMoneyWorth) {
        this.cashMoneyWorth = cashMoneyWorth;
    }

    public boolean isAllowedToMove () {
        return allowedToMove;
    }

    public void setAllowedToMove (boolean allowedToMove) {
        this.allowedToMove = allowedToMove;
    }

    public double getRotationAngle () {
        return rotationAngle;
    }

    public void setRotationAngle (double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public boolean isAllowedVisualsToRotate () {
        return allowedVisualsToRotate;
    }

    public void setAllowedVisualsToRotate (boolean allowedVisualsToRotate) {
        this.allowedVisualsToRotate = allowedVisualsToRotate;
    }
}
