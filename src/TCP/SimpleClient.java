package TCP;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class SimpleClient {
    public static void main(String[] args) throws IOException {
        
		// Hardcode in IP and Port here if required
    	args = new String[] {"127.0.0.1", "30121"};
    	ArrayList<String> packets = new ArrayList();
    	
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        try (Socket clientSocket = new Socket(hostName, portNumber); PrintWriter requestWriter = // stream to write text requests to server
                new PrintWriter(clientSocket.getOutputStream(), true);  BufferedReader responseReader= // stream to read text response from server
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); BufferedReader stdIn = // standard input stream to get user's requests
                new BufferedReader(new InputStreamReader(System.in))) {






			//code starts here


            String userInput;
            String serverResponse = "";
            boolean done = false;
            System.out.println("Type \"start\" to start");

            while (!((serverResponse = responseReader.readLine())).equals("DONE")) {
                userInput = stdIn.readLine();

                requestWriter.println(userInput); // send packet to server


                System.out.println("SERVER RESPONDS: \"" + serverResponse + "\"");
                if(serverResponse.equals("done")){
                    while(!done){
                        requestWriter.println("Give me the next packet.");
                        int index = Integer.parseInt(responseReader.readLine());
                        String packet = responseReader.readLine();
                        System.out.println(index + " " + packet);
                        packets.set(index, packet);
                    }
                }







            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
