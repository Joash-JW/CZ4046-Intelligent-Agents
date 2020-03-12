package main;

import model.Grid;
import model.State;
import util.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class ValueIteration {
    private static Grid grid;
    public static double[][] utilities;
    public static Constants.Actions[][] policy;
    public static ArrayList<double[][]> history = new ArrayList<double[][]>(Constants.I);

    public static void main(String[] args) {
        grid = new Grid("./Assignment 1/map/map1.txt");
        //grid = new Grid("./map/map1.txt");
        utilities = new double[grid.MAX_ROW][grid.MAX_COL]; // initialise utilities to 0
        policy = new Constants.Actions[grid.MAX_ROW][grid.MAX_COL];
//        System.out.println("\n==============");
//        System.out.println("Initial Policy");
//        System.out.println("==============");
//        printPolicy();
//        System.out.println("\n");
        runValueIteration();
        System.out.println("Final Policy:");
        printPolicy();
        writeCSV();
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

    /**
     * Function that returns the sum of all the utilities in the grid
     * @return sum of all the utilities
     * */
    public static double totalUtility() {
        double utility = 0;
        for (int row=0; row<grid.MAX_ROW; row++)
            for (int col=0; col<grid.MAX_COL; col++) {
                if (grid.getGrid().get(row)[col].isWall()) continue; // skip wall
                utility += utilities[row][col];
            }
        return utility;
    }

    public static void runValueIteration() {
        int iterations = 1;
        do {
            getBestUtility();
            System.out.println("Iteration " +iterations+ " - Total Utility: "+totalUtility());
            double[][] temp = new double[grid.MAX_ROW][grid.MAX_COL];
            copy2DArray(utilities, temp);
            history.add(temp);
        } while (++iterations <= Constants.I);
        System.out.println("====");
        printUtilities();
    }

    public static void getBestUtility() {
        for (int row = 0; row<grid.MAX_ROW; row++)
            for (int col = 0; col < grid.MAX_COL; col++) {
                State state = grid.getGrid().get(row)[col];
                if (state.isWall()) continue;
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
                    actionUtilities.put(intendedAction, utility);
                }
                Constants.Actions bestAction = null;
                double bestUtility = Collections.max(actionUtilities.values());
                for (Map.Entry<Constants.Actions, Double> map : actionUtilities.entrySet())
                    if(map.getValue() == bestUtility) bestAction = map.getKey();
                policy[row][col] = bestAction;
                utilities[row][col] = state.getReward() + Constants.DISCOUNT*bestUtility;
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

    /**
     * Function that copies a utility 2D array into another 2D array
     * @param src source array of data type double to be copied from
     * @param dst destination array of data type double to be copied to
     * */
    public static void copy2DArray(double[][] src, double[][] dst) {
        for (int row = 0; row < src.length; row++)
            System.arraycopy(src[row], 0, dst[row], 0, src[row].length);
    }

    /**
     * Function that writes the history of utility values into a csv file
     * */
    public static void writeCSV() {
        try {
            FileWriter csv = new FileWriter("./Assignment 1/valueIteration.csv");

            // write column names
            for (int row=0; row<grid.MAX_ROW; row++)
                for (int col=0; col<grid.MAX_COL; col++) {
                    if (grid.getGrid().get(row)[col].isWall()) csv.write("Wall");
                    else csv.write("("+row+", "+col+")");
                    if (!(col==grid.MAX_COL-1 && row==grid.MAX_ROW-1)) csv.write(";");

                }
            csv.write("\n");

            // write values
            for (double[][] values : history) {
                for (int row=0; row<grid.MAX_COL; row++)
                    for (int col=0; col<grid.MAX_COL; col++) {
                        if (col==grid.MAX_COL-1 && row==grid.MAX_ROW-1) csv.write(Double.toString(values[row][col]));
                        else csv.write(Double.toString(values[row][col])+";");
                    }
                csv.write("\n");
            }
            csv.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
