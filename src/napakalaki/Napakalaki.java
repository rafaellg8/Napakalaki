
package napakalaki;

import java.util.ArrayList;
import java.util.Collections;

public class Napakalaki {
    private Jugador jugadorActivo;
    private ArrayList<Jugador> Jugadores = new ArrayList();
    
    private Monstruo monstruoActivo;
    private ArrayList<Monstruo> descarteMonstruos = new ArrayList();
    private ArrayList<Monstruo> mazoMonstruos = new ArrayList();
    private ArrayList<Tesoro> descarteTesoros  = new ArrayList();
    private ArrayList<Tesoro> mazoTesoros = new ArrayList();
    private ArrayList<Sectario> mazoSectarios = new ArrayList();
    
    private Vista vista;
    
    // Establecemos la clase como singleton
    private static final Napakalaki instance = new Napakalaki();
    
    // Constructor privado
    private Napakalaki() { }

    public static Napakalaki getInstance() {
        return instance;
    }
    
    public Vista getVista()
    {
        return vista;
    }
    
    public void comenzarJuego(String[] nombreJugadores) {
        inicializarJuego();
        
        if(nombreJugadores.length < 3 || nombreJugadores.length > 4)
            throw new Error("Numero de jugadores incorrecto");
        
        inicializarJugadores(nombreJugadores); 
        repartirCartas();
        siguienteTurno();        
    }
    
