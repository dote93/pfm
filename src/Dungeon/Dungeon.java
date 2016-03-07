package Dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import Dungeon.Celda.Tipo_puertas;

/*
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
*/

/**
 * Clase que gestiona el dungeon
 * @version 1.0
 * @author  Alvaro Valle del Pozo
 * 			TFM
 * 			Profesor: Daniel Manrique y Jose Maria Font
 * 			Fecha: 01-02-2016
 */
public class Dungeon 
{
	int f;
	int c;
	int filas_iniciales= 0;
	int columnas_iniciales=0;
	
	//FENOTIPO INDIVIDUO
	public Celda [][] dungeon;
	
	//GENOTIPO INDIVIDUO
	public int [] genotipo; // array que va a almacenar el genotipo del invididuo 
	
	//Variable para saber cuantos tipos de celdas va a haber, es decir, el esponente de 2 elevado a N
	public int tipo_celdas; 
	
	//Variable para saber la posicion de donde estan las paredes
	public ArrayList<Celda>  posicion_puertas = new ArrayList<Celda>();

	//Variable para saber la posicion de donde estan los tesoros
	public ArrayList<Celda>  posicion_tesoros = new ArrayList<Celda>();
		
	//Variable para saber la posicion de donde estan los monstruos
	public ArrayList<Celda>  posicion_monstruos = new ArrayList<Celda>();
	
	
	
	//Variable que va a guardar una lista cada puerta (que esta almacena las distancias con cada tesoro)
	public ArrayList<Integer> distancias_PT = new ArrayList<Integer>();
	
	//Variable que va a guardar la media de las distancias entre PT de todo el dungeon
	public double media_distancias_PT = 0.0;
	
	//Arraylist que va a guardar las posiciones de los tesoros mas cercanos con respecto a cada puerta
	public ArrayList<Celda> posiciones_T_cercanos = new ArrayList<Celda>();
	
	
	//Arraylist de las distancias minimas 
	public ArrayList<Integer> distancia_min = new ArrayList<Integer>();
	
	
	//Fitness del individuo
	public double fitness = 0.0;
	
	
	//booleano para saber si ya se ha llegado a la meta y si es asi ya no se tiene que seguir pasando por la funcion
	boolean llegada= false;
	
	
	//Variable para calcular un numero aleatorio entre el numero de filas o columnas y 0
	int min = 0;
	
	//Variables para saber si ya hay una puerta en ese lado
	boolean hay_puerta_N;
	boolean hay_puerta_S;
	boolean hay_puerta_E;
	boolean hay_puerta_O;
	
	
	
	
	
	//Arraylist que guardan las paredes de la celda donde nos encontramos y de la opuesta a la pared seleccionada
	//ArrayList<Pared> lista = new ArrayList<Pared>();
	//ArrayList<Pared> lista_opuesta = new ArrayList<Pared>();
	
	
	//Distancia entre una celda P y una T
	public int distancia = 0;
	
	//Variable que va a guardar una lista cada puerta (que esta almacena las distancias con cada tesoro)
	public ArrayList<Integer> puertas_distancias = new ArrayList<Integer>();
	
	//Variable que va a guardar una lista de todas las distancias entre las puertas y los tesoros
	public ArrayList<Integer> tam_dist = new ArrayList<Integer>();
	
	//Varible que guarda todas las distancias de todos los individuos de la poblacion
	public ArrayList<Integer> puertas_distancias_poblacion = new ArrayList<Integer>();
		
	
	
	/** 
     *	Constructor de Dungeon
     */
	public Dungeon(int _f, int _c, int numero_monstruos, int numero_tesoros,ArrayList<int[]> pos_puertas, ArrayList<Tipo_puertas> t_puertas , int numero_puertas, int porcentaje, int tipo_celdas_)
	{
		//Se igualan las dimensiones del dungeon
		f= _f;
		c= _c;
		
		//se guarda el tipo de celdas que va a haber para saber el tamano del genotipo, tanto del individuo como de cada celda
		tipo_celdas = tipo_celdas_;
		
		//Inicializamos el dungeon para crear primero las dimensiones que va a tener el mapa
		inicializarDungeon(); 
		
		
		
		//SE ANADEN LOS OBJETOS AL MAPA***************************
		
		
		//Se anaden las puertas al mapa
		anadir_puertas_posicion(pos_puertas, t_puertas, numero_puertas);
		
		
		//Se anaden los tesoros al mapa
		numero_tesoros = comprobar_tesoros(f, c);
		
		
		generateDungeon(0,0);
		
		/*
		//Se anaden los monstruos al mapa
		anadir_monstruos(f, c, numero_monstruos);
		
		//Se anaden los tesoros al mapa
		anadir_tesoros(f, c, numero_tesoros);
		
		
		//SE ABREN LAS PAREDES*************************************
		//Generamos el dungeon empezando por la posicion 0 0
		generateDungeon(0, 0); 
		
		//Se quitan paredes dependiendo del porcentaje que se reciba
		//abrir_paredes(f, c, porcentaje);
		
		*/
		
		
		//SE CALCULA EL FITNESS DEL INDIVIDUO**********************
		//Se calcula las distancias PT
		calcular_distancias_PT(numero_puertas, numero_tesoros);
		
		//Calculo el fitness total
		//calcularfitness();
		
		
	}
	

