import java.io.*;
import java.net.*;
import java.lang.Exception;
public class Hilo_Nucleo_Engine extends Thread {
    private Socket skCliente;
    private AA_Player jug;
    public Hilo_Nucleo_Engine(Socket p_nucleo){
        this.skCliente=p_nucleo;


    }


    public String procesarMovimiento(String mov) {
        String respuesta="";
        switch (mov) {
        case "N":
           respuesta= AA_Nucleo_Engine.mapa.procesarMovimiento(jug, jug.fila - 1, jug.columna);

            break;
        case "S":
            respuesta=AA_Nucleo_Engine.mapa.procesarMovimiento(jug, jug.fila + 1, jug.columna);
            break;
        case "E":
            respuesta=AA_Nucleo_Engine.mapa.procesarMovimiento(jug, jug.fila, jug.columna + 1);
            break;
        case "W":
            respuesta =AA_Nucleo_Engine.mapa.procesarMovimiento(jug, jug.fila, jug.columna - 1);
            break;
        case "NW":
            respuesta =AA_Nucleo_Engine.mapa.procesarMovimiento(jug, jug.fila - 1, jug.columna - 1);
            break;
        case "NE":
            respuesta =AA_Nucleo_Engine.mapa.procesarMovimiento(jug, jug.fila - 1, jug.columna + 1);
            break;
        case "SE":
            respuesta =AA_Nucleo_Engine.mapa.procesarMovimiento(jug, jug.fila + 1, jug.columna + 1);
            break;
        case "SW":
            respuesta =AA_Nucleo_Engine.mapa.procesarMovimiento(jug, jug.fila + 1, jug.columna - 1);
            break;
        default:
            respuesta =AA_Nucleo_Engine.mapa.procesarMovimiento(jug, -100, -100);
        }
        return respuesta;
    }
    public static String leeSocket(Socket p_sk, String p_Datos) throws Exception {
        try {
            InputStream aux = p_sk.getInputStream();
            DataInputStream flujo = new DataInputStream(aux);
            p_Datos = new String();
            p_Datos = flujo.readUTF();
        } catch (Exception e) {
            //System.out.println("Error: " + e.toString());
            throw e;
        }
        return p_Datos;
    }

    /*
     * Escribe dato en el socket cliente. Devuelve numero de bytes escritos, o -1 si
     * hay error.
     */
    public void escribeSocket(Socket p_sk, String p_Datos) throws Exception{
        try {
            OutputStream aux = p_sk.getOutputStream();
            DataOutputStream flujo = new DataOutputStream(aux);
            flujo.writeUTF(p_Datos);
        } catch (Exception e) {
            //System.out.println("Error: " + e.toString());
            throw e;
        }
        return;
    }
    public boolean procesarDatos (String alias, String pwd){
        boolean login=false;
        
        try{
            BufferedReader br = new BufferedReader(new FileReader("ejemplo.txt"));

            for(String linea=br.readLine();linea!=null && !linea.equals("");linea=br.readLine()){
                String[] datos=linea.split(":");
                if(datos[0].equals(alias) && datos[1].equals(pwd)){
                    login =true;
                    jug=new AA_Player(linea);
                    break;
                }
            }
        }
        catch(Exception e){
            System.out.println("Error, no se procesan bien los datos");
        }
        return login;
    }
    public void run(){
        String datos="";
        String mJugador = "";
        String m="";
        String[] s;
        try{

                //resultado=this.leeSocket(skCliente, resultado);//Info del cliente
                //HACER MOVIMIENTO EN EL MAPA
                datos=leeSocket(skCliente, datos);
                s=datos.split(":");
                

               if( procesarDatos(s[0],s[1])){
                //Clase anyadir jugador en Map, .add
                //NO es infinito,ANYADIR FOR

                    this.escribeSocket(skCliente, "Login correcto");
                    if(AA_Nucleo_Engine.mapa.buscarJugador(jug.alias)==null){
                        AA_Nucleo_Engine.mapa.insertarJugador(jug);
                        
                    }
                    while(jug.nivel>0){
                        m = AA_Nucleo_Engine.mapa.mostrarMapa();
                        System.out.println(m);
                        try{
                            this.escribeSocket(skCliente, m);
                            mJugador = leeSocket(skCliente, mJugador);
                            
                            this.escribeSocket(skCliente, procesarMovimiento(mJugador));
                        }
                        catch(Exception e){
                            System.out.println("Error, no se ha podido conectar con el player");
                            break;
                        }

                    }
                    //escribeSocket(skCliente, "Has muerto");

               }
               else{
                   this.escribeSocket(skCliente, "Login incorrecto");
               }
               skCliente.close();
        }

        catch(Exception e){
            System.out.println("El jugador se ha desconectado");
        }
    }
}


