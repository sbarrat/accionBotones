import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

import com.sun.xml.internal.ws.api.ha.StickyFeature;
/**
 * Componentes Graficos de la aplicación
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
	int partidas = 0;
	int dimension; // Establecemos por defecto la dimension
	int marcadas = 0;
	Border pulsado;
	
	/**
	 * Inicializamos la aplicacion
	 */
	public Aplicacion() {
		/**
		 * Inicializamos la clase Minas
		 */
		minas = new Minas();
		dimension = 10;
		minas.setDimension( dimension );
		/**
		 * Cargamos la ventana principal
		 */
		ventanaPrincipal();
	}
	/**
	 * Ventana principal de la aplicacion
	 */
	public void ventanaPrincipal() {
		/**
		 * Marco principal de la aplicacion
		 */
		frame = new JFrame();
		frame.setTitle("Buscaminas 0.1");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setBounds(100, 100, 400, 400);
		/**
		 * Panel Superior
		 */
		centro = new JPanel();
		/**
		 * Panel Sur
		 */
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
		/**
		 * Agregamos paneles al Frame
		 */
		frame.add(centro, "Center");
		frame.add(sur,"South");
		/**
		 * Cargamos el menu del Juego
		 */
		menuOpciones();
		/**
		 * Valores por defecto del frame
		 */
		
		frame.setVisible(true);
	}
	
	/**
	 * Menu del Juego
	 */
	public void menuOpciones() {
		JMenuBar barraMenu = new JMenuBar();
		/**
		 * Opcion del Menu Juego
		 */
		JMenu juego = new JMenu( "Juego" );
		juego.add( creaOpcionMenu( "Nuevo", "nuevoJuego" ) );
		juego.add( creaOpcionMenu( "Salir", "salirJuego" ) );
		barraMenu.add( juego );
		/**
		 * Opcion del Menu Acerca de
		 */
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
		opcion.addActionListener(accionesJuego);
		return opcion;
	}
	
	public void nuevoJuego() {
		
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
		ly = new GridLayout( dimension, dimension );
		centro.setLayout(ly);
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
		System.out.println("Partida numero:" + partidas);
		partidas++;
		centro.setVisible( true );
		sur.setVisible( true );
//		tiempo = new Temporizador( this );
//		tiempo.tiempo();
	}
	/**
	 * Dialogo de Fin de Juego
	 */
	public void finJuego(){
		JDialog dialogo = new JDialog();
		FlowLayout fl = new FlowLayout();
		dialogo.setLayout(fl);
		dialogo.setBounds(100, 100, 400, 100);
		dialogo.setTitle("Fin de la Partida");
		JLabel texto = new JLabel("¿La partida ha terminado, quieres jugar otra o salir?");
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
			fuente = new Font ("Arial", Font.BOLD , 16);
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
				boton[fila][columna].setText(""+minas.cercana(fila,columna)+"");
			} 
			/**
			 * Pulsamos una mina
			 * Mostrar mina y fin juego
			 */
			if ( minas.hayMina(number) ) {
				boton[fila][columna].setText( null );
				boton[fila][columna].setIcon( imagenMina );
				finJuego();
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
