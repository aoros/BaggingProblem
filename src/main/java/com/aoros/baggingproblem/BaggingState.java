package com.aoros.baggingproblem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BaggingState {

    private final PackingDefinition packingDefinition;
    private final Bag[] bags;
    private final Set<Bag> bagSet = new HashSet<>();
    private Double constrainingValue;
    private Double originalConstrainingValue;

    public BaggingState(PackingDefinition packingDefinition) {
        this.packingDefinition = packingDefinition;
        this.bags = new Bag[packingDefinition.getNumAvailableBags()];
        calculateConstrainingValue();
    }

    public void add(Bag bag, int index) {
        bags[index] = bag.copyOf();
    }

    public Bag[] getBags() {
        return bags;
    }

    public Set<Bag> getBagSet() {
        bagSet.clear();
        bagSet.addAll(Arrays.asList(bags));
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

    public void setConstrainingValueToZero() {
        this.constrainingValue = 0.0;
    }

    public Double getConstrainingValue() {
        return constrainingValue;
    }

    public Double getOriginalConstrainingValue() {
        return originalConstrainingValue;
    }

    private void calculateConstrainingValue() {
        constrainingValue = 0.0;
        for (Bag bag : bags) {
            if (bag != null)
                constrainingValue += bag.getPercentageOfBagOpen() * bag.getWhiteListSize();
        }
        originalConstrainingValue = constrainingValue;
    }

    public BaggingState copyOf() {
        BaggingState copy = new BaggingState(packingDefinition);
        for (int i = 0; i < bags.length; i++) {
            copy.add(bags[i].copyOf(), i);
        }
        copy.calculateConstrainingValue();
        return copy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.bagSet);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final BaggingState other = (BaggingState) obj;

        return this.getBagSet().containsAll(other.getBagSet());
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
