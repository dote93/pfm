
package Dungeon;

import java.util.ArrayList;
import java.util.Scanner;

import Dungeon.Celda.Tipo_puertas;



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
		
	
		//Variable para subir o bajar el porcentaje del random de las paredes que abrimos de manera random
		int porcentaje = 80; //50 para 4 * 4
		int porcentaje_paredes = 10;  //20 para 4 * 4
		int numero_puertas = 4;
		
		
		int numero_monstruos = 5;
		int numero_tesoros = 5;
		
		//ArrayList que almacena los individuos de la poblacion
		ArrayList<Dungeon> Poblacion = new ArrayList<Dungeon>();

		ArrayList<int[]> pos_puertas = new ArrayList<int[]>();
		
		//Creo una variable de tipo evpopulation
		EvPopulation evopopulation = new EvPopulation();
		
		
		//Numero del esponente de 2 elevado a N para saber cuantos bits tiene el genotipo
		int tipo_celdas = 3;
		
		
		//Creo las posiciones de las puertas
		int [] puerta_1 = {((f / 2) - 1), 0}; // puerta centrada en el lado Oeste
		int [] puerta_2 = {0, ((c/2) - 1)}; //puerta centrada en el lado Norte
		int [] puerta_3 = {(f-1), ((c/2) - 1)}; //puerta centrada en el lado Sur
		int [] puerta_4 = {((f / 2) - 1), (c-1)}; ////puerta centrada en el lado Este
		
		
		//Anado las puertas a la lista con sus respectivas posiciones
		pos_puertas.add(puerta_1);
		pos_puertas.add(puerta_2);
		pos_puertas.add(puerta_3);
		pos_puertas.add(puerta_4);
		
		//Calculo el numero de puertas que va a haber
//		//int numero_puertas = pos_puertas.size();
		

		//Arraylist que guarda el tipo de puerta que es cada una de las puertas
		ArrayList<Tipo_puertas> t_puertas = new ArrayList<Tipo_puertas>();
		
		//Anado los tipos de puertas al arraylist
		/*t_puertas.add(Celda.Tipo_puertas.ENTRADA_SALIDA);//puerta Norte
		t_puertas.add(Celda.Tipo_puertas.ENTRADA); // Puerta Oeste
		t_puertas.add(Celda.Tipo_puertas.ENTRADA);//puerta Este
		t_puertas.add(Celda.Tipo_puertas.SALIDA);//puerta Sur*/
		
		/*****************************************************************************************************************************************/
		/*************************************************** INIZIALIZACION **********************************************************************/
		/*****************************************************************************************************************************************/
		
		//Inicializo la poblacion*********************************************************************
		Poblacion = evopopulation.populationInitialization(f, c, numero_poblacion, numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas);
		
		
		System.out.println("");
		System.out.println("-------------------------------------------------------");
		System.out.println("                POBLACION VALIDOS                      ");
		System.out.println("-------------------------------------------------------");
		//Pinta la poblacion de validos **************************************************************************
		evopopulation.pintar_poblacion(Poblacion);
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("-------------------------------------------------------");
		System.out.println("              POBLACION NO VALIDOS                     ");
		System.out.println("-------------------------------------------------------");
		
		//Pinta la poblacion de no validos ************************************************************************
		evopopulation.pintar_poblacion(evopopulation.Poblacion_noValidos);
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("-------------------------------------------------------");
		System.out.println("-------------------------------------------------------");
		System.out.println("");
		System.out.println("Individuos validos   : " + evopopulation.Validos);
		System.out.println("Individuos NO validos: " + evopopulation.No_validos);

		
		

		/*
		//Pinta un individuo**************************************************************************
		System.out.println("");
		System.out.println("");
		System.out.println("-------------------------------------------------------");
		evopopulation.pintar_individuo(Poblacion.get(9));
		*/

		
		
		
	} // Cierra el main
} // Cierra la clase main
