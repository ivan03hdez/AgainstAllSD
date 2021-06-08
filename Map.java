import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class Map {
    String[][] mjuego;
    String clima1;
    String clima2;
    String clima3;
    String clima4;
    ArrayList<AA_Player> jugadores=new ArrayList<AA_Player>();
    //Arraylist jugadores (arraylis.add)
    public static final String ANSI_RED = "";

    public static final String ANSI_GREEN = "";
    public static final String RESET = "";
    public static final String ANSI_YELLOW = "";
    public static final String ANSI_BLUE = "";
    public static final String ANSI_YELLOW_BACKGROUND = "";

    //Copiar colores y fondos
    //Habria que añadir array de jugadores para calcular su pos en el mapa y mostrarlos

    public String procesarMovimiento(AA_Player jugador,int filaNueva,int columnaNueva){
        if(jugador.nivel==0)
            return "Has perdido la batalla";
        else if(filaNueva==-100 && columnaNueva==-100)
            return "Sigues vivo";
        mjuego[jugador.fila][jugador.columna]=" ";
        int filaAct=jugador.fila;
        int columnaAct=jugador.columna;
        jugador.fila=actualizaPos(filaNueva, jugador.fila);
        jugador.columna = actualizaPos(columnaNueva, jugador.columna);
        if(cambiarClima(jugador.fila, jugador.columna, filaAct, columnaAct)){
            caclularEClima(jugador);
            if(jugador.nivel<=0)
                return "Has muerto por el efecto clima";
        }


        switch( mjuego[jugador.fila][jugador.columna]){
            case "M":
                mjuego[jugador.fila][jugador.columna]= " ";
                jugador.nivel=0;
                return "Has muerto por una mina";
            case ".":
            jugador.nivel+=1;
                mjuego[jugador.fila][jugador.columna] = String.valueOf(jugador.nivel);
                return "Te has comido un alimento";
            case " ":
                mjuego[jugador.fila][jugador.columna] = String.valueOf(jugador.nivel);
                return "Te has desplazado";
            default:
               return batalla(jugador);

        }
    }
    public  AA_Player buscarJugador(int nivel){
        AA_Player player=null;
        for(AA_Player p:jugadores){
            if(p.nivel==nivel){
                player=p;
                return player;
            }
        }
        return player;
    }

    public AA_Player buscarJugador(String alias) {
        AA_Player player = null;
        for (AA_Player p : jugadores) {
            if (p.alias.equals(alias)) {
                player = p;
                return player;
            }
        }
        return player;
    }
    public String batalla(AA_Player jugador){
         int j2=Integer.parseInt( mjuego[jugador.fila][jugador.columna]);
        String respuesta="Has empatado";
                if(jugador.nivel<j2){
                    mjuego[jugador.fila][jugador.columna] = " ";
                     jugador.nivel  = 0;
                       //el eminar el jugador del array
                       respuesta="Has perdido la batalla";

                }
                else if(jugador.nivel>j2){
                    mjuego[jugador.fila][jugador.columna] = String.valueOf(jugador.nivel);
                    respuesta = "Has ganado la batalla";
                    buscarJugador(j2).nivel=0;

                }
                return respuesta;

    }
    public int actualizaPos(int filaNueva,int filaAct){
        if(salidaMapa(filaNueva)){
            return (mjuego[0].length-1)-filaAct;
        }
        else{
            return  filaNueva;
        }
    }
    public boolean salidaMapa(int filaNueva){

        return filaNueva<0 || (filaNueva>(mjuego[0].length-1));

    }

    /*
    public AA_Player buscarJugador(){

    }
    */
    public Map(String climas){
        mjuego=new String[20][20];
        String[] climasaux=climas.split(";");
        clima1=climasaux[0];//No hay que hacerlo aleatorio??
        clima2 = climasaux[1];
        clima3 = climasaux[2];
        clima4 = climasaux[3];


    }
//3 metodos caclularEClima calcularCuadrante cambiarClima y añadir jugador
    public void insertarJugador(AA_Player j) {//Comprobar


        int nRand = aleatorio(0,20);
        //int nRand2= r.nextInt(10);
        int nRand2=aleatorio(0,20);


        do{

            nRand = aleatorio(0, 20);
            nRand2 = aleatorio(0, 20);
          
        }while(mjuego[nRand][nRand2]!= " ");
        j.fila=nRand;
        j.columna=nRand2;
        caclularEClima(j);
        String j1 = String.valueOf(j.nivel);
        mjuego[nRand][nRand2]=j1;
        jugadores.add(j);
    }
    public void caclularEClima( AA_Player p){
        if(getTemperatura(getClima(calcularCuadrante(p.fila, p.columna)))>=25.0f){
            p.nivel+=p.EC;
        }
        else if(getTemperatura(getClima(calcularCuadrante(p.fila, p.columna)))<=10.0f){
            p.nivel += p.EF;
        }

    }

    public String getClima(int i){
        String s="";
        switch(i){
            case 1:
            s=clima1;
            break;


            case 2:
            s=clima2;
            break;

            case 3:
            s= clima3;
            break;

            case 4:
            s= clima4;
            break;
        }
        return s;
    }
    public float getTemperatura(String clima){
        String[] t=clima.split(":");
        return Float.parseFloat(t[1]);
    }
    public int calcularCuadrante(int fila, int c){
        int cuadrante=0;
        if(fila<10){
            if(c<10){
                cuadrante=1;
            }
            else
                cuadrante=2;
        }
        else{
            if (c < 10) {
                cuadrante = 3;
            }
            else
                cuadrante = 4;
        }
        return cuadrante;
    }
    public boolean cambiarClima(int fNueva,int cNueva,int fAct,int cAct){
       return  (calcularCuadrante(fNueva, cNueva)!=calcularCuadrante(fAct, cAct));
    }




    public int aleatorio(int min,int max){
        Random r=new Random();
        return min + r.nextInt(max);
    }

    public String mostrarMapa() {
        //System.out.println(getClima(1)+getClima(2)+getClima(3)+getClima(4));
        // int mapa[][] = new int[20][20];
        String cadena=getClima(1)+" "+getClima(2)+" "+getClima(3)+" "+getClima(4)+"\n";
        for(int i=0;i<jugadores.size();i++){
            cadena+="Jugador:"+i+" Alias: " + jugadores.get(i).alias + " Nivel: " + jugadores.get(i).nivel + "\n";
        }
        //System.out.print("  ");
        cadena+="  ";
        String color="";
        String background="";
        for (int i = 0; i < mjuego.length; i++) {
            if(i<10){

                //System.out.print(i + "  ");
                cadena+= (i + "  ");
            }
            else{
                //System.out.print(i + " ");
                cadena+= (i + " ");
            }

        }
        //System.out.println("");
        cadena+=("\n");
        for (int i = 0; i < mjuego.length; i++) {
            if(i<10){
                //System.out.print(" ");
                cadena+=(" ");
            }
            //System.out.print(i);
            cadena+=(i);

            for (int j = 0; j < mjuego[i].length; j++) {

                switch(mjuego[i][j]){
                    case ".":
                        color=ANSI_GREEN;
                        break;
                    case "M":
                        color = ANSI_RED;
                        break;
                    case " ":
                        color="";
                        break;

                    default:
                        color= ANSI_BLUE;
                        //background=ANSI_YELLOW_BACKGROUND;

                }
                //System.out.print(color+background+ mjuego[i][j] + RESET+ "  ");
                cadena+=(color+background+ mjuego[i][j] + RESET+ "  ");

            }
            //System.out.println("");
            cadena+=("\n");
        }
        return cadena;

    }
    public void generarMapa(){//Generar minas alimentos y espacios
        Random r = new Random();
        for (int i = 0; i < mjuego.length; i++) {
            for (int j = 0; j < mjuego[i].length; j++) {
                int nRand=r.nextInt(100);//Son 100?
                if(nRand<=7){
                    mjuego[i][j] = "M";
                }
                else{
                    if(nRand>7 && nRand<=40){
                        mjuego[i][j]=".";
                    }
                    else{
                        if(nRand>40){
                            mjuego[i][j]=" ";
                        }
                    }
                }
            }
        }
    }
}
