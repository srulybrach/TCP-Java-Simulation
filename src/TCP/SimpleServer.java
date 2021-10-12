package TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimpleServer {

    static Random rnd = new Random();

    public static void main(String[] args) throws IOException {
        args = new String[]{"30121"};
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
             Socket clientSocket1 = serverSocket.accept();  PrintWriter responseWriter1 = new PrintWriter(clientSocket1.getOutputStream(), true);  BufferedReader requestReader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream())) ) {






            //CODE STARTS HERE
            ArrayList<String> packets = new ArrayList<String>();
            ArrayList<Integer> metadata = new ArrayList<Integer>();
            String usersInput = "";
            String fullMessage = "";
            boolean swap = false; //starts on false, switches to true when the user is done


            responseWriter1.println("Enter your packets pressing the enter key between them");
            while (!((usersInput = requestReader1.readLine())).equals("DONE")){
                if ((!usersInput.equals("done") && !swap)) { //first we receive all the data that we need to recieve from the client
                    System.out.println("\"" + usersInput + "\" received");
                    fullMessage += (usersInput + " ");
                    responseWriter1.println(usersInput);
                    packets.add(usersInput);
                }else if(!swap) { //once the user indicates that all the data has been sent

                    for (int i = 0; i < packets.size(); i++)
                        metadata.add(i + 1);
                    int seed = rnd.nextInt();
                    Collections.shuffle(packets, new Random(seed));
                    Collections.shuffle(metadata, new Random(seed));
                    packets.add("DONE");
                    metadata.add(0);
                    swap = true;
                    responseWriter1.println("done");
                }
                else if (swap){ //THIS IS WHERE WE LIVE
                    for (int i = 0; i < packets.size(); i++) {
                        responseWriter1.println(metadata.get(i));
                        responseWriter1.println(packets.get(i));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(
                    "Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}
