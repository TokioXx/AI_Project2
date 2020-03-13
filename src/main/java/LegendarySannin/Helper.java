package LegendarySannin;

import java.util.LinkedList;
import java.util.List;

public class Helper {
    public static List<String> enumerates(char[][] board) {
        List<String> res = new LinkedList<>(); 
        // ---
        for (int x = 0; x < board.length; x ++) res.add(collect(board, x, 0, 0, 1));
        // |||
        for (int y = 0; y < board.length; y ++) res.add(collect(board, 0, y, 1, 0));
        // \\\
        for (int x = 0; x < board.length; x ++) res.add(collect(board, x, 0, 1, 1));
        for (int y = 1; y < board.length; y ++) res.add(collect(board, 0, y, 1, 1));
        // ///
        for (int x = board.length - 1; x >= 0; x --) res.add(collect(board, x, 0, -1, 1));
        for (int y = 1; y < board.length; y ++) res.add(collect(board, board.length - 1, y, -1, 1));

        return res;
    }

    private static String collect(char[][] board, int x, int y, int dx, int dy) {
        StringBuilder sb = new StringBuilder();
        int len = board.length;

        while (x >= 0 && x < len && y >= 0 && y < len) {
            sb.append(board[x][y]); 

            x += dx;
            y += dy;
        }
        
        return sb.toString();
    }
}