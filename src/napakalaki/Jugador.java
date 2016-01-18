package napakalaki;

import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private int nivel = 1;
    private static int NIVEL_MINIMO = 1;
    private static int NIVEL_MAXIMO = 10;
    private static int TESOROS_OCULTOS_MAXIMO = 4;
 
    private MalRollo malRolloPendiente = new MalRollo();
    private ArrayList<Tesoro> tesorosOcultos = new ArrayList();
    private ArrayList<Tesoro> tesorosVisibles = new ArrayList();    

    @Override
    public String toString() {
        String f = new String();
        f+=nombre + ", nivel: " + nivel;
        f+="\n\tCartas en mano(ocultas): ";
        for(Tesoro t:tesorosOcultos)
            f+=t.getNombre() + " | ";
                f+="\n\tCartas equipadas(visibles): ";
        for(Tesoro t:tesorosVisibles)
            f+=t.getNombre() + " | ";
        
        if (!malRolloPendiente.esVacio())
            f+= "\nMal rollo pendiente: " + malRolloPendiente.toString();
        f+="\n\tNivel de combate: " + obtenerNivelCombate();
        
        return f;
    }
    
    public Jugador(String nombre) {
        this.nombre = nombre;
    }
    
    public Jugador(Jugador jugador){
        this.nombre = jugador.nombre;
        this.nivel = jugador.nivel;
        this.malRolloPendiente = new MalRollo(jugador.malRolloPendiente);
        tesorosOcultos.addAll(jugador.tesorosOcultos);
        tesorosVisibles.addAll(jugador.tesorosVisibles);
    }
    
    public String obtenerNombre(){
        return nombre;
    }
    
    public int obtenerNivel(){
        return nivel;
    }
    
    public ArrayList<Tesoro> obtenerTesorosOcultos() {
        return tesorosOcultos;
    }
    public ArrayList<Tesoro> obtenerTesorosVisibles() {
        return tesorosVisibles;
    }
    
    public MalRollo obtenerMalRolloPendiente()
    {
        return malRolloPendiente;
    }
    
    public int calcularNiveles(ArrayList<Tesoro> tesoros){
        int piezasOro = 0;
                
        for(Tesoro t: tesoros)
            piezasOro += t.obtenerPiezasOro();
                           
        return piezasOro / 1000;
    }
    
    public void modificarNivel(int nuevoNivel){   
        if (nuevoNivel >= NIVEL_MINIMO && nuevoNivel <= NIVEL_MAXIMO)
            nivel = nuevoNivel;
    }
    
    public int obtenerNivelCombate(){
        int bonus=0;
        
        if(tienesCollar())
            for(Tesoro t: tesorosVisibles)
                bonus += t.getValorEspecial();
        else
            for(Tesoro t: tesorosVisibles)
                bonus += t.getValorBasico();
       
        return bonus + nivel;
    }
    
    public void robarTesoro(Tesoro unTesoro){
        tesorosOcultos.add(unTesoro);
    }
    
    public void equiparTesoros(ArrayList<Tesoro> listaTesoro){
        for (Tesoro tesoro: listaTesoro){
            if (puedoEquipar(tesoro)){
                tesorosVisibles.add(tesoro);
                tesorosOcultos.remove(tesoro);
            }     
        }
    }
    
    private boolean puedoEquipar(Tesoro unTesoro){
        boolean puedo = true;
        
        ArrayList<TipoTesoro> tipos = new ArrayList();
        
        for(Tesoro t: tesorosVisibles)
            tipos.add(t.obtenerTipo());
        
        if(unTesoro.obtenerTipo() != TipoTesoro.MANO && unTesoro.obtenerTipo() != TipoTesoro.DOSMANOS )
            puedo = !tipos.contains(unTesoro.obtenerTipo());
        else if(unTesoro.obtenerTipo() == TipoTesoro.DOSMANOS)
            puedo = !tipos.contains(TipoTesoro.DOSMANOS) && !tipos.contains(TipoTesoro.MANO);
        else
            puedo = !tipos.contains(TipoTesoro.DOSMANOS) &&
                    (tipos.indexOf(TipoTesoro.MANO) == tipos.lastIndexOf(TipoTesoro.MANO));
           
        return puedo;
    }
    
    public boolean tienesTesoros(){
        return !(tesorosOcultos.isEmpty() && tesorosVisibles.isEmpty());
    }
    
    public boolean comprarNiveles(ArrayList<Tesoro> tesoros){
        boolean puedo;
        
        int niveles = calcularNiveles(tesoros);
        puedo = (niveles+nivel) < NIVEL_MAXIMO;
        if(puedo){
            incDecNivel(niveles);
            tesorosOcultos.removeAll(tesoros);
            tesorosVisibles.removeAll(tesoros);
        }
        
        return puedo;
    }
    
    public void incDecNivel(int incDec){
        nivel += incDec;
        if (nivel < NIVEL_MINIMO) 
            nivel =  NIVEL_MINIMO;
    }
    
    public ResultadoCombate combatir(Monstruo monstruoEnJuego){
        ResultadoCombate resultado;
        int nivelC = obtenerNivelCombate();
        int nivelM = obtenerNivelContrincante(monstruoEnJuego);
                
        if (nivelC > nivelM)
        {
            aplicarBuenRollo(monstruoEnJuego.cualEsTuBuenRollo());
            
            if(nivel >= NIVEL_MAXIMO)
                resultado = ResultadoCombate.VENCEYFIN;
            else
                resultado = ResultadoCombate.VENCE;
        }
        else{
            if (Napakalaki.getInstance().getVista().getDado("Tira para huir.", "5-6 huyes") < 5)
            {
                MalRollo malRollo = monstruoEnJuego.cualEsTuMalRollo();
                boolean muerte = malRollo.muerte();
                
                if(muerte){
                    muere();
                    resultado = ResultadoCombate.PIERDEYMUERE;                    
                }else{
                    incDecNivel(-malRollo.obtenerNivelesPerdidos());
                    incluirMalRollo(malRollo);
                    resultado = ResultadoCombate.PIERDE;
                }
                    
            }
            else
                resultado = ResultadoCombate.PIERDEYESCAPA;
        }
        return resultado;
    }
    
    public ArrayList<Tesoro> dameTodosTusTesoros(){
        ArrayList<Tesoro> tesoros = new ArrayList(tesorosOcultos);
        tesoros.addAll(tesorosVisibles);
        
        tesorosOcultos.clear();
        tesorosVisibles.clear();
        
        return tesoros;
    }
    
    public Tesoro devuelveElCollar(){
        for(Tesoro t: tesorosVisibles)
            if (t.obtenerTipo()==TipoTesoro.COLLAR)
            {
                tesorosVisibles.remove(t);
                return t;
            }
        
        
        return null;
    }
    
    public boolean tienesCollar(){
        for(Tesoro t: tesorosVisibles)
            if (t.obtenerTipo() == TipoTesoro.COLLAR)
                return true;
        return false;
    }
    
    public void aplicarBuenRollo(BuenRollo buenRollo){
        nivel += buenRollo.obtenerNivelesGanados();
    }
    
    public void muere(){
        nivel = NIVEL_MINIMO;
    }
        
    public int puedoPasar(){
        if(!malRolloPendiente.esVacio())
            return 1;
        else if (tesorosOcultos.size() > TESOROS_OCULTOS_MAXIMO)
            return tesorosOcultos.size() - TESOROS_OCULTOS_MAXIMO;
        else
            return 0;
    }
    
    public boolean descartarTesoros(ArrayList<Tesoro> tesorosVisDes, 
            ArrayList<Tesoro> tesorosOcuDes){
        
        boolean cumpleMR;
        
        if(tesorosVisDes.isEmpty() && tesorosOcuDes.isEmpty())
            return malRolloPendiente.esVacio();
        
        cumpleMR = cumploMalRollo(tesorosVisDes, tesorosOcuDes);
        
        tesorosVisDes.clear();
        tesorosOcuDes.clear();
        
        return cumpleMR;
    }
        
    public void incluirMalRollo(MalRollo malRollo){
        //Datos que se incluiran en el malRollo del jugador
        int ocuPerdidos = 0;
        int visPerdidos = 0;
        ArrayList<TipoTesoro> tipoOcuPerdidos = new ArrayList();
        ArrayList<TipoTesoro> tipoVisPerdidos = new ArrayList();
        
        //Datos de los que parte el jugador;
        ArrayList<TipoTesoro> tipoOcuJug = new ArrayList();
        ArrayList<TipoTesoro> tipoVisJug = new ArrayList();
        for (Tesoro t: tesorosOcultos)
            tipoOcuJug.add(t.obtenerTipo());
        for (Tesoro t: tesorosVisibles)
            tipoVisJug.add(t.obtenerTipo());
        
        // CARTAS OCULTAS
        //Caso 1: descartamos cartas cualesquiera
        if(malRollo.obtenerTipoOcultosPerdidos().isEmpty()){ 
            ocuPerdidos = Math.min(tesorosOcultos.size(),malRollo.obtenerOcultosPerdidos());
        }
        else{   //Caso 2: descartamos uno por cada tipo
            for (TipoTesoro tipo:malRollo.obtenerTipoOcultosPerdidos())
                if(tipoOcuJug.contains(tipo)){
                    tipoOcuPerdidos.add(tipo);
                    tipoOcuJug.remove(tipo);
                    ocuPerdidos++;
                }
        }
        
        // CARTAS VISIBLES - Analogo
        if(malRollo.obtenerTipoVisiblesPerdidos().isEmpty()){ 
            visPerdidos = Math.min(tesorosVisibles.size(),malRollo.obtenerVisiblesPerdidos());
        }
        else{
            for (TipoTesoro tipo:malRollo.obtenerTipoVisiblesPerdidos())
                if(tipoVisJug.contains(tipo)){
                    tipoVisPerdidos.add(tipo);
                    tipoVisJug.remove(tipo);
                    visPerdidos++;
                }
        }
        
        malRolloPendiente = new MalRollo("MalRollo pendiente",malRollo.obtenerNivelesPerdidos(),
                ocuPerdidos,visPerdidos,malRollo.muerte(),tipoOcuPerdidos,tipoVisPerdidos);

    }
    
    private boolean cumploMalRollo(ArrayList<Tesoro> tesVisibles, 
            ArrayList<Tesoro> tesOcultos){
        
        int visiblesRestantes;
        for(int i=0; i<tesVisibles.size(); i++){
            tesorosVisibles.remove(tesVisibles.get(i));
            
            visiblesRestantes = malRolloPendiente.obtenerVisiblesPerdidos();
            if(visiblesRestantes>0)
                malRolloPendiente.modificarVisiblesPerdidos(visiblesRestantes-1);
            
            if(malRolloPendiente.obtenerTipoVisiblesPerdidos().size()>0)
                malRolloPendiente.obtenerTipoVisiblesPerdidos().remove(tesVisibles.get(i).obtenerTipo());           
        }
        
        int ocultosRestantes;
        for(int i=0; i<tesOcultos.size(); i++){
            tesorosOcultos.remove(tesOcultos.get(i));
            
            ocultosRestantes = malRolloPendiente.obtenerOcultosPerdidos();
            if(ocultosRestantes>0)
                malRolloPendiente.modificarOcultosPerdidos(ocultosRestantes-1);
            
            if(malRolloPendiente.obtenerTipoOcultosPerdidos().size()>0)
                malRolloPendiente.obtenerTipoOcultosPerdidos().remove(tesOcultos.get(i).obtenerTipo());
        }   
        
        return malRolloPendiente.esVacio();
    }

    
    // Sesión sectarios
    public JugadorSectario convertirme(Sectario cartaSectario){
        return new JugadorSectario(this, cartaSectario);
    }
    
    public boolean puedoConvertirme(){        
        if(Napakalaki.getInstance().getVista().getDado("Tira para convertirte.", "6 te conviertes") == 6)
            return true;
        else
            return false;
    }
    
    protected int obtenerNivelContrincante(Monstruo monstruo) {
        return monstruo.getValorBasico();
    }
    
}
