package com.aoros.baggingproblem;

import com.aoros.baggingproblem.strategy.PackingStrategy;
import com.aoros.baggingproblem.strategy.DepthFirstPackingStrategy;
import com.aoros.baggingproblem.strategy.BreadthFirstPackingStrategy;
import com.aoros.baggingproblem.strategy.MrvForwardCheckingPackingStrategy;
import com.aoros.baggingproblem.strategy.MrvLcvPackingStrategy;
import com.aoros.baggingproblem.strategy.MrvPackingStrategy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bagit {

    // github info:   https://netbeans.org/kb/docs/ide/github_nb_screencast.html
    private static final boolean DEBUG = false;
    private static final boolean USE_TIMER = false;
    private static final String DEFAULT_GROCERY_LIST_FILE = "src/main/resources/g8.txt";
    private static final String BREADTH = "-breadth";
    private static final String DEPTH = "-depth";
    private static final String MRV = "-mrv";
    private static final String MRV_FC = "-mrv_fc";
    private static final String MRV_LCV = "-pq";

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();

        PackingDefinition packingDefinition = null;
        String strategyToUse = "";

        if (DEBUG) {
            packingDefinition = new PackingDefinition(DEFAULT_GROCERY_LIST_FILE);
            strategyToUse = MRV_LCV;
//            strategyToUse = MRV;
//            strategyToUse = DEPTH;
        } else {
            if (args.length < 1) {
                usage();
            }
            packingDefinition = new PackingDefinition(args[0]);
            if (args.length > 1) {
                strategyToUse = args[1];
            }
        }

        if (!packingDefinition.isIsValidList()) {
            printOutcome(new ArrayList<>(), startTime);
            return;
        }

        PackingStrategy strategy;
        if (BREADTH.equals(strategyToUse)) {
            strategy = new BreadthFirstPackingStrategy();
        } else if (DEPTH.equals(strategyToUse)) {
            strategy = new DepthFirstPackingStrategy();
        } else if (MRV.equals(strategyToUse)) {
            strategy = new MrvPackingStrategy();
        } else if (MRV_FC.equals(strategyToUse)) {
            strategy = new MrvForwardCheckingPackingStrategy();
        } else if (MRV_LCV.equals(strategyToUse)) {
            strategy = new MrvLcvPackingStrategy();
        } else {
            usage();
            return;
        }

        strategy.setDebug(DEBUG);
        strategy.setPackingDefinition(packingDefinition);
        List<BaggingState> solutions = strategy.packBags();

        printOutcome(solutions, startTime);
    }

    private static boolean solutionExists(List<BaggingState> solutions) {
        return solutions != null && !solutions.isEmpty();
    }

    private static void printOutcome(List<BaggingState> solutions, Long startTime) {
        if (USE_TIMER) {
            Long endTime = System.currentTimeMillis();
            Long timeInSecs = (endTime - startTime);
            String timingMsg = "Runtime duration " + timeInSecs + " millisecs.";
            if (solutionExists(solutions))
                timingMsg += " Success for ";
            else
                timingMsg += " Failure for ";

            timingMsg += DEFAULT_GROCERY_LIST_FILE;

            System.out.println(timingMsg);
        } else {
            if (!solutionExists(solutions)) {
                System.out.println("failure");
            } else {
                Set<Set<Bag>> solutionSet = new HashSet<>();
                for (BaggingState solution : solutions) {
                    Set<Bag> bagSet = new HashSet<>();
                    for (Bag bag : solution.getBags()) {
                        bagSet.add(bag);
                    }
                    solutionSet.add(bagSet);
                }
                for (Set<Bag> solution : solutionSet) {
                    System.out.println("success");
                    System.out.print(getBagItemNames(solution));
                }
            }
        }
    }

    public static void usage() {
        System.out.print("Usage: java Bagit filename [-depth | -breadth | -pq]");
        System.out.println();
        System.exit(-1);
    }

    public static String getBagItemNames(Set<Bag> bags) {
        StringBuilder builder = new StringBuilder();
        for (Bag bag : bags) {
            if (!bag.getBagItemNames().isEmpty()) {
                builder.append(bag).append("\n");
            }
        }

        return builder.toString();
    }
}
