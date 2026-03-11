/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package susana.actividad41;

import java.io.*;
import java.net.*;

/**
 *
 * @author USER
 */
public class Cliente {

    static final String HOST = "localhost";
    static final int Puerto = 2000;

    public Cliente() {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            Socket skCliente = new Socket(HOST, Puerto);
            System.out.println("Conectado al servidor.");

            
            // Creo los flujos de entrada y salida
            InputStream is = skCliente.getInputStream();
            DataInputStream flujo_entrada = new DataInputStream(is);

            OutputStream os = skCliente.getOutputStream();
            DataOutputStream flujo_salida = new DataOutputStream(os);

            // Variables
            String respuesta;
            int miNumero;
            boolean acertado;
            
            // 1. Leer mensaje de bienvenida
            respuesta = flujo_entrada.readUTF();
            System.out.println(respuesta);

            do {
                System.out.println("introduce un número");
                miNumero = Integer.parseInt(reader.readLine()); //lee string
                flujo_salida.writeInt(miNumero); //envia int

                respuesta = flujo_entrada.readUTF();
                System.out.println(respuesta);
                acertado = flujo_entrada.readBoolean(); 
            } while (!acertado);

            
// 
            skCliente.close();
            System.out.println("Juego finalizado. Conexión cerrada.");
        } catch (IOException e) {
            // Si ocurre algún error, se imprime el mensaje de error 
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] arg) {
        new Cliente();
    }

}
