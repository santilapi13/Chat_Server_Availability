package server.server_secundario;

import server.Codigos;
import server.ControladorNuevosUsuarios;
import server.Servidor;
import server.SocketUsuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class NotificadorCaida extends Thread {
    private Socket socket;
    private InputStreamReader entradaSocket;
    private PrintWriter salida;
    private BufferedReader entrada;

    public NotificadorCaida(Socket socket) throws IOException {
        this.socket = socket;
        this.entradaSocket = new InputStreamReader(socket.getInputStream());
        this.entrada = new BufferedReader(entradaSocket);
        this.salida = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String mensaje = this.entrada.readLine();
            if (mensaje.equals(Codigos.ACTIVAR_SECUNDARIO.name())) {
                System.out.println("Servidor secundario activado");
                for (Map.Entry<String, SocketUsuario> entry : Servidor.getInstance().getUsuarios().entrySet()) {
                    entry.getValue().notificarCaida();
                }
                System.out.println("Estado resincronizado");
            }

            mensaje = this.entrada.readLine();
            if (mensaje.equals(Codigos.REINICIAR_PRIMARIO.name())) {
                System.out.println("Servidor primario reiniciado. Resincronizando...");
                Servidor.getInstance().conectarConPrimario();
                Servidor.getInstance().informarUsuariosAlPrimario();
                for (Map.Entry<String, SocketUsuario> entry : Servidor.getInstance().getUsuarios().entrySet()) {
                    entry.getValue().reinicioPrimario();
                }
            }
        } catch (IOException e) {}
    }
}