    private void inicializarJuego() {
        // Inicializamos las cartas de tesoro
        mazoTesoros.add (new Tesoro ("¡Sí mi amo!",TipoTesoro.CASCO ,4,7,0) );
        mazoTesoros.add (new Tesoro ("Botas de investigación",TipoTesoro.CALZADO,3,4,600) );
        mazoTesoros.add (new Tesoro ("Capucha de Cthulhu",TipoTesoro.CASCO,3,5,500) );
        mazoTesoros.add (new Tesoro ("A prueba de babas",TipoTesoro.ARMADURA,3,5,400) );
        mazoTesoros.add (new Tesoro ("Botas de lluvia ácida",TipoTesoro.DOSMANOS ,1,1,800) );
        mazoTesoros.add (new Tesoro ("Casco minero",TipoTesoro.CASCO,2,4,400) );
        mazoTesoros.add (new Tesoro ("Zapato deja-amigos",TipoTesoro.CALZADO,0,1,500));
        mazoTesoros.add (new Tesoro ("Fez alópodo" ,TipoTesoro.CASCO,3,5,700));
        mazoTesoros.add (new Tesoro ("Necrotelecom" ,TipoTesoro.CASCO,2,3,300));
        mazoTesoros.add (new Tesoro ("Porra preternatural" ,TipoTesoro.MANO,2,3,200));
        mazoTesoros.add (new Tesoro ("Cuchillo de sushi arcano" ,TipoTesoro.MANO,2,3,300));
        mazoTesoros.add (new Tesoro ("La rebeca metálica" ,TipoTesoro.ARMADURA,2,3,400));
        mazoTesoros.add (new Tesoro ("Linterna a 2 manos" ,TipoTesoro.DOSMANOS,3,6,400));
        mazoTesoros.add (new Tesoro ("Clavo de rail ferroviario" ,TipoTesoro.MANO,3,6,400));
        mazoTesoros.add (new Tesoro ("Shogulador" ,TipoTesoro.DOSMANOS,1,1,600));
        mazoTesoros.add (new Tesoro ("Gaita" ,TipoTesoro.DOSMANOS,4,5,500));
        mazoTesoros.add (new Tesoro ("Necronomicón" ,TipoTesoro.DOSMANOS,5,7,800));
        mazoTesoros.add (new Tesoro ("Camiseta de la UGR" ,TipoTesoro.ARMADURA,1,7,100));
        mazoTesoros.add (new Tesoro ("Necrognomicón",TipoTesoro.MANO,2,4,200));
        mazoTesoros.add (new Tesoro ("Tentáculo de pega",TipoTesoro.CASCO,0,1,200));
        mazoTesoros.add (new Tesoro ("Lanzallamas",TipoTesoro.DOSMANOS,4,8,800));
        mazoTesoros.add (new Tesoro ("Mazo de los antiguos",TipoTesoro.MANO,3,4,200));
        mazoTesoros.add (new Tesoro ("Insecticida",TipoTesoro.MANO,2,3,300));
        mazoTesoros.add (new Tesoro ("Hacha prehistórica",TipoTesoro.MANO,2,5,500));
        mazoTesoros.add (new Tesoro ("El aparato de Pr. Tesla",TipoTesoro.ARMADURA,4,8,900));
        mazoTesoros.add (new Tesoro ("Varita de atizamiento",TipoTesoro.MANO,3,4,400));
        mazoTesoros.add (new Tesoro ("Escopeta de tres cañones",TipoTesoro.DOSMANOS,4,6,700));
        mazoTesoros.add (new Tesoro ("Necrocomicón",TipoTesoro.MANO,1,1,100));
        mazoTesoros.add (new Tesoro ("La fuerza de Mr. T",TipoTesoro.COLLAR,0,0,1000));
        mazoTesoros.add (new Tesoro ("Ametralladora Thompson",TipoTesoro.DOSMANOS,4,8,600));
        mazoTesoros.add (new Tesoro ("Necroplayboycón",TipoTesoro.MANO,3,5,300));
        mazoTesoros.add (new Tesoro ("Garabato místico",TipoTesoro.MANO,2,2,300));
        
        Collections.shuffle(mazoTesoros);
        
        
        
        ArrayList<TipoTesoro> tipoOcultosPerdidos = new ArrayList();
        ArrayList<TipoTesoro> tipoVisiblesPerdidos = new ArrayList();
    

        tipoVisiblesPerdidos.add(TipoTesoro.ARMADURA);
        tipoOcultosPerdidos.add(TipoTesoro.ARMADURA);
        mazoMonstruos.add(new Monstruo(
                "3 Byakhees de bonanza",8,
                new MalRollo("Pierdes tu armadura visible y otra oculta.",
                    1,1,1,false,
                    tipoOcultosPerdidos,
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(3,1) )
        );
        
        
        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.CASCO);
        mazoMonstruos.add(new Monstruo(
                "Chibithulhu",7,
                new MalRollo("Embobados con el lindo primigenio te descartas de tu casco visible.",
                    1,0,1,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(1,1) )
        );
        
        
        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.CALZADO);
        mazoMonstruos.add(new Monstruo(
                "El sopor de Dunwich",2,
                new MalRollo("El primordial bostezo contagioso. Pierdes el calzado visible.",
                    1,0,1,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(1,1) )
        );
        
        
        tipoOcultosPerdidos.clear();
        tipoVisiblesPerdidos.clear();
        tipoOcultosPerdidos.add(TipoTesoro.MANO);
        tipoVisiblesPerdidos.add(TipoTesoro.MANO);
        mazoMonstruos.add(new Monstruo(
                "Ángeles de la noche ibicenca",14,
                new MalRollo("Te atrapan para llevarte de fiesta y te dejan caer en mitad del vuelo. Descarta 1 mano visible y 1 mano oculta.",
                    1,1,1,false,
                    tipoOcultosPerdidos,
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(4,1) )
       );    
        
        mazoMonstruos.add(new Monstruo(
            "El gorrón en el umbral",10, 
            new MalRollo("Pierdes todos tus tesoros visibles.",
                        1,0,6,false, 
                        new ArrayList(),
                        new ArrayList()
                    ),
            new BuenRollo(3,1) ) 
        );
        
        
        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.ARMADURA);
        mazoMonstruos.add(new Monstruo(
                "H.P. Munchcraft",6,
                new MalRollo("Pierdes la armadura visible.",
                    1,0,1,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(2,1) )
        );
        
        
        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.ARMADURA);
        mazoMonstruos.add(new Monstruo(
                "Bichgooth",2,
                new MalRollo("Sientes bichos bajo la ropa. Descarta la armadura visible.",
                    1,0,1,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(1,1) )
        );
        

        mazoMonstruos.add(new Monstruo(
                "El rey de rosa",13,
                new MalRollo("Pierdes 5 niveles y 3 tesoros visibles.",
                    5,0,3,false,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(4,2) )
        );
        

        
        mazoMonstruos.add(new Monstruo(
                "La que redacta en las tinieblas",2,
                new MalRollo("Toses los pulmones y pierdes 2 niveles.",
                    2,0,0,false,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(1,1) )
        );
        

        
        mazoMonstruos.add(new Monstruo(
                "Los hondos",8,
                new MalRollo("Estos monstruos resultan bastante superficiales y te aburren mortalmente. Estás muerto.",
                    1,0,0,true,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(2,1) )
        );
        
  
        
        mazoMonstruos.add(new Monstruo(
                "Semillas Cthulhu",4,
                new MalRollo("Pierdes 2 niveles y 2 tesoros ocultos.",
                    2,2,0,false,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(2,1) )
        );
        
        
        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.MANO);
        mazoMonstruos.add(new Monstruo(
                "Dameargo",1,
                new MalRollo("Te intentas escaquear. Pierdes una mano visible.",
                    1,0,1,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(2,1) )
        );
        

        mazoMonstruos.add(new Monstruo(
                "Pollipólipo volante",3,
                new MalRollo("Da mucho asquito. Pierdes 3 niveles.",
                    3,0,0,false,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(1,1) )
        );
        

        mazoMonstruos.add(new Monstruo(
                "Yskhtihyssg-Goth",12,
                new MalRollo("No le hace gracia que pronuncien mal su nombre. Estás muerto.",
                    1,0,0,true,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(3,1) )
        );
        

        
        mazoMonstruos.add(new Monstruo(
                "Familia feliz",1,
                new MalRollo("La familia te atrapa. Estás muerto.",
                    1,0,0,true,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(4,1) )
        );
        
        
        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.DOSMANOS);
        mazoMonstruos.add(new Monstruo(
                "Roboggoth",8,
                new MalRollo("La quinta directiva primaria te obliga a perder 2 niveles y un tesoro 2 manos visibles.",
                    2,0,1,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(2,1) )
        );
        

        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.CASCO);
        mazoMonstruos.add(new Monstruo(
                "El espía",5,
                new MalRollo("Te asusta en la noche. Pierdes un casco visible.",
                    1,0,1,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(1,1) )
        );
        
     
        mazoMonstruos.add(new Monstruo(
                "El lenguas",20,
                new MalRollo("Menudo susto te llevas. Pierdes 2 niveles y 5 tesoros visibles.",
                    2,0,5,false,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(1,1) )
        );
        

        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.MANO);
        tipoVisiblesPerdidos.add(TipoTesoro.MANO);
        tipoVisiblesPerdidos.add(TipoTesoro.DOSMANOS);
        mazoMonstruos.add(new Monstruo(
                "Bicéfalo",20,
                new MalRollo("Te faltan manos para tanta cabeza. Pierdes 3 niveles y tus tesoros visibles de las manos.",
                    3,0,3,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(1,1) )
        );
        
        
        
        // Monstruos con Sectarios        
        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.MANO);
        mazoMonstruos.add(new Monstruo(
                "El mal indecible impronunciable",10,
                new MalRollo("Pierdes una mano visible.",
                    1,0,1,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(3,1), 
                -2)
        );
        

        mazoMonstruos.add(new Monstruo(
                "Testigos Oculares",6,
                new MalRollo("Pierdes tus tesoros visibles. Jajaja.",
                    1,0,6,false,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(2,1), 
                2)
        );
        
        mazoMonstruos.add(new Monstruo(
                "El gran cthulhu",20,
                new MalRollo("Hoy no es tu día de suerte. Mueres.",
                    1,0,0,true,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(2,5), 
                4)
        );
        
        mazoMonstruos.add(new Monstruo(
                "Serpiente Político",8,
                new MalRollo("Tu gobierno te recorta 2 niveles.",
                    2,0,0,false,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(2,1), 
                -2)
        );
        
        tipoVisiblesPerdidos.clear();
        tipoVisiblesPerdidos.add(TipoTesoro.CASCO);
        tipoVisiblesPerdidos.add(TipoTesoro.ARMADURA);
        mazoMonstruos.add(new Monstruo(
                "Felpuggoth",2,
                new MalRollo("Pierdes tu casco y tu armadura visible. Pierdes 3 tesoros ocultos.",
                    1,3,2,false,
                    new ArrayList(),
                    tipoVisiblesPerdidos
                ),
                new BuenRollo(1,1), 
                5)
        );
        
        mazoMonstruos.add(new Monstruo(
                "Shoggoth",16,
                new MalRollo("Pierdes 2 niveles.",
                    2,0,0,false,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(4,2), 
                -4)
        );
                
        mazoMonstruos.add(new Monstruo(
                "Lolitagooth",2,
                new MalRollo("Pintalabios negro. Pierdes 2 niveles.",
                    2,0,0,false,
                    new ArrayList(),
                    new ArrayList()
                ),
                new BuenRollo(1,1), 
                3)
        );
        
        Collections.shuffle(mazoMonstruos);
        
        
        mazoSectarios.add(new Sectario("Sectario", 1));
        mazoSectarios.add(new Sectario("Sectario", 2));
        mazoSectarios.add(new Sectario("Sectario", 1));
        mazoSectarios.add(new Sectario("Sectario", 2));
        mazoSectarios.add(new Sectario("Sectario", 1));
        mazoSectarios.add(new Sectario("Sectario", 1));
        
        Collections.shuffle(mazoSectarios);
        
    }
    
    private void inicializarJugadores(String[] nombreJugadores){
        for (String jugador: nombreJugadores){
            Jugadores.add(new Jugador(jugador));
        }
    }
    
    private void repartirCartas() {
        for (Jugador jugador: Jugadores)
        {
            int numTesoros;
            
            switch (getVista().getDado("Repartiendo cartas | 1:1,6:3,*:2","Jugador: " + jugador.obtenerNombre())){ 
                    case 1:{
                        numTesoros = 1;
                        break;
                    }
                    case 6:{
                        numTesoros = 3;
                        break;
                    }
                    default:{
                        numTesoros = 2;
                        break;
                    }   
            }
            
            for (int i = 1; i <= numTesoros; i++)
                jugador.robarTesoro(siguienteTesoro());    
        }
    }
    
    private Jugador primerJugador() {
        int primerJugador;        
            primerJugador = getVista().getDado("Primer Jugador","Se repite hasta que salga un índice menor que " + Jugadores.size());
            if(primerJugador>Jugadores.size())primerJugador=primerJugador%Jugadores.size();
            getVista().updateDado(""+primerJugador);
            //jL_dado.setText(Integer.toString(primerJugador));
        return Jugadores.get(primerJugador-1);
    }
    
    private Monstruo siguienteMonstruo() {
        if (mazoMonstruos.isEmpty()) //reciclamos mazo de tesoros
        {
            mazoMonstruos = new ArrayList(descarteMonstruos); 
            descarteMonstruos.clear();
            Collections.shuffle(mazoMonstruos);
        }
        monstruoActivo = mazoMonstruos.get(mazoMonstruos.size()-1);
        descarteMonstruos.add(monstruoActivo);
        mazoMonstruos.remove(monstruoActivo);
        return monstruoActivo;
        
    }
    
    private Tesoro siguienteTesoro() {
        if (mazoTesoros.isEmpty()) //reciclamos mazo de monstruos
        {
            mazoTesoros = new ArrayList(descarteTesoros); 
            descarteTesoros.clear();
            Collections.shuffle(mazoTesoros);
        }
        Tesoro tesoro = mazoTesoros.get(mazoTesoros.size()-1);
        mazoTesoros.remove(tesoro);
        return tesoro;
    }
    
    private Jugador siguienteJugador() {
        return Jugadores.get((Jugadores.indexOf(jugadorActivo) + 1) % Jugadores.size());     
    }
    
    public ResultadoCombate desarrollarCombate() {
        ResultadoCombate resultado = jugadorActivo.combatir(monstruoActivo);
        
        if (resultado == ResultadoCombate.VENCE)
        {
            BuenRollo buenRollo = monstruoActivo.cualEsTuBuenRollo();
            int tesorosGanados = buenRollo.obtenerTesorosGanados();
            
            for (int i = 1; i <= tesorosGanados; i++)
            {
                jugadorActivo.robarTesoro(siguienteTesoro());
            }
            
            if (jugadorActivo.tienesCollar())
            {
                descarteTesoros.add(jugadorActivo.devuelveElCollar());
            }
        }
        
        else if (resultado == ResultadoCombate.PIERDEYMUERE)
        {
            descarteTesoros.addAll(jugadorActivo.dameTodosTusTesoros());
        }
        
        else if( resultado == ResultadoCombate.PIERDE){
            if(jugadorActivo.puedoConvertirme()){
                JugadorSectario jugadorSectario = jugadorActivo.convertirme(siguienteSectario());
                Jugadores.set(Jugadores.indexOf(jugadorActivo), jugadorSectario);
                jugadorActivo = jugadorSectario;
            }
        }
        
        return resultado;
        
    }
    
    public boolean comprarNivelesJugador(ArrayList<Tesoro> listaTesoros) {
        boolean puedo; 
        puedo = jugadorActivo.comprarNiveles(listaTesoros);
        
        if (puedo)
            descarteTesoros.addAll(listaTesoros);
        
        return puedo;
    }
    
    public int siguienteTurno() {
        int fin = 0;
        
        if(jugadorActivo == null){
            jugadorActivo = primerJugador();
            monstruoActivo = siguienteMonstruo();
        }
        else{
            fin = jugadorActivo.puedoPasar();

            if (fin == 0){
                jugadorActivo = siguienteJugador();

                boolean tieneTesoros = jugadorActivo.tienesTesoros();

                if(!tieneTesoros){
                    int numTesoros;
                    switch (getVista().getDado("Numero de Tesoros a repartir | 1:1,6:3,*:2",
                            "Jugador: " + jugadorActivo.obtenerNombre())){
                            case 1:{
                                numTesoros = 1;
                                break;
                            }
                            case 6:{
                                numTesoros = 3;
                                break;
                            }
                            default:{
                                numTesoros = 2;
                                break;
                            }   
                    }
                    for (int i = 1; i <= numTesoros; i++)
                        jugadorActivo.robarTesoro(siguienteTesoro());
                } 

                monstruoActivo = siguienteMonstruo();
            }
        }
        
        return fin;        
    }
    
    public boolean descartarTesoros(ArrayList<Tesoro> tesorosVisibles, 
            ArrayList<Tesoro> tesorosOcultos) 
    {
        boolean cumpleMR;
        
        cumpleMR = jugadorActivo.descartarTesoros(tesorosVisibles, tesorosOcultos);
        
        descarteTesoros.addAll(tesorosVisibles);
        descarteTesoros.addAll(tesorosOcultos);
        
        return cumpleMR;
    }
    
    public Jugador obtenerJugadorActivo() {
        return jugadorActivo;
    }
    public Monstruo obtenerMonstruoActivo() {
        return monstruoActivo;
    }
    
    // Sesioón sectarios
    public Sectario siguienteSectario(){
        Sectario sectario = mazoSectarios.get(mazoSectarios.size()-1);
        mazoSectarios.remove(sectario);
        return sectario;
    }
    
    // Sesion interfaz grafica
    public void setVista(Vista vista) {
        this.vista = vista;
    }
    
    
}
