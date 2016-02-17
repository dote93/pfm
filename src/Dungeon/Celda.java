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
	
	
	//Lista de las paredes de los vecinos.
	ArrayList<Pared> lista = new ArrayList<Pared>();
	
	//Enum para saber que tipo de puerta hay en esa celda
	public enum Tipo_puertas
	{
		ENTRADA, SALIDA, ENTRADA_SALIDA
	};
	
	//Tipo de puerta
	public Tipo_puertas tipo_puerta;
	
	
	/** 
     *	Constructor de Celda
     */
	public Celda(int fila, int columna, int max_fila, int max_columna, boolean vista)
	{
		//igualamos las x y las y que recibimos a las variables que tenemos creadas en la clase celda para poder usarlo en otras
		//funciones de otras clases
		this.fila    = fila;
		this.columna = columna;
		
		
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
		
		//creamos las paredes pasandole las posiciones x,y y el tama–o maximo de x y el tama–o maximo de y 
		crearParedes(fila, columna, max_fila, max_columna);
	}
	
	/** 
     *	Funci—n que nos vuelve si la pared esta abierta o no en la direccion que recibe
     */
	public boolean isParedOpen(Pared.Direcciones direction)
	{
		boolean res = false;
		
		//Para cada pared de la lista
		for(Pared p:lista)
		{
			// Si la direccion de la pared es la misma que la direccion que nos est‡n pasando decimos que el booleano se ponga a true
			if(p.getDirection() == direction)
			{
				res = p.open;
			}
		}
		
		return res; // devolvemos si la pared esta abierta o no s
	}
	
	/** 
     *	Funci—n que nos crea las paredes de las celdas
     */
	public void crearParedes(int fila, int columna, int max_fila, int max_columna)
	{
		
		//Si soy esquina superior derecha
		if(columna == max_columna && fila == 0)
		{
			lista.add(new Pared(Pared.Direcciones.OESTE, false));
			lista.add(new Pared(Pared.Direcciones.SUR, false));
			
		}
		//Si soy esquina superior izquierda
		if(fila == 0 && columna == 0)
		{
			lista.add(new Pared(Pared.Direcciones.ESTE, false));
			lista.add(new Pared(Pared.Direcciones.SUR, false));
		}
		//Si soy esquina inferior derecha
		if(fila == max_fila && columna == max_columna)
		{
			lista.add(new Pared(Pared.Direcciones.OESTE, false));
			lista.add(new Pared(Pared.Direcciones.NORTE, false));
		}
		//si soy esquina inferior izquierda
		if(fila == max_fila && columna == 0)
		{
			lista.add(new Pared(Pared.Direcciones.ESTE, false));
			lista.add(new Pared(Pared.Direcciones.NORTE, false));
		}
		//Si soy lado derecho
		if(columna == max_columna && (fila > 0 && fila < max_fila))
		{
			lista.add(new Pared(Pared.Direcciones.NORTE, false));
			lista.add(new Pared(Pared.Direcciones.SUR, false));
			lista.add(new Pared(Pared.Direcciones.OESTE, false));
		}
		//Si soy lado izquierdo
		if(columna == 0 && (fila > 0 && fila < max_fila))
		{
			lista.add(new Pared(Pared.Direcciones.NORTE, false));
			lista.add(new Pared(Pared.Direcciones.SUR, false));
			lista.add(new Pared(Pared.Direcciones.ESTE, false));
		}
		//Si soy lado norte
		if(fila == 0 && (columna > 0 && columna < max_columna))
		{
			lista.add(new Pared(Pared.Direcciones.OESTE, false));
			lista.add(new Pared(Pared.Direcciones.ESTE, false));
			lista.add(new Pared(Pared.Direcciones.SUR, false));
		}
		//Si soy lado sur
		if(fila == max_fila && (columna > 0 && columna < max_columna))
		{
			lista.add(new Pared(Pared.Direcciones.OESTE, false));
			lista.add(new Pared(Pared.Direcciones.ESTE, false));
			lista.add(new Pared(Pared.Direcciones.NORTE, false));
		}
		//Si estoy por el medio
		if((fila > 0 && fila <max_fila) && (columna > 0 && columna < max_columna))
		{
			lista.add(new Pared(Pared.Direcciones.ESTE, false));
			lista.add(new Pared(Pared.Direcciones.OESTE, false));
			lista.add(new Pared(Pared.Direcciones.NORTE, false));
			lista.add(new Pared(Pared.Direcciones.SUR, false));
		}
	}
	
	/** 
     *	Funci—n que nos devuelve la pared opuesta de la direccion que recibe 
     */
	public Pared UnionParedes(Pared.Direcciones direction)
	{
		Pared.Direcciones opuesta = null;
		Pared paredOpuesta = null;
		
		switch(direction)
		{
		case ESTE:
			opuesta = Pared.Direcciones.OESTE;
			break;
		case OESTE:
			opuesta = Pared.Direcciones.ESTE;
			break;
		case NORTE:
			opuesta = Pared.Direcciones.SUR;
			break;
		case SUR:
			opuesta = Pared.Direcciones.NORTE;
			break;
		}
		
		//creamos un iterador de tipo pared que lo inicializamos conteniendo la lista de paredes
		Iterator<Pared> i =lista.iterator();
		// si seguimos teniendo contenido en el iterador continuamos
		while(i.hasNext())
		{
			// guardamos en una variable de tipo pared la siguiente posicion del iterador
			Pared miPared = i.next();
			
			// si la direccion de la pared es igual a la direccion a la opuesta guardamos en la variable pared opuesta la variable mipared
			if (miPared.direction == opuesta)
			{
				paredOpuesta = miPared;
			}
		}
		
		return paredOpuesta; // devolvemos la variable de tipo pared que contiene la direccion de la pared opuesta
	}

	/** 
     *	Funci—n que nos devuelde la lista de las paredes vecinas
     */
	public ArrayList<Pared> getLista() {
		return lista;
	}
	
	/** 
     *	Funci—n que calcula el coste entre la posicion de salida y la posicion en la que nos encontramos y el coste entre la posicion en la que nos
     *  encontramos y la meta
     */
	public int coste(int posicion_salida_x, int posicion_salida_y, int posicion_meta_x, int posicion_meta_y ,int posicion_x, int posicion_y)
	{
		//variables para guardar el coste y la estimacion de ir de la celda en la que nos encontramos hasta la final priviniendo de la inicial
		int coste;
		int estimacion;
		
		// igualamos el coste a la suma de los valores absolutos de las diferencias entre la posicion de salida y la posicion inicial en las variables x e y
		coste = (Math.abs(posicion_salida_x - posicion_x)) + (Math.abs(posicion_salida_y - posicion_y));
		
		// igualamos la estimacon a la suma de los valores absolutos de las diferencias entre la posicion de meta y la posicion inicial en las variables x e y
		estimacion = (Math.abs(posicion_meta_x - posicion_x)) + (Math.abs(posicion_meta_y - posicion_y));
		
		
		return (coste + estimacion); // devolvemos la suma del coste y la estimacion de la celda que hemos recibido
	}
	
}
