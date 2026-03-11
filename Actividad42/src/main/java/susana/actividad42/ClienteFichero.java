
package susana.actividad42;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class ClienteFichero {
    static final String HOST = "localhost";  // Dirección IP del servidor, en este caso se conecta a localhost (máquina local)
    static final int Puerto = 1500;         // Puerto al que se conecta el cliente

    
    public static void main(String[] arg) {
       try {
            Socket sCliente = new Socket(HOST, Puerto);
            
            DataInputStream flujo_entrada = new DataInputStream(sCliente.getInputStream());
            DataOutputStream flujo_salida = new DataOutputStream(sCliente.getOutputStream());
            
            Scanner sc = new Scanner(System.in);

            System.out.print("Introduce la ruta o nombre del fichero a solicitar: ");
            String fichero = sc.nextLine();

            // 1. Enviar nombre del fichero
            flujo_salida.writeUTF(fichero);

            // 2. Comprobar estado
            String estado = flujo_entrada.readUTF();

            if (estado.equals("OK")) {
                System.out.println("--- Contenido del Fichero ---");
                String linea;
                // Leemos hasta encontrar la marca "EOF"
                while (!(linea = flujo_entrada.readUTF()).equals("EOF")) {
                    System.out.println(linea);
                }
                System.out.println("--- Fin del Fichero ---");
            } else {
                // Si hubo error, leemos el mensaje de error
                String mensajeError = flujo_entrada.readUTF();
                System.out.println("Error del servidor: " + mensajeError);
            }

            sCliente.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
