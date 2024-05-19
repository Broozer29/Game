package game.objects.missiles;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import VisualAndAudioData.image.ImageEnums;
import game.gamestate.GameStateInfo;
import game.items.PlayerInventory;
import game.items.effects.EffectActivationTypes;
import game.items.effects.effecttypes.AttackSpeedModifierEffect;
import game.items.effects.effecttypes.DamageModifierEffect;
import game.items.enums.ItemEnums;
import game.managers.AnimationManager;
import game.objects.GameObject;
import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.objects.enemies.enemytypes.AlienBomb;
import game.objects.missiles.missiletypes.TazerProjectile;
import game.objects.player.spaceship.SpaceShip;
import game.util.CollisionDetector;
import game.objects.player.PlayerManager;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.missiles.specialAttacks.SpecialAttack;
import game.objects.missiles.missiletypes.Rocket1;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;


public class MissileManager {

    private static MissileManager instance = new MissileManager();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private PlayerManager playerManager = PlayerManager.getInstance();
    private CollisionDetector collisionDetector = CollisionDetector.getInstance();
    private CopyOnWriteArrayList<Missile> missiles = new CopyOnWriteArrayList<Missile>();
    private CopyOnWriteArrayList<SpecialAttack> specialAttacks = new CopyOnWriteArrayList<SpecialAttack>();

    private List<String> blackListedOnHitEffectActivatorObjects = Arrays.asList(new String[]{"Plasma Launcher Missile", "Drone Missile"});

    private MissileManager () {
    }

    public static MissileManager getInstance () {
        return instance;
    }

    public void resetManager () {

        for (Missile missile : missiles) {
            missile.setVisible(false);
        }

        for (SpecialAttack specialAttack : specialAttacks) {
            specialAttack.setVisible(false);
        }

        removeInvisibleProjectiles();

        missiles = new CopyOnWriteArrayList<Missile>();
        specialAttacks = new CopyOnWriteArrayList<SpecialAttack>();
    }


    private void initManagersIfNull () {
        if (enemyManager == null) {
            enemyManager = EnemyManager.getInstance();
        }

        if (animationManager == null) {
            animationManager = AnimationManager.getInstance();
        }

        if (playerManager == null) {
            playerManager = PlayerManager.getInstance();
        }
    }

    public void updateGameTick () {
        initManagersIfNull();
        moveMissiles();
        removeInvisibleProjectiles();
        checkMissileCollisions();
        triggerMissileActions();
    }

    private void removeInvisibleProjectiles () {
        for (int i = 0; i < missiles.size(); i++) {
            if (!missiles.get(i).isVisible()) {
                missiles.get(i).deleteObject();
                missiles.remove(i);
                i--;
            }
        }

        for (int i = 0; i < specialAttacks.size(); i++) {
            if (!specialAttacks.get(i).isVisible()) {
                specialAttacks.get(i).deleteObject();
                specialAttacks.remove(i);
                i--;
            }
        }


    }

    private void moveMissiles () {
        for (int i = missiles.size() - 1; i >= 0; i--) {
            if (missiles.get(i).isVisible()) {
                missiles.get(i).move();
                missiles.get(i).updateGameObjectEffects();
            }
        }
    }

    private void checkMissileCollisions () {
        for (Missile missile : missiles) {
            if (missile.isVisible()) {
                if(missile.getMissileType().equals(MissileTypeEnums.TazerProjectile)){ //Check special interactions first currently only tazers
                    checkMissileCollisionWithPlayer(missile);
                    checkMissileCollisionWithEnemies(missile);
                } else if (missile.isFriendly()) {  //Then generic friendly missiles on enemies
                    checkMissileCollisionWithEnemies(missile);
                } else { // Then generic enemy missiles on friendlies
                    checkMissileCollisionWithPlayer(missile);
                }

                if(missile.isDestroysMissiles()){
                    checkMissileCollisionWithMissiles(missile);
                }
            }
        }

        // Handle special attacks
        for (SpecialAttack specialAttack : specialAttacks) {
            if (specialAttack.getAnimation().isVisible()) {
                if (specialAttack.isFriendly()) {
                    checkSpecialAttackWithEnemyCollision(specialAttack);
                    checkSpecialAttackWithEnemyMissileCollision(specialAttack);
                } else {
                    checkEnemySpecialAttackCollision(specialAttack);
                    checkEnemySpecialAttackMissileCollision(specialAttack);
                }
            }
        }
    }

