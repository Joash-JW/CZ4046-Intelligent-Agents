package algo;

import model.State;
import util.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PolicyIteration extends Algorithm {

    public PolicyIteration(String path) {
        super(path);
    }

    public void run() {
        initPolicy();
        // value iteration method
        runPolicyIteration();

        // display results
        System.out.println("Final Utility:");
        printUtilities();
        System.out.println("Final Policy:");
        printPolicy();
    }

    /**
     * Function that initialises the grid policies, with action Right
     */
    private void initPolicy() {
        for (int row = 0; row < grid.MAX_ROW; row++)
            for (int col = 0; col < grid.MAX_COL; col++) {
                if (grid.getGrid().get(row)[col].isWall()) continue;
                policy[row][col] = Constants.Actions.R; // set initial policy
            }
    }

    private void runPolicyIteration() {
        boolean change;
        int iterations = 1;
        do {
            Constants.Actions[][] oldPolicy = new Constants.Actions[grid.MAX_ROW][grid.MAX_COL];
            copy2DArray(policy, oldPolicy);
            evaluatePolicy();
            System.out.println("Iteration " +iterations+ " - Total Utility: "+totalUtility());
            policyImprovement();
            System.out.println("New Policy:");
            printPolicy();
            iterations++;
            change = comparePolicy(oldPolicy, policy);
        } while (change);
    }

    private void evaluatePolicy() {
        int iterations = 1;
        do {
            for (int row = 0; row<grid.MAX_ROW; row++)
                for (int col = 0; col < grid.MAX_COL; col++) {
                    State state = grid.getGrid().get(row)[col];
                    if (state.isWall()) continue;
                    Constants.Actions intendedAction = policy[row][col];
                    Constants.Actions left=null; Constants.Actions right=null;
                    switch (intendedAction) {
                        case U: left = Constants.Actions.L; right = Constants.Actions.R; break;
                        case L: left = Constants.Actions.D; right = Constants.Actions.U; break;
                        case R: left = Constants.Actions.U; right = Constants.Actions.D; break;
                        case D: left = Constants.Actions.R; right = Constants.Actions.L; break;
                    }
                    double intendUtility = calculateUtility(row, col, intendedAction);
                    double leftUtility = calculateUtility(row, col, left);
                    double rightUtility = calculateUtility(row, col, right);
                    utilities[row][col] = state.getReward() + Constants.DISCOUNT*(Constants.INTENDED_PROB*intendUtility + Constants.RIGHT_ANGLE_PROB*leftUtility + Constants.RIGHT_ANGLE_PROB*rightUtility);
                }
        } while (++iterations <= Constants.I);
    }

    private void policyImprovement() {
        for (int row=0; row<grid.MAX_ROW; row++)
            for (int col=0; col<grid.MAX_COL; col++) {
                State state = grid.getGrid().get(row)[col];
                if (state.isWall()) continue;
                HashMap<Constants.Actions, Double> expectedUtilities = new HashMap<>(4); // up, left, right, down
                for (Constants.Actions action : Constants.Actions.values()) {
                    double expectedUtility = 0;
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
                double maxExpectedUtility = Collections.max(expectedUtilities.values());
                Constants.Actions updatedAction = null;
                for (Map.Entry<Constants.Actions, Double> map : expectedUtilities.entrySet()) if(map.getValue() == maxExpectedUtility) updatedAction = map.getKey();
                policy[row][col] = updatedAction;
            }
    }

    /**
     * Function that compares the 2 policies. Returns false when there is no change
     * */
    private boolean comparePolicy(Constants.Actions[][] oldPolicy, Constants.Actions[][] newPolicy) {
        for (int row=0; row<oldPolicy.length; row++) for (int col=0; col<oldPolicy[row].length; col++) {
            if (oldPolicy[row][col] == null) continue;
            else if (oldPolicy[row][col] != newPolicy[row][col]) {
                return true; // return true when there is a difference
            }
        } return false; // return false when there is no change
    }

    private void copy2DArray(Constants.Actions[][] src, Constants.Actions[][] dst) {
        for (int row = 0; row < src.length; row++) System.arraycopy(src[row], 0, dst[row], 0, src[row].length);
    }
}
