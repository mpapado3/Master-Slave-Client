package com.third_sub_ex;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Master {
    private int slavePort;
    private int clientPort;
    private SlaveThread slaveThread;
    private ClientThread clientThread;
    private boolean running = false;
    public static ArrayList<ClientThread> connectedClients; // Client connection List

    public Master(int slavePort, int clientPort) {
        this.slavePort = slavePort;
        this.clientPort = clientPort;
        this.connectedClients = new ArrayList<>(); //Αρχικοποιούμε τον μετρητή για τις συνδέσεις που έχουμε με slave
    }

    public void startServer() {
        try {
            this.slaveThread = new SlaveThread(slavePort);
            this.clientThread = new ClientThread(clientPort);
            System.out.println( "Αναμονή για σύνδεση client / slave" );
            slaveThread.start();
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        this.slaveThread.interrupt();
        this.clientThread.interrupt();

    }

    class SlaveThread extends Thread {
        private ServerSocket slaveSocket;

        SlaveThread(int slavePort) throws IOException {
            this.slaveSocket = new ServerSocket(slavePort);
        }
        @Override
        public void run() {
            running = true;
            while (running) {
                try {
                    // Call accept() to receive the next connection
                    Socket slSocket = slaveSocket.accept();
                    System.out.println("Δημιουργήθηκε μια νέα σύνδεση Slave");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ClientThread extends Thread {
        private ServerSocket clientSocket;

        ClientThread(int clientPort) throws IOException {
            this.clientSocket = new ServerSocket(clientPort);
        }

        @Override
        public void run() {
            running = true;
            while (running) {
                try {
                    Socket clSocket = clientSocket.accept();

                    ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clSocket);
                    clientRequestHandler.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Master server = new Master( 30091, 18889);
        server.startServer();
        // Automatically shutdown in 1 minute
        try {
            Thread.sleep( 60000 );
        } catch(Exception e) {
            e.printStackTrace();
        }
        server.stopServer();
    }

    class ClientRequestHandler extends Thread {
        private Socket socket;

        ClientRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Δημιουργήθηκε μια νέα σύνδεση Client");

                String inputLine;
                //Όσο έχουμε μηνύματα από τον client συνεχίζουμε
                while ((inputLine = in.readLine())  != null) {
                    //Τυπώνουμε στην κονσόλα το μήνυμα που έστειλε ο client
                    System.out.println("Client: " + inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class SlaveRequestHandler extends Thread {
        private Socket socket;

        SlaveRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader incoming = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    class RequestHandler extends Thread {
//        private Socket slaveSocket;
//        private Socket clientSocket;
//
//        RequestHandler(Socket slaveSocket, Socket clientSocket) {
//            this.slaveSocket = slaveSocket;
//            this.clientSocket = clientSocket;
//        }
//
//        @Override
//        public void run() {
//            try {
//                System.out.println("Δημιουργήθηκε μια νέα σύνδεση");
//
//                // Get input and output streams
//                BufferedReader slaveIn = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream()));
//                PrintWriter slaveOut = new PrintWriter(slaveSocket.getOutputStream());
//                BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream());
//
////                //Στένουμε μηνύματα καλοσορίσματος στον client και τον ενημερώνουμε ότι παίζει πρώτος
////                slaveOut.println("Θα ξεκινήσουμε ένα παιχνίδι Πέτρα, Ψαλίδι Χαρτί!");
////                slaveOut.flush();
////                slaveOut.println("Ξεκινάς πρώτος. Ποιά είναι η επιλογή σου?");
////                slaveOut.flush();
////                //Δημιουργούμε String πίνακα με τις 3 επιλογές
////                String[] selection = new String[]{"Πέτρα", "Ψαλίδι", "Χαρτί"};
////                //Δημιουργούμε string μεταβλητή που θα αποθηκεύει τα μηνύματα του client
////                String inputLine;
////                //Όσο έχουμε μηνύματα από τον client συνεχίζουμε
////                while ((inputLine = slaveIn.readLine()) != null) {
////                    //Τυπώνουμε στην κονσόλα το μήνυμα που έστειλε ο client
////                    System.out.println("Server: " + inputLine);
////                    //Δημιουργούμε έναν generator τυχαίων αριθμών από το 0 ως το 2.
////                    Random rnd = new Random();
////                    //Στέλνουμε στον client την τυχαία επιλογή που έκανε ο server
////                    slaveOut.println(selection[rnd.nextInt(3)]);
////                    slaveOut.flush();
////                    //Αν ο χρήστης έχει δώσει "ο" βγαίνουμε από το while
////                    if (inputLine.equals("ο"))
////                        break;
////                }
//
//                // Close our connection
//                slaveIn.close();
//                slaveOut.close();
//                slaveSocket.close();
//
//                System.out.println("Έκλεισε η σύνδεση");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
