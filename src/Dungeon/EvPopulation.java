package Dungeon;

import java.util.ArrayList;

import Dungeon.Pared.Direcciones;

/**
 * Clase EvPopulation que se encarga de crear y evolucionar una poblacion
 * @version 1.0
 * @author  Alvaro Valle del Pozo
 * 			TFM
 * 			Profesor: Jose Maria Font y Daniel Manrique
 * 			Fecha: 01-02-2016
 */
public class EvPopulation 
{
	
	//Variable que guarda la informacion de un mapa
	Dungeon mapa = null;
	
	//ArrayList que almacena los individuos de la poblacion
	ArrayList<Dungeon> Poblacion = new ArrayList<Dungeon>();
	
	//Variable para calcular un numero aleatorio entre el numero de filas o columnas y 0
	int min = 0;
	
	//Variables para saber si ya hay una puerta en ese lado
	boolean hay_puerta_N;
	boolean hay_puerta_S;
	boolean hay_puerta_E;
	boolean hay_puerta_O;
	
	
	
	//Arraylist que guardan las paredes de la celda donde nos encontramos y de la opuesta a la pared seleccionada
	ArrayList<Pared> lista = new ArrayList<Pared>();
	ArrayList<Pared> lista_opuesta = new ArrayList<Pared>();
	
	
	
	
	/**
	 * Constructor de EvPopulation
	 */
	public EvPopulation ()
	{

	}
	
	
	
	/**
	 * Funcion que se encarga de inicializar una poblacion con (x,y) dimensiones, x monstruos, x tesoros, x puertas y paredes aleatorias	
	 * @param f numero de filas
	 * @param c numero de columnas
	 * @param numero_poblacion individuos en la poblacion
	 * @param numero_monstruos cuantos monstruos por mapa
	 * @param numero_tesoros cuantos tesoros por mapa
	 * @param numero_puertas cuantas puertas por mapa
	 */
	public ArrayList<Dungeon> populationInitialization(int f, int c, int numero_poblacion, int numero_monstruos, int numero_tesoros, int numero_puertas, int porcentaje)
	{
		
		//Se crea un mapa por cada individuo y se anade a la poblacion
		for(int i = 0; i<numero_poblacion; i++)
		{
			
			mapa = new Dungeon(f, c); //El dungeon se pasa con las dimensiones (x, y)
			
			
			//Se inicializan las variables para saber si ya hay puerta o no en ese lado por cada individuo
			hay_puerta_N = false;
			hay_puerta_S = false;
			hay_puerta_E = false;
			hay_puerta_O = false;
			
			
			//Se anaden los monstruos al mapa
			anadir_monstruos(f, c, numero_monstruos);
			
			//LOG
			//System.out.println(" ");
			
			//Se anaden los tesoros al mapa
			anadir_tesoros(f, c, numero_tesoros);
			
			
			//LOG
			//System.out.println(" ");	
			
			//Se anaden las puertas al mapa
			anadir_puertas(f, c, numero_puertas);
			
			//LOG
			//System.out.println(" ");
			
			
			//Se abren paredes entre las puertas y los objetos
			
			
			//SE ABREN LAS PAREDES
			abrir_paredes(f, c, porcentaje);
			
			/*
			System.out.println("¿La pared esta abierta? ");
			System.out.println(mapa.dungeon[f-2][c-1].isParedOpen(Pared.Direcciones.ESTE));
			
			System.out.println("¿La pared esta abierta? ");
			System.out.println(mapa.dungeon[f-1][c-2].isParedOpen(Pared.Direcciones.SUR));
			
			*/
	
			//Se anade a la poblacion el mapa nuevo generado
			Poblacion.add(mapa);
		}
		
		//Se devuelve la poblacion creada
		return Poblacion;
	}
	
	
	
	
	/**
	 * Funcion que anade los tesoros al mapa
	 * @param f filas
	 * @param c columnas
	 * @param numero_tesoros
	 */
	public void anadir_tesoros(int f, int c, int numero_tesoros)
	{
		//Se anaden los x tesoros a cada mapa en posiciones random
		for (int tesoros=0; tesoros<numero_tesoros; tesoros++)
		{
			
			int random_x = (int)(Math.random() * (f - min) + min);
			int random_y = (int)(Math.random() * (c - min) + min);
			
			//LOG
			//System.out.println("Random fila: " + random_x + " Random columna: " + random_y + " para tesoro");
			
			
			//Si la posicion random seleccionada ya tiene un monstruo o tesoro se toma esta vuelta como nula restando el numero de vueltas dadas
			if (mapa.dungeon[random_x][random_y].monstruo == true || mapa.dungeon[random_x][random_y].tesoro == true)
			{
				tesoros = tesoros - 1;
			}
			
			//Si la posicion aleatoria no tiene un monstruo o tesoro, entonces le ponemos un tesoro
			else
			{
				mapa.dungeon[random_x][random_y].tesoro = true; 
			}
		}
	}
	
	
	
