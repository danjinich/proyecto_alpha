/*
    Interfaz de juego del cliente, usa botonees para simular que llega
    un monstruo.
    
    Para juego se invoca desde la interfaz de login. 

 */
package Cliente;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;

import Interfaces.LoginPartida;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;


public final class Juego extends javax.swing.JFrame {
    private final Color colorMonstruo;
    private final Color colorNormal;
    private final Icon iconoMonstruo = new ImageIcon(getClass().getResource("/Cliente/Whack-a-Mole.png"));
    private final Icon iconoHoyo = new ImageIcon(getClass().getResource("/Cliente/hole.png"));
    private final Icon iconoAgujeroNegro = new ImageIcon(getClass().getResource("/Cliente/black-hole.png"));
    JuegoCli jugador;
    JButton[] botones = new JButton[16];
    LoginPartida partida;
    String jugadorId;
    int puertoTCP;
    String ipTCP;
    int puertoMulticast;
    String ipMulticast;
    boolean primerJuego = true;

    // Constructor de la interfaz gráfica del juego
    public Juego(String jugadorId, int puntos, int puertoTCP, String ipTCP, int puertoMulticast, String ipMulticast, LoginPartida partida) {
        initComponents();
        colorMonstruo = Color.BLUE;
        colorNormal = Color.LIGHT_GRAY;
        this.partida = partida;
        this.jugadorId = jugadorId;
        this.puertoTCP = puertoTCP;
        this.ipTCP = ipTCP;
        this.puertoMulticast = puertoMulticast;
        this.ipMulticast = ipMulticast;
        setArrBotones(); // los botones se guardan en un arreglo
        setIconos(); // pone todas las casillas en gris
        setJugador(jugadorId, puntos, puertoTCP, ipTCP, puertoMulticast, ipMulticast);
    }

    // Inicia al jugador y la conexión
    public final void setJugador(String jugadorId, int puntos, int puertoTCP, String ipTCP, int puertoMulticast, String ipMulticast) {
        // JuegoCli es un hilo que maneja las interacciones del jugador y las envía al servidor
        jugador = new JuegoCli(jugadorId, puntos, puertoTCP, ipTCP, puertoMulticast, ipMulticast, partida);
        if (puntos >= 0) {
            primerJuego = false;
        }
        jugador.setGui(this);
        jugador.start();
        jTextField1.setText(jugadorId);
    }

    // Este método mete a un arreglo de botones cada JButton de forma que sea más fácil
    // acceder a éstos de forma dinámica
    public void setArrBotones() {
        botones[0] = b11;
        botones[1] = b12;
        botones[2] = b13;
        botones[3] = b14;
        botones[4] = b21;
        botones[5] = b22;
        botones[6] = b23;
        botones[7] = b24;
        botones[8] = b31;
        botones[9] = b32;
        botones[10] = b33;
        botones[11] = b34;
    }

    // Con este método se pretende "prender" o "apagar" todos los botones de la interfaz gráfica.
    // Su razón de ser es reestablecer a su estado original la interfaz gráfica tras un nuevo juego.
    public void setEstadoBotones(boolean estado) {
        botones[0].setEnabled(estado);
        botones[1].setEnabled(estado);
        botones[2].setEnabled(estado);
        botones[3].setEnabled(estado);
        botones[4].setEnabled(estado);
        botones[5].setEnabled(estado);
        botones[6].setEnabled(estado);
        botones[7].setEnabled(estado);
        botones[8].setEnabled(estado);
        botones[9].setEnabled(estado);
        botones[10].setEnabled(estado);
        botones[11].setEnabled(estado);
    }

    // Rellena los íconos de los botones
    public void setIconos() {
        this.setEstadoBotones(true);
        botones[0].setIcon(iconoHoyo);
        botones[1].setIcon(iconoHoyo);
        botones[2].setIcon(iconoHoyo);
        botones[3].setIcon(iconoHoyo);
        botones[4].setIcon(iconoHoyo);
        botones[5].setIcon(iconoHoyo);
        botones[6].setIcon(iconoHoyo);
        botones[7].setIcon(iconoHoyo);
        botones[8].setIcon(iconoHoyo);
        botones[9].setIcon(iconoHoyo);
        botones[10].setIcon(iconoHoyo);
        botones[11].setIcon(iconoHoyo);
    }

    // Este metodo escribe un mensaje con el ganador del juego en el TextField
    public void setGanador(String msj) {
        jTextField1.setText("El ganador es el jugador:\n " + msj);
        this.setEstadoBotones(false);
    }

