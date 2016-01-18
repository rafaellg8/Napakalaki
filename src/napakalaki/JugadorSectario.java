package napakalaki;

import java.util.ArrayList;

public class JugadorSectario extends Jugador{
    private static int numeroSectarios = 0; 
    private Sectario miCartaSectario;

    public JugadorSectario(Jugador jugador, Sectario carta) {
        super(jugador);
        miCartaSectario = new Sectario(carta.getNombre(), carta.getValorBasico());
        incrementarSectarios();
    }

    public Sectario getMiCartaSectario() {
        return miCartaSectario;
    }
    
    @Override
    public String toString() {
        return super.toString() + "\n\tJugador sectario: " 
                + "\n\t\tBonus basico: " + miCartaSectario.getValorBasico() 
                + " | Bonus especial: " + miCartaSectario.getValorEspecial();
    }

    public static int getNumeroSectarios() {
        return numeroSectarios;
    }
    
    private static void incrementarSectarios() {
        numeroSectarios++;
    }
    
    @Override
    public JugadorSectario convertirme(Sectario cartaSectario){
        return this;
    }
    
    @Override
    public int obtenerNivelCombate() {
        return super.obtenerNivelCombate() + miCartaSectario.getValorEspecial();
    }
    
    @Override
    protected int obtenerNivelContrincante(Monstruo monstruo) {
        return monstruo.getValorEspecial();
    }
    
    public int calcularNivelesAComprar(ArrayList<Tesoro> tesoros){
        int piezasOro = 0;
                
        for(Tesoro t: tesoros)
            piezasOro += 2*t.obtenerPiezasOro();
                           
        return piezasOro / 1000;
    }
    
    @Override
    public boolean puedoConvertirme(){
        return false;
    }
}
