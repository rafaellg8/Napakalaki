package napakalaki;

public class Sectario implements Carta {
    private String nombre;
    private int gananciaNivel;

    public Sectario(String nombre, int gananciaNivel) {
        this.nombre = nombre;
        this.gananciaNivel = gananciaNivel;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getValorBasico() {
        return gananciaNivel;
    }

    @Override
    public int getValorEspecial() {
        return getValorBasico()*JugadorSectario.getNumeroSectarios();
    }
}
