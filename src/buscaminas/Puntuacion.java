package buscaminas;
import java.io.Serializable;
/**
 * Creamos la clase Puntuacion Serializable para
 * almacenar la puntuacion como un objeto en el 
 * fichero de datos
 *
 */

@SuppressWarnings("serial")
public class Puntuacion implements Serializable{
	private int tiempo;
	private String nombre;
	
	public Puntuacion( int t, String n ){
		setTiempo(t);
		setNombre(n);
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
