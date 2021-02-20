package com.first_sub_ex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class RockScissorsServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null; //Αρχικοποιούμε ένα server socket που θα ορίσουμε μετά την πόρτα

        try {
            serverSocket = new ServerSocket(10007); //Ορίζουμε οτι η πόρτα που θα ακούει είναι η 10007
        } catch (IOException e) {
            //Αν προκύψει λάθος σύνδεσης μας εμφανίζει μήνυμα
            System.err.println("Δεν μπορεώ να συνδεθώ στην πόρτα: 10007.");
            System.exit(1);
        }

        Socket clientSocket = null; //Αχρικοποιούμε άλλο ένα socket που θα συνδεθεί ο client
        System.out.println("Αναμονή για σύνδεση....."); //Τυπώνουμε μήνυμα στην κονσόλα

        try {
            //Όταν ο client συνδεθεί στον σερβερ η accept μέθοδος ο σερβερ επιστρέφει την πόρτα που μπορεί
            //να επικοινωνήσει ο client
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            //Σε περίπτωση σφάλματος σύνδεσης θα εμφανίσει μήνυμα στον χρήστη
            System.err.println("Δεν έγινε σύνδεση.");
            System.exit(1);
        }
        //Τυπώνουμε μηνύματα ότι έγινε η σύνδεση και αναμένουμε την σύνδεση του client
        System.out.println("Επιτυχής Σύνδεση");
        System.out.println("Στέλνουμε μήνυμα καλοσορίσματος στον Παίκτη.....");
        //Ανοίγουμε input και output streams για να διαβάζουμε και να στέλνουμε μηνύματα στον client
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //Στένουμε μηνύματα καλοσορίσματος στον client και τον ενημερώνουμε ότι παίζει πρώτος
        out.println("Θα ξεκινήσουμε ένα παιχνίδι Πέτρα, Ψαλίδι Χαρτί!");
        out.flush();
        out.println("Ξεκινάς πρώτος. Ποιά είναι η επιλογή σου?");
        out.flush();
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
            out.flush();
            //Αν ο χρήστης έχει δώσει "ο" βγαίνουμε από το while
            if (inputLine.equals("ο"))
                break;
        }
        //Κλείνουμε όλες τις συνδέσεις και τα buffer
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();

    }
}
