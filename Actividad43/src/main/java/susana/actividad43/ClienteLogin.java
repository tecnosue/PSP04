package susana.actividad43;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteLogin {
    
    static final String HOST = "localhost";  // Dirección IP del servidor, en este caso se conecta a localhost (máquina local)
    static final int Puerto = 1500;         // Puerto al que se conecta el cliente

    
    public static void main(String[] arg) {
        try {
            Socket sCliente = new Socket(HOST, Puerto);
            System.out.println("Conectado al servidor.\n");
            
            DataInputStream flujo_entrada = new DataInputStream(sCliente.getInputStream());
            DataOutputStream flujo_salida = new DataOutputStream(sCliente.getOutputStream());
            
            Scanner sc = new Scanner(System.in);
            boolean conectado = true;
            
            while (conectado) {
                String mensaje = flujo_entrada.readUTF();
                
                // Mensajes que requieren respuesta del usuario
                if (mensaje.equals("Introduce usuario:") || 
                    mensaje.equals("Introduce contraseña:") ||
                    mensaje.equals("Introduce comando (ls/get/exit):") ||
                    mensaje.equals("Introduce el nombre del fichero:")) {
                    
                    System.out.print(mensaje + " ");
                    flujo_salida.writeUTF(sc.nextLine());
                    
                } else if (mensaje.equals("LOGIN_OK")) {
                    System.out.println("¡Login correcto!\n");
                    
                } else if (mensaje.equals("LOGIN_ERROR")) {
                    System.out.println("Usuario o contraseña incorrectos.\n");
                    
                } else if (mensaje.equals("LISTADO")) {
                    System.out.println("--- Contenido del directorio ---");
                    String linea;
                    while (!(linea = flujo_entrada.readUTF()).equals("FIN_LISTADO")) {
                        System.out.println(linea);
                    }
                    System.out.println("--- Fin del listado ---\n");
                    
                } else if (mensaje.equals("OK")) {
                    System.out.println("--- Contenido del Fichero ---");
                    String linea;
                    while (!(linea = flujo_entrada.readUTF()).equals("EOF")) {
                        System.out.println(linea);
                    }
                    System.out.println("--- Fin del Fichero ---\n");
                    
                } else if (mensaje.equals("ERROR")) {
                    System.out.println("Error: " + flujo_entrada.readUTF() + "\n");
                    
                } else if (mensaje.equals("COMANDO_ERROR")) {
                    System.out.println("Comando no válido. Usa: ls, get o exit\n");
                    
                } else if (mensaje.equals("ADIOS")) {
                    System.out.println("Sesión cerrada.");
                    conectado = false;
                }
            }
            
            sCliente.close();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}