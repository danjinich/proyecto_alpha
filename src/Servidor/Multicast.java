/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Multicast extends Thread {

    String ipMulticast = "228.5.6.7";
    int puertoMulticast = 6791;
    MulticastSocket s = null;
    InetAddress group;
    // Esta variable determina si hay monstruos activos en el juego o no
    boolean activoMonstruos = true;
    String ganador;
    Administrador admin;

    public void setAdmin(Administrador admin) {
        this.admin = admin;
    }

    public void setIpMulticast(String ipMulticast) {
        this.ipMulticast = ipMulticast;
    }

    public void setActivoMonstruos(boolean activoMonstruos) {
        this.activoMonstruos = activoMonstruos;
    }

    public void setPuertoMulticast(int puertoMulticast) {
        this.puertoMulticast = puertoMulticast;
    }

    public void iniciaMulticast() {
        try {
            group = InetAddress.getByName(ipMulticast);
            s = new MulticastSocket(puertoMulticast);
            s.joinGroup(group);
            s.setTimeToLive(1);
            // s.leaveGroup(group);
        } catch (IOException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setGanador(String usr) {
        System.out.println(usr);
        this.ganador = usr;
        this.activoMonstruos = false;
    }

    @Override
    public void run() {
        int aux;
        // Velocidad de aparición de los monstruos en milisegundos
        int velMonstruos = 500;
        Random rand = new Random();

        // Mientras haya monstruos activos en el juego, se envian los mensajes Multicast
        // La velocidad con la que se mandan estos mensajes es la velocidad con la que aparecen los monstruos
        while (activoMonstruos) {
            try {
                aux = rand.nextInt(11) + 1;

                String myMessage = aux + "";
                byte[] m = myMessage.getBytes();
                DatagramPacket messageOut = new DatagramPacket(m, m.length, group, puertoMulticast);
                s.send(messageOut);
                Thread.sleep(velMonstruos);
            } catch (IOException ex) {
                Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        System.out.println("¡Hay un ganador!");
        try {
            String myMessage = "100";
            byte[] m = myMessage.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, puertoMulticast);
            s.send(messageOut);
            System.out.println("Y el ganador es...");
            System.out.println("¡el jugador " + ganador + "!");
            m = ganador.getBytes();
            messageOut = new DatagramPacket(m, m.length, group, puertoMulticast);
            s.send(messageOut);
        } catch (IOException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
