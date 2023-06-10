package client.model;

import server.Codigos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class Resincronizador extends Thread {
    private static Resincronizador instance;
    private Resincronizador() {
    }
    public static Resincronizador getInstance() {
        if (instance == null)
            instance = new Resincronizador();
        return instance;
    }

    @Override
    public void run() {
        try {
            InputStreamReader entradaSocket = new InputStreamReader(Usuario.getInstance().getSocketSecundario().getInputStream());
            BufferedReader entrada = new BufferedReader(entradaSocket);
            String mensaje = entrada.readLine();
            if (mensaje.equals(Codigos.RESINCRONIZAR.name())) {
                Usuario.getInstance().cambiarASecundario();
                Usuario.getInstance().resincronizar();
            }
        } catch (IOException e) {
        }
    }

}
