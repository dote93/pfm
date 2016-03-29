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
	public ArrayList<Dungeon> populationInitialization(int f, int c, int numero_poblacion, int numero_monstruos, int numero_tesoros, ArrayList<int[]> pos_puertas, ArrayList<Tipo_puertas> t_puertas, int numero_puertas, int porcentaje, int porcentaje_paredes, int tipo_celdas)
	{
		
		//Se crea un mapa por cada individuo y se anade a la poblacion
		for(int i = 0; i<numero_poblacion; i++)
		{
			
			mapa = new Dungeon(f, c,  numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas ); //El dungeon se pasa con las dimensiones (x, y)
			
			
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
				
			}
			
		}

		
	}
}
