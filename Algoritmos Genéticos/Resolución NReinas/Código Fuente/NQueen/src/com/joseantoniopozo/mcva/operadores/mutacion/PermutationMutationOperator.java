package com.joseantoniopozo.mcva.operadores.mutacion;

import java.util.List;

import org.jgap.BaseGeneticOperator;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;

public class PermutationMutationOperator extends BaseGeneticOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -334419603978425576L;
	private int porcentajeMutacion;

	public PermutationMutationOperator(final Configuration configuracion, final int porcentajeCruce)
			throws InvalidConfigurationException {
		super(configuracion);
		this.porcentajeMutacion = porcentajeCruce;
	}

	public void operate(final Population poblacion, final List cromosomasCandidatos) {
		int tamaanyoPoblacion = Math.min(getConfiguration().getPopulationSize(), poblacion.size());
		RandomGenerator generador = getConfiguration().getRandomGenerator();
		for (int i = 0; i < tamaanyoPoblacion; i++) {
			IChromosome cromosomaAModificar = (IChromosome) poblacion
					.getChromosome(generador.nextInt(tamaanyoPoblacion)).clone();
			try {
				IChromosome cromosomaModificado = operate(cromosomaAModificar);
				if (cromosomaModificado != null) {
					cromosomasCandidatos.add(cromosomaModificado);
				}
			} catch (Exception e) {
			}
		}
	}

	public IChromosome operate(final IChromosome cromosomaOriginal) throws InvalidConfigurationException {
		IChromosome cromosomaResultante = (IChromosome) cromosomaOriginal.clone();
		int tamanyoCromosoma = cromosomaResultante.size();
		Gene[] genesResultantes = cromosomaResultante.getGenes();
		boolean haMutado = false;
		int indiceInicio = getConfiguration().getRandomGenerator().nextInt(2);
		for (int i = indiceInicio; i < tamanyoCromosoma; i = i + 2) {
			boolean mutar = getConfiguration().getRandomGenerator().nextInt(porcentajeMutacion)==0;
			if (mutar) {
				int indiceGenAnterior = 0;
				if (i == 0) {
					indiceGenAnterior = tamanyoCromosoma - 1;
				} else {
					indiceGenAnterior = i - 1;
				}
				Gene genAnterior = genesResultantes[indiceGenAnterior];
				genesResultantes[indiceGenAnterior] = genesResultantes[i];
				genesResultantes[i] = genAnterior;
				haMutado = true;
			}
		}
		if (!haMutado) {
			cromosomaResultante = null;
		} else {
			cromosomaResultante.setGenes(genesResultantes);
		}
		return cromosomaResultante;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

}
