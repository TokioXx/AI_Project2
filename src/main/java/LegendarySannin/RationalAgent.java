package LegendarySannin;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class State {
    int x;
    int y;
    long score;

    public State(int x, int y, long score) {
        this.x = x;
        this.y = y;
        this.score = score;
    }
}

public class RationalAgent extends Agent {
    public RationalAgent(int p, int depth) {
        super(p, depth);
    }

    public Set<Integer> getAvaliablePosition(char[][] board) {
        int len = board.length;
        Set<Integer> res = new HashSet<>();
        for (int i = 0; i < len; i ++) {
            for (int j = 0; j < len; j ++) {
                if (board[i][j] == '-') continue;

                for (int di = -1; di < 2; di ++) {
                    for (int dj = -1; dj < 2; dj ++) {
                        int x = i + di, y = j + dj;
                        if (x < 0 || x >= len|| y < 0 || y >= len) continue;
                        if (board[x][y] != '-') continue;
                        
                        res.add(x * len + y);
                    }
                }
            }
        }

        if (res.size() == 0 && board[len/2][len/2] == '-') res.add((len/2) * (len + 1));

        return res;
    }

    public State minimize(char[][] board, int depth, long alpha, long beta) {
        State s = new State(-1, -1, Long.MAX_VALUE);

        Set<Integer> avaliablePosition = getAvaliablePosition(board); 
        if (avaliablePosition.size() == 0) return null;

        for (int p: avaliablePosition) {
            int x = p / board.length, y = p % board.length;
            // System.out.println(String.format("Minimize(%d) (%d, %d)", depth, x, y));
            board[x][y] = priority ==  2 ? 'X' : 'O';

            State ns = value(board, depth + 1, alpha, beta);
            if (ns.score < s.score) s = new State(x, y, ns.score);
            if (s.score <= alpha) {
                this.unmark(board, x, y);
                break;
            }
            beta = Math.min(beta, s.score);

            this.unmark(board, x, y);
        }

        // System.out.println(String.format("Minimize(%d) (%d, %d) %d", depth, s.x, s.y, s.score));
        return s;
    }
    public State maximize(char[][] board, int depth, long alpha, long beta) {
        State s = new State(-1, -1, Long.MIN_VALUE);

        Set<Integer> avaliablePosition = getAvaliablePosition(board); 
        if (avaliablePosition.size() == 0) return null;

        for (int p: avaliablePosition) {
            int x = p / board.length, y = p % board.length;
            // System.out.println(String.format("Maximize(%d) (%d, %d)", depth, x, y));
            this.mark(board, x, y);

            State ns = value(board, depth + 1, alpha, beta);
            if (ns.score > s.score) s = new State(x, y, ns.score);
            if (s.score >= beta) {
                this.unmark(board, x, y);
                break;
            }
            alpha = Math.max(alpha, s.score);

            this.unmark(board, x, y);
        }

        // System.out.println(String.format("Maximize(%d) (%d, %d) %d", depth, s.x, s.y, s.score));
        return s;
    }

    public State value(char[][] board, int depth, long alpha, long beta) {
        if (depth > this.maxDepth) {
            long score = evaluate(board);
            // System.out.println(String.format("Terminal(%d) (%d)", depth, score));
            return new State(-1, -1, score);
        }
        
        if (depth % 2 == 1) return maximize(board, depth, alpha, beta);
        else return minimize(board, depth, alpha, beta);
    }

    public long evaluate(char[][] board) {
        long score = 0;

        score += evaluateMomentum(board);
        return score;
    }

    public long evaluateMomentum(char[][] board) {
        long score = 0;
        int sign = this.priority == 1 ? 1 : -1;
        for (String s: Helper.enumerates(board)) {
            long sx = sign * evaluateRowMomentum(s, 'X');
            long so = -sign * evaluateRowMomentum(s, 'O');
            score += sx + so;
            // System.out.println(String.format("Evaluate(%s): %d", s, sx + so));
        }

        return score;
    }

    public long evaluateRowMomentum(String s, char c) {
        long score = 0;
        Pattern p = Pattern.compile(String.format("-?[%c]+-?", c));
        Matcher m = p.matcher(s);
        
        if (!m.find()) return 0;
        String str = m.group();
        while (m.find(m.end())) {
            if (m.group().length() > str.length()) str = m.group();
        }

        int len = str.length(), count = 1;
        if (str.charAt(0) == '-') count ++;
        if (str.charAt(str.length() - 1) == '-') count ++;
        score = (int) (Math.pow(10, len - count)) * count;

        // System.out.println(String.format("Evaluate[%c] (%s): %d", c, s, score));
        return score;
    }

    public void move(char[][] board) {
        char[][] b1 = board.clone();
        for (int i = 0; i < b1.length; i ++) b1[i] = board[i].clone();
        State s = value(b1, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        mark(board, s.x, s.y);

        // System.out.println(String.format("Agent %d make a move (%d, %d)", priority, s.x, s.y));
    }
}