package model;

import util.Constants;

/*
 * A State class represents each cell in the grid, and contains:
 * 1. isWall - whether it contains a wall
 * 2. reward - reward value of this state
 */
public class State {
    private boolean isWall;
    private double reward;

    public State(String s) {
        switch (s) {
            case "P": setWall(false); setReward(Constants.GREEN_REWARD); break;
            case "M": setWall(false); setReward(Constants.BROWN_REWARD); break;
            case "W": setWall(true); break;
            default: setWall(false); setReward(Constants.WHITE_REWARD); break;
        }
    }

    /*Getters and setters*/
    public boolean isWall() {
        return isWall;
    }

    public double getReward() {
        return reward;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }
}
