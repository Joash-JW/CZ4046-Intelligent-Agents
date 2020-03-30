package main;

import algo.PolicyIteration;
import algo.ValueIteration;
import util.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/*
 * main entry point to experiment on complexity of maze on Value and Policy Iteration
 */
public class part2 {
    public static void main(String[] args) {
        String complexMapPath = "./map/complexMap.txt";
        generateMaze(complexMapPath, 20);

        // value iteration
        ValueIteration valueIteration = new ValueIteration(complexMapPath);
        System.out.println("Running Value Iteration:");
        long startTime = System.currentTimeMillis();
        valueIteration.run("");
        System.out.println("Discount factor: "+ Constants.DISCOUNT);
        System.out.println("Max Reward(R_MAX): "+ Constants.R_MAX);
        System.out.println("Constant C: "+ Constants.C);
        System.out.println("Epsilon: "+ Constants.EPSILON);
        System.out.println("Convergence Threshold: "+ Constants.CONVERGENCE_THRESH);
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime-startTime)+"ms");

        //policy iteration - uncomment below and comment out above to run policy iteration version
//        PolicyIteration policyIteration = new PolicyIteration(complexMapPath);
//        long startTime = System.currentTimeMillis();
//        System.out.println("Running Policy Iteration:");
//        policyIteration.run("");
//        System.out.println("Constant I: "+ Constants.I);
//        long endTime = System.currentTimeMillis();
//        System.out.println("Execution time: " + (endTime-startTime)+"ms");
    }

    /**
     * This function generates a maze environment randomly.
     * @param path txt file where the maze environment will be written to
     * @param size row or column size to generate the environment.
     */
    public static void generateMaze(String path, int size) {
        try {
            FileWriter txt = new FileWriter(path);
            Random r = new Random();
            int[] startPos = new int[]{r.nextInt(size), r.nextInt(size)};
            ArrayList<String> values = new ArrayList<String>(4);
            values.add("M"); values.add("P"); values.add("W"); values.add("E");

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    int i = r.nextInt(values.size());
                    String toWrite = values.get(i);
                    if (row == startPos[0] && col == startPos[1])
                        toWrite = "S";
                    txt.write(toWrite);
                } txt.write("\n");
            }
            txt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
