/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogot� - Colombia)
 * Departamento de Ingenier�a de Sistemas y Computaci�n 
 * Licenciado bajo el esquema Academic Free License version 2.1 
 *
 * Proyecto Cupi2 (http://cupi2.uniandes.edu.co)
 * Ejercicio: n3_avion
 * Autor: Equipo Cupi2 2017
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
 */
package uniandes.cupi2.avion.mundo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import uniandes.cupi2.avion.mundo.Silla.Clase;
import uniandes.cupi2.avion.mundo.Silla.Ubicacion;
import uniandes.cupi2.avion.mundo.Pasajero;
/**
 * Avi�n de pasajeros.
 */
public class Avion {
	// -----------------------------------------------------------------
	// Constantes
	// -----------------------------------------------------------------

	/**
	 * N�mero de sillas ejecutivas.
	 */
	public final static int SILLAS_EJECUTIVAS = 8;

	/**
	 * N�mero de sillas econ�micas.
	 */
	public final static int SILLAS_ECONOMICAS = 42;

	// -----------------------------------------------------------------
	// Atributos
	// -----------------------------------------------------------------

	/**
	 * Sillas de la clase ejecutiva del avi�n.
	 */

	private List<Silla> sillasEjecutivas;

	/**
	 * Sillas de la clase econ�mica del avi�n.
	 */
	private List<Silla> sillasEconomicas;

	// -----------------------------------------------------------------
	// Constructores
	// -----------------------------------------------------------------

	/**
	 * Construye el avi�n. <br>
	 * <b>post: </b> Se inicializan las listas de sillas ejecutivas y econ�micas.
	 */
	public Avion() {
		Ubicacion ubicacion;

		// Crea las sillas ejecutivas
		sillasEjecutivas = new ArrayList<>();

		// Crea las sillas econ�micas
		sillasEconomicas = new ArrayList<>();

		sillasEjecutivas.add(new Silla(1, Clase.EJECUTIVA, Ubicacion.VENTANA));
		sillasEjecutivas.add(new Silla(2, Clase.EJECUTIVA, Ubicacion.PASILLO));
		sillasEjecutivas.add(new Silla(3, Clase.EJECUTIVA, Ubicacion.PASILLO));
		sillasEjecutivas.add(new Silla(4, Clase.EJECUTIVA, Ubicacion.VENTANA));
		sillasEjecutivas.add(new Silla(5, Clase.EJECUTIVA, Ubicacion.VENTANA));
		sillasEjecutivas.add(new Silla(6, Clase.EJECUTIVA, Ubicacion.PASILLO));
		sillasEjecutivas.add(new Silla(7, Clase.EJECUTIVA, Ubicacion.PASILLO));
		sillasEjecutivas.add(new Silla(8, Clase.EJECUTIVA, Ubicacion.VENTANA));

		for (int numSilla = 1 + SILLAS_EJECUTIVAS, j = 1; j <= SILLAS_ECONOMICAS; numSilla++, j++) {
			// Sillas ventana
			if (j % 6 == 1 || j % 6 == 0)
				ubicacion = Ubicacion.VENTANA;
			// Sillas centrales
			else if (j % 6 == 2 || j % 6 == 5)
				ubicacion = Ubicacion.CENTRAL;
			// Sillas pasillo
			else
				ubicacion = Ubicacion.PASILLO;

			sillasEconomicas.add(new Silla(numSilla, Clase.ECONOMICA, ubicacion));
		}
	}

	// -----------------------------------------------------------------
	// M�todos
	// -----------------------------------------------------------------

	/**
	 * Asigna la silla al pasajero en la clase y ubicaci�n especificados. <br>
	 * <b>post: </b> Si existe una silla con la clase y la ubicaci�n dada, el
	 * pasajero queda asignado en la primera de ellas seg�n el orden num�rico.
	 * 
	 * @param pClase
	 *            Clase elegida por el pasajero. Clase pertenece {Clase.EJECUTIVA,
	 *            Clase.ECONOMICA}.
	 * @param pUbicacion
	 *            Ubicaci�n elegida por el pasajero. Si clase = Clase.ECONOMICA
	 *            entonces ubicaci�n pertenece {VENTANA, CENTRAL, PASILLO}, <br>
	 *            o si clase = Clase.EJECUTIVA entonces ubicaci�n pertenece
	 *            {VENTANA, PASILLO}.
	 * @param pPasajero
	 *            Pasajero a asignar. pPasajero != null y no tiene silla en el
	 *            avi�n.
	 * @return Silla asignada al pasajero o null si no se pudo asignar una silla al
	 *         pasajero en la ubicaci�n y clase especificados.
	 */
	public Silla asignarSilla(Clase pClase, Ubicacion pUbicacion, Pasajero pPasajero) {
		// busca una silla libre
		Silla silla = null;
		if (pClase == Clase.EJECUTIVA) {
			silla = buscarSillaLibreCurry(sillasEjecutivas).apply(pUbicacion);
		} else if (pClase == Clase.ECONOMICA) {
			silla = buscarSillaLibreCurry(sillasEconomicas).apply(pUbicacion);
		}
		if (silla != null) {
			silla.asignarAPasajero(pPasajero);
		}
		return silla;
	}

	/**
	 * Busca la siguiente silla ejecutiva que este libre y tenga la ubicaci�n
	 * indicada. <br>
	 * <b>pre: </b> La lista de sillas ejecutivas est� inicializada.
	 * 
	 * @param pUbicacion
	 *            Ubicaci�n en donde buscar la silla. pUbicaci�n pertenece {VENTANA,
	 *            PASILLO}.
	 * @return La silla libre encontrada. Si no encuentra una silla retorna null.
	 */
	public Silla buscarSillaEjecutivaLibre(Ubicacion pUbicacion) {
		return buscarSillaLibreCurry(sillasEjecutivas).apply(pUbicacion);
	}

	/**
	 * Busca la siguiente silla econ�mica que este libre y tenga la ubicaci�n
	 * indicada. <br>
	 * <b>pre: </b> La lista de sillas econ�micas est� inicializada.
	 * 
	 * @param pUbicacion
	 *            Ubicaci�n en donde buscar la silla. pUbicaci�n pertenece {VENTANA,
	 *            CENTRAL, PASILLO}.
	 * @return Silla libre encontrada. Si no encuentra una silla retorna null.
	 */
	public Silla buscarSillaEconomicaLibre(Ubicacion pUbicacion) {
		return buscarSillaLibreCurry(sillasEconomicas).apply(pUbicacion);
	}

	public Function<Ubicacion,Silla> buscarSillaLibreCurry(List<Silla> sillas){
		return (Ubicacion pUbicacion) ->sillas.stream()
				.filter((Silla silla) -> !(silla.sillaAsignada()) && silla.darUbicacion() == pUbicacion)
				.findFirst()
				.orElse(null);
	}
	/**
	 * Busca un pasajero en el avi�n.
	 * 
	 * @param pPasajero
	 *            Pasajero a buscar. pPasajero != null.
	 * @return Silla en la que se encontr� el pasajero. Si no lo encuentra retorna
	 *         null.
	 */
	public Silla buscarPasajero(Pasajero pPasajero) {
		// Busca el pasajero en ejecutiva
		Silla silla = buscarPasajeroEjecutivo(pPasajero);
		// Si no estaba en ejecutiva
		if (null == silla) {
			// Busca en econ�mica
			silla = buscarPasajeroEconomico(pPasajero);
		}

		return silla;

	}
	
	/**
     * Busca un pasajero en las sillas ejecutivas. <br>
     * <b>pre: </b> La lista de sillas ejecutivas est� inicializada.
     * @param pPasajero Pasajero a buscar. pPasajero != null.
     * @return Silla en la que se encontr� el pasajero. Si no lo encuentra retorna null.
     */
    public Silla buscarPasajeroEjecutivo( Pasajero pPasajero ) {
    	return buscarPasajeroCurry(sillasEjecutivas).apply(pPasajero);        
    }

	/**
	 * Busca un pasajero en las sillas econ�micas. <br>
	 * <b>pre: </b> La lista de sillas econ�micas est� inicializada.
	 * 
	 * @param pPasajero
	 *            Pasajero a buscar. pPasajero != null.
	 * @return Silla en la que se encontr� el pasajero. Si no lo encuentra retorna
	 *         null.
	 */
	public Silla buscarPasajeroEconomico(Pasajero pPasajero) {
		return buscarPasajeroCurry(sillasEconomicas).apply(pPasajero);   
	}

	private Function<Pasajero, Silla> buscarPasajeroCurry(List<Silla> sillas) {
		return (Pasajero pPasajero) -> sillas.stream()
				.filter((Silla silla)-> silla.sillaAsignada() && silla.darPasajero().igualA((pPasajero)))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Desasigna la silla de un pasajero. <br>
	 * <b>post: </b> Si se encuentra una silla con el pasajero, la silla quedara con
	 * su pasajero igual a null.
	 * 
	 * @param pPasajero
	 *            Pasajero a retirar. pPasajero != null.
	 * @return Retorna true si encontr� el pasajero y des asign� la silla, false en
	 *         caso contrario.
	 */
	public boolean desasignarSilla(Pasajero pPasajero) {
		// Busca el pasajero en el avi�n
		Silla silla = buscarPasajero(pPasajero);
		boolean resultado = false;
		// Si lo encuentra desasigna
		if (silla != null) {
			silla.desasignarSilla();
			resultado = true;
		}
		return resultado;
	}

	/**
	 * Retorna el n�mero de sillas ejecutivas ocupadas. <br>
	 * <b>pre: </b> La lista de sillas ejecutivas est� inicializada.
	 * 
	 * @return N�mero de silla ejecutivas ocupadas.
	 */
	public int contarSillasEjecutivasOcupadas() {
		return (int) sillasEjecutivas.stream()
				.filter((Silla silla) -> silla.sillaAsignada())
				.count();
	}

	/**
	 * Retorna el n�mero de sillas econ�micas ocupadas. <br>
	 * <b>pre: </b> La lista de sillas econ�micas est� inicializada.
	 * 
	 * @return N�mero de sillas econ�micas ocupadas.
	 */
	public int contarSillasEconomicasOcupadas() {
		return (int) sillasEconomicas.stream()
				.filter((Silla silla) -> silla.sillaAsignada())
				.count();
	}

	/**
	 * Calcula el porcentaje de ocupaci�n del avi�n.
	 * 
	 * @return Porcentaje total de ocupaci�n.
	 */
	public double calcularPorcentajeOcupacion() {
		double porcentaje;
		int totalSillas = SILLAS_ECONOMICAS + SILLAS_EJECUTIVAS;
		int sillasOcupadas = contarSillasEconomicasOcupadas() + contarSillasEjecutivasOcupadas();
		porcentaje = (double) sillasOcupadas / totalSillas * 100;
		return porcentaje;
	}

	/**
	 * Retorna las sillas ejecutivas del avi�n.
	 * 
	 * @return Sillas ejecutivas del avi�n.
	 */
	public Silla[] obtenerSillasEjecutivas() {
		return (Silla[]) sillasEjecutivas.toArray();
	}

	/**
	 * Retorna las sillas econ�micas del avi�n.
	 * 
	 * @return Sillas econ�micas del avi�n.
	 */
	public Silla[] obtenerSillasEconomicas() {
		return (Silla[]) sillasEconomicas.toArray();
	}

	/**
	 * M�todo para la extensi�n 1.
	 * 
	 * @return Respuesta 1.
	 */
	public String metodo1() {
		return "Respuesta 1";
	}

	/**
	 * M�todo para la extensi�n 2.
	 * 
	 * @return Respuesta 2.
	 */
	public String metodo2() {
		return "Respuesta 2";
	}

}