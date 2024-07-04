package game.objects.missiles;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import game.gamestate.GameStateInfo;
import game.items.PlayerInventory;
import game.items.effects.EffectActivationTypes;
import game.managers.AnimationManager;
import game.items.Item;
import game.objects.missiles.missiletypes.TazerProjectile;
import game.objects.player.spaceship.SpaceShip;
import game.util.CollisionDetector;
import game.objects.player.PlayerManager;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.missiles.specialAttacks.SpecialAttack;


public class MissileManager {

    private static MissileManager instance = new MissileManager();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private PlayerManager playerManager = PlayerManager.getInstance();
    private CollisionDetector collisionDetector = CollisionDetector.getInstance();
    private CopyOnWriteArrayList<Missile> missiles = new CopyOnWriteArrayList<Missile>();
    private CopyOnWriteArrayList<SpecialAttack> specialAttacks = new CopyOnWriteArrayList<SpecialAttack>();


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
        for (Missile missile : missiles) {
            if (missile.isVisible()) {
                missile.move();
                missile.updateGameObjectEffects();
            }
        }
    }

    private void checkMissileCollisions () {
        for (Missile missile : missiles) {
            if (missile.isVisible()) {
                if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) { //Check special interactions first currently only tazers
                    checkMissileCollisionWithPlayer(missile);
                    checkMissileCollisionWithEnemies(missile);
                } else if (missile.isFriendly()) {  //Then generic friendly missiles on enemies
                    checkMissileCollisionWithEnemies(missile);
                } else { // Then generic enemy missiles on friendlies
                    checkMissileCollisionWithPlayer(missile);
                }

                if (missile.interactsWithMissiles()) {
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
                    specialAttack.checkEnemySpecialAttackCollision(PlayerManager.getInstance().getSpaceship());
                    checkEnemySpecialAttackMissileCollision(specialAttack);
                }
            }
        }
    }


    private void checkEnemySpecialAttackMissileCollision (SpecialAttack specialAttack) {
        if (specialAttack.getSpecialAttackMissiles().isEmpty()) {
            for (Missile missile : missiles) {
                if (missile.isFriendly()) {
                    if (collisionDetector.detectCollision(missile, specialAttack)) {
                        missile.destroyMissile();
                    }
                }
            }
        }
    }

    // Checks collision between special attacks and enemies
    private void checkSpecialAttackWithEnemyCollision (SpecialAttack specialAttack) {
        boolean hasAppliedEffects = false;
        for (Enemy enemy : enemyManager.getEnemies()) {
            if (collisionDetector.detectCollision(enemy, specialAttack)) {
                specialAttack.applyDamageModification(enemy);
                specialAttack.dealDamageToGameObject(enemy);
                if (specialAttack.isAllowOnHitEffects() && specialAttack.canApplyEffectAgain()) {
                    specialAttack.applyEffectsWhenPlayerHitsEnemy(enemy);
                    hasAppliedEffects = true;
                }
            }
        }

        if (hasAppliedEffects) {
            specialAttack.setLastOnHitInterval(GameStateInfo.getInstance().getGameSeconds());
        }
    }


    // Checks collision between special attacks and enemy missiles
    private void checkSpecialAttackWithEnemyMissileCollision (SpecialAttack specialAttack) {
        if (specialAttack.getSpecialAttackMissiles().isEmpty()) {
            for (Missile missile : missiles) {
                if (!missile.isFriendly()) {
                    if (collisionDetector.detectCollision(missile, specialAttack)) {
                        missile.destroyMissile();
                    }
                }
            }
        }
    }

    private void checkMissileCollisionWithEnemies (Missile missile) {
        for (Enemy enemy : enemyManager.getEnemies()) {
            if (collisionDetector.detectCollision(missile, enemy)) {
                if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) {
                    ((TazerProjectile) missile).handleTazerMissile(missile, enemy);
                } else { //It's a player missile
                    missile.handleCollision(enemy);
                    missile.applyEffectsWhenPlayerHitsEnemy(enemy);
                }

            }
        }
    }


    private void checkMissileCollisionWithPlayer (Missile missile) {
        SpaceShip spaceship = playerManager.getSpaceship();
        if (collisionDetector.detectCollision(missile, spaceship)) {
            if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) {
                ((TazerProjectile) missile).handleTazerMissile(missile, spaceship);
            }
            missile.handleCollision(spaceship);
//            missile.dealDamageToGameObject(spaceship);
            applyEffectsWhenPlayerTakesDamage();
//            missile.destroyMissile(); //Assumes enemy projectiles always get destroyed on contact
        }
    }

    private void checkMissileCollisionWithMissiles (Missile missile) {
        if (missile.interactsWithMissiles()) {
            if (missile.isFriendly()) {
                //Check for all non-friendly missiles in the missile list, this is used by the player
                for (Missile enemyMissile : missiles) {
                    if (!enemyMissile.isFriendly() && collisionDetector.detectCollision(missile, enemyMissile)) {


                        if (missile.isDeletesMissiles()) {
                            enemyMissile.destroyMissile();
                        }

                        else if(missile.isDestructable()){
                            missile.takeDamage(enemyMissile.getDamage());
                            missile.setShowHealthBar(true);
                            enemyMissile.destroyMissile();
                        }

                    }
                }
            } else {
                //Check for all friendly missiles in the missile list, this is used by the enemies
                for (Missile friendlyMissile : missiles) {
                    if (friendlyMissile.isFriendly() && collisionDetector.detectCollision(missile, friendlyMissile)) {


                        if (missile.isDeletesMissiles()) {
                            friendlyMissile.destroyMissile();
                        }

                        else if(missile.isDestructable()){
                            missile.takeDamage(friendlyMissile.getDamage());
                            missile.setShowHealthBar(true);
                            friendlyMissile.destroyMissile();
                        }
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

    private void triggerMissileActions () {
        for (Missile missile : missiles) {
            missile.missileAction();
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


}
