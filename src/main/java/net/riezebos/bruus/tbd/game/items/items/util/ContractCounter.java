package net.riezebos.bruus.tbd.game.items.items.util;

import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;

import java.util.Objects;

public class ContractCounter {
    private int startCount;
    private static int requiredKills = 1;

    public ContractCounter () {
        startCount = GameStatsTracker.getInstance().getEnemiesKilled();
    }

    public int getStartCount () {
        return startCount;
    }

    public boolean isFinished(){
        System.out.println("Contract status: " + GameStatsTracker.getInstance().getEnemiesKilled() + " out of: " + (this.startCount + requiredKills));
        return (this.startCount + requiredKills) <= GameStatsTracker.getInstance().getEnemiesKilled();
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractCounter that = (ContractCounter) o;
        return startCount == that.startCount;
    }

    @Override
    public int hashCode () {
        return Objects.hash(startCount);
    }
}
