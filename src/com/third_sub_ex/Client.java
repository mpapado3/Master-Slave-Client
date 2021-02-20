package com.third_sub_ex;

import java.io.*;
import java.net.ServerSocket;
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

        ClientOut clientOut = new ClientOut(serverHostname);
        clientOut.start();
        ClientIn clientIn = new ClientIn(serverHostname);
        clientIn.start();
    }

    public static class ClientOut extends Thread {
        private Socket echoSocket;
        private PrintWriter writer;

        ClientOut(String serverHostname) throws IOException {
            this.echoSocket = new Socket(serverHostname, 18889);
            this.writer = new PrintWriter(echoSocket.getOutputStream(), true);;
        }

        @Override
        public void run() {
            System.out.println("Ο client συνδέθηκε!");
            Random rnd = new Random();
            try {
                for (int i=0; i<NUMBERS; i++) {
                    int num = rnd.nextInt(AMPLITUDE);
                    System.out.println(num);
                    writer.println(num);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            writer.close();
            }
        }


    public static class ClientIn extends Thread {
        private Socket echoSocket;
        private BufferedReader listener;

        public ClientIn(String serverHostname) throws IOException {
            this.echoSocket = new Socket(serverHostname, 18889);
            this.listener = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        }

        @Override
        public void run() {
            while (!echoSocket.isClosed()) {

            }
        }
    }

}
