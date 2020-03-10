package LegendarySannin;

import org.junit.Assert;
import org.junit.Test;

public class RatinoalAgentTest {

    @Test
    public void testScore() {
        Assert.assertEquals(new RationalAgent(1, 1).score("-XX-XXXX-O"), 1000000);
        // Assert.assertEquals(new RationalAgent(1, 1).score("-OOOO-"), -1000000);
        // Assert.assertEquals(new RationalAgent(1, 1).score("-XXXO"), 10000);
        // Assert.assertEquals(new RationalAgent(1, 1).score("XOOX"), -100);
    }

    @Test
    public void testBoard1() {
        char[][] board = {
            { '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-' },
            { '-', 'X', '-', 'X', '-' },
            { '-', '-', '-', '-', '-' },
            { '-', '-', '-', '-', '-' },
        };

        // State s = new RationalAgent(1, 1).value(board, 1);
        // Assert.assertEquals(s.x, 2);
        // Assert.assertEquals(s.y, 2);

        State s = new RationalAgent(2, 3).value(board, 1);
        Assert.assertEquals(s.x, 2);
        Assert.assertEquals(s.y, 2);
    }

    @Test
    public void testBoard2() {
        char[][] board = {
            { 'O', '-', '-', '-', '-' },
            { '-', 'X', '-', '-', '-' },
            { '-', '-', 'X', '-', '-' },
            { '-', '-', '-', 'X', '-' },
            { '-', '-', '-', '-', '-' },
        };

        // State s = new RationalAgent(2).value(board, 1);
        // Assert.assertEquals(s.x, 4);
        // Assert.assertEquals(s.y, 4);
        State s = new RationalAgent(1, 1).value(board, 1);
        Assert.assertEquals(s.x, 4);
        Assert.assertEquals(s.y, 4);
    }

    @Test
    public void testEvaluateCompactness1() {
        char[][] board = {
            { 'X', 'X', 'X' },
            { 'X', '-', 'X' },
            { 'X', 'X', 'X' },
        };

        Assert.assertEquals(new RationalAgent(1, 1).evaluateCompactness(board), -2 * 4 * 10 + -1 * 4 * 10);
    }

    @Test
    public void testEvaluateCompactness2() {
        char[][] board = {
            { 'X', 'X', 'O' },
            { 'X', '-', 'X' },
            { 'X', 'X', 'X' },
        };

        Assert.assertEquals(new RationalAgent(2, 1).evaluateCompactness(board), 80);
    }

    @Test
    public void testEvaluateCompactness3() {
        char[][] board = {
            { '-', '-', '-' },
            { '-', '-', '-' },
            { '-', '-', '-' },
        };

        Assert.assertEquals(new RationalAgent(2, 1).evaluateCompactness(board), 0);
    }

    @Test
    public void testGetAvailablePositions() {
        char[][] board = {
            { 'X', 'X', 'X' },
            { 'X', '-', 'X' },
            { 'X', 'X', 'X' },
        };

        Assert.assertEquals(new RationalAgent(1, 1).getAvaliablePosition(board, 'X').size(), 1);
    }

    @Test
    public void testGetAvailablePositions2() {
        char[][] board = {
            { '-', 'X', '-' },
            { '-', '-', '-' },
            { '-', '-', '-' },
        };

        Assert.assertEquals(new RationalAgent(1, 1).getAvaliablePosition(board, 'X').size(), 5);
    }

    @Test
    public void testValue1() {
        char[][] board = {
            { '-', '-', '-' },
            { '-', '-', '-' },
            { '-', '-', '-' },
        };
        State s = new RationalAgent(1, 1).value(board, 1);

        Assert.assertEquals(s.x, 1);
        Assert.assertEquals(s.y, 1);
    }

    @Test
    public void testClone() {
        char[][] b = {{'-'}};
        char[][] b1 = b.clone();
        for (int i = 0; i < b.length; i ++) b1[i] = b[i].clone();

        b1[0][0] = 'x';
        Assert.assertEquals('-', b[0][0]);
    }
}