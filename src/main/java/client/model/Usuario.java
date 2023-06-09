package client.model;

import client.controller.ControladorChat;
import client.controller.ControladorPrincipal;
import server.Codigos;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;

/**
 * @author : Grupo 4 - Avalos, Lapiana y Sosa
 */

public class Usuario implements Runnable, GestorSesiones, EnvioMensajes, GestorConexion, GestorSeguridad {
    private CredencialesUsuario credencialesUsuario;
    private Socket socketPrimario;
    private Socket socketSecundario;
    private InputStreamReader entradaSocket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private boolean escuchando;
    private ArrayList<SesionChat> sesionesAnteriores;
    private SesionChat sesionChatActual;
    private boolean solicitando = false;
    private String ipServidorPrimario;
    private int puertoServidorPrimario;

    private String password;

     //PATRON SINGLETON
    private static Usuario instance;
    private Usuario() throws UnknownHostException {
        this.credencialesUsuario = new CredencialesUsuario(InetAddress.getLocalHost().getHostAddress(), "");
        this.escuchando = false;
        this.sesionesAnteriores = new ArrayList<>();
        this.sesionChatActual = null;
        this.socketSecundario = null;
    }
    public static Usuario getInstance() throws UnknownHostException {
        if (instance == null)
            instance = new Usuario();
        return instance;
    }

     //GETTERS Y SETTERS
    public CredencialesUsuario getInformacion() {
        return credencialesUsuario;
    }
    public String getIP() {
        return this.credencialesUsuario.getIP();
    }
    public int getPuerto() {
        return this.credencialesUsuario.getPuerto();
    }
    public String getUsername() {
        return this.credencialesUsuario.getUsername();
    }
    public void setPuerto(int puerto) {
        this.credencialesUsuario.setPuerto(puerto);
    }
    public void setUsername(String username) {
        this.credencialesUsuario.setUsername(username);
    }
    public Socket getSocketPrimario() {
        return this.socketPrimario;
    }
    public Socket getSocketSecundario() {
        return this.socketSecundario;
    }
    public SesionChat getSesionActual() {
        return sesionChatActual;
    }
    @Override
    public void setSesionActual(SesionChat sesionChatActual) {
        this.sesionChatActual = sesionChatActual;
    }
    @Override
    public void addNuevaSesion(SesionChat sesionChat) {
        this.sesionesAnteriores.add(sesionChat);
    }

    public boolean isEscuchando() {
        return escuchando;
    }

    public BufferedReader getEntrada() {
        return entrada;
    }

    public PrintWriter getSalida() {
        return salida;
    }

    public void registrarseEnServidor(String IP, int puertoServer, String usuario, int puertoUsuario) throws IOException {
        this.credencialesUsuario.setPuerto(puertoUsuario);
        this.ipServidorPrimario = IP;
        this.puertoServidorPrimario = puertoServer;
        this.socketPrimario = new Socket(IP, puertoServer, null, puertoUsuario);
        this.credencialesUsuario.setUsername(usuario);
        iniciarESSockets();
    }

    public void conectarseServerSecundario() throws IOException {
        if (this.socketSecundario == null) {
            String[] msg = this.entrada.readLine().split(" ");
            System.out.println("Server secundario: " + msg[0] + " " + msg[1]);
            this.socketSecundario = new Socket(msg[0], Integer.parseInt(msg[1]));
            PrintWriter salidaSecundario = new PrintWriter(socketSecundario.getOutputStream(), true);
            salidaSecundario.println(this.credencialesUsuario.getUsername());
            Resincronizador.getInstance().start();
        }
    }

    private void iniciarESSockets() throws IOException {
        this.entradaSocket = new InputStreamReader(socketPrimario.getInputStream());
        this.entrada = new BufferedReader(entradaSocket);
        this.salida = new PrintWriter(socketPrimario.getOutputStream(), true);
        this.salida.println(this.credencialesUsuario.getUsername());
    }

    public void cambiarASecundario() throws IOException {
        this.entradaSocket = new InputStreamReader(socketSecundario.getInputStream());
        this.entrada = new BufferedReader(entradaSocket);
        this.salida = new PrintWriter(socketSecundario.getOutputStream(), true);
    }

    public void cambiarAPrimario() throws IOException {
        this.socketPrimario = new Socket(ipServidorPrimario, puertoServidorPrimario, null, this.getPuerto());
        this.entradaSocket = new InputStreamReader(socketPrimario.getInputStream());
        this.entrada = new BufferedReader(entradaSocket);
        this.salida = new PrintWriter(socketPrimario.getOutputStream(), true);
        this.salida.println(this.credencialesUsuario.getUsername());
    }

