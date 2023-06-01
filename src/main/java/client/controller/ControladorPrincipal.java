package client.controller;

import client.model.CredencialesUsuario;
import client.view.IVista;
import client.view.VentanaChat;
import client.view.VentanaPrincipal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import client.model.Usuario;

import javax.swing.*;

public class ControladorPrincipal implements ActionListener {

    private IVista vista;
    private static ControladorPrincipal instance;
    
    private ControladorPrincipal() {
        this.vista = new VentanaPrincipal();
        this.vista.setActionListener(this);
        ((VentanaPrincipal) this.vista).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ((VentanaPrincipal) this.vista).addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Usuario.getInstance().getSalida().println("503");
                    System.out.println("Cerrando ventana principal");
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    System.exit(0);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    
    public static ControladorPrincipal getInstance() throws UnknownHostException {
        if (instance == null) {
            instance = new ControladorPrincipal();
        }
        return instance;
    }

    public IVista getVista() {
        return vista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        try {
            if (comando.equalsIgnoreCase("")) {
                if (!Usuario.getInstance().isEscuchando()) {
                    Thread hilo = new Thread(Usuario.getInstance());
                    hilo.start();
                } else {
                    Usuario.getInstance().desactivarModoEscucha();
                }
            }
            if (comando.equalsIgnoreCase("SOLICITAR CHAT")) {

                String ip = vista.getDireccionIP();
                String puerto = vista.getPuertoIP();
                CredencialesUsuario credencialesUsuarioReceptor = new CredencialesUsuario(ip, Integer.parseInt(puerto), "");
                Usuario.getInstance().solicitarChat(credencialesUsuarioReceptor);

                String respuesta = Usuario.getInstance().getEntrada().readLine();

                // Solicitud aceptada
                if (respuesta.equalsIgnoreCase("200")) {
                    Usuario.getInstance().iniciarSesionChat();

                // Error en solicitud: no encontrado
                } else if (respuesta.equalsIgnoreCase("404")) {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "ERROR: Compruebe IP y puertos. Recuerde el otro usuario debe estar en modo escucha.");
                }

            }
            if (comando.equalsIgnoreCase("INICIAR CHAT")) {
                Usuario.getInstance().getSalida().println("350");
                ControladorChat.getInstance().nuevaVentana();
                this.vista.deseleccionar();
            }
        } catch (UnknownHostException ex1) {
        } catch (IOException ex2) {
        }

    }

    public void agregarUsuario( String usuario ) {
        vista.agregarUsuario( usuario );
    }

}
