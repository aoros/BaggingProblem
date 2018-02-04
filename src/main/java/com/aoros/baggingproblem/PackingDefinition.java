package com.aoros.baggingproblem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PackingDefinition {

    private Integer numAvailableBags;
    private Integer maxBagCapacity;
    private boolean isValidList;
    private final List<GroceryItem> groceryItems = new ArrayList<>();
    private final Set<String> allItemNames = new HashSet<>();

    public PackingDefinition(String definitionFileName) {
        try {
            Scanner in = new Scanner(new FileReader(definitionFileName));

            numAvailableBags = Integer.parseInt(scrubRow(in.nextLine()));
            maxBagCapacity = Integer.parseInt(scrubRow(in.nextLine()));

            while (in.hasNext()) {
                groceryItems.add(new GroceryItem(scrubRow(in.nextLine())));
            }

            for (GroceryItem item : groceryItems) {
                allItemNames.add(item.getName());
            }
            isValidList = initialValidityCheck(groceryItems);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PackingDefinition.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isIsValidList() {
        return isValidList;
    }

    public Integer getNumAvailableBags() {
        return numAvailableBags;
    }

    public Integer getMaxBagCapacity() {
        return maxBagCapacity;
    }

    public List<GroceryItem> getGroceryItems() {
        return groceryItems;
    }

    public Set<String> getAllItemNames() {
        return allItemNames;
    }

    private String scrubRow(String rowText) {
        int commentIndex = rowText.indexOf("//");
        if (commentIndex <= 0) {
            return rowText.trim();
        }
        return rowText.substring(0, commentIndex).trim();
    }

    @Override
    public String toString() {
        return "PackingDefinition{" + "numAvailableBags=" + numAvailableBags + ", maxBagCapacity=" + maxBagCapacity + ", \n\tgroceryItems=" + groceryItems + '}';
    }

    private boolean initialValidityCheck(List<GroceryItem> groceryItems) {
        int totalAvailableSize = numAvailableBags * maxBagCapacity;
        int totalSizeOfItems = 0;
        for (GroceryItem item : groceryItems) {
            totalSizeOfItems += item.getSize();
        }
        return totalAvailableSize >= totalSizeOfItems;
    }
}
