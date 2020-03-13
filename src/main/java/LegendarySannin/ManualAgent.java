package LegendarySannin;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

public class ManualAgent extends Agent {
    protected int priority;
    protected int maxDepth;

    private int x = -1;
    private int y = -1;

    public ManualAgent(int p, int d) {
        super(p, d);

        priority = p;
        maxDepth = d;
    }

    public void mark(char[][] board, int x, int y) {
        board[x][y] = priority == 1 ? 'X' : 'O';
    }

    public MouseInputAdapter getAdapter() {
        return new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                x = e.getX() / 50;
                y = e.getY() / 50;
            }
        };
    }

    public void move(char[][] board) {
        while (x == -1 || y == -1) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mark(board, x, y);

        x = -1;
        y = -1;
    }
}