import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {

        // DECLARING SOCKETS
        final ServerSocket serverSocket; // Create ServerSocket
        final Socket clientSocket; // Creates Socket
        final BufferedReader in; // input data from ClientSocket
        final PrintWriter out; // prints data to the ClientSocket
        final Scanner input = new Scanner(System.in); // To read data from keyboard.


        // DECLARING INSTANCE Of ServerSocket
        try {
            serverSocket = new ServerSocket(5000); // Port to listen to client's request
            clientSocket = serverSocket.accept(); // accept() - To wait for request from client, once it receives one it accepts it & create an instance of the Socket class

            out = new PrintWriter(clientSocket.getOutputStream()); // Writes data
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Reads data


            // Create Sender Thread - Take input from user and send data to client.
            Thread sender = new Thread(new Runnable() {
                String msg; // Variable contains data writer by user annotation to override run method.
                @Override
                public void run() { // Run contains code thread will execute
                    while (true) {
                        msg = input.nextLine(); // input data from user's keyboar
                        out.println(msg); // prints data stored in msg in clientSocket
                        out.flush(); // Forces sending of data
                    }
                }
            });
            sender.start();

            // Receive Thread - Read data sent from client
            Thread receive = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String msg = in.readLine(); // Reads data from clientSocket using "in" Object

                        while(msg != null) { // While client connected to server
                            System.out.println("Client" + msg); // Print to screen message sent by client
                            msg = in.readLine(); // read data from the clientSocket using "in" object
                        }

                        System.out.println("Client Disconnected");

                        // Closing Sockets & Streams
                        out.close();
                        clientSocket.close();
                        serverSocket.close();
                 } catch (IOException e) { // If msg == null means client not connected anymore
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
