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
import java.util.Calendar;

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
 * TODO:Ventana de FIN DE JUEGO cuando se gane
 * TODO:Mostrar los datos del fichero en puntuaciones
 * FIXME:Arreglar los datos en los bordes
 * 
 * 		
 *
 */
public class Aplicacion {
	String autor = "Paquito";
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
	int tiempoPartida = 0;
	int minasMarcadas[];
	int partidas = 0;
	int dimension; // Establecemos por defecto la dimension
	int marcadas = 0;
	Border pulsado;
	
	/**
	 * Inicializamos la aplicacion
	 */
	public Aplicacion() {
		dialogo = new JDialog();
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
		// Si no es la primera partida elimina los botones puestos
		if ( partidas >= 1 ) {
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					centro.remove(boton[i][j]);
				}
			}
		}
		// Ocultamos el panel central y la barra de estado
		centro.setVisible( false );
		sur.setVisible( false );
		// Inicializamos la etiqueta de Minas Marcadas
		txtMinas.setText(""+ marcadas+"");
		// Creamos el array de botones
		boton = new JButton [dimension][dimension];
		// Creamos la capa que almacenara los botones
		ly = new GridLayout( dimension, dimension );
		// Establecemos la capa
		centro.setLayout(ly);
		// Colocamos las Minas
		minas.colocaMinas();
		// Creamos los botones
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				boton[i][j] = new JButton();
				int action = i * dimension + j;
				boton[i][j].setActionCommand( String.valueOf( action ) );
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
		// Paramos el tiempo
		tiempo.parar(true);
		// Almacenamos el tiempo de partida
		tiempoPartida = tiempo.segundos;
		try {
			Fichero puntuacion = new Fichero();
			// Devuelve las puntuaciones
			DebugJuego( String.valueOf( puntuacion.puntuaciones() ) );
			// Agrega el tiempo y el nombre del Ganador
			// TODO: Pantalla de Ganador
			puntuacion.agregar( tiempoPartida, "Ruben" );
			puntuacion.guardar();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Si es la segunda partida se destruye el dialogo para crear uno nuevo
		finPartida();
	}
	/**
	 * Muestra el cuadro de dialogo de fin de Juego
	 */
	public void finPartida() {
		// Especificamos el numero de etiquetas a usar
		JLabel[] lblDialogo = new JLabel[1];
		// Especificamos el numero de botones a usar
		JButton[] btnDialogo = new JButton[2]; 
		
		lblDialogo[0] = new JLabel("¿La partida ha terminado en " + 
				formatoTiempo( tiempoPartida )+", quieres jugar otra o salir?");
		
		btnDialogo[0] = creaBoton( "Repetir", "nuevoJuego", accionesJuego );
		btnDialogo[1] = creaBoton( "Salir", "salirJuego", accionesJuego );
		
		dialogo = creaDialogo("Fin de la Partida", 500, 100, lblDialogo, btnDialogo);
	}
	/**
	 * Muestra el cuadro de dialogo de las puntuaciones del juego
	 */
	public void puntuacionesJuego() {
		// Especificamos el numero de etiquetas a usar
		JLabel[] lblDialogo = new JLabel[1];
		// Especificamos el numero de botones a usar
		JButton[] btnDialogo = new JButton[1];
		lblDialogo[0] = new JLabel( "Puntuaciones del Juego" );
		btnDialogo[0] = creaBoton( "Cerrar Ventana", "cierraDialogo", accionesJuego );
		dialogo = creaDialogo("Puntuaciones Juego", 500, 400, lblDialogo, btnDialogo);
	}
	/**
	 * Cuadro de Dialogo de Acerca del Juego
	 */
	public void acercaDeJuego(){
		// Especificamos el numero de etiquetas a usar
		JLabel[] lblDialogo = new JLabel[1];
		// Especificamos el numero de botones a usar
		JButton[] btnDialogo = new JButton[1];
		Calendar fecha = Calendar.getInstance();
		lblDialogo[0] = new JLabel( "Buscaminas 0.1 by " + autor + " " + fecha.get( Calendar.YEAR ) );
		btnDialogo[0] = creaBoton( "Cerrar Ventana", "cierraDialogo", accionesJuego ); 
		dialogo = creaDialogo( "Acerca de Buscaminas 0.1", 300, 100, lblDialogo, btnDialogo );
	}
	/**
	 * Cierra el cuadro de Dialogo
	 */
	public void cierraDialogo() {
		dialogo.setVisible( false );
	}
	/**
	 * Clase Auxiliar que establece al boton el nombre de la accion y el listener
	 * @param btn
	 * @param comando
	 * @param listener
	 * @return
	 */
	public JButton creaBoton(String texto, String comando, ActionListener listener ) {
		JButton btn = new JButton(texto);
		btn.setActionCommand( comando );
		btn.addActionListener( listener );
		return btn;
	}
	/**
	 * Clase auxiliar para la creacion de dialogos
	 * @param title
	 * @return
	 */
	public JDialog creaDialogo(String title, int ancho, int alto, JLabel lblDialogo[], JButton btnDialogo[]) {
		dialogo = new JDialog();
		FlowLayout fl = new FlowLayout();
		dialogo.setLayout(fl);
		dialogo.setBounds(100, 110, ancho, alto);
		dialogo.setTitle(title);
		for (int i = 0; i < lblDialogo.length; i++) {
			dialogo.add(lblDialogo[i]);
		}
		for (int i = 0; i < btnDialogo.length; i++) {
			dialogo.add(btnDialogo[i]);
		}
		dialogo.setModal(true);
		dialogo.setVisible(true);
		return dialogo;
	}
	/**
	 * Funcion auxiliar que devuelve el tiempo formateado
	 * @param tiempoPartida
	 * @return
	 */
	public String formatoTiempo( int tiempoPartida ) {
		String tiempoFinal;
		int horas = 0;
		int minutos = 0;
		int segundos = tiempoPartida;
		if ( tiempoPartida > 59 ) {
			minutos = tiempoPartida / 60;
			segundos = tiempoPartida % 60;
		} 
		if ( minutos > 59 ) {
			horas = minutos / 60;
			minutos = minutos % 60;
		}
		tiempoFinal =  ( horas < 10    ) ? "0" + horas    : "" + horas;
		tiempoFinal += ( minutos < 10  ) ? ":0" + minutos  : ":" + minutos;
		tiempoFinal += ( segundos < 10 ) ? ":0" + segundos : ":" + segundos;
		return tiempoFinal;
	}
	/**
	 * Funcion auxiliar para la depuracion
	 * @param texto
	 */
	public static void DebugJuego(String texto) {
		System.out.println( texto );
	}
	/**
	 * Metodo que comprueba la existencia del icono
	 * @param path
	 * @return
	 */
	protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = accionBotones.class.getResource( path );
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
			dialogo.dispose();
			if ( e.getActionCommand().equals( "salirJuego" ) ){
				frame.dispose();
				System.exit(0);
			}
			if ( e.getActionCommand().equals( "nuevoJuego" ) ) {
				nuevoJuego();
			}
			if ( e.getActionCommand().equals( "puntuacionJuego" ) ) {
				puntuacionesJuego();
			}
			if ( e.getActionCommand().equals( "acercaJuego" ) ) {
				acercaDeJuego();
			}
			if ( e.getActionCommand().equals( "cierraDialogo" ) ) {
				cierraDialogo();
			}
		}
	};
	/**
	 * Listener de los clics en las Minas
	 */
	ActionListener accionesMinas = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Creamos el borde Pulsado
			pulsado = BorderFactory.createLoweredBevelBorder();
			// Creamos la fuente negrita
			fuente = new Font ("Arial", Font.BOLD , 16);
			// Creamos el icono de la mina
			ImageIcon imagenMina = createImageIcon("gnome-mines.png");
			// Creamos los colores
			Color rojo = new Color( 120, 0, 0 );
			Color verde = new Color( 0, 120, 0 );
			Color azul = new Color( 0, 0, 120 );
			// Obtenemos los datos de la fila y la columna
			int number = Integer.parseInt( e.getActionCommand() );
			int columna = number % dimension;
			int fila = number / dimension;
			// Establecemos el icono del boton en nulo
			boton[fila][columna].setIcon(null);
			// Establecemos el boton como pulsado
			boton[fila][columna].setBorder( pulsado );
			// Si pulsamos una mina
			if ( minas.hayMina( number ) ) {
				boton[fila][columna].setText( null );
				boton[fila][columna].setIcon( imagenMina );
				try {
					finJuego();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			} else {
				int minaCercana = minas.cercana(fila, columna);
				if ( minaCercana > 0 ) { // Mina Cercada
					if ( minaCercana == 1 ) {
						boton[fila][columna].setForeground( verde );
					}
					else if ( minaCercana == 2 ) {
						boton[fila][columna].setForeground( azul );
					} else {
						boton[fila][columna].setForeground( rojo );
					}
					boton[fila][columna].setText( String.valueOf( minaCercana ) );
					boton[fila][columna].setFont( fuente );
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
			
			ImageIcon bandera = createImageIcon( "red_flag.png" );
			// Clic con el boton derecho en un boton
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
				DebugJuego( pulsado.getActionCommand() );
				txtMinas.setText( String.valueOf( marcadas ) );
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
