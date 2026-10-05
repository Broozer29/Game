package net.riezebos.bruus.tbd.game.items.items.util;

import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.Contract;
import net.riezebos.bruus.tbd.game.items.items.HelpRequested;

import java.util.Objects;

public class ContractCounter {
    private int startCount;

    public ContractCounter () {
        if(PlayerInventory.getInstance().getContractType() == ContractType.DEFAULT) {
            startCount = GameStatsTracker.getInstance().getEnemiesKilled();
        } else if(PlayerInventory.getInstance().getContractType() == ContractType.HELP_REQUESTED){
            startCount = 0;
        }
    }

    public int getStartCount () {
        return startCount;
    }

    public boolean isFinished(){
        if(PlayerInventory.getInstance().getContractType() == ContractType.DEFAULT) {
            return (this.startCount + Contract.killCountRequired) <= GameStatsTracker.getInstance().getEnemiesKilled();
        } else if(PlayerInventory.getInstance().getContractType() == ContractType.HELP_REQUESTED){
            return GameState.getInstance().getMiniBossesDefeatedThisLevel() >= HelpRequested.miniBossesRequired;
        }
        return false;
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
