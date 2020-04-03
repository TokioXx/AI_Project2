package LegendarySannin;

import java.io.IOException;
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
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (board[i][j] == '-')
                    continue;

                for (int di = -2; di <= 2; di++) {
                    for (int dj = -2; dj <= 2; dj++) {
                        int x = i + di, y = j + dj;
                        if (x < 0 || x >= len || y < 0 || y >= len)
                            continue;
                        if (board[x][y] != '-')
                            continue;

                        res.add(x * len + y);
                    }
                }
            }
        }

        if (res.size() == 0 && board[len / 2][len / 2] == '-')
            res.add((len / 2) * (len + 1));

        return res;
    }

    public State minimize(char[][] board, int depth, long alpha, long beta) {
        State s = new State(-1, -1, Long.MAX_VALUE);

        Set<Integer> avaliablePosition = getAvaliablePosition(board);
        if (avaliablePosition.size() == 0)
            return null;

        for (int p : avaliablePosition) {
            int x = p / board.length, y = p % board.length;
            // System.out.println(String.format("Minimize(%d) (%d, %d)", depth, x, y));
            board[x][y] = priority == 2 ? 'X' : 'O';

            State ns = value(board, depth + 1, alpha, beta);
            if (ns.score < s.score)
                s = new State(x, y, ns.score);
            if (s.score <= alpha) {
                this.unmark(board, x, y);
                break;
            }
            beta = Math.min(beta, s.score);

            this.unmark(board, x, y);
        }

        // System.out.println(String.format("Minimize(%d) (%d, %d) %d", depth, s.x, s.y,
        // s.score));
        return s;
    }

    public State maximize(char[][] board, int depth, long alpha, long beta) {
        State s = new State(-1, -1, Long.MIN_VALUE);

        Set<Integer> avaliablePosition = getAvaliablePosition(board);
        if (avaliablePosition.size() == 0)
            return null;

        for (int p : avaliablePosition) {
            int x = p / board.length, y = p % board.length;
            // System.out.println(String.format("Maximize(%d) (%d, %d)", depth, x, y));
            this.mark(board, x, y);

            State ns = value(board, depth + 1, alpha, beta);
            if (ns.score > s.score)
                s = new State(x, y, ns.score);
            if (s.score >= beta) {
                this.unmark(board, x, y);
                break;
            }
            alpha = Math.max(alpha, s.score);

            this.unmark(board, x, y);
        }

        // System.out.println(String.format("Maximize(%d) (%d, %d) %d", depth, s.x, s.y,
        // s.score));
        return s;
    }

    public State value(char[][] board, int depth, long alpha, long beta) {
        if (depth > this.maxDepth) {
            long score = evaluate(board);
            // System.out.println(String.format("Terminal(%d) (%d)", depth, score));
            return new State(-1, -1, score);
        }

        if (depth % 2 == 1)
            return maximize(board, depth, alpha, beta);
        else
            return minimize(board, depth, alpha, beta);
    }

    public long evaluate(char[][] board) {
        long score = 0;

        score += evaluateMomentum(board);
        // score += evaluateCross(board);
        return score;
    }

    public long evaluateCross(char[][] board) {
        int sign = this.priority == 1 ? 1 : -1;

        long score = 0;
        score += sign * this.evaluateCrossFor(board, 'X');
        score -= sign * this.evaluateCrossFor(board, 'O');

        return score;
    }

    public int evaluateCrossForHelper(char[][] board, char target, int x, int y, int dx, int dy) {
        if (board[x][y] != target) return 0;

        int res = 0, count = 0;
        for (int i = -3; i <= 3; i ++) {
            if (board[x + i * dx][y + i * dy] == target) count ++;
            else count = 0;

            res = Math.max(res, count);
        }

        return res;
    }

    public long evaluateCrossFor(char[][] board, char target) {
        long score = 0;
        for (int i = 3; i < board.length - 3; i ++) {
            for (int j = 3; j < board.length - 3; j ++) {

                int a = evaluateCrossForHelper(board, target, i, j, -1, -1);
                int b = evaluateCrossForHelper(board, target, i, j, 1, 1);
                int c = evaluateCrossForHelper(board, target, i, j, 1, 0);
                int d = evaluateCrossForHelper(board, target, i, j, 0, 1);

                int res = a + b + c + d;
                int max = Math.max(a, Math.max(b, Math.max(c, d)));

                // System.out.println(String.format("EvaluateCrossFor(%d, %d): res: %d, max: %d", i, j, res, max));
                score += (long) (Math.pow(10, max - 1)) * res;
            }
        }

        return score;
    }

    public long evaluateMomentum(char[][] board) {
        long score = 0;
        int sign = this.priority == 1 ? 1 : -1;
        for (String s : Helper.enumerates(board)) {
            long sx = sign * evaluateRowMomentum(s, 'X');
            long so = -sign * evaluateRowMomentum(s, 'O');
            score += sx + so;
            // System.out.println(String.format("Evaluate(%s): %d", s, sx + so));
        }

        return score;
    }

    public long evaluateRowMomentum(String s, char c) {
        long score = 0;

        if (Pattern.matches(String.format("[%c]{5}", c), s)) return Integer.MAX_VALUE / 2;
        if (Pattern.matches(String.format("[%c]{6,}", c), s)) return Integer.MAX_VALUE;

        Pattern p = Pattern.compile(String.format("-*[%c]+-*", c));
        Matcher m = p.matcher(s);

        if (!m.find())
            return 0;
        String str = m.group();
        while (m.find(m.end())) {
            if (m.group().length() > str.length())
                str = m.group();
        }

        if (str.length() < Board.TARGET) return 0;
        int l = str.indexOf(c), r = str.lastIndexOf(c);

        int len = r - l + 1;
        score = (int) (Math.pow(10, len)) * (str.length() - len);

        // System.out.println(String.format("Evaluate[%c] (%s): %d", c, s, score));
        return score;
    }

    public void move(char[][] board) {
        char[][] b1 = board.clone();
        for (int i = 0; i < b1.length; i++)
            b1[i] = board[i].clone();
        State s = value(b1, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        mark(board, s.x, s.y);
        try {
            if (Constant.GAMEID == null) return;
            requestMove(s.x, s.y);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(String.format("Agent %d make a move (%d, %d)", priority, s.x, s.y));
    }
}