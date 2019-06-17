/*
*
*  terminal: nc localhost 5555
*  type: !commands
*
*/

package academia;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    //portCreater();
    private int portNumber = 5555;

    // Sockets
    private Socket clientSocket = null;
    private ServerSocket serverSocket;

    // Pool of an undefined number of threads
    private ExecutorService cachedPool;

    private boolean quit;
    private LinkedList<ClientHandler> containerClients = new LinkedList<ClientHandler>();

    //Constructor
    public void start() {

        try {

            //Create Socket
            serverSocket = new ServerSocket(portNumber);

            //Create Pool
            cachedPool = Executors.newCachedThreadPool();

            // Stay on
            while (!quit) {

                System.out.println("\nWaiting for connection.\n");

                clientSocket = serverSocket.accept(); //block method

                // Create new client
                ClientHandler ch = new ClientHandler(clientSocket); //Runnable

                // Add Client to the List
                containerClients.add(ch);

                // Allocate a Pool of an undefined number of threads
                cachedPool.submit(ch);

            }

            // shut down Client Socket
            clientSocket.close();

            // shut down the executor immediately, even if the submitted tasks haven't finished its execution
            cachedPool.shutdown();

            //Exit
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void broadcast(String msg, String name) {

        String Time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println(Thread.currentThread().getName() + "("+Time+") <" + name + "> " + msg);
        Log.writting( Thread.currentThread().getName() + "("+Time+") <" + name + "> " + msg) ;

        synchronized (containerClients) {
            for (int i = 0; i < containerClients.size(); i++) {
                //avoid to send the msg to the author
                if (name != containerClients.get(i).name) {
                    containerClients.get(i).out.println("("+Time+") <" +name + "> " + msg);
                }
            }
        }
    }

    //Nested CLASS
    public class ClientHandler implements Runnable {

        Socket clientSocket;
        BufferedReader in;
        PrintWriter out;
        String line;
        String name = "No Name";

        //CONSTRUCTOR
        public ClientHandler(Socket clientsocket) throws IOException {
            this.clientSocket = clientsocket;
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                while (true) {

                    if (name.equals("No Name")) {

                        whatsYourName();
                        broadcast(name + " entering the chat", name);

                    } else {

                        line = in.readLine();

                        //Null msg
                        if (line != null || !line.equals("null")) {

                            //User wants to leave the server
                            switch(line) {
                                case "!quit":
                                    removeClient();
                                    broadcast("leave the server." , name);
                                    break;
                                case "!nickname":
                                    String oldName = name;
                                    whatsYourName();
                                    broadcast(oldName + ": Changed is name",name);
                                    break;
                                case "!commands":
                                    out.println("!quit !nickname !commands");
                                    break;
                                default:
                                    broadcast(line, name);
                            }

                        }else{
                            removeClient();
                        }
                    }
                }
            } catch (
                    IOException e) {
                System.out.println(e);
            }
        }

        public void removeClient() {
            System.out.println("Remove Client");
            synchronized (containerClients) {

                containerClients.remove(this);

                try {
                    Thread.currentThread().interrupt();
                    System.out.println("close socket");
                    clientSocket.close();
                    out.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void whatsYourName() {
            out.println("Whats your name?");
            try {
                this.name = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }//.Nested CLASS

}// .Server CLASS