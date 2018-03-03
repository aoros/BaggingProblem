package com.aoros.baggingproblem;

import com.aoros.bagging.local.search.BaggingData;
import com.aoros.bagging.local.search.BaggingFileReader;
import com.aoros.bagging.local.search.BaggingItems;
import com.aoros.bagging.local.search.BaggingLocalSearch;
import com.aoros.bagging.local.search.BaggingSolution;
import com.aoros.baggingproblem.strategy.PackingStrategy;
import com.aoros.baggingproblem.strategy.DepthFirstPackingStrategy;
import com.aoros.baggingproblem.strategy.BreadthFirstPackingStrategy;
import com.aoros.baggingproblem.strategy.MrvForwardCheckingPackingStrategy;
import com.aoros.baggingproblem.strategy.MrvLcvPackingStrategy;
import com.aoros.baggingproblem.strategy.MrvPackingStrategy;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bagit {

    // github info:   https://netbeans.org/kb/docs/ide/github_nb_screencast.html
    private static final boolean DEBUG = true;
    private static final boolean STRATEGY_DEBUG = false;
    private static final boolean USE_TIMER = true;
    private static final String DEFAULT_GROCERY_LIST_FILE = "src/main/resources/g4";
    private static final String BREADTH = "-breadth";
    private static final String DEPTH = "-depth";
    private static final String MRV = "-mrv";
    private static final String MRV_FC = "-mrv_fc";
    private static final String MRV_LCV = "-pq";
    private static final String COOL = "-cool";
    private static String fileName;
    private static String strategyToUse = "";
    private static Long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        PackingDefinition packingDefinition = null;

        if (DEBUG) {
            fileName = DEFAULT_GROCERY_LIST_FILE;
            packingDefinition = new PackingDefinition(fileName);
            // >>>>>>>>>>>>>>>>>>>>>>>>>>
            strategyToUse = COOL;
            // >>>>>>>>>>>>>>>>>>>>>>>>>>
        } else {
            if (args.length < 1) {
                usage();
            }
            fileName = args[0];
            packingDefinition = new PackingDefinition(fileName);
            if (args.length > 1) {
                strategyToUse = args[1];
            }
        }

        if (!packingDefinition.isIsValidList()) {
            printOutcome(new ArrayList<>(), startTime);
            return;
        }

        PackingStrategy strategy = null;
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
        } else if (COOL.equals(strategyToUse)) {
            int iters = 10;
            int timeLimitPerIterationInSecs = 6;
            startLocalSearch(fileName, iters, timeLimitPerIterationInSecs);
        } else {
            usage();
            return;
        }

        if (strategy != null) {
            strategy.setDebug(STRATEGY_DEBUG);
            strategy.setPackingDefinition(packingDefinition);
            List<BaggingState> solutions = strategy.packBags();
            printOutcome(solutions, startTime);
        }

    }

    private static boolean solutionExists(List<BaggingState> solutions) {
        return solutions != null && !solutions.isEmpty();
    }

    private static void printOutcome(List<BaggingState> solutions, Long startTime) {
        if (!solutionExists(solutions)) {
            System.out.println("failure");
        } else {
            printTimerResults(startTime);
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

    private static void printTimerResults(Long startTime) {
        if (USE_TIMER) {
            Long endTime = System.currentTimeMillis();
            Long timeInSecs = (endTime - startTime);
            String timingMsg = "\nRuntime[ms]: " + timeInSecs;
            timingMsg += "\nOption: " + strategyToUse;
            timingMsg += "\nFilename: " + fileName;
            System.out.println(timingMsg);
            System.out.println("");
        }
    }

    public static void usage() {
        System.out.print("Usage: java Bagit filename [-depth | -breadth | -pq | -cool]");
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

    private static void startLocalSearch(String filePath, int iters, int timeLimitInSecs) {
        try {
            BaggingItems items = new BaggingFileReader().read(filePath);
            BaggingData data = new BaggingData(items);

            boolean solutionFound = false;
            for (int k = 0; k < iters; k++) {
                BaggingSolution randomBaggingSolution = data.createRandomSolution();
                if (randomBaggingSolution == null) {
                    break;
                }
                BaggingLocalSearch search = new BaggingLocalSearch(randomBaggingSolution, timeLimitInSecs, STRATEGY_DEBUG);
                BaggingSolution solution = search.performSearch();

                if (solution.getSolutionScore() == 0) {
                    printTimerResults(startTime);
                    debugPrintBagOfItems(solution);
                    printSuccessfulSolution(solution, items);
                    solutionFound = true;
                    break;
                }
                if (DEBUG)
                    System.out.println("=== ITERATION: " + k + " ===");
            }
            if (!solutionFound)
                System.out.println("failure");
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to read file: " + filePath);
            System.exit(2);
        }

    }

    public static void debugPrintBagOfItems(BaggingSolution randomBaggingSolution) {
        if (DEBUG) {
            System.out.println("-----------------------------------------------");
            System.out.println("Solution Score: " + randomBaggingSolution.getSolutionScore());
            for (Set<Integer> bagOfItems : randomBaggingSolution.getBagsOfItems()) {
                System.out.println(bagOfItems);
            }
        }
    }

    private static void printMsg(String msg) {
        if (DEBUG)
            System.out.println(msg);
    }

    private static void printSuccessfulSolution(BaggingSolution solution, BaggingItems items) {
        System.out.println("success");
        StringBuilder builder = new StringBuilder();
        for (Set<Integer> bagOfItems : solution.getBagsOfItems()) {
            for (Integer item : bagOfItems) {
                builder.append(items.getIDToNameMap(item)).append("\t");
            }
            builder.append("\n");
        }
        System.out.println(builder.toString());
    }
}
