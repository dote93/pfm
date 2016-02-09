package Dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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
 * 			Profesor: Jose Maria Font y Daniel Manrique
 * 			Fecha: 01-02-2016
 */

public class Dungeon 
{
	int f;
	int c;
	int filas_iniciales= 0;
	int columnas_iniciales=0;
	public Celda [][] dungeon;
	
	
	ArrayList<Pared> lista_opuesta = new ArrayList<Pared>();
	
	
	//booleano para saber si ya se ha llegado a la meta y si es asi ya no se tiene que seguir pasando por la funcion
	boolean llegada= false;
	
	/** 
     *	Constructor de Dungeon
     */
	public Dungeon(int _x, int _y )
	{
		f= _x;
		c= _y;
		
		inicializarDungeon(); // Inicializamos el dungeon para crear primero las dimensiones que va a tener el mapa
		generateDungeon(filas_iniciales, columnas_iniciales); // generamos el dungeon empezando por la posicion 0 0
		
		// pintamos el dungeon que hemos generado
		//pintar(f, c);
		
	}
	
	/** 
     *	Función que genera el dungeon del tamaño que nos han dado y va abriendo las puertas entre las celdas
     */
	/*public void generateDungeon(int _x, int _y) 
	{
		Celda inicio = dungeon[_x][_y]; //guardamos las coordenadas de la celda en la que nos encontramos
		
		inicio.visitada = true;//marcamos la celda como visitada
		
		//Guardamos todas las celdas vecinas de la celda actual recorriendo la lista
		ArrayList<Pared> vecinos = inicio.getLista();
		Collections.reverse(vecinos);//le damos la vuelta a los vecinos que recibimos
		Collections.shuffle(vecinos);//desordenamos los vecinos para generar el siguiente vecino random
		//le damos la vuelta y luego le damos a desordenar para que haya menos probabilidades de que salga igual el mapa
		
		//Recorremos la lista de vecinos para comprobar cuales han sido visitados y cuales no y el primero que no esté
		//visitado lo visitamos
		Iterator<Pared> it =vecinos.iterator();
		//Mientras haya un vecino siguiente en la lista por el cual no hayamos pasado seguimos dentro del while
		while(it.hasNext())
		{
			//Cojo la primera vecina de la lista de vecinas.
			Pared currentPared = it.next();//guardamos la pared del vecino que tenemos como siguiente
			
			//Guardo una referencia a la celda vecina.
			int [] siguientePosicion = currentPared.movement(_x, _y, currentPared.direction);
			Celda neighbour = dungeon[siguientePosicion[1]][siguientePosicion[0]];
			
			if(neighbour.visitada)
			{
				//Si ha sido visitada continuo sin hacer nada, pues es una celda que ya ha sido abierta por algun lado
				continue;
			}
			else
			{
				//Pared vecino = neighbour.UnionParedes(currentPared.getDirection());
				
				//Pared opuesta = new Pared(currentPared.direction, false);
				
				Pared.Direcciones opuesta = null;
						
				//Compruebo cual es la celda opuesta
				if(currentPared.getDirection() == Pared.Direcciones.ESTE)
				{
					opuesta = Pared.Direcciones.OESTE;
				}
				
				else if(currentPared.getDirection() == Pared.Direcciones.OESTE)
				{
					opuesta = Pared.Direcciones.ESTE;
				}
				
				else if(currentPared.getDirection() == Pared.Direcciones.NORTE)
				{
					opuesta = Pared.Direcciones.SUR;
				}
				
				else if(currentPared.getDirection() == Pared.Direcciones.SUR)
				{
					opuesta = Pared.Direcciones.NORTE;
				}
				
				
				
				//Lista con todas las direcciones de las puertas de la celda siguiente
				lista_opuesta = dungeon[neighbour.fila][neighbour.columna].getLista();
				
				
				
				//Para cada elemento de la lista de la siguiente posicion busco donde se encuentra la pared opuesta con respecto a la posicion donde me 
				//encuentro
				for (int num_dir_opuesta = 0; num_dir_opuesta < lista_opuesta.size(); num_dir_opuesta++)
				{

					
					//Si la direccion opuesta coincide con la opuesta de la pared que hemos seleccionado de la celda donde nos encontramos
					//entonces abrimos esa pared
					if(lista_opuesta.get(num_dir_opuesta).direction == opuesta)
					{
						//Abro la puerta de la celda de al lado
						lista_opuesta.get(num_dir_opuesta).open = true;
						
						//Abro la puerta de la celda donde me encuentro
						//lista.get(elemento).open = true;
						//quito paredes tanto de la celda donde me encuentro como de la vecina a la que estoy accediendo
						currentPared.open = true;
					}
				}
				//vecino.open = true;
				

				//Marco el vecino como visitado, puesto que va a ser nuestra siguiente posicion
				generateDungeon(neighbour.fila, neighbour.columna);
			}
			
		}

	}
	*/

