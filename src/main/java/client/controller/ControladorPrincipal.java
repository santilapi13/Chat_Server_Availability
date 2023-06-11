package client.controller;

import client.model.CredencialesUsuario;
import client.view.IVista;
import client.view.VentanaChat;
import client.view.VentanaPrincipal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;

import client.model.Usuario;
import server.Codigos;

import javax.swing.*;

public class ControladorPrincipal implements ActionListener {

    private IVista vista;
    private static ControladorPrincipal instance;
    
    private ControladorPrincipal() {
        this.vista = new VentanaPrincipal();
        this.vista.setActionListener(this);
        try {
            this.actualizarListaUsuarios();
        } catch (IOException e) {
        }
        ((VentanaPrincipal) this.vista).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ((VentanaPrincipal) this.vista).addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Usuario.getInstance().getSalida().println(Codigos.DESCONECTAR);
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
            if (comando.equalsIgnoreCase("")) { // activar/desactivar modo escucha
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

                respuesta = Usuario.getInstance().chequeoReinicioPrimario(respuesta);

                // Solicitud aceptada
                if (respuesta.equals(Codigos.OK.name())) {
                    Usuario.getInstance().iniciarSesionChat();

                // Error en solicitud: no encontrado
                } else if (respuesta.equals(Codigos.NO_ENCONTRADO.name())) {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "ERROR: El usuario solicitado no se encuentra en Modo Escucha");
                }

            } else if (comando.equalsIgnoreCase("INICIAR CHAT")) {
                Usuario.getInstance().getSalida().println(Codigos.INICIAR_CHAT);
                ControladorChat.getInstance().nuevaVentana();
                this.vista.deseleccionar();

            } else if (comando.equalsIgnoreCase("ACTUALIZAR LISTA")) {
                this.actualizarListaUsuarios();
            }
        } catch (UnknownHostException ex1) {
        } catch (IOException ex2) {
        }

    }

    public void actualizarListaUsuarios() throws IOException {
        Usuario.getInstance().getSalida().println(Codigos.ACTUALIZAR_LISTA_USUARIOS);
        BufferedReader entrada = Usuario.getInstance().getEntrada();
        String msg = Usuario.getInstance().chequeoReinicioPrimario(entrada.readLine());
        int cantidad = Integer.parseInt(msg);
        this.vista.vaciarUsuariosDisponibles();
        for (int i = 0; i < cantidad; i++) {
            msg = Usuario.getInstance().chequeoReinicioPrimario(entrada.readLine());
            String usuario = msg;
            this.vista.agregarUsuarioDisponible(usuario);
        }
    }

    public void agregarUsuario( String usuario ) {
        vista.agregarUsuario( usuario );
    }

}
