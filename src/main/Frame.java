package main;

public class Frame {

    private final Roll[] rolls;
    private int rollIndex = 0;

    private boolean containedStrike;
    private boolean containedSpare;


    public Frame(int rolls) {
        this.rolls = new Roll[rolls];
        for (int i = 0; i < this.rolls.length; i++) {
            this.rolls[i] = new Roll();
        }
    }

    public Roll[] getRolls() {
        return rolls;
    }

    public int getRollIndex() {
        return rollIndex;
    }

    public void score(int pins) {
        rolls[rollIndex].setPins(pins);

        if (pins == 10) {
            setContainedStrike(true);
        }
        if (rolledSpare(pins)) {
            setContainedSpare(true);
        }

        rollIndex++;

    }

    private boolean rolledSpare(int pins) {
        return rollIndex > 0 && rolls[rollIndex - 1].getPins() + pins == 10;
    }

    public boolean isContainedSpare() {
        return containedSpare;
    }

    public void setContainedSpare(boolean containedSpare) {
        this.containedSpare = containedSpare;
    }

    public boolean isContainedStrike() {
        return containedStrike;
    }

    public void setContainedStrike(boolean containedStrike) {
        this.containedStrike = containedStrike;
    }

}
