package com.aoros.baggingproblem.strategy;

import com.aoros.baggingproblem.BaggingState;
import com.aoros.baggingproblem.GroceryItem;
import com.aoros.baggingproblem.PackingDefinition;
import com.aoros.baggingproblem.PackingUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstPackingStrategy implements PackingStrategy {

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

        Queue<BaggingState> queue = new LinkedList();
        queue.add(PackingUtils.getNewEmptyBagsState(packingDefinition, numBagsAllowed));

        int iters = 1;
        while (!queue.isEmpty()) {
            if (debug)
                PackingUtils.printQueue(queue, iters);

            BaggingState bagsState = queue.remove();
            int numberOfItemsInBags = bagsState.getNumItemsInBags();
            int itemIndexToGet = numberOfItemsInBags;
            if (numberOfItemsInBags == totalNumberOfGroceryItems) {
                if (debug)
                    System.out.println("iters: " + iters);
                solutions.add(bagsState);
            } else {
                for (int i = 0; i < numBagsAllowed; i++) {
                    BaggingState bagsStateCopy = bagsState.copyOf();
                    GroceryItem item = groceryItems.get(itemIndexToGet);
                    boolean didAddToBag = bagsStateCopy.getBags()[i].addItem(item);
                    if (didAddToBag) {
                        queue.add(bagsStateCopy);
                    }
                }
            }
            iters++;
        }

        return solutions;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
