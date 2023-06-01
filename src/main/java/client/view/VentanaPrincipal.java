package client.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@SuppressWarnings("serial")



public class VentanaPrincipal extends JFrame implements IVista, KeyListener, MouseListener {

	private JPanel contentPane;
	private ActionListener actionListener;
	private JPanel panel;
	private JPanel panel_1;
	private JLabel lblNewLabel_2;
	private JRadioButton rdbtnNewRadioButton;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblNewLabel_3;
	private JList<String> list;
	private JButton btnIniciarChat;


	private DefaultListModel<String> modeloUsuario = new DefaultListModel<String>();
	private DefaultListModel<String> modeloUsuariosDisponibles = new DefaultListModel<String>();
	private JPanel panel_4;
	private JLabel lblNewLabel_4;
	private JList<String> listUsuariosDisponibles;
	private JPanel panel_5;
	private JButton btnActualizar;
	private JButton btnSolicitarChat;
	private JPanel panel_6;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_5;


	//Metodo main para ejecutar ventana
	/*
	public static void main(String[] args)
	{

		VentanaPrincipal window = new VentanaPrincipal();
	}
	*/

	public VentanaPrincipal() {
		setTitle("Ventana Principal para establecer conexion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 474, 414);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new GridLayout(2, 2, 0, 0));

		lblNewLabel_2 = new JLabel("Escucha Habilitada");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_2);

		rdbtnNewRadioButton = new JRadioButton("");
		rdbtnNewRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnNewRadioButton.addMouseListener(this);
		panel_1.add(rdbtnNewRadioButton);

		lblNewLabel_5 = new JLabel("");
		panel_1.add(lblNewLabel_5);

		panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));

		panel_3 = new JPanel();
		panel_2.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		lblNewLabel_3 = new JLabel("Solicitudes Entrantes");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblNewLabel_3, BorderLayout.NORTH);

		list = new JList();
		panel_3.add(list, BorderLayout.CENTER);
		list.addMouseListener(this);

		btnIniciarChat = new JButton("Iniciar Chat");
		btnIniciarChat.setEnabled(false);
		panel_3.add(btnIniciarChat, BorderLayout.SOUTH);
		;
		this.setVisible(true);
		this.list.setModel(this.modeloUsuario);

		panel_4 = new JPanel();
		panel_2.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		lblNewLabel_4 = new JLabel("Usuarios Disponibles");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_4.add(lblNewLabel_4, BorderLayout.NORTH);

		listUsuariosDisponibles = new JList();
		panel_4.add(listUsuariosDisponibles, BorderLayout.CENTER);
		listUsuariosDisponibles.addMouseListener(this);

		this.listUsuariosDisponibles.setModel(modeloUsuariosDisponibles);

		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		this.list.setBorder(border);
		this.listUsuariosDisponibles.setBorder(border);

		panel_5 = new JPanel();
		panel_4.add(panel_5, BorderLayout.SOUTH);
		panel_5.setLayout(new GridLayout(2, 0, 0, 0));

		btnActualizar = new JButton("ACTUALIZAR LISTA");
		panel_5.add(btnActualizar);

		btnSolicitarChat = new JButton("SOLICITAR CHAT");
		panel_5.add(btnSolicitarChat);

		panel_6 = new JPanel();
		contentPane.add(panel_6, BorderLayout.NORTH);
		panel_6.setLayout(new BorderLayout(0, 0));

		lblNewLabel = new JLabel("CHAT APP");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(lblNewLabel, BorderLayout.NORTH);

		lblNewLabel_1 = new JLabel("<dynamic>");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(lblNewLabel_1, BorderLayout.SOUTH);
		this.btnSolicitarChat.setEnabled(false);
	}


	private void setPlaceholderText(JTextField textField, String placeholder) {
	}

	public void setActionListener(ActionListener actionListener) {
		this.btnSolicitarChat.addActionListener(actionListener);
		this.rdbtnNewRadioButton.addActionListener(actionListener);
		this.btnIniciarChat.addActionListener(actionListener);
		this.btnActualizar.addActionListener(actionListener);

		this.actionListener = actionListener;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	private boolean validarSolicitarChat() {
		boolean resp = false;

		resp = this.listUsuariosDisponibles.getSelectedIndex() != -1 && !this.rdbtnNewRadioButton.isSelected();

		return resp;
	}

	public void cerrarse() {
		this.dispose();

	}


	public String getDireccionIP() {
		String usuario = this.listUsuariosDisponibles.getSelectedValue();
		String[] partes = usuario.split(", ");
		System.out.println("Direccion IP: " + partes[1]);
		return partes[1];
	}

	@Override
	public String getPuertoServer() {
		return null;
	}

	@Override
	public String getPuertoUsuario() {
		return null;
	}

	public String getPuertoIP() {
		String usuario = this.listUsuariosDisponibles.getSelectedValue();
		String[] partes = usuario.split(", ");
		System.out.println("Puerto IP: " + partes[2]);
		return partes[2];
	}

	public String getText() {
		return null;
	}


	public void agregarMensaje(String mensaje) {
	}


	public void agregarUsuario(String usuario) {
		this.modeloUsuario.addElement(usuario);
		this.validate(); // Para que se actualice el JList
	}

	public void agregarUsuarioDisponible(String usuario) {
		this.modeloUsuariosDisponibles.addElement(usuario);
		this.validate(); // Para que se actualice el JList
	}

	public void vaciarUsuariosDisponibles() {
		this.modeloUsuariosDisponibles.removeAllElements();
	}

	public void deseleccionar() {
		this.list.clearSelection();
		this.listUsuariosDisponibles.clearSelection();
		this.modeloUsuario.removeAllElements();
		this.btnIniciarChat.setEnabled(false);
		this.rdbtnNewRadioButton.setSelected(false);
	}

	public String getUsername() {
		//return this.textFieldNombre.getText();
		return "s";
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!this.list.isSelectionEmpty()) {
			this.btnIniciarChat.setEnabled(true);
		} else {
			this.btnIniciarChat.setEnabled(false);
		}
		if (!this.listUsuariosDisponibles.isSelectionEmpty()) {
			this.btnSolicitarChat.setEnabled(true);
		} else {
			this.btnSolicitarChat.setEnabled(false);
		}
		this.btnSolicitarChat.setEnabled(validarSolicitarChat());
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public void setTextFieldNombre(String textFieldNombre) {
		//this.textFieldNombre.setText(textFieldNombre);
	}

	public void minimizarVentana() {
		this.setVisible(false);
	}

	public void abrirVentana() {
		this.setVisible(true);
	}

}