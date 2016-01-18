

package napakalaki;

public class Main {
        public static void main(String args[]) {
            Napakalaki juego = Napakalaki.getInstance();
            Vista v = new VentanaPrincipal(juego);
            juego.setVista(v);
            
            v.mostrar(args);            
        }
    
}
