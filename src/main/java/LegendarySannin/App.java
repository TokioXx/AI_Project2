package LegendarySannin;

import java.util.Arrays;

public class App {
    private static final long SLEEPTIME = 100;

    public static void main(String[] args) throws InterruptedException {
        char[][] board = new char[Board.SIZE][Board.SIZE];
        for (char[] row: board) Arrays.fill(row, '-');

        Board b = new Board(board);
        b.init();

        // Agent agent = new NetworkAgent();
        Agent agent = new RationalAgent(1, 2);
        Agent agent2 = new RationalAgent(2, 2);

        // ManualAgent agent2 = new ManualAgent(2, 2);
        // b.addListener(agent2.getAdapter());

        while (true) {
            // System.out.println("========= Step for Agent 1 =======");
            agent.move(board);
            b.update();
            Thread.sleep(SLEEPTIME);

            if (b.hasFinished() != 0) {
                Thread.sleep(SLEEPTIME * 10);
                System.exit(0);
            }

            // System.out.println("========= Step for Agent 2 =======");
            agent2.move(board);
            b.update();
            Thread.sleep(SLEEPTIME);

            if (b.hasFinished() != 0) {
                Thread.sleep(SLEEPTIME * 10);
                System.exit(0);
            }
        }
    }

}
