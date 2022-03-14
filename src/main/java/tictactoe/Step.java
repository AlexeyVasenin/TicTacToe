package tictactoe;

public class Step {
    private Integer numStep;
    private Long playerId;
    private String dot;

    public Step() {
    }


    public Integer getNumStep() {
        return numStep;
    }

    public void setNumStep(Integer numStep) {
        this.numStep = numStep;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }
}

