package Dungeon;

import java.util.ArrayList;

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
	public ArrayList<Dungeon> populationInitialization(int f, int c, int numero_poblacion, int numero_monstruos, int numero_tesoros, ArrayList<int[]> pos_puertas, ArrayList<Tipo_puertas> t_puertas, int numero_puertas, int porcentaje, int porcentaje_paredes, int tipo_celdas)
	{
		
		//Se crea un mapa por cada individuo y se anade a la poblacion
		for(int i = 0; i<numero_poblacion; i++)
		{
			
			mapa = new Dungeon(f, c,  numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas ); //El dungeon se pasa con las dimensiones (x, y)
	
			//Se anade a la poblacion el mapa nuevo generado
			Poblacion.add(mapa);
		}
		
		//Se devuelve la poblacion creada
		return Poblacion;
	}
	
	


	/**
	 * Funcion que se encarga de pintar los mapas de los individuos 
	 * @param Poblacion
	 */
	public void pintar_poblacion(ArrayList<Dungeon> Poblacion_)
	{
		
		for(int j= 0; j<Poblacion_.size(); j++)
		{
			pintar_individuo(Poblacion_.get(j));
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
				
				/*
				System.out.println(" ");
				System.out.println("Distancias (tamano = numero_T ("+ Individuo.posicion_tesoros.size() +") * numero_P ("+ Individuo.posicion_puertas.size() +")) = "+ Individuo.distancias_PT.size() +": ");
				System.out.println(Individuo.distancias_PT);
				*/
				
				System.out.println(" ");
				System.out.println("Tipo puertas: ");
				
				for (int f= 0; f < Individuo.f; f++)
				{
					for(int c= 0; c < Individuo.c; c++)
					{
						if (Individuo.dungeon[f][c].puerta && Individuo.dungeon[f][c].puerta_N) //Si es puerta norte
						{
							System.out.print("Puerta Norte "+ f +" "+ c + ": " + Individuo.dungeon[f][c].tipo_puerta_N + " ");
							System.out.println(" ");
						}
						
						if (Individuo.dungeon[f][c].puerta  && Individuo.dungeon[f][c].puerta_S)//Si es puerta sur
						{
							System.out.print("Puerta Sur   "+ f +" "+ c + ": " + Individuo.dungeon[f][c].tipo_puerta_S + " ");
							System.out.println(" ");
						}
						
						if (Individuo.dungeon[f][c].puerta  && Individuo.dungeon[f][c].puerta_E)//Si es puerta este
						{
							System.out.print("Puerta Este  "+ f +" "+ c + ": " + Individuo.dungeon[f][c].tipo_puerta_E + " ");
							System.out.println(" ");
						}
						
						if (Individuo.dungeon[f][c].puerta  && Individuo.dungeon[f][c].puerta_O)//Si es puerta oeste
						{
							System.out.print("Puerta Oeste "+ f +" "+ c + ": " + Individuo.dungeon[f][c].tipo_puerta_O + " ");
							System.out.println(" ");
						}
					}
				}
				System.out.println(" ");
				
				
				System.out.println(" ");
				System.out.println("Numero de monstruos: " + Individuo.numero_monstruos);
				System.out.println("Numero de tesoros: " + Individuo.numero_tesoros);
				System.out.println(" ");
				
				System.out.println(" ");
				System.out.println("Distancias minimas PT: "+ Individuo.distancia_min_PT);
				
				
				System.out.println(" ");
				System.out.println("Status individuo: "+ Individuo.dungeon_valido);
				
				/*
				System.out.println(" ");
				System.out.println("Media distancias: " + Individuo.media_distancias_PT);
				
				System.out.println(" ");
				System.out.println("Fitness: " + Individuo.fitness);
				System.out.println(" ");
				*/
			}
			
		}

		
	}
}
