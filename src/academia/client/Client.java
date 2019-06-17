package academia.client;

//Socket
import java.net.Socket;
//IO
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//Exceptions
import java.io.IOException;
//Pool of an undefined number of threads
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static void main(String[] args) {

        ExecutorService cachedPool;
        Socket clientSocket;

        try {

            clientSocket = new Socket("localhost", 5555);

            boolean quit = false;

            // Create in and out
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Allocate a Pool of an undefined number of threads
            cachedPool = Executors.newCachedThreadPool();

            // add Receive to the pool
            Receive receive = new Receive(in);
            cachedPool.submit(receive);

            // add Send to the pool
            Send send = new Send(out);
            cachedPool.submit(send);

            //Still ON until quit=true
            while (!quit) {



            }

            //Close the server
            System.out.println("Closing the server.");
            clientSocket.close();
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

