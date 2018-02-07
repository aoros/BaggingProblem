package com.aoros.baggingproblem.strategy;

import com.aoros.baggingproblem.BaggingState;
import com.aoros.baggingproblem.GroceryItem;
import com.aoros.baggingproblem.PackingDefinition;
import com.aoros.baggingproblem.PackingUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class DepthFirstPackingStrategy implements PackingStrategy {

    private boolean debug = false;
    private PackingDefinition packingDefinition;
    private Integer numBagsAllowed;

    @Override
    public void setPackingDefinition(PackingDefinition packingDefinition) {
        this.packingDefinition = packingDefinition;
        this.numBagsAllowed = packingDefinition.getNumAvailableBags();
    }

    @Override
    public List<BaggingState> packBags() {
        List<BaggingState> solutions = new ArrayList<>();
        List<GroceryItem> groceryItems = packingDefinition.getGroceryItems();
        int totalNumberOfGroceryItems = groceryItems.size();

        Stack<BaggingState> stack = new Stack<>();
        stack.push(PackingUtils.getNewEmptyBagsState(packingDefinition, numBagsAllowed));

        int iters = 1;
        while (!stack.isEmpty()) {
//            if (debug)
//                PackingUtils.printStack(stack, iters);

            BaggingState bagsState = stack.pop();
            int numberOfItemsInBags = bagsState.getNumItemsInBags();

            if (numberOfItemsInBags == totalNumberOfGroceryItems) {
                solutions.add(bagsState);
//                if (debug)
//                    System.out.println("iters: " + iters);
                return solutions;
            }

            Set<BaggingState> states = new HashSet<>();
            int itemIndexToGet = numberOfItemsInBags;
            for (int i = 0; i < numBagsAllowed; i++) {
                BaggingState bagsStateCopy = bagsState.copyOf();
                GroceryItem item = groceryItems.get(itemIndexToGet);
                boolean didAddToBag = bagsStateCopy.getBags()[i].addItem(item);

                if (didAddToBag)
                    states.add(bagsStateCopy);
            }
            stack.addAll(states);
            iters++;
        }

        return solutions;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
