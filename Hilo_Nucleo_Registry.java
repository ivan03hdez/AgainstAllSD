import java.io.*;
import java.net.*;
import java.lang.Exception;
public class Hilo_Nucleo_Registry extends Thread{
    Socket s;
    public Hilo_Nucleo_Registry(Socket registry){
         s=registry;
    }

    public static String mostrarDatos() throws Exception {
        int i = 0;
        String[] datos = new String[5];
        String respuesta = "";
        BufferedReader br = new BufferedReader(new FileReader(new File("ejemplo.txt")));
        String linea = br.readLine();
        while (linea != null) {// Estamos leyendo el fichero para leer climas
            datos[i] = linea;
            respuesta += datos[i];
            linea = br.readLine();
            i++;

        }

        return respuesta;
    }


    public void run(){
        try{
            String respuesta="";
            
                respuesta=leeSocket(s,respuesta);
            System.out.println(respuesta);
            String[] r=respuesta.split(":");
            System.out.println("Recibiendo respuesta del jugador");
            String texto=  r[0];
            String texto2= r[1];
            String texto3= r[2];
            String texto4= r[3];
            String texto5= r[4];

            
            try{
                File archivo = new File("ejemplo.txt");
                FileWriter escribir = new FileWriter(archivo, true);
                BufferedWriter bw=new BufferedWriter(escribir);



                
                bw.write(texto);

                bw.write(":");



                bw.write(texto2);

                bw.write(":");
                bw.write((texto3));
                bw.write(":");

                bw.write((texto4));
                bw.write(":");

                bw.write((texto5));


                bw.newLine();

                bw.close();
            
            }
            catch(Exception e){
                System.out.println("No se ha podido trabajar con el fichero");
                s.close();
                //System.exit(1);
                
            }
           s.close();
        }
        catch(IOException e){
            System.out.println("Jugador se ha desconectado");
        }
        


    }

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