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
	
	//Variables para saber cuantos individuos de la poblacion son validos y cuantos son invalidos
	int Validos;
	int No_validos;
	
	/**
	 * Constructor de EvPopulation
	 */
	public EvPopulation ()
	{
		//inicializo las variables a 0
		Validos = 0;
		No_validos = 0;
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
		
		//Se crea un mapa por cada individuo y se anade a la poblacion
		for(int i = 0; i<numero_poblacion; i++)
		{
			
			mapa = new Dungeon(f, c,  numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas, dificultad_nivel, ponderaciones_nivel); //El dungeon se pasa con las dimensiones (x, y)
			
			
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
		
		
		//Seleccionamos al azar a n individuos de la poblacion
		for (int i= 0; i < num_indiv_selecc; i++)
		{
			//System.out.print("Añado el individuo "+ i +" al array de semiseleccionados " + "\n");
			Semi_Seleccionados.add(Poblacion_.get(new Random().nextInt(((Poblacion.size()- 1) - 0) + 1) + 0));
			
		}
		
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
		
		//se anade el primer elemento de los seleccionados al mas alto
		Seleccionados.add(0, Semi_Seleccionados.get(0));
		System.out.print("----------------------------------------------------\n");
		
		//De esos n individuos, se selecciona como progenitor al de mayor fitness
		for (int i= 0; i< num_indiv_selecc; i++)
		{
			//si el fitness del almacenado es superior al que se encuentra en semi, lo sustituimmos ya que cuanto menor fitness mejor es el individuo
			if(Semi_Seleccionados.get(i).fitness < Seleccionados.get(0).fitness)
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
			Semi_Seleccionados2.add(Poblacion_.get(new Random().nextInt(((Poblacion.size()- 1) - 0) + 1) + 0));
			
		}
		
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
		
		
		//se anade el primer elemento de los seleccionados al mas alto
		Seleccionados.add(1, Semi_Seleccionados2.get(0));
		
		
		//De esos n individuos, se selecciona como progenitor al de mayor fitness
		for (int i= 0; i< num_indiv_selecc; i++)
		{
			
			//si el fitness del almacenado es superior al que se encuentra en semi, lo sustituimmos ya que cuanto menor fitness mejor es el individuo
			if(Semi_Seleccionados2.get(i).fitness < Seleccionados.get(1).fitness)
			{
				Seleccionados.set(1, Semi_Seleccionados2.get(i));
			}
			
		}

		
		//limpiamos el arraylist de semi_seleccionados 2
		Semi_Seleccionados2.clear();
		
		
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
		ArrayList<Dungeon> Individuos_A_Reemplazar = new ArrayList<Dungeon>();
		
		int [] posicion_peores = new int[newGeneration.size()];
		
		int numero_Indiv_a_reemplazar = newGeneration.size();
		int [] posicion_seleccionados_reemplazo = new int[newGeneration.size()];
		
		
		/*
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
		
		
		System.out.print("\n");
		System.out.print("----------------------------------------------------\n");
		
		System.out.print("\n");
		System.out.print("Individuos seleccionados a reemplazar 1: \n");
		//Seleccionamos al azar a n individuos de la poblacion
		for (int i= 0; i < numero_Indiv_a_reemplazar; i++)
		{
			//System.out.print("Añado el individuo "+ i +" al array de individuos a reemplazar " + "\n");
			posicion_seleccionados_reemplazo[i] = new Random().nextInt(((Poblacion_.size() - 1) - 0) + 1) + 0;
			
			if (i == 1) //solo cuando estemos en una posicion 1 
			{
				//si la posicion seleccionada es la misma que la anterior, volvemos a sacar otra posicion random
				while(posicion_seleccionados_reemplazo[i] == posicion_seleccionados_reemplazo[i - 1])
				{
					posicion_seleccionados_reemplazo[i] = new Random().nextInt(((Poblacion_.size() - 1) - 0) + 1) + 0;
				}
			}
			
			System.out.print("Posicion seleccionados reemplazo " + i + ": " + posicion_seleccionados_reemplazo[i] + "\n");
			
			for(int tam_gen = 0; tam_gen < Poblacion_.get(posicion_seleccionados_reemplazo[i]).genotipo.length; tam_gen++)
			{
				System.out.print(Poblacion_.get(posicion_seleccionados_reemplazo[i]).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(posicion_seleccionados_reemplazo[i]).fitness);
			System.out.println("");
			
			
			Individuos_A_Reemplazar.add(Poblacion_.get(posicion_seleccionados_reemplazo[i]));
			
		}
		
		System.out.print("\n");
		
		
		// Guardamos el peor de los dos seleccionados
		if(Individuos_A_Reemplazar.get(0).fitness > Individuos_A_Reemplazar.get(1).fitness)
		{
			/*System.out.print("\n");
			System.out.print("Individuo guardado 1: \n");
			*/
			badGeneration.add(0, Poblacion_.get(posicion_seleccionados_reemplazo[0]));
			posicion_peores[0] = posicion_seleccionados_reemplazo[0];
			
			/*System.out.println("Posicion: " + posicion_peores[0]);
			
			for(int tam_gen = 0; tam_gen < Poblacion_.get(posicion_seleccionados_reemplazo[0]).genotipo.length; tam_gen++)
			{
				System.out.print(Poblacion_.get(posicion_seleccionados_reemplazo[0]).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(posicion_seleccionados_reemplazo[0]).fitness);
			System.out.println("");
			*/
			
		}
		else
		{
			/*System.out.print("\n");
			System.out.print("Individuo guardado 1: \n");
			*/
			badGeneration.add(0, Poblacion_.get(posicion_seleccionados_reemplazo[1]));
			posicion_peores[0] = posicion_seleccionados_reemplazo[1];
			
			/*System.out.println("Posicion: " + posicion_peores[0]);
			*
			for(int tam_gen = 0; tam_gen < Poblacion_.get(posicion_seleccionados_reemplazo[1]).genotipo.length; tam_gen++)
			{
				System.out.print(Poblacion_.get(posicion_seleccionados_reemplazo[1]).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(posicion_seleccionados_reemplazo[1]).fitness);
			System.out.println("");*/
			
		}
		
		//borramos el arraylist de los individuos a reemplazar para la segunda vuelta
		Individuos_A_Reemplazar.clear();
		
		System.out.print("----------------------------------------------------\n");
		System.out.print("\n");
		
		
		//Seleccionamos al azar a n individuos de la poblacion 2
		int [] posicion_seleccionados_reemplazo2 = new int[newGeneration.size()];
		for (int i= 0; i < numero_Indiv_a_reemplazar; i++)
		{
			
			posicion_seleccionados_reemplazo2[i] = new Random().nextInt(((Poblacion_.size() - 1) - 0) + 1) + 0;
			
			//si el seleccionado es igual al que hemos guardado en 1 volvemos a hacer random para no tener problemas a la hora de reemplazar
			while(posicion_seleccionados_reemplazo2[i] == posicion_peores[0])
			{
				posicion_seleccionados_reemplazo2[i] = new Random().nextInt(((Poblacion_.size() - 1) - 0) + 1) + 0;
			}
			System.out.print("Posicion seleccionados reemplazo(2) " + i + ": " + posicion_seleccionados_reemplazo2[i] + "\n");
			
			for(int tam_gen = 0; tam_gen < Poblacion_.get(0).genotipo.length; tam_gen++)
			{
				System.out.print(Poblacion_.get(posicion_seleccionados_reemplazo2[i]).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(posicion_seleccionados_reemplazo2[i]).fitness);
			System.out.println("");
			
			Individuos_A_Reemplazar.add(i,Poblacion_.get(posicion_seleccionados_reemplazo2[i]));
			
		}
		
		
		// Guardamos el peor de los dos seleccionados
		if(Individuos_A_Reemplazar.get(0).fitness > Individuos_A_Reemplazar.get(1).fitness)
		{
			/*System.out.print("\n");
			System.out.print("Individuo guardado 2: \n");
			*/
			
			badGeneration.add(1, Poblacion_.get(posicion_seleccionados_reemplazo2[0]));
			posicion_peores[1] = posicion_seleccionados_reemplazo2[0];
			
			/*
			System.out.println("Posicion: " + posicion_peores[1]);
			
			for(int tam_gen = 0; tam_gen < Poblacion_.get(posicion_seleccionados_reemplazo2[0]).genotipo.length; tam_gen++)
			{
				System.out.print(Poblacion_.get(posicion_seleccionados_reemplazo2[0]).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(posicion_seleccionados_reemplazo2[0]).fitness);
			System.out.println("");*/
			
		}
		else
		{
			/*
			System.out.print("\n"); 
			System.out.print("Individuo guardado 2: \n");
			*/
			
			badGeneration.add(1, Poblacion_.get(posicion_seleccionados_reemplazo2[1]));
			posicion_peores[1] = posicion_seleccionados_reemplazo2[1];
			/*
			System.out.println("Posicion: " + posicion_peores[1]);
			
			for(int tam_gen = 0; tam_gen < Poblacion_.get(posicion_seleccionados_reemplazo2[1]).genotipo.length; tam_gen++)
			{
				System.out.print(Poblacion_.get(posicion_seleccionados_reemplazo2[1]).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + Poblacion_.get(posicion_seleccionados_reemplazo2[1]).fitness);
			System.out.println("");*/
			
		}
		
		
		System.out.print("----------------------------------------------------\n");
		System.out.print("----------------------------------------------------\n");
		System.out.println("Individuos seleccionados para reemplazo");

		for(int tam_bad_gen = 0; tam_bad_gen < badGeneration.size(); tam_bad_gen++)
		{
			System.out.println("Posicion: " + posicion_peores[tam_bad_gen]);
			
			for(int tam_gen = 0; tam_gen < badGeneration.get(tam_bad_gen).genotipo.length; tam_gen++)
			{
				System.out.print(badGeneration.get(tam_bad_gen).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + badGeneration.get(tam_bad_gen).fitness);
			System.out.println("");
		}
		
		System.out.print("----------------------------------------------------\n");
		System.out.print("----------------------------------------------------\n");
		

		//Seleccinamos cual es el mejor entre la nueva generacion
		
		Dungeon high_fitness = badGeneration.get(0);
		
		
		for(int i= 0; i < newGeneration.size(); i++)
		{
			//si el fitness del individuo i es mejor que el individuo 0, se guarda como el candidato para sustituir
			if(newGeneration.get(i).fitness < high_fitness.fitness)
			{
				high_fitness = newGeneration.get(i);
			}
		}		
		System.out.print("\n");
		System.out.print("\n");
		System.out.print("Individuo con mejor fitness: \n");
		
		for(int tam_gen = 0; tam_gen < high_fitness.genotipo.length; tam_gen++)
		{
			System.out.print(high_fitness.genotipo[tam_gen]);
		}
		System.out.println("");
		System.out.println("Fitness: " + high_fitness.fitness);
		System.out.println("");
		
		
		//Seleccionamos cual es el peor de los dos seleccionados y lo sustituimos por el mejor
	
		
		// Guardamos el peor de los dos seleccionados y lo modificamos en la poblacion
		if(badGeneration.get(0).fitness > badGeneration.get(1).fitness)
		{
			System.out.print("\n");
			System.out.print("Individuo peor: \n");
			
			System.out.println("Posicion: " + posicion_peores[0]);
			
			for(int tam_gen = 0; tam_gen < badGeneration.get(0).genotipo.length; tam_gen++)
			{
				System.out.print(badGeneration.get(0).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + badGeneration.get(0).fitness);
			System.out.println("");
			
			
			Poblacion_.set(posicion_peores[0], high_fitness);
			
		}
		else
		{
			System.out.print("\n");
			System.out.print("Individuo peor: \n");
			
			System.out.println("Posicion: " + posicion_peores[1]);
			
			for(int tam_gen = 0; tam_gen < badGeneration.get(1).genotipo.length; tam_gen++)
			{
				System.out.print(badGeneration.get(1).genotipo[tam_gen]);
			}
			System.out.println("");
			System.out.println("Fitness: " + badGeneration.get(1).fitness);
			System.out.println("");
			
			
			Poblacion_.set(posicion_peores[1], high_fitness);
			
		}		
		
		return Poblacion_;
	}
	
	/**
	 * Funcion que si el numero random que genera esta entre el 5%, muta aleatoriamente un gen de un individuo aleatorio de la poblacion
	 * @param Poblacion_ Poblacion que se va a mutar
	 * @return Devuelve la poblacion con la mutacion en el individuo correspondiente
	 */
	public ArrayList<Dungeon> mutation(ArrayList<Dungeon> Poblacion_) 
	{	
		
		//generamos un numero random entre 0 y 1 para hayar el porcentaje de si va a haber mutacion o no
		double random_num = Math.random();
		
		System.out.print("El numero random generado es: " + random_num + "\n");
		if(random_num < 0.05)// si sale un porcentaje de un 5% modificamos uno de los individuos de la poblacion
		{
			int random_individuo_mutacion = new Random().nextInt((((Poblacion_.size() - 1) - 0) + 1) + 0); //generar un numero random (((max - min) + 1) + min)
			//System.out.print("El numero random es: " + random_individuo_mutacion +"\n");
			System.out.print("Se procede a la mutacion del individuo: " + random_individuo_mutacion + "\n");
			
			
			Dungeon individuo_mutado = Poblacion.get(random_individuo_mutacion); // igualamos el individuo local al individuo que luego vamos a modificar para que tenga las variables igual
			
			
			//nos creamos una variable random que va a almacenar una posicion del genotipo que va a ser modificada
			int genotipo_modif = new Random().nextInt((((individuo_mutado.genotipo.length - 1) - 0) + 1) + 0);
			System.out.print("Genotipo a modificar: " +genotipo_modif+  "\n");
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
			individuo_mutado.numero_tesoros = individuo_mutado.comprobar_tesoros();
			individuo_mutado.numero_monstruos = individuo_mutado.comprobar_monstruos();
			individuo_mutado.generateDungeon(0,0);
			individuo_mutado.calcularfitness(individuo_mutado.numero_puertas);
			
			
			
			Poblacion_.set(random_individuo_mutacion, individuo_mutado); //modificamos el individuo en la poblacion con los datos que hemos modificado en el individuo mutado
			
			
		}
			
		/*//LOG	
		System.out.print("POBLACION ACTUAL: " + "\n");

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
		
		return Poblacion_;
	}
	
	
	public ArrayList<Dungeon> crossover(ArrayList<Dungeon> parents) 
	{
		//Arraylist de los individuos que van a ser los progenitores
		ArrayList<Dungeon> Descendientes = new ArrayList<Dungeon>();
		
		Descendientes = parents;
		
		int[] genotipo_hijo1 = new int[parents.get(0).genotipo.length];
		int[] genotipo_hijo2 = new int[parents.get(1).genotipo.length];
		
		int[] mascara;
		
		
		/*if(trace)
		{*/
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

		/*}*/
		
		
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
		/*if(trace)
		{*/
			System.out.print("Mascara:\n");
			for(int tam_gen = 0; tam_gen < mascara.length; tam_gen++)
			{
				System.out.print(mascara[tam_gen]);
			}
			System.out.println("");
			System.out.print("\n");
		/*}*/
		
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
		Dungeon individuo = parents.get(0);
		
		individuo.genotipo = genotipo_hijo1;
		
		//se calculan el resto de cosas del individuo para luego almacenarlo en descendientes
		individuo.revisar_genotipo();
		individuo.numero_tesoros = individuo.comprobar_tesoros();
		individuo.numero_monstruos = individuo.comprobar_monstruos();
		individuo.generateDungeon(0,0);
		individuo.calcularfitness(individuo.numero_puertas);
		
		
		//se modifica el primer hijo con el genotipo correspondiente y los datos del individuo correspondientes
		Descendientes.set(0, individuo);
		
		individuo = null; //se pone a null para poder inicializar la variable de nuevo
		
		individuo = parents.get(1);
		individuo.genotipo = genotipo_hijo2;
		
		
		//se calculan el resto de cosas del individuo para luego almacenarlo en descendientes
		individuo.revisar_genotipo();
		individuo.numero_tesoros = individuo.comprobar_tesoros();
		individuo.numero_monstruos = individuo.comprobar_monstruos();
		individuo.generateDungeon(0,0);
		individuo.calcularfitness(individuo.numero_puertas);
		
		
		//se modifica el segundo hijo con el genotipo correspondiente y los datos del individuo correspondientes
		Descendientes.set(1, individuo);
		
		
		
		//Mostramos los descendientes
		/*if(trace)
		{*/
		
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
		
		/*}*/
			
			
		return Descendientes; //Devolvemos a los hijos descendientes
	}
	

	/**
	 * Funcion que se encarga de pintar los mapas de los individuos 
	 * @param Poblacion
	 */
	public void pintar_poblacion(ArrayList<Dungeon> Poblacion_)
	{
		
		for(int j= 0; j<Poblacion_.size(); j++)
		{
			pintar_individuo(Poblacion_.get(j), Poblacion_);
		}
	}
	
	
	/**
	 * Funcion que pinta a un individuo
	 * @param Individuo
	 * @param Poblacion_
	 */
	public void pintar_individuo(Dungeon Individuo, ArrayList<Dungeon> Poblacion_)
	{
		//Recorro la poblacion
		for(int j= 0; j<Poblacion_.size(); j++)
		{
			//Compruebo que individuo es de la poblacion para pintar en que posicion se encuentra
			if(Individuo == Poblacion_.get(j))
			{
				System.out.println(" ");
				System.out.println(" ");
				System.out.println("-----------------------");
				System.out.println("Individuo " + j );
				Individuo.pintar();
				
			}
			
		}

		
	}
}
