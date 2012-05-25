import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class accionBotones implements ActionListener {

	/**
	 * @param args
	 */
	JButton boton[][];
	JFrame fr;
	JPanel centro, sur;
	JLabel lblTiempo, txtTiempo, lblMinas, txtMinas, lblQuedan, txtQuedan;
	GridLayout ly;
	long minas[];
	ActionListener cambioEstado;
	Temporizador tiempo;
	int partidas = 0;
	int dimension;
	int marcadas = 0;
	Border pulsado;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		accionBotones accion = new accionBotones();
		
	}
	/**
	 * Constructor de la clase
	 * TODO: Implementar paneles
	 */
	public accionBotones(){
		dimension = 10;
		fr = new JFrame();
		centro = new JPanel();
		sur = new JPanel();
		/**
		 * Etiquetas del panel sur: panel de estado
		 */
		lblTiempo = new JLabel("Tiempo transcurrido:");
		txtTiempo = new JLabel();
		lblMinas = new JLabel("Minas Marcadas:");
		txtMinas = new JLabel();
		lblQuedan = new JLabel("Minas Faltan:");
		txtQuedan = new JLabel();
		sur.add(lblTiempo);
		sur.add(txtTiempo);
		sur.add(lblMinas);
		sur.add(txtMinas);
		fr.add(centro, "Center");
		fr.add(sur,"South");
		menuOpciones();
		fr.setTitle("Buscaminas 0.1");
		fr.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		fr.setBounds(100, 100, 400, 400);
		fr.setVisible(true);
	}
	/**
	 * Se inicia un nuevo Juego
	 */
	public void nuevoJuego() {
		
		int style = Font.BOLD;
		Font font = new Font ("Arial", style , 16);
		/**
		 * Si no es la primera partida elimina todos los botones puestos
		 * en la en la pantalla
		 */
		if ( partidas >= 1 ) {
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					centro.remove(boton[i][j]);
				}
			}
		}
		centro.setVisible( false );
		sur.setVisible( false );
		
		txtMinas.setText(""+ marcadas+"");
		boton = new JButton [dimension][dimension];
		minas = new long[dimension];
		ly = new GridLayout( dimension, dimension );
		centro.setLayout(ly);
		colocaMinas();
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				boton[i][j] = new JButton();
				boton[i][j].setFont(font);
				int action = i * dimension + j;
				boton[i][j].setActionCommand(""+action+"");
				boton[i][j].addActionListener( accionesMinas );
				boton[i][j].addMouseListener( botonPulsado );
				centro.add(boton[i][j]);
			}
		}
		System.out.println("Partida numero:" + partidas);
		partidas++;
		centro.setVisible( true );
		sur.setVisible( true );
