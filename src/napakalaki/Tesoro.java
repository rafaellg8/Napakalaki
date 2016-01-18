package napakalaki;

public class Tesoro implements Carta {
    private String nombre;
    private TipoTesoro tipo;
    private int bonusMinimo;
    private int bonusMaximo;
    private int piezasOro;

    public Tesoro(String nombre, TipoTesoro tipo, int bonusMinimo, 
            int bonusMaximo, int piezasOro) 
    {
        this.nombre = nombre;
        this.tipo = tipo;
        this.bonusMinimo = bonusMinimo;
        this.bonusMaximo = bonusMaximo;
        this.piezasOro = piezasOro;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + tipo.toString() + ") " + "\nBonus mínimo = " + bonusMinimo +
                "\nBonus máximo = " + bonusMaximo + "\nPiezas de Oro = " + piezasOro;
    }

    @Override
    public String getNombre() {
        return nombre;
    }
    
    public int obtenerPiezasOro() {
        return piezasOro;
    }

    @Override
    public int getValorBasico() {
        return bonusMinimo;
    }

    @Override
    public int getValorEspecial() {
        return bonusMaximo;
    }

    public TipoTesoro obtenerTipo() {
        return tipo;
    }    
}
