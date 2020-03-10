package LegendarySannin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class State {
    int x;
    int y;
    int score;

    public State(int x, int y, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
    }
}

public class RationalAgent extends Agent {
    private static final Logger LOGGER = Logger.getLogger( RationalAgent.class.getName() );

    public RationalAgent(int p, int depth) {
        super(p, depth);
    }

    public Set<Integer> getAvaliablePosition(char[][] board, char target) {
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

    public State value(char[][] board, int depth) {
        if (depth > this.maxDepth) {
            int score = evaluate(board);
            // System.out.println("Value Score: " + score);
            // for (char[] b: board) System.out.println(Arrays.toString(b));
            return new State(-1, -1, score);
        }
        char target = (depth + priority) % 2 == 0 ? 'X' : 'O';
        Set<Integer> avaliablePosition = getAvaliablePosition(board, target);

        State s = new State(0, 0, depth % 2 == 1 ? Integer.MIN_VALUE : Integer.MAX_VALUE);
        for (int p: avaliablePosition) {
            int x = p / board.length, y = p % board.length;

            board[x][y] = target;

            State ns = value(board, depth + 1);
            // System.out.println(String.format("(%d) [%d, %d] [%d, %d]: %d", depth, x, y, ns.x, ns.y, ns.score));

            if (depth % 2 == 1 && ns.score > s.score) s = new State(x, y, ns.score);
            else if (depth % 2 == 0 && ns.score < s.score) s = new State(x, y, ns.score);

            board[x][y] = '-';
        }
        
        System.out.println(String.format("Final (%d) [%d, %d]: %d", depth, s.x, s.y, s.score));

        return s;
    }

    public int evaluate(char[][] board) {
        int score = 0;

        score += evaluateMomentum(board);
        score += evaluateCompactness(board);

        return score;
    }

    public int evaluateCompactness(char[][] board) {
        int score = 0, m = board.length / 2;
        int sign = this.priority == 1 ? 1 : -1;

        for (int i = 0; i < board.length; i ++) {
            for (int j = 0; j < board.length; j ++) {
                char c = board[i][j];

                if (c == '-') continue;
                else if (c == 'X') score -= sign * (Math.abs(i - m) + Math.abs(j - m));
                else if (c == 'O') score += sign * (Math.abs(i - m) + Math.abs(j - m));
            }
        }

        return score;
    }

    public int evaluateMomentum(char[][] board) {
        int score = 0;

        // ---
        for (int x = 0; x < board.length; x ++) score += scoreRow(board, x, 0, 0, 1);
        // |||
        for (int y = 0; y < board.length; y ++) score += scoreRow(board, 0, y, 1, 0);
        // \\\
        for (int x = 0; x < board.length; x ++) score += scoreRow(board, x, 0, 1, 1);
        for (int y = 1; y < board.length; y ++) score += scoreRow(board, 0, y, 1, 1);
        // ///
        for (int x = board.length - 1; x >= 0; x --) score += scoreRow(board, x, 0, -1, 1);
        for (int y = 1; y < board.length; y ++) score += scoreRow(board, board.length - 1, y, -1, 1);

        return score;
    }

    public int scoreRow(char[][] board, int x, int y, int dx, int dy) {
        StringBuilder sb = new StringBuilder();
        int len = board.length;

        while (x >= 0 && x < len && y >= 0 && y < len) {
            sb.append(board[x][y]); 

            x += dx;
            y += dy;
        }
        
        int score = score(sb.toString());
        // LOGGER.info(String.format("ScoreRow(%s): %d", sb.toString(), score));

        return score;
    }

    public int score(String s) {
        int sign = this.priority == 1 ? 1 : -1;
        int score = 0;

        Pattern px = Pattern.compile("[-O]X+[-O]");
        Pattern po = Pattern.compile("[-X]O+[-X]");
        Matcher mx = px.matcher(s);
        Matcher mo = po.matcher(s);

        if (mx.find()) {
            String str = mx.group();

            while (mx.find(mx.start() + 2)) {
                if (mx.group().length() > str.length()) str = mx.group();
            }

            int sx = sign * (int) Math.pow(10, str.length());
            if (str.charAt(0) != '-') sx /= 10;
            if (str.charAt(str.length() - 1) != '-') sx /= 10;
            
            score += sx;
        }

        if (mo.find()) {
            String str = mo.group();

            while (mo.find(mo.start() + 2)) {
                if (mo.group().length() > str.length()) str = mo.group();
            }

            int so = -sign * (int) Math.pow(10, str.length());
            if (str.charAt(0) != '-') so /= 10;
            if (str.charAt(str.length() - 1) != '-') so /= 10;
            
            score += so;
        }

        return score;
    }

    public void move(char[][] board) {
        char[][] b1 = board.clone();
        for (int i = 0; i < b1.length; i ++) b1[i] = board[i].clone();
        State s = value(b1, 1);
        mark(board, s.x, s.y);

        LOGGER.info(String.format("Agent %d make a move (%d, %d)", priority, s.x, s.y));
    }
}