package com.third_sub_ex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Slave {
    public static void main(String[] args) throws IOException {
        String serverHostname = "127.0.0.1"; //Ορίζουμε την διεύθυνση που είναι ο σέρβερ

        System.out.println("Αναμονή για σύνδεση στον σέρβερ " + serverHostname + " στην πόρτα 30091.");
        //αρχικοποιούμε το socket, το buffer για τα εισερχόμενα μηνύματα και τα εξερχόμενα
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            echoSocket = new Socket(serverHostname, 30091); //Ορίζουμε την port που θα χρησιμοποιήσουμε
            out = new PrintWriter(echoSocket.getOutputStream(), true); //Ορίζουμε την μεταβλητή που θα στέλνει τα μηνύματα που θα γράφουμε
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream())); //Ορίζουμε την μεταβλητή που ακούει τα μηνύματα που έρχονται από την σύνδεση
        } catch (UnknownHostException e) {
            System.err.println("Δεν μπορεί να πραγματοποιηθεί σύνδεση με τον σέρβερ: " + serverHostname); //Εμφανίζουμε μήνυμα αν υπάρξει κάποιο τυχαίο σφάλμα
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + serverHostname); //Εμφανίζουμε αν υπάρξει κάποιο Ι/Ο σφάλμα
            System.exit(1);
        }
        ArrayList<Integer> content = new ArrayList<>();
        int masterEntry;

        while ((masterEntry = in.read()) != -1) {
            //Αν ξαναπαίζουμε και επιλέξει "ν" δεν το στέλνουμε στον σερβερ, εμφανίζουμε το μήνυμα για είσοδο επιλογής χρήστη
            if (content.contains(masterEntry)) {
                System.out.println(masterEntry+" υπάρχει ήδη!");
                out.println(masterEntry+" υπάρχει ήδη!");
            } else {
                content.add(masterEntry);
            }
        }

        //Κλείνουμε όλες τις συνδέσεις και τα buffer
        out.close();
        in.close();
        echoSocket.close();
    }
}
