package main;

import algo.PolicyIteration;
import algo.ValueIteration;
import util.Constants;

/*
 * main entry point to run Value and Policy Iteration
 * */
public class main {

    public static void main(String[] args) {
        ValueIteration valueIteration = new ValueIteration("./map/map1.txt");
        System.out.println("Running Value Iteration:");
        valueIteration.run("./valueIteration.csv");
        System.out.println("Discount factor: "+ Constants.DISCOUNT);
        System.out.println("Max Reward(R_MAX): "+ Constants.R_MAX);
        System.out.println("Constant C: "+ Constants.C);
        System.out.println("Epsilon: "+ Constants.EPSILON);
        System.out.println("Convergence Threshold: "+ Constants.CONVERGENCE_THRESH);

        PolicyIteration policyIteration = new PolicyIteration("./map/map1.txt");
        policyIteration.run("./policyIteration.csv");
        System.out.println("Constant I: "+ Constants.I);
    }
}
