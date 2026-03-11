/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package susana.actividad41;

import java.io.*;
import java.net.*;
import java.util.Random;

/**
 *
 * @author USER
 */
public class Servidor extends Thread {

    Socket skCliente; 

    static final int Puerto = 2000;  // Publicarpuerto

    //constructor
    public Servidor(Socket sCliente) {
        skCliente = sCliente;
    }

    public static void main(String[] arg) {
        try {
            // Inicio el servidor en el puerto 
            ServerSocket skServidor = new ServerSocket(Puerto);
            System.out.println("Escucho el puerto " + Puerto);
            
            while (true) {
                // Esperamos un cliente 
                Socket skCliente = skServidor.accept(); 
                System.out.println("Cliente conectado");
                // Atiendo al cliente mediante un thread
                new Servidor(skCliente).start();
            }

        } catch (IOException e) {
            // En caso de error, se imprime el mensaje de error 
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        try {
            // Creo los flujos de entrada y salida
        InputStream is = skCliente.getInputStream();
        DataInputStream flujo_entrada = new DataInputStream(is);

        OutputStream os = skCliente.getOutputStream();
        DataOutputStream flujo_salida = new DataOutputStream(os);

        // 1. GENERAR NÚMERO SECRETO (Entre 0 y 100) 
        // CUERPO DEL ALGORITMO
        Random randomGenerator = new Random();
        int numeroSecreto = randomGenerator.nextInt(101);
        System.out.println("el numero secreto es: " + numeroSecreto);

        boolean acertado = false;
        flujo_salida.writeUTF("Tienes que adivinar un número del 0 al 100");

        // 2. BUCLE DEL JUEGO 
        while (!acertado) {
            // Esperamos a leer el número que envía el cliente 
            int numeroCliente = flujo_entrada.readInt();

            if (numeroCliente == numeroSecreto) {
                flujo_salida.writeUTF("¡Correcto! Has adivinado el número.");
                flujo_salida.writeBoolean(true); // Enviamos 'true' para 
                acertado = true;
            } else if (numeroCliente < numeroSecreto) {
                flujo_salida.writeUTF("El número secreto es MAYOR.");
                flujo_salida.writeBoolean(false); // Enviamos 'false' para 

            } else {
                flujo_salida.writeUTF("El número secreto es MENOR.");
                flujo_salida.writeBoolean(false); // Enviamos 'false' para 

            }
        }

        // Cerramos la conexión con el cliente 
        skCliente.close();
        System.out.println("Juego terminado.");
   
            
        }catch(Exception e){
            System.out.println( e.getMessage());

        }
    }
}
        

        
