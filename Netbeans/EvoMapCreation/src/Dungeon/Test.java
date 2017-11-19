package Dungeon;

import java.io.IOException;
import java.util.ArrayList;

import Dungeon.Celda.Tipo_puertas;

public class Test {
		
		public void test(){
			
		}

		public static void main(String[] args) throws IOException, CloneNotSupportedException
		{
			Test test = new Test();
			Dungeon indiv = test.test_indiv(test);
			indiv.pintar();
		}
		
		private Dungeon test_indiv(Test test) throws CloneNotSupportedException
		{
			//individuo para poder modificar el arraylist de hijos
			//Dungeon individuo1 = padre;
			Dungeon individuo = null;
			Dungeon padre = null;
			String genotipo_hijo1_str = "000 001 111 000 000 000 000 100 000 111 000 000 000 000 000 000 000 000 000 000 000 000 111 000 111 000 000 000 000 000 000 000 000 000 000 111 000 000 111 111 111 000 000 000 000 000 000 000 000 111 000 000 000 111 111 000 000 111 000 000 000 000 000 000 111 000 111 000 000 111 000 000 000 000 000 111 111 000 000 000 101 111 000 111 000 000 111 000 100 000 001 111 101 011 010 001 101 001 010 111";
			padre = new Dungeon();
			
			
			
			padre.f = 10;
			padre.c = 10;
			padre.tipo_celdas = 3;
			padre.porcentaje = (int)((padre.f*padre.c) * 70) / 100;
			padre.porcentaje_paredes = (int)((padre.f*padre.c) * 30) / 100;
			padre.celdas_Paredes = 29;
			padre.celdas_Vacias = 60;
			padre.dificultad_nivel = new double[]{
                                (int)Math.round(((int)((padre.porcentaje * 9)/100 ) * 20)/100 ),       //numero de monstruos que se esperan entre la puerta de entrada y la mas cercana
                                (int)Math.round(((int)((padre.porcentaje * 6)/100 ) * 40)/100 ),         //numero de tesoros que se esperan entre la puerta de entrada y la mas cercana
                                (int)((padre.porcentaje * 9)/100 ),  			                         //numero de monstruos         9% de monstruos de las celdas libres
                                (int)((padre.porcentaje * 6)/100 ),  			                         //numero de tesoros           6% de tesoros de las celdas libres
                                (int)Math.round((padre.porcentaje * 10)/100 ),             //area segura 1er monstruo    29% de las celdas libres para el 1er monstruo (20% de de las totales si son 100 celdas)
                                (int)Math.round((padre.porcentaje * 12)/100 ),		     //area segura 1er tesoro      22% de las celdas libres para el 1er tesoro (15% de las totales si son 100 celdas)
                                (int)Math.round((padre.porcentaje * 5)/100 ),  		     //seguridad 1er tesoro        15% de las celdas libres de distancia entre el monstruo y el 1er tesoro (10% de las totales si son 100 celdas)
                                padre.porcentaje_paredes                                   //celdas pared
                            };
			padre.ponderaciones_nivel = new double[]{
                                0.0995, //numero de monstruos en el recorrido PP
                                0.02, //numero de tesoros en el recorrido PP
                                0.29, //numero de monstruos
                                0.29, //numero de tesoros
                                0.23, //area segura 1er monstruo (ponderacion para todos los monstruos, a dividir)
                                0.02, //area segura 1er tesoro   (ponderacion para todos los tesoros, a dividir)
                                0.04, //seguridad 1er tesoro     (ponderacion para la seguridad de todos los tesoros, a dividir)
                                0.0105,  //celdas de tipo pared
			    };
			padre.numero_puertas = 2;
			
			int[] genotipo_hijo1 = test.gen_str_to_int_arr(genotipo_hijo1_str, padre.f, padre.c);
			individuo = new Dungeon();
			individuo.dungeon_valido = true;
			individuo.f = padre.f;
			individuo.c = padre.c;
			individuo.tipo_celdas = padre.tipo_celdas;
			individuo.porcentaje = padre.porcentaje;
			individuo.porcentaje_paredes = padre.porcentaje_paredes;
			individuo.celdas_Paredes = padre.celdas_Paredes;
			individuo.celdas_Vacias = padre.celdas_Vacias; 
			individuo.dificultad_nivel = padre.dificultad_nivel;
			individuo.ponderaciones_nivel = padre.ponderaciones_nivel;
			
			
			individuo.inicializarDungeon();
			
			individuo.genotipo = genotipo_hijo1;
			individuo.ResetearDungeon();
			individuo.revisar_genotipo();
			
			individuo.numero_puertas = padre.numero_puertas;
			
//			individuo.pos_puertas = padre.pos_puertas;
//			individuo.t_puertas = padre.t_puertas;
			
			individuo.posicion_puertas = new ArrayList<Celda>();
			
			
			//TODO Añadir las puertas en las mismas posiciones que el padre y 
			//que las celdas correspondientes tengan las mismas caracteristicas (puerta norte, ... puerta si, etc)
//			individuo.anadir_puertas(individuo.t_puertas, individuo.numero_puertas);
			
			int [] puerta1 = {0, 6};
			int [] puerta2 = {9, 2};
			
			individuo.dungeon[puerta1[0]][puerta1[1]].puerta = true;
			individuo.dungeon[puerta1[0]][puerta1[1]].puerta_N = true;
			individuo.dungeon[puerta1[0]][puerta1[1]].tipo_puerta_N = Tipo_puertas.SALIDA;
			individuo.posicion_puertas.add((Celda) individuo.dungeon[puerta1[0]][puerta1[1]].clone());
			individuo.dungeon[puerta2[0]][puerta2[1]].puerta = true;
			individuo.dungeon[puerta2[0]][puerta2[1]].puerta_S = true;
			individuo.dungeon[puerta2[0]][puerta2[1]].tipo_puerta_S = Tipo_puertas.ENTRADA_SALIDA;
			individuo.posicion_puertas.add((Celda) individuo.dungeon[puerta2[0]][puerta2[1]].clone());
			
			individuo.numero_tesoros = 0;
			individuo.numero_tesoros = individuo.comprobar_tesoros();

			individuo.numero_monstruos = 0;
			individuo.numero_monstruos = individuo.comprobar_monstruos();
			
			
			
			individuo.ResetearDungeonCamino();
			//TODO
			
			int posicionStartX = -1;
			int posicionStartY = -1;
			
			//TODO HAY QUE AÑADIR LAS PUERTAS DEL PADRE
			
			for(int i= 0; i< individuo.posicion_puertas.size(); i++)
			{
				if(individuo.dungeon[individuo.posicion_puertas.get(i).fila][individuo.posicion_puertas.get(i).columna].puerta_N)
				{
					if(individuo.dungeon[individuo.posicion_puertas.get(i).fila][individuo.posicion_puertas.get(i).columna].tipo_puerta_N.name().equals("ENTRADA_SALIDA"))
					{
						posicionStartX = individuo.posicion_puertas.get(i).fila;
						posicionStartY = individuo.posicion_puertas.get(i).columna;
					}
				}
				else if(individuo.dungeon[individuo.posicion_puertas.get(i).fila][individuo.posicion_puertas.get(i).columna].puerta_S)
				{
					if(individuo.dungeon[individuo.posicion_puertas.get(i).fila][individuo.posicion_puertas.get(i).columna].tipo_puerta_S.name().equals("ENTRADA_SALIDA"))
					{
						posicionStartX = individuo.posicion_puertas.get(i).fila;
						posicionStartY = individuo.posicion_puertas.get(i).columna;
					}
				}
				else if(individuo.dungeon[individuo.posicion_puertas.get(i).fila][individuo.posicion_puertas.get(i).columna].puerta_E)
				{
					if(individuo.dungeon[individuo.posicion_puertas.get(i).fila][individuo.posicion_puertas.get(i).columna].tipo_puerta_E.name().equals("ENTRADA_SALIDA"))
					{
						posicionStartX = individuo.posicion_puertas.get(i).fila;
						posicionStartY = individuo.posicion_puertas.get(i).columna;
					}
				}
				else if(individuo.dungeon[individuo.posicion_puertas.get(i).fila][individuo.posicion_puertas.get(i).columna].puerta_O)
				{
					if(individuo.dungeon[individuo.posicion_puertas.get(i).fila][individuo.posicion_puertas.get(i).columna].tipo_puerta_O.name().equals("ENTRADA_SALIDA"))
					{
						posicionStartX = individuo.posicion_puertas.get(i).fila;
						posicionStartY = individuo.posicion_puertas.get(i).columna;
					}
				}
			}
			
			individuo.generateDungeon(posicionStartX, posicionStartY);
			
//			System.out.println("Antes de calcular fintess 1");
//			individuo.pintar();
			
			individuo.calcularfitness(individuo.numero_puertas);
			
			return individuo;
		}
		
		/***
		 * Transforma el genotipo que esta en string formateado en un array de integers.
		 * @param gen
		 * @param f
		 * @param c
		 * @return
		 */
		@SuppressWarnings("unused")
		private int[] gen_str_to_int_arr(String gen, int f, int c){
			ArrayList<Integer> gen_arraylist = new ArrayList<>();
			int length = (f * c * 3);
			for(int i= 0; i<gen.length(); i++){
				String gen_pos =  Character.toString(gen.charAt(i));
				if(gen_pos.equals(" ")){
					continue;
				}
				else{
					gen_arraylist.add(Integer.parseInt(gen_pos));
				}
			}
			
			int[] gen_arr = new int[gen_arraylist.size()];
			for(int j= 0; j < gen_arraylist.size(); j++)
			{
				gen_arr[j] = gen_arraylist.get(j);
			}
			
			return gen_arr;
		}
}
