package napakalaki;

public class BuenRollo {
    private int gananciaTesoros;
    private int gananciaNiveles;

    public BuenRollo(int gananciaTesoros, int gananciaNiveles) {
        this.gananciaTesoros = gananciaTesoros;
        this.gananciaNiveles = gananciaNiveles;
    }
        
    @Override
    public String toString() {
        return "\tTesoros ganados: " + gananciaTesoros + "\n\tNiveles ganados: " + gananciaNiveles;
    }
    
    public int obtenerTesorosGanados() {
        return gananciaTesoros;
    }

    public int obtenerNivelesGanados() {
        return gananciaNiveles;
    }    
}
