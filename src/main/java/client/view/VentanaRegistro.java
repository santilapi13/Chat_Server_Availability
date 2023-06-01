package client.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.*;

public class VentanaRegistro extends JFrame implements IVista, KeyListener, MouseListener {

    private JTextField campoUsuario;
    private JTextField campoDireccionIP;
    private JTextField campoPuertoServer;
    private JTextField campoPuertoUsuario;
    private JButton btnRegistrarse;

    private ActionListener actionListener;

    public VentanaRegistro() {
        // Configurar la ventana
        setTitle("Ventana de Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(329, 365);
        getContentPane().setLayout(new GridLayout(1, 1)); // Utilizar GridLayout para reorganizar los componentes

        // Crear panel para agrupar los campos de texto y los labels
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // Crear labels
        JLabel labelUsuario = new JLabel("   Nombre de Usuario:");
        labelUsuario.setFont(new Font("Tahoma", Font.BOLD, 11));
        JLabel labelPuertoUsuario = new JLabel("   Puerto:");
        labelPuertoUsuario.setFont(new Font("Tahoma", Font.BOLD, 11));
        JLabel labelDireccionIP = new JLabel("   Direcci\u00F3n IP de Servidor:");
        labelDireccionIP.setFont(new Font("Tahoma", Font.BOLD, 11));
        JLabel labelPuertoServer = new JLabel("   Puerto del Servidor:");
        labelPuertoServer.setFont(new Font("Tahoma", Font.BOLD, 11));




        // Crear campo de texto para Nombre de Usuario
        campoUsuario = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    g.setColor(Color.LIGHT_GRAY); // Establecer el color de texto gris claro
                    g.drawString("Ingrese nombre de usuario...", getInsets().left, (getHeight() + getFont().getSize()) / 2);
                }
            }
        };
        campoUsuario.setUI(new BasicTextFieldUI());

        // Crear campo de texto para Puerto de Usuario
        campoPuertoUsuario = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    g.setColor(Color.LIGHT_GRAY); // Establecer el color de texto gris claro
                    g.drawString("Ingrese su puerto...", getInsets().left, (getHeight() + getFont().getSize()) / 2);
                }
            }
        };
        campoPuertoUsuario.setUI(new BasicTextFieldUI());

        // Crear campo de texto para Dirección IP de Servidor
        campoDireccionIP = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    g.setColor(Color.LIGHT_GRAY); // Establecer el color de texto gris claro
                    g.drawString("Ingrese dirección IP de servidor...", getInsets().left, (getHeight() + getFont().getSize()) / 2);
                }
            }
        };
        campoDireccionIP.setUI(new BasicTextFieldUI());

        // Crear campo de texto para Puerto de Servidor
        campoPuertoServer = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    g.setColor(Color.LIGHT_GRAY); // Establecer el color de texto gris claro
                    g.drawString("Ingrese puerto del servidor...", getInsets().left, (getHeight() + getFont().getSize()) / 2);
                }
            }
        };
        campoPuertoServer.setUI(new BasicTextFieldUI());

        // Agregar los componentes al panel
        panel.add(labelUsuario);
        panel.add(campoUsuario);
        panel.add(labelPuertoUsuario);
        panel.add(campoPuertoUsuario);
        panel.add(labelDireccionIP);
        panel.add(campoDireccionIP);
        panel.add(labelPuertoServer);
        panel.add(campoPuertoServer);

        // Agregar el panel a la ventana
        getContentPane().add(panel);

        JLabel lblNewLabel = new JLabel("");
        panel.add(lblNewLabel);

        btnRegistrarse = new JButton("REGISTRARSE");
        btnRegistrarse.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(btnRegistrarse);
        this.btnRegistrarse.setEnabled(false);

        this.campoUsuario.addKeyListener(this);
        this.campoDireccionIP.addKeyListener(this);
        this.campoPuertoServer.addKeyListener(this);
        this.campoPuertoUsuario.addKeyListener(this);

        // Hacer visible la ventana
        setVisible(true);
    }

    @Override
    public void setActionListener(ActionListener actionListener) {

        this.btnRegistrarse.addActionListener(actionListener);
        this.actionListener = actionListener;
    }


    @Override
    public void minimizarVentana() {

    }

    @Override
    public void abrirVentana() {

    }



    @Override
    public void cerrarse() {

    }

    @Override
    public String getDireccionIP() {

        return this.campoDireccionIP.getText();
    }

    @Override
    public String getPuertoIP() {
        return null;
    }

    @Override
    public String getPuertoServer() {
        return this.campoPuertoServer.getText();
    }

    @Override
    public String getPuertoUsuario() {
        return this.campoPuertoUsuario.getText();
    }

    @Override
    public String getText() {

            return this.campoUsuario.getText();
    }

    @Override
    public void agregarMensaje(String mensaje) {

    }

    @Override
    public void agregarUsuario(String usuario) {

    }

    @Override
    public void deseleccionar() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.btnRegistrarse.setEnabled(!campoUsuario.getText().isEmpty() && !campoPuertoUsuario.getText().isEmpty() && !campoDireccionIP.getText().isEmpty() && !campoPuertoServer.getText().isEmpty());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}