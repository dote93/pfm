
package Dungeon;

import java.util.ArrayList;
import java.util.Scanner;



/**
 * Clase main que se encarga de llamar al resto de clases
 * @version 1.0
 * @author  Alvaro Valle del Pozo
 * 			TFM
 * 			Profesor: Jose Maria Font y Daniel Manrique
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
		int numero_puertas = 3;
		
		//Variable para subir o bajar el porcentaje del random de las paredes que abrimos de manera random
		int porcentaje = 20;
		
		
		//ArrayList que almacena los individuos de la poblacion
		ArrayList<Dungeon> Poblacion = new ArrayList<Dungeon>();

		
		//Creo una variable de tipo evpopulation
		EvPopulation evopopulation = new EvPopulation();
		
		
		//Inicializo la poblacion*********************************************************************
		Poblacion = evopopulation.populationInitialization(f, c, numero_poblacion, numero_monstruos, numero_tesoros, numero_puertas, porcentaje);
		
		
		
		//Pinta la poblacion**************************************************************************
		evopopulation.pintar_poblacion(Poblacion);
		
		
		/*
		//Pinta un individuo**************************************************************************
		System.out.println("");
		System.out.println("");
		System.out.println("Pintado de individuo");
		evopopulation.pintar_individuo(Poblacion.get(numero_poblacion - 1));
		*/
		
		
		
	} // Cierra el main
} // Cierra la clase main
