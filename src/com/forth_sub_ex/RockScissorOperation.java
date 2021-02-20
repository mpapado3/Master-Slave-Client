package com.forth_sub_ex;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RockScissorOperation extends Remote {
    String serverTurn(String player) throws RemoteException;
}
