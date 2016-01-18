package napakalaki;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class VentanaPrincipal extends javax.swing.JFrame implements Vista{
    private Napakalaki juego;
    private JD_dado dado;
    private String[] nombresJugadores;
    
    private Monstruo monstruoEnJuego;
    private MonstruoGrafico imagenMonstruo;
    private Jugador jugadorActivo;
    private ArrayList<TesoroGrafico> tesorosVisiblesAlimpiar = new ArrayList();
    private ArrayList<Tesoro> tesorosVisiblesSeleccionados = new ArrayList();
    private ArrayList<TesoroGrafico> tesorosOcultosAlimpiar = new ArrayList();
    private ArrayList<Tesoro> tesorosOcultosSeleccionados = new ArrayList();
    private ArrayList<Boolean> jugadoresVivos = new ArrayList();
    
    private class TesoroGrafico extends JPanel {
        protected Tesoro tesoro; // asociación con el tesoro que representa 
        private JTextArea jL_nombre = new JTextArea();
        private JLabel jL_bonus = new JLabel();
        private JLabel jL_piezasOro = new JLabel();
        private JLabel jL_tipo = new JLabel();
        
        /* Imagen correspondiente al tesoro */
        private Image imagenTesoro;
        

        TesoroGrafico (Tesoro unTesoro) {
            tesoro = unTesoro;
            try{
                imagenTesoro = (new ImageIcon(getClass().getClassLoader().getResource("resources/Tesoros/"+tesoro.getNombre()+".png"))).getImage();
            } catch (Exception e){
                JOptionPane.showMessageDialog(this,"La imagen resources/Tesoros/"+tesoro.getNombre()+".png no está disponible.\n"
                        + "Contactar con los programadores para subsanar el fallo: " + e.getMessage(), "Error!!!", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            
            
            this.setPreferredSize(new java.awt.Dimension(120, 150));
            this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            this.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK));
                       
            jL_nombre.setFocusable(false);
            jL_nombre.setEditable(false);
            jL_nombre.setBackground(Color.WHITE);
            jL_nombre.setColumns(15);
            jL_nombre.setFont(new java.awt.Font("Dialog", 3, 11)); // NOI18N
            jL_nombre.setOpaque(false);
            jL_nombre.setLineWrap(true);
            jL_nombre.setRows(3);
            jL_nombre.setText(tesoro.getNombre());
            jL_nombre.setToolTipText("");
            jL_nombre.setWrapStyleWord(true);
            jL_nombre.setAutoscrolls(false);
            
            jL_bonus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jL_bonus.setText("+" + tesoro.getValorBasico() + "/+" + tesoro.getValorEspecial());
            jL_piezasOro.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            jL_piezasOro.setText(tesoro.obtenerPiezasOro() + " oro");
            jL_tipo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            jL_tipo.setText("" + tesoro.obtenerTipo());
            
            this.setOpaque(false);
            
            this.add(jL_nombre,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 110, -1));
            this.add(jL_tipo,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 120, 90, -1));
            this.add(jL_piezasOro,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 132, 90, -1));
            this.add(jL_bonus,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 132, 90, -1));

        }
         /* Sobreescritura del método paint del JPanel para añadir la imagen del tesoro */
        @Override
        public void paint(Graphics g)
        {
            if (imagenTesoro != null)
                g.drawImage(imagenTesoro,0,0,null);
            super.paint(g);
        }
    }
    
    private class TesoroGraficoVisible extends TesoroGrafico {
        TesoroGraficoVisible (Tesoro unTesoro) {
            super(unTesoro);
            addMouseListener(new java.awt.event.MouseAdapter() 
            {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (tesorosVisiblesSeleccionados.contains(TesoroGraficoVisible.this.tesoro))
                    {
                        tesorosVisiblesSeleccionados.remove(TesoroGraficoVisible.this.tesoro);
                        TesoroGraficoVisible.this.setBorder(BorderFactory.createLineBorder(Color.black));
                        TesoroGraficoVisible.this.setEnabled(true);
                    }
                    else
                    {
                        tesorosVisiblesSeleccionados.add(TesoroGraficoVisible.this.tesoro);
                        TesoroGraficoVisible.this.setBorder(BorderFactory.createMatteBorder(
                                    5, 5, 5, 5, Color.red));
                        TesoroGraficoVisible.this.setEnabled(false);
                    }
                }
            });
         }
    }
    
    private class TesoroGraficoOculto extends TesoroGrafico {
        TesoroGraficoOculto (Tesoro unTesoro) {
            super(unTesoro);
            addMouseListener(new java.awt.event.MouseAdapter() 
            {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {                 
                    if(tesorosOcultosSeleccionados.contains(TesoroGraficoOculto.this.tesoro))
                    {
                        tesorosOcultosSeleccionados.remove(TesoroGraficoOculto.this.tesoro);
                        TesoroGraficoOculto.this.setBorder(BorderFactory.createLineBorder(Color.black));
                        TesoroGraficoOculto.this.setEnabled(true);
                    }
                    else
                    {
                        tesorosOcultosSeleccionados.add(TesoroGraficoOculto.this.tesoro);
                        TesoroGraficoOculto.this.setBorder(BorderFactory.createMatteBorder(
                                    5, 5, 5, 5, Color.red));
                        TesoroGraficoOculto.this.setEnabled(false);
                    }
                }
            });
         }
    }
    
    private class MonstruoGrafico extends JPanel{
        private Monstruo monstruo;
        private Image imagenMonstruo;
        
        MonstruoGrafico (Monstruo monstruo){
            this.monstruo = monstruo;
            try {
                imagenMonstruo = (new ImageIcon(getClass().getClassLoader().getResource("resources/Monstruos/"+this.monstruo.getNombre()+".png"))).getImage();
            } catch (Exception e){
                JOptionPane.showMessageDialog(this,"La imagen resources/Monstruos/"+this.monstruo.getNombre()+".png no está disponible.\n"
                        + "Contactar con los programadores para subsanar el fallo: " + e.getMessage(), "Error!!!", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            this.setPreferredSize(new java.awt.Dimension(120, 150));
            this.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK));
            this.setOpaque(false);
        }
 
        /* Sobreescritura del método paint de JPanel para dibujar la imagen del monstruo */
        @Override
        public void paint(Graphics g)
        {
            if (imagenMonstruo != null)
                g.drawImage(imagenMonstruo,0,0,null);
            super.paint(g);
        }
                
        
    }

    public VentanaPrincipal(Napakalaki unJuego) {
        juego = unJuego;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jP_monstruos = new javax.swing.JPanel();
        jL_nombreMonstruo = new javax.swing.JLabel();
        jL_nivelesGanados = new javax.swing.JLabel();
        jL_tesorosGanados = new javax.swing.JLabel();
        jL_nivel = new javax.swing.JLabel();
        jL_nivelContraSectarios = new javax.swing.JLabel();
        jL_nivelesPerdidos = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jL_malRollo = new javax.swing.JTextArea();
        jP_imgMonstruo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jL_resultadoCombate = new javax.swing.JLabel();
        jL_nivel_resultado = new javax.swing.JLabel();
        jP_jugadores = new javax.swing.JPanel();
        jL_nombreJugador = new javax.swing.JLabel();
        jP_tesorosVisibles = new javax.swing.JPanel();
        jP_tesorosOcultos = new javax.swing.JPanel();
        jL_esSectario = new javax.swing.JLabel();
        jL_nivelCombate = new javax.swing.JLabel();
        jL_bonusSectario = new javax.swing.JLabel();
        jL_malRolloPendiente = new javax.swing.JLabel();
        jP_malRolloPendiente = new javax.swing.JPanel();
        jL_ocultosPerdidos = new javax.swing.JLabel();
        jL_tipoOcultosPerdidos = new javax.swing.JLabel();
        jL_visiblesPerdidos = new javax.swing.JLabel();
        jL_tipoVisiblesPerdidos = new javax.swing.JLabel();
        jL_nivelBasico = new javax.swing.JLabel();
        jL_excesoCartas = new javax.swing.JLabel();
        jB_equiparse = new javax.swing.JButton();
        jB_comprarNivel = new javax.swing.JButton();
        jB_descartarseTesoros = new javax.swing.JButton();
        jB_combatir = new javax.swing.JButton();
        jB_siguiente = new javax.swing.JButton();
        EXIT = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Napakalaki");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        jP_monstruos.setBorder(javax.swing.BorderFactory.createTitledBorder("Territorio del Terrible Monstruo"));
        jP_monstruos.setPreferredSize(new java.awt.Dimension(904, 180));

        jL_nombreMonstruo.setText("Nombre Monstruo");

        jL_nivelesGanados.setText("Niveles Ganados");

        jL_tesorosGanados.setText("Tesoros Ganados");

        jL_nivel.setText("Nivel");

        jL_nivelContraSectarios.setText("Nivel contra Sectarios");

        jL_nivelesPerdidos.setText("Niveles Perdidos");

        jL_malRollo.setEditable(false);
        jL_malRollo.setBackground(new java.awt.Color(238, 238, 238));
        jL_malRollo.setColumns(20);
        jL_malRollo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jL_malRollo.setLineWrap(true);
        jL_malRollo.setRows(4);
        jL_malRollo.setText("Mal Rollo del Monstruo");
        jL_malRollo.setToolTipText("");
        jL_malRollo.setWrapStyleWord(true);
        jL_malRollo.setAutoscrolls(false);
        jScrollPane1.setViewportView(jL_malRollo);

        jP_imgMonstruo.setMaximumSize(new java.awt.Dimension(120, 155));
        jP_imgMonstruo.setMinimumSize(new java.awt.Dimension(120, 150));
        jP_imgMonstruo.setPreferredSize(new java.awt.Dimension(120, 155));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultado del Combate"));

        jL_resultadoCombate.setForeground(new java.awt.Color(57, 250, 19));
        jL_resultadoCombate.setText("Resultado del Combate");
        jL_resultadoCombate.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(160, Short.MAX_VALUE)
                .addComponent(jL_resultadoCombate, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jL_resultadoCombate)
                .addContainerGap())
        );

        javax.swing.GroupLayout jP_monstruosLayout = new javax.swing.GroupLayout(jP_monstruos);
        jP_monstruos.setLayout(jP_monstruosLayout);
        jP_monstruosLayout.setHorizontalGroup(
            jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_monstruosLayout.createSequentialGroup()
                .addGroup(jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_monstruosLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jL_nombreMonstruo, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(jL_nivelesGanados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jL_tesorosGanados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(75, 75, 75)
                        .addGroup(jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jL_nivelContraSectarios, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(jL_nivelesPerdidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jP_monstruosLayout.createSequentialGroup()
                                .addComponent(jL_nivel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jL_nivel_resultado, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(44, 44, 44)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP_monstruosLayout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jP_imgMonstruo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jP_monstruosLayout.setVerticalGroup(
            jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_monstruosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jP_monstruosLayout.createSequentialGroup()
                        .addGroup(jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jL_nivel_resultado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jL_nombreMonstruo)
                                .addComponent(jL_nivel)))
                        .addGap(7, 7, 7)
                        .addGroup(jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jL_nivelesGanados)
                            .addComponent(jL_nivelContraSectarios))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jP_monstruosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jL_nivelesPerdidos)
                            .addComponent(jL_tesorosGanados))))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_monstruosLayout.createSequentialGroup()
                .addComponent(jP_imgMonstruo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jP_jugadores.setBorder(javax.swing.BorderFactory.createTitledBorder("Parcelita de los débiles jugadores"));

        jL_nombreJugador.setText("Nombre Jugador");

        jP_tesorosVisibles.setBorder(javax.swing.BorderFactory.createTitledBorder("Equipo"));

        jP_tesorosOcultos.setBorder(javax.swing.BorderFactory.createTitledBorder("Cartas Ocultas"));

        jL_esSectario.setText("Humano");

        jL_nivelCombate.setText("Nivel de Combate");

        jL_bonusSectario.setText("Bonus Sectario");

        jP_malRolloPendiente.setBorder(javax.swing.BorderFactory.createTitledBorder("MalRollo Pendiente"));

        jL_ocultosPerdidos.setText("Nº Ocultos a perder:");

        jL_tipoOcultosPerdidos.setText("Tipo:");

        jL_visiblesPerdidos.setText("Nº Visibles a perder");

        jL_tipoVisiblesPerdidos.setText("Tipo:");

        javax.swing.GroupLayout jP_malRolloPendienteLayout = new javax.swing.GroupLayout(jP_malRolloPendiente);
        jP_malRolloPendiente.setLayout(jP_malRolloPendienteLayout);
        jP_malRolloPendienteLayout.setHorizontalGroup(
            jP_malRolloPendienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_malRolloPendienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jP_malRolloPendienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jL_ocultosPerdidos, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jP_malRolloPendienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jL_tipoOcultosPerdidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jL_visiblesPerdidos, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                        .addComponent(jL_tipoVisiblesPerdidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP_malRolloPendienteLayout.setVerticalGroup(
            jP_malRolloPendienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_malRolloPendienteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jL_ocultosPerdidos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jL_tipoOcultosPerdidos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jL_visiblesPerdidos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jL_tipoVisiblesPerdidos)
                .addContainerGap())
        );

        jL_nivelBasico.setText("Nivel Básico");

        jL_excesoCartas.setText("");

        javax.swing.GroupLayout jP_jugadoresLayout = new javax.swing.GroupLayout(jP_jugadores);
        jP_jugadores.setLayout(jP_jugadoresLayout);
        jP_jugadoresLayout.setHorizontalGroup(
            jP_jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_jugadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jP_jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jP_tesorosVisibles, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                    .addComponent(jP_tesorosOcultos, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE))
                .addGroup(jP_jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_jugadoresLayout.createSequentialGroup()
                        .addGroup(jP_jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_jugadoresLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jL_malRolloPendiente))
                            .addGroup(jP_jugadoresLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jP_jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jL_nivelCombate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jL_esSectario, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jL_nivelBasico, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jL_excesoCartas, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jL_bonusSectario, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jL_nombreJugador, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 12, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jP_jugadoresLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jP_malRolloPendiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(24, Short.MAX_VALUE))))
        );
        jP_jugadoresLayout.setVerticalGroup(
            jP_jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_jugadoresLayout.createSequentialGroup()
                .addGroup(jP_jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_jugadoresLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jL_nombreJugador)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jL_nivelBasico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jL_nivelCombate)
                        .addGap(12, 12, 12)
                        .addComponent(jL_malRolloPendiente)
                        .addGap(18, 18, 18)
                        .addComponent(jL_esSectario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jL_bonusSectario)
                        .addGap(18, 18, 18)
                        .addComponent(jL_excesoCartas))
                    .addGroup(jP_jugadoresLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jP_tesorosVisibles, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addGroup(jP_jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jP_tesorosOcultos, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_malRolloPendiente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jB_equiparse.setText("Equiparse");
        jB_equiparse.setEnabled(false);
        jB_equiparse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_equiparseActionPerformed(evt);
            }
        });

        jB_comprarNivel.setText("Comprar Nivel");
        jB_comprarNivel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_comprarNivelActionPerformed(evt);
            }
        });

        jB_descartarseTesoros.setText("Descartarse Tesoros");
        jB_descartarseTesoros.setEnabled(false);
        jB_descartarseTesoros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_descartarseTesorosActionPerformed(evt);
            }
        });

        jB_combatir.setText("¡COMBATIR!");
        jB_combatir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_combatirActionPerformed(evt);
            }
        });

        jB_siguiente.setText("Siguiente");
        jB_siguiente.setEnabled(false);
        jB_siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_siguienteActionPerformed(evt);
            }
        });

        EXIT.setText("EXIT");
        EXIT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EXITActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jB_equiparse)
                        .addGap(18, 18, 18)
                        .addComponent(jB_comprarNivel)
                        .addGap(18, 18, 18)
                        .addComponent(jB_descartarseTesoros)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jB_combatir)
                        .addGap(110, 110, 110)
                        .addComponent(EXIT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jB_siguiente))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jP_jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jP_monstruos, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_monstruos, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jP_jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jB_siguiente)
                    .addComponent(jB_combatir)
                    .addComponent(jB_descartarseTesoros)
                    .addComponent(jB_comprarNivel)
                    .addComponent(jB_equiparse)
                    .addComponent(EXIT))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jB_equiparseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_equiparseActionPerformed
        juego.obtenerJugadorActivo().equiparTesoros(tesorosOcultosSeleccionados);
        actualizarJugador();
    }//GEN-LAST:event_jB_equiparseActionPerformed

    private void jB_descartarseTesorosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_descartarseTesorosActionPerformed
        juego.descartarTesoros(tesorosVisiblesSeleccionados, tesorosOcultosSeleccionados);
        actualizarJugador();

        if (jugadorActivo.obtenerMalRolloPendiente().esVacio())
            jB_equiparse.setEnabled(true);
    }//GEN-LAST:event_jB_descartarseTesorosActionPerformed

    private void jB_comprarNivelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_comprarNivelActionPerformed
        ArrayList<Tesoro> cartasaVender = tesorosOcultosSeleccionados;
        cartasaVender.addAll(tesorosVisiblesSeleccionados);
        juego.comprarNivelesJugador(cartasaVender);
        actualizarJugador();
    }//GEN-LAST:event_jB_comprarNivelActionPerformed

    private void jB_combatirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_combatirActionPerformed
        actualizarMonstruo();
        ResultadoCombate resultado = juego.desarrollarCombate();
        jL_resultadoCombate.setText(""+resultado);
        
        
        jugadoresVivos.set( buscaJugador(jugadorActivo.obtenerNombre()), true);
        if (resultado == ResultadoCombate.VENCEYFIN)
        {
            JOptionPane.showMessageDialog(this,"El jugador " + jugadorActivo.obtenerNombre() + " ha ganado la partida!!!", "Ganador!!!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        else if(resultado == ResultadoCombate.PIERDEYMUERE)
            jugadoresVivos.set( buscaJugador(jugadorActivo.obtenerNombre()), false);
        
        boolean cumplioMalRollo = jugadorActivo.obtenerMalRolloPendiente().esVacio();
        if(cumplioMalRollo == true)
        {
            jB_comprarNivel.setEnabled(true);
            jB_equiparse.setEnabled(true);
        }
        
        jB_siguiente.setEnabled(true);
        jB_descartarseTesoros.setEnabled(true);
        jB_comprarNivel.setEnabled(false);
        jB_combatir.setEnabled(false);
        actualizarJugador();
    }//GEN-LAST:event_jB_combatirActionPerformed

    private void jB_siguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_siguienteActionPerformed
            int fin = juego.siguienteTurno();
            jL_nivel_resultado.setText("");
            if(fin == 0)
            {
                actualizarJugador();
                
                if(jugadoresVivos.get( buscaJugador(jugadorActivo.obtenerNombre())) == false){
                    jB_equiparse.setEnabled(true);
                    jugadoresVivos.set( buscaJugador(jugadorActivo.obtenerNombre()), true);
                }
                else
                    jB_equiparse.setEnabled(false);
                
                jB_comprarNivel.setEnabled(true);
                jB_descartarseTesoros.setEnabled(false);
                jB_siguiente.setEnabled(false);
                jB_combatir.setEnabled(true);
                jL_malRolloPendiente.setText("");
                jL_excesoCartas.setText("");
                
                limpiaMonstruo();
            }
            else if (fin > 0)
            {
                jL_excesoCartas.setText("¡Cumple el Mal Rollo!: " + fin);
                actualizarJugador();
            }
    }//GEN-LAST:event_jB_siguienteActionPerformed

    private void EXITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EXITActionPerformed
        // TODO add your handling code here:
        
        System.exit(0);
    }//GEN-LAST:event_EXITActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new VentanaPrincipal().setVisible(true);
