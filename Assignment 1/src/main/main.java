package main;

import algo.PolicyIteration;
import algo.ValueIteration;

public class main {

    public static void main(String[] args) {
        ValueIteration valueIteration = new ValueIteration("./map/map1.txt");
        valueIteration.run();
        //PolicyIteration policyIteration = new PolicyIteration("./map/map1.txt");
        //policyIteration.run();
    }
}
