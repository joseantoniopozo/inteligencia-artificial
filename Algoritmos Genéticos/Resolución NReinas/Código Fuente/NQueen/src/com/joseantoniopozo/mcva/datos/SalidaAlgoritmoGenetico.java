package com.joseantoniopozo.mcva.datos;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class SalidaAlgoritmoGenetico {

	private int numeroGeneracion;
	private double valorFuncionFitness;
	private Integer[] posicionReinas;
	private boolean esSolucion;
	private boolean esFinal;
	private boolean stop;
	private long tiempoTotal;

	public SalidaAlgoritmoGenetico(int numeroGeneracion, double valorFuncionFitness, Integer[] posicionReinas) {
		super();
		this.numeroGeneracion = numeroGeneracion;
		this.valorFuncionFitness = valorFuncionFitness;
		this.posicionReinas = posicionReinas;
		this.esFinal = false;
	}

	public SalidaAlgoritmoGenetico(Integer[] posicionReinas, boolean esSolucion, boolean stop, long tiempoTotal) {
		super();
		this.posicionReinas = posicionReinas;
		this.esSolucion = esSolucion;
		this.esFinal = true;
		this.stop = stop;
		this.tiempoTotal = tiempoTotal;
	}

	public int getNumeroGeneracion() {
		return numeroGeneracion;
	}

	public double getValorFuncionFitness() {
		return valorFuncionFitness;
	}

	public Integer[] getPosicionReinas() {
		return posicionReinas;
	}

	public boolean esSolucion() {
		return esSolucion;
	}

	public boolean esFinal() {
		return esFinal;
	}

	public boolean isStop() {
		return stop;
	}

	public long getTiempoTotal() {
		return tiempoTotal;
	}

}
