package com.joseantoniopozo.mcva.fitness;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.impl.IntegerGene;

public class FuncionAptitudNReinas extends FitnessFunction {

	private static final long serialVersionUID = -5736245135638018186L;

	private int numeroReinas;
	public final static int MEJOR_VALOR_APTITUD = 0;

	public FuncionAptitudNReinas(int numeroReinas) {
		super();
		this.numeroReinas = numeroReinas;
	}

	@Override
	protected double evaluate(IChromosome cromosoma) {
		Gene[] genes = cromosoma.getGenes();
		int numeroConflictos = obtenerNumeroDeConflictos(genes);
		return numeroConflictos;
	}

	private int obtenerNumeroDeConflictos(Gene[] genes) {
		int numeroConflictos = 0;
		for (int i = 0; i < this.numeroReinas - 1; i++) {
			for (int j = i + 1; j < this.numeroReinas; j++) {
				if (conflictosDiagonal(genes[i], genes[j], i, j) || conflictosFila(genes[i], genes[j]))
					numeroConflictos++;
			}
		}
		return numeroConflictos;
	}

	private boolean conflictosDiagonal(Gene reina1, Gene reina2, int columnaReina1, int columnaReina2) {
		boolean existeConflictoDiagonal = false;
		if (columnaReina1 != columnaReina2) {
			IntegerGene gen1 = (IntegerGene) reina1;
			IntegerGene gen2 = (IntegerGene) reina2;
			int filaReina1 = gen1.intValue();
			int filaReina2 = gen2.intValue();
			existeConflictoDiagonal = (Math.abs(filaReina1 - filaReina2) == Math.abs(columnaReina1 - columnaReina2));
		}
		return existeConflictoDiagonal;
	}

	private boolean conflictosFila(Gene reina1, Gene reina2) {
		boolean existeConflictoFila = reina1.equals(reina2);
		return existeConflictoFila;
	}

}