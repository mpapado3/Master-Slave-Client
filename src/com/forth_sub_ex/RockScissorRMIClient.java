package com.forth_sub_ex;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RockScissorRMIClient {
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1888);
        RockScissorOperation rockScissorOperation = (RockScissorOperation) registry.lookup("RockScissorOperation");
        ImageIcon icon = new ImageIcon("src/com/forth_sub_ex/rockIcon.jpg");

        boolean repeat = true;
        while (repeat) {
            String player = (String) JOptionPane.showInputDialog(null, "Τί θα διαλέξεις", "Πέτρα, Ψαλίδι, Χαρτί", JOptionPane.INFORMATION_MESSAGE, icon, null, "");
            while (!(player.equals("Πέτρα") || player.equals("Ψαλίδι") || player.equals("Χαρτί"))) {
                JOptionPane.showMessageDialog(null, "Μάλλον δεν έγραψες κάτι καλά, δεν καταλαβαίνω τι επέλεξες", "Λάθος", JOptionPane.INFORMATION_MESSAGE, icon);
                player = (String) JOptionPane.showInputDialog(null, "Τί θα διαλέξεις", "Πέτρα, Ψαλίδι, Χαρτί", JOptionPane.INFORMATION_MESSAGE, icon, null, "");
            }
            String response = rockScissorOperation.serverTurn(player);
            JOptionPane.showMessageDialog(null, response, "Αποτέλεσμα", JOptionPane.INFORMATION_MESSAGE, icon);
            int newGame = JOptionPane.showConfirmDialog(null, "Θα ξαναπαίξεις?", "Νέα Παρτίδα", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, icon);
            if (newGame == 1) repeat=false;
        }

    }
}
