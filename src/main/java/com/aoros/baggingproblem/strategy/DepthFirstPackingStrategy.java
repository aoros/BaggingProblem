package com.aoros.baggingproblem.strategy;

import com.aoros.baggingproblem.BaggingState;
import com.aoros.baggingproblem.GroceryItem;
import com.aoros.baggingproblem.PackingDefinition;
import com.aoros.baggingproblem.PackingUtils;
import java.util.ArrayList;
import java.util.List;
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

            BaggingState bagsState = stack.pop();
            int numberOfItemsInBags = bagsState.getNumItemsInBags();
            int itemIndexToGet = numberOfItemsInBags;
            if (numberOfItemsInBags == totalNumberOfGroceryItems) {
                solutions.add(bagsState);
                if (debug) {
                    System.out.println(iters + " ");
                }
                return solutions;
            }

            for (int i = 0; i < numBagsAllowed; i++) {
                BaggingState bagsStateCopy = bagsState.copyOf();
                GroceryItem item = groceryItems.get(itemIndexToGet);
                boolean didAddToBag = bagsStateCopy.getBags()[i].addItem(item);
                if (didAddToBag) {
                    stack.add(bagsStateCopy);
                }
            }
            iters++;
        }

        if (debug) {
            System.out.println(iters + " ");
        }
        return solutions;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
