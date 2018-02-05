package com.aoros.baggingproblem.strategy;

import com.aoros.baggingproblem.Bag;
import com.aoros.baggingproblem.BaggingState;
import com.aoros.baggingproblem.GroceryItem;
import com.aoros.baggingproblem.PackingDefinition;
import com.aoros.baggingproblem.PackingUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MrvPackingStrategy implements PackingStrategy {

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
        int numGroceryItems = groceryItems.size();
        groceryItems.sort((GroceryItem o1, GroceryItem o2) -> {
            int o1Val = o1.getSize() + o1.getBlacklistSize(numGroceryItems);
            int o2Val = o2.getSize() + o2.getBlacklistSize(numGroceryItems);
            return o2Val - o1Val;
        });

        Stack<BaggingState> stack = new Stack<>();
        stack.push(PackingUtils.getNewEmptyBagsState(packingDefinition, numBagsAllowed));
        int totalNumberOfGroceryItems = groceryItems.size();

        int iters = 1;
        while (!stack.isEmpty()) {
            if (debug)
                printStack(stack, iters);

            BaggingState bagsState = stack.pop();

            if (isGoalStateReached(bagsState, totalNumberOfGroceryItems)) {
                solutions.add(bagsState);
                return solutions;
            }

            int itemIndexToGet = bagsState.getNumItemsInBags();
            for (int i = 0; i < numBagsAllowed; i++) {
                BaggingState bagsStateCopy = bagsState.copyOf();
                GroceryItem item = groceryItems.get(itemIndexToGet);
                boolean didAddToBag = bagsStateCopy.getBags()[i].addItem(item);
                
                if (didAddToBag)
                    stack.add(bagsStateCopy);
            }
            iters++;
        }

        return solutions;
    }

    private boolean isGoalStateReached(BaggingState bagsState, int totalNumberOfGroceryItems) {
        return bagsState.getNumItemsInBags() == totalNumberOfGroceryItems;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void printStack(Stack<BaggingState> stack, int iteration) {
        System.out.println("Stack Iteration " + iteration);
        for (int i = stack.size(); i > 0; i--) {
            System.out.println("   BaggingConfiguration Level " + i + ":");
            int b = 1;
            for (Bag bag : stack.get(i - 1).getBags()) {
                System.out.println("      Bag " + b + ": " + bag.getBagItemNames());
                b++;
            }
        }
        System.out.println("");
    }
}