	/**
	 * Funcion que genera el dungeon del tamano que nos han dado y va abriendo las puertas entre las celdas
	 * @param _x fila de la celda no visitada
	 * @param _y columna de la celda no visitada
	 */
	public void generateDungeon(int _x, int _y) 
	{
		Celda inicio = dungeon[_x][_y]; //guardamos las coordenadas de la celda en la que nos encontramos
		
		inicio.visitada = true;//marcamos la celda como visitada
		
		//Guardamos todas las celdas vecinas de la celda actual recorriendo la lista
		ArrayList<Celda> vecinos = get_Vecinos(inicio);
		//Collections.reverse(vecinos);//le damos la vuelta a los vecinos que recibimos
		//Collections.shuffle(vecinos);//desordenamos los vecinos para generar el siguiente vecino random
		//le damos la vuelta y luego le damos a desordenar para que haya menos probabilidades de que salga igual el mapa
		
		//Recorremos la lista de vecinos para comprobar cuales han sido visitados y cuales no y el primero que no estŽ
		//visitado lo visitamos
		Iterator<Celda> it =vecinos.iterator();
		//Mientras haya un vecino siguiente en la lista por el cual no hayamos pasado seguimos dentro del while
		while(it.hasNext())
		{
			//Cojo la primera vecina de la lista de vecinas.
			Celda currentVecino = it.next();//guardamos la pared del vecino que tenemos como siguiente
			
			//Guardo una referencia a la celda vecina.
			int [] siguientePosicion = {(currentVecino.fila - _x) + _x, (currentVecino.columna - _y) + _y};
			Celda neighbour = dungeon[siguientePosicion[0]][siguientePosicion[1]];
			
			
			//si el vecino ha sido visitado o es de tipo muro continuo, sino lo marco como celda visitada en la siguiente iteracion
			if(neighbour.visitada ) //|| (neighbour.genotipo_celda[0] == 1 && neighbour.genotipo_celda[1] == 1 && neighbour.genotipo_celda[2] == 1) )
			{
				//Si ha sido visitada continuo sin hacer nada, pues es una celda que ya ha sido abierta por algœn lado
				continue;
			}
			else
			{
				//Marco el vecino como visitado, puesto que va a ser nuestra siguiente posicion
				generateDungeon(neighbour.fila, neighbour.columna);
			}
			
		}

	}
	
	/**
	 * Funcion que se encarga de poner una puerta en la posicion que recibe
	 * @param f fila donde va a estar la puerta
	 * @param c columna donde va a estar la puerta
	 */
	public void anadir_puertas_posicion(ArrayList<int[]> pos_puertas, ArrayList<Celda.Tipo_puertas> t_puertas, int numero_puertas)
	{
		for (int puerta=0; puerta<numero_puertas; puerta++)
		{
			dungeon[pos_puertas.get(puerta)[0]][pos_puertas.get(puerta)[1]].puerta = true;
			
			//se guarda la posicion de la puerta en el mapa
			int[] posicion = {pos_puertas.get(puerta)[0], pos_puertas.get(puerta)[1]};
			posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
			
			
			dungeon[posicion[0]][posicion[1]].tipo_puerta = t_puertas.get(puerta);
			
			if(posicion[0] == 0 && (posicion[1] > 0 && posicion[1] < c ))// Si es lado norte
			{
				dungeon[posicion[0]][posicion[1]].puerta_N = true;
			}
			else if (posicion[0] == (f - 1) && (posicion[1] > 0 && posicion[1] < c ))// Si es lado sur
			{
				dungeon[posicion[0]][posicion[1]].puerta_S = true;
			}
			else if (posicion[1] == (c -1) && (posicion[0] > 0 && posicion[0] < f ))// Si es lado este
			{
				dungeon[posicion[0]][posicion[1]].puerta_E = true;
			}
			else if (posicion[1] == 0 && (posicion[0] > 0 && posicion[0] < f ))// Si es lado oeste
			{
				dungeon[posicion[0]][posicion[1]].puerta_O = true;
			}
			

		}
	}
	
