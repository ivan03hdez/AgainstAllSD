import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class AA_Nucleo_Registry {
 public static void main(String args[]) throws Exception{
    //puerto en el que escucha nuestro servidor clima

    String respuesta="";
	if(args.length==1){

    	Integer puerto= Integer.parseInt(args[0]);
        ServerSocket server=new ServerSocket(puerto);
        for (;;) { // bucle que hace una iteraccion por cada jugador que se una
            Socket SocketJugador = server.accept();
            System.out.println("Enviando al jugador...");
            //respuesta = AA_Nucleo_Registry.leeSocket(SocketJugador, respuesta);
           //escribeSocket(SocketJugador,mostrarDatos());
            Thread hijo=new Hilo_Nucleo_Registry(SocketJugador);
            hijo.start();
            
        }
    }

	else{
		System.out.println("Error, Introduzca el puerto correcto.");
	}

 	}
/*
    public static void datos(String n,String datos){
		 File f;
				FileWriter w;
				BufferedWriter bw;
				PrintWriter wr;

				try{
					f=new File(n);
					w=new FileWriter(f);
					bw=new BufferedWriter(w);
					wr= new PrintWriter(bw);

					wr.write(datos+"\n");
					wr.append("\n segunda");
					wr.append("\nfinal");

					wr.close();
					bw.close();

				} catch (Exception e){
					JOptionPane.showMessageDialog(null, "ha sucedido un erro"+e);
				}
	 }
*/
    public static String leeSocket(Socket p_sk, String p_Datos) {
        try {
            InputStream aux = p_sk.getInputStream();
            DataInputStream flujo = new DataInputStream(aux);
            p_Datos = new String();
            p_Datos = flujo.readUTF();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return p_Datos;
    }

    /*
     * Escribe dato en el socket cliente. Devuelve numero de bytes escritos, o -1 si
     * hay error.
     */
    public static void escribeSocket(Socket p_sk, String p_Datos) {
        try {
            OutputStream aux = p_sk.getOutputStream();
            DataOutputStream flujo = new DataOutputStream(aux);
            flujo.writeUTF(p_Datos);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return;
    }
}


