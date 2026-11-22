package main;

import java.util.HashMap;
import java.util.stream.Stream;

public class Game {

    private final Frame[] frames = new Frame[10];
    private final HashMap<Frame, Integer> strikes = new HashMap<>();
    private int currentFrameIndex;

    public Frame[] getFrames() {
        return frames;
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public void initializeGame() {
        for (int i = 0; i < frames.length; i++) {
            if (i == 9) {
                frames[i] = new Frame(3);
            } else {
                frames[i] = new Frame(2);
            }
        }
        currentFrameIndex = 0;
    }

    public void roll(int pinsKnockedDown) {
        var frame = frames[currentFrameIndex];

        if (frame.getRollIndex() != 2) {
            frame.score(pinsKnockedDown);
        } else {
            if (frame.isContainedStrike() || frame.isContainedSpare()) {
                frame.score(pinsKnockedDown);
            }
        }


        if (shouldApplySpareBonus()) {
            handleSpareBonus(pinsKnockedDown);
        }

        handleStrikeBonus(pinsKnockedDown);

        if (pinsKnockedDown == 10 && getCurrentFrameIndex() != 9) {
            strikes.put(frame, 2);
        }

        if (shouldAdvanceCurrentFrameIndex(pinsKnockedDown, frame)) {
            currentFrameIndex++;
        }
    }

    private boolean shouldApplySpareBonus() {
        return getCurrentFrameIndex() > 0 && frames[getCurrentFrameIndex() - 1].isContainedSpare();
    }

    private void handleSpareBonus(int pinsKnockedDown) {
        var frame = getFrames()[getCurrentFrameIndex() - 1];
        var roll = frame.getRolls()[frame.getRollIndex() - 1];
        roll.setPins(roll.getPins() + pinsKnockedDown);
    }

    private boolean shouldAdvanceCurrentFrameIndex(int pinsKnockedDown, Frame frame) {
        return getCurrentFrameIndex() != 9 && (frame.getRollIndex() == frame.getRolls().length || pinsKnockedDown == 10);
    }

    private void handleStrikeBonus(int pinsKnockedDown) {
        for (var frame : strikes.entrySet()) {
            if (frame.getValue().equals(0)) {
                continue;
            }
            var roll = frame.getKey().getRolls()[1];
            roll.setPins(roll.getPins() + pinsKnockedDown);
            frame.setValue(frame.getValue() - 1);
        }
    }

    public int score() {
        return Stream.of(frames).map(Frame::getRolls).flatMap(Stream::of).map(Roll::getPins).reduce(Integer::sum).orElse(0);
    }

}
