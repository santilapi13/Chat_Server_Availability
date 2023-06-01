package client.controller;

import client.model.Usuario;
import client.view.IVista;
import client.view.VentanaChat;
import client.view.VentanaPrincipal;
import client.view.VentanaRegistro;
import server.Codigos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

public class ControladorRegistro  implements ActionListener  {

    private IVista vista;

    private static ControladorRegistro instance;

    private ControladorRegistro()  {
        this.vista = new VentanaRegistro();
        this.vista.setActionListener(this);
    }

    public static ControladorRegistro getInstance() throws UnknownHostException {
        if (instance == null) {
            instance = new ControladorRegistro();
        }
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        try {
            if (comando.equalsIgnoreCase("Registrarse")) {
                String ip = vista.getDireccionIP();
                int puertoServer = Integer.parseInt(vista.getPuertoServer());
                int puertoUsuario = Integer.parseInt(vista.getPuertoUsuario());
                String usuario = vista.getText();

                Usuario.getInstance().registrarseEnServidor(ip, puertoServer, usuario, puertoUsuario);

                String msg = Usuario.getInstance().getEntrada().readLine();

                if (msg.equals(Codigos.USERNAME_REPETIDO.name()))
                    JOptionPane.showMessageDialog(null, "Username ya registrado. Elija uno nuevo.");
                else if (msg.equals(Codigos.OK.name())) {
                    Usuario.getInstance().setPassword(Usuario.getInstance().getEntrada().readLine());
                    ((VentanaRegistro) vista).dispose();
                    ControladorPrincipal.getInstance().getVista().abrirVentana();
                }
            }
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e1) {
            System.out.println("Error al conectarse al servidor");
        }

    }
}
