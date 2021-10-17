package TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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
            String clientMessage = requestReader1.readLine(); //reads the packets the client sends the server

            List<String> packets = new ArrayList<String>(Arrays.asList(clientMessage.split(" "))); //parses those into individual packets
            ArrayList<Integer> metadata = new ArrayList<Integer>(); //create a metadata ArrayList of the same size to keep track of placement
            for (int i = 0; i < packets.size(); i++) //fill the metadata with the order of the packets
                metadata.add(i);
            int seed = rnd.nextInt(); //create a seed to shuffle both the packets and the metadata the same shuffle
            Collections.shuffle(packets, new Random(seed));
            Collections.shuffle(metadata, new Random(seed));
            metadata.add(packets.size()); //add size of packet and done to end of packets signifying the last packet
            packets.add("DONE");
            while (true) {
                if(requestReader1.readLine().equals("DONE"))
                    System.exit(0);
                for (int i = 0; i < packets.size(); i++) {
                    int odds = rnd.nextInt(100);
                    if (odds < 79) {
                        responseWriter1.println(packets.get(i));
                        responseWriter1.println(metadata.get(i));
                    } else {
                        responseWriter1.println("ERROR");
                        responseWriter1.println(0);
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