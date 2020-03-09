package util;

public class Constants {
    public static double GREEN_REWARD = +1.00;
    public static double BROWN_REWARD = -1.00;
    public static double WHITE_REWARD = -0.04;

    public static final double DISCOUNT = 0.99; //Discount factor

    // Transition model
    public static final double INTENDED_PROB = 0.8;
    public static final double RIGHT_ANGLE_PROB = 0.1;

    public enum Actions { U, L, R, D } // actions: up, left, right, down

    // Constant k (i.e. number of times simplified Bellman update is executed to produce the next utility estimate)
    public static final int I = 100;

    public static final double Rmax = 1.0;

    public static final double c = 1/(1-DISCOUNT);

    public static final double UTILITY_UPPER_BOUND = c*Rmax;
}