//		tiempo = new Temporizador( this );
//		tiempo.tiempo();
	}
	
	/**
	 * Menu del Juego
	 */
	public void menuOpciones() {
		JMenuBar barraMenu = new JMenuBar();
		JMenu juego = new JMenu("Juego");
		JMenuItem nuevo = new JMenuItem("Nuevo");
		nuevo.setActionCommand("nuevoJuego");
		nuevo.addActionListener(accionesJuego);
		JMenuItem salir = new JMenuItem("Salir");
		salir.setActionCommand("salirJuego");
		salir.addActionListener(accionesJuego);
		juego.add(nuevo);
		juego.add(salir);
		barraMenu.add(juego);
		fr.setJMenuBar(barraMenu);
	}
	
	public void colocaMinas() {
		
		boolean colocada = false;
		long mina = 0;
		// Colocamos la primera mina
		minas[0] = Math.round( Math.random()*dimension*dimension );
		for (int i = 1; i < dimension; i++) {
			while( !colocada ) {
				mina = Math.round( Math.random()*dimension*dimension );
				for (int j = 0; j < dimension; j++) {
					if( minas[j] == mina) {
						colocada = false;
						break;
					}
					colocada = true;
				}
			}
			minas[i] = mina;
			colocada = false;
		}
		for (int i = 0; i < minas.length; i++) {
			System.out.println(minas[i]);
		}
	}
	public void debugCercano( int fila, int columna ) {
		int poscheck = fila * dimension + columna;
		System.out.println("Fila:"+fila+";Columna:"+columna+";Posicion:"+poscheck);
	}
	/**
	 * Comprobar si el pulsado tiene alguan cerca
	 */
	public int cercana( int fila, int columna ){
		//comprobar 8 valores fila, columna
		int cerca = 0;
		int numCol = dimension - 1;
		int numFil = dimension - 1;
		// posicion izquierda arriba
		System.out.println("Fila " + fila + ", Columna " + columna );
		if ( fila > 0 && columna > 0 ) {
			if ( hayMina( ( fila - 1 ) * dimension + ( columna - 1 ) ) ) {
				cerca++;
			}
		}
		// posicion arriba
		if ( fila > 0 ) {
			if ( hayMina( ( fila ) * dimension + ( columna - 1 ) ) ) {
				cerca++;
			}
		}
		// posicion arriba derecha
		if ( fila > 0 && columna < numCol ) {
			if ( hayMina( ( fila + 1 ) * dimension + ( columna - 1 ) ) ) {
				cerca++;
			}
		}
		// posicion centro izquierda
		if ( columna > 0 ) {
			if ( hayMina( ( fila - 1 ) * dimension + ( columna ) ) ) {
				cerca++;
			}
		}
		// posicion centro derecha
		if ( columna < numCol ) {
			if ( hayMina( ( fila + 1 ) * dimension + ( columna ) ) ) {
				cerca++;
			}
		}
		// posicion izquierda abajo
		if ( fila < numFil && columna > 0 ) {
			if ( hayMina( ( fila - 1 ) * dimension + ( columna + 1 ) ) ) {
				cerca++;
			}
		}
		// posicion abajo
		if ( fila < numFil ) {
			if ( hayMina( ( fila ) * dimension + ( columna + 1 ) ) ) {
				cerca++;
			}
		}
		// posicion abajo derecha
		if ( fila < numFil && columna < numCol ) {
			if ( hayMina( ( fila + 1 ) * dimension + ( columna + 1 ) ) ) {
				cerca++;
			}
		}
		
		return cerca;
		
	}
	/**
	 * Detecta si hay una mina en esa posicion;
	 * @param posicion
	 * @return
	 */
	public boolean hayMina( int posicion ) {
		boolean minaDetectada = false;
		for (int i = 0; i < minas.length; i++) {
			if ( minas[i] == posicion ) {
				minaDetectada = true;
				break;
			}
		}
		return minaDetectada;
	}
	
	/**
	 * Metodo que comprueba la existencia del icono
	 * @param path
	 * @return
	 */
	protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = accionBotones.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("El fichero no existe: " + path);
            return null;
        }
    }
	
	/**
	 * Listener por defecto
	 */
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Listener de las acciones del Juego
	 */
	ActionListener accionesJuego = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if ( e.getActionCommand().equals("salirJuego")){
				fr.dispose();
				System.exit(0);
			}
			if ( e.getActionCommand().equals("nuevoJuego")) {
				nuevoJuego();
			}
		}
	};
	/**
	 * Listener de los clics en las Minas
	 */
	ActionListener accionesMinas = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			pulsado = BorderFactory.createLoweredBevelBorder();
			int number = Integer.parseInt( e.getActionCommand() );
			int columna = number % dimension;
			int fila = number / dimension;
			ImageIcon imagenMina = createImageIcon("gnome-mines.png");
			boton[fila][columna].setIcon(null);
			boton[fila][columna].setBorder( pulsado );
			if ( cercana(fila, columna) > 0 ) {
				boton[fila][columna].setText(""+cercana(fila,columna)+"");
			} 
			if ( hayMina(number) ) {
				boton[fila][columna].setText( null );
				boton[fila][columna].setIcon( imagenMina );
			}
		}
	};
	
	MouseListener botonPulsado = new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			System.out.println( e.getButton() );
			ImageIcon bandera = createImageIcon("red_flag.png");
			// Boton derecho
			if ( e.getButton() == 3 ) {
				
				JButton pulsado = (JButton) e.getComponent();
				if (pulsado.getIcon() != null ) {
					pulsado.setIcon(null);
					marcadas--;
				} else {
					pulsado.setIcon( bandera );
					marcadas++;
				}
				txtMinas.setText(""+marcadas+"");
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	
	

}
