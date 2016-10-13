package Dungeon;

import java.util.ArrayList;
import java.util.Random;

import Dungeon.Celda.Tipo_puertas;

/**
 * Clase EvPopulation que se encarga de crear y evolucionar una poblacion
 * @version 1.0
 * @author  Alvaro Valle del Pozo
 * 			TFM
 * 			Profesor: Daniel Manrique y Jose Maria Font
 * 			Fecha: 01-02-2016
 */
public class EvPopulation 
{
	
	//Variable que guarda la informacion de un mapa
	Dungeon mapa = null;
	
	//ArrayList que almacena los individuos de la poblacion
	ArrayList<Dungeon> Poblacion = new ArrayList<Dungeon>();
	
	//ArrayList que almacena los individuos de la poblacion
	ArrayList<Dungeon> Poblacion_noValidos = new ArrayList<Dungeon>();
	
	/*//Variables para saber cuantos individuos de la poblacion son validos y cuantos son invalidos
	int Validos;
	int No_validos;*/
	
	//variables para guardar al mejor individuo de la iteracion y de la anterior y ver si la poblacion a mejorado o a empeorado
	public Dungeon individuo_parada;
	public Dungeon individuo_parada_copia;
	
	public int contador_iteraciones = 0;
	public boolean stop = false;
	
	public int motivo;
	
