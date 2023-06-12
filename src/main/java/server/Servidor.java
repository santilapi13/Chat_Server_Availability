package server;

import server.server_secundario.NotificadorCaida;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Servidor {
    private HashMap<String, SocketUsuario> usuarios = new HashMap<String, SocketUsuario>();
    private ServerSocket socketServerNuevosUsuarios;
    private ServerSocket socketServerSecundario;
    private NotificadorCaida monitor;
    private Socket serverRedundante;
    private static Servidor instance;
    private int puerto;
    private boolean primario;
    private String password = generarNumero();
    private int usuariosAResincronizar;

    public static Servidor getInstance() throws IOException {
        if (instance == null)
            instance = new Servidor();
        return instance;
    }

    private Servidor() {
        this.primario = true;
        this.serverRedundante = null;
        this.usuariosAResincronizar = -1;
    }

    public HashMap<String, SocketUsuario> getUsuarios() {
        return usuarios;
    }

    public void setPuerto(int puerto) throws IOException {
        this.puerto = puerto;
        this.socketServerNuevosUsuarios = new ServerSocket(puerto);
        this.socketServerSecundario = new ServerSocket(puerto + 1);
    }

    public NotificadorCaida getMonitor() {
        return monitor;
    }

    public boolean isPrimario() {
        return this.primario;
    }

    public void setPrimario(boolean primario) {
        this.primario = primario;
    }

    public void conectarConSecundario(String IP, int puertoSecundario) {
        System.out.println("Enviando solicitud al servidor secundario...");
        while (this.serverRedundante == null) {
            try {
                this.serverRedundante = new Socket(IP, puertoSecundario + 1);
                System.out.println("Conexion establecida con el servidor secundario.");
            } catch (IOException e) {
                System.out.println("No se pudo conectar con el servidor secundario. Reintentando en 5 segundos...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    public void conectarConPrimario() throws IOException {
        System.out.println("Esperando solicitud del servidor primario...");
        this.serverRedundante = this.socketServerSecundario.accept();
        System.out.println("Conexion establecida con el servidor primario.");
    }

    public void conectarConMonitor() throws IOException {
        System.out.println("Esperando conexion desde el monitor...");
        Socket socket = this.socketServerNuevosUsuarios.accept();
        this.monitor = new NotificadorCaida(socket);
        System.out.println("Conexion establecida con el monitor.");
        this.monitor.start();
    }

    public void registrarUsuario(Socket socket) throws IOException {
        SocketUsuario socketUsuario = new SocketUsuario(socket);
        if (!this.usuarios.containsKey(socketUsuario.getUsername())) {
            System.out.println("Usuario registrado: " + socketUsuario.getUsername());
            if (this.primario) {
                this.usuarios.put(socketUsuario.getUsername(), socketUsuario);
                if (usuariosAResincronizar > 0) {
                    socketUsuario.resincronizar();
                    usuariosAResincronizar--;
                } else {
                    socketUsuario.getSalida().println(Codigos.OK);
                    socketUsuario.getSalida().println(this.password);
                    if (serverRedundante != null) {
                        int puerto = this.serverRedundante.getPort() - 1;
                        socketUsuario.getSalida().println(this.serverRedundante.getInetAddress().getHostAddress() + " " + puerto);  // Se pasa la info del server secundaria al usuario
                    }
                    socketUsuario.start();
                }
            } else
                this.usuarios.put(socketUsuario.getUsername(), socketUsuario);

        } else
            socketUsuario.getSalida().println(Codigos.USERNAME_REPETIDO);
    }

    public void desconectarUsuario(String username) {
        this.usuarios.remove(username);
    }

    public void actualizarListaUsuarios(String usuarioAActualizar, PrintWriter salida) {
        salida.println(this.usuarios.size() - 1);
        for (Map.Entry<String, SocketUsuario> usuario : this.usuarios.entrySet()) {
            if (usuario.getValue().getUsername() != usuarioAActualizar) {
                // Envía username, IP y Puerto
                salida.println(usuario.getKey() + ", " + usuario.getValue().getSocket().getInetAddress().getHostAddress() + ", " + usuario.getValue().getSocket().getPort());
            }

        }
    }

    public void escucharNuevosUsuarios() throws IOException {
        Socket socket = socketServerNuevosUsuarios.accept();
        System.out.println("Usuario " + socket.getInetAddress().getHostAddress() + " : " + socket.getPort() + " intenta registrarse");
        this.registrarUsuario(socket);
    }

    public String procesarSolicitud(String mensaje, String username) throws UnknownHostException {
        String[] partes = mensaje.split(" ");
        String IP = partes[0];
        int puerto = Integer.parseInt(partes[1]);
        System.out.println("Usuario " + username + " solicito iniciar un chat con " + IP + ":" + puerto + ".");
        String usernameInterlocutor = null;

        // Busca el usuario con el que quiere establecer conexion.
        for (Map.Entry<String, SocketUsuario> entry : this.usuarios.entrySet()) {
            SocketUsuario v = entry.getValue();

            // Si el IP y puerto ingresados coinciden con alguno, los vincula (setea el interlocutor).
            if (v.isEscuchando() && v.getInterlocutor() == null && v.getSocket().getInetAddress().getHostAddress().equals(IP) && v.getSocket().getPort() == puerto) {
                v.setInterlocutor(username);
                usernameInterlocutor = v.getUsername();
                v.getSalida().println(username);
            }
        }
        return usernameInterlocutor;
    }

    public void cerrarChat(String username) {
        SocketUsuario usuario = this.usuarios.get(username);
        usuario.getSalida().println(Codigos.CERRAR_CHAT);
    }

    public void enviarMensaje(String username, String mensaje) {
        SocketUsuario usuario = this.usuarios.get(username);
        System.out.println("Enviando mensaje a " + username + "...");
        usuario.getSalida().println(Codigos.NUEVO_MENSAJE);
        usuario.getSalida().println(mensaje);
    }

    public String generarNumero() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int numero = random.nextInt(9) + 1; // Genera un número aleatorio entre 1 y 9
            sb.append(numero);
        }

        return sb.toString();
    }

    public void informarUsuariosAlPrimario() throws IOException {
        PrintWriter salida = new PrintWriter(this.serverRedundante.getOutputStream(), true);
        salida.println(this.usuarios.size());
        System.out.println("Enviando cantidad de usuarios (" + this.usuarios.size() + ") al servidor primario...");
    }

    public void recibirUsuariosAResincronizar() throws IOException {
        InputStreamReader entradaSocket = new InputStreamReader(this.serverRedundante.getInputStream());
        BufferedReader entrada = new BufferedReader(entradaSocket);
        this.usuariosAResincronizar = Integer.parseInt(entrada.readLine());
        System.out.println("Usuarios a resincronizar: " + this.usuariosAResincronizar);
    }

}
