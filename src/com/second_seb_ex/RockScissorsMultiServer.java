package com.second_seb_ex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class RockScissorsMultiServer extends Thread{
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;

    public RockScissorsMultiServer(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                System.out.println( "Αναμονή για σύνδεση client" );

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket );
                requestHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        RockScissorsMultiServer server = new RockScissorsMultiServer( 10007 );
        server.startServer();
        // Automatically shutdown in 1 minute
        try {
            Thread.sleep( 60000 );
        } catch(Exception e) {
            e.printStackTrace();
        }
        server.stopServer();
    }

    class RequestHandler extends Thread {
        private Socket socket;

        RequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println("Δημιουργήθηκε μια νέα σύνδεση");

                // Get input and output streams
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                //Στένουμε μηνύματα καλοσορίσματος στον client και τον ενημερώνουμε ότι παίζει πρώτος
                out.println("Θα ξεκινήσουμε ένα παιχνίδι Πέτρα, Ψαλίδι Χαρτί!");
                out.println("Ξεκινάς πρώτος. Ποιά είναι η επιλογή σου?");
                //Δημιουργούμε String πίνακα με τις 3 επιλογές
                String[] selection = new String[]{"Πέτρα", "Ψαλίδι", "Χαρτί"};
                //Δημιουργούμε string μεταβλητή που θα αποθηκεύει τα μηνύματα του client
                String inputLine;
                //Όσο έχουμε μηνύματα από τον client συνεχίζουμε
                while ((inputLine = in.readLine()) != null) {
                    //Τυπώνουμε στην κονσόλα το μήνυμα που έστειλε ο client
                    System.out.println("Server: " + inputLine);
                    //Δημιουργούμε έναν generator τυχαίων αριθμών από το 0 ως το 2.
                    Random rnd = new Random();
                    //Στέλνουμε στον client την τυχαία επιλογή που έκανε ο server
                    out.println(selection[rnd.nextInt(3)]);
                    //Αν ο χρήστης έχει δώσει "ο" βγαίνουμε από το while
                    if (inputLine.equals("ο"))
                        break;
                }

                // Close our connection
                in.close();
                out.close();
                socket.close();

                System.out.println("Έκλεισε η σύνδεση");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
