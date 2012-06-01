package buscaminas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Clase encargada de la lectura y escritura del fichero de Puntuaciones
 * 
 */
public class Fichero {
	private final String archivo = "puntuaciones";
	Hashtable<String, Puntuacion> tabla;
	private String totalRegistros;

	@SuppressWarnings("unchecked")
	public Fichero() throws IOException, ClassNotFoundException {
		FileInputStream fichero = null;
		ObjectInputStream objeto = null;
		try {
			fichero = new FileInputStream(archivo);
			objeto = new ObjectInputStream(fichero);
			// Volcamos los datos en el HashTable
			tabla = (Hashtable<String, Puntuacion>) objeto.readObject();
			// Cerramos el stream;
			objeto.close();
		} catch (FileNotFoundException e) {
			// Si no existe fichero lo crea
			tabla = new Hashtable<String, Puntuacion>();
		}
		//
		setTotalRegistros(tabla.size());

	}

	public String getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = String.valueOf(totalRegistros);
	}

	/**
	 * Agregamos la puntuacion al array
	 * 
	 * @param tiempo
	 * @param nombre
	 * @return
	 */
	public boolean agregar(int tiempo, String nombre) {

		if (!tabla.containsKey(totalRegistros)) {
			Puntuacion nueva = new Puntuacion(tiempo, nombre);
			tabla.put(totalRegistros, nueva);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Devolvemos todas las puntuaciones
	 */
	@SuppressWarnings("rawtypes")
	public Enumeration puntuaciones() {

		return tabla.keys();
	}

	/**
	 * Guarda la tabla en el disco
	 * 
	 * @throws IOException
	 */
	public void guardar() throws IOException {
		FileOutputStream ficheroSalida = new FileOutputStream(archivo);
		ObjectOutputStream objetoSalida = new ObjectOutputStream(ficheroSalida);
		objetoSalida.writeObject(tabla);
		objetoSalida.close();
	}
	/**
	 * Devuelve la puntuacion seleccionada
	 * @param numero
	 * @return
	 */
	public Puntuacion verRecord( String numero ){
		if( tabla.containsKey( numero) )  {
			return tabla.get( numero );
		} else {
			return null;
		}
	}

}
