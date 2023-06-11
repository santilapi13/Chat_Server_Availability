package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class SocketUsuario extends Thread {
    private String username;
    private String interlocutor;
    private Socket socket;
    private InputStreamReader entradaSocket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private boolean escuchando;
    private boolean enChat;

    @Override
    public void run() {
        while (true) {
            try {
                if (!enChat) {
                    this.escucharSolicitudes();
                } else
                    this.escucharMensajes();
            } catch (IOException e) {}
        }
    }

    public SocketUsuario(Socket socket) throws IOException {
        this.interlocutor = null;
        this.socket = socket;
        this.entradaSocket = new InputStreamReader(socket.getInputStream());
        this.entrada = new BufferedReader(entradaSocket);
        this.salida = new PrintWriter(socket.getOutputStream(), true);
        this.username = this.entrada.readLine();
        this.escuchando = false;
        this.enChat = false;
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStreamReader getEntradaSocket() {
        return entradaSocket;
    }

    public PrintWriter getSalida() {
        return salida;
    }

    public BufferedReader getEntrada() {
        return entrada;
    }
    public boolean isEscuchando() {
        return escuchando;
    }
    public String getInterlocutor() {
        return interlocutor;
    }
    public void setInterlocutor(String interlocutor) {
        this.interlocutor = interlocutor;
    }
    public void setEnChat(boolean enChat) {
        this.enChat = enChat;
    }


    private void escucharSolicitudes() throws IOException {
        String mensaje = this.entrada.readLine();

        // Si el usuario se desconecto, lo elimina de la lista.
        if (mensaje.equals(Codigos.DESCONECTAR.name())) {
            System.out.println("Usuario " + this.username + " se desconecto.");
            Servidor.getInstance().desconectarUsuario(this.username);

        // Si el usuario activo el modo escucha.
        } else if (mensaje.equals(Codigos.ACTIVAR_ESCUCHA.name())) {
            System.out.println("Usuario " + this.username + " activo el modo escucha.");
            this.escuchando = true;

        // Si el usuario desactivo el modo escucha.
        } else if (mensaje.equals(Codigos.DESACTIVAR_ESCUCHA.name())) {
            System.out.println("Usuario " + this.username + " desactivo el modo escucha.");
            this.escuchando = false;
            this.salida.println(Codigos.NADIE_SOLICITO);

        // Si el usuario solicito actualizar la lista de usuarios.
        } else if (mensaje.equals(Codigos.ACTUALIZAR_LISTA_USUARIOS.name())) {
            Servidor.getInstance().actualizarListaUsuarios(this.username, this.salida);

        // Si el usuario inicia el chat con quien se lo solicito
        } else if (this.interlocutor != null && mensaje.equals(Codigos.INICIAR_CHAT.name())) {
            this.escuchando = false;
            this.enChat = true;
            System.out.println("Usuario " + this.username + " ingreso al chat con " + this.interlocutor + ".");

        } else if (this.interlocutor == null) {
            String usernameInterlocutor = Servidor.getInstance().procesarSolicitud(mensaje, this.username);
            if (usernameInterlocutor != null) {
                this.enChat = true;
                this.interlocutor = usernameInterlocutor;
                this.salida.println(Codigos.OK);
                System.out.println("Usuario " + username + " se conecto con " + this.interlocutor + ".");
                this.salida.println(this.interlocutor);
            } else
                this.salida.println(Codigos.NO_ENCONTRADO);
        }
    }

    private void escucharMensajes() throws IOException {
        String codigo = this.entrada.readLine();

        // Si el usuario salio del chat.
        if (codigo.equals(Codigos.CERRAR_CHAT.name())) {
            System.out.println("Usuario " + this.username + " salio de la sesion de chat.");
            Servidor.getInstance().cerrarChat(this.interlocutor);
            this.enChat = false;
            this.interlocutor = null;

        // Si su interlocutor le envio un mensaje
        } else if (codigo.equals(Codigos.NUEVO_MENSAJE.name())) {
            String mensaje = this.entrada.readLine();
            System.out.println("Usuario " + this.username + " quiere enviar un mensaje.");
            Servidor.getInstance().enviarMensaje(this.interlocutor, mensaje);
        }
    }

    public void notificarCaida() throws IOException {
        this.salida.println(Codigos.RESINCRONIZAR);
        this.resincronizar();
    }

    public void resincronizar() throws IOException {
        try {
            String[] mensaje = this.entrada.readLine().split(" ");
            this.escuchando = Boolean.parseBoolean(mensaje[0]);
            this.enChat = !mensaje[1].equals("null");
            if (this.enChat)
                this.interlocutor = mensaje[1];
            this.start();
        } catch (IOException e) {
            // TODO: probar si funciona (se debe desconectar el usuario si se desconecto durante el primario)
            Servidor.getInstance().desconectarUsuario(this.username);
        }
    }

    public void reinicioPrimario() {
        this.salida.println(Codigos.REINICIAR_PRIMARIO);
    }

}