	public void generateDungeon(int _x, int _y) 
	{
		Celda inicio = dungeon[_x][_y]; //guardamos las coordenadas de la celda en la que nos encontramos
		
		inicio.visitada = true;//marcamos la celda como visitada
		
		//Guardamos todas las celdas vecinas de la celda actual recorriendo la lista
		ArrayList<Pared> vecinos = inicio.getLista();
		Collections.reverse(vecinos);//le damos la vuelta a los vecinos que recibimos
		Collections.shuffle(vecinos);//desordenamos los vecinos para generar el siguiente vecino random
		//le damos la vuelta y luego le damos a desordenar para que haya menos probabilidades de que salga igual el mapa
		
		//Recorremos la lista de vecinos para comprobar cuales han sido visitados y cuales no y el primero que no esté
		//visitado lo visitamos
		Iterator<Pared> it =vecinos.iterator();
		//Mientras haya un vecino siguiente en la lista por el cual no hayamos pasado seguimos dentro del while
		while(it.hasNext())
		{
			//Cojo la primera vecina de la lista de vecinas.
			Pared currentPared = it.next();//guardamos la pared del vecino que tenemos como siguiente
			
			//Guardo una referencia a la celda vecina.
			int [] siguientePosicion = currentPared.movement(_x, _y);
			Celda neighbour = dungeon[siguientePosicion[0]][siguientePosicion[1]];
			
			if(neighbour.visitada)
			{
				//Si ha sido visitada continuo sin hacer nada, pues es una celda que ya ha sido abierta por algún lado
				continue;
			}
			else
			{
				//quito paredes tanto de la celda donde me encuentro como de la vecina a la que estoy accediendo
				currentPared.open = true;
				Pared vecino = neighbour.UnionParedes(currentPared.getDirection());
				vecino.open = true;
				

				//Marco el vecino como visitado, puesto que va a ser nuestra siguiente posicion
				generateDungeon(neighbour.fila, neighbour.columna);
			}
			
		}

	}
	
