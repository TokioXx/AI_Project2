package LegendarySannin;

// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.net.http.HttpResponse.BodyHandlers;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        // HttpClient client = HttpClient.newHttpClient();
        // HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://google.com")).build();

        // client.sendAsync(request, BodyHandlers.ofString())
        //  .thenApply(HttpResponse::body)
        //  .thenAccept(System.out::println)
        //  .join(); 

        int size = 10;
        char[][] board = new char[size][size];
        Board b = new Board(10, board);
        b.init();

        Agent agent = new Agent(1);
        Agent agent2 = new Agent(2);

        while (true) {
            agent.move(board);
            Thread.sleep(500);
            agent2.move(board);
            Thread.sleep(500);

            b.update();
            if (b.hasFinished() != 0) {
                Thread.sleep(3000);
                System.exit(0);
            }
        }
    }

}
