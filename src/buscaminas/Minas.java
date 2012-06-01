package buscaminas;
public class Minas {

	boolean colocada;
	long mina;
	long minas[];
	private int dimension = 10;
	public Minas(){
		
	}
	/**
	 * Establecemos la dimension del numero de minas
	 * @param nuevaDimension
	 */
	public void setDimension( int nuevaDimension  ) {
		dimension = nuevaDimension;
		minas = new long[dimension];
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
		mina = 0;
		// Colocamos la primera mina
		minas[0] = generaAleatorio();
		// Colocamos el resto de minas
		for (int i = 1; i < getDimension(); i++) {
			while( !colocada ) {
				mina = generaAleatorio();
				for (int j = 0; j < getDimension(); j++) {
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
			Aplicacion.DebugJuego(String.valueOf(minas[i]));
		}
	}
	public long generaAleatorio() {
		return Math.round( Math.random() * getDimension() * getDimension() );
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
	 * Comprobar si el pulsado tiene alguan cerca
	 */
	public int cercana( int fila, int columna ){
		//comprobar 8 valores fila, columna
		int cerca = 0;
		int numCol = dimension - 1;
		int numFil = dimension - 1;
		// posicion izquierda arriba
//		System.out.println("Fila " + fila + ", Columna " + columna );
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

	
}
