import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
public class AA_Player{
	//public static Map mapa;
	public int fila;
	public int columna;
	public int nivel;
	public int EC;
	public int EF;
	public String alias;
	public String contra;
	private static Scanner keyboard = new Scanner(System.in);
	public AA_Player(String respuestaR){

		String[] arrayS=respuestaR.split(":");
		System.out.println(respuestaR);
		alias=arrayS[0];
		contra = arrayS[1];
		nivel = Integer.parseInt(arrayS[2]);
		EF = Integer.parseInt(arrayS[3]);
		EC = Integer.parseInt(arrayS[4]);
		//ver Formato de respuesta y asignar a las variables de clase

	}
	public static String crearPerfil()throws Exception{
		keyboard.nextLine();
		System.out.println("Introduzca su alias");
		String texto = "";
		String texto2 = "";
		int texto3 = 0;
		int texto4 = 0;
		int texto5 = 0;

		texto = keyboard.nextLine();

		System.out.println("Introduzca su pasword");

		texto2 = keyboard.nextLine();
		System.out.println("La entrada recibida es :\"" + texto2 + "\"");

		do {
			System.out.println("Introduzca su Nivel");

			texto3 = keyboard.nextInt();

		} while (texto3 < 0);
		System.out.println("La entrada recibida es :\"" + texto3 + "\"");

		do {
			System.out.println("Introduzca su Efecto ante el frio");

			texto4 = keyboard.nextInt();

		} while (texto4 < -10 || texto4 > 10);
		System.out.println("La entrada recibida es :\"" + texto4 + "\"");


		do {
			System.out.println("Introduzca su Efecto ante el calor");

			texto5 = keyboard.nextInt();

		} while (texto5 < -10 || texto5 > 10);
		System.out.println("La entrada recibida es :\"" + texto5 + "\"");
		return (texto +":"+ texto2 +":"+ Integer.toString(texto3)+":"+ Integer.toString(texto4)+":"+ Integer
				.toString(texto5));
	}
        public static Socket crearConexionRegistry(String s1,int i){
            try{
                return new Socket(s1, i);//hostR
               }
               catch(Exception e){
                       System.out.println("No se ha podido conectar con el servidor Registry, no podrás crear un perfil");
               }
            return null;
        }

 public static void main(String args[]) throws Exception{
    //puerto en el que escucha nuestro servidor clima 1ARGUMENTO
	//ip del ordenador donde esta el servidor al que me quiero conectar (String IPServidor)
	String texto = "";
	String texto2 = "";
	String respuesta="";
	String respuesta2="";
	String respuesta3 = "";
	String isAlive="";
        String mov="";
	int o=0;
	if(args.length==4){
		try{

	 		Integer puertoE = Integer.parseInt(args[1]);
			 Integer puertoR = Integer.parseInt(args[3]);

			String hostE=args[0];
			String hostR=args[2];
			Socket clienteE= new Socket(hostE,puertoE);//hostE
			Socket clienteR=null;
			


			while(o!=3){
                            System.out.println("Elija una de estas 2 opciones:" + "\n" + "1.Registrarse" +  "\n"
                                                    + "2.Jugar"+"\n"+"3.Salir");
                            do{
                                    o = keyboard.nextInt();
                            }while(o!=1 && o!=2 && o!=3);
                            if(o==1){
                                clienteR=crearConexionRegistry(hostR,puertoR);
                                //System.out.println("clienteR:"+clienteR);
                                    if(clienteR!=null){
                                        respuesta2=AA_Player.crearPerfil();
                                        //System.out.println(respuesta2);
                                        AA_Player.escribeSocket(clienteR, respuesta2);
                                        System.out.println("Datos Mandados");
                                    }
                            }
                            else if(o==2){
                                
                                //Duration tiempoT=Duration.between(start, end);
                                //Duration.between(start, end);
                                String alias=keyboard.nextLine();
                                System.out.println("Introduce tu alias");
                                 alias=keyboard.nextLine();
                                System.out.println("Introduce tu pasword");
                                String pwd=keyboard.nextLine();

                                escribeSocket(clienteE, alias+":"+pwd);
                                respuesta=AA_Player.leeSocket(clienteE, respuesta);
                                if(respuesta.equals("Login correcto")){
                                        System.out.println("Escrito4");
                                        //for (;;) {

                                        while (!(respuesta3.equals("Has muerto"))) {
                                                respuesta3=AA_Player.leeSocket(clienteE, respuesta3);
                                                System.out.println(respuesta3);
                                                mov=gestion();//Hay que hacer un escribeSocket
                                                escribeSocket(clienteE,mov);
                                                isAlive=AA_Player.leeSocket(clienteE, isAlive);
                                                if(!isAlive(isAlive)){
                                                        System.out.println(isAlive);
                                                        break;
                                                }
                                        }

                                }
                                else
                                   System.out.println("No te has podido conectar, alias o pasword incorrectos");

                            }
                            else
                                break;
                            
                        }
                   
			if (clienteR != null)
				clienteR.close();
			clienteE.close();
		}
		catch(Exception e){
			System.out.println("Error,no se ha podido conectar con el servidor engine");
		}

	}
        else{
            System.out.println("Error,se requieren IPEngine,pEngine,IPRegistry,pRegistry");
        }

}
 
 public static String gestion()throws Exception{
     final Reader rd=new InputStreamReader(System.in);
     //definir tiempo
     
     Instant start,end;
     
     
      
      double tmaxEspera=0.0d;
       System.out.println("Planea tu movimiento(N,S,E,W,NE,NW,SW,SE");
       //keyboard.nextLine();
       while(true){
           start=Instant.now();
           //System.out.println("Antes del .ready");
            if(rd.ready())
                return keyboard.nextLine();
            
            else if(tmaxEspera>2.0d)
                return "";
            
           
           
            end=Instant.now();
            Duration tiempoT=Duration.between(start, end);
            tmaxEspera+=tiempoT.getNano()/Math.pow(10, 9);
            
       }
       
 }
 
public static boolean isAlive(String respuesta){
	return !(respuesta.equals("Has muerto por una mina")||respuesta.equals("Has perdido la batalla")|| respuesta.equals(
			"Has muerto por el efecto clima")|| respuesta.equals("Has ganado la batalla"));


}

 public static String leeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			InputStream aux = p_sk.getInputStream();
			DataInputStream flujo = new DataInputStream( aux );
			p_Datos = new String();
			p_Datos = flujo.readUTF();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
      return p_Datos;
	}

	/*
	* Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
	* o -1 si hay error.
	*/
	public static void  escribeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			OutputStream aux = p_sk.getOutputStream();
			DataOutputStream flujo= new DataOutputStream( aux );
			flujo.writeUTF(p_Datos);
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
		return;
	}


}
