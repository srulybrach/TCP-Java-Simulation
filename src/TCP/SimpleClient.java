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
            System.out.println("Write your full sentence here that will be broken up into packets.");
            String userInput = stdIn.readLine();
            requestWriter.println(userInput);
            int index = 0;
            String packet;
            boolean firstRun = true;
          while( ( areEmptySpots(packets) && !firstRun) || firstRun   ){
              requestWriter.println("NOT DONE");
              packet = responseReader.readLine();
              index = Integer.parseInt(responseReader.readLine());
                while(packets.size() <= index)
                    packets.add("");
                if(!packet.equals("ERROR"))
                    packets.set(index, packet);
                if(packet.equals("DONE"))
                    firstRun = false;
            }
          requestWriter.println("DONE");
            for (String singlePacket:packets)
                System.out.println(singlePacket);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    public static boolean areEmptySpots(ArrayList<String> spots){
        for (String spot:spots)
            if ( spot.equals("") )
                return true;
        return false;
    }



}

