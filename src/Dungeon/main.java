
package Dungeon;

import java.util.ArrayList;
import java.util.Scanner;



/**
 * Clase main que se encarga de llamar al resto de clases
 * @version 1.0
 * @author  Alvaro Valle del Pozo
 * 			TFM
 * 			Profesor: Daniel Manrique y Jose Maria Font
 * 			Fecha: 01-02-2016
 */
public class main
{
	

	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) 
	{
		Scanner scan = new Scanner(System.in);
		
		//****************************************************************************************
		
		//Se inicializan los parametros que van a tener los individuos de la poblacion
		int f = 10;
		int c = 10;
		int numero_poblacion = 10;
		
		int numero_monstruos = 5;
		int numero_tesoros = 5;
		
		//Variable para subir o bajar el porcentaje del random de las paredes que abrimos de manera random
		int porcentaje = 25; //25
		
		
		//ArrayList que almacena los individuos de la poblacion
		ArrayList<Dungeon> Poblacion = new ArrayList<Dungeon>();

		ArrayList<int[]> pos_puertas = new ArrayList<int[]>();
		
		//Creo una variable de tipo evpopulation
		EvPopulation evopopulation = new EvPopulation();
		
		
		//Creo las posiciones de las puertas
		int [] puerta_1 = {((f / 2) - 1), 0}; // puerta centrada en el lado oeste
		int [] puerta_2 = {0, ((c/2) - 1)}; //puerta centrada en el lado norte
		int [] puerta_3 = {(f-1), ((c/2) - 1)}; //puerta centrada en el lado sur
		int [] puerta_4 = {((f / 2) - 1), (c-1)}; ////puerta centrada en el lado este
		
		
		//Anado las puertas a la lista con sus respectivas posiciones
		pos_puertas.add(puerta_1);
		pos_puertas.add(puerta_2);
		pos_puertas.add(puerta_3);
		pos_puertas.add(puerta_4);
		
		//Calculo el numero de puertas que va a haber
		int numero_puertas = pos_puertas.size();
		
		/*****************************************************************************************************************************************/
		/*************************************************** INIZIALIZACION **********************************************************************/
		
		//Inicializo la poblacion*********************************************************************
		Poblacion = evopopulation.populationInitialization(f, c, numero_poblacion, numero_monstruos, numero_tesoros,pos_puertas, numero_puertas, porcentaje);
		
		
		//Se calculan las distancias de las puertas con los tesoros de cada individuo de la poblacion**********************
		Poblacion = evopopulation.calcular_distancias_PT(Poblacion, numero_puertas, numero_tesoros);
		
		
		//Pinta la poblacion**************************************************************************
		evopopulation.pintar_poblacion(Poblacion);
		

		
		/*
		//LOG
		System.out.println("");
		System.out.println("Puertas_distancias global");
		System.out.println(evopopulation.puertas_distancias_poblacion);
		System.out.println("");
		System.out.println("Tamano Puertas_distancias global= " + evopopulation.puertas_distancias_poblacion.size());
		*/
		
		
		
		//Pinta un individuo**************************************************************************
		System.out.println("");
		System.out.println("");
		System.out.println("-------------------------------------------------------");
		evopopulation.pintar_individuo(Poblacion.get(9));
		

		
		
		
	} // Cierra el main
} // Cierra la clase main
