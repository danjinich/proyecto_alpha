/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.Serializable;

// Esta clase existe como un "mensajero",
// la cual envía los datos de la conexión del servidor para que el jugador pueda conectarse y jugar
// Dicho esto, el RMI envía un objeto serializado de clase Conex para que pueda conectarse.
public class Conex implements Serializable {
    String jugadorId;
    int puntos;
    int puertoTCP;
    String ipTCP;
    int puertoMulticast;
    String ipMulticast;

    public Conex(String jugadorId, int puntos, int puertoTCP, String ipTCP, int puertoMulticast, String ipMulticast) {
        this.jugadorId = jugadorId;
        this.puntos = puntos;
        this.puertoTCP = puertoTCP;
        this.ipTCP = ipTCP;
        this.puertoMulticast = puertoMulticast;
        this.ipMulticast = ipMulticast;
    }

    public Conex(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    // Todo jugador nuevo en el servidor empieza con puntos = -1
    public Conex(String jugadorId, int puertoTCP, String ipTCP, int puertoMulticast, String ipMulticast) {
        this.jugadorId = jugadorId;
        this.puntos = -1;
        this.puertoTCP = puertoTCP;
        this.ipTCP = ipTCP;
        this.puertoMulticast = puertoMulticast;
        this.ipMulticast = ipMulticast;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getPuertoTCP() {
        return puertoTCP;
    }

    public String getIpTCP() {
        return ipTCP;
    }

    public int getPuertoMulticast() {
        return puertoMulticast;
    }

    public String getIpMulticast() {
        return ipMulticast;
    }

}
