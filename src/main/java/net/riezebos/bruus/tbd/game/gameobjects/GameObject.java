package net.riezebos.bruus.tbd.game.gameobjects;

import net.riezebos.bruus.tbd.game.UI.GameUICreator;
import net.riezebos.bruus.tbd.game.UI.UIObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossBeacon;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.*;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HomingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.game.util.ArmorCalculator;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.game.util.VisualLayer;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageResizer;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageRotator;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameObject extends Sprite {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private final int id;

    protected SpriteAnimation animation; //This is a code smell, because spriteanimation is already extension of sprite, need to untangle this
    protected SpriteAnimation destructionAnimation;
    protected SpriteAnimation shieldDamagedAnimation;
    protected SpriteAnimation exhaustAnimation;
    protected SpriteAnimation chargingUpAttackAnimation;
    protected boolean showHealthBar;
    protected boolean allowedToMove;

    //Audio variables
    protected AudioEnums deathSound;
    protected VisualLayer visualLayer = VisualLayer.Upper; //Default this to false, should only be overridden when explicitly intended to

    //Hitpoint variables
    protected float currentHitpoints;
    protected float maxHitPoints;
    protected float currentShieldPoints;
    protected float maxShieldPoints;
    protected float baseArmor;
    protected float armorBonus;
    protected float damageReductionMultiplier = 1;


    //Damage variables
    protected boolean hasAttack;
    protected float damage;
    protected float bonusDamageMultiplier = 1.0f;
    protected boolean allowedToDealDamage; //Set to false for explosions that hit a target
    protected float attackSpeed;
    protected float attackSpeedBonusPercentage;
    protected boolean appliesOnHitEffects;
    protected List<EffectInterface> effectsToApply = new ArrayList<>();
    protected boolean showDamage = true;

    //Game logic variables
    protected boolean friendly;

    //Movement variables
    protected GameObject objectToCenterAround;
    protected GameObject objectToFollow;
    protected MovementConfiguration movementConfiguration;


    protected Point currentLocation;
    protected int currentBoardBlock;
    protected int knockbackStrength;


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

    protected float cashMoneyWorth;
    protected double rotationAngle = Direction.RIGHT.toAngle();
    protected boolean allowedVisualsToRotate;

    protected CopyOnWriteArrayList<EffectInterface> effects = new CopyOnWriteArrayList<>();
    protected List<SpriteAnimation> effectAnimations = new ArrayList<>();
    protected float xpOnDeath = 0;
    protected boolean isACrit = false;
    protected double lastTimeDamageTakenFromLaserbeams = 0;

    public GameObject(SpriteConfiguration spriteConfiguration) {
        super(spriteConfiguration);
        if (spriteConfiguration.getImageType() != null) {
            this.setImage(spriteConfiguration.getImageType());
        }
        this.id = idGenerator.incrementAndGet(); // Generate a unique ID
        initGameObject();
    }

    public GameObject(SpriteAnimationConfiguration spriteAnimationConfiguration) {
        super(spriteAnimationConfiguration.getSpriteConfiguration());
        this.animation = new SpriteAnimation(spriteAnimationConfiguration);
        initGameObject();
        this.id = idGenerator.incrementAndGet(); // Generate a unique ID
    }

    protected void initGameObject() {
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

    protected void initMovementConfiguration(MovementConfiguration movementConfiguration) {
        this.movementRotation = movementConfiguration.getRotation();
        movementConfiguration.setCurrentLocation(currentLocation);

        if (movementConfiguration.getDestination() == null) {
            movementConfiguration.setDestination(movementConfiguration.getPathFinder().calculateInitialEndpoint(currentLocation, movementRotation, this.friendly));
        }
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

    public void resetMovementPath() {
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
    public void addEffect(EffectInterface effect) {
        if (visible && currentHitpoints > 0) {
            EffectInterface existingEffect = getExistingEffect(effect);

            if (existingEffect == null) {
                effects.add(effect);
            } else {
                refreshEffect(existingEffect);
                return; //Already have it, so we don't need to add the animation of the new effect
            }

            if (effect.getAnimation() != null) {
                boolean effectAnimationExists = false;
                for (SpriteAnimation effectAnim : effectAnimations) {
                    if (effectAnim.getImageEnum().equals(effect.getAnimation().getImageEnum())) {
                        effectAnimationExists = true;
                    }
                }

                if (!effectAnimationExists) {
                    effect.getAnimation().refreshAnimation(); //To handle ignite, idk why it does this as of now
                    effectAnimations.add(effect.getAnimation());
                    AnimationManager.getInstance().addUpperAnimation(effect.getAnimation());
                }
            }
        }
    }

    private EffectInterface getExistingEffect(EffectInterface effect) {
        return effects.stream()
                .filter(e -> e.getEffectIdentifier().equals(effect.getEffectIdentifier()))
                .findFirst()
                .orElse(null);
    }

    private void activateOnDeathEffects() {
        activateEffects(EffectActivationTypes.OnObjectDeath);
        cleanseAllEffects();
    }

    public void updateGameObjectEffects() {
        activateEffects(EffectActivationTypes.CheckEveryGameTick);
    }


    private void activateEffects(EffectActivationTypes providedActivationType) {
        List<EffectInterface> toRemove = new ArrayList<>();
        for (EffectInterface effect : effects) {
            if (effect.getEffectTypesEnums() == providedActivationType) {
                effect.activateEffect(this);
                if (effect.shouldBeRemoved(this)) {
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
                    effect.removeEffect(this);
                }
            }
        }

        effects.removeAll(toRemove);
    }

    private void refreshEffect(EffectInterface effect) {
        //Effects themselves should determine what to do in this scenario
        effect.resetDuration();
        effect.increaseEffectStrength(this);
    }

    private void cleanseAllEffects() {
        //Does NOT set all effects to invisible, so might lead to problems later on
        //For example, effects that trigger after or during the objects deletion
        effects.clear();
    }

    //*****************DELETION/DAMAGE*******************************

    public void deleteObject() {
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

    public void takeDamage(float damageTaken) {
        if (currentHitpoints <= 0) {
            return; //The target is already dead, no need to go through here again
        }

        if (damageTaken > 0) {
            damageTaken = (ArmorCalculator.calculateDamage(damageTaken, this) * damageReductionMultiplier);
        }

        if (damageTaken > 0) {
            lastGameSecondDamageTaken = GameState.getInstance().getGameSeconds();
        }

        if (!this.isFriendly() && !(damageTaken >= 9998 && damageTaken <= 10000)) {
            //Assume that if this object is not friendly, the damage came from the player
            GameStatsTracker.getInstance().addDamageDealt(damageTaken);
            GameStatsTracker.getInstance().setHighestDamageDealt(damageTaken);
        }

        this.currentHitpoints -= damageTaken;
        if (currentHitpoints > maxHitPoints) {
            currentHitpoints = maxHitPoints;
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
                if (object.isVisible()) {
                    object.setCashMoneyWorth(0);
                    object.takeDamage(object.getMaxHitPoints() * 5);
                }
            }

            for (GameObject object : objectOrbitingThis) {
                if (object.isVisible()) {
                    object.setCashMoneyWorth(0);
                    object.takeDamage(object.getMaxHitPoints() * 5);
                }
            }

            AudioManager.getInstance().addAudio(deathSound);

            triggerOnDeathActions();

            this.setVisible(false);
            activateOnDeathEffects();
            PlayerInventory.getInstance().addMinerals(this.cashMoneyWorth);
            GameStatsTracker.getInstance().addMoneyAcquired(this.cashMoneyWorth);

        }
    }

    public void triggerOnDeathActions() {
        //Supposed to be overriden. Used for "Enemy kill counter" for example
    }

    public void applyAfterCollisionItemEffects(GameObject target) {
        if (this.appliesOnHitEffects) {
            List<Item> onHitItems = PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.AfterCollision);
            for (Item item : onHitItems) {
                item.applyEffectToObject(target);
                item.applyEffectToObject(this, target);
            }
        }
    }

    public void applyBeforeCollisionAttackModifyingItemEffects(GameObject target) {
        for (Item item : PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.BeforeCollision)) {
            item.modifyAttackingObject(this, target);
            item.applyEffectToObject(target);
            item.applyEffectToObject(this, target);
        }
    }

    public void centerDestructionAnimation() {
        if (this.getAnimation() != null) {
            this.getDestructionAnimation().setOriginCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());
        } else {
            this.getDestructionAnimation().setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        }
    }

    public void dealDamageToGameObject(GameObject target) {
        for (EffectInterface effectInterface : effectsToApply) {
            target.addEffect(effectInterface);
        }



        float damage = ArmorCalculator.calculateDamage(getDamage(), target);
        target.takeDamage(damage);

        if (showDamage && damage >= 1) {
            OnScreenTextManager.getInstance().addDamageNumberText(Math.round(damage), target.getCenterXCoordinate(),
                    target.getCenterYCoordinate(), isACrit);
        }

    }


    //*****************MOVEMENT*******************************
    public void move() {
        toggleHealthBar();
        if (this.isAllowedToMove()) {
            SpriteMover.getInstance().moveGameObject(this, movementConfiguration);
            moveAnimations();
            this.bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);

            for (GameObject object : objectsFollowingThis) {
                object.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
            }
        }
        updateBoardBlock();
        updateVisibility();
    }

    private void toggleHealthBar() {
        if (this instanceof Missile || showHealthBar) {
            return;
        }

        if (this.currentHitpoints < this.maxHitPoints) {
            showHealthBar = true;
        }
    }

    private void moveAnimations() {
        if (this.animation != null) {
            moveAnimations(animation);
        }


        //Not needed
//        if (this.destructionAnimation != null) {
//            moveAnimations(destructionAnimation);
//        }

        for (SpriteAnimation spriteAnimation : effectAnimations) {
            moveAnimationsToCenter(spriteAnimation);
        }

//        updateChargingAttackAnimationCoordination();
    }

    private void moveAnimations(SpriteAnimation animation) {
        animation.setXCoordinate(this.getXCoordinate());
        animation.setYCoordinate(this.getYCoordinate());
        animation.setAnimationBounds(animation.getXCoordinate(), animation.getYCoordinate());
    }

    private void moveAnimationsToCenter(SpriteAnimation animation) {
        animation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
    }


    private void updateVisibility() {
        if (SpriteRemover.getInstance().shouldRemoveVisibility(this)) {
            this.visible = false;
        }

        if (!this.visible && this.animation != null) {
            this.animation.setVisible(false);
        }

        if (this.animation != null && !this.animation.isVisible()) {
            this.setVisible(false);
        }
    }


    //*****************VISUAL ALTERATION*******************************
    public void rotateGameObjectTowards(Direction direction, boolean crop) {
        if (ImageRotator.getInstance().isBlockedFromRotating(this.getImageEnum())) {
            return;
        }

        if (!isAllowedVisualsToRotate()) {
            return;
        }

        this.rotationAngle = direction.toAngle();

        if (this.animation != null) {
            rotateGameObjectSpriteAnimations(animation, direction, crop);
        }

        if (this.image != null) {
            this.image = ImageRotator.getInstance().rotate(originalImage, direction, crop);
            super.recalculateBoundsAndSize();
        }
    }

    @Override
    public boolean isVisible() {
        if (this.animation != null) {
            return this.animation.isVisible();
        }
        return visible;
    }

    protected void updateChargingAttackAnimationCoordination() {
        if (this.chargingUpAttackAnimation != null) {
            double baseDistance = (this.getWidth() + this.getHeight()) / 2.0;
            Point chargingUpLocation = calculateFrontPosition(this.getCenterXCoordinate(), this.getCenterYCoordinate(), rotationAngle, baseDistance);
            this.chargingUpAttackAnimation.setCenterCoordinates(chargingUpLocation.getX(), chargingUpLocation.getY());
        }
    }

    private Point calculateFrontPosition(int centerX, int centerY, double angleDegrees, double distanceToFront) {
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the new position using trigonometry
        int newX = centerX + (int) (Math.cos(angleRadians) * distanceToFront);
        int newY = centerY + (int) (Math.sin(angleRadians) * distanceToFront);

        return new Point(newX, newY);
    }

    public void rotateGameObjectTowards(int targetXCoordinate, int targetYCoordinate, boolean crop) {
        if (ImageRotator.getInstance().isBlockedFromRotating(this.getImageEnum()) || !allowedVisualsToRotate) {
            return;
        }

        double calculatedAngle = ImageRotator.getInstance().calculateAngle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetXCoordinate, targetYCoordinate);
        rotateObjectTowardsAngle(calculatedAngle, crop);
    }

    protected void rotateObjectTowardsAngle(double calculatedAngle, boolean crop) {
        if (ImageRotator.getInstance().isBlockedFromRotating(this.getImageEnum())) {
            return;
        }

        if (this.rotationAngle != calculatedAngle) {
            if (this.animation != null) {
                this.animation.rotateAnimation(calculatedAngle, crop);
            } else if (this.image != null) {
                this.image = ImageRotator.getInstance().rotateOrFlip(this.originalImage, calculatedAngle, crop);
                if (this.scale != 1) {
                    this.setScale(this.scale);
                }

                super.recalculateBoundsAndSize();
            }

            this.rotationAngle = calculatedAngle;
        }
    }

    private void rotateGameObjectSpriteAnimations(SpriteAnimation sprite, Direction direction, boolean crop) {
        if (sprite != null) {
            sprite.rotateAnimation(direction, crop);
        }
    }

    public void rotateAfterMovement() {
        if (!this.allowedVisualsToRotate) {
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

    public void rotateObjectTowardsDestination(boolean crop) {
        if (!this.isAllowedVisualsToRotate()) {
            return;
        }

        Point destination = null;

        if (movementConfiguration.getCurrentPath() != null && !movementConfiguration.getCurrentPath().getWaypoints().isEmpty()) {
            destination = movementConfiguration.getCurrentPath().getWaypoints().get(movementConfiguration.getCurrentPath().getWaypoints().size() - 1);
        } else if (movementConfiguration.getDestination() != null) {
            destination = movementConfiguration.getDestination();
        }

        destination = adjustDestinationForRotationUsingDimensions(this, destination);
        this.rotateGameObjectTowards(destination.getX(), destination.getY(), crop);
    }

    public void rotateObjectTowardsRotation(boolean crop) {
        if (this.isAllowedVisualsToRotate() && this.getMovementConfiguration().getRotation() != null) {
            this.rotateGameObjectTowards(this.getMovementConfiguration().getRotation(), crop);
        }
    }

    protected void rotateObjectTowardsPoint(Point point, boolean crop) {
        if (this.isAllowedVisualsToRotate()) {
            this.rotateGameObjectTowards(point.getX(), point.getY(), crop);
        }
    }

    private Point adjustDestinationForRotationUsingDimensions(GameObject gameObject, Point destination) {
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
    public void onCreationEffects() {
        //Exist to be overriden
    }


    //-------------------------------------getters and setters below------------

    public SpriteAnimation getDestructionAnimation() {
        return destructionAnimation;
    }

    public void setDestructionAnimation(SpriteAnimation destructionAnimation) {
        this.destructionAnimation = destructionAnimation;
    }

    public Point getCurrentLocation() {
        if (this.movementConfiguration == null) {
            return new Point(this.xCoordinate, this.yCoordinate);
        }
        return this.movementConfiguration.getCurrentLocation();
    }

    public int getCurrentBoardBlock() {
        return this.currentBoardBlock;
    }

    public void updateCurrentLocation(Point newLocation) {
        this.movementConfiguration.setCurrentLocation(newLocation);
    }

    public SpriteAnimation getShieldDamagedAnimation() {
        return shieldDamagedAnimation;
    }

    public void setShieldDamagedAnimation(SpriteAnimation shieldDamagedAnimation) {
        this.shieldDamagedAnimation = shieldDamagedAnimation;
    }

    public SpriteAnimation getExhaustAnimation() {
        return exhaustAnimation;
    }

    public void setExhaustAnimation(SpriteAnimation exhaustAnimation) {
        this.exhaustAnimation = exhaustAnimation;
    }

    public boolean isShowHealthBar() {
        return showHealthBar;
    }

    public void setShowHealthBar(boolean showHealthBar) {
        this.showHealthBar = showHealthBar;
    }

    public AudioEnums getDeathSound() {
        return deathSound;
    }

    public void setDeathSound(AudioEnums deathSound) {
        this.deathSound = deathSound;
    }

    public float getCurrentHitpoints() {
        if (currentHitpoints < 0) {
            currentHitpoints = 0;
        }
        return currentHitpoints;
    }

    public void setCurrentHitpoints(float currentHitpoints) {
        this.currentHitpoints = currentHitpoints;
    }

    public float getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(float maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public float getCurrentShieldPoints() {
        return currentShieldPoints;
    }

    public void setCurrentShieldPoints(float currentShieldPoints) {
        this.currentShieldPoints = currentShieldPoints;
    }

    public float getMaxShieldPoints() {
        return maxShieldPoints;
    }

    public void setMaxShieldPoints(float maxShieldPoints) {
        this.maxShieldPoints = maxShieldPoints;
    }

    public boolean isHasAttack() {
        return hasAttack;
    }

    public void setHasAttack(boolean hasAttack) {
        this.hasAttack = hasAttack;
    }

    public boolean isAllowedToDealDamage() {
        return allowedToDealDamage;
    }

    public void setAllowedToDealDamage(boolean allowedToDealDamage) {
        this.allowedToDealDamage = allowedToDealDamage;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    public GameObject getObjectToCenterAround() {
        return objectToCenterAround;
    }

    public void setObjectToCenterAround(GameObject objectToCenterAround) {
        this.objectToCenterAround = objectToCenterAround;
    }

    public GameObject getObjectToFollow() {
        return objectToFollow;
    }

    public void setObjectToFollow(GameObject objectToFollow) {
        this.objectToFollow = objectToFollow;
        this.movementConfiguration.setTargetToChase(objectToFollow);
    }

    public MovementConfiguration getMovementConfiguration() {
        return movementConfiguration;
    }

    public void setMovementConfiguration(MovementConfiguration movementConfiguration) {
        this.movementConfiguration = movementConfiguration;
    }

    public List<GameObject> getObjectsFollowingThis() {
        return objectsFollowingThis;
    }

    public void addFollowingGameObject(GameObject followingObject) {
        this.objectsFollowingThis.add(followingObject);
    }

    public SpriteAnimation getAnimation() {
        return this.animation;
    }

    public void setAnimation(SpriteAnimation spriteAnimation) {
        this.animation = spriteAnimation;
    }

    public float getScale() {
        if (this.animation != null) {
            return animation.getScale();
        }
        return scale;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public void setScale(float newScale) {
        if (newScale == this.scale) {
            return;
        }


        this.scale = newScale;
        if (this.animation != null) {
            this.animation.setAnimationScale(scale);
        } else {
            this.image = ImageResizer.getInstance().getScaledImage(image, scale);
            configureImageDimensions();
            super.recalculateBoundsAndSize();
        }
    }

    public void setPathFinder(PathFinder newPathFinder) {
        if (this.movementConfiguration != null) {
            this.movementConfiguration.setPathFinder(newPathFinder);
        } else {
            System.out.println("Tried to adjust a PathFinder without having an existing MovementConfiguration");
        }
    }

    public void updateBoardBlock() {
        if (this.animation != null) {
            this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(animation.getXCoordinate());
        } else {
            this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        }
    }

    public List<GameObject> getObjectOrbitingThis() {
        return objectOrbitingThis;
    }


    public boolean isBoxCollision() {
        return boxCollision;
    }

    public void setBoxCollision(boolean boxCollision) {
        this.boxCollision = boxCollision;
    }

    public double getLastGameSecondDamageTaken() {
        return lastGameSecondDamageTaken;
    }

    public void setLastGameSecondDamageTaken(long lastGameSecondDamageTaken) {
        this.lastGameSecondDamageTaken = lastGameSecondDamageTaken;
    }

    public GameObject getOwnerOrCreator() {
        return ownerOrCreator;
    }

    public void setOwnerOrCreator(GameObject ownerOrCreator) {
        this.ownerOrCreator = ownerOrCreator;
    }

    public float getBaseArmor() {
        return baseArmor;
    }

    public void setBaseArmor(float baseArmor) {
        this.baseArmor = baseArmor;
    }

    public float getArmorBonus() {
        return armorBonus;
    }

    public void adjustArmorBonus(float amount) {
        this.armorBonus += amount; // Modify the armor bonus by the given amount
    }

    public float getCashMoneyWorth() {
        return cashMoneyWorth;
    }

    public void setCashMoneyWorth(float cashMoneyWorth) {
        this.cashMoneyWorth = cashMoneyWorth;
    }

    public float getXpOnDeath() {
        return xpOnDeath;
    }

    public void setXpOnDeath(float xpOnDeath) {
        this.xpOnDeath = xpOnDeath;
    }

    public boolean isAllowedToMove() {
        return allowedToMove;
    }

    public void setAllowedToMove(boolean allowedToMove) {
        this.allowedToMove = allowedToMove;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public boolean isAllowedVisualsToRotate() {
        return allowedVisualsToRotate;
    }

    public void setAllowedVisualsToRotate(boolean allowedVisualsToRotate) {
        this.allowedVisualsToRotate = allowedVisualsToRotate;
    }

    public boolean isCenteredAroundObject() {
        return centeredAroundObject;
    }

    public void setCenteredAroundObject(boolean centeredAroundObject) {
        this.centeredAroundObject = centeredAroundObject;
    }

    @Override
    public void setCenterCoordinates(int newXCoordinate, int newYCoordinate) {
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

    @Override
    public void addYOffset(int yoffset) {
        if (this.animation != null) {
            this.animation.addYOffset(yoffset);
        }
        this.yOffset = yoffset;
    }

    @Override
    public void addXOffset(int xOffset) {
        if (this.animation != null) {
            this.animation.addXOffset(xOffset);
        }
        this.xOffset = xOffset;
    }


    public float getAttackSpeed() {
        float baseAttackSpeed = this.attackSpeed; // The default cooldown in milliseconds
        float attackSpeedBonus = this.attackSpeedBonusPercentage; // Total attack speed modifier applied

        if (attackSpeedBonus <= -100) { // Prevent division by zero or negative scaling
            return baseAttackSpeed * 3.0f; // Clamp to maximum slowdown
        }

        float newAttackSpeed = baseAttackSpeed / (1 + attackSpeedBonus / 100); // Adjusted calculation

        // Minimum threshold to prevent the attack speed from becoming too fast
        if (newAttackSpeed < 0.03f) {
            newAttackSpeed = 0.03f;
        }

        // Maximum threshold to prevent attack speed from being too slow
        if (newAttackSpeed > baseAttackSpeed * 3.0f) {
            newAttackSpeed = baseAttackSpeed * 3.0f;
        }

        return newAttackSpeed;
    }


    public float getDamage() {
        float attackDamage = this.damage * this.bonusDamageMultiplier;
        if (isACrit) {
            attackDamage *= PlayerStats.getInstance().getCriticalStrikeDamageMultiplier();
        }
        if (attackDamage < 0.1) {
            return 0.1f;
        } else {
            return attackDamage;
        }
    }


    public void modifyBonusDamageMultiplier(float bonusMultiplier) {
        this.bonusDamageMultiplier += bonusMultiplier;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void modifyAttackSpeedBonus(float bonusPercentage) {
        this.attackSpeedBonusPercentage += bonusPercentage;
    }

    public void modifyMovementSpeedModifier(float bonusSpeed) {
        this.movementConfiguration.modifyMovementSpeedModifier(bonusSpeed);
    }

    public int getCenterXCoordinate() {
        if (this.animation != null) {
            return animation.getXCoordinate() + xOffset + (animation.getWidth() / 2);
        } else
            return xCoordinate + xOffset + (width / 2);
    }

    public int getCenterYCoordinate() {
        if (this.animation != null) {
            return animation.getYCoordinate() + yOffset + (animation.getHeight() / 2);
        } else
            return yCoordinate + yOffset + (height / 2);
    }


    public int getWidth() {
        if (this.animation != null) {
            return animation.getWidth();
        } else
            return this.width;

    }

    public int getHeight() {
        if (this.animation != null) {
            return animation.getHeight();
        } else
            return this.height;
    }

    public void setTransparancyAlpha(boolean shouldIncrease, float newAlphaTransparancy, float transparacyStepSize) {
        if (this.animation != null) {
            this.animation.setTransparancyAlpha(shouldIncrease, newAlphaTransparancy, transparacyStepSize);
            super.setTransparancyAlpha(shouldIncrease, newAlphaTransparancy, transparacyStepSize);
        } else {
            super.setTransparancyAlpha(shouldIncrease, newAlphaTransparancy, transparacyStepSize);
        }
    }

    public float getTransparancyAlpha() {
        if (this.animation != null) {
            return animation.getTransparancyAlpha();
        } else {
            return transparancyAlpha;
        }
    }

    public boolean isACrit() {
        return isACrit;
    }

    public void setACrit(boolean ACrit) {
        isACrit = ACrit;
    }

    public void addEffectToApply(EffectInterface effectInterface) {
        if (!effectsToApply.contains(effectInterface)) {
            this.effectsToApply.add(effectInterface);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public ImageEnums getImageEnum() {
        if (this.animation != null) {
            return this.animation.getImageEnum();
        }
        return this.imageEnum;
    }

    public Rectangle getBounds() {
        if (this.animation != null) {
            return animation.getBounds();
        }
        return super.getBounds();
    }

    public List<EffectInterface> getEffects() {
        return effects;
    }

    public int getKnockbackStrength() {
        return knockbackStrength;
    }

    public void setKnockbackStrength(int knockbackStrength) {
        this.knockbackStrength = knockbackStrength;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public VisualLayer getVisualLayer() {
        return visualLayer;
    }

    public boolean isShowDamage() {
        return showDamage;
    }

    public void setShowDamage(boolean showDamage) {
        this.showDamage = showDamage;
    }

    public double getLastTimeDamageTakenFromLaserbeams() {
        return lastTimeDamageTakenFromLaserbeams;
    }

    public void setLastTimeDamageTakenFromLaserbeams(double lastTimeDamageTakenFromLaserbeams) {
        this.lastTimeDamageTakenFromLaserbeams = lastTimeDamageTakenFromLaserbeams;
    }

    public float getDamageReductionMultiplier() {
        return damageReductionMultiplier;
    }

    public void modifyDamageReductionMultiplier(float damageReductionMultiplier) {
        this.damageReductionMultiplier += damageReductionMultiplier;
    }

    public void modifyArmorBonus(float amount) {
        this.armorBonus += amount;
    }

    public boolean hasEffect(EffectIdentifiers effectIdentifiers){
        return this.effects.stream().anyMatch(effect -> effect.getEffectIdentifier().equals(effectIdentifiers));
    }
}
