package com.joseantoniopozo.mcva.operadores.cruce;

import java.util.List;

import org.jgap.BaseGeneticOperator;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;

public class UniformCrossoverOperator extends BaseGeneticOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5308088585558503372L;
	private double porcentajeCruce;

	public UniformCrossoverOperator(final Configuration configuracion, final double porcentajeCruce)
			throws InvalidConfigurationException {
		super(configuracion);
		this.porcentajeCruce = porcentajeCruce;
	}

	public void operate(final Population poblacion, final List cromosomasCandidatos) {
		int tamaanyoPoblacion = Math.min(getConfiguration().getPopulationSize(), poblacion.size());
		int numeroCromosomas = (int) (tamaanyoPoblacion * porcentajeCruce) / 2;
		RandomGenerator generador = getConfiguration().getRandomGenerator();
		for (int i = 0; i < numeroCromosomas; i++) {
			IChromosome primerCromosoma = (IChromosome) poblacion.getChromosome(generador.nextInt(tamaanyoPoblacion))
					.clone();
			IChromosome segundoCromosoma = (IChromosome) poblacion.getChromosome(generador.nextInt(tamaanyoPoblacion))
					.clone();
			try {
				IChromosome primerCromosomaModificado = operate(primerCromosoma, segundoCromosoma);
				IChromosome segundoCromosomaModificado = operate(primerCromosoma, segundoCromosoma);
				cromosomasCandidatos.add(primerCromosomaModificado);
				cromosomasCandidatos.add(segundoCromosomaModificado);
			} catch (Exception e) {
			}
		}
	}

	public IChromosome operate(final IChromosome primerCromosoma, final IChromosome segundoCromosoma)
			throws InvalidConfigurationException {
		IChromosome cromosomaResultante = (IChromosome) primerCromosoma.clone();
		Gene[] genesSegundoCromosoma = segundoCromosoma.getGenes();
		int tamanyoCromosoma = primerCromosoma.size();
		Gene[] genesResultantes = cromosomaResultante.getGenes();
		for (int i = 0; i < tamanyoCromosoma; i++) {
			boolean genSegundoCromosoma = getConfiguration().getRandomGenerator().nextBoolean();
			if (genSegundoCromosoma) {
				genesResultantes[i] = genesSegundoCromosoma[i];
			}
		}
		cromosomaResultante.setGenes(genesResultantes);
		return cromosomaResultante;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

}