	public ArrayList<Dungeon> Individuos_parada = new ArrayList<Dungeon>();
	
	
	public int mutaciones;
	
	
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
	 * @param t_puertas 
	 * @param numero_puertas cuantas puertas por mapa
	 */
	public ArrayList<Dungeon> populationInitialization(int f, int c, int numero_poblacion, int numero_monstruos, int numero_tesoros, ArrayList<int[]> pos_puertas, ArrayList<Tipo_puertas> t_puertas, int numero_puertas, int porcentaje, int porcentaje_paredes, int tipo_celdas, double [] dificultad_nivel, double [] ponderaciones_nivel)
	{
		
		//se inicializan los individuos de parada y la copia para luego modificarlos en la convergencia
		
		individuo_parada = new Dungeon(f, c, numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas, dificultad_nivel, ponderaciones_nivel);
		individuo_parada_copia = new Dungeon(f, c, numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas, dificultad_nivel, ponderaciones_nivel);
		
		
		//Se crea un mapa por cada individuo y se anade a la poblacion
		for(int i = 0; i<numero_poblacion; i++)
		{
			
			mapa = new Dungeon(f, c,  numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas, dificultad_nivel, ponderaciones_nivel); //El dungeon se pasa con las dimensiones (x, y)
			
			
			Poblacion.add(mapa);
			
			/*
			//Si el individuo es no_valido lo anado a la lista de no validos
			if(!mapa.dungeon_valido)
			{
				No_validos++;
				
				//Anado el no valido a la lista de la poblacion de no validos
				Poblacion_noValidos.add(mapa);
		
				
				
			}
			
			//Si el individuos es valiudo lo anado a la lista de validos
			if(mapa.dungeon_valido)
			{
				Validos++;
				
				//Se anade a la poblacion de validos el mapa nuevo generado
				Poblacion.add(mapa);
			}
			*/
			
		}
		
		
		//Se devuelve la poblacion creada
		return Poblacion;
	}
	
	
	/**
	 * Funcion que selecciona 2 individuos de la poblacion con el mejor fitness entra un grupo de n individuos
	 * @param Poblacion_ Poblacion de la que se va a seleccionar a los individuos
	 * @param num_indiv_selecc Tamano de los grupos
	 * @return Individuos seleccionados con mejor fitness
	 */
	public ArrayList<Dungeon> selection (ArrayList<Dungeon> Poblacion_, int num_indiv_selecc)
	{
		
		ArrayList<Dungeon> Seleccionados = new ArrayList<>();
		ArrayList<Dungeon> Semi_Seleccionados = new ArrayList<>();
		ArrayList<Dungeon> Semi_Seleccionados2 = new ArrayList<>();
		
		
		/*//LOG
		System.out.print("\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("                     POBLACION                      \n");
		System.out.print("----------------------------------------------------\n");
		for (int i= 0; i < Poblacion_.size(); i++)
		{
			for(int tam_genotipo = 0; tam_genotipo < Poblacion_.get(0).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Poblacion_.get(i).genotipo[tam_genotipo]);
			}
			
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(i).fitness);
			System.out.println("");
			
		}
		
		
		System.out.print("----------------------------------------------------\n");
		System.out.print("\n");
		*/
		
		
		int tamano_poblacion = Poblacion_.size() -1;
		
		//Seleccionamos al azar a n individuos de la poblacion
		for (int i= 0; i < num_indiv_selecc; i++)
		{
			//System.out.print("Añado el individuo "+ i +" al array de semiseleccionados " + "\n");
			Semi_Seleccionados.add(Poblacion_.get(new Random().nextInt(((tamano_poblacion - 1) - 0) + 1) + 0));
			
		}
		
		/*//LOG
		System.out.print("\n");
		System.out.print("Individuos preseleccionados 1: \n");
		int i2 = Semi_Seleccionados.size();
		while (i2 > 0)
		{
			for(int tam_genotipo = 0; tam_genotipo < Semi_Seleccionados.get(i2 - 1).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Semi_Seleccionados.get(i2 - 1).genotipo[tam_genotipo]);
			}
			
			System.out.println("");
			System.out.println("Fitness: " + Semi_Seleccionados.get(i2 - 1).fitness);
			System.out.println("");
			i2--;
		}
		System.out.print("----------------------------------------------------\n");
		*/
		
		//se anade el primer elemento de los seleccionados al mas alto
		Seleccionados.add(0, Semi_Seleccionados.get(0));
		
		//De esos n individuos, se selecciona como progenitor al de mayor fitness
		for (int i= 0; i< num_indiv_selecc; i++)
		{
			//si el fitness del seleccionado guardado es nulo, guardamos el semiseleccionado como mejor
			if(Seleccionados.get(0).fitness == -100)
			{
				Seleccionados.set(0, Semi_Seleccionados.get(i));
			}
			
			//si el fitness del almacenado es superior al que se encuentra en semi, lo sustituimmos ya que cuanto menor fitness mejor es el individuo
			else if((Semi_Seleccionados.get(i).fitness < Seleccionados.get(0).fitness) && (Seleccionados.get(0).fitness >= 0))
			{
				Seleccionados.set(0, Semi_Seleccionados.get(i));
			}
		}
		
		//limpiamos el arraylist de semi_seleccionados
		Semi_Seleccionados.clear();

		
		
		//Seleccionamos al azar a n individuos de la poblacion
		for (int i = 0; i < num_indiv_selecc; i++)
		{
			//System.out.print("Añado el individuo "+ i +" al array de semiseleccionados2 " + "\n");
			Semi_Seleccionados2.add(Poblacion_.get(new Random().nextInt(((tamano_poblacion - 1) - 0) + 1) + 0));
			
		}
		
		/*//LOG
		System.out.print("\n");
		System.out.print("Individuos preseleccionados 2: \n");
		int i22 = Semi_Seleccionados2.size();
		while (i22 > 0)
		{
			
			for(int tam_genotipo = 0; tam_genotipo < Semi_Seleccionados2.get(i22 - 1).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Semi_Seleccionados2.get(i22 - 1).genotipo[tam_genotipo]);
			}
			
			System.out.println("");
			System.out.println("Fitness: " + Semi_Seleccionados2.get(i22 - 1).fitness);
			System.out.println("");
			
			i22--;
		}
		*/
		
		
		//se anade el primer elemento de los seleccionados al mas alto
		Seleccionados.add(1, Semi_Seleccionados2.get(0));
		
		
		//De esos n individuos, se selecciona como progenitor al de mayor fitness
		for (int i= 0; i< num_indiv_selecc; i++)
		{
			
			//si el fitness del almacenado es superior al que se encuentra en semi, lo sustituimmos ya que cuanto menor fitness mejor es el individuo
			if((Semi_Seleccionados2.get(i).fitness < Seleccionados.get(1).fitness) && (Semi_Seleccionados2.get(i).fitness >= 0))
			{
				Seleccionados.set(1, Semi_Seleccionados2.get(i));
			}
			
		}

		
		//limpiamos el arraylist de semi_seleccionados 2
		Semi_Seleccionados2.clear();
		
		/*//LOG
		System.out.print("\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("Individuos seleccionados: " + "\n");
		System.out.print(" \n");
		for (int tam_seleccionados = 0; tam_seleccionados < Seleccionados.size(); tam_seleccionados++)
		{
			for(int tam_genotipo = 0; tam_genotipo < Seleccionados.get(tam_seleccionados).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Seleccionados.get(tam_seleccionados).genotipo[tam_genotipo]);
			}
			System.out.println("");
			System.out.println("Fitness: " + Seleccionados.get(tam_seleccionados).fitness);
			System.out.println("");
			
		}
		
		System.out.print("----------------------------------------------------\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("\n");
		*/
		
		//se devuelven los individuos con mejor fitness
		return Seleccionados; 	
	}
	
	
	/**
	 * Funcion que va a reemplazar el mejor individuo de la nueva generacion con el peor individuo de los individuos seleccionados como peor generacion
	 * @param Poblacion_  Poblacion que se quiere modificar
	 * @param newGeneration Individuos que se han seleccionado de cara a mejorar la poblacion (solo se pondra el mejor de los 2)
	 * @return Devuelve la poblacion modificada con el mejor de la nueva generacion sustituido por el peor entre los seleccionados
	 */
	public ArrayList<Dungeon> replacement(ArrayList<Dungeon> Poblacion_, ArrayList<Dungeon> newGeneration) 
	{
		ArrayList<Dungeon> badGeneration = new ArrayList<Dungeon>();
		//ArrayList<Dungeon> Individuos_A_Reemplazar = new ArrayList<Dungeon>();
		
		int [] posicion_peores = new int[newGeneration.size()];
		
		int numero_Indiv_a_reemplazar = newGeneration.size();
		//int [] posicion_seleccionados_reemplazo = new int[newGeneration.size()];

		/*//LOG
		System.out.print("\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("            POBLACION SIN REEMPLAZO                 \n");
		System.out.print("----------------------------------------------------\n");
		for (int i= 0; i < Poblacion_.size(); i++)
		{
			for(int tam_genotipo = 0; tam_genotipo < Poblacion_.get(0).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Poblacion_.get(i).genotipo[tam_genotipo]);
			}
			
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(i).fitness);
			System.out.println("");
			
		}
		System.out.print("----------------------------------------------------\n");
		System.out.print("\n");
		*/
		
		//Seleccionamos los n individuos peores de la poblacion
		for (int i= 0; i < numero_Indiv_a_reemplazar; i++)
		{
			//recorremos la poblacion para buscar el individuo con peor fitness
			for(int individuo = 0; individuo < Poblacion_.size(); individuo++)
			{
				//si es el primer individuo a seleccionar como el peor no se comprueba nada
				if(i == 0)
				{
					//si estamos en el primer individuo lo seleccionamos como el peor
					if (individuo == 0)
					{
						badGeneration.add(Poblacion_.get(0));
						
						//guardo la posicion en la poblacion del peor individuo
						posicion_peores[i] = individuo;
						
					}
					
					//si estamos en el resto de individuos de la poblacion comprobamos si es peor al que hemos guardado
					else if (individuo > 0)
					{
						//si el individuo es peor al guardado, lo sustituimos
						if(badGeneration.get(i).fitness < Poblacion_.get(individuo).fitness)
						{
							badGeneration.set(i, Poblacion_.get(individuo));
							
							//guardo la posicion en la poblacion del peor individuo
							posicion_peores[i] = individuo;
							
						}
						
					}
				}	
				
				//SI ya hemos seleccionado algun individuo malo de la poblacion 
				//comprobamos que el que vamos a seleccionar no sea ninguno de los ya elegidos
				else
				{
					//si estamos en el primer individuo lo seleccionamos como el peor
					if (individuo == 0)
					{
						badGeneration.add(Poblacion_.get(0));
						
						//guardo la posicion en la poblacion del peor individuo
						posicion_peores[i] = individuo;
					}
					
					//si estamos en el resto de individuos de la poblacion comprobamos si es peor al que hemos guardado
					else if (individuo > 0)
					{
						//SI el individuo es peor que el que hemos guardado y no es igual al individuo que tenemos guardado anteriormente, entonces lo sustituimos
						if((badGeneration.get(i).fitness < Poblacion_.get(individuo).fitness) && (posicion_peores[0] != individuo))
						{
							
							badGeneration.set(i, Poblacion_.get(individuo));
							
							//guardo la posicion en la poblacion del peor individuo
							posicion_peores[i] = individuo;
							
						}
						
					}
					
				}
				
			}
				
		}
		
		//Sustituimos los individuos nuevos por los seleccionados como peores
		for(int new_indiv = 0; new_indiv < newGeneration.size(); new_indiv++)
		{
			//se modifican los individuos con peor fitness de la poblacion por los nuevos individuos
			Poblacion_.set(posicion_peores[new_indiv], newGeneration.get(new_indiv));
		}
		
		badGeneration = null;
		
		posicion_peores = null;
		
		/*//LOG
		System.out.print("\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("            POBLACION SIN REEMPLAZADA               \n");
		System.out.print("----------------------------------------------------\n");
		for (int i= 0; i < Poblacion_.size(); i++)
		{
			for(int tam_genotipo = 0; tam_genotipo < Poblacion_.get(0).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Poblacion_.get(i).genotipo[tam_genotipo]);
			}
			
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(i).fitness);
			System.out.println("");
			
		}
		
		System.out.println("Mutacion: " + mutaciones);
		
		System.out.print("----------------------------------------------------\n");
		System.out.print("\n");
		*/
		
		return Poblacion_;
	}
	
