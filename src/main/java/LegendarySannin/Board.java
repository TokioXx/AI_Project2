package LegendarySannin;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Board extends JPanel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int size;
    private char[][] board;
    private JFrame frame;

    public Board(int size, char[][] board) {
        this.frame = new JFrame();
        this.size = size;
        this.board = board;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for(int i = 0; i < this.size; i ++){
            for(int j = 0; j < this.size; j ++){
                g.clearRect(i * Constant.WIDTH, j * Constant.WIDTH, Constant.WIDTH - 2, Constant.WIDTH - 2);
            }
        }
        g.setFont(new Font("Monaco", Font.PLAIN, 40));
        for(int i = 0; i < this.size; i ++){
            for(int j = 0; j < this.size; j ++){
                if (this.board[i][j] != 0) g.drawString(String.valueOf(board[i][j]), (i) * Constant.WIDTH + 10, (j + 1) * Constant.WIDTH - 10);
            }
        }

        int fi = hasFinished();
        if (fi != 0) {
            g.drawString(fi == 1 ? "Cong!! You Win !!" : "Ooops!! You Lose!!", Constant.WIDTH, 5 * Constant.WIDTH - 10);
        }
    }

    public void update() {
        this.frame.repaint();
        this.frame.revalidate();
    }

    public int hasFinished() {
        int count = 0;
        for(int i = 0; i < this.size; i ++){
            for(int j = 0; j < this.size; j ++){
                if (this.board[i][j] != 0) count ++;
            }
        } 
        if (count > size * size - 10) return 1;
        return 0;
    }

    public void init() {
        this.frame.setSize(Constant.WIDTH * this.size, Constant.WIDTH * this.size + 20);
        this.frame.getContentPane().add(this);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }
}