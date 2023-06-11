package server;

import java.io.IOException;

public class ControladorNuevosUsuarios extends Thread {

    //singleton
    private boolean seguir;
    private static ControladorNuevosUsuarios instance;

    public static ControladorNuevosUsuarios getInstance() {
        if (instance == null) {
            instance = new ControladorNuevosUsuarios();
        }
        return instance;
    }

    private ControladorNuevosUsuarios() {
        this.seguir = true;
    }

    public void setSeguir(boolean seguir) {
        this.seguir = seguir;
    }

    @Override
    public void run() {
        try {
            while (seguir)
                Servidor.getInstance().escucharNuevosUsuarios();
        } catch (IOException e1) {
        }
    }

}
