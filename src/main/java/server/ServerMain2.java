package server;

import server.disponibilidad.Heartbeat;

import java.io.IOException;
import java.util.Scanner;

public class ServerMain2 {
    public static void main(String[] args) {
        try {
            Servidor.getInstance().setPuerto(5678);
            Servidor.getInstance().setPrimario(false);
            Servidor.getInstance().conectarConPrimario();

            Servidor.getInstance().conectarConMonitor();
            ControladorNuevosUsuarios.getInstance().start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
