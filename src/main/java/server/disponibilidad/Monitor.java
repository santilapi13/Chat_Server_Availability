package server.disponibilidad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Monitor extends Thread {
    // TODO: Conectarlo con ambos servidores.
    // TODO: Crear un método que reciba un mensaje de latido y, si no lo recibe, avisar al secundario.
    private Socket serverPrimario;
    private Socket serverSecundario;

    public void conectarServerPrimario(String ip, int puerto) throws IOException {
        this.serverPrimario = new Socket(ip, puerto);
    }

    public void conectarServerSecundario(String ip, int puerto) throws IOException {
        this.serverSecundario = new Socket(ip, puerto);
    }

    @Override
    public void run() {
        try {
            while (true) {
                InputStreamReader entradaSocket = new InputStreamReader(serverPrimario.getInputStream());
                BufferedReader entrada = new BufferedReader(entradaSocket);
                String mensaje = entrada.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
