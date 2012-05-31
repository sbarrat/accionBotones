package buscaminas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Componentes Graficos de la aplicación
 * TODO: Ventana de FIN DE JUEGO
 * 		 Agrega datos de anteriores
 * 
 * 		
 *
 */
public class Aplicacion {
	JButton boton[][];
	JFrame frame;
	JPanel centro, sur;
	JLabel lblTiempo, txtTiempo, lblMinas, txtMinas, lblQuedan, txtQuedan;
	JDialog dialogo;
	GridLayout ly;
	ActionListener cambioEstado;
	Minas minas;
	Font fuente;
	Temporizador tiempo;
	int minasMarcadas[];
	int partidas = 0;
	int dimension; // Establecemos por defecto la dimension
	int marcadas = 0;
	Border pulsado;
	
	/**
	 * Inicializamos la aplicacion
	 */
	public Aplicacion() {
		fuente = new Font ("Arial", Font.BOLD , 16);
		minas = new Minas();
		dimension = 10;
		minasMarcadas = new int[dimension];
		minas.setDimension( dimension );
		// Cargamos la ventana principal del juego
		ventanaPrincipal();
	}
	/**
	 * Ventana principal de la aplicacion
	 */
	public void ventanaPrincipal() {
		// Marco principal de la aplicacion
		frame = new JFrame();
		frame.setTitle("Buscaminas 0.1");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setBounds(100, 100, 500, 500);
		// Panel Superior
		centro = new JPanel();
		// Barra de estado
		sur = new JPanel();
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
		// Agregamos paneles al Frame
		frame.add(centro, "Center");
		frame.add(sur,"South");
		// Cargamos el menu del Juego
		menuOpciones();
		// Hacemos visible la ventana de la aplicacion
		frame.setVisible(true);
	}
	/**
	 * Menu del Juego
	 */
	public void menuOpciones() {
		JMenuBar barraMenu = new JMenuBar();
		// Menu de Juego
		JMenu juego = new JMenu( "Juego" );
		juego.add( creaOpcionMenu( "Nuevo", "nuevoJuego" ) );
		juego.add( creaOpcionMenu( "Salir", "salirJuego" ) );
		barraMenu.add( juego );
		// Menu Ayuda
		JMenu ayuda = new JMenu("Ayuda");
		ayuda.add( creaOpcionMenu( "Puntuaciones", "puntuacionJuego" ) );
		ayuda.add( creaOpcionMenu( "Acerca de...", "acercaJuego" ) );
		barraMenu.add( ayuda );
		frame.setJMenuBar( barraMenu );
	}
	/**
	 * Funcion auxiliar para la creacion de opciones de Juego
	 * @param nombre String
	 * @param accion String
	 * @return JMenuItem
	 */
	public JMenuItem creaOpcionMenu( String nombre, String accion ) {
		JMenuItem opcion = new JMenuItem( nombre );
		opcion.setActionCommand( accion );
		opcion.addActionListener( accionesJuego );
		return opcion;
	}
	/**
	 * Iniciamos un nuevo Juego
	 */
	public void nuevoJuego() {
		// Si no es la primera partida elimina los botones puestos y oculta el dialogo
		if ( partidas >= 1 ) {
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					centro.remove(boton[i][j]);
				}
			}
			// Elimina todos los datos del cuadro de dialogo para que no agrege
			dialogo.setVisible(false);
		}
		// Ocultamos el panel central y la barra de estado
		centro.setVisible( false );
		sur.setVisible( false );
		// Inicializamos la etiqueta de Minas Marcadas
		txtMinas.setText(""+ marcadas+"");
		// Creamos el array de botones
		boton = new JButton [dimension][dimension];
		ly = new GridLayout( dimension, dimension );
		centro.setLayout(ly);
		// Colocamos las Minas
		minas.colocaMinas();
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				boton[i][j] = new JButton();
				boton[i][j].setFont( fuente );
				int action = i * dimension + j;
				boton[i][j].setActionCommand(""+action+"");
				boton[i][j].addActionListener( accionesMinas );
				boton[i][j].addMouseListener( botonPulsado );
				centro.add(boton[i][j]);
			}
		}
		DebugJuego( "Partida numero:" + partidas );
		// Conteamos el numero de partidas
		partidas++;
		// Volvemos a poner visible los paneles
		centro.setVisible( true );
		sur.setVisible( true );
		// Iniciamos el temporizador
		tiempo = new Temporizador( txtTiempo );
		tiempo.start();
		
	}
	
	/**
	 * Dialogo de Fin de Juego
	 * @throws ClassNotFoundException 
	 */
	public void finJuego() throws ClassNotFoundException{
		tiempo.parar(true);
		int tiempoPartida = tiempo.segundos;
		try {
			Fichero puntuacion = new Fichero();
			// Devuelve las puntuaciones
			System.out.println(puntuacion.puntuaciones());
			
			puntuacion.agregar(tiempoPartida, "Ruben");
			puntuacion.guardar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ( partidas > 1 ) {
			dialogo.dispose();
		}
		dialogo = new JDialog();
		FlowLayout fl = new FlowLayout();
		dialogo.setLayout(fl);
		dialogo.setBounds(100, 100, 400, 100);
		dialogo.setTitle("Fin de la Partida");
		JLabel texto = new JLabel("¿La partida ha terminado en "+tiempoPartida+", quieres jugar otra o salir?");
		
		dialogo.add(texto);
		
		JButton botonRepetir = new JButton("Repetir");
		botonRepetir.setActionCommand("nuevoJuego");
		botonRepetir.addActionListener(accionesJuego);
		JButton botonSalir = new JButton("Salir");
		botonSalir.setActionCommand("salirJuego");
		botonSalir.addActionListener(accionesJuego);
		
		dialogo.add(botonRepetir);
		dialogo.add(botonSalir);
		dialogo.setModal(true);
		dialogo.setVisible(true);
	}
	
	public static void DebugJuego(String texto) {
		System.out.println( texto );
	}
	/**
	 * Metodo que comprueba la existencia del icono
	 * @param path
	 * @return
	 */
	protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = accionBotones.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon( imgURL );
        } else {
            System.err.println( "El fichero no existe: " + path );
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
				frame.dispose();
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
			/**
			 * Pulsamos una casilla cercana a una mina
			 */
			if ( minas.cercana(fila, columna) > 0 ) {
				if ( minas.cercana(fila, columna) == 1 ) {
					boton[fila][columna].setForeground(new Color(0,120,0) );
				}
				else if ( minas.cercana(fila, columna) == 2 ) {
					boton[fila][columna].setForeground(new Color(0,0,120) );
				} else {
					boton[fila][columna].setForeground(new Color(120,0,0) );
				}
				
				boton[fila][columna].setText(""+minas.cercana(fila,columna)+"");
			} 
			/**
			 * Pulsamos una mina
			 * Mostrar mina y fin juego
			 */
			if ( minas.hayMina(number) ) {
				boton[fila][columna].setText( null );
				boton[fila][columna].setIcon( imagenMina );
				try {
					finJuego();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	};
	
	/**
	 * Detectamos el boton pulsado con el raton
	 */
	MouseListener botonPulsado = new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			ImageIcon bandera = createImageIcon("red_flag.png");
			// Boton derecho
			if ( e.getButton() == 3 ) {
				
				JButton pulsado = (JButton) e.getComponent();
				if (pulsado.getIcon() != null ) {
					pulsado.setIcon(null);
					minasMarcadas[marcadas] = -1;
					marcadas--;
				} else {
					if( marcadas < 10 ) {
						minasMarcadas[marcadas] = Integer.parseInt( pulsado.getActionCommand() );
						pulsado.setIcon( bandera );
						marcadas++;
					}
				}
				if( marcadas == 10 ) {
					// TODO: Comprobar array de marcadas con array de minas
					
				}
				DebugJuego(pulsado.getActionCommand());
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
