/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estresador;

import Interfaces.Conex;
import Interfaces.LoginPartida;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteEstresado extends Thread {
    private final String idJuego;
    private int puntos = 0;
    private static int jugadores;
    private static FileWriter myWriter;
    private static CountDownLatch doneSignal;

    public ClienteEstresado(String idJuego) {
        this.idJuego = idJuego;
    }

    @Override
    public void run() {
        try {
            Random rand = new Random();
            int aux2;
            byte[] buffer;
            long tiempo = 0;
            int golpes = 0;

            while (true) {
            double conexion = 0;
                buffer = new byte[1000];

                new DatagramPacket(buffer, buffer.length);

                if (golpes == 5) {
                    double promedio = tiempo / 5;
                    double porExito = 5 / conexion;
                    // System.out.println(promedio);
                    myWriter.write(jugadores + ", " + promedio + ", " + porExito + ",\n"); // Escribimos el res
                    doneSignal.countDown();//Le bajamos al contador
                    break;  
                } else {
                   aux2 = rand.nextInt(3);
                   conexion++;
                    //System.out.println(aux2);
                    if (aux2 == 0) {
                        tiempo = tiempo + this.golpe();
                        golpes++;
                        // System.out.println(golpes);
                    }
                }
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ClienteEstresado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // cuando le pegas a un mounstro este metodo manda un aviso al servidor
    // usando sockets.
    public long golpe() {
        Socket s = null;
        long time;
        try {

            puntos = puntos + 1;

            int tcpPort = 7899;
            String tcpIP = "localhost";
            s = new Socket(tcpIP, tcpPort);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            time = System.currentTimeMillis();
            out.writeUTF(idJuego);

            DataInputStream in = new DataInputStream(s.getInputStream());
            in.readUTF();

            time = System.currentTimeMillis() - time;
            return time;
        } catch (IOException ex) {
            Logger.getLogger(ClienteEstresado.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClienteEstresado.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }

    public static void main(String[] args) {
        try {

        File myObj = new File("datos.csv");
        if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
        } else {
            System.out.println("File already exists.");
        }
        myWriter = new FileWriter("datos.csv");
        myWriter.write("Numero, Promedio, PorcentajeExito\n");
        for(int j=50; j<=1000; j+=50) {
            jugadores = j;

                ClienteEstresado c;
                Conex con;
                System.setProperty("java.security.policy", "file:/home/danjf/IdeaProjects/ProyectoAlpha/src/Cliente/client.policy");
                if (System.getSecurityManager() == null) {
                    System.setSecurityManager(new SecurityManager());
                }
                String name = "Login";
                Registry registry = LocateRegistry.getRegistry("localhost");  //Aqui va la IP del servidor
                LoginPartida partida = (LoginPartida) registry.lookup(name);

                //Crear un csv con los resultados
                //Un contador, para asegurarnos que no se cierre el writer
                doneSignal = new CountDownLatch(jugadores);

                for (int i = 0; i < jugadores; i++) {
                    con = partida.conexion(i + 1 + "");
                    c = new ClienteEstresado(i + 1 + "");
                    c.start();
                }

                doneSignal.await();
                System.out.println(jugadores);
            }
            myWriter.close();
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ClienteEstresado.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
