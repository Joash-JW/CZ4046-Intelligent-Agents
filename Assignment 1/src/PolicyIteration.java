import java.util.ArrayList;
import java.util.List;

public class PolicyIteration {
    private static Grid grid;
    private static double utilities[][];
    private static int rows;
    private static int cols;
    private static double DISCOUNT = 0.99;

    public static void main(String[] args) {
        Grid grid = new Grid("./env/map1.txt");
        initPolicy(grid);
        System.out.println("\n==============");
        System.out.println("Initial Policy");
        System.out.println("==============");
        grid.printPolicy();
        grid.printGrid();
        evaluatePolicy(grid);
    }

    public static void initPolicy(Grid grid) {
        rows = grid.getGrid().size(); cols = grid.getGrid().get(0).length;
        utilities = new double[rows][cols];
        for (Cell[] cells : grid.getGrid()) {
            for (int i=0; i<cols; i++) {
                if (!cells[i].isWall()) {
                    cells[i].setCellPolicy(">");
                }
            }
        }
    }

    public static void evaluatePolicy(Grid grid) {
        List<Cell[]> map = grid.getGrid();
        for (int x=0; x<rows; y++) {
            for (int y=0; y<cols; x++) {
                try {
                    String intendedAction = map.get(x)[y].getCellPolicy();

                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
    }
}
