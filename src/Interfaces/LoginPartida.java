/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Ésta es la interfaz remota que implementará el servidor
// para enviar los servicios necesarios al jugador.
public interface LoginPartida extends Remote {
    Conex conexion(String IDPlayer) throws RemoteException;

    void isListo(String IDPlayer) throws RemoteException;

    void logout(String IDPlayer) throws RemoteException;

    String getPuntaje() throws RemoteException;

    int getMisPuntos(String IDPlayer) throws RemoteException;
}
