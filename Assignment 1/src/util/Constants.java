package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
 * This static class contains constants that needs to be used in the program
 * */
public class Constants {
    // Reward values
    public static double GREEN_REWARD = +1.00;
    public static double BROWN_REWARD = -1.00;
    public static double WHITE_REWARD = -0.04;

    public static final double DISCOUNT = 0.990; //Discount factor

    // Transition model
    public static final double INTENDED_PROB = 0.8;
    public static final double RIGHT_ANGLE_PROB = 0.1;

    public enum Actions { U, L, R, D } // actions: up, left, right, down

    // the maximum reward
    public static final double R_MAX = Collections.max(
            new ArrayList<Double>(Arrays.asList(GREEN_REWARD, BROWN_REWARD, WHITE_REWARD)));

    // Constant parameter C to adjust
    public static final double C = 0.1;

    public static final double EPSILON = C*R_MAX; // formula for epsilon

    // convergence threshold formula
    public static final double CONVERGENCE_THRESH = EPSILON*(1-DISCOUNT)/DISCOUNT;

    // Constant I (i.e. number of times simplified Bellman update is executed to produce the next utility estimate)
    public static final int I = 100;
}