	/**
	 * Funcion que si el numero random que genera esta entre el 5%, muta aleatoriamente un gen de un individuo aleatorio de la poblacion
	 * @param Poblacion_ Poblacion que se va a mutar
	 * @return Devuelve la poblacion con la mutacion en el individuo correspondiente
	 */
	public ArrayList<Dungeon> mutation(ArrayList<Dungeon> Poblacion_) 
	{	
		
		/*//LOG	
		System.out.print("\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("               POBLACION SIN LA MUTACION            \n");
		System.out.print("----------------------------------------------------\n");
		for (int i= 0; i < Poblacion_.size(); i++)
		{
			for(int tam_genotipo = 0; tam_genotipo < Poblacion_.get(0).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Poblacion_.get(i).genotipo[tam_genotipo]);
			}
			
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(i).fitness);
			System.out.println("");
			
		}
		System.out.print("----------------------------------------------------\n");
		System.out.print("\n");
		*/
		
		
		//generamos un numero random entre 0 y 1 para hallar el porcentaje de si va a haber mutacion o no
		double random_num = Math.random();
		
		/*//LOG
		System.out.print("El numero random generado es: " + random_num + "\n");
		*/
		
		int tamano_poblacion = Poblacion_.size() - 1;
		
		
		System.out.println("El tam de la poblacion es: " + tamano_poblacion);
		System.out.println(" ");
		
		if(random_num < 0.05)// si sale un porcentaje de un 5% modificamos uno de los individuos de la poblacion
		{
			
			mutaciones++;
			
			int random_individuo_mutacion = new Random().nextInt(tamano_poblacion); //generar un numero random (((max - min) + 1) + min)
			
			
			/*//LOG
			System.out.print("Se procede a la mutacion del individuo: " + random_individuo_mutacion + "\n");
			*/
			
			Dungeon individuo_mutado = Poblacion.get(random_individuo_mutacion); // igualamos el individuo local al individuo que luego vamos a modificar para que tenga las variables igual
			
			int tam_genotipo = (individuo_mutado.f * individuo_mutado.c) - 1;
			
			//nos creamos una variable random que va a almacenar la posicion de la celda que va a modificar
			int genotipo_modif = new Random().nextInt(tam_genotipo); //(((individuo_mutado.f * individuo_mutado.c) - 1) + 1) + 1); //(((max - min) + 1) + min)
		
			
			/*//LOG
			System.out.print("Celda a modificar: " + genotipo_modif + "\n");
			
			System.out.println("Genotipo del individuo a mutar: ");
			for (int i = 0; i < individuo_mutado.genotipo.length; i++)
			{
				System.out.print(individuo_mutado.genotipo[i]);
			}
			
			System.out.println("");
			
			*/
			
			//Generamos un numero aleatorio entre el 0 y el 7 para determinar que tipo de celda va a ser la que se va a mutar
			int random_tipo_celda = new Random().nextInt(((7 - 0) + 1) + 0); 
			
			/*//LOG
			System.out.println("NUmero random: " + random_tipo_celda);
			*/
			
			//Variables para saber en que celda me encuentro y para poder calcular asi la posicion en la que me encuentro del genotipo
			int contador_celda = 0;
			int posicion = 0;
			
			//Recorremos el individuo para modificar el genotipo de la celda en el genotipo global
			for(int i = 0; i < individuo_mutado.dungeon.length; i++)
			{
				for(int j = 0; j < individuo_mutado.dungeon[i].length; j++)
				{	
					
					//Para cada bit del genotipo de cada celda genero un numero aleatorio (0 o 1)
					for(int n_tipo_celdas = 0; n_tipo_celdas < individuo_mutado.tipo_celdas; n_tipo_celdas++)
					{
						//posicion del array del genotipo donde se tiene que guardar cada bit
						posicion = (contador_celda * individuo_mutado.tipo_celdas) + n_tipo_celdas;
						
						
						//Si el contador de la celda coincide con el genotipo a modificar, se modifica
						if(contador_celda == genotipo_modif)
						{
							
							//si estoy en la posicion 0 y el numero generado es el 0
							if(n_tipo_celdas == 0 && random_tipo_celda == 0)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
						
							//si estoy en la posicion 1 y el numero generado es el 0
							else if(n_tipo_celdas == 1 && random_tipo_celda == 0)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
						
							//si estoy en la posicion 2 y el numero generado es el 0
							else if(n_tipo_celdas == 2 && random_tipo_celda == 0)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
							//****************************************************************************************
							
							//si estoy en la posicion 0 y el numero generado es el 1
							if(n_tipo_celdas == 0 && random_tipo_celda == 1)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
						
							//si estoy en la posicion 1 y el numero generado es el 1
							else if(n_tipo_celdas == 1 && random_tipo_celda == 1)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
						
							//si estoy en la posicion 2 y el numero generado es el 1
							else if(n_tipo_celdas == 2 && random_tipo_celda == 1)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
							//****************************************************************************************
							
							//si estoy en la posicion 0 y el numero generado es el 2
							if(n_tipo_celdas == 0 && random_tipo_celda == 2)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
						
							//si estoy en la posicion 1 y el numero generado es el 2
							else if(n_tipo_celdas == 1 && random_tipo_celda == 2)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
						
							//si estoy en la posicion 2 y el numero generado es el 2
							else if(n_tipo_celdas == 2 && random_tipo_celda == 2)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
							//****************************************************************************************
							
							//si estoy en la posicion 0 y el numero generado es el 3
							if(n_tipo_celdas == 0 && random_tipo_celda == 3)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
						
							//si estoy en la posicion 1 y el numero generado es el 3
							else if(n_tipo_celdas == 1 && random_tipo_celda == 3)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
						
							//si estoy en la posicion 2 y el numero generado es el 3
							else if(n_tipo_celdas == 2 && random_tipo_celda == 3)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
							//****************************************************************************************
							
							//si estoy en la posicion 0 y el numero generado es el 4
							if(n_tipo_celdas == 0 && random_tipo_celda == 4)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
						
							//si estoy en la posicion 1 y el numero generado es el 0
							else if(n_tipo_celdas == 1 && random_tipo_celda == 4)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
						
							//si estoy en la posicion 2 y el numero generado es el 4
							else if(n_tipo_celdas == 2 && random_tipo_celda == 4)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
							//****************************************************************************************
							
							//si estoy en la posicion 0 y el numero generado es el 5
							if(n_tipo_celdas == 0 && random_tipo_celda == 5)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
						
							//si estoy en la posicion 1 y el numero generado es el 5
							else if(n_tipo_celdas == 1 && random_tipo_celda == 5)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
						
							//si estoy en la posicion 2 y el numero generado es el 5
							else if(n_tipo_celdas == 2 && random_tipo_celda == 5)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
							//****************************************************************************************
							
							//si estoy en la posicion 0 y el numero generado es el 6
							if(n_tipo_celdas == 0 && random_tipo_celda == 6)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
						
							//si estoy en la posicion 1 y el numero generado es el 6
							else if(n_tipo_celdas == 1 && random_tipo_celda == 6)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
						
							//si estoy en la posicion 2 y el numero generado es el 6
							else if(n_tipo_celdas == 2 && random_tipo_celda == 6)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 0;
							}
							//****************************************************************************************
							
							//si estoy en la posicion 0 y el numero generado es el 7
							if(n_tipo_celdas == 0 && random_tipo_celda == 7)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
						
							//si estoy en la posicion 1 y el numero generado es el 7
							else if(n_tipo_celdas == 1 && random_tipo_celda == 7)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
						
							//si estoy en la posicion 2 y el numero generado es el 7
							else if(n_tipo_celdas == 2 && random_tipo_celda == 7)
							{
								//guardo el bit creado en el array con todos los bits del individuo
								individuo_mutado.genotipo[posicion] = 1;
							}
							//****************************************************************************************
						
						}
			
					}
					
					//Incremento el contador de la celda
					contador_celda++;
				}
			}
			
			
			/*//LOG
			System.out.println("Genotipo del individuo a mutar: ");
			for (int i = 0; i < individuo_mutado.genotipo.length; i++)
			{
				System.out.print(individuo_mutado.genotipo[i]);
			}
			
			System.out.println("");
			
			System.out.print("PARADA" + "\n");
			*/
			
			
			//modificamos el fitness del individuo y recalculamos el resto de parametros
			individuo_mutado.revisar_genotipo();
			individuo_mutado.posicion_monstruos = new ArrayList<Celda>();
			individuo_mutado.posicion_tesoros = new ArrayList<Celda>();
			individuo_mutado.numero_tesoros = individuo_mutado.comprobar_tesoros();
			individuo_mutado.numero_monstruos = individuo_mutado.comprobar_monstruos();
			individuo_mutado.ResetearDungeonCamino();
			individuo_mutado.generateDungeon(0,0);
			individuo_mutado.calcularfitness(individuo_mutado.numero_puertas);
			
			//si el individuo que hemos generado tiene un fitness nulo, se translada a la poblacion de no validos
			if (individuo_mutado.fitness == -100)
			{
				//se anade a la poblacion de no validos el individuo
				Poblacion_noValidos.add(Poblacion_.get(random_individuo_mutacion));
				
				//se elimina el individuo de la poblacion ya que se ha pasado a la poblacion de no validos
				Poblacion_.remove(random_individuo_mutacion);
				
				
			}
			else
			{
				//modificamos el individuo en la poblacion con los datos que hemos modificado en el individuo mutado
				Poblacion_.set(random_individuo_mutacion, individuo_mutado); 
			}
			
			
			
			
			/*//LOG
			System.out.print("Genotipo a modificar: " +genotipo_modif+  "\n");
			*/
			
			/*
			//si el genotipo seleccionado aleatoriamente es 0 lo ponemos a 1 y si es 1 lo ponemos a 0
			if(individuo_mutado.genotipo[genotipo_modif] == 0)
			{
				individuo_mutado.genotipo[genotipo_modif] = 1;
			}
			else
			{
				individuo_mutado.genotipo[genotipo_modif] = 0;
			}
			
			//modificamos el fitness del individuo y recalculamos el resto de parametros
			individuo_mutado.revisar_genotipo();
			individuo_mutado.posicion_monstruos = new ArrayList<Celda>();
			individuo_mutado.posicion_tesoros = new ArrayList<Celda>();
			individuo_mutado.numero_tesoros = individuo_mutado.comprobar_tesoros();
			individuo_mutado.numero_monstruos = individuo_mutado.comprobar_monstruos();
			individuo_mutado.ResetearDungeonCamino();
			individuo_mutado.generateDungeon(0,0);
			individuo_mutado.calcularfitness(individuo_mutado.numero_puertas);
			
			//si el individuo que hemos generado tiene un fitness nulo, se translada a la poblacion de no validos
			if (individuo_mutado.fitness == -100)
			{
				//se anade a la poblacion de no validos el individuo
				Poblacion_noValidos.add(Poblacion_.get(random_individuo_mutacion));
				
				//se elimina el individuo de la poblacion ya que se ha pasado a la poblacion de no validos
				Poblacion_.remove(random_individuo_mutacion);
				
				
			}
			else
			{
				//modificamos el individuo en la poblacion con los datos que hemos modificado en el individuo mutado
				Poblacion_.set(random_individuo_mutacion, individuo_mutado); 
			}
			
			*/
			
		}
			
