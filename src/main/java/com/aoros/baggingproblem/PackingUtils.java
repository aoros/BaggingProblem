package com.aoros.baggingproblem;

import java.util.Stack;

public class PackingUtils {

    public static BaggingState getNewEmptyBagsState(PackingDefinition packingDefinition, int numBagsAllowed) {
        BaggingState bags = new BaggingState(packingDefinition);
        for (int i = 0; i < numBagsAllowed; i++) {
            bags.add(new Bag(packingDefinition), i);
        }
        return bags;
    }

    public static void printStack(Stack<BaggingState> stack, int iteration) {
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
