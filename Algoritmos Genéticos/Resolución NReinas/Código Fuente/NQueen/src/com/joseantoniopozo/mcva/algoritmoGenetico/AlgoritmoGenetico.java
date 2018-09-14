package com.joseantoniopozo.mcva.algoritmoGenetico;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.SwappingMutationOperator;
import org.jgap.impl.TournamentSelector;

import com.joseantoniopozo.mcva.datos.ParametrosAlgoritmoGenetico;
import com.joseantoniopozo.mcva.datos.SalidaAlgoritmoGenetico;
import com.joseantoniopozo.mcva.fitness.FuncionAptitudNReinas;

public class AlgoritmoGenetico extends Observable {

	private ParametrosAlgoritmoGenetico parametros;
	private Integer[] posicionReinas;
	private static boolean stop;
	protected static final int MAX_EVOLUCIONES = 25000;

	public AlgoritmoGenetico(ParametrosAlgoritmoGenetico parametros) {
		super();
		this.parametros = parametros;
		this.posicionReinas = new Integer[parametros.getNumeroReinas()];
		stop = false;
	}

	public void lanzarAlgoritmo() throws InvalidConfigurationException {

		SalidaAlgoritmoGenetico salida;
		Gene[] genes;
		IChromosome mejorIndividuo;
		double valorFitnessMejorIndividuo;
		boolean solucionEncontrada = false;
		long instanteComienzo = 0;
		long instanteFinal = 0;
		Genotype poblacion = this.create(parametros.getNumeroIndividuos());

		instanteComienzo = System.currentTimeMillis();
		for (int i = 0; i < MAX_EVOLUCIONES && !solucionEncontrada && !stop; i++) {
			poblacion.evolve();
			mejorIndividuo = poblacion.getFittestChromosome();
			valorFitnessMejorIndividuo = mejorIndividuo.getFitnessValue();
			genes = mejorIndividuo.getGenes();
			obtenerPosicionReinas(genes);

			salida = new SalidaAlgoritmoGenetico(i + 1, valorFitnessMejorIndividuo, posicionReinas);
			notificar(salida);

			if (valorFitnessMejorIndividuo == FuncionAptitudNReinas.MEJOR_VALOR_APTITUD) {
				solucionEncontrada = true;
			}
		}
		instanteFinal = System.currentTimeMillis();
		long totalSegundos = instanteFinal - instanteComienzo;
		salida = new SalidaAlgoritmoGenetico(posicionReinas, solucionEncontrada, stop, totalSegundos);
		notificar(salida);
	}

	private void obtenerPosicionReinas(Gene[] genes) {
		int numeroReinas = parametros.getNumeroReinas();
		for (int i = 0; i < numeroReinas; i++) {
			posicionReinas[i] = (Integer) genes[i].getAllele();
		}
	}

	private void notificar(SalidaAlgoritmoGenetico salida) {
		setChanged();
		notifyObservers(salida);
	}

	private Genotype create(int tamanyoPoblacion) throws InvalidConfigurationException {

		Configuration conf = new DefaultConfiguration();
		conf.setPreservFittestIndividual(true);

		Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
		conf.setFitnessEvaluator(new DeltaFitnessEvaluator());

		conf.addGeneticOperator(parametros.obtenerOperadorCruce(conf));
		conf.addGeneticOperator(parametros.obtenerOperadorMutacion(conf));
		conf.addNaturalSelector(parametros.obtenerOperadorSeleccion(conf), true);

		int numeroReinas = parametros.getNumeroReinas();
		FuncionAptitudNReinas funcionAptitudNReinas = new FuncionAptitudNReinas(numeroReinas);
		conf.setFitnessFunction(funcionAptitudNReinas);

		Gene[] genesEjemplo = new Gene[numeroReinas];
		for (int i = 0; i < numeroReinas; i++) {
			genesEjemplo[i] = new IntegerGene(conf, 0, numeroReinas - 1);
			genesEjemplo[i].setAllele((int) (i));
		}

		IChromosome sampleChromosome = new Chromosome(conf, genesEjemplo);
		conf.setSampleChromosome(sampleChromosome);
		conf.setPopulationSize(tamanyoPoblacion);
		Genotype population = Genotype.randomInitialGenotype(conf);

		return population;
	}

	public static void parar() {
		stop = true;
	}

}
