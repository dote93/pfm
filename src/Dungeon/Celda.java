package Dungeon;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Clase que gestiona las celdas
 * @version 1.0
 * @author  Alvaro Valle del Pozo
 * 			TFM
 * 			Profesor: Jose Maria Font y Daniel Manrique
 * 			Fecha: 01-02-2016
 */

public class Celda 
{
	
	//Si la celda ha sido visitada.
	public boolean visitada;
	
	// Posicion de su precursora
	public int [] Posicion_precursor;
	
	//Coordenadas x,y de la celda.	
	int fila;	
	int columna;
	
	//Maximas coordenadas del mapa
	int max_fila;
	int max_columna;
	
	//Varibales para saber que es lo que va a conetener dentro la celda
	public boolean puerta;
	public boolean tesoro;
	public boolean monstruo;
	
	//Variables para saber en que lado esta la puerta
	public boolean puerta_N;
	public boolean puerta_S;
	public boolean puerta_E;
	public boolean puerta_O;
	
	//Variables para comprobar la distancia entre una puerta y un tesoro
	public boolean inicio;
	public boolean destino;
	public boolean camino;
	
	
	public int[] genotipo_celda;
	
	//Lista de las paredes de los vecinos.
	//ArrayList<Pared> lista = new ArrayList<Pared>();
	
	//Enum para saber que tipo de puerta hay en esa celda
	public enum Tipo_puertas
	{
		ENTRADA, SALIDA, ENTRADA_SALIDA
	};
	
	//Tipo de puerta
	public Tipo_puertas tipo_puerta;
	
	
	public ArrayList<Celda> Vecinos = new ArrayList<Celda>();
	
	/** 
     *	Constructor de Celda
     */
	public Celda(int fila, int columna, int max_fila, int max_columna, boolean vista, int[] genotipo)
	{
		//igualamos las x y las y que recibimos a las variables que tenemos creadas en la clase celda para poder usarlo en otras
		//funciones de otras clases
		this.fila    = fila;
		this.columna = columna;
		
		this.max_fila = max_fila;
		this.max_columna = max_columna;
		
		
		//Se inicializa la celda sin contener ningun monstruo ni puerta ni tesoro 
		puerta   = false;
		tesoro   = false;
		monstruo = false;
		
		//Se inicializan el lado donde va a estar la puerta a false
		puerta_N = false;
		puerta_S = false;
		puerta_E = false;
		puerta_O = false;
		
		
		// decimos que la celda ha sido visitada
		visitada = vista;
		
		// Inicializamos el array de int que luego va a guardar las coordenadas de su precursora
		Posicion_precursor = new int [2];
		
		
		//se guarda el genotipo de la celda
		genotipo_celda= genotipo;
		
		//creamos las paredes pasandole las posiciones x,y y el tama�o maximo de x y el tama�o maximo de y 
		//crearParedes(fila, columna, max_fila, max_columna);
	}
	
	
	
	/** 
     *	Funci�n que calcula el coste entre la posicion de salida y la posicion en la que nos encontramos (coste) y el coste entre la posicion en la que nos
     *  encontramos y la meta (estimacion)
     */
	public int coste(int posicion_salida_x, int posicion_salida_y, int posicion_meta_x, int posicion_meta_y ,int posicion_x, int posicion_y)
	{
		//variables para guardar el coste y la estimacion de ir de la celda en la que nos encontramos hasta la final viniendo de la inicial
		int coste;
		int estimacion;
		
		// igualamos el coste a la suma de los valores absolutos de las diferencias entre la posicion de salida y la posicion inicial en las variables x e y
		coste = (Math.abs(posicion_salida_x - posicion_x)) + (Math.abs(posicion_salida_y - posicion_y));
		
		// igualamos la estimacon a la suma de los valores absolutos de las diferencias entre la posicion de meta y la posicion inicial en las variables x e y
		estimacion = (Math.abs(posicion_meta_x - posicion_x)) + (Math.abs(posicion_meta_y - posicion_y));
		
		
		return (coste + estimacion); // devolvemos la suma del coste y la estimacion de la celda que hemos recibido
	}
	
}
