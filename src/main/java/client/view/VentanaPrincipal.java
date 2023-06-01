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
	private JLabel lblNewLabel;
	private JPanel panel;
	private JPanel panel_1;
	private JLabel lblNewLabel_2;
	private JRadioButton rdbtnNewRadioButton;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblNewLabel_3;
	private JList<String> list;
	private JButton btnIniciarChat;
	private JButton btnSolicitarChat;
	private JPanel panel_4;
	private JLabel lblNewLabel_4;
	private JTextField textFieldIP;
	private JTextField textFieldPuerto;


	private DefaultListModel<String> modeloUsuario = new DefaultListModel<String>();
	private JLabel lblNewLabel_1;

	private JLabel lblNewLabel_6;


	public VentanaPrincipal() {
		setTitle("Ventana Principal para establecer conexion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 474, 414);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);

		lblNewLabel = new JLabel("CHAT APP");
		lblNewLabel.setForeground(new Color(255, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel, BorderLayout.NORTH);

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
		rdbtnNewRadioButton.addMouseListener(this);
		panel_1.add(rdbtnNewRadioButton);

		lblNewLabel_1 = new JLabel();
		panel_1.add(lblNewLabel_1);

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

		panel_4 = new JPanel();
		panel_2.add(panel_4);

		lblNewLabel_4 = new JLabel("Direccion IPv6");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 11));

		btnSolicitarChat = new JButton("SOLICITAR CHAT");
		btnSolicitarChat.setEnabled(false);

		textFieldIP = new JTextField();
		textFieldIP.setColumns(10);
		textFieldIP.addKeyListener(this);
		setPlaceholderText(textFieldIP, "Ingrese dirección IP");

		textFieldPuerto = new JTextField();
		textFieldPuerto.setColumns(10);
		textFieldPuerto.addKeyListener(this);
		setPlaceholderText(textFieldPuerto, "Ingrese puerto IP");

		JLabel lblNewLabel_5 = new JLabel("Puerto IP");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblNewLabel_6 = new JLabel("Solicitar Chat");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 14));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
				gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
								.addGap(63)
								.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
										.addComponent(btnSolicitarChat)
										.addComponent(textFieldIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblNewLabel_4)
										.addComponent(textFieldPuerto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblNewLabel_5)
										.addGroup(gl_panel_4.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(lblNewLabel_6, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
								.addContainerGap(44, Short.MAX_VALUE))
		);
		gl_panel_4.setVerticalGroup(
				gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
								.addComponent(lblNewLabel_6)
								.addGap(34)
								.addComponent(lblNewLabel_4)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(textFieldIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(30)
								.addComponent(lblNewLabel_5)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textFieldPuerto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
								.addComponent(btnSolicitarChat)
								.addGap(32))
		);
		panel_4.setLayout(gl_panel_4);
		;
		this.setVisible(true);
		this.list.setModel(this.modeloUsuario);
	}


	private void setPlaceholderText(JTextField textField, String placeholder) {
		textField.setForeground(Color.GRAY);
		textField.setText(placeholder);
		textField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				if (textField.getText().equals(placeholder)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}
			}

			public void focusLost(java.awt.event.FocusEvent evt) {
				if (textField.getText().isEmpty()) {
					textField.setForeground(Color.GRAY);
					textField.setText(placeholder);
				}
			}
		});
	}

	public void setActionListener(ActionListener actionListener) {
		this.btnSolicitarChat.addActionListener(actionListener);
		this.rdbtnNewRadioButton.addActionListener(actionListener);
		//this.textFieldNombre.addActionListener(actionListener);
		this.btnIniciarChat.addActionListener(actionListener);

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
		this.btnSolicitarChat.setEnabled(validarSolicitarChat());
	}

	private boolean validarSolicitarChat() {
		boolean resp = false;

		resp = this.textFieldPuerto.getText() != null && !this.textFieldPuerto.getText().isEmpty() && this.textFieldIP.getText() != null && !this.textFieldIP.getText().isEmpty();

		return resp;
	}

	public void cerrarse() {
		this.dispose();

	}


	public String getDireccionIP()
	{
		return this.textFieldIP.getText();
	}

	@Override
	public String getPuertoServer() {
		return null;
	}

	@Override
	public String getPuertoUsuario() {
		return null;
	}

	public String getPuertoIP()
	{
		return this.textFieldPuerto.getText();
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


	public void deseleccionar() {
		this.list.clearSelection();
		this.modeloUsuario.removeAllElements();
		this.btnIniciarChat.setEnabled(false);
		this.rdbtnNewRadioButton.setSelected(false);
		//this.textFieldNombre.setEnabled(true);
		this.textFieldIP.setEnabled(true);
		this.textFieldPuerto.setEnabled(true);
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
		if (e.getSource() == this.rdbtnNewRadioButton) {
			if (this.rdbtnNewRadioButton.isSelected()) {
				this.textFieldIP.setText("");
				this.textFieldPuerto.setText("");
				//this.textFieldNombre.setEnabled(false);
				this.textFieldIP.setEnabled(false);
				this.textFieldPuerto.setEnabled(false);
			} else {
				//this.textFieldNombre.setEnabled(true);
				this.textFieldIP.setEnabled(true);
				this.textFieldPuerto.setEnabled(true);
			}
		}
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