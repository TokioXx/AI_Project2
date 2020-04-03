package LegendarySannin;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

abstract class Agent {
    protected int priority;
    protected int maxDepth;
    protected final OkHttpClient httpClient = new OkHttpClient();

    public Agent(int p, int d) {
        priority = p;
        maxDepth = d;
    }

    public void mark(char[][] board, int x, int y) {
        board[x][y] = priority ==  1 ? 'X' : 'O';
    }

    public void unmark(char[][] board, int x, int y) {
        board[x][y] = '-';
    }

    public boolean requestMove(int x, int y) throws IOException {

        RequestBody body = new FormBody.Builder()
            .add("gameId", Constant.GAMEID)
            .add("type", "move")
            .add("teamId", Constant.TEAMID)
            .add("move", x + "," + y)
            .build();

        Request request = new Request.Builder()
            .url(Constant.HOST)
            .addHeader("x-api-key", Constant.APIKEY)
            .addHeader("userid", Constant.USERID)
            .post(body)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            System.out.println(response.body().string());
        }

        return true;
    }
    
    abstract public void move(char[][] board);
}