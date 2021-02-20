package com.first_sub_ex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class RockScissorsClient {

    public static void main(String[] args) throws IOException {
        String serverHostname = "127.0.0.1"; //Ορίζουμε την διεύθυνση που είναι ο σέρβερ

        System.out.println("Αναμονή για σύνδεση στον σέρβερ " + serverHostname + " στην πόρτα 10007.");
        //αρχικοποιούμε το socket, το buffer για τα εισερχόμενα μηνύματα και τα εξερχόμενα
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            echoSocket = new Socket(serverHostname, 10007); //Ορίζουμε την port που θα χρησιμοποιήσουμε
            out = new PrintWriter(echoSocket.getOutputStream(), true); //Ορίζουμε οτι η μεταβλητή θα στέλνει τα μηνύματα που θα γράφουμε
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream())); //Ορίζουμε ότι η μεταβλητή ακούει τα μηνύματα που έρχονται από την σύνδεση
        } catch (UnknownHostException e) {
            System.err.println("Δεν μπορώ να συνδεθώ στον σέρβερ: " + serverHostname); //Εμφανίζουμε μήνυμα αν υπάρξει κάποιο τυχαίο σφάλμα
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + serverHostname); //Εμφανίζουμε αν υπάρξει κάποιο Ι/Ο σφάλμα
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); //μεταβλητή που θα δέχεται την είσοδο από το πληκτρολόγιο του χρήστη
        String userInput;

        System.out.println("echo: " + in.readLine()); //Μήνυμα από τον server
        System.out.println("echo: " + in.readLine()); //Μήνυμα από τον server

        System.out.println("Θα παίξω :");
        while ((userInput = stdIn.readLine()) != null) {
            //Αν ξαναπαίζουμε και επιλέξει "ν" δεν το στέλνουμε στον σερβερ, εμφανίζουμε το μήνυμα για είσοδο επιλογής χρήστη
            if (userInput.equals("ν")) {
                System.out.println("Θα παίξω :");
                userInput = stdIn.readLine();
            }
            //Αν ο χρήστης δώσει κάποια άλλη λέξη ή δεν την έχει γράψει σωστά, τον ενημερώνουμε ότι πρέπει να το ξαναγράψει
            //Για να αποφύγουμε να μην παίρνει και το "ο" που είναι για τον τερματισμό το βάζουμε στις εξαιρέσεις.
            while (!(userInput.equals("Πέτρα") || userInput.equals("Ψαλίδι") || userInput.equals("Χαρτί") || userInput.equals("ο"))) {
                System.out.println("Μάλλον δεν έγραψες κάτι καλά, δεν καταλαβαίνω τι επέλεξες");
                System.out.println("Θα Παίξω :");
                userInput = stdIn.readLine();
            }
            out.println(userInput); //Στέλνουμε στον σέρβερ την επιλογή μας
            if (userInput.equals("ο"))
                break; //Αν έχουμε επιλέξει "ο" τερματίζουμε το παιχνίδι
            String reply = in.readLine();
            System.out.println("echo: " + reply); //Εμφανίζουμε τι έχει παίξει ο σέρβερ
            System.out.println(result(reply, userInput)); //Βρίσκουμε το αποτέλεσμα και εμφανίζουμε μήνυμα
            System.out.print("Θέλεις να ξαναπαίξεις (ν/ο): "); //Ρωτάμε αν θέλουμε να ξαναπαίξουμε
        }

        //Κλείνουμε όλες τις συνδέσεις και τα buffer
        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }

    //Μέθοδος που υπολογίζει το αποτέλεσμα
    private static String result(String server, String client) {
        //Αν οι 2 επιλογές είναι ίδιες εμφανίζουμε μήνυμα ισοπαλίας
        if (client.equals(server)) {
            return "Ισοπαλία";
        } else if ((client.equals("Πέτρα") && server.equals("Ψαλίδι")) ||
                (client.equals("Ψαλίδι") && server.equals("Χαρτί")) ||
                (client.equals("Χαρτί") && server.equals("Πέτρα"))) {
            return "Κέρδισες"; //Αν ο χρήστης έχει κάνει επιλογή που κερδίζει εμφανίζουμε αντίστοιχο μήνυμα
        } else {
            //Σε κάθε άλλη περίπτωση κερδίζει ο server
            return "Έχασες, κέρδισε ο σερβερ";
        }
    }
}
