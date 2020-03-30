package algo;

import model.State;
import util.Constants;

import java.util.*;

/*
 * Subclass of Algorithm, implementing Policy Iteration
 */
public class PolicyIteration extends Algorithm {

    public PolicyIteration(String path) {
        super(path);
    }

    /**
     * main entry point for policy iteration algorithm
     */
    public void run(String csvPath) {
        // policy iteration method
        runPolicyIteration();

        // display results
        System.out.println("Final Utility:");
        printUtilities();
        System.out.println("Final Policy:");
        printPolicy();

        // log utilities values to CSV for graph plotting
        writeCSV(csvPath);
    }

    /**
     * policy iteration algorithm
     */
    private void runPolicyIteration() {
        initPolicy(); //initialise policy
        boolean change;
        int iterations = 1;
        double[][] initialUtility = new double[grid.MAX_ROW][grid.MAX_COL];
        ValueIteration.copy2DArray(utilities, initialUtility);
        history.add(initialUtility); // initial
        do {
            Constants.Actions[][] oldPolicy = new Constants.Actions[grid.MAX_ROW][grid.MAX_COL];
            copy2DArray(policy, oldPolicy);
            evaluatePolicy();
            policyImprovement();
            change = comparePolicy(oldPolicy, policy);
            System.out.println("Iteration " +iterations+ " - Change: "+change);
            iterations++;
        } while (change);
    }

    /**
     * Function that initialises the grid policies with a random action
     */
    private void initPolicy() {
        List<Constants.Actions> actions = Arrays.asList(Constants.Actions.values());
        Random r = new Random();
        for (int row = 0; row < grid.MAX_ROW; row++)
            for (int col = 0; col < grid.MAX_COL; col++) {
                State state = grid.getGrid().get(row)[col];
                if (state.isWall()) continue; // skip wall
                int i = r.nextInt(actions.size()); // to set policy randomly
                policy[row][col] = actions.get(i); // set initial policy
            }
    }

    /**
     * policy evaluation algorithm, implementing simplified Bellman Equation
     */
    private void evaluatePolicy() {
        int iterations = 1;
        do {
            for (int row = 0; row<grid.MAX_ROW; row++)
                for (int col = 0; col < grid.MAX_COL; col++) {
                    State state = grid.getGrid().get(row)[col];
                    if (state.isWall()) continue; //skip wall
                    Constants.Actions intendedAction = policy[row][col];
                    Constants.Actions left, right;
                    left = right = null;
                    // set right and left of intended action
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
                    utilities[row][col] = state.getReward() + Constants.DISCOUNT*utility;
                }
            double[][] currUtilities = new double[grid.MAX_ROW][grid.MAX_COL];
            ValueIteration.copy2DArray(utilities, currUtilities);
            history.add(currUtilities); // store utility estimates of this iteration
        } while (++iterations <= Constants.I);
    }

    /**
     * policy improvement algorithm, to calculate new policy based on the updated utilities
     */
    private void policyImprovement() {
        for (int row=0; row<grid.MAX_ROW; row++)
            for (int col=0; col<grid.MAX_COL; col++) {
                State state = grid.getGrid().get(row)[col];
                if (state.isWall()) continue; // skip wall
                HashMap<Constants.Actions, Double> expectedUtilities = new HashMap<>(4);
                for (Constants.Actions action : Constants.Actions.values()) {
                    double expectedUtility = 0;
                    // get utility value of each action
                    switch (action) {
                        case U: expectedUtility = Constants.INTENDED_PROB*calculateUtility(row, col, Constants.Actions.U)
                                + Constants.RIGHT_ANGLE_PROB*calculateUtility(row, col, Constants.Actions.L)
                                + Constants.RIGHT_ANGLE_PROB*calculateUtility(row, col, Constants.Actions.R); break;
                        case L: expectedUtility = Constants.INTENDED_PROB*calculateUtility(row, col, Constants.Actions.L)
                                + Constants.RIGHT_ANGLE_PROB*calculateUtility(row, col, Constants.Actions.D)
                                + Constants.RIGHT_ANGLE_PROB*calculateUtility(row, col, Constants.Actions.U); break;
                        case R: expectedUtility = Constants.INTENDED_PROB*calculateUtility(row, col, Constants.Actions.R)
                                + Constants.RIGHT_ANGLE_PROB*calculateUtility(row, col, Constants.Actions.U)
                                + Constants.RIGHT_ANGLE_PROB*calculateUtility(row, col, Constants.Actions.D); break;
                        case D: expectedUtility = Constants.INTENDED_PROB*calculateUtility(row, col, Constants.Actions.D)
                                + Constants.RIGHT_ANGLE_PROB*calculateUtility(row, col, Constants.Actions.R)
                                + Constants.RIGHT_ANGLE_PROB*calculateUtility(row, col, Constants.Actions.L); break;
                    }
                    expectedUtilities.put(action, expectedUtility);
                }
                // find action with the best utility
                double maxExpectedUtility = Collections.max(expectedUtilities.values());
                Constants.Actions updatedAction = null;
                for (Map.Entry<Constants.Actions, Double> map : expectedUtilities.entrySet())
                    if(map.getValue() == maxExpectedUtility)
                        updatedAction = map.getKey();
                policy[row][col] = updatedAction; // update policy
            }
    }

    /**
     * Function that compares the 2 policies. Returns false when there is no change
     */
    private boolean comparePolicy(Constants.Actions[][] oldPolicy, Constants.Actions[][] newPolicy) {
        for (int row=0; row<oldPolicy.length; row++) for (int col=0; col<oldPolicy[row].length; col++) {
            if (oldPolicy[row][col] == null) continue;
            else if (oldPolicy[row][col] != newPolicy[row][col]) {
                return true; // return true when there is a difference
            }
        } return false; // return false when there is no change
    }

    /**
     * Function that copies an action 2D array into another 2D array
     * @param src source array of data type Actions to be copied from
     * @param dst destination array of data type Actions to be copied to
     */
    protected static void copy2DArray(Constants.Actions[][] src, Constants.Actions[][] dst) {
        for (int row = 0; row < src.length; row++) System.arraycopy(src[row], 0, dst[row], 0, src[row].length);
    }
}
