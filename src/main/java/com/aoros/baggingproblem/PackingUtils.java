package com.aoros.baggingproblem;

public class PackingUtils {

    public static BaggingConfiguration getNewEmptyBagsState(PackingDefinition packingDefinition, int numBagsAllowed) {
        BaggingConfiguration bags = new BaggingConfiguration(packingDefinition);
        for (int i = 0; i < numBagsAllowed; i++) {
            bags.add(new Bag(packingDefinition), i);
        }
        return bags;
    }
}
