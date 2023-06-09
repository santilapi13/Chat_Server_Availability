package server.disponibilidad;

import java.util.Scanner;

public class MonitorMain {
    public static void main(String[] args) {

        try {
            Monitor monitor = new Monitor();

            Scanner sc = new Scanner(System.in);

            System.out.println("Ingrese IP del servidor primario: ");
            String ip = sc.nextLine();
            System.out.println("Ingrese puerto del servidor primario: ");
            String puerto = sc.nextLine();
            monitor.conectarServerPrimario(ip, Integer.parseInt(puerto));

            System.out.println("Ingrese IP del servidor secundario: ");
            ip = sc.nextLine();
            System.out.println("Ingrese puerto del servidor secundario: ");
            puerto = sc.nextLine();
            monitor.conectarServerSecundario(ip, Integer.parseInt(puerto));

            monitor.start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
