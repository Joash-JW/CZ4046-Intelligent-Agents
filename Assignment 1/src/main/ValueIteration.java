package main;

import model.Grid;
import model.State;
import util.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// https://github.com/Javelin1991/CZ4046_Intelligent_Agents/blob/master/CZ4046_Assignment_1/Assignment_1_Report.pdf
// https://github.com/Yuance/MazeIntelligentAgents/blob/master/src/algo/ValueIteration.java
// http://www.cs.cmu.edu/afs/cs/academic/class/15780-s16/www/slides/mdps.pdf
public class ValueIteration {
    private static Grid grid;
    public static double[][] utilities;
    public static Constants.Actions[][] policy;

    public static void main(String[] args) {
        grid = new Grid("./map/testmap.txt");
        utilities = new double[grid.MAX_ROW][grid.MAX_COL]; // initialise utilities to 0
        policy = new Constants.Actions[grid.MAX_ROW][grid.MAX_COL];
        initPolicy();
        System.out.println("\n==============");
        System.out.println("Initial Policy");
        System.out.println("==============");
        printPolicy();
        System.out.println("\n");
        runValueIteration();
    }

    /**
     * Function that initialises the grid policies, with action Right
     */
    public static void initPolicy() {
        for (int row = 0; row < grid.MAX_ROW; row++)
            for (int col = 0; col < grid.MAX_COL; col++) {
                if (grid.getGrid().get(row)[col].isWall()) continue;
                policy[row][col] = Constants.Actions.R; // set initial policy
            }
    }

    /**
     * Function that prints the grid policies
     */
    public static void printPolicy(){
        for (int row = 0; row < grid.MAX_ROW; row++) {
            for (int col = 0; col < grid.MAX_COL; col++) {
                if (grid.getGrid().get(row)[col].isWall()) {
                    System.out.print("| ");
                    System.out.print("Wall");
                    System.out.print(" ");
                }
                else {
                    System.out.print("|   ");
                    System.out.print(policy[row][col]);
                    System.out.print("  ");
                }
            } System.out.print("|\n");
        }
    }

    /**
     * Function that prints the utility values
     */
    public static void printUtilities(){
        for(int row=0; row<grid.MAX_ROW; row++) {
            for (int col=0; col<grid.MAX_COL; col++) {
                if(grid.getGrid().get(row)[col].isWall()) {
                    System.out.print("|  ");
                    System.out.print("Wall");
                    System.out.print(" |");
                }
                else {
                    System.out.print("|");
                    System.out.print(String.format("%.5f", utilities[row][col]));
                    System.out.print("|");
                }
            } System.out.print("\n");
        }
    }

    public static double totalUtility() {
        double utility = 0;
        for (int row=0; row<grid.MAX_ROW; row++) for (int col=0; col<grid.MAX_COL; col++) {
            if (grid.getGrid().get(row)[col].isWall()) continue;
            utility += utilities[row][col];
        } return utility;
    }

    public static void runValueIteration() {
        int iterations = 0;
        do {
            Constants.Actions[][] oldPolicy = new Constants.Actions[grid.MAX_ROW][grid.MAX_COL];
            //copy2DArray(policy, oldPolicy);
            valueIteration();
            System.out.println("Iteration " +iterations+ " - Total Utility: "+totalUtility());
            updatePolicy();
            System.out.println("New Policy:");
            printPolicy();
            //change = comparePolicy(oldPolicy, policy);
        } while (++iterations != 1000);
        printUtilities();
    }

    public static void valueIteration() {
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
    }

    /**
     * Function that returns the utility of an action
     * @param row current row number of the grid
     * @param col current column number of the grid
     * @param action action to move
     * @return utility value. if a wall is present or it is at the corners, return 0
     */
    public static double calculateUtility(int row, int col, Constants.Actions action) {
        double utility = 0;
        switch (action) {
            case U: utility = (row-1 >= 0 && !grid.getGrid().get(row-1)[col].isWall()) ? utilities[row-1][col] : utilities[row][col]; break;
            case L: utility = (col-1 >= 0 && !grid.getGrid().get(row)[col-1].isWall()) ? utilities[row][col-1] : utilities[row][col]; break;
            case R: utility = (col+1 < grid.MAX_COL && !grid.getGrid().get(row)[col+1].isWall()) ? utilities[row][col+1] : utilities[row][col]; break;
            case D: utility = (row+1 < grid.MAX_ROW && !grid.getGrid().get(row+1)[col].isWall()) ? utilities[row+1][col] : utilities[row][col]; break;
        }
        return utility;
    }

    public static void updatePolicy() {
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
}
