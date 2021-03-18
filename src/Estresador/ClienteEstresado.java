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


public class ClienteEstresado extends Thread{
    private final String idJuego;//Es el nombre del jugador
    private static int jugadores;//Numero de jugadores concurrentes
    private static FileWriter myWriter;//Buffer para escribir en el CSV
    private static CountDownLatch doneSignal;//Revisa que aceben todos los threads

    public ClienteEstresado(String idJuego) {
        this.idJuego = idJuego;
    }

    @Override
    public void run(){
        try {
            Random rand = new Random();
            int aux2;
            byte[] buffer;
            double tiempo = 0;
            int golpes = 0;//Numero de golpes exitosos
            double conexion=0;//Numero de intento de golpes
            while(golpes<5){//sigue hasta ganar
                buffer = new byte[1000];

                new DatagramPacket(buffer, buffer.length);
                aux2 = rand.nextInt(3);//Trata de golpear
                conexion++;
                //System.out.println(aux2);
                if (aux2 == 0) {//vemos si golpeo bien
                    tiempo = tiempo + this.golpe();//le agrgamos el tiempo que le tomo
                    golpes++;//Registramos el golpe

                    //System.out.println(golpes);
                }

                Thread.sleep(10);//esperamos para el siguiente intento
            }
            double promedio = tiempo / 5;
            double porExito=5/conexion;
            //System.out.println(promedio);
            myWriter.write(jugadores+","+promedio +","+porExito+",\n");//Escribimos el resultado
            doneSignal.countDown();//Le bajamos al contador
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ClienteEstresado.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
      

    //cuando le pegas a un mounstro este metodo manda un aviso al servidor
    //usando sockets.
    public long golpe(){
        Socket s = null;
        long time;
        try {

            int tcpPort = 7899;
            String tcpIP = "localhost";
            s = new Socket(tcpIP, tcpPort);//Creamos el socket
            DataOutputStream out =
                    new DataOutputStream( s.getOutputStream());
            time = System.currentTimeMillis();//Tiempo de inicio
            out.writeUTF(idJuego);//mandamos un mensaje
           
            DataInputStream in = new DataInputStream(s.getInputStream());
            in.readUTF();//recibimos un mensaje
           
            time = System.currentTimeMillis() - time;
            return time;
        }catch (IOException ex){
            Logger.getLogger(ClienteEstresado.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } 
        finally {
            if(s != null) try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(ClienteEstresado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public static void main(String[] args) {
        try {
            System.setProperty("java.security.policy", "file:/home/danjf/IdeaProjects/ProyectoAlpha/src/Cliente/client.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            File myObj = new File("datos.csv");//creamos el csv si no existe
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            myWriter = new FileWriter("datos.csv");//empezamos el buffer
            myWriter.write("Numero, Promedio, PorcentajeExito\n");//nombres de columnas
            for(int j=50; j<=1000; j+=50) {//loop que determina el numero de jugadores concurrentes
                jugadores = j;
                for(int k=1; k<=10; k++) {//Para cada numero de jugadores se hacen 10 pruebas
                    ClienteEstresado c;
                    Conex con;


                    String name = "Login";
                    Registry registry = LocateRegistry.getRegistry("localhost");  //Aqui va la IP del servidor
                    LoginPartida Log = (LoginPartida) registry.lookup(name);

                    //Crear un csv con los resultados


                    //Un contador, para asegurarnos que no se cierre el writer
                    doneSignal = new CountDownLatch(jugadores);

                    for (int i = 0; i < jugadores; i++) {//Se van a crear e iniciar todos los jugadores
                        con = Log.Conect(i + 1 + "");
                        c = new ClienteEstresado(i + 1 + "");
                        c.start();
                    }

                    doneSignal.await();//Nos esperamos a que acaben
                    System.out.println(jugadores);
                }
            }
            myWriter.close();
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ClienteEstresado.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}

