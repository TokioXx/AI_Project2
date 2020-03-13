package LegendarySannin;

import org.junit.Assert;
import org.junit.Test;

public class RatinoalAgentTest {

    @Test
    public void testScore() {
        RationalAgent agent = new RationalAgent(1, 1);
        Assert.assertTrue(agent.evaluateRowMomentum("-X-", 'X') > agent.evaluateRowMomentum("-XO", 'X'));
        Assert.assertTrue(agent.evaluateRowMomentum("-XXX-", 'X') > agent.evaluateRowMomentum("-XX-O", 'X'));
        Assert.assertTrue(agent.evaluateRowMomentum("-XXXX-", 'X') > agent.evaluateRowMomentum("-XXX-", 'X'));
    }

    @Test
    public void testGetAvailablePositions() {
        char[][] board = {
            { 'X', 'X', 'X' },
            { 'X', '-', 'X' },
            { 'X', 'X', 'X' },
        };

        Assert.assertEquals(new RationalAgent(1, 1).getAvaliablePosition(board).size(), 1);
    }

    @Test
    public void testGetAvailablePositions2() {
        char[][] board = {
            { '-', 'X', '-' },
            { '-', '-', '-' },
            { '-', '-', '-' },
        };

        Assert.assertEquals(new RationalAgent(1, 1).getAvaliablePosition(board).size(), 5);
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