package client.controller;

import client.model.Usuario;
import client.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

public class ControladorChat implements ActionListener, Runnable  {

    private IVista vista;
    private static ControladorChat instance;

    private Thread recibirMensajesThread;
    private boolean conexionEstablecida = true;

    private boolean cerroVentana = false;


    private ControladorChat() {
    }

    public void nuevaVentana() throws UnknownHostException {
        ControladorPrincipal.getInstance().getVista().minimizarVentana();
        this.vista = new VentanaChat();
        this.vista.setActionListener(this);
        ((VentanaChat) this.vista).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ((VentanaChat) this.vista).addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    cerroVentana = true;
                    ControladorChat.getInstance().conexionEstablecida = false;
                    Usuario.getInstance().getSalida().println("504");   // Se desconecto del chat
                    Usuario.getInstance().desconectar();
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    ControladorPrincipal.getInstance().getVista().abrirVentana();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        recibirMensajesThread = new Thread(this);
        recibirMensajesThread.start();
    }

    public static ControladorChat getInstance() throws UnknownHostException {
        if (instance == null) {
            instance = new ControladorChat();
        }
        return instance;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.equalsIgnoreCase("ENVIAR")) {
            String mensaje = vista.getText();
            ((VentanaChat) vista).vaciarTextField();
            try {
                if (mensaje != null && !mensaje.isEmpty()) {
                    Usuario.getInstance().enviarMensaje(mensaje);
                    vista.agregarMensaje(Usuario.getInstance().getUsername() + " (Yo): " + mensaje);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void run() {
        try {
            this.conexionEstablecida = true;
            while (conexionEstablecida) {
                String codigo = null;

                if (!cerroVentana)
                    codigo = Usuario.getInstance().getEntrada().readLine();

                // Si su interlocutor salio del chat.
                if (codigo.equals("504")) {
                    conexionEstablecida = false;

                    // Si su interlocutor le envio un mensaje.
                } else if (codigo.equals("351")) {
                    vista.agregarMensaje(Usuario.getInstance().getSesionActual().getRemoto().getUsername() + ": " + Usuario.getInstance().recibirMensaje());
                }
            }
            if (!cerroVentana) {
                Usuario.getInstance().getSalida().println("504");
                Usuario.getInstance().desconectar();
                ControladorPrincipal.getInstance().getVista().abrirVentana();
                java.awt.Toolkit.getDefaultToolkit().beep();
                ((VentanaChat) vista).dispose();
            }
            cerroVentana = false;
        } catch (IOException e) {
        }
    }

}