    // Este método revisa a qué elemento de la UI le dio clic el usuario.
    // Si golpeó al monstruo, se cambia el ícono al hoyo y se envía un mensaje
    // al servidor para avisar que éste le golpeó. En caso de un fallo, se le notifica al usuario
    public void clic(int id) {
        if (botones[id - 1].getIcon().equals(iconoMonstruo)) {
            botones[id - 1].setIcon(iconoHoyo);
            jTextField1.setText("¡Le diste al monstruo!");
            jugador.golpe();
        } else {
            jTextField1.setText("Fallaste el golpe :(");
        }
    }

    // Este método cambia los avisos del JLabel
    public void setAviso(String text) {
        avi.setText(text);
    }

    // Este método cambia el puntaje que se despliega en el TextField
    public void setPuntos(String text) {
        txtPuntos.setText(text);
    }

    // Este método pone un botón, dado su id, con el fondo del ícono del monstruo
    public void setMonstruo(int id) {
        botones[id].setIcon(iconoMonstruo);
    }

    // Este método habilita el botón de inicio [cuando no hay un juego corriendo]
    public void habilitaInicio() {
        btnInicio.setEnabled(true);
    }

    // Para identificar si un juego acabó, se cambia el ícono del primer botón a un hoyo negro.
    // Este agujero negro "chupa" todo lo que estaba en la interfaz :)
    public void setNuevoJuego() {
        botones[0].setIcon(iconoAgujeroNegro);
    }

    // CÓDIGO AUTOGENERADO PARA LA INTERFAZ GRÁFICA
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - unknown
    private void initComponents() {
        b11 = new JButton();
        b12 = new JButton();
        b14 = new JButton();
        b13 = new JButton();
        b21 = new JButton();
        b22 = new JButton();
        b24 = new JButton();
        b23 = new JButton();
        b34 = new JButton();
        b31 = new JButton();
        b32 = new JButton();
        b33 = new JButton();
        jTextField1 = new JTextField();
        jLabel1 = new JLabel();
        txtPuntos = new JTextField();
        btnInicio = new JButton();
        btnSal = new JButton();
        avi = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(590, 270));
        var contentPane = getContentPane();

        //---- b11 ----
        b11.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b11.setMinimumSize(new Dimension(64, 64));
        b11.setMaximumSize(new Dimension(64, 64));
        b11.addActionListener(e -> b11ActionPerformed(e));

        //---- b12 ----
        b12.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b12.setMinimumSize(new Dimension(64, 64));
        b12.setMaximumSize(new Dimension(64, 64));
        b12.addActionListener(e -> b12ActionPerformed(e));

        //---- b14 ----
        b14.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b14.setMinimumSize(new Dimension(64, 64));
        b14.setMaximumSize(new Dimension(64, 64));
        b14.addActionListener(e -> b14ActionPerformed(e));

        //---- b13 ----
        b13.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b13.setMinimumSize(new Dimension(64, 64));
        b13.setMaximumSize(new Dimension(64, 64));
        b13.addActionListener(e -> b13ActionPerformed(e));

        //---- b21 ----
        b21.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b21.setMinimumSize(new Dimension(64, 64));
        b21.setMaximumSize(new Dimension(64, 64));
        b21.addActionListener(e -> b21ActionPerformed(e));

        //---- b22 ----
        b22.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b22.setMinimumSize(new Dimension(64, 64));
        b22.setMaximumSize(new Dimension(64, 64));
        b22.addActionListener(e -> b22ActionPerformed(e));

        //---- b24 ----
        b24.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b24.setMinimumSize(new Dimension(64, 64));
        b24.setMaximumSize(new Dimension(64, 64));
        b24.addActionListener(e -> b24ActionPerformed(e));

        //---- b23 ----
        b23.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b23.setMinimumSize(new Dimension(64, 64));
        b23.setMaximumSize(new Dimension(64, 64));
        b23.addActionListener(e -> b23ActionPerformed(e));

        //---- b34 ----
        b34.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b34.setMinimumSize(new Dimension(64, 64));
        b34.setMaximumSize(new Dimension(64, 64));
        b34.addActionListener(e -> b34ActionPerformed(e));

        //---- b31 ----
        b31.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b31.setMinimumSize(new Dimension(64, 64));
        b31.setMaximumSize(new Dimension(64, 64));
        b31.addActionListener(e -> b31ActionPerformed(e));

        //---- b32 ----
        b32.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b32.setMinimumSize(new Dimension(64, 64));
        b32.setMaximumSize(new Dimension(64, 64));
        b32.addActionListener(e -> b32ActionPerformed(e));

        //---- b33 ----
        b33.setIcon(new ImageIcon(getClass().getResource("/Cliente/hole.png")));
        b33.setMinimumSize(new Dimension(64, 64));
        b33.setMaximumSize(new Dimension(64, 64));
        b33.addActionListener(e -> b33ActionPerformed(e));

        //---- jTextField1 ----
        jTextField1.setFont(new Font("Tahoma", Font.PLAIN, 18));

        //---- jLabel1 ----
        jLabel1.setText("Puntuaciones: ");

