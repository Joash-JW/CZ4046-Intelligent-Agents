package model;

import util.Constants;

/*
* A model.State class represents each cell in the grid, and contains:
* 1. isWall - whether it contains a wall
* 2. reward - reward of this state
* */
public class State {
    private boolean isWall;
    private double reward;
    private boolean isAgent;

    public State(String s) {
        switch (s) {
            case "P": setWall(false); setReward(Constants.GREEN_REWARD); setAgent(false); break;
            case "M": setWall(false); setReward(Constants.BROWN_REWARD); setAgent(false); break;
            case "W": setWall(true); setAgent(false); break;
            case "S": setWall(false); setReward(Constants.WHITE_REWARD); setAgent(true); break;
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

    public boolean isAgent() {
        return isAgent;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
    }
}
