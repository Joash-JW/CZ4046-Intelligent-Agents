import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Grid {
    private List<Cell[]> grid = new ArrayList<Cell[]>();

    public Grid(String path) {
        /**
         * Constructor.
         * @param path path to read environment grid from
         * @return environment object
         */
        File file = new File(path);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc.hasNextLine()) {
            String[] row = sc.nextLine().split("");
            Cell[] cells = new Cell[row.length];
            for (int i=0; i<row.length; i++) {
                cells[i] = new Cell(row[i]);
            }
            grid.add(cells);
        }
        System.out.println("Grid of " + grid.size() + "x" + grid.get(0).length);
        System.out.println("Grid:");
        this.printRewards();
    }

    public void printGrid(){
        /**
         * Function that prints the grid with agent current position
         */
        for (Cell[] cells : this.grid) {
            for (Cell cell : cells) {
                if (cell.isWall()) {
                    System.out.print("| ");
                    System.out.print("Wall");
                    System.out.print(" ");
                }
                else if (cell.isAgent()){
                    System.out.print("|   X  ");
                }
                else System.out.print("|      ");
            } System.out.print("|\n");
        }
    }

    public void printRewards(){
        /**
         * Function that prints the grid rewards
         */
        for (Cell[] cells : this.grid) {
            for (Cell cell : cells) {
                if (cell.isWall()) {
                    System.out.print("[");
                    System.out.print("Wall");
                    System.out.print("]");
                }
                else {
                    System.out.print("[");
                    System.out.print(cell.getReward());
                    System.out.print("]");
                }
            } System.out.print("\n");
        }
    }

    public void printPolicy(){
        /**
         * Function that prints the grid policies
         */
        for (Cell[] cells : this.grid) {
            for (Cell cell : cells) {
                if (cell.isWall()) {
                    System.out.print("[");
                    System.out.print("Wall");
                    System.out.print("]");
                }
                else {
                    System.out.print("[");
                    System.out.print(cell.getCellPolicy());
                    System.out.print("]");
                }
            } System.out.print("\n");
        }
    }

    public List<Cell[]> getGrid() { return grid; }

    public double calculateUtility(int row, int col) {
        Cell cell = grid.get(row)[col];
        String intendedAction = cell.getCellPolicy();
        double utility = cell.getReward();
        switch (intendedAction) {
            case ">": utility +=
            case "<":
            case "^":
            case "v":
        }
    }
}
