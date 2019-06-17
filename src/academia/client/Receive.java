package academia.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Receive implements Runnable {

    BufferedReader in = null;

    public Receive(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {

        while (true) {
            String input = null;
            try {
                input = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(input);
        }

    }
}
