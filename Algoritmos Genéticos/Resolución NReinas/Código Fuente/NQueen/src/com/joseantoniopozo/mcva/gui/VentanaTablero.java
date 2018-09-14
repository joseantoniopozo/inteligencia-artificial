package com.joseantoniopozo.mcva.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

public class VentanaTablero extends JPanel {

	private int numeroCasillas;
	private Integer[] posicionReinas;
	private static final int TAMANYO_CASILLA = 50;
	private static final Color COLOR_REINA = Color.RED;
	private static final Color COLORES[] = new Color[] { Color.WHITE, Color.BLACK };

	public VentanaTablero(int numeroCasillas) {
		super();
		this.numeroCasillas = numeroCasillas;
		this.posicionReinas = new Integer[numeroCasillas];
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		int xPos = 0;
		int yPos = 0;
		int colorIndex = 0;

		for (int i = 0; i < this.numeroCasillas; i++) {

			if (this.numeroCasillas % 2 == 0) {
				if (colorIndex == 0) {
					colorIndex = 1;
				} else {
					colorIndex = 0;
				}
			}

			for (int j = 0; j < numeroCasillas; j++) {
				int filaReina = 999999999;
				if (posicionReinas != null && posicionReinas[0] != null) {
					filaReina = posicionReinas[j];
				}
				Color tileColor = COLORES[colorIndex];
				Rectangle tile = new Rectangle(xPos, yPos, TAMANYO_CASILLA, TAMANYO_CASILLA);
				if (filaReina == i) {
					tileColor = COLOR_REINA;
				}
				g2d.setColor(tileColor);
				g2d.fill(tile);

				colorIndex = (colorIndex + 1) % 2;
				xPos += TAMANYO_CASILLA;
			}

			yPos += TAMANYO_CASILLA;
			xPos = 0;
		}
	}

	public void setPosicionReinas(Integer[] posicionesReinas) {
		this.posicionReinas = posicionesReinas;
	}

}
