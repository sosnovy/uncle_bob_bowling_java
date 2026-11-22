package test;

import main.Frame;
import main.Game;
import main.Roll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestGame {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void createNotNullGame() {
        assertNotNull(game);
    }

    @Test
    void defaultScore() {
        initializeGame();
        var score = game.score();

        assertEquals(0, score);
    }

    @Test
    void rollZero() {
        initializeGame();
        game.roll(0);
        assertEquals(0, game.score());
    }

    @Test
    void defaultFrame() {
        initializeGame();

        assertEquals(10, game.getFrames().length);
    }

    @Test
    void defaultRollReturnsZero() {
        var roll = new Roll();

        assertEquals(0, roll.getPins());
    }

    @Test
    void createFrameWithTwoRolls() {
        var frame = new Frame(2);
        assertEquals(2, frame.getRolls().length);
    }

    @Test
    void initializeGame() {
        game.initializeGame();
        var totalRollCount = Arrays.stream(game.getFrames()).map(frame -> frame.getRolls().length).reduce(Integer::sum).orElse(0);

        assertEquals(21, totalRollCount);
        assertEquals(0, game.getCurrentFrameIndex());

    }

    @Test
    void rollAllFrames() {
        initializeGame();
        for (int i = 0; i < 10; i++) {
            game.roll(5);
            game.roll(5);
        }
        assertEquals(190, game.score());
    }

    @Test
    void rollFrameFull() {
        var frame = new Frame(2);

        for (int i = 1; i <= frame.getRolls().length; i++) {
            frame.score(i);
        }

        assertEquals(3, Arrays.stream(frame.getRolls()).map(Roll::getPins).reduce(Integer::sum).orElse(0));
    }

    @Test
    void rollStrike() {
        initializeGame();

        assertEquals(0, game.getCurrentFrameIndex());
        game.roll(10);
        assertEquals(1, game.getCurrentFrameIndex());
    }

    @Test
    void rollSpare() {
        initializeGame();

        game.roll(5);
        game.roll(5);

        assertTrue(game.getFrames()[game.getCurrentFrameIndex() - 1].isContainedSpare());
    }

    @Test
    void rollSpareBonus() {
        rollSpare();
        game.roll(6);

        assertEquals(22, game.score());
    }

    @Test
    void bonusPointsAfterStrike() {
        rollStrike();
        game.roll(5);
        game.roll(4);
        assertEquals(28, game.score());

    }

    @Test
    void bonusPointsForThreeStrikes() {
        initializeGame();
        game.roll(10);
        game.roll(10);
        game.roll(10);
        assertEquals(60, game.score());
    }

    @Test
    void strikesOnly() {
        initializeGame();
        for (int i = 0; i < 12; i++) {
            game.roll(10);
        }
        assertEquals(300, game.score());
    }

    @Test
    void cannotRollFinalBonusRoll() {
        initializeGame();
        for (int i = 0; i < 10; i++) {
            game.roll(2);
            game.roll(2);
        }
        game.roll(5);

        assertEquals(40, game.score());
    }

}
