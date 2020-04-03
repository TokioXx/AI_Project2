package LegendarySannin;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.Request;
import okhttp3.Response;

public class NetworkAgent extends Agent {

    // private Agent agent;

    public NetworkAgent(int p, int d) {
        super(p, d);

        priority = p;
        maxDepth = d;

        // this.agent = new RationalAgent(p, d);
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
            while (true) {
                char[][] board2 = getBoard();
                int count = 0;

                for (int i = 0; i < board2.length; i ++) {
                    for (int j = 0; j < board2.length; j ++) {
                        if (board2[i][j] != '-') {
                            board[i][j] = board2[i][j] == 'X' ? 'O' : 'X';
                            count ++;
                        }
                    } 
                } 

                if (count % 2 == this.priority % 2) break;
                Thread.sleep(1000 * 5);
            }

            // agent.move(board);
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}