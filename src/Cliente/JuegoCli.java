/*
    clase quee funciona comoo manejaddor del cliientte, corre sobre un threadd.
    
 */
package Cliente;

import Interfaces.LoginPartida;

import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JuegoCli extends Thread {
    String juegoId;
    public Juego gui;
    int puertoTCP;
    String ipTCP;
    int puertoMulticast;
    String ipMulticast;
    LoginPartida partida;
    int puntos;

    public JuegoCli(String juegoId, int puntos, int puertoTCP, String ipTCP, int puertoMulticast, String ipMulticast,
                    LoginPartida partida) {
        this.juegoId = juegoId;
        this.puertoTCP = puertoTCP;
        this.ipTCP = ipTCP;
        this.puertoMulticast = puertoMulticast;
        this.ipMulticast = ipMulticast;
        this.partida = partida;
        this.puntos = puntos;
    }

    @Override
    public void run() {
        try {
            // Conectar a multicast
            MulticastSocket s = null;
            InetAddress group = InetAddress.getByName(ipMulticast);
            s = new MulticastSocket(puertoMulticast);
            s.joinGroup(group);
            // variables de run
            byte[] buffer;
            DatagramPacket monstruo;
            int id;
            String aux;
            String puntajes;
            // Conexión Multicast
            while (true) {
                buffer = new byte[1000];
                monstruo = new DatagramPacket(buffer, buffer.length);
                // Puntaje de todos los jugadores en esta iteración
                puntajes = partida.getPuntaje();
                // Imprime el puntaje en la interfaz gráfica
                gui.setPuntos(puntajes);
                // Imprime el aviso en la interfaz gráfica
                gui.setAviso("¡Bienvenide al juego! ¡Mucha suerte!");
                // El socket recibe un id del botón para enviar en qué posición se encuentra el monstruo
                // Si el socket recibe la bandera de ID = 100, el juego acabó.
                s.receive(monstruo);
                id = parseInt(new String(monstruo.getData(), 0, monstruo.getLength()));
                // Si alguien gana, el ID del botón es igual a 100
                if (id == 100) {
                    // El multicast recibe el ID del jugador que ganó
                    s.receive(monstruo);
                    System.out.println(monstruo.getData().toString());
                    aux = (new String(monstruo.getData(), 0, monstruo.getLength()));
                    // Imprime al ganador en la interfaz gráfica
                    gui.setGanador(aux);
                    // Se vacía todo con el agujero negro :)
                    gui.setNuevoJuego();
                    // Se habilita el botón de inicio para que les jugadores puedan volver a jugar
                    gui.habilitaInicio();
                    // Se reinician los puntajes
                    puntajes = partida.getPuntaje();
                    gui.setPuntos(puntajes);
                    break;
                } else {
                    // Si nadie ha ganado, se cambia la posición del monstruo dentro de los botones
                    gui.setIconos();
                    gui.setMonstruo(id);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(JuegoCli.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // El método golpe avisa al servidor cuando un jugador dio un golpe exitoso a un monstruo
    public void golpe() {
        Socket s = null;
        try {
            // Se envía la info de los puntajes al servidor.
            puntos = puntos + 1;
            s = new Socket(ipTCP, puertoTCP);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeUTF(juegoId);
        } catch (IOException ex) {
            Logger.getLogger(JuegoCli.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (IOException ex) {
                    Logger.getLogger(JuegoCli.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }

    public void setGui(Juego gui) {
        this.gui = gui;
    }

}
