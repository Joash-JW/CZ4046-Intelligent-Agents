package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * A Grid class represents the maze environment and contains:
 * 1. grid - a list of State objects
 * 2. MAX_ROW - maximum number of rows
 * 3. MAX_COL - maximum number of columns
 * */
public class Grid {
    private ArrayList<State[]> grid = new ArrayList<State[]>();
    public final int MAX_ROW;
    public final int MAX_COL;

    /**
     * Constructor.
     * @param path path to read grid map from
     * @return grid object
     */
    public Grid(String path) {
        File file = new File(path);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc.hasNextLine()) {
            String[] row = sc.nextLine().split("");
            State[] states = new State[row.length];
            for (int i=0; i<row.length; i++) {
                states[i] = new State(row[i]);
            }
            grid.add(states);
        }
        MAX_ROW = grid.size();
        MAX_COL = grid.get(0).length;
        System.out.println("Grid of " + MAX_ROW + "x" + MAX_COL + " loaded");
        //printRewards();
    }

    /**
     * Function that prints the reward values
     */
    public void printRewards() {
        for (State[] states : this.grid) {
            for (State state : states) {
                if (state.isWall()) {
                    System.out.print("| ");
                    System.out.print("Wall");
                    System.out.print(" ");
                }
                else {
                    System.out.print("|   ");
                    System.out.print(state.getReward());
                    System.out.print("  ");
                }
            } System.out.print("|\n");
        }
    }

    /*Getters*/
    public ArrayList<State[]> getGrid() { return grid; }
}