	/**
	 * Funcion que anade monstruos al mapa
	 * @param f filas
	 * @param c columnas
	 * @param numero_monstruos
	 */
	public void anadir_monstruos(int f, int c, int numero_monstruos)
	{
		//Se anaden los x monstruos a cada mapa en posiciones random
		for (int monstruos=0; monstruos<numero_monstruos; monstruos++)
		{
			
			int random_x = (int)(Math.random() * (f - min) + min);
			int random_y = (int)(Math.random() * (c - min) + min);
			
			//LOG
			//System.out.println("Random fila: " + random_x + " Random columna: " + random_y + " para monstruo");
			
			//Si la posicion random seleccionada ya tiene un monstruo se toma esta vuelta como nula restando el numero de vueltas dadas
			if (mapa.dungeon[random_x][random_y].monstruo == true)
			{
				monstruos = monstruos - 1;
			}
			
			//Si la posicion aleatoria no tiene un monstruo, entonces le ponemos uno
			else
			{
				mapa.dungeon[random_x][random_y].monstruo = true; 
			}
		}
	}
	
	
	/**
	 * Funcion que anade puertas al mapa
	 * @param f filas
	 * @param c columnas
	 * @param numero_puertas
	 */
	public void anadir_puertas(int f, int c, int numero_puertas)
	{
		//Se anaden las x puertas a cada mapa en posiciones random
		for (int puertas=0; puertas<numero_puertas; puertas++)
		{
			
			
			int random_x = (int)(Math.random() * (f - min) + min);
			int random_y = (int)(Math.random() * (c - min) + min);
			
			
			//Si la posicion random seleccionada ya tiene un monstruo o tesoro o puerta se toma esta vuelta como nula restando el numero de vueltas dadas
			if (mapa.dungeon[random_x][random_y].monstruo == true || mapa.dungeon[random_x][random_y].tesoro == true || mapa.dungeon[random_x][random_y].puerta == true)
			{
				puertas = puertas - 1;
			}
			
			//Si la posicion aleatoria no tiene un monstruo o tesoro o puerta, entonces le ponemos una puerta
			else
			{
				//LOG
				//System.out.println("Random fila: " + random_x + " Random columna: " + random_y + " para puerta");
				
				//si me encuentro en el lado Este que al pintar es el lado Norte
				if (random_x == 0 && (random_y == 0 || random_y < (c-1)) && hay_puerta_E != true)
				{
					mapa.dungeon[random_x][random_y].puerta = true;
					hay_puerta_E = true;
				}
				
				//si me encuentro en el lado Norte que al pintar es el lado Oeste
				else if ((random_x == 0 || random_x < (f-1)) && random_y == 0 && hay_puerta_N != true)
				{
					mapa.dungeon[random_x][random_y].puerta = true;
					hay_puerta_N = true;
				}
				
				//si me encuentro en el lado Oeste que al pintar es el lado Sur
				else if (random_x == (f-1) && (random_y == 0 || random_y < (c-1)) && hay_puerta_O != true)
				{
					mapa.dungeon[random_x][random_y].puerta = true;
					hay_puerta_O = true;
				}
				
				//si me encuentro en el lado Sur que al pintar es el lado Este
				else if ((random_x == 0 || random_x < (f-1)) && random_y == (c-1) && hay_puerta_S != true)
				{
					mapa.dungeon[random_x][random_y].puerta = true;
					hay_puerta_S = true;
				}
				
				//Si ya hay alguna puerta en ese lado, se vuelve a buscar una posicion random
				else
				{
					puertas = puertas -1;
				}
				 
			}
		}
	}

	
	/**
	 * Funcion que tira paredes de manera random entre las celdas
	 * @param f filas
	 * @param c columnas
	 */
	public void abrir_paredes(int f, int c, int porcentaje)
	{
		//Se abren las paredes entre las celdas de manera random
		for(int fila = 0; fila < f; fila++)
		{
			for(int columna = 0; columna < c; columna++)
			{
					
				//Se obtiene la lista con las direcciones de las paredes que podria tener la celda[fila][columna]
				lista = mapa.dungeon[fila][columna].getLista();
				

				//Para cada pared de la lista
				for(int elemento = 0; elemento < lista.size(); elemento++)
				{
					//Variable que almacena la posicion del vecino dada la posicion de la celda y la direccion de la pared
					int [] siguientePosicion = lista.get(elemento).movement(fila, columna, lista.get(elemento).direction);
					
					//LOG
					//System.out.println("Direccion de la pared " + elemento + ": " + lista.get(elemento).direction);
					
					//Se almacena la direccion del elemento x de la lista
					Direcciones direction = lista.get(elemento).direction;
					
					
					Pared.Direcciones opuesta = null;
					
					//Compruebo cual es la celda opuesta
					if(direction == Pared.Direcciones.ESTE)
					{
						opuesta = Pared.Direcciones.OESTE;
					}
					
					else if(direction == Pared.Direcciones.OESTE)
					{
						opuesta = Pared.Direcciones.ESTE;
					}
					
					else if(direction == Pared.Direcciones.NORTE)
					{
						opuesta = Pared.Direcciones.SUR;
					}
					
					else if(direction == Pared.Direcciones.SUR)
					{
						opuesta = Pared.Direcciones.NORTE;
					}
					
					
					//Lista con todas las direcciones de las puertas de la celda siguiente
					lista_opuesta = mapa.dungeon[siguientePosicion[0]][siguientePosicion[1]].getLista();
					
					
					//Para cada elemento de la lista de la siguiente posicion busco donde se encuentra la pared opuesta con respecto a la posicion donde me encuentro
					for (int num_dir_opuesta = 0; num_dir_opuesta < lista_opuesta.size(); num_dir_opuesta++)
					{
						//Genero un numero random para ver si abro de manera aleatoria esa pared o no
						int random = (int)(Math.random() * (100 - 0) + 0);
						
						//Si la direccion opuesta coincide con la opuesta de la pared que hemos seleccionado de la celda donde nos encontramos
						//y el numero random generado es 1 entonces abrimos esa pared
						if(lista_opuesta.get(num_dir_opuesta).direction == opuesta && random <= porcentaje)
						{
							//Abro la puerta de la celda de al lado
							lista_opuesta.get(num_dir_opuesta).open = true;
							
							//Abro la puerta de la celda donde me encuentro
							lista.get(elemento).open = true;
						}
					}
					
				}
		
			}
		}
	}
	
	
	/**
	 * Funcion que se encarga de pintar los mapas de los individuos 
	 * @param Poblacion
	 */
	public void pintar_poblacion(ArrayList<Dungeon> Poblacion)
	{
		
		for(int j= 0; j<Poblacion.size(); j++)
		{
			System.out.println(" ");
			System.out.println("Individuo " + j );
			Poblacion.get(j).pintar();
		}
	}
	
	/**
	 * Funcion que pinta a un individuo
	 * @param Individuo
	 */
	public void pintar_individuo(Dungeon Individuo)
	{
		//Recorro la poblacion
		for(int j= 0; j<Poblacion.size(); j++)
		{
			//Compruebo que individuo es de la poblacion para pintar en que posicion se encuentra
			if(Individuo == Poblacion.get(j))
			{
				System.out.println(" ");
				System.out.println("Individuo " + j );
				Individuo.pintar();
			}
			
		}

		
	}
}
