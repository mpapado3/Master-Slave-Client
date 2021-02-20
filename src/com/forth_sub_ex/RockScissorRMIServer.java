package com.forth_sub_ex;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class RockScissorRMIServer extends UnicastRemoteObject implements RockScissorOperation {
    protected RockScissorRMIServer() throws RemoteException {
        super();
    }

    @Override
    public String serverTurn(String player) throws RemoteException {
        //Δημιουργούμε String πίνακα με τις 3 επιλογές
        String[] selection = new String[]{"Πέτρα", "Ψαλίδι", "Χαρτί"};
        System.out.println("Server: " + player);
        Random rnd = new Random();
        String serverSelection = selection[rnd.nextInt(3)];
        String result = result(serverSelection, player);
        return "Ο server έβγαλε " + serverSelection + ". \n" + result;
    }

    //Μέθοδος που υπολογίζει το αποτέλεσμα
    private static String result(String server, String client) {
        //Αν οι 2 επιλογές είναι ίδιες εμφανίζουμε μήνυμα ισοπαλίας
        if (client.equals(server)) {
            return "Ήρθατε Ισοπαλία";
        } else if ((client.equals("Πέτρα") && server.equals("Ψαλίδι")) ||
                (client.equals("Ψαλίδι") && server.equals("Χαρτί")) ||
                (client.equals("Χαρτί") && server.equals("Πέτρα"))) {
            return "Κέρδισες"; //Αν ο χρήστης έχει κάνει επιλογή που κερδίζει εμφανίζουμε αντίστοιχο μήνυμα
        } else {
            //Σε κάθε άλλη περίπτωση κερδίζει ο server
            return "Έχασες, κέρδισε ο σερβερ";
        }
    }

    public static void main (String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1888);
            registry.rebind("RockScissorOperation", new RockScissorRMIServer());
            System.out.println("Ο server είναι έτοιμος...");
        } catch (Exception e) {
            System.err.println("Ο server παρουσίασε σφάλμα: " + e.toString());
            e.printStackTrace();
        }
    }
}
