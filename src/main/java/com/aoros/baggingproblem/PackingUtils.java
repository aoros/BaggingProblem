package com.aoros.baggingproblem;

import java.util.Queue;
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
            System.out.println("   BaggingState Level " + i + ":");
            int b = 1;
            for (Bag bag : stack.get(i - 1).getBags()) {
                System.out.println("      Bag " + b + ": " + bag.getBagItemNames());
                b++;
            }
        }
        System.out.println("");
    }

    public static void printQueue(Queue<BaggingState> queue, int iteration) {
        System.out.println("Queue Iteration " + iteration);
        int i = 1;
        for (BaggingState state : queue) {
            System.out.println("   Queue Iteration " + iteration
                    + " / BaggingState Level " + i
                    + " / origVal: " + state.getOriginalConstrainingValue());
            int b = 1;
            for (Bag bag : state.getBags()) {
                System.out.println("      Bag " + b + ": " + bag.getBagItemNames());
                b++;
            }
            i++;
        }
        System.out.println("");
    }
}
