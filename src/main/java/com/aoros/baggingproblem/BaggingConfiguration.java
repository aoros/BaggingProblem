package com.aoros.baggingproblem;

import java.util.HashSet;
import java.util.Set;

public class BaggingConfiguration {

    private final PackingDefinition packingDefinition;
    private final Bag[] bags;
    private final Set<Bag> bagSet = new HashSet<>();

    public BaggingConfiguration(PackingDefinition packingDefinition) {
        this.packingDefinition = packingDefinition;
        this.bags = new Bag[packingDefinition.getNumAvailableBags()];
    }

    public void add(Bag bag, int index) {
        bags[index] = bag.copyOf();
    }

    public Bag[] getBags() {
        return bags;
    }

    public Set<Bag> getBagSet() {
        return bagSet;
    }

    public Integer getNumItemsInBags() {
        int numItemsInBags = 0;
        for (Bag bag : bags) {
            if (bag != null) {
                numItemsInBags += bag.getBagItemNames().size();
            }
        }
        return numItemsInBags;
    }

    public BaggingConfiguration copyOf() {
        BaggingConfiguration copy = new BaggingConfiguration(packingDefinition);
        for (int i = 0; i < bags.length; i++) {
            copy.add(bags[i].copyOf(), i);
        }
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Bag bag : bags) {
            if (!bag.getBagItemNames().isEmpty()) {
                builder.append(bag).append("\n");
            }
        }

        return builder.toString();
    }
}
