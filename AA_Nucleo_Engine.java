import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.Instant;


public class AA_Nucleo_Engine {
    public static Map mapa;
    
    
    
    public static void main(String args[]) throws Exception {

        String respuesta="";


        if(args.length==6){

            Integer puerto = Integer.parseInt(args[0]);//Estaba 1
          
            Integer nmax=Integer.parseInt(args[1]);
          
            String c1=args[2];
            String c2=args[3];
            String c3=args[4];
            String c4=args[5];
            ServerSocket server = new ServerSocket(puerto);
           
            
            respuesta+= c1+":"+openWeather(c1)+";"+c2+":"+openWeather(c2)+";"+c3+":"+openWeather(c3)+";"+c4+":"+openWeather(c4);
          
            mapa=new Map(respuesta);
            mapa.generarMapa();
            String m=mapa.mostrarMapa();
            System.out.println(m);
            Thread h=new Hilo_Nucleo_Engine_Map(1.0d);
            h.start();
            for (;;) { // bucle que hace una iteraccion por cada jugador que se una

                Socket jugador= server.accept();
                System.out.println("Esperando...");
                Thread hijo=new Hilo_Nucleo_Engine(jugador);
                hijo.start();
                //jugador.close();
            }

        }
        else {
            System.out.println("Error, puertoEscucha maxJ  c1 c2 c3 c4");
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
    public static float openWeather(String c) throws  Exception{
        
        
        String url="http://api.openweathermap.org/data/2.5/weather?q="+c+"&appid=c0f03de2e3425d591ee5aa9104ae21f6";
        boolean existe=false;
        boolean existeTree=false;
        boolean existeN=false;
        float n=0.0f;
       URL uri=new URL(url);
       int t=0;
       InputStreamReader ir=new InputStreamReader(uri.openStream());
       BufferedReader bf=new BufferedReader(ir);
       String r=bf.readLine();
       while(r!=null){
            int mayor=r.indexOf("temp");
            int menor=r.indexOf("feels_like");
            String num=r.substring(mayor+6,menor-2);
            n=Float.parseFloat(num)-(float) 273.15;
               
               
               r=bf.readLine();
       }
           
           
       
       return n;
    }
   
    


}