	/**
	 * Funcion que comprueba los tesoros del mapa
	 * @param f filas
	 * @param c columnas
	 * @param numero_tesoros
	 */
	public int comprobar_tesoros(int f, int c)
	{
		int numero_tesoros = 0;
		
		//Se comprueban los tesoros que se han anadido en el mapa
		for (int fila=0; fila<f; fila++)
		{
			
			for(int columna = 0; columna< c; columna++)
			{
		
				//Hay una moneda
				if(dungeon[fila][columna].genotipo_celda[0] == 0 && dungeon[fila][columna].genotipo_celda[1] == 1 && dungeon[fila][columna].genotipo_celda[2] == 1)
				{
					dungeon[fila][columna].tesoro = true; 
					posicion_tesoros.add(dungeon[fila][columna]);
					numero_tesoros++;
				}
				
				//Hay una llave
				else if(dungeon[fila][columna].genotipo_celda[0] == 1 && dungeon[fila][columna].genotipo_celda[1] == 0 && dungeon[fila][columna].genotipo_celda[2] == 1)
				{
					dungeon[fila][columna].tesoro = true; 
					posicion_tesoros.add(dungeon[fila][columna]);
					numero_tesoros++;
					
				}
				
				//Hay una piedra preciosa
				else if(dungeon[fila][columna].genotipo_celda[0] == 1 && dungeon[fila][columna].genotipo_celda[1] == 1 && dungeon[fila][columna].genotipo_celda[2] == 0)
				{
					dungeon[fila][columna].tesoro = true; 
					posicion_tesoros.add(dungeon[fila][columna]);
					numero_tesoros++;
				}
			}
			
		}
		return numero_tesoros;
	}
	
	
	/**
	 * Funcion que se encarga de ver cuales son los vecinos de una celda
	 * @return
	 */
	public ArrayList<Celda> get_Vecinos(Celda celda)
	{
		
		//Si soy esquina superior izquierda
		if(celda.fila == 0 && celda.columna == 0)
		{
			
			celda.Vecinos.add(dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
			celda.Vecinos.add(dungeon[celda.fila + 1][0]); //Vecino por el Sur
		}
		
		//Si soy esquina superior derecha
		else if(celda.columna == celda.max_columna && celda.fila == 0)
		{
			celda.Vecinos.add(dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
			celda.Vecinos.add(dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
		}
		
		//Si soy esquina inferior derecha
		else if(celda.fila == celda.max_fila && celda.columna == celda.max_columna)
		{
			celda.Vecinos.add(dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
			celda.Vecinos.add(dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
		}
		//si soy esquina inferior izquierda
		else if(celda.fila == celda.max_fila && celda.columna == 0)
		{
			celda.Vecinos.add(dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
			celda.Vecinos.add(dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
		}
		//Si soy lado derecho
		else if(celda.columna == celda.max_columna && (celda.fila > 0 && celda.fila < celda.max_fila))
		{
			celda.Vecinos.add(dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
			celda.Vecinos.add(dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
			celda.Vecinos.add(dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
		}
		//Si soy lado izquierdo
		else if(celda.columna == 0 && (celda.fila > 0 && celda.fila < celda.max_fila))
		{
			celda.Vecinos.add(dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
			celda.Vecinos.add(dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
			celda.Vecinos.add(dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
		}
		//Si soy lado norte
		else if(celda.fila == 0 && (celda.columna > 0 && celda.columna < celda.max_columna))
		{
			celda.Vecinos.add(dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
			celda.Vecinos.add(dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
			celda.Vecinos.add(dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
		}
		//Si soy lado sur
		else if(celda.fila == celda.max_fila && (celda.columna > 0 && celda.columna < celda.max_columna))
		{
			celda.Vecinos.add(dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
			celda.Vecinos.add(dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
			celda.Vecinos.add(dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
		}
		//Si estoy por el medio
		else if((celda.fila > 0 && celda.fila < celda.max_fila) && (celda.columna > 0 && celda.columna < celda.max_columna))
		{
			celda.Vecinos.add(dungeon[celda.fila][celda.columna + 1]); //Vecino por el Este
			celda.Vecinos.add(dungeon[celda.fila][celda.columna - 1]); //Vecino por el Oeste
			celda.Vecinos.add(dungeon[celda.fila - 1][celda.columna]); //Vecino por el Norte
			celda.Vecinos.add(dungeon[celda.fila + 1][celda.columna]); //Vecino por el Sur
		}
		
		return celda.Vecinos;
	}
	
	
	/**
	 * Funcion que realiza la busqueda del camino optimo entre la celda origen y la meta
	 * @param x_inicio 
	 * @param y_inicio
	 * @param x_final
	 * @param y_final
	 */
	public void llegada_optima(int x_inicio, int y_inicio, int x_final, int y_final)
	{
		//reseteamos los booleanos de salida, meta y camino de todo el dungoen primero
		ResetearDungeonCamino();
		
		//reseteo la distancia
		//distancia = 0;
		
		boolean no_camino = false;
		
		//Creamos dos celdas nuevas 
		 Celda celda_inicio = dungeon[x_inicio][y_inicio];
		Celda celda_final = dungeon[x_final][y_final];
		
		//ponemos a true las celdas de salida y de meta a true 
		celda_inicio.inicio= true;
		celda_final.destino= true; 
		
		
		//Nos creamos dos listas de las celdas abiertas y cerradas para poder ir recorriendo el dungeon e ir guardando las celdas 
		//que estan abiertas(no hemos pasado pero si por un vecino de ellas) y las cerradas(por las q hemos entrado)
		
		ArrayList<Celda> celdas_abiertas = new ArrayList<Celda>();
		ArrayList<Celda> celdas_cerradas = new ArrayList<Celda>();
		
		
		//anadimos la celda inicial en la lista de abiertas
		celdas_abiertas.add(celda_inicio);
		
		
		//Si la lista de celdas abiertas no esta vacia entonces seguimos en el bucle
		while(!celdas_abiertas.isEmpty())// Devuelve True si el ArrayList esta vacio. Sino Devuelve False
		{
			//Se calcula el coste de ir desde la celda donde nos encontramos hasta la siguiente
			int coste = celdas_abiertas.get(0).coste(celda_inicio.fila,celda_inicio.columna,celda_final.fila,celda_final.columna,celdas_abiertas.get(0).fila,celdas_abiertas.get(0).columna);
			int posicion = 0;
			
			//calculamos el coste de todas las celdas que estan en abierto para ver cual es la de menor coste
			for(int i= 0; i<celdas_abiertas.size(); i++)
			{
				int coste_provisional= celdas_abiertas.get(i).coste(celda_inicio.fila,celda_inicio.columna,celda_final.fila,celda_final.columna,celdas_abiertas.get(i).fila,celdas_abiertas.get(i).columna);
				int posicion_provisional = i;
				
				//si el coste provisional es menor que el coste calculado se guarda la posicion de la celda en el array y su coste 
				//siempre y cuando no sea una celda que no se va a poder transitar
				// IMPORTANTE NO PONER <= PORQUE SINO SE PISA UN CAMINO MEJOR
				if(coste_provisional < coste ) //&& celdas_abiertas.get(i).genotipo_celda[0] != 1 && celdas_abiertas.get(i).genotipo_celda[1] != 1 && celdas_abiertas.get(i).genotipo_celda[2] != 1) 
				{
					coste= coste_provisional;
					posicion= posicion_provisional;
				}
				
			}
			
			//guardamos la celda actual y la marcamos como que tiene el mejor coste
			Celda celda_A= celdas_abiertas.get(posicion);
			
			//si la celda A es el destino se acaba el algoritmo, si no es as’, se a–ade a closed
			if(celda_A == celda_final)
			{
				break; //se sale del bucle while
			}
			else
			{
				celdas_cerradas.add(celda_A);//a–adimos la celda A a cerradas
				celdas_abiertas.remove(celda_A);//borramos la celda A de abiertas
				
				//guardo la lista de vecinos de la celda 
				ArrayList<Celda> vecinos =  getlistaVecinos(celda_A);
				
				if(!vecinos.isEmpty()) //Si la lista de vecinos transitables no esta vacia calculo el coste de transitar hacia el
				{
					for(Celda mi_vecino : vecinos)//para cada vecino
					{
						if(!celdas_cerradas.contains(mi_vecino))// si V no esta en la lista de cerradas 
						{
							int coste_provisional = mi_vecino.coste(celda_inicio.fila,celda_inicio.columna,celda_final.fila,celda_final.columna,mi_vecino.fila,mi_vecino.columna);
							int coste_celda_A = celda_A.coste(celda_inicio.fila,celda_inicio.columna,celda_final.fila,celda_final.columna,celda_A.fila,celda_A.columna);
							
							// Si la lista de celdas abiertas no contiene el vecino o el coste provisional es menor al coste de la celda A,
							// entonces entramos en este if
							if(!celdas_abiertas.contains(mi_vecino) || coste_provisional < coste_celda_A)	
							{
								//guardamos las posiciones de x e y en el vecino de la celda precursora (A)
								mi_vecino.Posicion_precursor[0]= celda_A.fila;
								mi_vecino.Posicion_precursor[1]= celda_A.columna;
								
								// Si la lista de celdas abiertas no contiene al vecino, lo a–adimos a esta
								if(!celdas_abiertas.contains(mi_vecino))
								{
									celdas_abiertas.add(mi_vecino);
								}
							}
						}

					}	
				}
				
				else //Si no tenemos ningun vecino transitable entonces no hay camino
				{
					no_camino = true;
					break;
				}
			}	
						
		}
		
		if(no_camino == false) // si existe un camino entonces lo recorro
		{
			System.out.print("Hay camino");
			//Recorremos el camino optimo empezando desde la celda final
			RecorrerCamino(celda_final);	
		}
		
		//Reseteamos los booleanos de salida, meta y camino de todo el dungoen primero
		ResetearDungeonCamino();
		
	}

	
	/**
	 * Funcion calcula la distancia entre las puertas y los tesoros guardando un arraylist con las distancias
	 * @param Poblacion
	 * @return 
	 */
	public void calcular_distancias_PT(int numero_puertas, int numero_tesoros)
	{
		//Se inicializan los arraylist
		puertas_distancias = new ArrayList<Integer>();
		tam_dist = new ArrayList<Integer>();
		
		
		//Variable para guardar la distancia entre una puerta y un tesoro
		int distancia_ = 0;
		distancia = 0;
		
		//Variable para guardar la distancia del tesoro mas cercano a la puerta (se inicializa al mayor numero de movimientos que podria hacer que es f x c )
		int distancia_minima = f * c;
		
		//Variable para guardar las caracteristicas de la celda donde se encuentra el T mas cercano a la P
		Celda posicion_T_minima = null;
		
		//Se calculan las distancias entre cada puerta con cada tesoro
		for(int contador_puertas = 0; contador_puertas < numero_puertas; contador_puertas++)//por cada puerta
		{
			
			//Para cada tesoro con respecto a una puerta se guarda su distancia
			for(int contador_tesoros = 0; contador_tesoros < numero_tesoros; contador_tesoros++)
			{
				
				//Se calcula la distancia de cada puerta con cada tesoro
				llegada_optima(posicion_puertas.get(contador_puertas).fila, posicion_puertas.get(contador_puertas).columna, posicion_tesoros.get(contador_tesoros).fila,posicion_tesoros.get(contador_tesoros).columna);
				
				
				distancia_ = distancia;
				//se anade una posicion con valor 0 por cada tesoro que haya en el mapa para guardar su distancia con la puerta
				tam_dist.add(distancia_);
				
				//Si la distancia calculada es inferior a la distancia minima almacenada se guardan los datos
				if(distancia_ < distancia_minima && distancia > 0)
				{
					//Se iguala la distancia minima con la calculada
					distancia_minima = distancia_;
					
					//Se guarda la posicion del tesoro
					posicion_T_minima = posicion_tesoros.get(contador_tesoros);
				}
				
				//Se resetea la distancia
				distancia_ = 0;
				distancia = 0;
			}
			
			//Se anaden las distancias de cada tesoro con respecto a la puerta i
			puertas_distancias.addAll(tam_dist);
				
			
			//Guardo en un arraylist las posicion del tesoro mas cercano a la puerta
			posiciones_T_cercanos.add(posicion_T_minima);
			
			//Guardo en un arraylist las distancias minimas
			distancia_min.add(distancia_minima);
			
			
			//Reseteo la celda que me he guardado, la distancia mínima y las distancias para la siguiente puerta
			posicion_T_minima = null;
			distancia_minima = f * c;
			tam_dist.clear();
			
		}
		
		
		//Anado las distancias a una variable que va a guardar todo y en el individuo y elimino esa lista para que no de errores en el siguiente individuo
		puertas_distancias_poblacion.addAll(puertas_distancias);
		distancias_PT = puertas_distancias;
	
	}
	
	
	/**
	 * Funcion que calcula el fitness del individuo que recibe
	 * @param Individuo individuo de la poblacion que recibe
	 * @return Se devuelve el individuo con el fitness ya calculado
	 */
	public void calcularfitness() 
	{
		
		
		//Variable que va a guardar la media de distancias que tienen las puertas con el tesoro mas cercano a ellas
		double media_distancias = 0.0;
		
		
		//Calcular la media de las distancias del individuo de PT.
		
		for(int contador_distancias = 0; contador_distancias < distancia_min.size(); contador_distancias++)
		{
			/*LOG
			System.out.println(" ");
			System.out.println("Distancia " + contador_distancias +":");
			System.out.println(distancia_min.get(contador_distancias));
			*/
			media_distancias = distancia_min.get(contador_distancias) + media_distancias;
		}
		
		//Calculo la media
		media_distancias = media_distancias / (distancia_min.size());
		
		//Guardo la media en el individuo
		media_distancias_PT =  media_distancias;
		
		//Anadir la media al fitness.
		fitness = media_distancias_PT;
		
		//TODO Calcular cuantos monstruos hay entre el recorrido de P T del T mas cercano a P.
		
		//TODO Anadir al fitness cuantos monstruos hay en el camino de cada P del individuo y restar 1 monstruo por cada camino que no tenga un monstruo delante entre la P y el T
		//TODO Comprobar cuantos monstruos del total hay en las lineas ya que pueden haber 4 puertas y 3 monstruos y puede haber una linea que nunca tenga un monstruo delante.
		
		
	}
	
	
	
	/** 
     *	Funcion que crea un dungeon de las dimensiones f c
     */
	public void inicializarDungeon()
	{
		
		//creamos el dungeon con las dimensiones que nos han pasado para saber las dimensiones totales
		dungeon = new Celda[f][c];
		
		
		//Se inicializa el genotipo 
		int tamano_genotipo = f * c * tipo_celdas;
		genotipo = new int [tamano_genotipo];
		
		/*LOG
		System.out.println("Tamano genotipo: " + genotipo.length);
		System.out.println(" ");
		*/
		
		//Variables para saber en que celda me encuentro y para poder calcular asi la posicion en la que me encuentro del genotipo
		int contador_celda = 0;
		int posicion = 0;
		
		//Inicializamos el dungeon (sin pintarlo).
		for(int i = 0; i <= dungeon.length-1; i++)
		{
			for(int j = 0; j <= dungeon[i].length-1; j++)
			{
				
				//Variable que va a guardar el genotipo de la celda en la que nos encontramos
				int[] genotipo_celda = new int[tipo_celdas];
				
				//Para cada bit del genotipo de cada celda genero un numero aleatorio (0 o 1)
				for(int n_tipo_celdas = 0; n_tipo_celdas < tipo_celdas; n_tipo_celdas++)
				{
					//Se crea un random entre el 0 y el 1 inclusives 
					int random_genotipo = new Random().nextInt(((1 - 0) + 1) + 0);
					
					posicion = (contador_celda * tipo_celdas) + n_tipo_celdas;
					//guardo el bit creado en el array con todos los bits del individuo
					genotipo[posicion] = random_genotipo;

					//Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
					genotipo_celda[n_tipo_celdas] = genotipo[posicion];
					
				}
				
				/*LOG
				System.out.println("Genotipo de la celda: " + contador_celda);
				System.out.print(genotipo_celda[0]);
				System.out.print(genotipo_celda[1]);
				System.out.print(genotipo_celda[2]);
				System.out.println(" ");
				*/
				
				
				//creamos una celda con las posiciones que recibimos y le ponemos que es falsa la visita, tambien se le envía el genotipo que va a tener la celda
				dungeon[i][j] = new Celda(i, j, f-1, c-1, false, genotipo_celda);
				
				
				//Incremento el contador de la celda
				contador_celda++;
			}
		}
		
	}


	/**
	 * Funcion que devuelve una lista de los vecinos transitables de la celda en la que nos encontramos
	 * @param celda_actual celda donde nos encontramos
	 * @return lista vecinos transitables de la celda actual
	 */
	public ArrayList<Celda> getlistaVecinos(Celda celda_actual)
	{
	//Nos creamos un arraylist de celda para guardar los vecinos de la celda que recibimos 
		ArrayList<Celda> vecinos_celda_actual = new ArrayList<Celda>();
		
		// Para cada vecino de la celda actual
		for(Celda tipo_celda : celda_actual.Vecinos)//recorrer la lista de paredes de la celda actual
		{
			// Si el vecino es de tipo muro continuo,sino es una celda que se puede transitar
			if(tipo_celda.genotipo_celda[0] == 1 && tipo_celda.genotipo_celda[1] == 1 && tipo_celda.genotipo_celda[2] == 1)
			{
				continue;
			}
			else
			{
				// nos creamos un array unidimensional de integers que va a guardar las coordenadas de la siguiente celda a la que vamos a transitar
				int [] siguientePosicion = new int[2];
				siguientePosicion[0] =	(tipo_celda.fila - celda_actual.fila) + celda_actual.fila;
				siguientePosicion[1] = (tipo_celda.columna - celda_actual.columna) + celda_actual.columna;
				// A–adimos a la lista de vecinos posibles a los que podemos transitar pas‡ndole las coordenadas que hemos recibido anteriormentes
				vecinos_celda_actual.add(dungeon[siguientePosicion[0]][siguientePosicion[1]]);
			}
		}
		return vecinos_celda_actual; // devolvemos el vecino al que podriamos transitar 
	
	}

	
	/**
	 * Funcion que va recorriendo el camino que hemos hecho desde la meta hasta la celda inicial
	 * @param posicion_meta posicion final
	 */
	public void RecorrerCamino (Celda posicion_meta)
	{
		// Creamos una celda y guardamos la posicion de meta en ella ya que empezamos por el final
		Celda celda_camino = posicion_meta;
		
		distancia = 0;
		
		// mientras la celda camino no tenga la variable boolean posicion inicio a true no nos salimos, pues todavia no hemos terminado de recorrer el 
		// camino hasta llegar a la posicion de inicio viniendo desde la posicion de meta
		while(celda_camino.inicio == false)
		{
			//Nos creamos una celda que la igualamos a la celda precursora de la celda camino
			Celda celda_precursora = dungeon[celda_camino.Posicion_precursor[0]][celda_camino.Posicion_precursor[1]];
			
	
			//Si la celda donde nos encontramos no es ni el destino ni el inicio ( teniendo en cuenta que recorremos el camino al reves, entonces contamos como 
			//que esa celda es un camino
			if(!celda_camino.destino &&  !celda_camino.inicio)
			{
				
				//si la siguiente posicion no es la de inicio (puerta) entonces contamos que esta celda donde nos encontramos es un movimiento
				distancia++;
			}
			
			celda_camino = celda_precursora; // continuamos con el recorrrido
		}
		
	}
	
	/** 
     *	Funcion que recorre el dungeon y va reseteando el camino que hayas hecho anteriormente para volver a calcular un camino PT
     */
	public void ResetearDungeonCamino()
	{
		for(int i = 0; i <= dungeon.length-1; i++)
		{
			for(int j = 0; j <= dungeon[i].length-1; j++)
			{
				//resetamos los booleanos y los ponemos a false de nuevo por si estamos volviendo de nuevo a pasar por este dungeon
				dungeon[i][j].inicio = false;
				dungeon[i][j].camino = false;
				dungeon[i][j].destino = false;
			}
		}
	}
	
	/** 
     *	Funcion que pinta el dungeon en ASCI
     */
	public void pintar() 
	{
		
		//TODO EDITAR LA FORMA EN QUE PINTA LAS HABITACIONES
		
		for(int i = 0; i <= dungeon.length-1; i++)
		{
			//Pintamos los techos con este for
			for(int j = 0; j <= dungeon[i].length; j++)
			{
				// si nos encontramos en la fila inicial, todas tienen techos cerrados, pues no se puede abrir esas paredes
				if(j < dungeon[i].length && i == 0)
				{
					System.out.print("+---");
				}
				
				// si estamos en otra fila que no sea la primera entramos en este if
				if(j < dungeon[i].length && i != 0)
				{
					//pintamos un mas cada vez que entramos para separar las celdas y poner las esquinas de cada una con un mas
					System.out.print("+");
					
					// si la pared norte de la celda en la que estamos est‡ abierta entonces pintamos espacios, sino pintamos lineas para mostrar que est‡ cerrada
					System.out.print("---");
					
					
				}
				
				// si hemos llegado al final de la fila pintamos el mas final
				if(j == dungeon[i].length)
				{
					System.out.println("+");
				}
			}
			
			//Pintamos las paredes con este for
			for(int j = 0; j <= dungeon[i].length; j++)
			{
				
				// si nos encontramos en el lado de la izquierda pintamos pared y espacios o, dependiendo de si es la celda salida, meta o camino pintamos una letra representativa en vez de un espacio en esa celda
				if(j < dungeon[i].length && j == 0)
				{
					if(dungeon[i][j].genotipo_celda[0] == 0 && dungeon[i][j].genotipo_celda[1] == 0 && dungeon[i][j].genotipo_celda[2] == 0)//Si es una celda vacia no pintamos nada
					{
						System.out.print("|   ");
					}
					else if(dungeon[i][j].genotipo_celda[0] == 0 && dungeon[i][j].genotipo_celda[1] == 0 && dungeon[i][j].genotipo_celda[2] == 1)//Si es una celda que tiene un gigante se pinta una G
					{
						System.out.print("| G ");
					}
					else if(dungeon[i][j].genotipo_celda[0] == 0 && dungeon[i][j].genotipo_celda[1] == 1 && dungeon[i][j].genotipo_celda[2] == 0)//Si es una celda que tiene un murcielago se pinta una M
					{
						System.out.print("| M ");
					}
					else if(dungeon[i][j].genotipo_celda[0] == 0 && dungeon[i][j].genotipo_celda[1] == 1 && dungeon[i][j].genotipo_celda[2] == 1)//Si es una celda que tiene una moneda se pinta una C
					{
						System.out.print("| C ");
					}
					
					else if(dungeon[i][j].genotipo_celda[0] == 1 && dungeon[i][j].genotipo_celda[1] == 0 && dungeon[i][j].genotipo_celda[2] == 0) //Si es una celda que tiene un soldado se pinta una S
					{
						System.out.print("| S ");
					}
					
					else if(dungeon[i][j].genotipo_celda[0] == 1 && dungeon[i][j].genotipo_celda[1] == 0 && dungeon[i][j].genotipo_celda[2] == 1)//Si es una celda que tiene una llave se pinta una L
					{
						System.out.print("| L ");
					}
					else if(dungeon[i][j].genotipo_celda[0] == 1 && dungeon[i][j].genotipo_celda[1] == 1 && dungeon[i][j].genotipo_celda[2] == 0)//Si es una celda que tiene una piedra preciosa se pinta una P
					{
						System.out.print("| P ");
					}
					
					else if(dungeon[i][j].genotipo_celda[0] == 1 && dungeon[i][j].genotipo_celda[1] == 1 && dungeon[i][j].genotipo_celda[2] == 1) //Si es una celda que tiene muro se pinta una O
					{
						System.out.print("| * ");
					}
				}
				
				// si nos encontramos por el medio de la fila y no estamos en el lado izquierdo tenemos que ver si las paredes que tiene estan abiertas o no
				if(j < dungeon[i].length && j != 0)
				{
					if(dungeon[i][j].genotipo_celda[0] == 0 && dungeon[i][j].genotipo_celda[1] == 0 && dungeon[i][j].genotipo_celda[2] == 0)//Si es una celda vacia no pintamos nada
					{
						System.out.print("|   ");
					}
					else if(dungeon[i][j].genotipo_celda[0] == 0 && dungeon[i][j].genotipo_celda[1] == 0 && dungeon[i][j].genotipo_celda[2] == 1)//Si es una celda que tiene un gigante se pinta una G
					{
						System.out.print("| G ");
					}
					else if(dungeon[i][j].genotipo_celda[0] == 0 && dungeon[i][j].genotipo_celda[1] == 1 && dungeon[i][j].genotipo_celda[2] == 0)//Si es una celda que tiene un murcielago se pinta una M
					{
						System.out.print("| M ");
					}
					else if(dungeon[i][j].genotipo_celda[0] == 0 && dungeon[i][j].genotipo_celda[1] == 1 && dungeon[i][j].genotipo_celda[2] == 1)//Si es una celda que tiene una moneda se pinta una C
					{
						System.out.print("| C ");
					}
					
					else if(dungeon[i][j].genotipo_celda[0] == 1 && dungeon[i][j].genotipo_celda[1] == 0 && dungeon[i][j].genotipo_celda[2] == 0) //Si es una celda que tiene un soldado se pinta una S
					{
						System.out.print("| S ");
					}
					
					else if(dungeon[i][j].genotipo_celda[0] == 1 && dungeon[i][j].genotipo_celda[1] == 0 && dungeon[i][j].genotipo_celda[2] == 1)//Si es una celda que tiene una llave se pinta una L
					{
						System.out.print("| L ");
					}
					else if(dungeon[i][j].genotipo_celda[0] == 1 && dungeon[i][j].genotipo_celda[1] == 1 && dungeon[i][j].genotipo_celda[2] == 0)//Si es una celda que tiene una piedra preciosa se pinta una P
					{
						System.out.print("| P ");
					}
					
					else if(dungeon[i][j].genotipo_celda[0] == 1 && dungeon[i][j].genotipo_celda[1] == 1 && dungeon[i][j].genotipo_celda[2] == 1) //Si es una celda que tiene muro se pinta una O
					{
						System.out.print("| * ");
					}
						
						
					
				}
				
				// si hemos llegado al final de la fila pintamos la barra final y un salto de linea para seguir pintando en la siguiente linea
				if(j == dungeon[i].length)
				{
					System.out.println("|");
				}
				
			}
			// si las x son igual a la dimension del dungeon -1, es decir, que hemos llegado a la ultima fila del dungeon, pintamos el suelo del dungeon que va a estar cerrado 
			if(i == dungeon.length-1)
			{
				for(int h = 0; h < dungeon[i].length; h++)
				{
					System.out.print("+---");

					if(h == dungeon[i].length-1)
					{
						System.out.println("+");
					}
				}
			}
			
		}
		
		System.out.println("Genotipo del individuo: ");
		int posicion = 0;
		for(int celda = 0; celda < (genotipo.length / tipo_celdas); celda ++)
		{
			for(int cont = 0; cont < tipo_celdas ; cont ++)
			{
				System.out.print(genotipo[posicion]);
				posicion++;
			}
			System.out.print(" ");
			
		}
		System.out.println(" ");
	
	
	} // Cierra la funcion pintar
	
	
}//Cierra la clase