    private void checkEnemySpecialAttackCollision (SpecialAttack specialAttack) {
        if (specialAttack.getSpecialAttackMissiles().isEmpty()) {
            if (collisionDetector.detectCollision(PlayerManager.getInstance().getSpaceship(), specialAttack)) {
                if (specialAttack.isAllowedToDealDamage()) {
                    PlayerManager.getInstance().getSpaceship().takeDamage(specialAttack.getDamage());
                }

                if (!specialAttack.isAllowRepeatedDamage()) {
                    specialAttack.setAllowedToDealDamage(false);
                }
            }
        }
    }

    private void checkEnemySpecialAttackMissileCollision (SpecialAttack specialAttack) {
        if (specialAttack.getSpecialAttackMissiles().isEmpty()) {
            for (Missile missile : missiles) {
                if (missile.isFriendly()) {
                    if (collisionDetector.detectCollision(missile, specialAttack)) {
                        handleMissileDestruction(missile);
                    }
                }
            }
        }
    }

    // Checks collision between special attacks and enemies
    private void checkSpecialAttackWithEnemyCollision (SpecialAttack specialAttack) {
//        for (Missile missile : specialAttack.getSpecialAttackMissiles()) {
//            for (Enemy enemy : enemyManager.getEnemies()) {
//                if (collisionDetector.detectCollision(missile, enemy)) {
//                    enemy.takeDamage(specialAttack.getDamage());
//                }
//            }
//        }
        boolean hasAppliedEffects = false;


        for (Enemy enemy : enemyManager.getEnemies()) {
            if (collisionDetector.detectCollision(enemy, specialAttack)) {
                applyDamageModification(specialAttack, enemy);
                enemy.takeDamage(specialAttack.getDamage());
                if(specialAttack.isAllowOnHitEffects() && specialAttack.canApplyEffectAgain()) {
                    applyEffectsWhenPlayerHitsEnemy(enemy, specialAttack);
                    hasAppliedEffects = true;
                }
            }
        }

        if(hasAppliedEffects){
            specialAttack.setLastOnHitInterval(GameStateInfo.getInstance().getGameSeconds());
        }
    }


    // Checks collision between special attacks and enemy missiles
    private void checkSpecialAttackWithEnemyMissileCollision (SpecialAttack specialAttack) {
        if (specialAttack.getSpecialAttackMissiles().isEmpty()) {
            for (Missile missile : missiles) {
                if (!missile.isFriendly()) {
                    if (collisionDetector.detectCollision(missile, specialAttack)) {
                        handleMissileDestruction(missile);
                    }
                }
            }
        }
    }

    private void checkMissileCollisionWithEnemies (Missile missile) {
        for (Enemy enemy : enemyManager.getEnemies()) {
            if (collisionDetector.detectCollision(missile, enemy)) {
                if(missile.getMissileType().equals(MissileTypeEnums.TazerProjectile)){
                    handleTazerProjectile(missile, enemy);
                } else { //It's a player missile
                    applyDamageModification(missile, enemy);
                    handleCollision(enemy, missile);
                    applyEffectsWhenPlayerHitsEnemy(enemy, missile);
                }

            }
        }
    }


    private void checkMissileCollisionWithPlayer (Missile missile) {
        if (collisionDetector.detectCollision(missile, playerManager.getSpaceship())) {

            if(missile.getMissileType().equals(MissileTypeEnums.TazerProjectile)){
                handleTazerProjectile(missile, playerManager.getSpaceship());
            }

            playerManager.getSpaceship().takeDamage(missile.getDamage());
            applyEffectsWhenPlayerTakesDamage();
            handleMissileDestruction(missile);
        }
    }

