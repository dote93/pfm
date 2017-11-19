package Dungeon;


//Imports para escribir y modificar un archivo de texto
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

import java.util.ArrayList;
//import para la fecha
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LogWriter {
	
	
	//---------------- FECHA y VARIABLES PARA GUARDAR EN EL ARCHIVO -------------------
	
	/**
	 * Simple constructor
	 */
	public LogWriter()
	{
		super();
	}

	/**
	 * Constructor that is going to write a file with the info
	 * @param i
	 * @throws IOException 
	 */
	public LogWriter(String ruta, File file,String fecha_completa, int contador, ArrayList<Dungeon> Poblacion, EvPopulation evopopulation) throws IOException
	{
		//Variable con la que vamos a escribir en el archivo
		BufferedWriter bw = null;
		try
		{	
			//Si el archivo ya existe, solo escribo la linea correspondiente
			if(file.exists())
			{
				bw = new BufferedWriter(new FileWriter(file.getAbsolutePath() , true));
				bw.newLine();
				if(contador == 0)
				{
					bw.newLine();
					bw.newLine();
					bw.write("*******************************************************************************************");
					bw.newLine();
					bw.append("Iteracion NumIndividuos MejorFitness MejorGenotipo PeorFitness PeorGenotipo MediaFitness ");
					bw.newLine();
					bw.write(fecha_completa);
					bw.newLine();
					bw.newLine();
					bw.write("00"); //se anade la iteracion en la que estamos
				}
				else if(contador < 10)
				{
					bw.write("0" + Integer.toString(contador)); //se anade la iteracion en la que estamos
				}
				else
				{
					bw.write(Integer.toString(contador)); //se anade la iteracion en la que estamos
				}
				
				if(Poblacion.size() < 10)
				{
					bw.append(",0" + Poblacion.size());
				}
				else
				{
					bw.append("," + Poblacion.size());
				}
				bw.append("," + evopopulation.Individuos_parada.get(0).getFitness());
				
//				String mejorGenotipo = new String();
//				for(int tam_genotipo = 0; tam_genotipo < evopopulation.Individuos_parada.get(0).genotipo.length; tam_genotipo++)
//				{
//					
//					mejorGenotipo = mejorGenotipo + (evopopulation.Individuos_parada.get(0).genotipo[tam_genotipo]);
//				}
//				
//				bw.append(" " + mejorGenotipo); //se anade el genotipo del mejor individuo
//				
				Dungeon worstIndividuo = new Dungeon();
				worstIndividuo = evopopulation.getWorstIndividuo(Poblacion);
				
				bw.append("," + worstIndividuo.getFitness());
				
//				String peorGenotipo = new String();
//				for(int tam_genotipo = 0; tam_genotipo < worstIndividuo.genotipo.length; tam_genotipo++)
//				{
//					peorGenotipo = peorGenotipo + (worstIndividuo.genotipo[tam_genotipo]);
//				}
//				
//				bw.append(" " + peorGenotipo); //se anade el genotipo del peor individuo
//				
				double mean = evopopulation.getMeanPoblacion(Poblacion);
				
				bw.append("," + mean);
				
				double variance = evopopulation.getVariancePoblacion(Poblacion, mean);
				
				bw.append("," + variance);
				
				double standardDesviation = evopopulation.getStandardDeviationPoblacion(variance);
				
				bw.append("," + standardDesviation);
				
				
				double[] confidence_interval = evopopulation.getConfienceIntervalPoblacion(standardDesviation, mean, evopopulation.Poblacion.size());

				bw.append("," + confidence_interval[0]);
				
				bw.append("," + confidence_interval[1]);
				
			}
			
			//si el archivo no existia, escribo una primera linea informativa
			else
			{
				bw = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
				
				if(contador == 0)
				{
					bw.write("*******************************************************************************************");
					bw.newLine();
					bw.append("Iteracion NumIndividuos MejorFitness MejorGenotipo PeorFitness PeorGenotipo MediaFitness ");
					bw.newLine();
					bw.write(fecha_completa);
					bw.newLine();
					bw.newLine();
					bw.write("00"); //se anade la iteracion en la que estamos
				}
				else if(contador < 10)
				{
					bw.write("0" + Integer.toString(contador)); //se anade la iteracion en la que estamos
				}
				else
				{
					bw.write(Integer.toString(contador)); //se anade la iteracion en la que estamos
				}
				
				if(Poblacion.size() < 10)
				{
					bw.append(",0" + Poblacion.size());
				}
				else
				{
					bw.append("," + Poblacion.size());
				}
				bw.append("," + evopopulation.Individuos_parada.get(0).getFitness());
				
//				String mejorGenotipo = new String();
//				for(int tam_genotipo = 0; tam_genotipo < evopopulation.Individuos_parada.get(0).genotipo.length; tam_genotipo++)
//				{
//					
//					mejorGenotipo = mejorGenotipo + (evopopulation.Individuos_parada.get(0).genotipo[tam_genotipo]);
//				}
//				
//				bw.append(" " + mejorGenotipo); //se anade el genotipo del mejor individuo
//				
				Dungeon worstIndividuo = new Dungeon();
				worstIndividuo = evopopulation.getWorstIndividuo(Poblacion);
				
				bw.append("," + worstIndividuo.getFitness());
				
				String peorGenotipo = new String();
				
//				for(int tam_genotipo = 0; tam_genotipo < worstIndividuo.genotipo.length; tam_genotipo++)
//				{
//					peorGenotipo = peorGenotipo + (worstIndividuo.genotipo[tam_genotipo]);
//				}
//				
//				bw.append(" " + peorGenotipo); //se anade el genotipo del peor individuo
//				
				double mean = evopopulation.getMeanPoblacion(Poblacion);
				
				bw.append("," + mean);
				
				double variance = evopopulation.getVariancePoblacion(Poblacion, mean);
				
				bw.append("," + variance);
				
				double standardDesviation = evopopulation.getStandardDeviationPoblacion(variance);
				
				bw.append("," + standardDesviation);

				double[] confidence_interval = evopopulation.getConfienceIntervalPoblacion(standardDesviation, mean, evopopulation.Poblacion.size());

				bw.append("," + confidence_interval[0]);
				
				bw.append("," + confidence_interval[1]);
				
			}

		}
		catch (IOException e)
		{
			//Error processing code
			System.out.println("Error: " + e);
		}
		finally
		{
			//si el archivo existiao se ha creado y no ha habido ningun error, se cierra
			if(bw != null)
			{
				bw.close();
			}
		}
	}
	
}
