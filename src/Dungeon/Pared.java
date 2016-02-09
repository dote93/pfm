package Dungeon;



/**
 * Clase que gestiona la pared
 * @version 1.0
 * @author  Alvaro Valle del Pozo
 * 			TFM
 * 			Profesor: Jose Maria Font y Daniel Manrique
 * 			Fecha: 01-02-2016
 */

public class Pared 
{
		
	//Se hace un enum con el tipo de pared que es
	public enum Direcciones
	{
		ESTE, OESTE, NORTE, SUR
	};
	
	//Direccion de  la pared
	public Direcciones direction;
	
	//Si la pared esta abierta
	public boolean open;
	
	/** 
     *	Contructor de Pared
     */
	public Pared(Pared.Direcciones direccionRecibe, boolean isOpen )
	{
		//guardamos la direccion que recibimos a la variable global que tenemos para poder usarlo en otras funciones
		direction = direccionRecibe;
		
		//guardamos si la pared esta abierta o no a la variable global que tenemos creada para poder usarlo en otras funciones 
		open = isOpen;
		
	}
	
	/** 
     *	Funci—n que devuelve la direccion de la pared
     */
	public Pared.Direcciones getDirection()
	{
		return direction;
	}
	
	/** 
     *	Funci—n que devuelve el resultado de desplazarse de la celda que recibe y la siguiente posicion a la que se va a mover pasandole un array unidimensional de int con la posicion x, y 
     */
	public int[] movement(int x, int y, Direcciones direction) //**********************************************************************************
	{
		//La celda en la que estoy
		int cellX = x;
		int cellY = y;
		
		//paso un numero para saber la posicion ( 0,1,2,3)
		//Switch para saber la posicion
		switch(direction)
		{
		case ESTE:
			//nos movemos una posicion a la derecha en el eje x
			cellY += 1;
			break;
		case OESTE:
			//nos movemos una posicion a la izquierda en el eje x
			cellY -= 1;
			break;
		case NORTE:
			//nos movemos una posicion hacia arriba en el eje y
			cellX -= 1;
			break;
		case SUR:
			//nos movemos una posicion hacia abajo en el eje y
			cellX += 1;
			break;
		}
		//Devolvemos un array con los valores de las coordenadas x eys
		int[] result = {cellX, cellY};
		return result;
	}
	
	public int[] movement(int x, int y)
	{
		//La celda en la que estoy
		int cellX = x;
		int cellY = y;
		
		//paso un numero para saber la posicion ( 0,1,2,3)
		//Switch para saber la posicion
		switch(direction)
		{
		case ESTE:
			//nos movemos una posicion a la derecha en el eje y
			cellY += 1;
			break;
		case OESTE:
			//nos movemos una posicion a la izquierda en el eje y
			cellY -= 1;
			break;
		case NORTE:
			//nos movemos una posicion hacia arriba en el eje x
			cellX -= 1;
			break;
		case SUR:
			//nos movemos una posicion hacia abajo en el eje x
			cellX += 1;
			break;
		}
		//Devolvemos un array con los valores de las coordenadas x y
		int[] result = {cellX, cellY};
		return result;
	}
}
