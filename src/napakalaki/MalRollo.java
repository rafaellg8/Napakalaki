package napakalaki;

import java.util.ArrayList;

public class MalRollo {
    private String texto;
    private int nivelesPerdidos;
    private int ocultosPerdidos;
    private int visiblesPerdidos;
    private boolean muerte;
    
    private ArrayList<TipoTesoro> tipoOcultosPerdidos = new ArrayList();
    private ArrayList<TipoTesoro> tipoVisiblesPerdidos = new ArrayList();
    
    public MalRollo(){
        texto = "";
        nivelesPerdidos = ocultosPerdidos = visiblesPerdidos = 0;
        muerte = false;
    }

    public MalRollo(String texto, int nivelesPerdidos, int ocultosPerdidos, 
            int visiblesPerdidos, boolean muerte,
            ArrayList<TipoTesoro> tipoOcultosPerdidos,
            ArrayList<TipoTesoro> tipoVisiblesPerdidos)
    {
        this.texto = texto;
        this.nivelesPerdidos = nivelesPerdidos;
        this.ocultosPerdidos = ocultosPerdidos;
        this.visiblesPerdidos = visiblesPerdidos;
        this.muerte = muerte;
        this.tipoOcultosPerdidos.addAll(tipoOcultosPerdidos);
        this.tipoVisiblesPerdidos.addAll(tipoVisiblesPerdidos);
    }
    
    public MalRollo (MalRollo malRollo)
    {
        this.texto = malRollo.texto;
        this.nivelesPerdidos = malRollo.nivelesPerdidos;
        this.ocultosPerdidos = malRollo.ocultosPerdidos;
        this.visiblesPerdidos = malRollo.visiblesPerdidos;
        this.muerte = malRollo.muerte;
        this.tipoOcultosPerdidos = new ArrayList(malRollo.tipoOcultosPerdidos);
        this.tipoVisiblesPerdidos = new ArrayList(malRollo.tipoVisiblesPerdidos);
    }
    
    @Override
    public String toString() {
        String f = new String();
            f+= "\n\tNiveles perdidos = " + nivelesPerdidos;
            if(ocultosPerdidos!=0){
                f+="\n\tTesoros ocultos perdidos = " + ocultosPerdidos + "  | ";
                for (TipoTesoro t: tipoOcultosPerdidos)
                    f+= t.toString() + " ";
                f+="|";
             }
            if(visiblesPerdidos!=0){
                f+= "\n\tTesoros visibles perdidos = " + visiblesPerdidos + "  | ";
                for (TipoTesoro t: tipoVisiblesPerdidos)
                    f+= t.toString() + " ";
                f+="|";
            }
        if(muerte==true)
            f+= "\n\tMuerte ON";
        
        return f;
    }
    
    public boolean muerte() {
        return muerte;
    }
    
    public String obtenerTexto() {
        return texto;
    }
    
    public int obtenerNivelesPerdidos() {
        return nivelesPerdidos;
    }

    public ArrayList<TipoTesoro> obtenerTipoOcultosPerdidos() {
        return tipoOcultosPerdidos;
    }

    public ArrayList<TipoTesoro> obtenerTipoVisiblesPerdidos() {
        return tipoVisiblesPerdidos;
    }
    
    public int obtenerVisiblesPerdidos(){
        return visiblesPerdidos;
    }
    
    public int obtenerOcultosPerdidos(){
        return ocultosPerdidos;
    }

    public void modificarVisiblesPerdidos(int VisiblesPerdidos) {
        this.visiblesPerdidos = VisiblesPerdidos;
    }

    public void modificarOcultosPerdidos(int ocultosPerdidos) {
        this.ocultosPerdidos = ocultosPerdidos;
    }
       
    public boolean esVacio(){
        return ocultosPerdidos==0 && visiblesPerdidos==0 
                && tipoOcultosPerdidos.isEmpty() && tipoVisiblesPerdidos.isEmpty();
    }
}
