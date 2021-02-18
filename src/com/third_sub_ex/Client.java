package com.third_sub_ex;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final int NUMBERS = 50;
    private static final int AMPLITUDE = 100;
    private static int masterPort;

    public Client(int port) {
        this.masterPort = port;
    }

    public static void main(String[] args) throws IOException{
        String serverHostname = "127.0.0.1"; //Ορίζουμε την διεύθυνση που είναι ο σέρβερ

        System.out.println("Αναμονή για σύνδεση στον σέρβερ " + serverHostname + " στην πόρτα 30091.");
        //αρχικοποιούμε το socket, το buffer για τα εισερχόμενα μηνύματα και τα εξερχόμενα
        Socket echoSocket = null;
        BufferedReader in = null;
//        PrintWriter sout = null;
        try {
            echoSocket = new Socket(serverHostname, 18889); //Ορίζουμε την port που θα χρησιμοποιήσουμε
//            sout = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream())); //Ορίζουμε την μεταβλητή που ακούει τα μηνύματα που έρχονται από την σύνδεση
        } catch (UnknownHostException e) {
            System.err.println("Δεν μπορεί να πραγματοποιηθεί σύνδεση με τον σέρβερ: " + serverHostname); //Εμφανίζουμε μήνυμα αν υπάρξει κάποιο τυχαίο σφάλμα
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + serverHostname); //Εμφανίζουμε αν υπάρξει κάποιο Ι/Ο σφάλμα
            System.exit(1);
        }

//        System.out.println("Ο client συνδέθηκε!");
//        Random rnd = new Random();
//        for (int i=0; i<NUMBERS; i++) {
//            int num = rnd.nextInt(AMPLITUDE);
//            System.out.println(num);
//            sout.println(num);
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        sout.close();

        ClientOut clientOut = new ClientOut(echoSocket);
        clientOut.start();
        ClientIn clientIn = new ClientIn(in);
        clientIn.start();

        //Κλείνουμε όλες τις συνδέσεις και τα buffer
        in.close();
        echoSocket.close();
    }

    public static class ClientOut extends Thread {
        private PrintWriter out;

        public ClientOut(Socket echoSocket) throws IOException {
            this.out = new PrintWriter(echoSocket.getOutputStream(), true);;
        }

        @Override
        public void run() {
            System.out.println("Ο client συνδέθηκε!");
            Random rnd = new Random();
            try {
                for (int i=0; i<NUMBERS; i++) {
                    int num = rnd.nextInt(AMPLITUDE);
                    System.out.println(num);
                    out.println(num);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            out.close();
            }
        }


    public static class ClientIn extends Thread {
        private BufferedReader in;

        public ClientIn(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {

        }
    }

}
