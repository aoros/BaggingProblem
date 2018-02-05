package com.aoros.baggingproblem;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Bag {

    private final Integer maxCapacity;
    private final Set<String> allItemNames;
    private Set<String> allowedItems;
    private final Set<String> bagItemNames = new HashSet<>();
    private Integer amountInBag = 0;

    public Bag(PackingDefinition packingDefinition) {
        this.maxCapacity = packingDefinition.getMaxBagCapacity();
        this.allItemNames = packingDefinition.getAllItemNames();
        this.allowedItems = packingDefinition.getAllItemNames();
    }

    private Bag(Bag bag) {
        this.maxCapacity = bag.getMaxCapacity();
        this.allItemNames = bag.getAllItemNames();
        this.allowedItems = bag.getAllowedItems();
        this.amountInBag = bag.getAmountInBag();
        for (String bagItemName : bag.getBagItemNames()) {
            this.bagItemNames.add(bagItemName);
        }
    }

    public Set<String> getAllowedItems() {
        return allowedItems;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Set<String> getAllItemNames() {
        return allItemNames;
    }

    public Integer getAmountInBag() {
        return amountInBag;
    }

    public Set<String> getBagItemNames() {
        return bagItemNames;
    }

    public boolean addItem(GroceryItem groceryItem) {
        if (amountInBag + groceryItem.getSize() > maxCapacity) {
            return false;
        }
        if (!safeToAdd(groceryItem)) {
            return false;
        }
        amountInBag += groceryItem.getSize();
        bagItemNames.add(groceryItem.getName());
        adjustAllowedItemsSet(groceryItem);
        return true;
    }

    public Bag copyOf() {
        return new Bag(this);
    }

    private boolean safeToAdd(GroceryItem groceryItem) {
        if (!allowedItems.contains(groceryItem.getName())) {
            return false;
        }

        Set<String> whiteListSet = groceryItem.getWhiteListSet(allItemNames);
        Set<String> bagItemNamesCopy = getStringSetCopy(bagItemNames);
        bagItemNamesCopy.removeAll(whiteListSet);
        return bagItemNamesCopy.isEmpty();
    }

    private Set<String> getStringSetCopy(Set<String> stringSet) {
        Set<String> copy = new HashSet<>();
        for (String str : stringSet) {
            copy.add(str);
        }
        return copy;
    }

    @Override
    public String toString() {
        if (bagItemNames.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String itemName : bagItemNames) {
            stringBuilder.append(itemName).append("\t");
        }
        String output = stringBuilder.toString();
        return output.substring(0, output.length() - 1); // substring to remove last 'space' character
    }

    private void adjustAllowedItemsSet(GroceryItem groceryItem) {
        Set<String> itemWhiteList = groceryItem.getWhiteListSet(allItemNames);
        Set<String> newAllowedItems = new HashSet<>(allowedItems);
        newAllowedItems.retainAll(itemWhiteList); // retainAll is the intersection of the two sets
        allowedItems = newAllowedItems;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.bagItemNames);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bag other = (Bag) obj;
        if (!Objects.equals(this.bagItemNames, other.bagItemNames)) {
            return false;
        }
        return true;
    }

}
