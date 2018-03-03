package com.aoros.bagging.local.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import org.jamesframework.core.problems.datatypes.IntegerIdentifiedData;

public class BaggingData implements IntegerIdentifiedData {

    private final Set<Integer> IDs = new HashSet<>();
    private final Map<Integer, BaggingSolution> IDsToSolutionMap = new HashMap<>();
    private final BaggingItems items;

    public BaggingData(BaggingItems items) {
        this.items = items;
    }

    public BaggingSolution createRandomSolution() {
        Random r = new Random();

        BaggingSolution solution = new BaggingSolution(items);
        boolean allItemsAdded = false;
        int randSolutionCount = 0;
        while (!allItemsAdded) {
            allItemsAdded = true;
            for (Integer itemId : items.getItemIds()) {
                boolean addedToBag = false;
                int addToBagCount = 0;
                while (!addedToBag) {
                    int bagNumber = r.nextInt(items.getNumAvailableBags());
//                    int itemSize = items.getSizes()[itemId];
//                    if (solution.willItemFitInBag(bagNumber, itemSize)) {
                        solution.addItemToBag(bagNumber, itemId);
                        addedToBag = true;
//                    } else {
                        addToBagCount++;
                        if (addToBagCount > items.getNumAvailableBags() * 100) {
                            allItemsAdded = false;
                            break;
//                        }
                    }
                }
            }
            randSolutionCount++;
            if (randSolutionCount > 10000)
                return null;
        }
        return solution;
    }

    public void createRandomSolutions(int numToCreate) {
        for (int i = 0; i < numToCreate; i++) {
            IDsToSolutionMap.put(i, createRandomSolution());
            IDs.add(i);
        }
    }

    @Override
    public Set<Integer> getIDs() {
        return IDs;
    }

    public Map<Integer, BaggingSolution> getIDsToSolutionMap() {
        return IDsToSolutionMap;
    }

    public void printOutBaggingDataSolutionScores() {
        for (Entry entry : IDsToSolutionMap.entrySet()) {
            Integer id = (Integer) entry.getKey();
            BaggingSolution solution = (BaggingSolution) entry.getValue();

            System.out.println("id: " + id + "   solution score: " + solution.getSolutionScore() + "   bag of items: " + Arrays.toString(solution.getBagsOfItems()));
            if (solution.getSolutionScore() == 0) {
                for (Integer _id : items.getItemIds())
                    System.out.println("id: " + _id + "   item name: " + items.getIDToNameMap(_id));
                break;
            }
        }
    }
}
