package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.behaviour.TwinBossBoxManouvre;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.behaviour.TwinBossLaserbeamCentreManouvre;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.behaviour.TwinBossLeftRightManouvre;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.behaviour.TwinBossQuickMissileAttack;
import net.riezebos.bruus.tbd.game.gamestate.GameState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TwinBossManager {

    private List<TwinBoss> twinBossList = new ArrayList<>();
    private static TwinBossManager instance = new TwinBossManager();

    private float twinBossCurrentHealth = 0;
    private float twinBossMaxHealth = 0;
    public static final int twinCount = 4;
    private List<BossActionable> bossBehaviourList = new ArrayList<>();
    private static boolean hasGrantedKillRewards = false;
    private BossActionable lastUsedBossActionable = null;

    private double finishedAttackTime = 0;

    public static TwinBossManager getInstance() {
        return instance;
    }

    private TwinBossManager() {
        resetTwinBossManager();
        twinBossList.clear();
        //These resets are required IF the bosses die whilst attacking, as the attacks will not be properly reset by the activateBehaviour method and they must be reset due to static variables
        TwinBossBoxManouvre.resetBehaviour();
        TwinBossLeftRightManouvre.resetBehaviour();
        TwinBossQuickMissileAttack.resetBehaviour();
        TwinBossLaserbeamCentreManouvre.resetBehaviour();
    }

    public static void setCooldownToPreventBackToBackTeleportBehaviour(BossActionable bossActionable) {

        double currentGameSeconds = GameState.getInstance().getGameSeconds() - 5;

        if (!(bossActionable instanceof TwinBossLeftRightManouvre)) {
            TwinBossLeftRightManouvre.lastAttackTime = currentGameSeconds;
        }
        if (!(bossActionable instanceof TwinBossBoxManouvre)) {
            TwinBossBoxManouvre.lastAttackTime = currentGameSeconds;
        }
        if (!(bossActionable instanceof TwinBossLaserbeamCentreManouvre)) {
            TwinBossLaserbeamCentreManouvre.lastAttackTime = currentGameSeconds;
        }
    }

    public void resetTwinBossManager() {
        twinBossList.clear();
        hasGrantedKillRewards = false;
        twinBossMaxHealth = 0;
        twinBossCurrentHealth = 0;

        //recreate behaviour for all enemies
        TwinBossBoxManouvre twinBossBoxManouvre = new TwinBossBoxManouvre();
        bossBehaviourList.add(twinBossBoxManouvre);

        TwinBossLeftRightManouvre twinBossLeftRightManouvre = new TwinBossLeftRightManouvre();
        bossBehaviourList.add(twinBossLeftRightManouvre);

        TwinBossQuickMissileAttack twinBossQuickMissileAttack = new TwinBossQuickMissileAttack();
        bossBehaviourList.add(twinBossQuickMissileAttack);

        TwinBossLaserbeamCentreManouvre twinBossLaserbeamCentreManouvre = new TwinBossLaserbeamCentreManouvre();
        bossBehaviourList.add(twinBossLaserbeamCentreManouvre);

        bossBehaviourList = bossBehaviourList.stream()
                .sorted(Comparator.comparingInt(BossActionable::getPriority).reversed())
                .collect(Collectors.toList());
    }

    public BossActionable getAvailableBossActionable() {
        //Manual cooldown between abilities, this helps with teleporting manouvres to finish before starting the next one
        if (finishedAttackTime + 1 <= GameState.getInstance().getGameSeconds()) {
            //Return the first available bossActionable but do not allow for repeated actions
            BossActionable chosenAction = bossBehaviourList.stream()
                    .filter(bossActionable -> bossActionable.isAvailable(null))
                    .filter(bossActionable -> lastUsedBossActionable == null
                            || !bossActionable.getClass().equals(lastUsedBossActionable.getClass()))
                    .findFirst()
                    .orElse(null);

            return chosenAction;
        } else return null;
    }

    public void clearAllTwinAttacks() {
        twinBossList.stream().forEach(twinBoss -> twinBoss.setCurrentActiveBehavior(null));
        finishedAttackTime = GameState.getInstance().getGameSeconds();
    }

    public float getTwinBossCurrentHealth() {
        return twinBossCurrentHealth;
    }

    public float getTwinBossMaxHealth() {
        return twinBossMaxHealth;
    }

    public void damageTwinBoss(float damage) {
        twinBossCurrentHealth -= damage;

        if (twinBossCurrentHealth >= twinBossMaxHealth) {
            twinBossCurrentHealth = twinBossMaxHealth;
        }


        if (twinBossCurrentHealth <= 0) {
            for (TwinBoss twinBoss : twinBossList) {
                twinBoss.killTwinBoss();
            }
        }
    }

    public void setTwinBossMaxHealth(float twinBossMaxHealth) {
        this.twinBossMaxHealth = twinBossMaxHealth;
    }

    public void setTwinBossCurrentHealth(float twinBossCurrentHealth) {
        this.twinBossCurrentHealth = twinBossCurrentHealth;
    }

    public void addTwinBoss(TwinBoss twinBoss) {
        twinBossList.add(twinBoss);
    }

    public static boolean isHasGrantedKillRewards() {
        return hasGrantedKillRewards;
    }

    public static void setHasGrantedKillRewards(boolean hasGrantedKillRewards) {
        TwinBossManager.hasGrantedKillRewards = hasGrantedKillRewards;
    }

    public BossActionable getLastUsedBossActionable() {
        return lastUsedBossActionable;
    }

    public void setLastUsedBossActionable(BossActionable lastUsedBossActionable) {
        //Exception for the missile attack, as quick missile attacks are supposed to be repeatable in succesion
        if (lastUsedBossActionable != null && !lastUsedBossActionable.getClass().equals(TwinBossQuickMissileAttack.class)) {
            this.lastUsedBossActionable = lastUsedBossActionable;
        }
    }
}
