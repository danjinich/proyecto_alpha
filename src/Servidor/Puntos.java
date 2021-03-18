/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
// Esta clase maneja el conteo de puntos de cada jugador del lado del servidor.
public class Puntos extends Thread {
    Administrador admin;
    Partida partida;
    Socket clientSocket;
    DataInputStream in;
    // Estresamiento
    // DataOutputStream out;
    // ------------------

    public Puntos(Administrador admin, Socket clientSocket, Partida partida) {
        try {
            this.admin = admin;
            this.partida = partida;
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            // Estresamiento
            // out = new DataOutputStream(clientSocket.getOutputStream());
            // ------------------
        } catch (IOException ex) {
            Logger.getLogger(Puntos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Esta función actualiza los puntos de cada jugador.
    // Si algún jugador llegó a la cantidad de puntosGanar (= 5), le avisa al Administrador
    // que hay un ganador y limpia el juego.
    public synchronized void actualizaPuntos(String nom) {
        int puntos;
        int jugadorIndex;
        int puntosGanar = 5;

        // Busca el índice en el que está el jugador al que se le actualizarán los puntos
        jugadorIndex = partida.getIndex(nom);
        // Recibe los puntos actualizados del jugador en el índice jugadorIndex
        puntos = partida.getPuntos(jugadorIndex);

        if (puntos == puntosGanar) {
            System.out.println(nom);
            admin.setGanador(nom);
            partida.limpiaJuego();
        }
    }

    @Override
    public void run() {
        try {
            String id;
            id = in.readUTF();
            System.out.println(id);
            actualizaPuntos(id);
            // Estresamiento
            // out.writeUTF("a");
            // -----------------------
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Puntos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
