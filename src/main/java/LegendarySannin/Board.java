package LegendarySannin;

import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

class Board extends JPanel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int SIZE = 12;
    public static final int TARGET = 6;

    private char[][] board;
    private JFrame frame;

    public Board(char[][] board) {
        this.frame = new JFrame();
        this.board = board;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for(int i = 0; i < SIZE; i ++){
            for(int j = 0; j < SIZE; j ++){
                g.clearRect(i * Constant.WIDTH, j * Constant.WIDTH, Constant.WIDTH - 2, Constant.WIDTH - 2);
                g.drawString(String.valueOf(i + ", "), (i) * Constant.WIDTH + 5, (j + 1) * Constant.WIDTH - 10);
                g.drawString(String.valueOf(j), (i) * Constant.WIDTH + 20, (j + 1) * Constant.WIDTH - 10);
            }
        }
        g.setFont(new Font("Monaco", Font.PLAIN, 40));
        for(int i = 0; i < SIZE; i ++){
            for(int j = 0; j < SIZE; j ++){
                if (this.board[i][j] != '-') g.drawString(String.valueOf(board[i][j]), (i) * Constant.WIDTH + 10, (j + 1) * Constant.WIDTH - 10);
            }
        }

        // int fi = hasFinished();
        // if (fi == 3) {
        //     g.drawString("DRAW !!!", Constant.WIDTH, 5 * Constant.WIDTH - 10);
        // } else if (fi != 0) {
        //     g.drawString(fi == 1 ? "Cong!! You Win !!" : "Ooops!! You Lose!!", Constant.WIDTH, 5 * Constant.WIDTH - 10);
        // }
    }

    public void update() {
        this.frame.repaint();
        this.frame.revalidate();
    }

    public int hasFinished() {
        int res = 0, count = 0;

        for (int x = 0; x < board.length; x ++) for (int y = 0; y < board.length; y ++) if (board[x][y] != '-') count ++;
        if (count == SIZE * SIZE) return 3;

        // ---
        for (int x = 0; x < board.length; x ++) res = Math.max(res, validate(board, x, 0, 0, 1));
        // |||
        for (int y = 0; y < board.length; y ++) res = Math.max(res, validate(board, 0, y, 1, 0));
        // \\\
        for (int x = 0; x < board.length; x ++) res = Math.max(res, validate(board, x, 0, 1, 1));
        for (int y = 1; y < board.length; y ++) res = Math.max(res, validate(board, 0, y, 1, 1));
        // ///
        for (int x = board.length - 1; x >= 0; x --) res = Math.max(res, validate(board, x, 0, -1, 1));
        for (int y = 1; y < board.length; y ++) res = Math.max(res, validate(board, board.length - 1, y, -1, 1));

        return res; 
    }

    public int validate(char[][] board, int x, int y, int dx, int dy) {
        StringBuilder sb = new StringBuilder();
        int len = board.length;

        while (x >= 0 && x < len && y >= 0 && y < len) {
            sb.append(board[x][y]); 

            x += dx;
            y += dy;
        }

        char[] s = new char[TARGET];

        Arrays.fill(s, 'X');
        if (sb.toString().indexOf(String.valueOf(s)) >= 0) return 1;
        Arrays.fill(s, 'O');
        if (sb.toString().indexOf(String.valueOf(s)) >= 0) return 2;

        return 0;
    }

    public void init() {
        this.frame.setSize(Constant.WIDTH * SIZE, Constant.WIDTH * SIZE + 20);
        this.frame.getContentPane().add(this);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    public void addListener(MouseInputAdapter adapter) {
        this.addMouseListener(adapter);
    }
}