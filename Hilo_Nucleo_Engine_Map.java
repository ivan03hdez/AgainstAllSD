

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hilo_Nucleo_Engine_Map extends Thread {
    private final double Tescritura;
    public Hilo_Nucleo_Engine_Map(double t){
        Tescritura=t;
    }
    @Override
    public void run(){
        try {
            gestionUsuarios();
        } catch (Exception ex) {
            System.out.println("No se ha podido trabajar con el fichero");
        }
    }
     public static void escribirMapa() throws Exception{
        FileWriter f=new FileWriter("mapa.txt");
        BufferedWriter bw=new BufferedWriter(f);
	bw.write(AA_Nucleo_Engine.mapa.mostrarMapa());
        bw.close();
        
    }
     public static void escribirEstadoUsuarios() throws Exception{
         
        FileWriter f=new FileWriter("estadoUsuarios.txt");
        BufferedWriter bw=new BufferedWriter(f);
	
        for(AA_Player p:AA_Nucleo_Engine.mapa.jugadores){
            String isAlive="Vivo";
            if(p.nivel<=0)
                isAlive="Muerto";
            else
                isAlive="Vivo";
            String nivelInicial=procesarDatos(p.alias,p.contra);
            bw.write(p.alias +":"+ p.contra + ":"+ nivelInicial +":"+ p.nivel +":"+ p.EF +":"+ p.EC +":"+ isAlive+"\n" );
        }
        bw.close();
     }
      public static String procesarDatos (String alias, String pwd){
        String login="";
        
        try{
            BufferedReader br = new BufferedReader(new FileReader("ejemplo.txt"));

            for(String linea=br.readLine();linea!=null && !linea.equals("");linea=br.readLine()){
                String[] datos=linea.split(":");
                if(datos[0].equals(alias) && datos[1].equals(pwd)){
                    login=datos[2];
                   
                    break;
                }
            }
        }
        catch(Exception e){
            System.out.println("Error, no se ha podido coger el nivel inicial del jugador");
        }
        return login;
    }
    public void gestionUsuarios() throws Exception{
         Instant start,end;
        double tmaxEspera=0.0d;
       
        while(true){
           start=Instant.now();
           if(tmaxEspera>this.Tescritura){
                escribirMapa();
                escribirEstadoUsuarios();
                tmaxEspera=0;
           } 
            end=Instant.now();
            Duration tiempoT=Duration.between(start, end);
            tmaxEspera+=tiempoT.getNano()/Math.pow(10, 9);
            
       }
    }
}
