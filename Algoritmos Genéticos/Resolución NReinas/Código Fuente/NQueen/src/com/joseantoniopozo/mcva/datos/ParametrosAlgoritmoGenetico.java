package com.joseantoniopozo.mcva.datos;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.jgap.Configuration;
import org.jgap.GeneticOperator;
import org.jgap.InvalidConfigurationException;
import org.jgap.NaturalSelector;
import org.jgap.impl.AveragingCrossoverOperator;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.GaussianMutationOperator;
import org.jgap.impl.SwappingMutationOperator;
import org.jgap.impl.TournamentSelector;
import org.jgap.impl.TwoWayMutationOperator;
import org.jgap.impl.WeightedRouletteSelector;

import com.joseantoniopozo.mcva.operadores.cruce.EvenOddCrossoverOperator;
import com.joseantoniopozo.mcva.operadores.cruce.UniformCrossoverOperator;
import com.joseantoniopozo.mcva.operadores.mutacion.PermutationMutationOperator;
import com.joseantoniopozo.mcva.operadores.mutacion.UniformMutationOperator;

public class ParametrosAlgoritmoGenetico {

	private int numeroReinas;
	private int operadorCruce;
	private int operadorMutacion;
	private int operadorSeleccion;
	private double probabilidadCruce;
	private double probabilidadMutacion;
	private int numeroIndividuos;

	public static final Integer[] NUMERO_REINAS = { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
	public static final String[] OPERADORES_CRUCE = { "Average", "EvenOdd", "Uniform" };
	public static final String[] OPERADORES_MUTACION = { "Gaussian", "Permutation", "Uniform" };
	public static final String[] OPERADORES_SELECCION = { "Tournament", "Best Chromosomes" };

	public ParametrosAlgoritmoGenetico(int numeroReinas, int operadorCruce, int operadorMutacion, int operadorSeleccion,
			double probabilidadCruce, double probabilidadMutacion, int numeroIndividuos) {
		super();
		this.numeroReinas = numeroReinas;
		this.operadorCruce = operadorCruce;
		this.operadorMutacion = operadorMutacion;
		this.operadorSeleccion = operadorSeleccion;
		this.probabilidadCruce = probabilidadCruce;
		this.probabilidadMutacion = probabilidadMutacion;
		this.numeroIndividuos = numeroIndividuos;
	}

	public GeneticOperator obtenerOperadorCruce(Configuration configuracion) throws InvalidConfigurationException {
		GeneticOperator operadorCruce = null;
		switch (this.operadorCruce) {
		case 0:
			AveragingCrossoverOperator operadorCruceAveraging = new AveragingCrossoverOperator(configuracion);
			operadorCruceAveraging.setCrossoverRate((int) (1 / probabilidadCruce));
			operadorCruce = operadorCruceAveraging;
			break;
		case 1:
			operadorCruce = new EvenOddCrossoverOperator(configuracion, probabilidadCruce);
			break;
		case 2:
			operadorCruce = new UniformCrossoverOperator(configuracion, probabilidadCruce);
			break;
		}
		return operadorCruce;

	}

	public GeneticOperator obtenerOperadorMutacion(Configuration configuracion) throws InvalidConfigurationException {
		GeneticOperator operadorMutacion = null;
		switch (this.operadorMutacion) {
		case 0:
			operadorMutacion = new GaussianMutationOperator(configuracion, (int) (1 / probabilidadMutacion));
			break;
		case 1:
			operadorMutacion = new PermutationMutationOperator(configuracion, (int) (1 / probabilidadMutacion));
			break;
		case 2:
			operadorMutacion = new UniformMutationOperator(configuracion, (int) (1 / probabilidadMutacion));
			break;
		}
		return operadorMutacion;
	}

	public NaturalSelector obtenerOperadorSeleccion(Configuration configuracion) throws InvalidConfigurationException {
		NaturalSelector operadorSeleccion = null;
		switch (this.operadorSeleccion) {
		case 0:
			operadorSeleccion = new TournamentSelector(configuracion, 10, 0.8);
			break;
		case 1:
			operadorSeleccion = new BestChromosomesSelector(configuracion);
			break;
		}
		return operadorSeleccion;
	}

	public int getOperadorCruce() {
		return operadorCruce;
	}

	public int getNumeroReinas() {
		return numeroReinas;
	}

	public int getOperadorMutacion() {
		return operadorMutacion;
	}

	public int getOperadorSeleccion() {
		return operadorSeleccion;
	}

	public double getProbabilidadCruce() {
		return probabilidadCruce;
	}

	public double getProbabilidadMutacion() {
		return probabilidadMutacion;
	}

	public int getNumeroIndividuos() {
		return numeroIndividuos;
	}

}
