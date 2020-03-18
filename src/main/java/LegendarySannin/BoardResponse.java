package LegendarySannin;

public class BoardResponse {
    private String output;
    private int target;
    private String code;
 
    public BoardResponse() {
    }

    public char[][] getBoard() {
        String[] split = output.split("\n");

        char[][] res = new char[split.length][split[0].length()];
        for (int i = 0; i < split.length; i ++) res[i] = split[i].toCharArray();

        return res;
    }
}
