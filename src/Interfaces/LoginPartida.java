/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface LoginPartida extends Remote{
    
    Conex Conect( String IDPlayer ) throws RemoteException;

    void listo(String IDPlayer)throws RemoteException;
    
    void logout(String IDPlayer)throws RemoteException;
    
    String puntaje()throws RemoteException;
    
    int misPuntos(String IDPlayer)throws RemoteException;
    
}
