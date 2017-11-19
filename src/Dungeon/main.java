
package Dungeon;

import java.util.ArrayList;
import java.util.Scanner;

//Imports para escribir y modificar un archivo de texto
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


import java.io.PrintStream;
//import para la fecha
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;




//Import de los tipos de puertas que hay
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
	
	// AYUDAAAAAAAAAAAAAAAAAAAAAAAA
	// Control + 7 para comentar varias lineas seleccionadas
	
	public main ()
	{
		
	}

	@SuppressWarnings({ "resource", "unused", "unchecked" })
	public static void main(String[] args) throws IOException, CloneNotSupportedException
	{
		Scanner scan = new Scanner(System.in);
		
		//****************************************************************************************
		
		//Se inicializan los parametros que van a tener los individuos de la poblacion
		int f = 10;
		int c = 10;
		int numero_puertas = 2;
		int numero_poblacion = 10;
		
	
		//Variable para subir o bajar el porcentaje del random de las paredes que abrimos de manera random
		int porcentaje = (int)((f*c) * 70) / 100; //50 para 4 * 4 //50 para 10 * 10
		int porcentaje_paredes = (int)((f*c) * 30) / 100; //25  //20 para 4 * 4 //40 para 10 * 10 //AHORA CON UN 20% funciona, si es mayor para 10*10 alguna puerta acaba cayendo donde hay una pared
		
		
		
		int numero_monstruos = (int)((porcentaje * 9)/100 );
		int numero_tesoros = (int)((porcentaje * 6)/100 );
		
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
				(int)Math.round((numero_monstruos * 20)/100 ),       //numero de monstruos que se esperan entre la puerta de entrada y la mas cercana
				(int)Math.round((numero_tesoros * 40)/100 ),         //numero de tesoros que se esperan entre la puerta de entrada y la mas cercana
				numero_monstruos,  			                         //numero de monstruos         9% de monstruos de las celdas libres
				numero_tesoros,  			                         //numero de tesoros           6% de tesoros de las celdas libres
				(int)Math.round((porcentaje * 10)/100 ),             //area segura 1er monstruo    29% de las celdas libres para el 1er monstruo (20% de de las totales si son 100 celdas)
				(int)Math.round((porcentaje * 12)/100 ),		     //area segura 1er tesoro      22% de las celdas libres para el 1er tesoro (15% de las totales si son 100 celdas)
				(int)Math.round((porcentaje * 4)/100 ),  		     //seguridad 1er tesoro        15% de las celdas libres de distancia entre el monstruo y el 1er tesoro (10% de las totales si son 100 celdas)
				porcentaje_paredes,                                   //celdas pared
		};
		
		//variable que va a guardar las ponderaciones que vamos a establecer (a que variables vamos a querer dar mas importancia
		//para el fitness (todo tiene que sumar 1)
		double [] ponderaciones_facil_m_t = {
				0.0995, //numero de monstruos en el recorrido PP
				0.02, //numero de tesoros en el recorrido PP
				0.35, //numero de monstruos
				0.35, //numero de tesoros
				0.13, //area segura 1er monstruo (ponderacion para todos los monstruos, a dividir)
				0.00, //area segura 1er tesoro   (ponderacion para todos los tesoros, a dividir)
				0.04, //seguridad 1er tesoro     (ponderacion para la seguridad de todos los tesoros, a dividir)
				0.0105,  //celdas de tipo pared
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
		
		
		boolean parada = false;
		
		evopopulation.contador_iteraciones = 0;
		evopopulation.stop = false;
		int contador = 0;
		
		ArrayList<Dungeon> Seleccionados = null;
		ArrayList<Dungeon> Descendientes = null;
		
		
		evopopulation.mutaciones = 0;
		
		int individuos_eliminados = 0;
		
		
		//---------------- FECHA y VARIABLES PARA GUARDAR EN EL ARCHIVO -------------------
		
		
		Date fechaActual = new Date();
		DateFormat formatoHora = new SimpleDateFormat("HH_mm_ss");
        DateFormat formatoFecha = new SimpleDateFormat("yyyy_MM_dd");
        
        String nombre_archivo = formatoFecha.format(fechaActual); //+ "_" + formatoHora.format(fechaActual);
        
        String fecha_completa = formatoFecha.format(fechaActual) + "_" + formatoHora.format(fechaActual);
        
        /*//LOG
        System.out.println("Fecha: " + nombre_archivo);
        */
        
        //Variable con la que vamos a escribir en el archivo
		BufferedWriter bw = null;
		
		
		//RUTA Relativa
		String ruta = "../pfm/Pruebas/";
		File file_;
		file_ = new File(ruta + nombre_archivo + ".txt");
		
		
		//---------------------------------------------------------------------------------
		
		
		//Desvia la salida de la consola al archivo fijado
		//TODO Comentar estas lineas mientras se esta debugueando y no esta en modo para sacar resultados
//		PrintStream out = new PrintStream(new FileOutputStream((String)("../pfm/Pruebas/"  + fecha_completa + "_output.txt")));
//		System.setOut(out);
		
		/*****************************************************************************************************************************************/
		/*************************************************** INIZIALIZACION **********************************************************************/
		/*****************************************************************************************************************************************/
		
		
		ArrayList<Dungeon> Poblacion_provisional = new ArrayList<Dungeon>();
		
		while (Poblacion.size() < 10) 
		{
			// Inicializo la
			// poblacion*********************************************************************
			Poblacion_provisional = evopopulation.populationInitialization(f, c, numero_poblacion, numero_monstruos,
					numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas,
					dificultad_nivel, ponderaciones_nivel);

			for (int i = 0; i < Poblacion_provisional.size(); i++) {
				// Si el individuo es no_valido lo anado a la lista de no
				// validos
				if (!Poblacion_provisional.get(i).dungeon_valido || Poblacion_provisional.get(i).fitness < 0) {
					No_validos++;
					// Anado el no valido a la lista de la poblacion de no
					// validos
					try {
						Poblacion_No_Validos.add((Dungeon) Poblacion_provisional.get(i).clone());
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				// Si el individuos es valiudo lo anado a la lista de validos
				else if (Poblacion_provisional.get(i).dungeon_valido && Poblacion_provisional.get(i).fitness >= 0) {
					Validos++;
					// Se anade a la poblacion de validos el mapa nuevo generado
					try {
						Poblacion.add((Dungeon) Poblacion_provisional.get(i).clone());
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		
		
		//Reseteamos la poblacion provisional y la volvemso a inicializar
		Poblacion_provisional = null;
		Poblacion_provisional = new ArrayList<Dungeon>();
		
		
		
		/**LOG*/
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
		
		
		
		
		
		//***************************************************************************************************************************************************
		//***************************************************************************************************************************************************
		
		//variable para saber si queremos generar una evolucion o no
		
		boolean evolucion = true;
		
		
		//***************************************************************************************************************************************************
		//***************************************************************************************************************************************************
		
		if(evolucion)
		{
			//si estamos en la primera iteracion, se anaden dos posiciones a la lista de individuos de parada siendo temporalmente el primer individuo de la poblacion
			if(evopopulation.contador_iteraciones == 0)
			{
				try {
					evopopulation.Individuos_parada.add(0, (Dungeon) Poblacion.get(0).clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					evopopulation.Individuos_parada.add(1, (Dungeon) Poblacion.get(0).clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			evopopulation.Individuos_parada = new ArrayList<Dungeon>(2);
			evopopulation.Individuos_parada.add(new Dungeon());
			evopopulation.Individuos_parada.add(new Dungeon());
			
			
			evopopulation.individuo_parada.set_fitness(3000);
			
			//se modifica el individuo temporal con un fitness malo para que luego se reemplace
			
			try {
				evopopulation.Individuos_parada.set(0, null);
				evopopulation.Individuos_parada.set(0, new Dungeon());
				evopopulation.Individuos_parada.set(0,(Dungeon) evopopulation.individuo_parada.clone());
//				evopopulation.Individuos_parada.get(0).pintar();
			} catch (CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			
			
			//se anade el individuo de parada modificado con un mal fitness
			try {
				evopopulation.Individuos_parada.set(1, null);
				evopopulation.Individuos_parada.set(1, new Dungeon());
				evopopulation.Individuos_parada.set(1, (Dungeon) evopopulation.individuo_parada.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//se comprueba si se encuentra el mejor individuo entre la poblacion que hemos inicializado, si no es asi, se empieza a evolucionar
//			if (evopopulation.converge((ArrayList<Dungeon>) Poblacion.clone()))
//			{
//				parada = true;
//			}
			
			//Se pone a 1 debido a que ya hemos hecho una iteracion porque hemos generado una poblacion bajo unos principios/reglas
			evopopulation.contador_iteraciones = 1;
			
			//se comprueba si se encuentra el mejor individuo entre la poblacion que hemos inicializado, si no es asi, se empieza a evolucionar
			if (evopopulation.converge((ArrayList<Dungeon>) Poblacion.clone()))
			{
				parada = true;
			}
			
			//TODO Escribir la poblacion inicial porque entre ellos puede estar el mejor individuo antes de empezar si quiera a evolucionar
			LogWriter logWriter = new LogWriter();
			
			//Se escribe a los primeros individuos de la poblacion
			logWriter = new LogWriter(ruta, file_, fecha_completa, contador, (ArrayList<Dungeon>) Poblacion.clone(), evopopulation);
			
			contador = contador + 1;
			
			while(!parada)
			{
//				/**LOG**/
//				System.out.print("\n");
//				System.out.print("----------------------------------------------------\n");
//				System.out.print("                  POBLACION ACTUAL                  \n");
//				System.out.print("----------------------------------------------------\n");
//				for (int i= 0; i < Poblacion.size(); i++)
//				{
//					for(int tam_genotipo = 0; tam_genotipo < Poblacion.get(0).genotipo.length; tam_genotipo++)
//					{
//						
//						System.out.print(Poblacion.get(i).genotipo[tam_genotipo]);
//					}
//					
//					System.out.println("");
//					System.out.println("Fitness: " + Poblacion.get(i).fitness);
//					System.out.println("");
//					
//				}
//				System.out.print("----------------------------------------------------\n");
//				System.out.print("\n");
				
			
				/**Antes de seleccionar a cualquier individuo, primero se transladan los individuos con fitness -100 a la poblacion de no validos**/
				for (int i= 0; i < Poblacion.size(); i++)
				{
					if(Poblacion.get(i).fitness < 0)
					{
						try {
							Poblacion_No_Validos.add((Dungeon) Poblacion.get(i).clone());
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Poblacion.remove(i);
						individuos_eliminados++;
						
					}
				
				}
				
				/**LOG
				System.out.println("INDIVIDUOS ELIMINADOS 01: " + individuos_eliminados);**/
				
				individuos_eliminados = 0;
				
				
				
				
				
				/**System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("/////////////////////////////Seleccion//////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");*/
				
				if (Poblacion.size() >= 2)
				{
					//se seleccionan 2 individuos para luego reemplazar el peor del reemplazo con el mejor de los seleccionados
					Seleccionados = evopopulation.selection((ArrayList<Dungeon>) Poblacion.clone(), 2);
				}
			
				
				/**System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("/////////////////////////////Descendientes//////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");*/
				
//				System.out.println("POBLACION ANTES DE DESCENCIENTES");
//				
//				for (int i = 0; i < Poblacion.size(); i++) {
//					Poblacion.get(i).pintar();
//				}
			
//				System.out.println("");
				
				if (Poblacion.size() >= 2)
				{
					//se cruzan los dos seleccionados para crear la nueva generacion
					Descendientes = evopopulation.crossover((ArrayList<Dungeon>) Seleccionados.clone());
				}
				
				
//				System.out.println("POBLACION DESPUES DE DESCENDIENTES");
//				
//				for (int i = 0; i < Poblacion.size(); i++) {
//					Poblacion.get(i).pintar();
//				}
			
//				System.out.println("");
				
				if(Descendientes.size() >= 1)
				{
					//Se comprueba que los descendienetes no sean nulos (-100), si es así, se eliminan de descendientes
					for(int num_descendiente = 0; num_descendiente < Descendientes.size(); num_descendiente++)
					{
						if(Descendientes.get(num_descendiente).fitness < 0)
						{
							
							Poblacion_No_Validos.add((Dungeon) Descendientes.get(num_descendiente).clone());
							
							System.out.println("");
							System.out.println("Fitness descendiente MALO: " + Descendientes.get(num_descendiente).fitness);
							System.out.println("");
							Descendientes.remove(num_descendiente);
						}
					}
				}
				
//				for (int i = 0; i < Descendientes.size(); i++) {
//					Descendientes.get(i).pintar();
//				}
//			
//				System.out.println("");
				
				/**System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("/////////////////////////////Reemplazo//////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");*/
				
//				System.out.println("POBLACION ANTES DEL REEMPLAZO");
//				
//				for (int i = 0; i < Poblacion.size(); i++) {
//					Poblacion.get(i).pintar();
//				}
			
//				System.out.println("");
				
				if (Poblacion.size() >= 2)
				{
					//Se reemplaza el peor individuo seleccionado con el mejor de la seleccion
					ArrayList<Dungeon> PoblacionEvolved = evopopulation.replacement((ArrayList<Dungeon>) Poblacion.clone(), (ArrayList<Dungeon>) Descendientes.clone());
					
					Poblacion = null; 
					
					Poblacion = (ArrayList<Dungeon>) PoblacionEvolved.clone();
					
					//evopopulation.Poblacion = Poblacion;
				}	

//				System.out.println("POBLACION DESPUES DEL REEMPLAZO");
//				
//				for (int i = 0; i < Poblacion.size(); i++) {
//					Poblacion.get(i).pintar();
//				}
			
//				System.out.println("");
				
//				for (int i= 0; i < Poblacion.size(); i++)
//				{
//					if(Poblacion.get(i).fitness < 0)
//					{
//						try {
//							Poblacion_No_Validos.add((Dungeon) Poblacion.get(i).clone());
//						} catch (CloneNotSupportedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						Poblacion.remove(i);
//						individuos_eliminados++;
//					}
//					
//					
//				}
//				
//				/**LOG
//				System.out.println("INDIVIDUOS ELIMINADOS 02: " + individuos_eliminados);**/
//				
				individuos_eliminados = 0;
			
				
				/**System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("/////////////////////////////Mutacion///////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");*/
				
				
				if (Poblacion.size() >= 2)
				{
	
					//se muta aleatoriamente un individuo on un 5%
					Poblacion = evopopulation.mutation((ArrayList<Dungeon>) Poblacion.clone());
					//evopopulation.Poblacion = Poblacion;
				}
				
				for (int i= 0; i < Poblacion.size(); i++)
				{
					if(Poblacion.get(i).fitness < 0)
					{
						try {
							Poblacion_No_Validos.add((Dungeon) Poblacion.get(i).clone());
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Poblacion.remove(i);
						individuos_eliminados++;
					}
					
					
				}
//				
//				//System.out.println("INDIVIDUOS ELIMINADOS 03: " + individuos_eliminados);
//				
				individuos_eliminados = 0;
		
				/**System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("//////////////////////Introduccion de nuevos individuos/////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");*/
				
				//Solo si la poblacion es demasiado pequena
				if (Poblacion.size() < 10)
				{
					Poblacion_provisional = new ArrayList<Dungeon>();
					Poblacion_provisional = evopopulation.populationInitialization(f, c, 2, numero_monstruos, numero_tesoros, pos_puertas, t_puertas, numero_puertas, porcentaje, porcentaje_paredes, tipo_celdas, dificultad_nivel, ponderaciones_nivel);
					
					for (int i= 0; i < Poblacion_provisional.size(); i++)
					{
						//Si el individuo es no_valido lo anado a la lista de no validos
						if(!Poblacion_provisional.get(i).dungeon_valido || Poblacion_provisional.get(i).fitness == -100)
						{
							No_validos++;
							//Anado el no valido a la lista de la poblacion de no validos
							try {
								Poblacion_No_Validos.add((Dungeon) Poblacion_provisional.get(i).clone());
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	
						}
						
						//Si el individuos es valiudo lo anado a la lista de validos
						if(Poblacion_provisional.get(i).dungeon_valido && Poblacion_provisional.get(i).fitness >= 0)
						{
							Validos++;
							//Se anade a la poblacion de validos el mapa nuevo generado
							try {
								Poblacion.add((Dungeon) Poblacion_provisional.get(i).clone());
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					//Reseteamos la poblacion provisional y la volvemso a inicializar
					Poblacion_provisional = null;
				}
							
				/**System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("/////////////////////////////Convergencia///////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("////////////////////////////////////////////////////////////////////////////////////");*/
				
				for (int i= 0; i < Poblacion.size(); i++)
				{
					if(Poblacion.get(i).fitness < 0)
					{
						try {
							Poblacion_No_Validos.add((Dungeon) Poblacion.get(i).clone());
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Poblacion.remove(i);
						individuos_eliminados++;
					}
					
					
				}
//				
//				/**LOG
//				System.out.println("INDIVIDUOS ELIMINADOS 04: " + individuos_eliminados);*/
//				
//				individuos_eliminados = 0;
//				
//				
				//se comprueba si la poblacion esta evolucionando o no
				parada = evopopulation.converge((ArrayList<Dungeon>) Poblacion.clone());	
				//parada = true;
				
				
				/**LOG**/
				System.out.print("\n");
				System.out.print("----------------------------------------------------\n");
				System.out.print("                  POBLACION ACTUAL                  \n");
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
				
							
				//TODO ESCRIBIR AQUI EL ARCHIVO CON LOS DATOS DEL MEJOR Y EL PEOR INDIVIDUO DE LA POBLACION
				
				
//				try
//				{
//					//Si el archivo ya existe, solo escribo la linea correspondiente
//					if(file_.exists())
//					{
//						bw = new BufferedWriter(new FileWriter(ruta, true));
//						bw.newLine();
//						if(contador == 0)
//						{
//							bw.newLine();
//							bw.newLine();
//							bw.write("*******************************************************************************************");
//							bw.newLine();
//							bw.append("Iteracion NumIndividuos MejorFitness MejorGenotipo PeorFitness PeorGenotipo MediaFitness ");
//							bw.newLine();
//							bw.write(fecha_completa);
//							bw.newLine();
//							bw.newLine();
//							bw.write("00"); //se anade la iteracion en la que estamos
//						}
//						else if(contador < 10)
//						{
//							bw.write("0" + Integer.toString(contador)); //se anade la iteracion en la que estamos
//						}
//						else
//						{
//							bw.write(Integer.toString(contador)); //se anade la iteracion en la que estamos
//						}
//						
//						if(Poblacion.size() < 10)
//						{
//							bw.append(",0" + Poblacion.size());
//						}
//						else
//						{
//							bw.append("," + Poblacion.size());
//						}
//						bw.append("," + evopopulation.Individuos_parada.get(0).getFitness());
//						
////						String mejorGenotipo = new String();
////						for(int tam_genotipo = 0; tam_genotipo < evopopulation.Individuos_parada.get(0).genotipo.length; tam_genotipo++)
////						{
////							
////							mejorGenotipo = mejorGenotipo + (evopopulation.Individuos_parada.get(0).genotipo[tam_genotipo]);
////						}
////						
////						bw.append(" " + mejorGenotipo); //se anade el genotipo del mejor individuo
////						
//						Dungeon worstIndividuo = new Dungeon();
//						worstIndividuo = evopopulation.getWorstIndividuo(Poblacion);
//						
//						bw.append("," + worstIndividuo.getFitness());
//						
////						String peorGenotipo = new String();
////						for(int tam_genotipo = 0; tam_genotipo < worstIndividuo.genotipo.length; tam_genotipo++)
////						{
////							peorGenotipo = peorGenotipo + (worstIndividuo.genotipo[tam_genotipo]);
////						}
////						
////						bw.append(" " + peorGenotipo); //se anade el genotipo del peor individuo
////						
//						bw.append("," + evopopulation.getMeanPoblacion(Poblacion));
//						
//						
//					}
//					
//					//si el archivo no existia, escribo una primera linea informativa
//					else
//					{
//						bw = new BufferedWriter(new FileWriter(ruta, true));
//						
//						if(contador == 0)
//						{
//							bw.write("*******************************************************************************************");
//							bw.newLine();
//							bw.append("Iteracion NumIndividuos MejorFitness MejorGenotipo PeorFitness PeorGenotipo MediaFitness ");
//							bw.newLine();
//							bw.write(fecha_completa);
//							bw.newLine();
//							bw.newLine();
//							bw.write("00"); //se anade la iteracion en la que estamos
//						}
//						else if(contador < 10)
//						{
//							bw.write("0" + Integer.toString(contador)); //se anade la iteracion en la que estamos
//						}
//						else
//						{
//							bw.write(Integer.toString(contador)); //se anade la iteracion en la que estamos
//						}
//						
//						if(Poblacion.size() < 10)
//						{
//							bw.append(",0" + Poblacion.size());
//						}
//						else
//						{
//							bw.append("," + Poblacion.size());
//						}
//						bw.append("," + evopopulation.Individuos_parada.get(0).getFitness());
//						
////						String mejorGenotipo = new String();
////						for(int tam_genotipo = 0; tam_genotipo < evopopulation.Individuos_parada.get(0).genotipo.length; tam_genotipo++)
////						{
////							
////							mejorGenotipo = mejorGenotipo + (evopopulation.Individuos_parada.get(0).genotipo[tam_genotipo]);
////						}
////						
////						bw.append(" " + mejorGenotipo); //se anade el genotipo del mejor individuo
////						
//						Dungeon worstIndividuo = new Dungeon();
//						worstIndividuo = evopopulation.getWorstIndividuo(Poblacion);
//						
//						bw.append("," + worstIndividuo.getFitness());
//						
//						String peorGenotipo = new String();
//						
////						for(int tam_genotipo = 0; tam_genotipo < worstIndividuo.genotipo.length; tam_genotipo++)
////						{
////							peorGenotipo = peorGenotipo + (worstIndividuo.genotipo[tam_genotipo]);
////						}
////						
////						bw.append(" " + peorGenotipo); //se anade el genotipo del peor individuo
////						
//						bw.append("," + evopopulation.getMeanPoblacion(Poblacion));
//
//					}
//					
//				}
//				catch (IOException e)
//				{
//					//Error processing code
//					System.out.println("Error: " + e);
//				}
//				finally
//				{
//					//si el archivo existiao se ha creado y no ha habido ningun error, se cierra
//					if(bw != null)
//					{
//						bw.close();
//					}
//				}
				
				logWriter = new LogWriter(ruta, file_, fecha_completa, contador, (ArrayList<Dungeon>) Poblacion.clone(), evopopulation);
				
					System.out.println("Fitness peor indiv         :" + evopopulation.getWorstIndividuo(Poblacion).fitness);
					System.out.println("Fitness indiv temporal     : " + evopopulation.Individuos_parada.get(0).fitness);
					System.out.println("Fitness indiv anterior iter: " + evopopulation.Individuos_parada.get(1).fitness);
					
				System.out.println("Iteracion: " + contador);
				
				contador++;
				
			}
				
		}
				
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
//		evopopulation.pintar_poblacion(Poblacion_No_Validos);
		
		
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
		System.out.print("----------------------------------------------------\n");
		
		
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
		
		
		System.out.println("Salgo en la iteracion: " + contador);
		System.out.println("Motivo: " + evopopulation.motivo);
		System.out.println("Mutaciones hechas: " + evopopulation.mutaciones );
		
		if (evolucion) {
			System.out.println("Mejor individuo de la poblacion");

			if (evopopulation.Individuos_parada.get(0).fitness < 0) {
				System.out.println("HA HABIDO UN ERROR");
			}
			evopopulation.Individuos_parada.get(0).pintar();
		}
		
		else
		{
			if(Poblacion.size()>= 1)
			//Poblacion.get(0).pintar();
			evopopulation.Individuos_parada.get(0).pintar();
		}
		
		
		//Se pintan los datos esperados para saber que es lo que se buscaba
		
		System.out.println("Numero de monstruos recorrido esperados   : " + dificultad_nivel[0]);
		System.out.println("Numero de tesoros recorrido esperados     : " + dificultad_nivel[1]);
		System.out.println("Numero de monstruos esperados             : " + dificultad_nivel[2]);
		System.out.println("Numero de tesoros esperados               : " + dificultad_nivel[3]);
		System.out.println("Area Segura monstruo esperada             : " + dificultad_nivel[4]);
		System.out.println("Area Segura tesoro esperada               : " + dificultad_nivel[5]);
		System.out.println("Seguridad tesoro esperada                 : " + dificultad_nivel[6]);
		System.out.println("Numero de celdas Pared esperadas          : " + dificultad_nivel[7]);
		System.out.println("Numero de celdas Libres esperadas         : " + (porcentaje - numero_monstruos - numero_tesoros));
		
		
		
		System.out.print("----------------------------------------------------\n");
		System.out.print("----------------------------------------------------\n");

		
		/**
		//Pinta un individuo**************************************************************************
		System.out.println("");
		System.out.println("");
		System.out.println("-------------------------------------------------------");
		evopopulation.pintar_individuo(Poblacion.get(9));
		*/
		
		
		//SE RENOMBRA EL ARCHIVO DE LOG
		File fileRenamed = new File(ruta + fecha_completa + ".txt");
		
		file_.renameTo(fileRenamed);
		
	} // Cierra el main
} // Cierra la clase main
