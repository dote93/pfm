
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
		int numero_poblacion = 10;
		
	
		//Variable para subir o bajar el porcentaje del random de las paredes que abrimos de manera random
		int porcentaje = 70; //50 para 4 * 4 //50 para 10 * 10
		int porcentaje_paredes = 20; //25  //20 para 4 * 4 //40 para 10 * 10 //AHORA CON UN 20% funciona, si es mayor para 10*10 alguna puerta acaba cayendo donde hay una pared
		
		
		
		int numero_monstruos = 5;
		int numero_tesoros = 5;
		
		//ArrayList que almacena los individuos de la poblacion
		ArrayList<Dungeon> Poblacion = new ArrayList<Dungeon>();
		//ArrayList que almacena los individuos de la poblacion
		ArrayList<Dungeon> Poblacion_No_Validos = new ArrayList<Dungeon>();
		
		//inicializo las variables a 0
		int Validos = 0;
		int No_validos = 0;

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
		
		ArrayList<Dungeon> Poblacion_provisional = new ArrayList<Dungeon>();
		
		
		//Inicializo la poblacion*********************************************************************
		Poblacion_provisional = evopopulation.populationInitialization(f, c, numero_poblacion, numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas, dificultad_nivel, ponderaciones_nivel);
		
		for (int i= 0; i < Poblacion_provisional.size(); i++)
		{
			//Si el individuo es no_valido lo anado a la lista de no validos
			if(!Poblacion_provisional.get(i).dungeon_valido || Poblacion_provisional.get(i).fitness == -100)
			{
				No_validos++;
				//Anado el no valido a la lista de la poblacion de no validos
				Poblacion_No_Validos.add(Poblacion_provisional.get(i));

			}
			
			//Si el individuos es valiudo lo anado a la lista de validos
			if(Poblacion_provisional.get(i).dungeon_valido && Poblacion_provisional.get(i).fitness >= 0)
			{
				Validos++;
				//Se anade a la poblacion de validos el mapa nuevo generado
				Poblacion.add(Poblacion_provisional.get(i));
			}
		}
		
		
		
		
		//LOG	
		System.out.print("\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("               POBLACION  INICIAL                   \n");
		System.out.print("----------------------------------------------------\n");
		for (int i= 0; i < Poblacion.size(); i++)
		{
			for(int tam_genotipo = 0; tam_genotipo < Poblacion.get(0).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Poblacion.get(i).genotipo[tam_genotipo]);
			}
			
			System.out.println("");
			System.out.println("Fitness: " + Poblacion.get(i).fitness);
			System.out.println("");
			
		}
		System.out.print("----------------------------------------------------\n");
		System.out.print("\n");
		
		
		boolean parada = false;
		
		evopopulation.contador_iteraciones = 0;
		evopopulation.stop = false;
		int contador = 0;
		
		ArrayList<Dungeon> Seleccionados = null;
		ArrayList<Dungeon> Descendientes = null;
		
		Poblacion_provisional = null;
		Poblacion_provisional = new ArrayList<Dungeon>();
		
		do
		{
			//LOG	
			System.out.print("POBLACION ACTUAL: " + "\n");

			System.out.print("\n");
			System.out.print("----------------------------------------------------\n");
			System.out.print("                     POBLACION                      \n");
			System.out.print("----------------------------------------------------\n");
			for (int i= 0; i < Poblacion.size(); i++)
			{
				for(int tam_genotipo = 0; tam_genotipo < Poblacion.get(0).genotipo.length; tam_genotipo++)
				{
					
					System.out.print(Poblacion.get(i).genotipo[tam_genotipo]);
				}
				
				System.out.println("");
				System.out.println("Fitness: " + Poblacion.get(i).fitness);
				System.out.println("");
				
			}
			System.out.print("----------------------------------------------------\n");
			System.out.print("\n");
			
		
			//Antes de seleccionar a cualquier individuo, primero se transladan los individuos con fitness -100 a la poblacion de no validos
			
			for (int i= 0; i < Poblacion.size(); i++)
			{
				if(Poblacion.get(i).fitness == -100)
				{
					Poblacion_No_Validos.add(Poblacion.get(i));
					Poblacion.remove(i);
				}
				
				
			}
			/*
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("/////////////////////////////Seleccion//////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			*/
			if (Poblacion.size() > 2)
			{
				//se seleccionan 2 individuos para luego reemplazar el peor del reemplazo con el mejor de los seleccionados
				Seleccionados = evopopulation.selection(Poblacion, 2);
			}
		
	
			/*
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("/////////////////////////////Descendientes//////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			*/
			
			if (Poblacion.size() > 2)
			{
				//se cruzan los dos seleccionados para crear la nueva generacion
				Descendientes = evopopulation.crossover(Seleccionados);
			}
			
			/*
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("/////////////////////////////Reemplazo//////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			*/
			if (Poblacion.size() > 2)
			{
				//Se reemplaza el peor individuo seleccionado con el mejor de la seleccion
				Poblacion = evopopulation.replacement(Poblacion, Descendientes);
				evopopulation.Poblacion = Poblacion;
			}	
			
			
			for (int i= 0; i < Poblacion.size(); i++)
			{
				if(Poblacion.get(i).fitness == -100)
				{
					Poblacion_No_Validos.add(Poblacion.get(i));
					Poblacion.remove(i);
				}
				
				
			}
			
			/*
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("/////////////////////////////Mutacion///////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			*/
			if (Poblacion.size() > 2)
			{
				//se muta aleatoriamente un individuo on un 5%
				Poblacion = evopopulation.mutation(Poblacion);
				evopopulation.Poblacion = Poblacion;
			}
			
			for (int i= 0; i < Poblacion.size(); i++)
			{
				if(Poblacion.get(i).fitness == -100)
				{
					Poblacion_No_Validos.add(Poblacion.get(i));
					Poblacion.remove(i);
				}
				
				
			}
			
			/*
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("/////////////////////////////Convergencia///////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////////////////");
			*/
			
			//se comprueba si la poblacion esta evolucionando o no
			parada = evopopulation.converge(Poblacion);	
			
			contador++;
			
		}
		while(!parada);
		
		System.out.println("Salgo en la iteracion: " + contador);
		
		
		System.out.print("\n");
		System.out.print("----------------------------------------------------\n");
		System.out.print("                  POBLACION FINAL                   \n");
		System.out.print("----------------------------------------------------\n");
		for (int i= 0; i < Poblacion.size(); i++)
		{
			for(int tam_genotipo = 0; tam_genotipo < Poblacion.get(0).genotipo.length; tam_genotipo++)
			{
				
				System.out.print(Poblacion.get(i).genotipo[tam_genotipo]);
			}
			
			System.out.println("");
			System.out.println("Fitness: " + Poblacion.get(i).fitness);
			System.out.println("");
			
		}
		System.out.print("----------------------------------------------------\n");
		System.out.print("\n");
			
		
		
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
		evopopulation.pintar_poblacion(Poblacion_No_Validos);
		
		
		//se mira a ver cuantos individuos hay en cada poblacion
		Validos = Poblacion.size();
		No_validos = Poblacion_No_Validos.size();
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("-------------------------------------------------------");
		System.out.println("-------------------------------------------------------");
		System.out.println("");
		System.out.println("Individuos validos   : " + Validos);
		System.out.println("Individuos NO validos: " + No_validos);

		
		System.out.print("----------------------------------------------------\n");
		
		System.out.println("Mejor individuo de la poblacion");
		
		evopopulation.individuo_parada.pintar();
		
		System.out.print("----------------------------------------------------\n");
		

		/*
		//Pinta un individuo**************************************************************************
		System.out.println("");
		System.out.println("");
		System.out.println("-------------------------------------------------------");
		evopopulation.pintar_individuo(Poblacion.get(9));
		*/

		
		
		
	} // Cierra el main
} // Cierra la clase main