    private void checkMissileCollisionWithMissiles(Missile missile){
        if(missile.isDestroysMissiles()){
            if(missile.isFriendly()){
                //Check for all non-friendly missiles in the missile list, this is used by the player
                for(Missile enemyMissile : missiles){
                    if(!enemyMissile.isFriendly() && collisionDetector.detectCollision(missile, enemyMissile)){
                        handleMissileDestruction(enemyMissile);
                    }
                }
            } else {
                //Check for all friendly missiles in the missile list, this is used by the enemies
                for(Missile enemyMissile : missiles){
                    if(enemyMissile.isFriendly() &&collisionDetector.detectCollision(missile, enemyMissile)){
                        handleMissileDestruction(enemyMissile);
                    }
                }
            }
        }
    }

    private void applyEffectsWhenPlayerTakesDamage () {
        List<Item> onHitItems = PlayerInventory.getInstance().getItemByActivationTypes(EffectActivationTypes.OnPlayerHit);
        for (Item item : onHitItems) {
            item.applyEffectToObject(PlayerManager.getInstance().getSpaceship());
        }
    }

    private void applyEffectsWhenPlayerHitsEnemy (Enemy enemy, GameObject source) {
        List<Item> onHitItems = PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.AfterCollision);
        for (Item item : onHitItems) {
            if(item.getItemName().equals(ItemEnums.PlasmaLauncher) && blackListedOnHitEffectActivatorObjects.contains(source.getObjectType())){
                continue;
            }
            item.applyEffectToObject(enemy);
        }
    }

    private void handleTazerProjectile(GameObject missile, GameObject target){
        if(missile instanceof TazerProjectile){
            ((TazerProjectile) missile).handleTazerMissile(missile, target);
        }
    }


    private void handleCollision (Enemy enemy, Missile missile) {
        if (missile instanceof Rocket1) {
            missile.missileAction();
            missile.setVisible(false);
        } else {
            enemy.takeDamage(missile.getDamage());
            missile.setVisible(false);
            if (missile.getDestructionAnimation() != null) {
                animationManager.addUpperAnimation(missile.getDestructionAnimation());
            }
        }
    }



    private void handleMissileDestruction (Missile missile) {
//        if(missile.getMissileType().equals(MissileTypeEnums.BarrierProjectile)){
//            return;
//        }

        missile.setVisible(false);
        if (missile.getDestructionAnimation() != null) {
            centerDestructionAnimation(missile);
            animationManager.addUpperAnimation(missile.getDestructionAnimation());
        }
    }

    //Needed to center the animation of the destruction around the projectile
    private void centerDestructionAnimation (Missile missile) {
        if (missile.getAnimation() != null) {
            missile.getDestructionAnimation().setOriginCoordinates(missile.getAnimation().getCenterXCoordinate(), missile.getAnimation().getCenterYCoordinate());
        } else {
            missile.getDestructionAnimation().setOriginCoordinates(missile.getCenterXCoordinate(), missile.getCenterYCoordinate());
        }
    }

    private void triggerMissileActions () {
        for (Missile missile : missiles) {
//            if (missile instanceof Rocket1) {
            missile.missileAction();
//            }
        }
    }

    public void addSpecialAttack (SpecialAttack specialAttack) {
        this.specialAttacks.add(specialAttack);
    }

    public List<Missile> getMissiles () {
        return missiles;
    }

    public List<SpecialAttack> getSpecialAttacks () {
        return specialAttacks;
    }

    public void addExistingMissile (Missile missile) {
        this.missiles.add(missile);
    }

    private void applyDamageModification (GameObject attack, GameObject target) {
        for (Item item : PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.BeforeCollision)) {
            item.modifyAttackValues(attack, target);
        }
    }
}
