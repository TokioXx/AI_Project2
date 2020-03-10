package LegendarySannin;

abstract class Agent {
    protected int priority;
    protected int maxDepth;

    public Agent(int p, int d) {
        priority = p;
        maxDepth = d;
    }

    public void mark(char[][] board, int x, int y) {
        board[x][y] = priority ==  1 ? 'X' : 'O';
    }
    
    abstract public void move(char[][] board);
}