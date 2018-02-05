package com.aoros.baggingproblem;

public class PackingUtils {

    public static BaggingState getNewEmptyBagsState(PackingDefinition packingDefinition, int numBagsAllowed) {
        BaggingState bags = new BaggingState(packingDefinition);
        for (int i = 0; i < numBagsAllowed; i++) {
            bags.add(new Bag(packingDefinition), i);
        }
        return bags;
    }
}
