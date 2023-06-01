package server.disponibilidad;

import server.Servidor;

import java.io.IOException;

public class Heartbeat extends Thread {
    private static Heartbeat instance;

    private Heartbeat() {
    }

    public static Heartbeat getInstance() {
        if (instance == null)
            instance = new Heartbeat();
        return instance;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                Servidor.getInstance().getMonitor().sendHeartbeat();
            } catch (InterruptedException e) {
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
