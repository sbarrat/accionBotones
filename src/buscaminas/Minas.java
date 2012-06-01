package buscaminas;
public class Minas {

	boolean colocada;
	long mina;
	private long minas[];
	private int dimension = 10;
	public Minas(){
		
	}
	/**
	 * Establecemos la dimension del numero de minas
	 * @param nuevaDimension
	 */
	public void setDimension( int nuevaDimension  ) {
		dimension = nuevaDimension;
		setMinas(new long[dimension]);
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
		getMinas()[0] = generaAleatorio();
		// Colocamos el resto de minas
		for (int i = 1; i < getDimension(); i++) {
			while( !colocada ) {
				mina = generaAleatorio();
				for (int j = 0; j < getDimension(); j++) {
					if( getMinas()[j] == mina) {
						colocada = false;
						break;
					}
					colocada = true;
				}
			}
			getMinas()[i] = mina;
			colocada = false;
		}
		for (int i = 0; i < getMinas().length; i++) {
			Aplicacion.DebugJuego(String.valueOf(getMinas()[i]));
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
		for (int i = 0; i < getMinas().length; i++) {
			if ( getMinas()[i] == posicion ) {
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
		
//		System.out.println("Fila " + fila + ", Columna " + columna );
		Aplicacion.DebugJuego(fila+":"+columna +"-"+"Posicion:" + (fila*dimension+columna));
		// posicion izquierda arriba
		if ( fila > 0 && columna > 0 ) {
			if ( hayMina( ( fila - 1 ) * dimension + ( columna - 1 ) ) ) {
				cerca++;
				Aplicacion.DebugJuego("cerca1:"+ (( fila - 1 ) * dimension + ( columna - 1 )));
			}
		}
		// posicion arriba
		if ( fila > 0 ) {
			if ( hayMina( ( fila ) * dimension + ( columna - 1 ) ) ) {
				cerca++;
				Aplicacion.DebugJuego("cerca2:" + (( fila ) * dimension + ( columna - 1 )));
			}
		}
		// posicion arriba derecha
		if ( fila > 0 && columna < numCol ) {
			if ( hayMina( ( fila + 1 ) * dimension + ( columna - 1 ) ) ) {
				cerca++;
				Aplicacion.DebugJuego("cerca3:" + (( fila + 1 ) * dimension + ( columna - 1 )));
			}
		}
		// posicion centro izquierda
		if ( columna > 0 ) {
			if ( hayMina( ( fila - 1 ) * dimension + ( columna ) ) ) {
				cerca++;
				Aplicacion.DebugJuego("cerca4:" + (( fila - 1 ) * dimension + ( columna ) ));
			}
			
		}
		// posicion centro derecha
		if ( columna < numCol ) {
			if ( hayMina( ( fila + 1 ) * dimension + ( columna ) ) ) {
				cerca++;
				Aplicacion.DebugJuego("cerca5:" + (( fila + 1 ) * dimension + ( columna )));
			}
		}
		// posicion izquierda abajo
		if ( fila < numFil && columna > 0 ) {
			if ( hayMina( ( fila - 1 ) * dimension + ( columna + 1 ) ) ) {
				cerca++;
				Aplicacion.DebugJuego("cerca6:" + (( fila - 1 ) * dimension + ( columna + 1 )));
			}
		}
		// posicion abajo
		if ( fila < numFil ) {
			if ( hayMina( ( fila ) * dimension + ( columna + 1 ) ) ) {
				cerca++;
				Aplicacion.DebugJuego("cerca7:" + (( fila ) * dimension + ( columna + 1 )));
			}
		}
		// posicion abajo derecha
		if ( fila < numFil && columna < numCol ) {
			if ( hayMina( ( fila + 1 ) * dimension + ( columna + 1 ) ) ) {
				cerca++;
				Aplicacion.DebugJuego("cerca8:" + (( fila + 1 ) * dimension + ( columna + 1 )));
			}
		}
		
		return cerca;
		
	}
	long[] getMinas() {
		return minas;
	}
	void setMinas(long minas[]) {
		this.minas = minas;
	}

	
}
