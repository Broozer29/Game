package net.riezebos.bruus.tbd.game.items.items.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContractHelper {

    private static ContractHelper contractHelper = new ContractHelper();

    private List<ContractCounter> activeContracts = new ArrayList<>();


    private ContractHelper () {
    }

    public static ContractHelper getInstance () {
        return contractHelper;
    }

    public void addContract (ContractCounter contractCounter) {
        activeContracts.add(contractCounter);
    }

    public List<ContractCounter> getFinishedContracts () {
        return activeContracts.stream().filter(contractCounter -> contractCounter.isFinished()).collect(Collectors.toList());
    }

    public void removeContract (ContractCounter contractCounter) {
        if (activeContracts.contains(contractCounter)) {
            activeContracts.remove(contractCounter);
        }
    }
}
