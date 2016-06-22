
package Dungeon;

import java.util.ArrayList;
import java.util.Random;
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
		int numero_puertas = 4;
		int numero_poblacion = 1;
		
	
		//Variable para subir o bajar el porcentaje del random de las paredes que abrimos de manera random
		int porcentaje = 70; //50 para 4 * 4 //50 para 10 * 10
		int porcentaje_paredes = 20; //25  //20 para 4 * 4 //40 para 10 * 10 //AHORA CON UN 20% funciona, si es mayor para 10*10 alguna puerta acaba cayendo donde hay una pared
		
		
		
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
		
		
		
		//variable para luego comprobar el fitness de la habitacion (ya estan divididas entre 100 (son porcentajes))
		double [] nivel_facil_m_t = {
				0.06,  //numero de monstruos         6% de monstruos
				0.04,  //numero de tesoros           4% de tesoros
				0.20, //area segura 1er monstruo    20% de las celdas libres para el 1er monstruo
				0.15, //area segura 1er tesoro      15% de las celdas libres para el 1er tesoro
				0.10, //seguridad 1er tesoro         5% de las celdas libres de distancia entre el monstruo y el 1er tesoro
		};
		
		//variable que va a guardar las ponderaciones que vamos a establecer (a que variables vamos a querer dar mas importancia
		//para el fitness (todo tiene que sumar 1)
		double [] ponderaciones_facil_m_t = {
				0.26,  //numero de monstruos
				0.26,  //numero de tesoros
				0.16, //area segura 1er monstruo (ponderacion para todos los monstruos, a dividir)
				0.16, //area segura 1er tesoro   (ponderacion para todos los tesoros, a dividir)
				0.16, //seguridad 1er tesoro     (ponderacion para la seguridad de todos los tesoros, a dividir)
				
		};
		
		
		double [] dificultad_nivel = nivel_facil_m_t;
		/*double [] dificultad_nivel_facil_s_p_monstruos = nivel_facil_s_p_monstruos;
		double [] dificultad_nivel_facil_s_p_tesoros = nivel_facil_s_p_tesoros;
		double [] dificultad_nivel_facil_s_tesoros = nivel_facil_s_tesoros;
		*/
		double [] ponderaciones_nivel = ponderaciones_facil_m_t;
		/*double [] ponderaciones_nivel_facil_s_p_monstruos = ponderaciones_facil_s_p_monstruos;
		double [] ponderaciones_nivel_facil_s_p_tesoros = ponderaciones_facil_s_p_tesoros;
		double [] ponderaciones_nivel_facil_s_tesoros = ponderaciones_facil_s_tesoros;
		*/
		
		/*****************************************************************************************************************************************/
		/*************************************************** INIZIALIZACION **********************************************************************/
		/*****************************************************************************************************************************************/
		
		//Inicializo la poblacion*********************************************************************
		Poblacion = evopopulation.populationInitialization(f, c, numero_poblacion, numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas, dificultad_nivel, ponderaciones_nivel);
		

		
	
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
