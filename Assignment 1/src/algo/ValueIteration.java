package algo;

import model.State;
import util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/*
 * Subclass of Algorithm, implementing Value Iteration
 * */
public class ValueIteration extends Algorithm {

    public ValueIteration(String path) {
        super(path);
    }

    /**
     * main entry point for value iteration algorithm
     * */
    public void run(String csvPath) {
        // value iteration method
        runValueIteration();

        // display results
        System.out.println("Final Utility:");
        printUtilities();
        System.out.println("Final Policy:");
        printPolicy();

        // log utilities values to CSV for graph plotting
        writeCSV(csvPath);
    }

    /**
     * value iteration algorithm
     * */
    private void runValueIteration() {
        int iterations = 1;
        double maxDelta;
        do {
            maxDelta = 0;
            maxDelta = getBestUtility(maxDelta);
            System.out.println("Iteration " +iterations+ " - Max Delta: "+maxDelta);
            double[][] temp = new double[grid.MAX_ROW][grid.MAX_COL];
            copy2DArray(utilities, temp);
            history.add(temp);
            iterations++;
        } while (maxDelta >= Constants.CONVERGENCE_THRESH);
    }

    /**
     * Function that calculates the utility values of actions and selects the optimal one
     * @param delta a small value of type double, 0 or minimum value possible
     * @return maximum change in utility
     * */
    private double getBestUtility(double delta) {
        for (int row = 0; row<grid.MAX_ROW; row++)
            for (int col = 0; col < grid.MAX_COL; col++) {
                State state = grid.getGrid().get(row)[col];
                if (state.isWall()) continue; // skip wall
                HashMap<Constants.Actions, Double> actionUtilities = new HashMap<>(4);
                for (Constants.Actions intendedAction: Constants.Actions.values()) {
                    Constants.Actions left, right;
                    left = right = null;
                    switch (intendedAction) {
                        case U: left = Constants.Actions.L; right = Constants.Actions.R; break;
                        case L: left = Constants.Actions.D; right = Constants.Actions.U; break;
                        case R: left = Constants.Actions.U; right = Constants.Actions.D; break;
                        case D: left = Constants.Actions.R; right = Constants.Actions.L; break;
                    }
                    double intendUtility = calculateUtility(row, col, intendedAction);
                    double leftUtility = calculateUtility(row, col, left);
                    double rightUtility = calculateUtility(row, col, right);
                    double utility = Constants.INTENDED_PROB*intendUtility + Constants.RIGHT_ANGLE_PROB*leftUtility + Constants.RIGHT_ANGLE_PROB*rightUtility;
                    utility = state.getReward() + Constants.DISCOUNT*utility;
                    actionUtilities.put(intendedAction, utility);
                }
                Constants.Actions bestAction = null;
                double bestUtility = Collections.max(actionUtilities.values());
                for (Map.Entry<Constants.Actions, Double> map : actionUtilities.entrySet())
                    if(map.getValue() == bestUtility) bestAction = map.getKey();
                policy[row][col] = bestAction;
                double newDelta = Math.abs(bestUtility - utilities[row][col]);
                delta = Math.max(newDelta, delta);
                utilities[row][col] = bestUtility;
            } return delta;
    }

    /**
     * Function that copies a utility 2D array into another 2D array
     * @param src source array of data type double to be copied from
     * @param dst destination array of data type double to be copied to
     * */
    protected static void copy2DArray(double[][] src, double[][] dst) {
        for (int row = 0; row < src.length; row++)
            System.arraycopy(src[row], 0, dst[row], 0, src[row].length);
    }
}