	/** 
     *	Función que realiza la busqueda del camino optimo entre la celda origen y la meta
     */
/*	public void llegada_optima(int x_inicio, int y_inicio, int x_final, int y_final)
	{
		//reseteamos los booleanos de salida, meta y camino de todo el dungoen primero
		ResetearDungeonCamino();
		
		//Creamos dos celdas nuevas 
		Celda celda_inicio = dungeon[x_inicio][y_inicio];
		Celda celda_final = dungeon[x_final][y_final];
		
		//ponemos a true las celdas de salida y de meta a true 
		celda_inicio.posicion_salida= true;
		celda_final.posicion_meta= true;
		
		
		//Nos creamos dos listas de las celdas abiertas y cerradas para poder ir recorriendo el dungeon e ir guardando las celdas 
		//que estan abiertas(no hemos pasado pero si por un vecino de ellas) y las cerradas(por las q hemos entrado)
		
		ArrayList<Celda> celdas_abiertas = new ArrayList<Celda>();
		ArrayList<Celda> celdas_cerradas = new ArrayList<Celda>();
		
		
		//añadimos la celda inicial en la lista de abiertas
		celdas_abiertas.add(celda_inicio);
		
		
		//Si la lista de celdas abiertas no esta vacía entonces seguimos en el bucle
		while(!celdas_abiertas.isEmpty())// Devuelve True si el ArrayList esta vacio. Sino Devuelve False
		{
			int coste = celdas_abiertas.get(0).coste(celda_inicio.fila,celda_inicio.columna,celda_final.fila,celda_final.columna,celdas_abiertas.get(0).fila,celdas_abiertas.get(0).columna);
			int posicion = 0;
			
			//calculamos el coste de todas las celdas que estan en abierto para ver cual es la de menor coste
			for(int i= 0; i<celdas_abiertas.size(); i++)
			{
				int coste_provisional= celdas_abiertas.get(i).coste(celda_inicio.fila,celda_inicio.columna,celda_final.fila,celda_final.columna,celdas_abiertas.get(i).fila,celdas_abiertas.get(i).columna);
				int posicion_provisional = i;
				
				//si el coste provisional es mayor 
				if(coste_provisional <= coste)
				{
					coste= coste_provisional;
					posicion= posicion_provisional;
				}
				
			}
			
			//guardamos la celda actual y la marcamos como que tiene el mejor coste
			Celda celda_A= celdas_abiertas.get(posicion);
			
			//si la celda A es el destino se acaba el algoritmo, si no es así, se añade a closed
			if(celda_A == celda_final)
			{
				break; //se sale del bucle while
			}
			else
			{
				celdas_cerradas.add(celda_A);//añadimos la celda A a cerradas
				celdas_abiertas.remove(celda_A);//borramos la celda A de abiertas
				
				//guardo la lista de vecinos de la celda 
				ArrayList<Celda> vecinos =  getlistaVecinos(celda_A);
				
				
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
							
							// Si la lista de celdas abiertas no contiene al vecino, lo añadimos a esta
							if(!celdas_abiertas.contains(mi_vecino))
							{
								celdas_abiertas.add(mi_vecino);
							}
						}
					}

				}	
				
			}	
						
		}
		RecorrerCamino(celda_final);// Recorremos el camino optimo empezando desde la celda final		
	}
*/
	
	
	/** 
     *	Función que nos pinta el dungeon y tambien  nos sirve para pintar el dungeon con el A*
     */
	public void pintar() 
	{
		
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
					
					// si la pared norte de la celda en la que estamos está abierta entonces pintamos espacios, sino pintamos lineas para mostrar que está cerrada
					if(dungeon[i][j].isParedOpen(Pared.Direcciones.NORTE))
					{
						System.out.print("   ");
					}
					else
					{
						System.out.print("---");
					}
					
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
					if(dungeon[i][j].puerta)//Si es celda inical pintamos una S
					{
						System.out.print("| P ");
					}
					if(dungeon[i][j].tesoro)//Si es celda final pintamos una F
					{
						System.out.print("| T ");
					}
					if(dungeon[i][j].monstruo && dungeon[i][j].puerta == false)//Si es celda camino pintamos una O
					{
						System.out.print("| M ");
					}
					if(!dungeon[i][j].puerta && !dungeon[i][j].tesoro && !dungeon[i][j].monstruo) // si la celda no tiene a true ningun booleano representativo mostramos una barra con espacios
					{
						System.out.print("|   ");
					}
				}
				