    public void resincronizar() throws IOException {
        // Avisa si estaba escuchando, y si estaba en chat
        if (this.sesionChatActual != null)
            this.salida.println(this.escuchando + " " + this.sesionChatActual.getRemoto().getUsername());
        else
            this.salida.println(this.escuchando + " " + null);
        if (this.sesionChatActual == null)
            ControladorPrincipal.getInstance().actualizarListaUsuarios();
    }

    public void prepararReinicio() throws IOException {
        try {
            // Reutiliza el socketPrimario para comprobar el reinicio
            this.socketPrimario.close();
            ServerSocket serverSocket = new ServerSocket(this.socketSecundario.getLocalPort() + 1);
            this.socketPrimario = serverSocket.accept();
            System.out.println("Conectado con socket de reinicio");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            InputStreamReader entradaSocket = null;
            try {
                entradaSocket = new InputStreamReader(socketPrimario.getInputStream());
                BufferedReader entrada = new BufferedReader(entradaSocket);
                String mensaje = entrada.readLine();
                if (mensaje.equals(Codigos.REINICIAR_PRIMARIO.name())) {
                    System.out.println("Reinicio detectado. Resincronizando con servidor principal...");
                    this.cambiarAPrimario();
                    this.resincronizar();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

     @Override
     public void run() {
        try {
            this.activarModoEscucha();
        } catch (IOException e) {}
     }

    private void activarModoEscucha() throws IOException {
        this.salida.println(Codigos.ACTIVAR_ESCUCHA);
        escuchando = true;
        System.out.println("Modo escucha activado.");
        this.iniciarSesionChat();
    }

    public void desactivarModoEscucha() throws IOException {
        this.salida.println(Codigos.DESACTIVAR_ESCUCHA);
        escuchando = false;
        System.out.println("Modo escucha desactivado.");
    }

    public void solicitarChat(CredencialesUsuario credencialesUsuarioReceptor) throws IOException {
        String IP = credencialesUsuarioReceptor.getIP();

        if (IP.equals("localhost")){
            InetAddress localAddress = socketPrimario.getLocalAddress();
            IP = localAddress.getHostAddress();
        }

        this.salida.println(IP + " " + credencialesUsuarioReceptor.getPuerto());
        this.solicitando = true;
    }

    public void iniciarSesionChat() throws IOException {
        String usernameRemoto = this.entrada.readLine();
        if (!usernameRemoto.equals(Codigos.NADIE_SOLICITO.name())) {
            System.out.println("Usuario remoto: " + usernameRemoto);
            if (this.escuchando || this.solicitando) {
                this.sesionChatActual = new SesionChat(this.credencialesUsuario, new CredencialesUsuario(this.socketPrimario.getInetAddress().toString(), this.socketPrimario.getPort(), usernameRemoto));
                if (this.solicitando)
                    ControladorChat.getInstance().nuevaVentana();
                else
                    ControladorPrincipal.getInstance().agregarUsuario(usernameRemoto);
            }
        }
    }

    public void enviarMensaje(String mensaje) throws IOException {

        /*
        String mensajeEncriptado;
        try {
            mensajeEncriptado = encriptar(this.password, mensaje, "DES");
        } catch (Exception e) {
            e.printStackTrace();
            return; // Manejo del error de encriptación
        }

        System.out.println(mensajeEncriptado);
        */
        this.sesionChatActual.addMensaje(mensaje, true);
        this.salida.println(Codigos.NUEVO_MENSAJE);
        //this.salida.println(mensajeEncriptado);

        this.salida.println(mensaje);
    }

    public String encriptar(String pass, String texto, String algoritmo) throws Exception {
        java.security.Key key = new SecretKeySpec(pass.getBytes(), algoritmo);
        Cipher cipher = Cipher.getInstance(algoritmo);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytesEncriptados = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(bytesEncriptados);
    }

    public String recibirMensaje() throws IOException {
        String mensaje = this.entrada.readLine();
        this.sesionChatActual.addMensaje(mensaje, false);

        // Desencriptar el mensaje
        /*
        String mensajeDesencriptado;
        try {
            mensajeDesencriptado = desencriptar(this.password, mensaje, "DES");
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Manejo del error de desencriptación
        }

        return mensajeDesencriptado;
        */
        return mensaje;
    }

    public String desencriptar(String pass, String textoEncriptado, String algoritmo) throws Exception {
        java.security.Key key = new SecretKeySpec(pass.getBytes(), algoritmo);
        Cipher cipher = Cipher.getInstance(algoritmo);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] bytesEncriptados = Base64.getDecoder().decode(textoEncriptado);
        byte[] bytesDesencriptados = cipher.doFinal(bytesEncriptados);
        return new String(bytesDesencriptados);
    }

    public void desconectar() throws IOException {
        this.addNuevaSesion(this.sesionChatActual);
        sesionChatActual = null;
        this.escuchando = false;
        this.solicitando = false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
