package com.aoros.baggingproblem.strategy;

import com.aoros.baggingproblem.BaggingState;
import com.aoros.baggingproblem.GroceryItem;
import com.aoros.baggingproblem.PackingDefinition;
import com.aoros.baggingproblem.PackingUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class MrvLcvPackingStrategy implements PackingStrategy {

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

        // Sort the groceryItems list based on the Min Remaining Variables concept
        // Basically, bigger size + number of items in blacklist are sorted to the
        // the front of the list.
        groceryItems.sort((GroceryItem o1, GroceryItem o2) -> {
            int o1Val = o1.getSize() + o1.getBlacklistSize(numGroceryItems);
            int o2Val = o2.getSize() + o2.getBlacklistSize(numGroceryItems);
            return o2Val - o1Val;
        });

        Queue<BaggingState> pQueue = new PriorityQueue<>(new Comparator<BaggingState>() {
            @Override
            public int compare(BaggingState bs1, BaggingState bs2) {
                return (int) (bs2.getConstrainingValue() - bs1.getConstrainingValue());
            }
        });
        pQueue.add(PackingUtils.getNewEmptyBagsState(packingDefinition, numBagsAllowed));
        int totalNumberOfGroceryItems = groceryItems.size();

        int iters = 1;
        while (!pQueue.isEmpty()) {
            if (debug)
                PackingUtils.printQueue(pQueue, iters);

            BaggingState bagsState = pQueue.poll();

            if (isGoalStateReached(bagsState, totalNumberOfGroceryItems)) {
                solutions.add(bagsState);
                if (debug)
                    System.out.println("iters: " + iters);
                return solutions;
            }

            Set<BaggingState> states = new HashSet<>();
            int itemIndexToGet = bagsState.getNumItemsInBags();
            for (int i = 0; i < numBagsAllowed; i++) {
                BaggingState bagsStateCopy = bagsState.copyOf();
                GroceryItem item = groceryItems.get(itemIndexToGet);
                boolean didAddToBag = bagsStateCopy.getBags()[i].addItem(item);

                if (didAddToBag)
                    states.add(bagsStateCopy);
            }
            pQueue.addAll(states);
            zeroConstrainingValue(states);
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

    private void zeroConstrainingValue(Set<BaggingState> states) {
        for (BaggingState state : states) {
            state.setConstrainingValueToZero();
        }
    }
}
