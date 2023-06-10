package server.monitor;

import server.Codigos;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Monitor {
    private boolean primarioVivo;
    private int puerto;
    private Timer heartbeatTimer;
    private Socket serverSecundario;

    public Monitor(int puerto) {
        this.primarioVivo = true;
        this.heartbeatTimer = new Timer();
        this.puerto = puerto;
        this.serverSecundario = null;
    }

    public void conectarServerSecundario(String ip, int puerto) {
        while (this.serverSecundario == null) {
            try {
                Thread.sleep(1000);
                this.serverSecundario = new Socket(ip, puerto);
                System.out.println("Conexion establecida con el servidor secundario.");
            } catch (IOException e) {
                System.out.println("No se pudo conectar con el servidor secundario...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void empezarMonitoreo() throws IOException {
        ServerSocket conexionPrimario = new ServerSocket(this.puerto);
        while (primarioVivo) {
            this.heartbeatTimer.schedule(new HeartbeatTask(), 3000);

            conexionPrimario.accept();
            this.recibirHeartbeat();
        }
    }

    public void recibirHeartbeat() {
        this.heartbeatTimer.cancel();
        this.heartbeatTimer = new Timer();
        System.out.println("Se recibio latido del servidor primario.");
    }

    public void activarSecundario() {
        System.out.println("No se recibio latido del servidor primario en los ultimos 3 segs.");
        System.out.println("Activando servidor secundario...");
        try {
            PrintWriter salida = new PrintWriter(this.serverSecundario.getOutputStream(), true);
            salida.println(Codigos.ACTIVAR_SECUNDARIO);
        } catch (IOException e) {
            System.out.println("No se pudo activar el servidor secundario.");
        }
    }

    private class HeartbeatTask extends TimerTask {
        @Override
        public void run() {
            primarioVivo = false;
            activarSecundario();
        }
    }

}
