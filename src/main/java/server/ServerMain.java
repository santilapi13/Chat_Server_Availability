package server;

import server.disponibilidad.Heartbeat;

import java.io.IOException;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        try {
            String ip;
            String puerto;
            Scanner sc = new Scanner(System.in);

            if (args.length == 0) {
                Servidor.getInstance().setPuerto(2345); // Por defecto es 2345

                System.out.println("Ingrese IP del servidor secundario: ");
                ip = sc.nextLine();
                System.out.println("Ingrese puerto del servidor secundario: ");
                puerto = sc.nextLine();
                Servidor.getInstance().conectarConSecundario(ip, Integer.parseInt(puerto));

            } else {
                Servidor.getInstance().setPuerto(Integer.parseInt(args[0]));
                if (args.length == 2 && args[1].equals("s")) {
                    Servidor.getInstance().setPrimario(false);
                    Servidor.getInstance().conectarConPrimario();
                } else {

                    System.out.println("Ingrese IP del servidor secundario: ");
                    ip = sc.nextLine();
                    System.out.println("Ingrese puerto del servidor secundario: ");
                    puerto = sc.nextLine();
                    Servidor.getInstance().conectarConSecundario(ip, Integer.parseInt(puerto));
                }
            }

            Servidor.getInstance().conectarConMonitor();
            ControladorNuevosUsuarios.getInstance().start();
            if (Servidor.getInstance().isPrimario()) {
                Heartbeat.getInstance().start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
