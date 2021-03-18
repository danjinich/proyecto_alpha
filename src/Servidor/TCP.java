/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

// Esta clase crea un hilo por cada conexión del usuario para signarle un socket de TCP

public class TCP extends Thread {

    Administrador admin;
    Partida partida;
    ServerSocket listenSocket;

    public TCP(int serverPort) {
        try {
            // int serverPort = 7899;
            listenSocket = new ServerSocket(serverPort);
        } catch (IOException ex) {
            Logger.getLogger(TCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAdmin(Administrador admin) {
        this.admin = admin;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket clientSocket = listenSocket.accept();
                Puntos p = new Puntos(admin, clientSocket, partida);
                p.start();
            }
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            try {
                listenSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
