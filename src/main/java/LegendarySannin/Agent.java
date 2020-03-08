package LegendarySannin;

import java.util.Random;

class Agent {
    private int priority;

    public Agent(int p) {
        priority = p;
    }
    
    public void move(char[][] board) {
        int w = board.length;

        while (true) {
            int x = new Random().nextInt(w), y = new Random().nextInt(w);
            if (board[x][y] == 0) {
                board[x][y] = priority == 1 ? 'X' : 'O';
                return;
            }
        }
    }
}