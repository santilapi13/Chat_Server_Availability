package server;

import java.io.IOException;

public class ControladorNuevosUsuarios extends Thread {

    //singleton
    private static ControladorNuevosUsuarios instance;

    public static ControladorNuevosUsuarios getInstance() {
        if (instance == null) {
            instance = new ControladorNuevosUsuarios();
        }
        return instance;
    }

    private ControladorNuevosUsuarios() {
    }

    @Override
    public void run() {
        try {
            while (true)
                Servidor.getInstance().escucharNuevosUsuarios();
        } catch (IOException e1) {
        }
    }

}
