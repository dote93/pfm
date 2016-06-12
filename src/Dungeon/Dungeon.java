package Dungeon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import Dungeon.Celda.Tipo_puertas;

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
	
	
	public int numero_monstruos = 0; 
	public int numero_tesoros = 0; 
	
	public int celdas_Paredes = 0; //variable para saber cuantas paredes se han colocado en el mapa
	
	public int celdas_Vacias = 0; //variable para saber cuantas celdas vacias se han colocado en el mapa
	
	
	
	//Variable para guardar cuanto porcenaje queremos que haya de celdas vacias
	int porcentaje = 0;
	
	//Variable para guardar cuanto porcenaje queremos que haya de celdas pared
	int porcentaje_paredes = 0;
	
	//Variable para saber la posicion de donde estan las paredes
	ArrayList<Celda>  posicion_puertas = new ArrayList<Celda>();

	//Variable para saber la posicion de donde estan los tesoros
	ArrayList<Celda>  posicion_tesoros = new ArrayList<Celda>();
		
	//Variable para saber la posicion de donde estan los monstruos
	ArrayList<Celda>  posicion_monstruos = new ArrayList<Celda>();
	
	
	//Arraylist de las distancias minimas PT de todas las puertas
	ArrayList<Integer> distancia_min_PT = new ArrayList<Integer>();
	
	//Arraylist de las distancias minimas PM
	ArrayList<Integer> distancia_min_PM = new ArrayList<Integer>();
	
	
	//Fitness desglosado 
	
	public double[] fitness_por_partes;
	
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
	
	//Distancia entre una celda P y una T
	public int distancia = -1;
		
	//Variable para saber si el dungeon es valido o no
	public boolean dungeon_valido;
	
	
	
	/** 
     *	Constructor de Dungeon
     */
	public Dungeon(int _f, int _c, int numero_monstruos_, int numero_tesoros_,ArrayList<int[]> pos_puertas, ArrayList<Tipo_puertas> t_puertas , int numero_puertas, int porcentaje_, int porcentaje_paredes_, int tipo_celdas_)
	{
		//Se igualan las dimensiones del dungeon
		f= _f;
		c= _c;
		
		//se guarda el tipo de celdas que va a haber para saber el tamano del genotipo, tanto del individuo como de cada celda
		tipo_celdas = tipo_celdas_;
		
		porcentaje = porcentaje_;
		porcentaje_paredes = porcentaje_paredes_;
		
		celdas_Paredes = (porcentaje_paredes * (f * c)) / 100;
		celdas_Vacias = (porcentaje * (f * c)) / 100; 
		
		//Inicializamos el dungeon para crear primero las dimensiones que va a tener el mapa
		inicializarDungeon(); 
		
		/*LOG
		System.out.println("Termino inicializar dungeon");
		*/
		
		//SE ANADEN LOS OBJETOS AL MAPA***************************
		
		
		//Se anaden las puertas al mapa
		//anadir_puertas_posicion(pos_puertas, t_puertas, numero_puertas);
		anadir_puertas(t_puertas, numero_puertas);
		
		
		
		/*LOG
		System.out.println("Puertas anadidas");
		*/
		
		//Se comprueban los tesoros del mapa
		numero_tesoros = comprobar_tesoros();
		
		/*//LOG
		System.out.println(" ");
		System.out.println("tesoros colocados: " + numero_tesoros);
		*/
		
		numero_monstruos = comprobar_monstruos();
		
		/*//LOG
		System.out.println(" ");
		System.out.println("monstruos colocados: " + numero_monstruos);
		*/
		
		//Se establecen los vecinos por cada celda
		generateDungeon(0,0);
		
		/*LOG
		System.out.println(" ");
		System.out.println("Termino el generate");
		System.out.println(" ");
		*/
		
		
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
		calcularfitness(numero_puertas);
		

		
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
		
		//Variables para saber en que celda me encuentro y para poder calcular asi la posicion en la que me encuentro del genotipo
		int contador_celda = 0;
		int posicion = 0;
		
		//Variables random para crear el genotipo de una celda
		int random_porcentaje = 0;
		
		//Variables para saber cuantas celdas tienen que haber de tipo pared y de tipo vacia para que este mas o menos en el porcentaje que 
		//se espera
		int numero_celdas_Paredes = (porcentaje_paredes * (f * c)) / 100;
		
		/*
		System.out.println("Paredes: " + numero_celdas_Paredes);
		*/
		
		int numero_celdas_Vacias = (porcentaje * (f * c)) / 100;
		
		/*
		System.out.println("Vacias: " + numero_celdas_Vacias);
		*/
		//Variable que va a guardar el genotipo de la celda en la que nos encontramos
		int[] genotipo_celda = null;
		
	
		
		//reseteo la variable de celdas pared para saber cuantas se han colocado al final al igual que de las celdas en blanco
		celdas_Paredes = 0;
		celdas_Vacias = 0;
		
		//Inicializamos el dungeon (sin pintarlo).
		for(int i = 0; i < dungeon.length; i++)
		{
			for(int j = 0; j < dungeon[i].length; j++)
			{
				//Genero un numero aleatorio de 0 a 100 
				random_porcentaje = new Random().nextInt(((100 - 0) + 1) + 0);
								
				
				//Variable que va a guardar el genotipo de la celda en la que nos encontramos
				genotipo_celda = new int[tipo_celdas];
				
				//Para cada bit del genotipo de cada celda genero un numero aleatorio (0 o 1)
				for(int n_tipo_celdas = 0; n_tipo_celdas < tipo_celdas; n_tipo_celdas++)
				{
					//posicion del array del genotipo donde se tiene que guardar cada bit
					posicion = (contador_celda * tipo_celdas) + n_tipo_celdas;
					
					
					//Si el numero random creado esta dentro de 0 y porcentaje y no se han puesto todas las celdas vacias, se crea celda vacia
					if(random_porcentaje >= 0 && random_porcentaje < porcentaje && numero_celdas_Vacias != 0) //porcentaje
					{
						//Si me encuentro en el ultimo bit elimino una posible celda que tenia que estar vacia
						if (n_tipo_celdas == 2)
						{
							//Resto una celda de las vacias que deberia de colocar como maximo
							numero_celdas_Vacias--;
							
							//sumo una a las celdas vacias que se han colocado
							celdas_Vacias++;
							
							//guardo el bit creado en el array con todos los bits del individuo
							genotipo[posicion] = 0;
	
							//Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
							genotipo_celda[n_tipo_celdas] = genotipo[posicion];
						
						}
						
						//Si no es el ultimo bit entonces se pone a 0 la posicion 0 y 1
						else
						{
							//guardo el bit creado en el array con todos los bits del individuo
							genotipo[posicion] = 0;
	
							//Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
							genotipo_celda[n_tipo_celdas] = genotipo[posicion];
						}
					}
					
					//Si el numero random creado esta entre porcentaje y el porcentaje de paredes + porcentaje y puedo colocar mas paredes, se crea una pared
					else if(random_porcentaje < (porcentaje + porcentaje_paredes) &&  random_porcentaje >= porcentaje && numero_celdas_Paredes != 0)
					{
						//Si me encuentro en el ultimo bit pongo el genotipo igual a 1 para que sea 111
						if (n_tipo_celdas == 2)
						{
							//Resto una celda de las paredes que deberia de colocar como maximo
							numero_celdas_Paredes--;
							
							
							//sumo una a las celdas pared que se han colocado
							celdas_Paredes++;
							
							
							//guardo el bit creado en el array con todos los bits del individuo
							genotipo[posicion] = 1;

							//Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
							genotipo_celda[n_tipo_celdas] = genotipo[posicion];
						}
						
						//Si no es el ultimo bit entonces se pone a 1 al ultimo bit
						else
						{
							//guardo el bit creado en el array con todos los bits del individuo
							genotipo[posicion] = 1;

							//Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
							genotipo_celda[n_tipo_celdas] = genotipo[posicion];
						}
					
						
					}
					
					//Si el numero random creado esta entre (porcentaje + porcentaje_pared) y 100% o ya he colocado todas las celdas vacias o paredes posibles, entonces hacemos que sea de tipo objeto 
					else
					{
					
						//Si me encuentro en el ultimo bit del genotipo y es igual a vacio la transcripcion, entonces cambio el ultimo bit para que no sea celda vacia
						if ((genotipo_celda[0] == 0 && genotipo_celda[1] == 0) && (n_tipo_celdas == 2))
						{
							
							//guardo el bit creado en el array con todos los bits del individuo
							genotipo[posicion] = 1;

							//Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
							genotipo_celda[n_tipo_celdas] = genotipo[2];
							
							/*
							System.out.println("Reseteo1");
							System.out.println("Celda_posicion: " + (posicion / 3) );
							System.out.println("Genotipo 0: " + genotipo[posicion - 2]);
							System.out.println("Genotipo 1: " + genotipo[posicion - 1]);
							System.out.println("Genotipo 2: " + genotipo[posicion]);
							System.out.println(" ");
							*/
							
							
						}
						
						//Si el genotipo es todo 1 generamos el ultimo bit a 0 para que no sea de tipo pared
						else if((genotipo_celda[0] == 1 && genotipo_celda[1] == 1) && (n_tipo_celdas == 2))
						{
							
							//guardo el bit creado en el array con todos los bits del individuo
							genotipo[posicion] = 0;

							//Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
							genotipo_celda[n_tipo_celdas] = genotipo[posicion];
							
							/*
							System.out.println("Reseteo2");
							System.out.println("Genotipo 0: " + genotipo[posicion - 2]);
							System.out.println("Genotipo 1: " + genotipo[posicion - 1]);
							System.out.println("Genotipo 2: " + genotipo[posicion]);
							System.out.println(" ");
							*/
							
						}
						
						//Genero un bit aleatorio ya que no ha cumplido ninguna de las reglas anteriores
						else
						{
							//Se crea un random entre el 0 y el 1 (ya que devuelve un entero solo van a ser esos dos)
							int random_genotipo = new Random().nextInt(((1 - 0) + 1) + 0);
							
							//guardo el bit creado en el array con todos los bits del individuo
							genotipo[posicion] = random_genotipo;
	
							//Se guarda de manera local el genotipo creado para luego pasarle todo el genotipo entero a la celda
							genotipo_celda[n_tipo_celdas] = genotipo[posicion];
						}
					}
					
					
				}
				
				
				//creamos una celda con las posiciones que recibimos y le ponemos que es falsa la visita, tambien se le envía el genotipo que va a tener la celda
				dungeon[i][j] = new Celda(i, j, f-1, c-1, false, genotipo_celda);
				
				
				//Incremento el contador de la celda
				contador_celda++;
			}
		
		}
		//System.out.println("He terminado");
	}
	

	/**
	 * Funcion que recorre todo el dungeon para ir poniendo las celdas vecinas en cada celda del dungeon
	 * @param _x fila de la celda no visitada
	 * @param _y columna de la celda no visitada
	 */
	public void generateDungeon(int _x, int _y) 
	{
		Celda inicio = dungeon[_x][_y]; //guardamos las coordenadas de la celda en la que nos encontramos
		
		inicio.visitada = true;//marcamos la celda como visitada
		
		//Guardamos todas las celdas vecinas de la celda actual recorriendo la lista
		ArrayList<Celda> vecinos = get_Vecinos(inicio);
		
		
		//Recorremos la lista de vecinos para comprobar cuales han sido visitados y cuales no y el primero que no esta
		//visitado lo visitamos
		Iterator<Celda> it =vecinos.iterator();
		//Mientras haya un vecino siguiente en la lista por el cual no hayamos pasado seguimos dentro del while
		while(it.hasNext())
		{
			//Cojo la primera vecina de la lista de vecinas.
			Celda currentVecino = it.next();//guardamos la pared del vecino que tenemos como siguiente
			
			//Guardo una referencia a la celda vecina.
			int [] siguientePosicion = {(currentVecino.fila), (currentVecino.columna)};
			Celda neighbour = dungeon[siguientePosicion[0]][siguientePosicion[1]];
			
			
			//si el vecino ha sido visitado o es de tipo muro continuo, sino lo marco como celda visitada en la siguiente iteracion
			if(neighbour.visitada ) //|| (neighbour.genotipo_celda[0] == 1 && neighbour.genotipo_celda[1] == 1 && neighbour.genotipo_celda[2] == 1))
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
	 * Funcion que se encarga de guardar en el arraylist las celdas vecinas de la celda que recibe
	 * @param celda Celda que queremos ver sus vecinos
	 * @return se devuelve el arraylist que guarda la celda con los correspondientes vecinos
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
	 * Funcion que se encarga de poner una puerta en la posicion que recibe
	 * @param f fila donde va a estar la puerta
	 * @param c columna donde va a estar la puerta
	 */
/*	public void anadir_puertas_posicion(ArrayList<int[]> pos_puertas, ArrayList<Celda.Tipo_puertas> t_puertas, int numero_puertas)
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
*/	
	
	/**
	 * Funcion que anade puertas al mapa
	 * @param f filas
	 * @param c columnas
	 * @param numero_puertas
	 */
	public void anadir_puertas(ArrayList<Celda.Tipo_puertas> t_puertas, int numero_puertas)
	{
		
		//Se inicializan las variables para saber si ya hay puerta o no en ese lado por cada individuo
		hay_puerta_N = false;
		hay_puerta_S = false;
		hay_puerta_E = false;
		hay_puerta_O = false;
		
		//Variables para comprobar si no hay puertas de entrada_salida y salida establecerlas
		boolean p_salida = false;
		boolean p_entrada_salida = false;
		
		//Variables para generar la posicion random donde voy a colocar la puerta
		int random_x = 0;
		int random_y = 0;
		
		//Se anaden las x puertas a cada mapa en posiciones random
		for (int puertas=0; puertas<numero_puertas; puertas++)
		{
			
			random_x = 0;
			random_y = 0;
			
			//Si no hay puerta norte
			if (!hay_puerta_N)
			{
								
				random_x = 0;
				random_y = (int)(Math.random() * (c - min) + min);
				
				
				//Si tenemos el tipo de puerta que es lo establecemos
				if(t_puertas.size() != 0)
				{
					dungeon[random_x][random_y].puerta = true;
					dungeon[random_x][random_y].puerta_N = true;
					dungeon[random_x][random_y].tipo_puerta_N = t_puertas.get(puertas);
					hay_puerta_N = true;
					
					//se guarda la posicion de la puerta en el mapa
					int[] posicion = {random_x, random_y};
					posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
				
				}
				else
				{
					dungeon[random_x][random_y].puerta = true;
					hay_puerta_N = true;
					dungeon[random_x][random_y].puerta_N = true;
					
					//se guarda la posicion de la puerta en el mapa
					int[] posicion = {random_x, random_y};
					posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
					
					//Creo un random que va del 0 al 1 (Salida, Entrada-Salida) y que va a elegir de que tipo va a ser la puerta
					int random_tipo_puerta = (int)(Math.random()*(0-(2))+(2)); //se usa de 0 al 2 ya que redondea para abajo y saldria siempre 0
					
					if((numero_puertas - puertas) > 1)
					{
						if(random_tipo_puerta == 1 && !p_entrada_salida) //Si es 1 y no se ha colocado una puerta entrada_salida decimos que la puerta es de entrada_salida
						{
							dungeon[random_x][random_y].tipo_puerta_N = Tipo_puertas.ENTRADA_SALIDA;
							p_entrada_salida = true;
						}
						else //Si es 0 o ya se ha colocado una puerta de entrada_salida decimos que la puerta es de salida 
						{	
							dungeon[random_x][random_y].tipo_puerta_N = Tipo_puertas.SALIDA;
							p_salida = true;
						}
												
					}
					else //Si nos encontramos en la ultima vuelta y no se ha puesto una puerta de salida, entonces esta puerta sera de entrada_salida
					{
						if(!p_entrada_salida) //Si no hay entrada_salida ponemos una entrada_salida
						{
							dungeon[random_x][random_y].tipo_puerta_N = Tipo_puertas.ENTRADA_SALIDA;
							p_entrada_salida = true;
						}
						else //Si hay entrada_salida ponemos una salida
						{
							dungeon[random_x][random_y].tipo_puerta_N = Tipo_puertas.SALIDA;
							p_salida = true;
						}

					}
					
				}
			}
			
			//Si no hay puerta sur
			else if (!hay_puerta_S)
			{
				random_x = f - 1;
				random_y = (int)(Math.random() * (c - min) + min);
				
				
				//Si tenemos el tipo de puerta que es lo establecemos
				if(t_puertas.size() != 0)
				{
					dungeon[random_x][random_y].puerta = true;
					dungeon[random_x][random_y].tipo_puerta_S = t_puertas.get(puertas);
					hay_puerta_S = true;
					dungeon[random_x][random_y].puerta_S = true;
					
					//se guarda la posicion de la puerta en el mapa
					int[] posicion = {random_x, random_y};
					posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
				}
				else
				{
					dungeon[random_x][random_y].puerta = true;
					hay_puerta_S = true;
					dungeon[random_x][random_y].puerta_S = true;
					
					//se guarda la posicion de la puerta en el mapa
					int[] posicion = {random_x, random_y};
					posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
					
					//Creo un random que va del 0 al 1 (Salida, Entrada-Salida) y que va a elegir de que tipo va a ser la puerta
					int random_tipo_puerta = (int)(Math.random()*(0-(2))+(2)); //se usa de 0 al 2 ya que redondea para abajo y saldria siempre 0
										
					if((numero_puertas - puertas) > 1)
					{
						if(random_tipo_puerta == 1 && !p_entrada_salida) //Si es 1 y no se ha colocado una puerta entrada_salida decimos que la puerta es de entrada_salida
						{
							dungeon[random_x][random_y].tipo_puerta_S = Tipo_puertas.ENTRADA_SALIDA;
							p_entrada_salida = true;
						}
						else //Si es 0 o ya se ha colocado una puerta de entrada_salida decimos que la puerta es de salida 
						{	
							dungeon[random_x][random_y].tipo_puerta_S = Tipo_puertas.SALIDA;
							p_salida = true;
						}
												
					}
					else //Si nos encontramos en la ultima vuelta y no se ha puesto una puerta de salida, entonces esta puerta sera de entrada_salida
					{
						if(!p_entrada_salida) //Si no hay entrada_salida ponemos una entrada_salida
						{
							dungeon[random_x][random_y].tipo_puerta_S = Tipo_puertas.ENTRADA_SALIDA;
							p_entrada_salida = true;
						}
						else //Si hay entrada_salida ponemos una salida
						{
							dungeon[random_x][random_y].tipo_puerta_S = Tipo_puertas.SALIDA;
							p_salida = true;
						}

					}
				}
				
			}
			
			//Si no hay puerta este
			else if (!hay_puerta_E)
			{
				random_x = (int)(Math.random() * (f - min) + min);
				random_y = c - 1;
			
				
				//Si tenemos el tipo de puerta que es lo establecemos
				if(t_puertas.size() != 0)
				{
					dungeon[random_x][random_y].puerta = true;
					dungeon[random_x][random_y].tipo_puerta_E = t_puertas.get(puertas);
					hay_puerta_E = true;
					dungeon[random_x][random_y].puerta_E = true;
					
					//se guarda la posicion de la puerta en el mapa
					int[] posicion = {random_x, random_y};
					posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
				}
				else
				{
					dungeon[random_x][random_y].puerta = true;
					hay_puerta_E = true;
					dungeon[random_x][random_y].puerta_E = true;
					
					//se guarda la posicion de la puerta en el mapa
					int[] posicion = {random_x, random_y};
					posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
					
					//Creo un random que va del 0 al 1 (Salida, Entrada-Salida) y que va a elegir de que tipo va a ser la puerta
					int random_tipo_puerta = (int)(Math.random()*(0-(2))+(2)); //se usa de 0 al 2 ya que redondea para abajo y saldria siempre 0
					
					if((numero_puertas - puertas) > 1)
					{
						if(random_tipo_puerta == 1 && !p_entrada_salida) //Si es 1 y no se ha colocado una puerta entrada_salida decimos que la puerta es de entrada_salida
						{
							dungeon[random_x][random_y].tipo_puerta_E = Tipo_puertas.ENTRADA_SALIDA;
							p_entrada_salida = true;
						}
						else //Si es 0 o ya se ha colocado una puerta de entrada_salida decimos que la puerta es de salida 
						{	
							dungeon[random_x][random_y].tipo_puerta_E = Tipo_puertas.SALIDA;
							p_salida = true;
						}
												
					}
					else //Si nos encontramos en la ultima vuelta y no se ha puesto una puerta de salida, entonces esta puerta sera de entrada_salida
					{
						if(!p_entrada_salida) //Si no hay entrada_salida ponemos una entrada_salida
						{
							dungeon[random_x][random_y].tipo_puerta_E = Tipo_puertas.ENTRADA_SALIDA;
							p_entrada_salida = true;
						}
						else //Si hay entrada_salida ponemos una salida
						{
							dungeon[random_x][random_y].tipo_puerta_E = Tipo_puertas.SALIDA;
							p_salida = true;
						}

					}
				}
				
			}
			
			//Si no hay puerta oeste
			else if (!hay_puerta_O)
			{		
				random_x = (int)(Math.random() * (f - min) + min);
				random_y = 0;
				
				//Si tenemos el tipo de puerta que es lo establecemos
				if(t_puertas.size() != 0)
				{
					dungeon[random_x][random_y].puerta = true;
					dungeon[random_x][random_y].puerta_O = true;
					dungeon[random_x][random_y].tipo_puerta_O = t_puertas.get(puertas);
					hay_puerta_O = true;
					
					//se guarda la posicion de la puerta en el mapa
					int[] posicion = {random_x, random_y};
					posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
				}
				else
				{
					dungeon[random_x][random_y].puerta = true;
					hay_puerta_O = true;
					dungeon[random_x][random_y].puerta_O = true;
					
					//se guarda la posicion de la puerta en el mapa
					int[] posicion = {random_x, random_y};
					posicion_puertas.add(dungeon[posicion[0]][posicion[1]]);
					
					//Creo un random que va del 0 al 1 (Salida, Entrada-Salida) y que va a elegir de que tipo va a ser la puerta
					int random_tipo_puerta = (int)(Math.random()*(0-(2))+(2)); //se usa de 0 al 2 ya que redondea para abajo y saldria siempre 0
					
					if((numero_puertas - puertas) > 1)
					{
						if(random_tipo_puerta == 1 && !p_entrada_salida) //Si es 1 y no se ha colocado una puerta entrada_salida decimos que la puerta es de entrada_salida
						{
							dungeon[random_x][random_y].tipo_puerta_O = Tipo_puertas.ENTRADA_SALIDA;
							p_entrada_salida = true;
						}
						else //Si es 0 o ya se ha colocado una puerta de entrada_salida decimos que la puerta es de salida 
						{	
							dungeon[random_x][random_y].tipo_puerta_O = Tipo_puertas.SALIDA;
							p_salida = true;
						}
												
					}
					else //Si nos encontramos en la ultima vuelta y no se ha puesto una puerta de salida, entonces esta puerta sera de entrada_salida
					{
						if(!p_entrada_salida) //Si no hay entrada_salida ponemos una entrada_salida
						{
							dungeon[random_x][random_y].tipo_puerta_O = Tipo_puertas.ENTRADA_SALIDA;
							p_entrada_salida = true;
						}
						else //Si hay entrada_salida ponemos una salida
						{
							dungeon[random_x][random_y].tipo_puerta_O = Tipo_puertas.SALIDA;
							p_salida = true;
						}

					}
				}
				
			}
			
				 
		}
		
		//LOG
		/*
		System.out.println(" ");
		*/
	}
	
	
	/**
	 * Funcion que comprueba los tesoros del mapa
	 * @param f filas
	 * @param c columnas
	 * @param numero_tesoros
	 */
	public int comprobar_tesoros()
	{
		int numero_tesoros = 0;
		
		//Se comprueban los tesoros que se han anadido en el mapa
		for (int fila=0; fila < f; fila++)
		{
			
			for(int columna = 0; columna < c; columna++)
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
	 * Funcion que comprueba los tesoros del mapa
	 * @param f filas
	 * @param c columnas
	 * @param numero_tesoros
	 */
	public int comprobar_monstruos()
	{
		int numero_monstruos = 0;
		
		//Se comprueban los monstruos que se han anadido en el mapa
		for (int fila=0; fila < f; fila++)
		{
			
			for(int columna = 0; columna < c; columna++)
			{
		
				//Hay un gigante
				if(dungeon[fila][columna].genotipo_celda[0] == 0 && dungeon[fila][columna].genotipo_celda[1] == 0 && dungeon[fila][columna].genotipo_celda[2] == 1)
				{
					dungeon[fila][columna].monstruo = true; 
					posicion_monstruos.add(dungeon[fila][columna]);
					numero_monstruos++;
				}
				
				//Hay un murcielago
				else if(dungeon[fila][columna].genotipo_celda[0] == 0 && dungeon[fila][columna].genotipo_celda[1] == 1 && dungeon[fila][columna].genotipo_celda[2] == 0)
				{
					dungeon[fila][columna].monstruo = true; 
					posicion_monstruos.add(dungeon[fila][columna]);
					numero_monstruos++;
					
				}
				
				//Hay un soldado
				else if(dungeon[fila][columna].genotipo_celda[0] == 1 && dungeon[fila][columna].genotipo_celda[1] == 0 && dungeon[fila][columna].genotipo_celda[2] == 0)
				{
					dungeon[fila][columna].monstruo = true; 
					posicion_monstruos.add(dungeon[fila][columna]);
					numero_monstruos++;
				}
			}
			
		}
		return numero_monstruos;
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
		distancia = -1;
		
		
		
		
		//Si la celda de inicio es de tipo pared o la de meta entonces no hay camino
		if ((dungeon[x_inicio][y_inicio].genotipo_celda[0] == 1 && dungeon[x_inicio][y_inicio].genotipo_celda[1] == 1 && dungeon[x_inicio][y_inicio].genotipo_celda[2] == 1) || (dungeon[x_final][y_final].genotipo_celda[0] == 1 && dungeon[x_final][y_final].genotipo_celda[1] == 1 && dungeon[x_final][y_final].genotipo_celda[2] == 1) )
		{
			distancia = -1;
			//ResetearDungeonCamino();
		}
		
		//Si el destino es donde me encuentro entonces no hay que buscar ningun camino ya que nos encontramos en la meta
		else if(x_inicio == x_final && y_inicio == y_final) 
		{
			//la distancia es 0 
			distancia = 0;
			
			//ResetearDungeonCamino();
		}
			
		//Sino puede que haya un camino	
		else
		{
		
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
					if(coste_provisional < coste) 
					{
						coste= coste_provisional;
						posicion= posicion_provisional;
					}
					
				}
				
				//guardamos la celda actual y la marcamos como que tiene el mejor coste
				Celda celda_A = celdas_abiertas.get(posicion);
				
				//si la celda A es el destino se acaba el algoritmo, si no es asi, se anade a closed
				if(celda_A == celda_final)
				{
					no_camino = false;
					break; //se sale del bucle while
				}
				else
				{
					no_camino = true; //todavia no se ha encontrado un camino 
					
					celdas_cerradas.add(celda_A);//anadimos la celda A a cerradas
					celdas_abiertas.remove(celda_A);//borramos la celda A de abiertas
					
					//guardo la lista de vecinos de la celda 
					ArrayList<Celda> vecinos =  getlistaVecinos(celda_A);
					
					//Si la lista de vecinos transitables no esta vacia calculo el coste de transitar hacia el
					if(!vecinos.isEmpty()) 
					{
						for(Celda mi_vecino : vecinos)//para cada vecino
						{
							// si V no esta en la lista de cerradas
							if(!celdas_cerradas.contains(mi_vecino)) 
							{
								int coste_provisional = mi_vecino.coste(celda_inicio.fila,celda_inicio.columna,celda_final.fila,celda_final.columna,mi_vecino.fila,mi_vecino.columna);
								int coste_celda_A = celda_A.coste(celda_inicio.fila,celda_inicio.columna,celda_final.fila,celda_final.columna,celda_A.fila,celda_A.columna);
								
								// Si la lista de celdas abiertas no contiene el vecino o el coste provisional es menor al coste de la celda A,
								// entonces entramos en este if
								if(!celdas_abiertas.contains(mi_vecino) || coste_provisional <= coste_celda_A)	
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
	
						}//cierra el for de para cada vecino	
					}//cierra el if de la lista de vecinos
					
				}//cierra el else	
							
			}//cierra el while
			
			
			if(no_camino == false ) // si existe un camino entonces lo recorro
			{
				/*
				System.out.println("Hay camino");
				*/
				
				
				//Recorremos el camino optimo empezando desde la celda final
				RecorrerCamino(celda_final);	
			}
			
			else //Si no existe un camino entonces la distancia es -1 
			{
				distancia = -1;
			}
			
			//Reseteamos los booleanos de salida, meta y camino de todo el dungoen primero
			ResetearDungeonCamino();
			
			//se resetean las variables
			celda_inicio = null;
			celda_final = null;
			
		}
				
	}


	/**
	 * Funcion calcula la distancia entre las puertas y los tesoros guardando un arraylist con las distancias
	 * @param numero_puertas numero de puertas que hay en el mapa
	 * @param numero_tesoros numero de tesoros que hay en el mapa
	 */
	public void calcular_distancias_PT(int numero_puertas, int numero_tesoros)
	{
		
		//Variable para guardar la distancia entre una puerta y un tesoro
		int distancia_ = 0;
		distancia = -1;
		
		
		//Variable para guardar las caracteristicas de la celda donde se encuentra el T mas cercano a la P
		Celda posicion_T_minima = null;
		
		//Se calculan las distancias entre cada puerta con cada tesoro
		for(int contador_puertas = 0; contador_puertas < numero_puertas; contador_puertas++)//por cada puerta
		{
			
			//Inicializo la variable que va a guardar las distancias con todos los tesoros
			posicion_puertas.get(contador_puertas).tam_dist_T = new ArrayList<Integer>();
			
			//Variable para guardar la distancia del tesoro mas cercano a la puerta (se inicializa al mayor numero de movimientos que podria hacer que es f x c )
			int distancia_minima = f * c;
			
			
			//Si no hay tesoros salgo del bucle
			if (numero_tesoros == 0)
			{
				System.out.println("NO HAY TESOROS");
				break;
			}
			
			else
			{
				//Para cada tesoro con respecto a una puerta se guarda su distancia
				for(int contador_tesoros = 0; contador_tesoros < numero_tesoros; contador_tesoros++)
				{
					
					
					
					//Se calcula la distancia de cada puerta con cada tesoro
					llegada_optima(posicion_puertas.get(contador_puertas).fila, posicion_puertas.get(contador_puertas).columna, posicion_tesoros.get(contador_tesoros).fila,posicion_tesoros.get(contador_tesoros).columna);
					
					
					//si la puerta es de tipo entrada salida entonces guardo su distancia para luego calcular el fitness con esa distancia
					if(posicion_puertas.get(contador_puertas).tipo_puerta_N == Tipo_puertas.ENTRADA_SALIDA || posicion_puertas.get(contador_puertas).tipo_puerta_S == Tipo_puertas.ENTRADA_SALIDA || posicion_puertas.get(contador_puertas).tipo_puerta_E == Tipo_puertas.ENTRADA_SALIDA || posicion_puertas.get(contador_puertas).tipo_puerta_O == Tipo_puertas.ENTRADA_SALIDA)
					{
						posicion_tesoros.get(contador_tesoros).distancia_P_cercana = distancia;
					}

					
					//Si no hay camino entre un tesoro y una puerta la habitacion no es valida
					if(distancia == -1)
					{
						dungeon_valido = false;
					}
					
					distancia_ = distancia;
					
					//se anade una posicion con valor distancia_ por cada tesoro que haya en el mapa para guardar su distancia con respecto a la puerta i
					posicion_puertas.get(contador_puertas).tam_dist_T.add(distancia_);
					
					//Si la distancia calculada es inferior a la distancia minima almacenada se guardan los datos
					if(distancia_ < distancia_minima && distancia != -1) //&& distancia_ >= 0) //>= 0 si se quiere que en la celda donde se encuentra la puerta pueda haber un tesoro
					{
						//Se iguala la distancia minima con la calculada
						distancia_minima = distancia_;
						
						//Se guarda la posicion del tesoro
						posicion_T_minima = posicion_tesoros.get(contador_tesoros);
					}
					
					//si me encuentro en la ultima vuelta y todos los tesoros tienen una distancia negativa, entonces guardo como la distancia minima -1 
					//(se puede deber a que la puerta este incomunicada o que sea de tipo pared esa celda)
					if(distancia == -1 && (contador_tesoros == (numero_tesoros - 1)) && distancia_minima == f * c)
					{
						//Se iguala la distancia minima con la calculada
						distancia_minima = -1;
						
						//Se guarda la posicion de la celda donde esta la puerta para tener una posicion
						posicion_T_minima = posicion_puertas.get(contador_puertas);
											
					}
					
					
					//Se resetea la distancia
					distancia_ = 0;
					distancia = -1;
					
					
					
				}
				
				//Se anaden las distancias de cada tesoro con respecto a la puerta i
				//puertas_distancias.addAll(tam_dist);
					
				
				
				//Guardo en un arraylist las posicion del tesoro mas cercano a la puerta
				posicion_puertas.get(contador_puertas).Posicion_T_cercano[0]= posicion_T_minima.fila;
				posicion_puertas.get(contador_puertas).Posicion_T_cercano[1]= posicion_T_minima.columna;
				
				//Si no se ha modificado la distancia minima porque no hay ningun objeto o camino posible entre la puerta y los objetos, decimos que la distancia minima es -1
				if (distancia_minima == (f * c))
				{
					distancia_minima = - 1;
				}
				
				
				//Voy colocando en cada posicion la correspondiente medida
				if(posicion_puertas.get(contador_puertas).puerta_N)
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PT.add(0, distancia_minima);
					
				}
				
				else if(posicion_puertas.get(contador_puertas).puerta_S)
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PT.add(1, distancia_minima);
				}
				
				else if(posicion_puertas.get(contador_puertas).puerta_E)
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PT.add(2, distancia_minima);
				}
				
				else if(posicion_puertas.get(contador_puertas).puerta_O)
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PT.add(3, distancia_minima);
				}
				
				else 
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PT.add(distancia_minima);
				}
		
			}
			
			
		}
	
	}
	
	
	/**
	 * Funcion calcula la distancia entre las puertas y los monstruos guardando un arraylist con las distancias
	 * @param Poblacion
	 */
	public void calcular_distancias_PM(int numero_puertas, int numero_monstruos)
	{
		
		
		//Variable para guardar la distancia entre una puerta y un tesoro
		int distancia_ = 0;
		distancia = -1;
				
		//Variable para guardar las caracteristicas de la celda donde se encuentra el M mas cercano a la P
		Celda posicion_M_minima = null;
		
		//Se calculan las distancias entre cada puerta con cada tesoro
		for(int contador_puertas = 0; contador_puertas < numero_puertas; contador_puertas++)//por cada puerta
		{
			
			//Inicializo la variable que va a guardar las distancias con todos los monstruos
			posicion_puertas.get(contador_puertas).tam_dist_M = new ArrayList<Integer>();
			
			//Variable para guardar la distancia del tesoro mas cercano a la puerta (se inicializa al mayor numero de movimientos que podria hacer que es f x c )
			int distancia_minima = f * c;
			
			
			//Si no hay monstruos salgo del bucle
			if (numero_monstruos == 0)
			{
				System.out.println("NO HAY MONSTRUOS");
				break;
			}
			
			else
			{
				//Para cada tesoro con respecto a una puerta se guarda su distancia
				for(int contador_monstruos = 0; contador_monstruos < numero_monstruos; contador_monstruos++)
				{
					
					
					//Se calcula la distancia de cada puerta con cada tesoro
					llegada_optima(posicion_puertas.get(contador_puertas).fila, posicion_puertas.get(contador_puertas).columna, posicion_monstruos.get(contador_monstruos).fila,posicion_monstruos.get(contador_monstruos).columna);
					
					
					
					//Si no hay camino entre un monstruo y una puerta la habitacion no es valida
					if(distancia == -1)
					{
						dungeon_valido = false;
					}
					
					distancia_ = distancia;
					
					//se anade una posicion con valor distancia_ por cada monstruo que haya en el mapa para guardar su distancia con respecto a la puerta i
					posicion_puertas.get(contador_puertas).tam_dist_M.add(distancia_);
					
					//Si la distancia calculada es inferior a la distancia minima almacenada se guardan los datos
					if(distancia_ < distancia_minima && distancia != -1) //&& distancia_ >= 0) //>= 0 si se quiere que en la celda donde se encuentra la puerta pueda haber un tesoro
					{
						//Se iguala la distancia minima con la calculada
						distancia_minima = distancia_;
						
						//Se guarda la posicion del monstruo
						posicion_M_minima = posicion_monstruos.get(contador_monstruos);
					}
					
					//si me encuentro en la ultima vuelta y todos los monstruos tienen una distancia negativa, entonces guardo como la distancia minima -1 
					//(se puede deber a que la puerta este incomunicada o que sea de tipo pared esa celda)
					if(distancia == -1 && (contador_monstruos == (numero_monstruos - 1)) && distancia_minima == f * c)
					{
						//Se iguala la distancia minima con la calculada
						distancia_minima = -1;
						
						//Se guarda la posicion de la celda donde esta la puerta para tener una posicion
						posicion_M_minima = posicion_puertas.get(contador_puertas);
											
					}
					
					
					//Se resetea la distancia
					distancia_ = 0;
					distancia = -1;
					
					
					
				}
				
				
				//Guardo en un arraylist las posicion del tesoro mas cercano a la puerta
				posicion_puertas.get(contador_puertas).Posicion_M_cercano[0]= posicion_M_minima.fila;
				posicion_puertas.get(contador_puertas).Posicion_M_cercano[1]= posicion_M_minima.columna;
				
				//Si no se ha modificado la distancia minima porque no hay ningun monstruo o camino posible entre la puerta y los monstruos, decimos que la distancia minima es -1
				/*if (distancia_minima == (f * c))
				{
					distancia_minima = - 1;
				}*/
				
				
				//Voy colocando en cada posicion la correspondiente medida
				if(posicion_puertas.get(contador_puertas).puerta_N)
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PM.add(0, distancia_minima);
					
				}
				
				else if(posicion_puertas.get(contador_puertas).puerta_S)
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PM.add(1, distancia_minima);
				}
				
				else if(posicion_puertas.get(contador_puertas).puerta_E)
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PM.add(2, distancia_minima);
				}
				
				else if(posicion_puertas.get(contador_puertas).puerta_O)
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PM.add(3, distancia_minima);
				}
				
				else 
				{
					//Guardo en un arraylist las distancias minimas
					distancia_min_PM.add(distancia_minima);
				}
		
			}//cierra el else
					
		}//cierra el for
	
	}//cierra la funcion

	
	/**
	 * Funcion que calcula si hay camino entre las puertas
	 * @param numero de puertas que hay en el mapa
	 */
	public void calcular_camino_PP(int numero_puertas)
	{
		
		//booleano que mientras haya una distancia entre la puerta 0 y las demas es que hay camino, de lo contrario, es un dungeon sin 
		//conexion entre las puertas
		
		dungeon_valido = true;
		
		//Se resetea la variable distancia
		distancia = -1;
		
		
		//Se calcula la distancia entre la puerta 0 con el resto de puertas
		for(int contador_puertas = 0; contador_puertas < numero_puertas; contador_puertas++)//por cada puerta
		{
			//Se resetea la distancia
			distancia = -1;
			
			//si el dungeon es valido se comprueba la distancia, en el momento en el que ya no haya camino entre dos puertas será invalido el camino
			if(dungeon_valido != false)
			{
				//Se calcula la distancia de la puerta 0 con las demas
				llegada_optima(posicion_puertas.get(0).fila, posicion_puertas.get(0).columna, posicion_puertas.get(contador_puertas).fila, posicion_puertas.get(contador_puertas).columna);
				
	
				//Si solo hay una puerta y la distancia es 0 porque no hay otra puerta; el dungeon es valido
				if(distancia == 0 && contador_puertas == numero_puertas)
				{
					dungeon_valido = true;
				}
				
				//Si hay una distancia es que es un dungeon valido porque hay un camino
				else if (distancia > 0)
				{

					dungeon_valido = true;
				}
				
				//si la distancia es -1 entonces es que no hay un camino PP y ya no es un dungeon_valido
				else if (distancia == -1)
				{
					dungeon_valido = false;
				}
			
			}//cierra el if
			
		}//cierra el for
		
	}//cierra la funcion
	
	
	
	/**
	 * Funcion que calcula el fitness de los tesoros con respecto a la puerta mas cercana de dicho tesoro
	 * @return
	 */
	public double[] calcular_fitness_tesoros()
	{
		
		//varibale que va a guardar el fitness de cada tesoro
		double[] fitness_tesoros = new double[numero_tesoros];	
		
		//Variables para calcular el st
		//float st_resta = 0; //Es float porque sino la division no la da con decimales
		//float st_suma = 0; //Es float porque sino la division no la da con decimales
		
		//metrica de seguridad del tesoro con respecto a la puerta y al monstruo
		float st = 0;
		
		//variable que va a almacenar todos los st para luego quedarme con el menor
		float[] st_provisional = new float[numero_monstruos];
		
		//Por cada tesoro que hay en el mapa calculo su fitness
		for (int tesoro= 0; tesoro < numero_tesoros; tesoro++)
		{
			
			//inicializo el array
			st_provisional = new float[numero_monstruos];
			
			//System.out.println("Distancia P_T tesoro " + posicion_tesoros.get(tesoro).fila + " " + posicion_tesoros.get(tesoro).columna + ":             " + " " + + posicion_tesoros.get(tesoro).distancia_P_cercana);
			
			//por cada monstruo que hay en el mapa calculo las distancias entre ese monstruo y el tesoro i
			for(int monstruo = 0; monstruo < numero_monstruos; monstruo++)
			{
				//calculo cual es la distancia minima entre el tesoro y el resto de monstruos que hay en el mapa
				llegada_optima(posicion_tesoros.get(tesoro).fila, posicion_tesoros.get(tesoro).columna, posicion_monstruos.get(monstruo).fila, posicion_monstruos.get(monstruo).columna);
				
								
				//calculo el numerador y el denominador (hay que hacer esto para convertirlo a float y poder hacer una division con decimales)
				//st_resta = distancia  - posicion_tesoros.get(tesoro).distancia_P_cercana;  //caso 1  //posicion_tesoros.get(tesoro).distancia_P_cercana - distancia; //caso 2
				//st_suma = distancia + posicion_tesoros.get(tesoro).distancia_P_cercana;    //caso 1  //posicion_tesoros.get(tesoro).distancia_P_cercana + distancia; //caso 2
				
				
				System.out.println("Distancia T_M tesoro "+ posicion_tesoros.get(tesoro).fila + " " + posicion_tesoros.get(tesoro).columna +" monstruo "+ posicion_monstruos.get(monstruo).fila + " " + posicion_monstruos.get(monstruo).columna + ": " + distancia);
				
				//obtengo el st
				st = distancia; //st_resta / st_suma;
				
				//System.out.println("(" + distancia + "-" + posicion_tesoros.get(tesoro).distancia_P_cercana + ")/(" +  distancia + "+" + posicion_tesoros.get(tesoro).distancia_P_cercana + ")= " + st);
				
				//si el resultado obtenido es mayor o igual a 0 entonces guardamos el resultado, sino guardamos un 0
				if(st >= 0)
				{
					//guardo el resultado en un array
					st_provisional[monstruo] = st;
					
					
				}
				else
				{
					//guardo el resultado en un array
					st_provisional[monstruo] = 0;
				}
				
				//reseteo las variables
				//st_resta = 0;
				//st_suma = 0;
				st = 0;
				
				
			}
			
			System.out.println(" ");
			
			//reseteo el st para guardar el valor minimo del st
			st = f * c;
			
			//obtengo el valor con menor resultado del array provisional
			for(float valor : st_provisional)
			{
				//si el valor es mayor o igual a 0 y menor que el dato que hemos guardado entonces almacenamos ese dato
				if(valor >= 0.0 && valor < st)
				{
					st = valor;
				}
			}	
			
			
			
			//guardo en la posicion correspondiente el fitness minimo para ese tesoro
			dungeon[posicion_tesoros.get(tesoro).fila][posicion_tesoros.get(tesoro).columna].distancia_seguridad_M = st;
			fitness_tesoros[tesoro] = st;
						
		}
		
		
		//devuelvo el array con los fitness calculados de cada tesoro
		return fitness_tesoros;
	}
	
	
	/**
	 * Funcion que se encarga de calcular el fitness de las puertas, para ello lo que hace es ver si tiene objetos cerca 
	 * (se hace con busqueda en anchura)
	 */
	public double[] calcular_fitness_puertas()
	{
		
		//array que vamos a pasar con los dos fitness de las puertas, el de los monstruos y el de los tesoros por cada puerta
		double[] fitness_puertas_total = new double[2 * posicion_puertas.size()];
		
	
		//variable que va a almacenar la media de los fitness de las puertas
		double fitness_puertas = 0;
		
		//variable que va a guardar el fitness de cada puerta para luego hacer la media entre las puertas
		double fitness_puerta_M = -1;//new double[posicion_puertas.size()];
		double fitness_puerta_T = -1;//new double[posicion_puertas.size()];
	
		//listas que van a guardar las celdas que se han visitado y las celdas que quedan por visitar
		ArrayList<Celda> celdas_abiertas;
		ArrayList<Celda> celdas_cerradas;
		
		//lista que va a guardar el area de las celdas que son seguras para la puerta
		ArrayList<Celda> celdas_recorridas = new ArrayList<Celda>();
		
		
		//variable que va a guardar el numero de las celdas seguras
		double area = 0.0;
		
		//variables para calcular el numerador y el denominador del fitness
		double numerador = 0.0;
		double denominador = 0.0;
		double division = 0.0;
		
		//area maximo que va a haber entre la puerta y el monstruo/tesoro
		double area_max = 0.0;
		
		int contador_puertas = 0; //variable para saber en que posicion guardar el fitness de cada puerta
		
		//Varible para saber si se ha detectado o no un objeto
		boolean objeto_detectado;

		
		
		
		//Se realiza este bucle 2 veces para poner el fitness de los monstruos y de los tesoros en el array que vamos a devolver
		for(int i= 0; i < 2; i++)
		{
			
			if(i == 0)
			{
				System.out.println(" ");
				System.out.println("----------------");
				System.out.println("FITNESS DE SEGURIDAD DE LAS PUERTAS (MONSTRUOS)");
				System.out.println(" ");
			}
			
			else
			{
				System.out.println(" ");
				System.out.println("----------------");
				System.out.println("FITNESS DE SEGURIDAD DE LAS PUERTAS (TESOROS)");
				System.out.println(" ");
			}
			
			
			//reseteo la variable para saber en que posicion guardar el fitness de cada puerta
			contador_puertas = 0;
			
			//reseteo el contador del fitness para hacer la media y el array del fitness de todas las puertas
			fitness_puertas = 0.0;
			fitness_puerta_M = -1;//new double[posicion_puertas.size()];
			fitness_puerta_T = -1;//new double[posicion_puertas.size()];
			
			//Para cada puerta que hay almacenada en el array de posiciones
			for (Celda puerta: posicion_puertas)
			{
				
				//SOLO SE CALCULA EL AREA DE LA PUERTA QUE ES DE TIPO ENTRADA
				if (puerta.tipo_puerta_N == Tipo_puertas.ENTRADA_SALIDA || puerta.tipo_puerta_S == Tipo_puertas.ENTRADA_SALIDA || puerta.tipo_puerta_E == Tipo_puertas.ENTRADA_SALIDA || puerta.tipo_puerta_O == Tipo_puertas.ENTRADA_SALIDA)
				{
						
					//variable que va a guardar el numero de monstruos o tesoros que hay para hacer el bucle correspodiente dependiendo de si es de monstruos o de tesoros
					int posiciones= 0; 
					
					if (i == 0) //si estamos en la vuelta de los montruos seteo posiciones al numero de monstruos en el mapa, sino lo seteo al de tesoros
					{
						posiciones = posicion_monstruos.size();
					}
					else
					{
						posiciones = posicion_tesoros.size();
					}
					
					ResetearDungeonArea(); //reseteo las celdas para calcular el area
					
					
					//pintar (); //SOLO PARA DEBUG
					
					//Bucle para calcular el fitness del area de cada monstruo o tesoro con respecto a cada puerta
					for(int each_MT = 0; each_MT < posiciones; each_MT++)
					{
						//inicializo de las variables para resetearlas
						celdas_abiertas = new ArrayList<Celda>();
						celdas_cerradas = new ArrayList<Celda>();
						celdas_recorridas = new ArrayList<Celda>();
						
						area_max = 0.0;
						area = 0.0;
						
						//reseteo la variable para que vuelva a buscar el objeto en la nueva puerta
						objeto_detectado = false;
						
						//Variable que va a guardar la posicion del objeto
						int[] posicion_objeto_encontrado = new int[2];
						
			
						//Si en la puerta se encuentra un monstruo o tesoro y no se ha calculado su area, se calcula, sino, continuamos con el resto de areas
						if(((puerta.monstruo && i== 0) || (puerta.tesoro&& i== 1)) && !dungeon[puerta.fila][puerta.columna].area_calculado)
						{
							
							area = 0.0;
							
							if (i == 0) // si la vuelta en la que me encuentro es la del fitness puerta - monstruo
							{
								System.out.println("Area monstruo " + puerta.fila + " " + puerta.columna + " con la puerta " + puerta.fila + " " + puerta.columna + ": "+ area);
							}
							else // si la vuelta en la que me encuentro es la del fitness puerta - monstruo
							{
								System.out.println("Area tesoro " + puerta.fila + " " + puerta.columna + " con la puerta " + puerta.fila + " " + puerta.columna + ": "+ area);
							}
							
							//TODO MODIFICAR EL NUMERADOR Y SUSTITUIRLO POR EL AREA
							numerador = 1.0;
							denominador = (f * c) - celdas_Paredes;
							
							division = numerador / denominador;
							
							//Independientemente de la vuelta en la que me encuentre, la celda de la puerta la establecemos como que hemos calculado su area
							dungeon[puerta.fila][puerta.columna].area_calculado = true;
							
							//si me encuentro en la vuelta del fitness de los monstruos lo almaceno en su variables
							if (i == 0)
							{
								
								//se anade a la puerta correspondiente su fitness sumandole los calculados anteriormente
								fitness_puerta_M = (division * area) + fitness_puerta_M;
								
								System.out.println(area + "/" + "((" + f + "*" + c +")" + "-" + celdas_Paredes + ") = " +  (division * area));
								System.out.println(" ");
								
								
								//si ya hemos calculado todos los fitness de area hacemos la media y pasamos a calcular el fitness de seguridad de las puertas con los tesoros
								if(each_MT == ( posiciones - 1))
								{
									
									System.out.println(" ");
									System.out.println("Media del fitness tesoros de la puerta " + puerta.fila + " " + puerta.columna + ": " + fitness_puerta_M + " / " + posiciones + " = " + (fitness_puerta_M / numero_monstruos));
									System.out.println(" ");
									fitness_puerta_M = fitness_puerta_M / numero_monstruos;
									
								}
								
								
							}
							else //si me encuentro en la vuelta de los tesoros lo guardo en su variable 
							{
								//se anade a la puerta correspondiente su fitness sumandole los calculados anteriormente
								fitness_puerta_T = (division * area) + fitness_puerta_T;
								
								System.out.println(area + "/" + "((" + f + "*" + c +")" + "-" + celdas_Paredes + ") = " +  (division * area));
								System.out.println(" ");
								
								//si ya hemos calculado todos los fitness de area hacemos la media
								if(each_MT == ( posiciones - 1))
								{
									
									System.out.println(" ");
									System.out.println("Media del fitness tesoros de la puerta " + puerta.fila + " " + puerta.columna + ": " + fitness_puerta_T + " / " + posiciones + " = " + (fitness_puerta_T / numero_tesoros));
									System.out.println(" ");
									fitness_puerta_T = fitness_puerta_T / numero_tesoros;	
									
								}
								
							}
						}
						
						
						else
						{
							area_max = 0.0;
							area = 0.0;
							
							celdas_abiertas.add(puerta); //se anade el nodo inicial como celda visitada
							
						
							//Mientras no se encuentre un monstruo u objeto se sigue buscando
							while(!celdas_abiertas.isEmpty())
							{
								
								//guardo la primera posicion del array como la siguiente celda a la que voy a transitar
								Celda celda_A = celdas_abiertas.get(0);
								
								celdas_cerradas.add(celda_A);//anadimos la celda A a cerradas
								celdas_abiertas.remove(0);//borramos la celda A de abiertas
								
								//guardo la lista de vecinos de la celda 
								ArrayList<Celda> vecinos =  getlistaVecinos(celda_A);
								
								//Si la lista de vecinos transitables no esta vacia continuo
								if(!vecinos.isEmpty()) 
								{
									for(Celda mi_vecino : vecinos)//para cada vecino
									{
										// si el vecino no esta en la lista de cerradas
										if(!celdas_cerradas.contains(mi_vecino)) 
										{
										
											// Si la lista de celdas abiertas no contiene el vecino entonces se anade a la lista de abiertas
											if(!celdas_abiertas.contains(mi_vecino))	
											{
												//si la celda vecina contiene un monstruo o tesoro y no se habia detectado antes (no se ha calculado su area) se calcula el area maxima
												if(((mi_vecino.monstruo && i== 0) || (mi_vecino.tesoro && i== 1)) && !objeto_detectado && !dungeon[mi_vecino.fila][mi_vecino.columna].area_calculado)
												{
													objeto_detectado = true;
													
													//guardo la posicion del objeto para los comentarios
													posicion_objeto_encontrado[0] = mi_vecino.fila;
													posicion_objeto_encontrado[1] = mi_vecino.columna;
													
													
													//calculo el area maximo
													llegada_optima(puerta.fila, puerta.columna, mi_vecino.fila, mi_vecino.columna);
													area_max = distancia;	
													
													//se pone ese vecino como true ya que se ha calculado su area
													dungeon[mi_vecino.fila][mi_vecino.columna].area_calculado = true;
												
												}
												
												else
												{
													//TODO REVISAR SI ESTO ES NECESARIO O NO
													//Si todavia no se ha detectado un objeto se anade a la lista de abiertas el vecino
													if(!objeto_detectado)
													{
														celdas_abiertas.add(mi_vecino);
													}
												}
											}
										}
				
									}//cierra el for de para cada vecino
									
									//si nos encontramos en la celda de la puerta y ya hemos detectado un monstruo en sus vecinos guardamos en celdas recorridas la celda_A que se encuentra en las celdas
									//cerradas
									if(celda_A == puerta && objeto_detectado)
									{
										celdas_recorridas = celdas_cerradas;
									}
									
									//El area de seguridad de la puerta es igual al de las celdas cerradas (luego se eliminan las que estan a la misma distancia)
									celdas_recorridas = celdas_cerradas;
									
								}//cierra el if de la lista de vecinos	
											
							}//cierra el while
							
							
							//para cada celda de las recorridas que tenga la misma distancia entre el monstruo/tesoro y la puerta se elimina de la lista
							for(Celda celda_segura:celdas_recorridas) 
							{
								
								//se calcula la distancia entre la celda tesoro/monstruo y la puerta
								llegada_optima(puerta.fila, puerta.columna, celda_segura.fila, celda_segura.columna);
								
								//System.out.println("Distancia celda_puerta: " + distancia);
								
								//si la distancia entre la celda segura y la celda donde se encuentra el monstruo/tesoro es la misma, no se cuenta para calcular el area
								//a no ser que solo haya una celda en recorridas(la de la puerta)
								if (distancia >= area_max && celdas_recorridas.size() != 1)
								{
									continue;
								}
								else
								{
									area = area + 1.0;
								}
									
							}
							
							if (i == 0) // si la vuelta en la que me encuentro es la del fitness puerta - monstruo
							{
								System.out.println("Area monstruo " + posicion_objeto_encontrado[0] + " " + posicion_objeto_encontrado[1] + " con la puerta " + puerta.fila + " " + puerta.columna + ": "+ area);
							}
							else // si la vuelta en la que me encuentro es la del fitness puerta - monstruo
							{
								System.out.println("Area tesoro " + posicion_objeto_encontrado[0] + " " + posicion_objeto_encontrado[1] + " con la puerta " + puerta.fila + " " + puerta.columna + ": "+ area);
							}
							
							
							//TODO MODIFICAR EL NUMERADOR Y SUSTITUIRLO POR EL AREA
							numerador = 1.0;
							denominador = (f * c) - celdas_Paredes;
							
							division = numerador / denominador;
							
							
							//si me encuentro en la vuelta del fitness de los monstruos lo almaceno en su variables
							if (i == 0)
							{
								
								//se anade a la puerta correspondiente su fitness sumandole los calculados anteriormente
								fitness_puerta_M = (division * area) + fitness_puerta_M;
								
								System.out.println(area + "/" + "((" + f + "*" + c +")" + "-" + celdas_Paredes + ") = " +  (division * area));
								System.out.println(" ");
								
								
								//si ya hemos calculado todos los fitness de area hacemos la media y pasamos a calcular el fitness de seguridad de las puertas con los tesoros
								if(each_MT == ( posiciones - 1))
								{
									System.out.println(" ");
									System.out.println("Media del fitness monstruos de la puerta " + puerta.fila + " " + puerta.columna + ": " + fitness_puerta_M + " / " + posiciones + " = " + (fitness_puerta_M / numero_monstruos));
									System.out.println(" ");
									fitness_puerta_M = fitness_puerta_M / numero_monstruos ;
									
								}
								
								
							}
							else //si me encuentro en la vuelta de los tesoros lo guardo en su variable 
							{
								//se anade a la puerta correspondiente su fitness sumandole los calculados anteriormente
								fitness_puerta_T = (division * area) + fitness_puerta_T;
								
								System.out.println(area + "/" + "((" + f + "*" + c +")" + "-" + celdas_Paredes + ") = " +  (division * area));
								System.out.println(" ");
								
								//si ya hemos calculado todos los fitness de area hacemos la media
								if(each_MT == ( posiciones - 1))
								{
									System.out.println(" ");
									System.out.println("Media del fitness tesoros de la puerta " + puerta.fila + " " + puerta.columna + ": " + fitness_puerta_T + " / " + posiciones + " = " + (fitness_puerta_T / numero_tesoros));
									System.out.println(" ");
									fitness_puerta_T = fitness_puerta_T / numero_tesoros;	
									
								}
								
							}
							
							
						}//cierra el else de monstruos y tesoros
						
					
						//se resetean las variables
						celdas_abiertas = null;
						celdas_cerradas = null;
						celdas_recorridas = null;
						
						posicion_objeto_encontrado = null;
						
					}//se cierra el for para cada monstruo o cada tesoro
					
					//se incrementa el contador de las puertas
					contador_puertas++;
					
					
				}
				
				System.out.println(" ");
			
				
				//SI estamos en la vuelta de los montruos lo guardamos en la posicion 0, sino en la 1 (para los tesoros)
				if (i == 0)
				{
					fitness_puertas_total[0] = fitness_puerta_M;
				}
				else
				{
					fitness_puertas_total[1] = fitness_puerta_T;
				}
				
				
			
		
			}
			
		}//se acaba el for para rellenar el array

		
		return fitness_puertas_total;
	}
	
	
	/**
	 * Funcion que calcula el fitness del dungeon
	 */
	public void calcularfitness(int numero_puertas) 
	{
				
		//calculo el camino que hay entre la puerta 0 y el resto
		calcular_camino_PP(numero_puertas);
		
		//Si hay al menos un tesoro calculo las distancias entre P y T para comprobar cual es la puerta mas cercana a cada tesoro
		if(numero_tesoros != 0)
		{
			//Se calcula las distancias PT
			calcular_distancias_PT(numero_puertas, numero_tesoros);
			
		}
		
		//Si hay al menos un monstruo calculo las distancias entre P y M
		/*if(numero_monstruos != 0)
		{
			//Se calcula las distancias PM
			calcular_distancias_PM(numero_puertas, numero_monstruos);
			
		}*/
		
		
		//Se inicializa la variable que va a guardar los fitness de cada cosa del individuo
		//
		//posicion 0 - fitness tesoros con la puerta mas cercana
		//posicion 1 - fitness de las puertas con los monstruos
		//posicion 2 - fitness de las puertas con los tesoros
		fitness_por_partes = new double[3];
		
		//Calculo el fitness del dungeon siempre y cuando haya tesoros y monstruos 
		//en el mapa y haya camino entre la/s puerta/s y los objetos, sino tiene un fitness negativo
		if(numero_monstruos != 0 && numero_tesoros != 0 && dungeon_valido == true)
		{
			//calculo el fitness de los tesoros
			double[] fitness_tesoros = calcular_fitness_tesoros();
			
			for(int tesoro= 0; tesoro < numero_tesoros; tesoro++)
			{
				fitness_por_partes[0] = fitness_por_partes[0] + fitness_tesoros[tesoro];
				System.out.println("Fitness tesoro "+ posicion_tesoros.get(tesoro).fila + " " + posicion_tesoros.get(tesoro).columna + ": " + posicion_tesoros.get(tesoro).distancia_seguridad_M);
			}
			System.out.println(" ");
			
			System.out.println("Media del fitness de seguridad de los tesoros: " + fitness_por_partes[0] + " / " + numero_tesoros + " = " + (fitness_por_partes[0] / numero_tesoros));
			
			fitness_por_partes[0] = fitness_por_partes[0] / numero_tesoros;
			
		
			
			//ponderaciones para los fitness para incrementar que salga un tipo de habitacion u otra
			double ponderacion_fit_seg_tesoros = 1.0;
			double ponderacion_fit_seg_pu_mons = 1.0;
			double ponderacion_fit_seg_pu_teso = 1.0;
			
			//calculo el fitness de las puertas
			double[] fitness_puertas = calcular_fitness_puertas();
			
			fitness_por_partes[1] = fitness_puertas[0];
			fitness_por_partes[2] = fitness_puertas[1];
			
			//por cada puerta que hay en el mapa cojo su fitness con el tesoro y con el monstruo y hago la media
			/*for(int puerta= 0; puerta < numero_puertas; puerta++)
			{
				//para calcular el fitness de los monstruos
				fitness_por_partes[1] = fitness_por_partes[1] + fitness_puertas[puerta];
				
				//para calcular el fitness de los tesoros
				fitness_por_partes[2] = fitness_por_partes[2] + fitness_puertas[puerta + numero_puertas];
			}*/
			
			//hago la media de los fitness de seguridad de las puertas
			/*fitness_por_partes[1] = fitness_por_partes[1] / numero_puertas;
			fitness_por_partes[2] = fitness_por_partes[2] / numero_puertas;
			*/
			
			System.out.println(" ");
			System.out.println("-----------------------------------------------------------");
			System.out.println("Fitness seguridad tesoros:           " + fitness_por_partes[0] + " * " + ponderacion_fit_seg_tesoros + " = " + (ponderacion_fit_seg_tesoros * fitness_por_partes[0]));
			System.out.println("Fitness seguridad puertas monstruos: " + fitness_por_partes[1] + " * " + ponderacion_fit_seg_pu_mons + " = " + (ponderacion_fit_seg_pu_mons * fitness_por_partes[1]));
			System.out.println("Fitness seguridad puertas tesoros:   " + fitness_por_partes[2] + " * " + ponderacion_fit_seg_pu_teso + " = " + (ponderacion_fit_seg_pu_teso * fitness_por_partes[2]));
		
			
			//se suman los fitness calculados para obtener el fitness final
			fitness = (ponderacion_fit_seg_tesoros * fitness_por_partes[0]) + (ponderacion_fit_seg_pu_mons * fitness_por_partes[1]) + (ponderacion_fit_seg_pu_teso * fitness_por_partes[2]);
		}
		else 
		{
			dungeon_valido = false;
			
			fitness = -100;
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
				siguientePosicion[0] =	tipo_celda.fila;
				siguientePosicion[1] = tipo_celda.columna;
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
		
		distancia = 1;
		
		// mientras la celda camino no tenga la variable boolean posicion inicio a true no nos salimos, pues todavia no hemos terminado de recorrer el 
		// camino hasta llegar a la posicion de inicio viniendo desde la posicion de meta
		while(celda_camino.inicio == false)
		{
			//Nos creamos una celda que la igualamos a la celda precursora de la celda camino
			Celda celda_precursora = dungeon[celda_camino.Posicion_precursor[0]][celda_camino.Posicion_precursor[1]];
			
	
			//Si la celda donde nos encontramos no es ni el destino ni el inicio ( teniendo en cuenta que recorremos el camino al reves, entonces contamos como 
			//que esa celda es un camino
			/*if(!celda_camino.destino || !celda_camino.inicio)
			{
				
				//si la siguiente posicion no es la de inicio (puerta) entonces contamos que esta celda donde nos encontramos es un movimiento
				distancia++;
			}*/
			
			if(celda_precursora.inicio) //si la celda siguiente es la de inicio no la contamos
			{
				celda_camino = celda_precursora; // continuamos con el recorrrido
				
			}
			else
			{
				celda_camino = celda_precursora; // continuamos con el recorrrido
				distancia++; //anado un movimiento a la distancia
			}
			
			

			
		}
		
		//System.out.println("He terminado de recorrer el camino");
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
	 * Funcion que recorre el dungeon y va reseteando las celdas que han sido marcadas para continuar calculando el area
	 */
	public void ResetearDungeonArea()
	{
		for(int i = 0; i <= dungeon.length-1; i++)
		{
			for(int j = 0; j <= dungeon[i].length-1; j++)
			{
				//resetamos los booleanos y los ponemos a false de nuevo para poder volver a poner areas en este mapa
				dungeon[i][j].area_calculado = false;
	
			}
		}
		
	}
	
	/** 
     *	Funcion que pinta el dungeon en ASCI
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
				if(j < dungeon[i].length)
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
		
		
		//SE PINTAN LAS CARACTERISTICAS DE LA HABITACION
		/*
		System.out.println(" ");
		System.out.println("-------------");
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
		System.out.println("-------------");
		*/
				
		System.out.println(" ");
		System.out.println("-------------");
		System.out.println("Numero de monstruos: " + numero_monstruos);
		System.out.println("Numero de tesoros  : " + numero_tesoros);
		System.out.println("-------------");
		
		/*
		System.out.println(" ");
		System.out.println("-------------");
		System.out.println("Distancias minimas PT: "+ distancia_min_PT);
		System.out.println("Distancias minimas PM: "+ distancia_min_PM);
		System.out.println("-------------");
		*/
		
		System.out.println(" ");
		System.out.println("-------------");
		System.out.println("Tipo puertas: ");
		
		for (int fila= 0; fila < f; fila++)
		{
			for(int columna= 0; columna < c; columna++)
			{
				if (dungeon[fila][columna].puerta && dungeon[fila][columna].puerta_N) //Si es puerta norte
				{
					System.out.println("Puerta Norte "+ fila +" "+ columna + ": " + dungeon[fila][columna].tipo_puerta_N + " ");
//					System.out.println("Distancias PT: "+ dungeon[fila][columna].tam_dist_T);
//					System.out.println("Distancias PM: "+ dungeon[fila][columna].tam_dist_M);
					System.out.println(" ");
				}
				
				if (dungeon[fila][columna].puerta  && dungeon[fila][columna].puerta_S)//Si es puerta sur
				{
					System.out.println("Puerta Sur   "+ fila +" "+ columna + ": " + dungeon[fila][columna].tipo_puerta_S + " ");
//					System.out.println("Distancias PT: "+ dungeon[fila][columna].tam_dist_T);
//					System.out.println("Distancias PM: "+ dungeon[fila][columna].tam_dist_M);
					System.out.println(" ");
				}
				
				if (dungeon[fila][columna].puerta  && dungeon[fila][columna].puerta_E)//Si es puerta este
				{
					System.out.println("Puerta Este  "+ fila +" "+ columna + ": " + dungeon[fila][columna].tipo_puerta_E + " ");
//					System.out.println("Distancias PT: "+ dungeon[fila][columna].tam_dist_T);
//					System.out.println("Distancias PM: "+ dungeon[fila][columna].tam_dist_M);
					System.out.println(" ");
				}
				
				if (dungeon[fila][columna].puerta  && dungeon[fila][columna].puerta_O)//Si es puerta oeste
				{
					System.out.println("Puerta Oeste "+ fila +" "+ columna + ": " + dungeon[fila][columna].tipo_puerta_O + " ");
//					System.out.println("Distancias PT: "+ dungeon[fila][columna].tam_dist_T);
//					System.out.println("Distancias PM: "+ dungeon[fila][columna].tam_dist_M);
					System.out.println(" ");
				}
			}
		}
		System.out.println("-------------");
		
		/*
		System.out.println(" ");
		System.out.println("Status individuo: "+ dungeon_valido);
		*/
		System.out.println(" ");
		System.out.println("Fitness: "+ fitness);
		System.out.println("-----------------------");
	
	} // Cierra la funcion pintar
	
	
}//Cierra la clase
