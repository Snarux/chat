package academia.client;

import java.io.PrintWriter;
import java.util.Scanner;

public class Send implements Runnable {

    PrintWriter out;
    String written;

    public Send(PrintWriter out) {
        this.out = out;
    }

    public static String messageWritter() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    @Override
    public void run() {
        while (true) {

            written = messageWritter();
            out.println(written);

            //remote turn off
            //if(written.equals("quit")){
            //    System.exit(0);
            //}

        }
    }
}
