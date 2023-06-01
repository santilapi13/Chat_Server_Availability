package server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 0)
                Servidor.getInstance().setPuerto(2345); // Por defecto es 2345
            else
                Servidor.getInstance().setPuerto(Integer.parseInt(args[0]));
            ControladorNuevosUsuarios.getInstance().start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
