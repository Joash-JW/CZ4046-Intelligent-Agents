package algo;

import model.Grid;
import util.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * An abstract Algorithm class and contains:
 * 1. grid - Grid object
 * 2. utilities - utility values of the grid
 * 3. policy - policy of the grid
 * 4. history - to store utility values at each iteration
 */
public abstract class Algorithm {
    protected Grid grid;
    protected double[][] utilities;
    protected Constants.Actions[][] policy;
    protected ArrayList<double[][]> history;

    /**
     * Constructor.
     * @param path path to read maze environment from
     */
    public Algorithm(String path) {
        // initialisation
        grid = new Grid(path);
        utilities = new double[grid.MAX_ROW][grid.MAX_COL]; // initialise utilities to 0
        policy = new Constants.Actions[grid.MAX_ROW][grid.MAX_COL];
        history = new ArrayList<double[][]>(Constants.I);
    }

    /**
     * Function that returns the utility of an action
     * @param row current row number of the grid
     * @param col current column number of the grid
     * @param action action to move
     * @return utility value of the action
     */
    protected double calculateUtility(int row, int col, Constants.Actions action) {
        double utility = 0;
        // get the utility of the next state based on the action
        // if a wall is present, the utility is the current state (no change)
        switch (action) {
            case U: utility = (row-1 >= 0 && !grid.getGrid().get(row-1)[col].isWall()) ? utilities[row-1][col] : utilities[row][col]; break;
            case L: utility = (col-1 >= 0 && !grid.getGrid().get(row)[col-1].isWall()) ? utilities[row][col-1] : utilities[row][col]; break;
            case R: utility = (col+1 < grid.MAX_COL && !grid.getGrid().get(row)[col+1].isWall()) ? utilities[row][col+1] : utilities[row][col]; break;
            case D: utility = (row+1 < grid.MAX_ROW && !grid.getGrid().get(row+1)[col].isWall()) ? utilities[row+1][col] : utilities[row][col]; break;
        }
        return utility;
    }

    /**
     * Function that prints the grid policies
     */
    protected void printPolicy(){
        for (int row = 0; row < grid.MAX_ROW; row++) {
            for (int col = 0; col < grid.MAX_COL; col++) {
                if (grid.getGrid().get(row)[col].isWall()) {
                    System.out.print("| ");
                    System.out.print("Wall ");
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
    protected void printUtilities(){
        for(int row=0; row<grid.MAX_ROW; row++) {
            for (int col=0; col<grid.MAX_COL; col++) {
                if(grid.getGrid().get(row)[col].isWall()) {
                    System.out.print("|  ");
                    System.out.print("Wall  ");
                }
                else {
                    System.out.print("|");
                    System.out.print(String.format("%.5f", utilities[row][col]));
                    System.out.print("");
                }
            } System.out.print("|\n");
        }
        for(int row=0; row<grid.MAX_ROW; row++) {
            for (int col=0; col<grid.MAX_COL; col++) {
                System.out.print("("+row+","+col+"): ");
                if(grid.getGrid().get(row)[col].isWall()) {
                    System.out.print("Wall");
                }
                else {
                    System.out.print(String.format("%.3f", utilities[row][col]));
                }
                System.out.print("\n");
            }
        }
    }

    /**
     * Function that writes the history of utility values into a csv file
     */
    protected void writeCSV(String path) {
        try {
            FileWriter csv = new FileWriter(path);

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
