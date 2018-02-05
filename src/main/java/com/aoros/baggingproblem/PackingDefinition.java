package com.aoros.baggingproblem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    private final Map<String, Set<String>> whiteListDomainMap = new HashMap<>();
    private final Set<Set<String>> uniqueDomainSets = new HashSet<>();

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

            createWhiteListDomainMap();
            createUniqueDomainSets();
            validityCheck();
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

    public Map<String, Set<String>> getWhiteListDomainMap() {
        return whiteListDomainMap;
    }

    public Set<Set<String>> getUniqueDomainSets() {
        return uniqueDomainSets;
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

    private void validityCheck() {
        int totalAvailableSize = numAvailableBags * maxBagCapacity;
        int totalSizeOfItems = 0;
        for (GroceryItem item : groceryItems) {
            totalSizeOfItems += item.getSize();
        }
        isValidList = totalAvailableSize >= totalSizeOfItems;
    }

    private void createWhiteListDomainMap() {
        Map<String, Set<String>> mapPass1 = new HashMap<>();
        for (GroceryItem item : groceryItems) {
            Set<String> whiteListSet = item.getWhiteListSet(allItemNames);
            whiteListSet.add(item.getName());
            mapPass1.put(item.getName(), whiteListSet);
        }

        Map<String, Set<String>> mapPass2 = new HashMap<>();
        for (Entry entry : mapPass1.entrySet()) {
            String groceryItemName = (String) entry.getKey();
            Set<String> domainSet = (Set<String>) entry.getValue();

            Set<String> newDomainSet = new HashSet<>();
            for (String domainItemName : domainSet) {
                if (groceryItemName.equals(domainItemName))
                    newDomainSet.add(domainItemName);
                else if (mapPass1.get(domainItemName).contains(groceryItemName))
                    newDomainSet.add(domainItemName);
            }
            mapPass2.put(groceryItemName, newDomainSet);
        }
        whiteListDomainMap.putAll(mapPass2);
    }

    private void createUniqueDomainSets() {
        for (Set<String> domainSet : whiteListDomainMap.values()) {
            uniqueDomainSets.add(domainSet);
        }
    }
}
