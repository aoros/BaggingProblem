package com.aoros.baggingproblem;

import java.util.HashSet;
import java.util.Set;

public class GroceryItem {

    private final String name;
    private final Integer size;
    private Boolean isWhiteList = null;
    private final Set<String> compatibilitySet = new HashSet<>();

    public GroceryItem(String groceryItemDefinition) {
        String[] items = groceryItemDefinition.split("\\s+");

        this.name = items[0];
        this.size = Integer.parseInt(items[1]);

        if (items.length > 2) {
            setWhiteOrBlackListSymbol(items[2]);
        }
        if (items.length > 3) {
            for (int i = 3; i < items.length; i++) {
                compatibilitySet.add(items[i]);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    public Set<String> getCompatibilitySet() {
        return compatibilitySet;
    }

    private void setWhiteOrBlackListSymbol(String whiteOrBlackListSymbol) {
        if ("+".equals(whiteOrBlackListSymbol)) {
            isWhiteList = true;
        } else if ("-".equals(whiteOrBlackListSymbol)) {
            isWhiteList = false;
        } else {
            throw new IllegalArgumentException("White or Black list symbol must be '+' or '-' respectively. Symbol found was " + whiteOrBlackListSymbol);
        }
    }

    public Set<String> getWhiteListSet(Set<String> sourceSet) {
        if (isWhiteList == null) {
            return sourceSet;
        }

        if (isWhiteList) {
            return compatibilitySet;
        }

        Set<String> whiteListSet = new HashSet<>();
        whiteListSet.addAll(sourceSet);
        whiteListSet.removeAll(compatibilitySet);
        return whiteListSet;
    }

    public int getBlacklistSize(int countOfAllGroceryItems) {
        if (isWhiteList == null)
            return 0;

        return countOfAllGroceryItems - compatibilitySet.size();
    }

    @Override
    public String toString() {
        return "GroceryItem{" + "name=" + name + ", size=" + size + ", isWhiteList=" + isWhiteList + ", compatibilityList=" + compatibilitySet + '}';
    }
}
