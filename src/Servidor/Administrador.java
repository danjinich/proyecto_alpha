/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Interfaces.LoginPartida;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

// Esta clase es la del administrador del servidor.
// Todo objeto de tipo Administrador funcionará como un "coordinador"
// de las acciones ejecutadas del lado del servidor

public class Administrador {
    Multicast m;

    public Administrador() {

    }
    // Este setter conecta la instancia del Multicast
    public void setMulticast(Multicast m) {
        this.m = m;
    }

    // Este setter establece al ganador.
    // Dado que el juego debe parar cuando hay un ganador, el método es synchronized
    public synchronized void setGanador(String nom) {
        System.out.println(nom);
        m.setGanador(nom);
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            Administrador a = new Administrador();
            Multicast m = new Multicast();
            m.setAdmin(a);
            a.setMulticast(m);
            m.iniciaMulticast();
            TCP tcp = new TCP(7899);
            Partida engine = new Partida();
            tcp.setPartida(engine);
            engine.setAdmin(a);
            tcp.setAdmin(a);
            tcp.start();

            System.setProperty("java.security.policy",
                    "file:/Users/pablo/Documents/Escuela/ITAM/Catorceavo semestre/Sistemas distribuidos/proyecto_alpha/src/Cliente/client.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            LocateRegistry.createRegistry(1099);

            String name = "Inicio";
            LoginPartida stub = (LoginPartida) UnicastRemoteObject.exportObject(engine, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);

            // Comienza la ejecución del servidor
            System.out.println("El servidor está activado :)");
            while (true) {
                if (!engine.isJugadoresListos()) {
                    Thread.sleep(100);
                } else {
                    // Aquí se revisa si:
                    // 1. No ay un juego en curso. De ser así, se empieza uno (si todos los jugadores están listos)
                    // 2. Si hay un juego en curso, se "avanza" la partida (este caso es importante de considerar cuando entra un jugador nuevo).
                    // 3. Si el juego tiene que ser reiniciado
                    if (!engine.isEnCurso() && engine.isFin()) {
                        engine.inicioPartida();
                        m.start();
                        engine.setFin(false);
                    } else if (engine.isEnCurso() && !engine.isFin()) {
                        engine.siguePartida();
                    } else {
                        // Reinicia el juego si todos los jugadores están listos.
                        if (engine.isJugadoresListos()) {
                            engine.setEnCurso(false);
                            engine.inicioPartida();
                            m = new Multicast();
                            m.setAdmin(a);
                            a.setMulticast(m);
                            m.iniciaMulticast();
                            m.start();
                            engine.setFin(false);
                        } else {
                            Thread.sleep(100);
                        }
                    }
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
