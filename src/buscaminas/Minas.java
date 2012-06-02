package buscaminas;

import java.util.Enumeration;
import java.util.Hashtable;

public class Minas {

	boolean colocada;
	int mina;
	private int totalMarcadas = 0;
	Hashtable<Integer, Integer> marcadas;
	private int dimension = 10;
	public Minas(){
		
	}
	/**
	 * Establecemos la dimension del numero de minas
	 * @param nuevaDimension
	 */
	public void setDimension( int nuevaDimension  ) {
		dimension = nuevaDimension;
	}
	/**
	 * Obtenemos la dimension
	 * @return
	 */
	private int getDimension() {
		return dimension;
	}
	/**
	 * Colocamos las minas
	 */
	public void colocaMinas() {
		colocada = false;
		marcadas = new Hashtable<Integer, Integer>();
		// Colocamos la primera mina
		marcadas.put( generaAleatorio(), 0 );
		// Colocamos el resto de minas
		for (int i = 1; i < getDimension(); i++) {
			while( !colocada ) {
				mina = generaAleatorio();
				if ( marcadas.containsKey( mina ) ) {
						colocada = false;
				} else { 
					marcadas.put( mina, (int) 0 );
					colocada = true;
				}
			}
			colocada = false;
		}
		// TODO: Borrar
		estadoMinas();
	}
	/**
	 * Genera la posicion aleatoria de la mina
	 * @return
	 */
	public int generaAleatorio() {
		return (int) Math.round( Math.random() * getDimension() * getDimension() );
	}
	/**
	 * Si la mina no esta marcada la establece como marcada, si no la desmarca
	 * @param mina
	 * @return
	 */
	public boolean marcaMina( int mina){
		if ( marcadas.containsKey( mina ) ) {
			if ( marcadas.get( mina ) == 1 ) {
				marcadas.put( mina, (int) 0 );
				setTotalMarcadas( getTotalMarcadas() - 1 );
				//TODO: Borrar
				estadoMinas();
				getTotalMarcadas();
				return false;
			} else {
				marcadas.put(mina, 1);
				setTotalMarcadas( getTotalMarcadas() + 1 );
				// TODO: Borrar
				estadoMinas();
				getTotalMarcadas();
				return true;
			}
		} else {
			return false;
		}
	}
	/**
	 * Devuelve en consola el estado de las minas
	 */
	public void estadoMinas() {
		int key;
		int cont = 1;
		Enumeration<Integer> keys = marcadas.keys();
		while(keys.hasMoreElements()){
			key = keys.nextElement();
			System.out.println( "" + cont + ":" + key + ":" + marcadas.get( key ) );
			cont++;
		}
	}
	/**
	 * Detecta si hay una mina en esa posicion;
	 * @param posicion
	 * @return
	 */
	public boolean hayMina( int posicion ) {
		if ( marcadas.containsKey( posicion ) ){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Comprobar si el pulsado tiene alguan cerca
	 */
	public int cercana( int fila, int columna ){
		//comprobar 8 valores fila, columna
		int cerca = 0;
		int numCol = dimension - 1;
		int numFil = dimension - 1;
		if ( columna > 0 && fila > 0 ) {
			if ( hayMina( ( fila - 1 ) * dimension + ( columna - 1 ) ) ) { // arriba izquierda. No hacer en columna 0 y en fila 0
				cerca++;
			}
		}
		if ( fila > 0 ) {
			if ( hayMina( ( fila - 1 ) * dimension + ( columna     ) ) ) { // arriba. No hacer en fila 0
				cerca++;
			}
		}
		if ( fila > 0 && columna < numCol ) {
			if ( hayMina( ( fila - 1 ) * dimension + ( columna + 1 ) ) ) { // arriba derecha. No hacer en fila 0 y columna final
				cerca++;
			}
		}
		if ( columna > 0 ) {
			if ( hayMina( ( fila     ) * dimension + ( columna - 1 ) ) ) { // medio izquierda. No hacer en columna 0
				cerca++;
			}
		}
		if ( columna < numCol ) {
			if ( hayMina( ( fila     ) * dimension + ( columna + 1 ) ) ) { // medio derecha. No hacer en columna final
				cerca++;
			}
		}
		if ( columna > 0 && fila < numFil ) {
			if ( hayMina( ( fila + 1 ) * dimension + ( columna - 1 ) ) ) { // abajo izquierda. No hacer fila final y columna 0
				cerca++;
			}
		}
		if ( fila < numFil ) {
			if ( hayMina( ( fila + 1 ) * dimension + ( columna     ) ) ) { // abajo. No hacer en ultima fila
				cerca++;
			}
		}
		if ( fila < numFil && columna < numCol ) {
			if ( hayMina( ( fila + 1 ) * dimension + ( columna + 1 ) ) ) { // abajo derecha. No hacer ultima fila y ultima columna
				cerca++;
			}
		}
		return cerca;
		
	}
	int getTotalMarcadas() {
		return totalMarcadas;
	}
	void setTotalMarcadas(int totalMarcadas) {
		if ( this.totalMarcadas >= 0 ) {
			this.totalMarcadas = totalMarcadas;
		}
	}

	
}