		/*//LOG	
		System.out.print("\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("               POBLACION CON LA MUTACION            \n");
		System.out.print("----------------------------------------------------\n");
		for (int i= 0; i < Poblacion_.size(); i++)
		{
			for(int tam_genotipo = 0; tam_genotipo < Poblacion_.get(0).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Poblacion_.get(i).genotipo[tam_genotipo]);
			}
			
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(i).fitness);
			System.out.println("");
			
		}
		System.out.print("----------------------------------------------------\n");
		
		System.out.print("\n");
		*/
		
		return Poblacion_;
	}
	
	/**
	 * Funcion que cruza la antigua generacion para formar la nueva generacion
	 * @param parents Padres que van a ser cruzados
	 * @return Descencientes que han salido del cruce de los padres
	 */
	public ArrayList<Dungeon> crossover(ArrayList<Dungeon> parents) 
	{
		//Arraylist de los individuos que van a ser los progenitores
		ArrayList<Dungeon> Descendientes = new ArrayList<Dungeon>();
		
		Descendientes = parents;
		
		int[] genotipo_hijo1 = new int[parents.get(0).genotipo.length];
		int[] genotipo_hijo2 = new int[parents.get(1).genotipo.length];
		
		int[] mascara;
		
		
		/*//LOG
		if(trace)
		{
			System.out.print("Los padres: \n");
			
			for(int tam_gen = 0; tam_gen < parents.get(0).genotipo.length; tam_gen++)
			{
				System.out.print(parents.get(0).genotipo[tam_gen]);
			}
			System.out.println("");
			
			for(int tam_gen = 0; tam_gen < parents.get(1).genotipo.length; tam_gen++)
			{
				System.out.print(parents.get(1).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("");

		}*/
		
		
		//Generamos la probabilidad de que sea 0 o 1 a traves de la fórmula de Bernoulli
		double probabilidad1 = parents.get(0).fitness;
		double probabilidad2 =parents.get(0).fitness + parents.get(1).fitness;
		double probabilidad = probabilidad1 / probabilidad2;
		
		
		//Inicializamos la máscara
		mascara = new int[parents.get(0).genotipo.length];
		
		double random_num_masc; //numero que vamos a usar para generar la mascara de forma algo aleatoria
		
		//Guardamos un número random entre 0 y 1 en cada posicion de la máscara
		for (int i = 0; i < parents.get(0).genotipo.length; i++)
		{
			random_num_masc = Math.random(); //generamos un numero random entre 0 y 1
			
			if (random_num_masc > probabilidad ) // si el numero generado es mayor a la probabilidad entonces establecemos un 1 en la posicion i de la máscara
			{
				mascara[i] = 1;
			}
			else
			{
				mascara[i] = 0;
			}
			
		}
		
		
		//Mostramos la mascara por pantalla
		/*//LOG
		if(trace)
		{
			System.out.print("Mascara:\n");
			for(int tam_gen = 0; tam_gen < mascara.length; tam_gen++)
			{
				System.out.print(mascara[tam_gen]);
			}
			System.out.println("");
			System.out.print("\n");
		}*/
		
		//Inicializamos los descendientes
		
		for(int tam_gen = 0; tam_gen < Descendientes.get(0).genotipo.length; tam_gen++)
		{
			genotipo_hijo1[tam_gen] = 0;
			genotipo_hijo2[tam_gen] = 0;
		}
		
		//Recorremos cada genotipo
		for(int i = 0; i < mascara.length; i++) 
		{
			
			
			//Si el genotipo en la máscara es 1 hacemos un swap y guardamos el genotipo del padre 1 en el genotipo del hijo 2 y viceversa
			if(mascara[i] == 1)
			{
				genotipo_hijo1[i] = parents.get(1).genotipo[i];	
				genotipo_hijo2[i] = parents.get(0).genotipo[i];

			}
			else
			{
				genotipo_hijo1[i] = parents.get(0).genotipo[i];	
				genotipo_hijo2[i] = parents.get(1).genotipo[i];	

			}
		}
		
		
		//individuo para poder modificar el arraylist de hijos
		//Dungeon individuo1 = parents.get(0);
		Dungeon individuo = null;
		
		ArrayList<Tipo_puertas> t_puertas = new ArrayList<Tipo_puertas>();
		ArrayList<int[]> pos_puertas = new ArrayList<int[]>();
		//Creo las posiciones de las puertas
		int [] puerta_1 = {((parents.get(0).f / 2) - 1), 0}; // puerta centrada en el lado Oeste
		int [] puerta_2 = {0, ((parents.get(0).c/2) - 1)}; //puerta centrada en el lado Norte
		int [] puerta_3 = {(parents.get(0).f-1), ((parents.get(0).c/2) - 1)}; //puerta centrada en el lado Sur
		int [] puerta_4 = {((parents.get(0).f / 2) - 1), (parents.get(0).c-1)}; ////puerta centrada en el lado Este
		
		
		//Anado las puertas a la lista con sus respectivas posiciones
		pos_puertas.add(puerta_1);
		pos_puertas.add(puerta_2);
		pos_puertas.add(puerta_3);
		pos_puertas.add(puerta_4);
		
		individuo = new Dungeon(parents.get(0).f, parents.get(0).c, parents.get(0).numero_monstruos, parents.get(0).numero_tesoros, pos_puertas, t_puertas, parents.get(0).numero_puertas, parents.get(0).porcentaje, parents.get(0).porcentaje_paredes, parents.get(0).tipo_celdas, parents.get(0).dificultad_nivel, parents.get(0).ponderaciones_nivel);
		
		int [] genotipo_individuo = genotipo_hijo1;
		
		individuo.genotipo = genotipo_individuo;
		
		//se calculan el resto de cosas del individuo para luego almacenarlo en descendientes
		individuo.revisar_genotipo();
		individuo.posicion_monstruos = new ArrayList<Celda>();;
		individuo.posicion_tesoros = new ArrayList<Celda>();;
		individuo.numero_tesoros = individuo.comprobar_tesoros();
		individuo.numero_monstruos = individuo.comprobar_monstruos();
		individuo.ResetearDungeonCamino();
		individuo.generateDungeon(0,0);
		individuo.calcularfitness(individuo.numero_puertas);
		
		
		//si el fitness del individuo sale nulo entonces se translada a la poblacion de no validos y se copia un padre como sucesor
		/*if(individuo.fitness == -100)
		{
			//se modifica el segundo hijo con el genotipo correspondiente y los datos del individuo correspondientes
			Descendientes.set(0, parents.get(0));
			
			Poblacion_noValidos.add(individuo);
			
		}
		else
		{
			//se modifica el segundo hijo con el genotipo correspondiente y los datos del individuo correspondientes
			Descendientes.set(0, individuo);
		}*/
		
		
		//se modifica el segundo hijo con el genotipo correspondiente y los datos del individuo correspondientes
		Descendientes.set(0, individuo);
		
		genotipo_individuo = null;
		individuo = null; //se pone a null para poder inicializar la variable de nuevo
		
		individuo = new Dungeon(parents.get(0).f, parents.get(0).c, parents.get(0).numero_monstruos, parents.get(0).numero_tesoros, pos_puertas, t_puertas, parents.get(0).numero_puertas, parents.get(0).porcentaje, parents.get(0).porcentaje_paredes, parents.get(0).tipo_celdas, parents.get(0).dificultad_nivel, parents.get(0).ponderaciones_nivel);
		
		
		//individuo = parents.get(1);
		genotipo_individuo = genotipo_hijo2;
		individuo.genotipo = genotipo_individuo;
		
		
		//se calculan el resto de cosas del individuo para luego almacenarlo en descendientes
		individuo.revisar_genotipo();
		individuo.posicion_monstruos = new ArrayList<Celda>();;
		individuo.posicion_tesoros = new ArrayList<Celda>();;
		individuo.numero_tesoros = individuo.comprobar_tesoros();
		individuo.numero_monstruos = individuo.comprobar_monstruos();
		individuo.ResetearDungeonCamino();
		individuo.generateDungeon(0,0);
		individuo.calcularfitness(individuo.numero_puertas);
		
		
		//si el fitness del individuo sale nulo entonces se translada a la poblacion de no validos y se copia un padre como sucesor
		/*if(individuo.fitness == -100)
		{
			//se modifica el segundo hijo con el genotipo correspondiente y los datos del individuo correspondientes
			Descendientes.set(1, parents.get(1));
			
			Poblacion_noValidos.add(individuo);
			
		}
		else
		{
			//se modifica el segundo hijo con el genotipo correspondiente y los datos del individuo correspondientes
			Descendientes.set(1, individuo);
		}*/
		
		//se modifica el segundo hijo con el genotipo correspondiente y los datos del individuo correspondientes
		Descendientes.set(1, individuo);
		
			
		
		//Mostramos los descendientes
		/*//LOG
		if(trace)
		{
		
			System.out.print("\n");
			System.out.print("Hijos: \n");
			
			
			for(int tam_gen = 0; tam_gen < Descendientes.get(0).genotipo.length; tam_gen++)
			{
				System.out.print(Descendientes.get(0).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + Descendientes.get(0).fitness);
			System.out.println("");
			
			for(int tam_gen = 0; tam_gen < Descendientes.get(1).genotipo.length; tam_gen++)
			{
				System.out.print(Descendientes.get(1).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + Descendientes.get(1).fitness);
			System.out.println("");
			System.out.print("\n");
		
		}*/
			
			
		return Descendientes; //Devolvemos a los hijos descendientes
	}
	
	/**
	 * Funcion que comprueba si esta evolucionando la poblacion o esta empeorando, si es asi, para la ejecucion
	 * @param Poblacion_ Poblacion a comprobar para ver si esta mejorando
	 * @return Devuelve true si no evoluciona mas la poblacion o ha encontrado al individuo idoneo
	 */
	public boolean converge(ArrayList<Dungeon> Poblacion_) 
	{		
		

		//Si llegamos a mas de 100 iteraciones que el mejor individuo de la poblacion es el mismo devolvemos true
		
		if( contador_iteraciones < 100)
		{
			//si estamos en la primera iteracion, debido a que no hay ningun individuo de parada guardado se inicializan las variables a individuos con un mal fitness
			if (contador_iteraciones == 0)
			{

				individuo_parada.set_fitness(3000);

				//se modifica el individuo temporal con un fitness malo para que luego se reemplace
				Individuos_parada.set(0, individuo_parada);		
				
				
				//se anade el individuo de parada modificado con un mal fitness
				Individuos_parada.set(1, Individuos_parada.get(0));
				
				
				System.out.println("Fitness indiv temporal     : " + Individuos_parada.get(0).fitness);
				System.out.println("Fitness indiv anterior iter: " + Individuos_parada.get(1).fitness);
				
			}
			
			//si ya hemos dado la primera iteracion, entonces el individuo de parada va a ser el individuo de la anterior iteracion
			else
			{
				//se pone el individuo de la anterior iteracion en el individuo temporal (una copia, el de la anterior iteracion no se toca)
				//Individuos_parada.set(0, Individuos_parada.get(1));		
				
				
				System.out.println("Fitness indiv temporal     : " + Individuos_parada.get(0).fitness);
				System.out.println("Fitness indiv anterior iter: " + Individuos_parada.get(1).fitness);
				
				
				System.out.print("\n");
				System.out.print("----------------------------------------------------\n");
				System.out.print("                     POBLACION                      \n");
				System.out.print("----------------------------------------------------\n");
				for (int i= 0; i < Poblacion_.size(); i++)
				{
					for(int tam_genotipo = 0; tam_genotipo < Poblacion_.get(0).genotipo.length; tam_genotipo++)
					{
						
						System.out.print(Poblacion_.get(i).genotipo[tam_genotipo]);
					}
					
					System.out.println("");
					System.out.println("Fitness: " + Poblacion_.get(i).fitness);
					System.out.println("");
					
				}
				System.out.print("----------------------------------------------------\n");
				System.out.print("\n");
				
			}
			
			
			
			//Recorremos la poblacion
			for(int i = 0 ; i < Poblacion_.size() ; i++)
			{
				
				//se guarda al mejor individuo sin que sea nulo
				if((Poblacion_.get(i).fitness < Individuos_parada.get(1).fitness) && (Poblacion_.get(i).fitness >= 0))
				{
					Individuos_parada.set(1, Poblacion_.get(i));
					//individuo_parada = Poblacion_.get(i); //guardamos al mejor individuo de la poblacion
					
				}
			}	
			
			//si ninguno de los individuos guardados es negativo y ademas, estamos en la iteracion 1, comprobamos si estamos mejorando
			if ((Individuos_parada.get(0).fitness >= 0) && (Individuos_parada.get(1).fitness >= 0) && (contador_iteraciones > 0))
			{
				//Si el individuo que hemos guardado como el mejor de la poblacion no es mejor al individuo que hemos guardado en la anterior iteracion, paramos
				if(Individuos_parada.get(1).fitness > Individuos_parada.get(0).fitness)
				{
					
					motivo = 0;
					
					stop = true;
					
				}
				
				else
				{	
				
					//Si el individuo parada es mejor que el individuo que hemos copiado anteriormente, entonces lo guardamos como el mejor individuo de las iteraciones
					if (Individuos_parada.get(1).fitness < Individuos_parada.get(0).fitness)
					{
						
						Individuos_parada.set(0, Individuos_parada.get(1));
						
					}
					
					
			
				}
			}
			
			//si el individuo que hemos conseguido es el idóneo paramos
			if(Individuos_parada.get(1).fitness == 0.0)
			{
				
				Individuos_parada.set(0, Individuos_parada.get(1));
				
				motivo = 1;				
				stop = true;
				
			}
			
			contador_iteraciones++; 
		}
		
		//Si llevamos mas iteraciones de las estipuladas y no hemos encontrado al individuo idoneo, guardamos el mejor individuo de esa poblacion
		else
		{
			
			//recorremos la poblacion
			for(int i = 0 ; i < Poblacion_.size() ; i++)
			{
				
				//si el individuo de parada tiene un fitness negativo, se guarda como el mejor el individuo de la poblacion
				if (Individuos_parada.get(0).fitness < 0)
				{
					//si el individuo de la poblacion no es negativo, entonces modificamos ambos individuos de parada
					if(Poblacion_.get(i).fitness >= 0)
					{
						Individuos_parada.set(0, Poblacion_.get(i));
						Individuos_parada.set(1, Poblacion_.get(i));
					}
				}
				
				//se comprueba que el individuo i no sea mejor que el guardado en la anterior iteracion
				if((Poblacion_.get(i).fitness < Individuos_parada.get(0).fitness) && (Poblacion_.get(i).fitness >= 0))
				{
					//se modifica el individuo 1 para guardar el mejor individuo de la poblacion que supera la anterior iteracion
					Individuos_parada.set(1, Poblacion_.get(i));
				
				}
				
				
				
			}
			
			
			//una vez comprobado si hay un individuo mejor en la poblacion que la anterior iteracion, se modifica el individuo 0 idoneo
			
			if(Individuos_parada.get(1).fitness < Individuos_parada.get(0).fitness)
			{
				//se guarda el mejor individuo en la posicion 0
				Individuos_parada.set(0, Individuos_parada.get(1));
			}
			
			
			/*
			
			if (Individuos_parada.get(1).fitness == -100)
			{
				Individuos_parada.get(0).fitness = 6000;
				
				
				//Recorremos la poblacion
				for(int i = 0 ; i < Poblacion_.size() ; i++)
				{

					//se guarda al mejor individuo sin que sea nulo
					if((Poblacion_.get(i).fitness < Individuos_parada.get(0).fitness) && (Poblacion_.get(i).fitness >= 0))
					{
						Individuos_parada.set(0, Poblacion_.get(i));
					
					}
				}	
			}
			
			//si el individuo de la anterior iteracion no tiene un fitness negativo, ponemos que el mejor individuo es el de la anterior iteracion
			else
			{
				Individuos_parada.set(0, Individuos_parada.get(1));
			}
			*/
			
			motivo = 2;
			stop = true;
		}
		
		
		return stop;
	}

	/**
	 * Funcion que se encarga de pintar los mapas de los individuos 
	 * @param Poblacion
	 */
	public void pintar_poblacion(ArrayList<Dungeon> Poblacion_)
	{
		
		for(int j= 0; j<Poblacion_.size(); j++)
		{
			pintar_individuo(j, Poblacion_);
		}
	}
	
	
	/**
	 * Funcion que pinta a un individuo
	 * @param Individuo Posicion en la que se encuentra el individuo en la poblacion
	 * @param Poblacion_ Poblacion de individuos
	 */
	public void pintar_individuo(int Individuo, ArrayList<Dungeon> Poblacion_)
	{
		//Recorro la poblacion
		for(int j= 0; j<Poblacion_.size(); j++)
		{
			//si j coincide con la posicion en la que se encuentra el individuo pintamos dicho individuo
			if(j == Individuo)
			{
				System.out.println(" ");
				System.out.println(" ");
				System.out.println("-----------------------");
				System.out.println("Individuo " + j );
				Poblacion_.get(Individuo).pintar();
				
			}
			
		}

		
	}
}
