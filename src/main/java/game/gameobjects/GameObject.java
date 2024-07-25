package game.gameobjects;

import game.gameobjects.missiles.missiletypes.BarrierProjectile;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatsTracker;
import game.items.Item;
import game.items.PlayerInventory;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.managers.AnimationManager;
import game.managers.OnScreenTextManager;
import game.movement.*;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.image.ImageResizer;
import VisualAndAudioData.image.ImageRotator;
import game.items.effects.EffectInterface;
import game.gameobjects.missiles.specialAttacks.ElectroShred;
import game.gameobjects.player.PlayerStats;
import game.util.ArmorCalculator;
import game.util.BoardBlockUpdater;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.Sprite;
import visualobjects.SpriteAnimation;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    protected float bonusDamageMultiplier = 1.0f;
    protected boolean allowedToDealDamage; //Set to false for explosions that hit a target
    protected float attackSpeed;
    protected float attackSpeedBonusModifier;
    protected boolean appliesOnHitEffects;

    //Game logic variables
    protected boolean friendly;

    //Movement variables
    protected GameObject objectToCenterAround;
    protected GameObject objectToFollow;
    protected MovementConfiguration movementConfiguration;


    protected int lastBoardBlock;
    protected Point currentLocation;
    protected int currentBoardBlock;


    //Objects following this object
    protected List<GameObject> objectsFollowingThis = new ArrayList<GameObject>();

    protected List<GameObject> objectOrbitingThis = new ArrayList<GameObject>();

    //Other
    protected String objectType;
    protected Direction movementRotation;
    protected boolean boxCollision;
    protected double lastGameSecondDamageTaken;
    protected GameObject ownerOrCreator;
    protected boolean centeredAroundObject = false;

    private int movementCounter = 0;
    protected float cashMoneyWorth;
    protected double rotationAngle;
    protected boolean allowedVisualsToRotate;

    protected CopyOnWriteArrayList<EffectInterface> effects = new CopyOnWriteArrayList<>();
    protected List<SpriteAnimation> effectAnimations = new ArrayList<>();

    protected float xpOnDeath = 0;

    public GameObject (SpriteConfiguration spriteConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration);
        initGameObject();
        if (spriteConfiguration.getImageType() != null) {
            this.loadImage(spriteConfiguration.getImageType());
        }
    }

    public GameObject (SpriteAnimationConfiguration spriteAnimationConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration.getSpriteConfiguration());
        this.animation = new SpriteAnimation(spriteAnimationConfiguration);
