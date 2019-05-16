/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketcliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Docente AT-4
 */
public class SocketCliente {
    
    private Socket socket;
    private DataInputStream bufferEntrada;
    private DataOutputStream bufferSalida;
    
    Scanner scan = new Scanner(System.in);
    
    final String COMANDO_SALIR = "salir()";
    
    public void conectar(String ip, int puerto){
        try {
            socket = new Socket(ip, puerto);
            System.out.println("Conectar a: " + 
                    socket.getInetAddress().getHostName());                        
        } catch (Exception e) {            
            System.out.println("Error al conectar con el servidor: " +
                    e.getMessage());
        }
    }
    public void cerrarConexion(){
        try {
            bufferEntrada.close();
            bufferSalida.close();
            socket.close();
            System.out.println("Fin de conexión");
        } catch (Exception e) {
            System.out.println("Error al cerrar la conexión: " + 
                    e.getMessage());
        }
    }
    
    public void getDatos(){
        String mensaje = "";
        try {
            do {                
                bufferEntrada = new DataInputStream(
                        socket.getInputStream());
                mensaje = (String) bufferEntrada.readUTF();
                System.out.print("\n << Servidor >> " + 
                        mensaje);
                System.out.print("\n << Cliente >> " );
            } while (!mensaje.equals(COMANDO_SALIR));
        } catch (Exception e) {
            System.out.println("Error al recibir mensaje: " + 
                    e.getMessage());
        }
    }
    
    public void sendDatos(){
        String entrada = "";
        while (true) {            
            System.out.print("\n << Cliente >> " );
            entrada = scan.nextLine();
            if (entrada.length() > 0) {
                enviar(entrada);
            }            
        }
    }
    public void enviar(String mensaje){
        try {
            bufferSalida = new DataOutputStream(
                socket.getOutputStream());
            bufferSalida.writeUTF(mensaje);
            bufferSalida.flush();
        } catch (Exception e) {
            System.out.println("Error no se puede enviar el mensaje: "+ 
                    e.getMessage());
        }
    }
    public void ejecutarCliente(final String ip, final int puerto){
        Thread hilo =  new Thread(new Runnable() {
            @Override
            public void run() {
                 try {
                     conectar(ip, puerto);
                     getDatos();
                } finally {
                     cerrarConexion();
                 }
            }
            // www.github.com/ozwaldo
        });
        hilo.start();
    }
    public static void main(String[] args) {
        SocketCliente cliente = new SocketCliente();
        
        Scanner scan = new  Scanner(System.in);
        System.out.print("Escriba la IP [localhost]: ");
        String ip = scan.nextLine();
        if (ip.length() <= 0) {
            ip = "localhost";
        }
        
        System.out.print("Escriba el puerto [5555]: ");
        String puerto = scan.nextLine();
        if (puerto.length() <= 0) {
            puerto = "5555";
        cliente.ejecutarCliente(ip, Integer.parseInt(puerto));
        cliente.sendDatos();
    }
    
}
