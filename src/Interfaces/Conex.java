/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.Serializable;

//Clase usada para regresar los datos de conexion al jugador.
// El jugador recibe un objeto de clase conexion por el RMI
public class Conex implements Serializable {
    String idJugador;
    int puntos;
    int tcpPort;// = 7899;
    String tcpIP;// = "localhost";
    int mulPort;// = 6791;
    String mulIP;// = "228.5.6.7";

    public Conex(String idJugador, int puntos, int tcpPort, String tcpIP, int mulPort, String mulIP) {
        this.idJugador = idJugador;
        this.puntos = puntos;
        this.tcpPort = tcpPort;
        this.tcpIP = tcpIP;
        this.mulPort = mulPort;
        this.mulIP = mulIP;

    }

    public Conex(String idJugador) {
        this.idJugador = idJugador;
    }

    public Conex(String idJugador, int tcpPort, String tcpIP, int mulPort, String mulIP) {
        this.idJugador = idJugador;
        this.puntos = -1;
        this.tcpPort = tcpPort;
        this.tcpIP = tcpIP;
        this.mulPort = mulPort;
        this.mulIP = mulIP;
    }

    public String getIdJugador() {
        return idJugador;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public String getTcpIP() {
        return tcpIP;
    }

    public int getMulPort() {
        return mulPort;
    }

    public String getMulIP() {
        return mulIP;
    }

}