//        this.animation.setAnimationScale(spriteAnimationConfiguration.getSpriteConfiguration().getScale());
        initGameObject();
    }

    protected void initGameObject () {
        if (this.animation != null) {
            this.currentLocation = new Point(this.animation.getXCoordinate(), this.animation.getYCoordinate());
            //Align the object with it's animation
            this.xCoordinate = this.animation.getXCoordinate();
            this.yCoordinate = this.animation.getYCoordinate();
        } else {
            this.currentLocation = new Point(this.xCoordinate, this.yCoordinate);
        }

        this.visible = true;
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        this.allowedToMove = true;
        this.allowedVisualsToRotate = true;
    }

    protected void initMovementConfiguration (MovementConfiguration movementConfiguration) {
        this.movementRotation = movementConfiguration.getRotation();
        movementConfiguration.setCurrentLocation(currentLocation);
        movementConfiguration.setDestination(movementConfiguration.getPathFinder().calculateInitialEndpoint(currentLocation, movementRotation, this.friendly));
        if (movementConfiguration.getPathFinder() instanceof HomingPathFinder) {
            movementConfiguration.setTargetToChase(((HomingPathFinder) movementConfiguration.getPathFinder()).getTarget(this.friendly, this.xCoordinate, this.yCoordinate));
            movementConfiguration.setHasLock(true);
        }
        if (movementConfiguration.getXMovementSpeed() == 0 && movementConfiguration.getYMovementSpeed() == 0) {
            this.rotationAngle = this.movementRotation.toAngle();
        }
        movementConfiguration.setStepsTaken(0);
        this.movementConfiguration = movementConfiguration;
    }

    public void resetMovementPath () {
        if (movementConfiguration == null) {
            return;
        }

        movementConfiguration.resetMovementPath();

        if (this.animation != null) {
            this.currentLocation = new Point(this.animation.getXCoordinate(), this.animation.getYCoordinate());
            this.xCoordinate = this.animation.getXCoordinate();
            this.yCoordinate = this.animation.getYCoordinate();
        } else {
            this.currentLocation = new Point(this.xCoordinate, this.yCoordinate);
        }


        movementConfiguration.setCurrentLocation(this.currentLocation);
        movementConfiguration.setDestination(movementConfiguration.getPathFinder().calculateInitialEndpoint(this.currentLocation, movementRotation, this.friendly));

        if (movementConfiguration.getPathFinder() instanceof HomingPathFinder) {
            movementConfiguration.setTargetToChase(((HomingPathFinder) movementConfiguration.getPathFinder()).getTarget(this.friendly, this.xCoordinate, this.yCoordinate));
            movementConfiguration.setHasLock(true);
        }

        movementConfiguration.setStepsTaken(0);
    }


    //*****************EFFECTS*******************************

    //Effect animations are NOT moved in this class
    public void addEffect (EffectInterface effect) {
        if (visible && currentHitpoints > 0) {
            EffectInterface existingEffect = getExistingEffect(effect);

            if (existingEffect == null) {
                effects.add(effect);
            } else {
                refreshEffect(existingEffect);
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

    private EffectInterface getExistingEffect (EffectInterface effect) {
        return effects.stream()
                .filter(e -> e.getEffectIdentifier().equals(effect.getEffectIdentifier()))
                .findFirst()
                .orElse(null);
    }

    private void activateOnDeathEffects () {
        activateEffects(EffectActivationTypes.OnObjectDeath);
        cleanseAllEffects();
    }

    public void updateGameObjectEffects () {
        activateEffects(EffectActivationTypes.CheckEveryGameTick);
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
                        //This is here to possibly remove the bug of persistent effect animations
                        if (AnimationManager.getInstance().getUpperAnimations().contains(effect.getAnimation())) {
                            AnimationManager.getInstance().getUpperAnimations().remove(effect.getAnimation());
                        }
                    }
                    toRemove.add(effect);
                }
            }
        }

        effects.removeAll(toRemove);
    }

    private void refreshEffect (EffectInterface effect) {
        //Effects themselves should determine what to do in this scenario
        effect.resetDuration();
        effect.increaseEffectStrength();
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

        if (this.destructionAnimation != null && !this.destructionAnimation.isPlaying()) {
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
        if (movementConfiguration != null) {
            this.movementConfiguration.deleteConfiguration();
        }
        this.movementConfiguration = null;
        this.ownerOrCreator = null;

        this.visible = false;
    }

    public void takeDamage (float damageTaken, boolean showDamageText) {
        if(currentHitpoints <= 0){
            return; //The target is already dead, no need to go through here again
        }

        if (damageTaken > 0) {
            damageTaken = ArmorCalculator.calculateDamage(damageTaken, this);
        }

        if(!this.isFriendly()){
            //Assume that if its not friendly, the damage came from the player
            GameStatsTracker.getInstance().addDamageDealt(damageTaken);
            GameStatsTracker.getInstance().setHighestDamageDealt(damageTaken);
        }

        this.currentHitpoints -= damageTaken;
        if (damageTaken > 0) {
            lastGameSecondDamageTaken = GameStateInfo.getInstance().getGameSeconds();
        }

        if (currentHitpoints > maxHitPoints) {
            currentHitpoints = maxHitPoints;
        }

        if(showDamageText){
            OnScreenTextManager.getInstance().addDamageNumberText(damageTaken, this.getCenterXCoordinate(), this.getCenterYCoordinate());
        }

        if (this.currentHitpoints <= 0) {
            if (this.xpOnDeath > 0) {
                PlayerStats.getInstance().addXP(xpOnDeath);
            }
            if (this.destructionAnimation != null) {
                this.destructionAnimation.setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
//                this.destructionAnimation.rotateAnimation(this.rotationAngle, false);
                AnimationManager.getInstance().addUpperAnimation(destructionAnimation);
            }

            for (GameObject object : objectsFollowingThis) {
                if(object.isVisible()) {
                    object.setCashMoneyWorth(0);
                    object.takeDamage(object.getMaxHitPoints() * 5, false);
                }
            }

            for(GameObject object : objectOrbitingThis){
                if(object.isVisible()) {
                    object.setCashMoneyWorth(0);
                    object.takeDamage(object.getMaxHitPoints() * 5, false);
                }
            }

            try {
                AudioManager.getInstance().addAudio(deathSound);
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }


            triggerClassSpecificOnDeathTriggers();

            this.setVisible(false);
            activateOnDeathEffects();
            PlayerInventory.getInstance().gainCashMoney(this.cashMoneyWorth);
            GameStatsTracker.getInstance().addMoneyAcquired(this.cashMoneyWorth);
        }
    }

    public void triggerClassSpecificOnDeathTriggers(){
        //Supposed to be overriden. Used for "Enemy kill counter" for example
    }

    public void applyEffectsWhenPlayerHitsEnemy (GameObject object) {
        if (this.appliesOnHitEffects) {
            List<Item> onHitItems = PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.AfterCollision);
            for (Item item : onHitItems) {
                item.applyEffectToObject(object);
            }
        }
    }

    public void applyDamageModification (GameObject target) {
        for (Item item : PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.BeforeCollision)) {
            item.modifyAttackValues(this, target);
        }
    }

    public void centerDestructionAnimation () {
        if (this.getAnimation() != null) {
            this.getDestructionAnimation().setOriginCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());
        } else {
            this.getDestructionAnimation().setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        }
    }

    public void dealDamageToGameObject (GameObject gameObject) {
        boolean showDamage = true;
        if(this instanceof ElectroShred){
            showDamage = false;
        }
        gameObject.takeDamage(getDamage(), showDamage);
    }


    //*****************MOVEMENT*******************************
    public void move () {
        toggleHealthBar();
        SpriteMover.getInstance().moveGameObject(this, movementConfiguration);
        moveAnimations();
        this.bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
        updateBoardBlock();
        updateVisibility();

        if (animation != null) {
//            animation.setAnimationBounds(xCoordinate, yCoordinate);
        }

        for (GameObject object : objectsFollowingThis) {
            if (object.isCenteredAroundObject()) {
                object.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
                if (object.getAnimation() != null) {
                    object.getAnimation().setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
                }
            } else {

                //object.move();
                //Objects here should be in their respective managers. Only special attacks have to be moved like this!
                //Because they have no movement configuration and don't need one, else they would be missiles
            }
        }
    }

    private void toggleHealthBar () {
        if (this.currentHitpoints < this.maxHitPoints) {
            showHealthBar = true;
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
        animation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
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
    public void rotateGameObjectTowards (Direction direction, boolean crop) {
        if (!isAllowedVisualsToRotate() || this.rotationAngle == direction.toAngle()) {
            return;
        }

        this.rotationAngle = direction.toAngle();

        if (this.animation != null) {
            rotateGameObjectSpriteAnimations(animation, direction, crop);
        }
        if (this.exhaustAnimation != null) {
            rotateGameObjectSpriteAnimations(exhaustAnimation, direction, crop);
        }

        if (this.destructionAnimation != null) {
            rotateGameObjectSpriteAnimations(destructionAnimation, direction, crop);
        }
        if (this.image != null) {
            this.image = ImageRotator.getInstance().rotate(originalImage, direction, crop);
            super.recalculateBoundsAndSize();
        }
    }

    protected void updateChargingAttackAnimationCoordination () {
        if (this.chargingUpAttackAnimation != null) {
            double baseDistance = (this.getWidth() + this.getHeight()) / 2.0;
            Point chargingUpLocation = ImageRotator.getInstance().calculateFrontPosition(this.getCenterXCoordinate(), this.getCenterYCoordinate(), rotationAngle, baseDistance);
            this.chargingUpAttackAnimation.setCenterCoordinates(chargingUpLocation.getX(), chargingUpLocation.getY());
        }
    }

    public void rotateGameObjectTowards (int targetXCoordinate, int targetYCoordinate, boolean crop) {
        //Not cropping caused the bug
        if(!allowedVisualsToRotate){
            return;
        }

        double calculatedAngle = ImageRotator.getInstance().calculateAngle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetXCoordinate, targetYCoordinate);
        rotateObjectTowardsAngle(calculatedAngle, crop);
    }

    protected void rotateObjectTowardsAngle (double calculatedAngle, boolean crop) {
        if (this.rotationAngle != calculatedAngle) {
            if (this.animation != null) {
                this.animation.rotateAnimation(calculatedAngle, crop);
            } else if (this.image != null) {
                this.image = ImageRotator.getInstance().rotateOrFlip(this.originalImage, calculatedAngle, crop);
                super.recalculateBoundsAndSize();
            }

            this.rotationAngle = calculatedAngle;
            updateChargingAttackAnimationCoordination();
        }
    }

    private void rotateGameObjectSpriteAnimations (SpriteAnimation sprite, Direction direction, boolean crop) {
        if (sprite != null) {
            sprite.rotateAnimation(direction, crop);
        }
    }

    public void rotateAfterMovement () {
        updateChargingAttackAnimationCoordination();
        if(!this.allowedVisualsToRotate){
            return;
        }
        //Rotate towards the destination if one is found, else rotate towards its rotation enum value
        if (!movementConfiguration.getCurrentPath().getWaypoints().isEmpty()) {
            rotateObjectTowardsDestination(true);
            setAllowedVisualsToRotate(false);
        } else {
            rotateObjectTowardsRotation(true);
            setAllowedVisualsToRotate(false);
        }
    }

    public void rotateObjectTowardsDestination (boolean crop) {
        if(!this.isAllowedVisualsToRotate()){
            return;
        }

        Point destination = null;

        if(movementConfiguration.getCurrentPath() != null && !movementConfiguration.getCurrentPath().getWaypoints().isEmpty()){
            destination = movementConfiguration.getCurrentPath().getWaypoints().get(movementConfiguration.getCurrentPath().getWaypoints().size() - 1);
        } else if(movementConfiguration.getDestination() != null){
            destination = movementConfiguration.getDestination();
        }

        destination = adjustDestinationForRotationUsingDimensions(this, destination);
        this.rotateGameObjectTowards(destination.getX(), destination.getY(), crop);
    }

    public void rotateObjectTowardsRotation (boolean crop) {
        if (this.isAllowedVisualsToRotate() && this.getMovementConfiguration().getRotation() != null) {
            this.rotateGameObjectTowards(this.getMovementConfiguration().getRotation(), crop);
        }
    }

    protected void rotateObjectTowardsPoint (Point point, boolean crop) {
        if (this.isAllowedVisualsToRotate()) {
            this.rotateGameObjectTowards(point.getX(), point.getY(), crop);
        }
    }

    private Point adjustDestinationForRotationUsingDimensions (GameObject gameObject, Point destination) {
        int height = 0;
        int width = 0;
        if (gameObject.getAnimation() != null) {
            height = gameObject.getAnimation().getHeight();
            width = gameObject.getAnimation().getWidth();
        } else {
            height = gameObject.getHeight();
            width = gameObject.getWidth();
        }

        // Assuming the GameObject's (x, y) represents its top-left corner,
        // and we need to adjust the destination to point towards the center of the GameObject.
        // Calculate the center offsets
        int centerXOffset = width / 2;
        int centerYOffset = height / 2;

        // Adjust the destination by adding the calculated offsets
        // This adjustment assumes the destination point is intended to be the center point
        // of where the GameObject should rotate towards.
        Point adjustedDestination = new Point(destination.getX() + centerXOffset, destination.getY() + centerYOffset);

        return adjustedDestination;
    }

    //*****************SPECIFIC ENEMY BEHAVIOURS*******************************
    public void onCreationEffects () {
        //Exist to be overriden
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
        this.movementConfiguration.setTargetToChase(objectToFollow);
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
        if (this.animation != null) {
            return animation.getScale();
        }
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
            super.recalculateBoundsAndSize();
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
        if (this.animation != null) {
            this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(animation.getXCoordinate());
        } else {
            this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        }
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

    public boolean isAllowedVisualsToRotate () {
        return allowedVisualsToRotate;
    }

    public void setAllowedVisualsToRotate (boolean allowedVisualsToRotate) {
        this.allowedVisualsToRotate = allowedVisualsToRotate;
    }

    public boolean isCenteredAroundObject () {
        return centeredAroundObject;
    }

    public void setCenteredAroundObject (boolean centeredAroundObject) {
        this.centeredAroundObject = centeredAroundObject;
    }

    @Override
    public void setCenterCoordinates (int newXCoordinate, int newYCoordinate) {
        if (this.image != null && this.animation == null) {
            this.xCoordinate = newXCoordinate - (this.width / 2) + xOffset;
            this.yCoordinate = newYCoordinate - (this.height / 2) + yOffset;
            this.currentLocation = new Point(xCoordinate, yCoordinate);
        }

        if (this.animation != null) {
            this.xCoordinate = newXCoordinate - (this.animation.getWidth() / 2) + this.animation.getXOffset();
            this.yCoordinate = newYCoordinate - (this.animation.getHeight() / 2) + this.animation.getYOffset();
            this.animation.setCenterCoordinates(newXCoordinate, newYCoordinate);
            this.currentLocation = new Point(animation.getXCoordinate(), animation.getYCoordinate());
        }
    }





    public float getAttackSpeed () {
        float baseAttackSpeed = this.attackSpeed;
        float attackSpeedIncrease = this.attackSpeedBonusModifier;

        // Calculate the new attack speed
        float newAttackSpeed = baseAttackSpeed / (1 + attackSpeedIncrease / 100);

        //Maximum attack speed
        if (newAttackSpeed < 0.1) {
            newAttackSpeed = 0.1f;
        }
        // Ensure the attack speed does not fall below a minimum threshold
        return Math.round(newAttackSpeed);
    }

    public float getDamage () {
        float attackDamage = this.damage * this.bonusDamageMultiplier;
        if (attackDamage < 0.1) {
            return 0.1f;
        } else {
            return attackDamage;
        }
    }

    public void modifyBonusDamageMultiplier (float bonusPercentage) {
        this.bonusDamageMultiplier += bonusPercentage;
    }

    public void setAttackSpeed (int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void modifyAttackSpeedBonus (float bonusPercentage) {
        this.attackSpeedBonusModifier += bonusPercentage;
    }

//    public int getXCoordinate () {
//        if (this.animation != null) {
//            return this.animation.getXCoordinate();
//        } else {
//            return this.xCoordinate;
//        }
//    }


    public int getCenterXCoordinate () {
        if (this.animation != null) {
            return animation.getXCoordinate() + xOffset + (animation.getWidth() / 2);
        } else
            return xCoordinate + xOffset + (width / 2);
    }

    public int getCenterYCoordinate () {
        if (this.animation != null) {
            return animation.getYCoordinate() + yOffset + (animation.getHeight() / 2);
        } else
            return yCoordinate + yOffset + (height / 2);
    }


    public int getWidth () {
        if (this.animation != null) {
            return animation.getWidth();
        } else
            return this.width;

    }

    public int getHeight () {
        if (this.animation != null) {
            return animation.getHeight();
        } else
            return this.height;
    }

    public void setTransparancyAlpha (boolean shouldIncrease, float newAlphaTransparancy, float transparacyStepSize) {
        if (this.animation != null) {
            this.animation.setTransparancyAlpha(shouldIncrease, newAlphaTransparancy, transparacyStepSize);
        } else {
            super.setTransparancyAlpha(shouldIncrease, newAlphaTransparancy, transparacyStepSize);
        }
    }


    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return showHealthBar == that.showHealthBar && allowedToMove == that.allowedToMove && Float.compare(originalScale, that.originalScale) == 0 && Float.compare(currentHitpoints, that.currentHitpoints) == 0 && Float.compare(maxHitPoints, that.maxHitPoints) == 0 && Float.compare(currentShieldPoints, that.currentShieldPoints) == 0 && Float.compare(maxShieldPoints, that.maxShieldPoints) == 0 && Float.compare(baseArmor, that.baseArmor) == 0 && Float.compare(armorBonus, that.armorBonus) == 0 && hasAttack == that.hasAttack && Float.compare(damage, that.damage) == 0 && Float.compare(bonusDamageMultiplier, that.bonusDamageMultiplier) == 0 && allowedToDealDamage == that.allowedToDealDamage && Float.compare(attackSpeed, that.attackSpeed) == 0 && Float.compare(attackSpeedBonusModifier, that.attackSpeedBonusModifier) == 0 && friendly == that.friendly && lastBoardBlock == that.lastBoardBlock && currentBoardBlock == that.currentBoardBlock && boxCollision == that.boxCollision && Double.compare(lastGameSecondDamageTaken, that.lastGameSecondDamageTaken) == 0 && centeredAroundObject == that.centeredAroundObject && movementCounter == that.movementCounter && Float.compare(cashMoneyWorth, that.cashMoneyWorth) == 0 && Double.compare(rotationAngle, that.rotationAngle) == 0 && allowedVisualsToRotate == that.allowedVisualsToRotate && Float.compare(xpOnDeath, that.xpOnDeath) == 0 && Objects.equals(animation, that.animation) && Objects.equals(destructionAnimation, that.destructionAnimation) && Objects.equals(shieldDamagedAnimation, that.shieldDamagedAnimation) && Objects.equals(exhaustAnimation, that.exhaustAnimation) && Objects.equals(chargingUpAttackAnimation, that.chargingUpAttackAnimation) && deathSound == that.deathSound && Objects.equals(objectToCenterAround, that.objectToCenterAround) && Objects.equals(objectToFollow, that.objectToFollow) && Objects.equals(movementConfiguration, that.movementConfiguration) &&  Objects.equals(currentLocation, that.currentLocation) && Objects.equals(objectsFollowingThis, that.objectsFollowingThis) && Objects.equals(objectOrbitingThis, that.objectOrbitingThis) && Objects.equals(objectType, that.objectType) && movementRotation == that.movementRotation && Objects.equals(ownerOrCreator, that.ownerOrCreator) && Objects.equals(effects, that.effects) && Objects.equals(effectAnimations, that.effectAnimations);
    }

    @Override
    public int hashCode () {
        return Objects.hash(animation, destructionAnimation, shieldDamagedAnimation, exhaustAnimation, chargingUpAttackAnimation, showHealthBar, allowedToMove, originalScale, deathSound, currentHitpoints, maxHitPoints, currentShieldPoints, maxShieldPoints, baseArmor, armorBonus, hasAttack, damage, bonusDamageMultiplier, allowedToDealDamage, attackSpeed, attackSpeedBonusModifier, friendly, objectToCenterAround, objectToFollow, movementConfiguration, lastBoardBlock, currentLocation, currentBoardBlock, objectsFollowingThis, objectOrbitingThis, objectType, movementRotation, boxCollision, lastGameSecondDamageTaken, ownerOrCreator, centeredAroundObject, movementCounter, cashMoneyWorth, rotationAngle, allowedVisualsToRotate, effects, effectAnimations, xpOnDeath);
    }
}
