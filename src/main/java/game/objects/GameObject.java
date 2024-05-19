package game.objects;

import game.gamestate.GameStateInfo;
import game.items.PlayerInventory;
import game.items.effects.EffectActivationTypes;
import game.managers.AnimationManager;
import game.movement.*;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.HoverPathFinder;
import game.movement.pathfinders.PathFinder;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.image.ImageResizer;
import VisualAndAudioData.image.ImageRotator;
import game.items.effects.EffectInterface;
import game.objects.enemies.Enemy;
import game.objects.enemies.enemytypes.AlienBomb;
import game.objects.enemies.enums.EnemyEnums;
import game.objects.missiles.missiletypes.GenericMissile;
import game.objects.player.PlayerManager;
import game.objects.player.PlayerStats;
import game.spawner.LevelManager;
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
    protected int attackSpeedCurrentFrameCount;
    protected float attackSpeedBonusModifier;

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
        this.movementTracker = new MovementTracker();
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

        movementConfiguration.setStepsTaken(0);
        this.movementConfiguration = movementConfiguration;
    }

    public void resetMovementPath () {
//        if(movementConfiguration == null){
//            return;
//        }

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

    private EffectInterface getExistingEffect(EffectInterface effect) {
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
        if (movementConfiguration != null) {
            this.movementConfiguration.deleteConfiguration();
        }
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
        toggleHealthBar();
        SpriteMover.getInstance().moveGameObject(this, movementConfiguration);
        moveAnimations();
        this.bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
        updateBoardBlock();
        updateVisibility();

        if (this.movementConfiguration.getXMovementSpeed() == 0 && this.movementConfiguration.getYMovementSpeed() == 0) {
            this.rotationAngle = this.movementRotation.toAngle();
        }

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

    private void toggleHealthBar(){
        if(this.currentHitpoints < this.maxHitPoints){
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
    public void rotateGameObjectTowards (Direction direction, boolean crop) {
        if(this.rotationAngle == direction.toAngle()){
            return;
        }
        this.rotationAngle = direction.toAngle();

        if (this.animation != null) {
            rotateGameObjectSpriteAnimations(animation, direction, crop);
        }
        if (this.exhaustAnimation != null) {
            rotateGameObjectSpriteAnimations(animation, direction, crop);
        }

        if (this.destructionAnimation != null) {
            rotateGameObjectSpriteAnimations(animation, direction,crop);
        }
        if (this.image != null) {
            this.image = ImageRotator.getInstance().rotate(originalImage, direction, crop);
            super.recalculateBoundsAndSize();
        }
    }

    private void updateChargingAttackAnimationCoordination () {
        if (this.chargingUpAttackAnimation != null) {
            double baseDistance = (this.getWidth() + this.getHeight()) / 2.0;
            Point chargingUpLocation = ImageRotator.getInstance().calculateFrontPosition(this.getCenterXCoordinate(), this.getCenterYCoordinate(), rotationAngle, baseDistance);
            this.chargingUpAttackAnimation.setCenterCoordinates(chargingUpLocation.getX(), chargingUpLocation.getY());
        }

    }

    public void rotateGameObjectTowards (int targetXCoordinate, int targetYCoordinate, boolean crop) {
        double calculatedAngle = 0;
        if (this.animation != null) {
            calculatedAngle = ImageRotator.getInstance().calculateAngle(this.animation.getCenterXCoordinate(), this.animation.getCenterYCoordinate(), targetXCoordinate, targetYCoordinate);
        } else {
            calculatedAngle = ImageRotator.getInstance().calculateAngle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetXCoordinate, targetYCoordinate);
        }

        rotateObjectTowardsAngle(calculatedAngle, crop);

    }

    protected void rotateObjectTowardsAngle(double calculatedAngle, boolean crop){
        if (this.rotationAngle != calculatedAngle) {
            if (this.animation != null) {
                this.animation.rotateAnimation(calculatedAngle, crop);
            } else if (this.image != null) {
                this.image = ImageRotator.getInstance().rotateOrFlip(this.originalImage, calculatedAngle, crop);
                super.recalculateBoundsAndSize();
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

    private void rotateGameObjectSpriteAnimations (SpriteAnimation sprite, Direction direction, boolean crop) {
        if (sprite != null) {
            sprite.rotateAnimation(direction, crop);
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
            this.animation.setCenterCoordinates(newXCoordinate, newYCoordinate);
            this.currentLocation = new Point(animation.getXCoordinate(), animation.getYCoordinate());
        }
        //Maybe the other animations too? Requires testing but theoretically it should be fine if they are only updated when needed
    }


    public void rotateObject () {
//        if (movementConfiguration.getPathFinder() instanceof HoverPathFinder || movementConfiguration.getCurrentPath().getWaypoints().isEmpty()
//                || (movementConfiguration.getXMovementSpeed() == 0 && movementConfiguration.getYMovementSpeed() == 0)) {
            handleRotation();
//        }
    }

    protected void handleRotation () {
        if (this instanceof Enemy) {
            Enemy enemyObject = (Enemy) this;
            boolean crop = true;
            // Check for specific missile type pathfinders
            if (enemyObject.getMissileTypePathFinders() == PathFinderEnums.Homing
                    || enemyObject.getMissileTypePathFinders() == PathFinderEnums.StraightLine) {
                // Rotate towards the player, assuming these are only used for enemies that aim
                rotateObjectTowardsPoint(PlayerManager.getInstance().getSpaceship().getCurrentLocation(), crop);
                updateChargingAttackAnimationCoordination();
                return;
            }

            if(enemyObject.getEnemyType().equals(EnemyEnums.Alien_Bomb)){
                enemyObject.rotateGameObjectTowards(enemyObject.getOwnerOrCreator().getMovementConfiguration().getRotation(), true);
//                enemyObject.setAllowedVisualsToRotate(false);
                return;
            }
        }

        if (!movementConfiguration.getCurrentPath().getWaypoints().isEmpty()) {
            rotateObjectTowardsDestination(false);
//            rotateObjectTowardsRotation();
        } else {
            rotateObjectTowardsRotation(false);
        }
        updateChargingAttackAnimationCoordination();

    }

    protected void rotateObjectTowardsDestination (boolean crop) {
        if (this.isAllowedVisualsToRotate() && !movementConfiguration.getCurrentPath().getWaypoints().isEmpty()) {
            Point destination = movementConfiguration.getCurrentPath().getWaypoints().get(movementConfiguration.getCurrentPath().getWaypoints().size() - 1);
            destination = adjustDestinationForRotation(this, destination);
            this.rotateGameObjectTowards(destination.getX(), destination.getY(), crop);
        }
    }

    protected void rotateObjectTowardsRotation (boolean crop) {
        if (this.isAllowedVisualsToRotate() && this.getMovementConfiguration().getRotation() != null) {
//            if (this.getAnimation() != null) {
//                this.getAnimation().rotateAnimation(this.getMovementConfiguration().getRotation());
//            } else {

            this.rotateGameObjectTowards(this.getMovementConfiguration().getRotation(), crop);

//            }
        }
    }

    protected void rotateObjectTowardsPoint (Point point, boolean crop) {
        if (this.isAllowedVisualsToRotate()) {
            point = adjustDestinationForRotation(this, point);
            this.rotateGameObjectTowards(point.getX(), point.getY(), crop);
        }
    }


    private Point adjustDestinationForRotation (GameObject gameObject, Point destination) {
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
}
