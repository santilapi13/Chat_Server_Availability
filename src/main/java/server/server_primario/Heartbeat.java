package server.server_primario;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Heartbeat extends Thread {
    private static Heartbeat instance;
    private String ip;
    private int puerto;

    private Heartbeat() {
    }

    public static Heartbeat getInstance() {
        if (instance == null)
            instance = new Heartbeat();
        return instance;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000);
                Socket socket = new Socket(ip, puerto);
            } catch (IOException e) {
                System.out.println("No se pudo conectar con el monitor. Reintentando en 5 segundos...");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e1);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
