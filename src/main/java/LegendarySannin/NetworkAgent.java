package LegendarySannin;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.Request;
import okhttp3.Response;

public class NetworkAgent extends Agent {

    public NetworkAgent() {
        super();
    }

    public char[][] requestBoard() {
        return null;
    }

    public char[][] getBoard() throws IOException {
        Request request = new Request.Builder()
            .url(String.format("%s?%s%s", Constant.HOST, "type=boardString&gameId=", Constant.GAMEID))
            .addHeader("x-api-key", Constant.APIKEY)
            .addHeader("userid", Constant.USERID)
            .get()
            .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Gson gson = new Gson(); 
            BoardResponse board = gson.fromJson(response.body().string(), BoardResponse.class);
            return board.getBoard();
        }
    }

    public void move(char[][] board) {
        try {
            char[][] board2 = getBoard();

            for (int i = 0; i < board2.length; i ++) {
                for (int j = 0; j < board2.length; j ++) {
                    if (board2[i][j] != '-') board[i][j] = board2[i][j] == 'X' ? 'O' : 'X';
                } 
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}