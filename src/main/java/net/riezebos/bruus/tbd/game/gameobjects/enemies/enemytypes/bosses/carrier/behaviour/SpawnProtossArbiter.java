package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.carrier.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;

public class SpawnProtossArbiter implements BossActionable {
    //Arbiter die de baas healed
    //Hoge cooldown zodat de speler hem moet prioritizen
    //Synergy met andere protoss ships, moeilijk deze te raken als de andere leven
    //Dubieus over deze ability
    private int priority = 10;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        return false;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return false;
    }

}