        //---- txtPuntos ----
        txtPuntos.setFont(new Font("Tahoma", Font.PLAIN, 18));

        //---- btnInicio ----
        btnInicio.setText("Iniciar juego");
        btnInicio.addActionListener(e -> btnInicioActionPerformed(e));

        //---- btnSal ----
        btnSal.setText("Cerrar sesi\u00f3n");
        btnSal.addActionListener(e -> btnSalActionPerformed(e));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                        .addComponent(b31, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(b21, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(b11, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(b32, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(b22, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(b12, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                        .addComponent(b23, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(b13, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(b33, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(b34, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(b24, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(b14, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(9, 9, 9)
                            .addComponent(avi)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(jLabel1)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                    .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                            .addComponent(btnInicio)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnSal))
                                        .addComponent(jTextField1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtPuntos, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)))))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                .addComponent(b12, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(b13, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(b11, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(b14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(b21, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(b22, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(b23, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(b24, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(b31, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(b32, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(b33, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(b34, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnSal)
                                .addComponent(btnInicio))
                            .addGap(18, 18, 18)
                            .addComponent(jLabel1)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtPuntos, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
                    .addGap(180, 180, 180)
                    .addComponent(avi))
        );
        pack();
        setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents

    private void b11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b11ActionPerformed
        // TODO add your handling code here:
        clic(1);

    }//GEN-LAST:event_b11ActionPerformed

    private void b12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b12ActionPerformed
        // TODO add your handling code here:
        clic(2);
    }//GEN-LAST:event_b12ActionPerformed

    private void b13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b13ActionPerformed
        // TODO add your handling code here:
        clic(3);
    }//GEN-LAST:event_b13ActionPerformed

    private void b14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b14ActionPerformed
        // TODO add your handling code here:
        clic(4);
    }//GEN-LAST:event_b14ActionPerformed

    private void b21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b21ActionPerformed
        // TODO add your handling code here:
        clic(5);
    }//GEN-LAST:event_b21ActionPerformed

    private void b22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b22ActionPerformed
        // TODO add your handling code here:
        clic(6);
    }//GEN-LAST:event_b22ActionPerformed

    private void b23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b23ActionPerformed
        // TODO add your handling code here:
        clic(7);
    }//GEN-LAST:event_b23ActionPerformed

    private void b24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b24ActionPerformed
        // TODO add your handling code here:
        clic(8);
    }//GEN-LAST:event_b24ActionPerformed

    private void b31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b31ActionPerformed
        // TODO add your handling code here:
        clic(9);
    }//GEN-LAST:event_b31ActionPerformed

    private void b32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b32ActionPerformed
        // TODO add your handling code here:
        clic(10);
    }//GEN-LAST:event_b32ActionPerformed

    private void b33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b33ActionPerformed
        // TODO add your handling code here:
        clic(11);
    }//GEN-LAST:event_b33ActionPerformed

    private void b34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b34ActionPerformed
        // TODO add your handling code here:
        clic(12);
    }//GEN-LAST:event_b34ActionPerformed

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed


        //La idea de este botón es dejar que cada uno de los clientes
        //ponga que está listo y se inicie el juego si todoss los 
        //cclientes quee esstan conectados piicaron el boton.

        //Avecees cuando un solo cliente le pica se iniicia eel juego.
        //avi es un textfiiield.

        try {
            // TODO add your handling code here:
            if (primerJuego) { // si hace login
                primerJuego = false;
                partida.isListo(jugadorId);
                btnInicio.setEnabled(false);
                avi.setText("Esperando a otros jugadores...");
            } else { // si no es su primer juego (se salió y volvió a entrar o inicia una nueva partida)
                primerJuego = false;
                setJugador(jugadorId, 0, puertoTCP, ipTCP, puertoMulticast, ipMulticast);
                partida.isListo(jugadorId);
                btnInicio.setEnabled(false);
                avi.setText("Esperando a otros jugadores...");
                setIconos();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed

        //Este boton es para "salir" del juego, si le picas tache
        //en vez de este boton no te marca el logout y no puedes 
        //volver a entrar con el mismo nombre.

        try {
            // TODO add your handling code here:
            partida.logout(jugadorId);
            this.dispose();
        } catch (RemoteException ex) {
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnSalActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new Juego().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JButton b11;
    private JButton b12;
    private JButton b14;
    private JButton b13;
    private JButton b21;
    private JButton b22;
    private JButton b24;
    private JButton b23;
    private JButton b34;
    private JButton b31;
    private JButton b32;
    private JButton b33;
    private JTextField jTextField1;
    private JLabel jLabel1;
    private JTextField txtPuntos;
    private JButton btnInicio;
    private JButton btnSal;
    private JLabel avi;
    // End of variables declaration//GEN-END:variables
}

