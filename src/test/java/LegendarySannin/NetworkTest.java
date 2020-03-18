package LegendarySannin;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class NetworkTest {
    /**
     * Rigorous Test :-)
     * 
     * @throws IOException
     */
    @Test
    public void requestMove() throws IOException
    {
        Agent agent = new RationalAgent(1, 2);
        assertTrue(agent.requestMove(1, 1));
    }

    @Test
    public void requestBoard() throws IOException
    {
        NetworkAgent agent = new NetworkAgent();
        agent.move(null);
    }
}