//            }
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton EXIT;
    private javax.swing.JButton jB_combatir;
    private javax.swing.JButton jB_comprarNivel;
    private javax.swing.JButton jB_descartarseTesoros;
    private javax.swing.JButton jB_equiparse;
    private javax.swing.JButton jB_siguiente;
    private javax.swing.JLabel jL_bonusSectario;
    private javax.swing.JLabel jL_esSectario;
    private javax.swing.JLabel jL_excesoCartas;
    private javax.swing.JTextArea jL_malRollo;
    private javax.swing.JLabel jL_malRolloPendiente;
    private javax.swing.JLabel jL_nivel;
    private javax.swing.JLabel jL_nivelBasico;
    private javax.swing.JLabel jL_nivelCombate;
    private javax.swing.JLabel jL_nivelContraSectarios;
    private javax.swing.JLabel jL_nivel_resultado;
    private javax.swing.JLabel jL_nivelesGanados;
    private javax.swing.JLabel jL_nivelesPerdidos;
    private javax.swing.JLabel jL_nombreJugador;
    private javax.swing.JLabel jL_nombreMonstruo;
    private javax.swing.JLabel jL_ocultosPerdidos;
    private javax.swing.JLabel jL_resultadoCombate;
    private javax.swing.JLabel jL_tesorosGanados;
    private javax.swing.JLabel jL_tipoOcultosPerdidos;
    private javax.swing.JLabel jL_tipoVisiblesPerdidos;
    private javax.swing.JLabel jL_visiblesPerdidos;
    private javax.swing.JPanel jP_imgMonstruo;
    private javax.swing.JPanel jP_jugadores;
    private javax.swing.JPanel jP_malRolloPendiente;
    private javax.swing.JPanel jP_monstruos;
    private javax.swing.JPanel jP_tesorosOcultos;
    private javax.swing.JPanel jP_tesorosVisibles;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void  mostrar(String[] args) {
        JD_nombresJugadores dialogoNombres;
        dado = new JD_dado (this,true);
        dialogoNombres = new JD_nombresJugadores (this,true);
        nombresJugadores = dialogoNombres.getNombres();
        
        for (int i = 0; i < nombresJugadores.length; i++)
            jugadoresVivos.add(false);
        //jugadoresVivos.set(nombresJugadores.length-1 , true);
        
        juego.comenzarJuego(nombresJugadores);
        actualizarJugador();
        limpiaMonstruo();

        this.setVisible(true);
    }

    @Override
    public int getDado(String s1, String s2) {
        return dado.getValor(s1, s2);
    }
    public void updateDado(String s1){
        dado.update(s1);
    }
    
    private void actualizarMonstruo()
    {
        if (imagenMonstruo != null)
            jP_imgMonstruo.remove(imagenMonstruo);
        monstruoEnJuego = juego.obtenerMonstruoActivo();
        imagenMonstruo = new MonstruoGrafico(monstruoEnJuego);
        jL_nombreMonstruo.setText (monstruoEnJuego.getNombre());
        jL_nivelesGanados.setText("Niveles Ganados: " + monstruoEnJuego.cualEsTuBuenRollo().obtenerNivelesGanados());
        jL_tesorosGanados.setText("Tesoros Ganados: " + monstruoEnJuego.cualEsTuBuenRollo().obtenerTesorosGanados());
        jL_nivel_resultado.setText(""+monstruoEnJuego.getValorBasico());
        jL_nivel.setText("Nivel : ");
        if(monstruoEnJuego.getValorBasico()>jugadorActivo.obtenerNivelCombate()){
            jL_nivel_resultado.setForeground(Color.red);
        }else{
            if(monstruoEnJuego.getValorBasico()<jugadorActivo.obtenerNivelCombate()){
                jL_nivel_resultado.setForeground(Color.green);
            }else{
                jL_nivel_resultado.setForeground(Color.black);
            }
        }
        jL_nivelContraSectarios.setForeground(Color.orange);
        jL_nivelContraSectarios.setText("Nivel contra Sectarios: " + monstruoEnJuego.getValorEspecial());
        
        jL_nivelesPerdidos.setText("Niveles perdidos: " + monstruoEnJuego.cualEsTuMalRollo().obtenerNivelesPerdidos());
        jL_resultadoCombate.setText("");
        jL_malRollo.setText (monstruoEnJuego.cualEsTuMalRollo().obtenerTexto());
        
        jP_imgMonstruo.add(imagenMonstruo);
        
        pack();
        repaint();

    }
    
    private void actualizarJugador()
    {        
        TesoroGrafico unTesoroGrafico; 
        jugadorActivo = juego.obtenerJugadorActivo(); 

        jL_nombreJugador.setText("" + jugadorActivo.obtenerNombre());
        jL_nivelBasico.setText("Nivel Básico: " + jugadorActivo.obtenerNivel());
        jL_nivelCombate.setText("Nivel de combate: " + jugadorActivo.obtenerNivelCombate());


        if (jugadorActivo instanceof JugadorSectario){
            jL_esSectario.setText("Sectario");
            jL_bonusSectario.setText("+" + ((JugadorSectario)jugadorActivo).getMiCartaSectario().getValorBasico() 
                                      + " por cada Sectario en juego -> " + JugadorSectario.getNumeroSectarios());
        }
        else{
            jL_esSectario.setText("Humano");
            jL_bonusSectario.setText("");
        }

        for (TesoroGrafico tg : tesorosVisiblesAlimpiar)
            jP_tesorosVisibles.remove(tg);
        tesorosVisiblesAlimpiar.clear();

        for (Tesoro t : jugadorActivo.obtenerTesorosVisibles()) 
        {
            unTesoroGrafico = new TesoroGraficoVisible(t);
            jP_tesorosVisibles.add (unTesoroGrafico);
            tesorosVisiblesAlimpiar.add(unTesoroGrafico);
        }
        for (TesoroGrafico tg : tesorosOcultosAlimpiar)
            jP_tesorosOcultos.remove(tg);
        tesorosOcultosAlimpiar.clear();

        for (Tesoro t : jugadorActivo.obtenerTesorosOcultos()) 
        {
            unTesoroGrafico = new TesoroGraficoOculto(t);
            jP_tesorosOcultos.add(unTesoroGrafico);
            tesorosOcultosAlimpiar.add(unTesoroGrafico);
        }

        tesorosVisiblesSeleccionados.clear();
        tesorosOcultosSeleccionados.clear();

        
        // Actualizamos malRolloPendiente
        if(jugadorActivo.obtenerMalRolloPendiente().obtenerOcultosPerdidos()==0 
                && jugadorActivo.obtenerMalRolloPendiente().obtenerTipoOcultosPerdidos().isEmpty())
        {
            jL_ocultosPerdidos.setText("");
            jL_tipoOcultosPerdidos.setText("");
        }
        else
        {
            jL_ocultosPerdidos.setText("Nº ocultos perdidos: " + jugadorActivo.obtenerMalRolloPendiente().obtenerOcultosPerdidos());
           
            String f = new String();
            f += "Tipos: ";
            for (TipoTesoro t: jugadorActivo.obtenerMalRolloPendiente().obtenerTipoOcultosPerdidos())
                f += t + " | ";
            
            jL_tipoOcultosPerdidos.setText(f);
        }
        
        if(jugadorActivo.obtenerMalRolloPendiente().obtenerVisiblesPerdidos()==0 
                && jugadorActivo.obtenerMalRolloPendiente().obtenerTipoVisiblesPerdidos().isEmpty())
        {
            jL_visiblesPerdidos.setText(""); 
            jL_tipoVisiblesPerdidos.setText("");

        }
        else
        {
            jL_visiblesPerdidos.setText("Nº visibles perdidos: " + jugadorActivo.obtenerMalRolloPendiente().obtenerVisiblesPerdidos());
            
            String f = "Tipos: ";
            for (TipoTesoro t: jugadorActivo.obtenerMalRolloPendiente().obtenerTipoVisiblesPerdidos())
                f += t + " | ";
            
            jL_tipoVisiblesPerdidos.setText(f);
        }

        repaint();
        pack();
    }
    
    private void limpiaMonstruo(){
        //liampiamos info del monstruo
        if (imagenMonstruo != null)
            jP_imgMonstruo.remove(imagenMonstruo);
        jL_nombreMonstruo.setText ("");
        jL_nivelesGanados.setText("");
        jL_tesorosGanados.setText("");
        jL_nivel.setText("");
        jL_nivelContraSectarios.setText("");
        jL_nivelesPerdidos.setText("");
        jL_resultadoCombate.setText("");
        jL_malRollo.setText ("");
    }
    
    private int buscaJugador(String nombre){
        for (int i = 0; i < nombresJugadores.length; i++) {
            if(nombresJugadores[i].equals(nombre))
                return i;
        }
        return -1;
    }
}
