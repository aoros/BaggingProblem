package com.aoros.baggingproblem.strategy;

import com.aoros.baggingproblem.Bag;
import com.aoros.baggingproblem.BaggingState;
import com.aoros.baggingproblem.PackingDefinition;
import java.util.List;

public interface PackingStrategy {

    public void setPackingDefinition(PackingDefinition packingDefinition);

    public List<BaggingState> packBags();
    
    public void setDebug(boolean debug);

    public default void print(BaggingState bagsState) {
        System.out.println("==================");
        for (Bag b : bagsState.getBags()) {
            System.out.println(b);
        }
        System.out.println("==================");
    }
}