				// si nos encontramos por el medio de la fila y no estamos en el lado iquierdo tenemos que ver si las paredes que tiene están abiertas o no
				if(j < dungeon[i].length && j != 0)
				{
					// si la pared oeste de la celda en la que estamos esta abierta, no pintamos una barra, sino que espacios o si es una celda representativa, mostramos la letra correspondiente junto con espacios
					if(dungeon[i][j].isParedOpen(Pared.Direcciones.OESTE))
					{
						if(dungeon[i][j].puerta)//Si es celda inical pintamos una S
						{
							System.out.print("  P ");
						}
						if(dungeon[i][j].tesoro)//Si es celda final pintamos una F
						{
							System.out.print("  T ");
						}
						if(dungeon[i][j].monstruo && dungeon[i][j].puerta == false)//Si es celda camino pintamos una O
						{
							System.out.print("  M ");
						}
						if(!dungeon[i][j].puerta && !dungeon[i][j].tesoro && !dungeon[i][j].monstruo) // si la celda no tiene a true ningun booleano representativo mostramos espacios
						{
							System.out.print("    ");
						}
						
					}
					else // si la tiene cerrada entonces mostramos la pared junto con lo que contiene esa celda
					{
						if(dungeon[i][j].puerta)//Si es celda inical pintamos una S
						{
							System.out.print("| P ");
						}
						if(dungeon[i][j].tesoro)//Si es celda final pintamos una F
						{
							System.out.print("| T ");
						}
						if(dungeon[i][j].monstruo && dungeon[i][j].puerta == false)//Si es celda camino pintamos una O
						{
							System.out.print("| M ");
						}
						if(!dungeon[i][j].puerta && !dungeon[i][j].tesoro && !dungeon[i][j].monstruo) // si la celda no tiene a true ningun booleano representativo mostramos una barra con espacios
						{
							System.out.print("|   ");
						}
						
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
		
	}
	
	/** 
     *	Función que crea un dungeon de las dimensiones que nos ha dado el usuario
     */
	public void inicializarDungeon()
	{
		//creamos el dungeon con las dimensiones que nos han pasado para saber las dimensiones totales
		dungeon= new Celda[f][c];
		
		//Inicializamos el dungeon (sin pintarlo).
		for(int i = 0; i <= dungeon.length-1; i++)
		{
			for(int j = 0; j <= dungeon[i].length-1; j++)
			{
				//creamos una celda con las posiciones que recibimos y le ponemos que es falsa la visita
				dungeon[i][j] = new Celda(i, j, f-1, c-1, false);
			}
		}
	}

/*
	/** 
     *	Función que devuelve una lista de los vecinos transitables de la celda en la que nos encontramos
     */
/*	public ArrayList<Celda> getlistaVecinos(Celda celda_actual)
	{
		//Nos creamos un arraylist de celda para guardar los vecinos de la celda que recibimos 
		ArrayList<Celda> vecinos_celda_actual = new ArrayList<Celda>();
		
		// Para cada pared de la celda actual
		for(Pared paredes_CA : celda_actual.lista)//recorrer la lista de paredes de la celda actual
		{
			// Si la pared está abierta
			if(paredes_CA.open)
			{
				// nos creamos un array unidimensional de integers que va a guardar las coordenadas de la siguiente celda a la que vamos a transitar
				int [] siguientePosicion = paredes_CA.movement(celda_actual.fila, celda_actual.columna);
				// Añadimos a la lista de vecinos posibles a los que podemos transitar pasándole las coordenadas que hemos recibido anteriormentes
				vecinos_celda_actual.add(dungeon[siguientePosicion[0]][siguientePosicion[1]]);
			}
		}
		return vecinos_celda_actual; // devolvemos el vecino al que podriamos transitar 
		
	}
*/	
	/** 
     *	Función que va recorriendo el camino que hemos hecho desde la meta hasta la celda inicial
     */
	public void RecorrerCamino (Celda posicion_meta)
	{
		// Creamos una celda y guardamos la posicion de meta en ella 
		Celda celda_camino = posicion_meta;
		
		// mientras la celda camino no tenga la variable boolean posicion salida a true no nos salimos, pues todavia no hemos terminado de recorrer el 
		// camino hasta llegar a la posicion de salida viniendo desde la posicion de meta
		while(celda_camino.puerta == false)
		{
			// nos creamos una celda que la igualamos a la celda precursora de la celda camino
			Celda celda_precursora = dungeon[celda_camino.Posicion_precursor[0]][celda_camino.Posicion_precursor[1]];
			// decimos que la posicion camino de la celda precursora está a true para luego poder pintar una O
			celda_precursora.monstruo= true;
			
			// si la celda precursora no tiene la posicion de salida a false, la igualamos a esta a false para luego al pintar no tener problemass
			if(celda_precursora.puerta == false)
			{
				celda_camino.puerta = false;
			}
			celda_camino= celda_precursora; // continuamos con el recorrrido
		}
		
	}
	
	/** 
     *	Función que recorre el dungeon y va reseteando el camino que hayas hecho anteriormente por si el usuario quiere volver a meter las posiciones
     *  inicial y final que no se muestre el camino que haya hecho anteriormente
     */
	public void ResetearDungeonCamino()
	{
		for(int i = 0; i <= dungeon.length-1; i++)
		{
			for(int j = 0; j <= dungeon[i].length-1; j++)
			{
				//resetamos los booleanos y los ponemos a false de nuevo por si estamos volviendo de nuevo a pasar por este dungeon
				dungeon[i][j].monstruo = false;
				dungeon[i][j].puerta = false;
				dungeon[i][j].tesoro = false;
			}
		}
	}
	
}
