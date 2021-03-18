/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Interfaces.Conex;
import Interfaces.LoginPartida;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Esta el objeto de clase administra y organiza todos los datos de la partida que se está corriendo
public class Partida implements LoginPartida {
    private boolean fin;
    private boolean enCurso;
    public ArrayList<Jugador> jugadores;
    private String ultima;
    // La el objeto de clase Partida comunica todo lo que está sucediendo a la clase Administrador
    Administrador admin;

    public Partida() {
        fin = true;
        enCurso = false;
        this.jugadores = new ArrayList<>();
    }

    // Este método revisa si todos los jugadores están listos para jugar
    public boolean isJugadoresListos() {
        int num = jugadores.size();
        boolean res = false;
        System.out.println("Esperando jugadores...");
        if (num > 0) {
            System.out.println("Jugadores listos: " + num);
            res = true;
            for (int i = 0; i < num; i++) {
                if (jugadores.get(i).isEnJuego()) {
                    res = jugadores.get(i).isListo() && res;
                }
            }
            // Saca del juego a los jugadores que cerraron sesión
            if (res) {
                for (int i = 0; i < num; i++) {
                    if (!jugadores.get(i).isEnJuego()) {
                        jugadores.remove(i);
                        i = i - 1;
                        num = num - 1;
                    }
                }
            }
        }

        return res;
        // Estresamiento
        // return true;
        // -------------
    }

    // Este método permite verificar si el jugador por entrar a la partida es nuevo o no.
    // En caso de ya existir, le regresa a su conexión anterior.
    @Override
    public Conex conexion(String IDPlayer) throws RemoteException {
        Conex con;
        int num = jugadores.size();
        boolean existe = false;
        int i = 0;
        Jugador aux = new Jugador(IDPlayer);
        while (i < num && !existe) {
            existe = jugadores.get(i).equals(aux);
            i++;
        }
        if (existe && !jugadores.get(i - 1).isEnJuego()) {
            jugadores.get(i - 1).setEnJuego(true);
            jugadores.get(i - 1).setListo(false);
            jugadores.get(i - 1).setAcabo(false);
            con = new Conex(IDPlayer, jugadores.get(i - 1).getPuntos(), 7899, "localhost", 6791, "228.5.6.7");
        } else if (existe) {
            con = new Conex(null);
        } else {
            System.out.println("Agregando nuevo jugador...");
            jugadores.add(new Jugador(IDPlayer, 0));
            con = new Conex(IDPlayer, 7899, "localhost", 6791, "228.5.6.7");
        }
        return con;
    }

    // Limpia el juego
    public void limpiaJuego() {
        try {
            this.getPuntaje();
            fin = true;
            for (Jugador jugadore : jugadores) {
                jugadore.resetPuntos();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Si el jugadore está listo para jugar, manda este método
    @Override
    public void isListo(String IDPlayer) throws RemoteException {
        int index;
        Jugador aux = new Jugador(IDPlayer);
        index = jugadores.indexOf(aux);
        jugadores.get(index).setListo(true);
    }

    // Si el jugador salió del juego y cerró sesión, manda este método
    @Override
    public void logout(String IDPlayer) throws RemoteException {
        int index;
        Jugador aux = new Jugador(IDPlayer);
        index = jugadores.indexOf(aux);
        jugadores.get(index).setEnJuego(false);
    }

    // Getter de los puntajes del juego
    @Override
    public String getPuntaje() throws RemoteException {
        if (!fin) {
            ultima = jugadores.toString();
        }
        return ultima;

    }

    // Este método inicia una nueva partida
    public void inicioPartida() {
        limpiaJuego();
        enCurso = true;
        int num = jugadores.size();
        for (int i = 0; i < num; i++) {
            jugadores.get(i).setAcabo(false);
            jugadores.get(i).setListo(false);
            if (!jugadores.get(i).isEnJuego()) {
                jugadores.remove(i);
                i = i - 1;
                num = num - 1;
            }
        }
    }

    // Este método es un getter de los puntos de un jugador dado su ID
    @Override
    public int getMisPuntos(String IDPlayer) throws RemoteException {
        int index;
        Jugador aux = new Jugador(IDPlayer);
        index = jugadores.indexOf(aux);
        return jugadores.get(index).getPuntos();
    }

    // Este método agrega nuevos jugadores a la partida.
    void siguePartida() {
        enCurso = true;
        for (Jugador jugadore : jugadores) {
            jugadore.setAcabo(false);
            jugadore.setListo(false);
        }
    }

    // Getters y setters
    public void setAdmin(Administrador admin) {
        this.admin = admin;
    }

    public int getIndex(String nom) {
        return jugadores.indexOf(new Jugador(nom));
    }

    public int getPuntos(int index) {
        return jugadores.get(index).incPuntos();
    }

    public boolean isFin() {
        return fin;
    }

    public boolean isEnCurso() {
        return enCurso;
    }

    public String getUltima() {
        return ultima;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public void setEnCurso(boolean enCurso) {
        this.enCurso = enCurso;
    }

    public void setUltima(String ultima) {
        this.ultima = ultima;
    }
}
