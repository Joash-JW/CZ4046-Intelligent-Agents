
/*
* A cell class contains:
* 1. isWall - whether it contains a wall
* 2. reward - reward of this cell
* 3. cellPolicy - Policy of the cell
* */
public class Cell {
    private boolean isWall;
    private double reward;
    private String cellPolicy;
    private boolean isAgent;

    public Cell(String s) {
        switch (s) {
            case "P": setWall(false); setReward(1); setAgent(false); break;
            case "M": setWall(false); setReward(-1); setAgent(false); break;
            case "W": setWall(true); setReward(-10); setAgent(false); break;
            case "S": setWall(false); setReward(-0.04); setAgent(true); break;
            default: setWall(false); setReward(-0.04); break;
        }
    }

    /*Getters and setters*/
    public boolean isWall() {
        return isWall;
    }

    public double getReward() {
        return reward;
    }

    public String getCellPolicy() {
        return cellPolicy;
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

    public void setCellPolicy(String cellPolicy) {
        this.cellPolicy = cellPolicy;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
    }
}
