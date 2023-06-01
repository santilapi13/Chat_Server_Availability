package server;

import server.disponibilidad.Heartbeat;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        try {
            if (args.length == 0)
                Servidor.getInstance().setPuerto(2345); // Por defecto es 2345
            else {
                Servidor.getInstance().setPuerto(Integer.parseInt(args[0]));
                if (args.length == 2 && args[1].equals("s"))
                    Servidor.getInstance().setPrimario(false);
            }

            if (Servidor.getInstance().isPrimario()) {
                ControladorNuevosUsuarios.getInstance().start();
                Heartbeat.getInstance().start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
