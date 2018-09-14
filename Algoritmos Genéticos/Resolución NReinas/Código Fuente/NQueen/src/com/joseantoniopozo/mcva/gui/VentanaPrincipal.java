package com.joseantoniopozo.mcva.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import org.jgap.InvalidConfigurationException;

import com.joseantoniopozo.mcva.algoritmoGenetico.AlgoritmoGenetico;
import com.joseantoniopozo.mcva.datos.ParametrosAlgoritmoGenetico;
import com.joseantoniopozo.mcva.datos.SalidaAlgoritmoGenetico;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class VentanaPrincipal implements Observer {

	// Frames
	private JFrame framePrincipal;
	private JFrame frameTablero;

	// Panels
	private JPanel panelControl;
	private JPanel panelParametros;
	private JPanel panelLog;
	private VentanaTablero panelTablero;

	// Componentes
	private JButton botonArrancar;
	private JButton botonConfigurar;
	private JButton botonRestablecer;
	private JButton botonParar;
	private JButton botonInformacion;
	private JComboBox<Integer> desplegableNumeroReinas;
	private JComboBox<String> desplegableOperadosCruce;
	private JComboBox<String> desplegableOperadosMutacion;
	private JComboBox<String> desplegableOperadosSeleccion;
	private JTextField textoProbabilidadCruce;
	private JTextField textoProbabilidadMutacion;
	private JTextField textoNumeroIndividuos;
	private JTextArea textoAreaLog;

	// Valores de entrada
	private ParametrosAlgoritmoGenetico parametrosEntrada;

	// Tamanyos
	private static final int TAMANYO_VERTICAL = 60;
	private static final int TAMANYO_HORIZONTAL = 55;

	// Valor Log
	private String valorLog = "";

	public static void arrancarApp() {
		VentanaPrincipal appNReinas = new VentanaPrincipal();
		appNReinas.crearGui();
		appNReinas.anyadirListener();
		appNReinas.restrablecer();
		appNReinas.lanzarAplicacion();
	}

	private void crearGui() {
		// Ventana principal
		framePrincipal = new JFrame("N Queens - Genetics Algorithms");
		framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framePrincipal.setLocation(25, 25);
		URL urlFavicon = getClass().getResource("/resources/iconos/favicon.png");
		ImageIcon favicon = new ImageIcon(urlFavicon);
		framePrincipal.setIconImage(favicon.getImage());

		// Panel Control
		panelControl = new JPanel();
		// panelControl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Dimension d2 = new Dimension(40, 40);
		panelControl.setPreferredSize(d2);
		panelControl.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Componentes Panel Control
		botonArrancar = new JButton();
		botonArrancar.setEnabled(false);
		botonArrancar.setBorder(null);
		URL direccion = getClass().getClassLoader().getResource("resources/iconos/run.png");
		ImageIcon icono = new ImageIcon(direccion);
		botonArrancar.setIcon(icono);
		botonArrancar.setToolTipText("Run");
		botonArrancar.setVerticalTextPosition(SwingConstants.CENTER);
		botonArrancar.setHorizontalTextPosition(SwingConstants.LEFT);
		panelControl.add(botonArrancar);

		botonParar = new JButton("");
		botonParar.setEnabled(false);
		botonParar.setBorder(null);
		direccion = getClass().getClassLoader().getResource("resources/iconos/stop.png");
		icono = new ImageIcon(direccion);
		botonParar.setIcon(icono);
		botonParar.setToolTipText("Stop");
		panelControl.add(botonParar);

		botonConfigurar = new JButton();
		botonConfigurar.setBorder(null);
		direccion = getClass().getClassLoader().getResource("resources/iconos/setup.png");
		icono = new ImageIcon(direccion);
		botonConfigurar.setIcon(icono);
		botonConfigurar.setToolTipText("Setup");
		panelControl.add(botonConfigurar);

		botonRestablecer = new JButton();
		botonRestablecer.setBorder(null);
		direccion = getClass().getClassLoader().getResource("resources/iconos/reset.png");
		icono = new ImageIcon(direccion);
		botonRestablecer.setIcon(icono);
		botonRestablecer.setToolTipText("Reset");
		panelControl.add(botonRestablecer);

		botonInformacion = new JButton();
		botonInformacion.setBorder(null);
		direccion = getClass().getClassLoader().getResource("resources/iconos/info.png");
		icono = new ImageIcon(direccion);
		botonInformacion.setIcon(icono);
		botonInformacion.setToolTipText("Information");
		panelControl.add(botonInformacion);
		
		// Panel Parametros
		panelParametros = new JPanel();
		// panelParametros.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Dimension d1 = new Dimension(310, 200);
		panelParametros.setPreferredSize(d1);
		panelParametros.setLayout(new GridLayout(7,2));

		// Componentes Panel Parametros
		desplegableNumeroReinas = new JComboBox<Integer>(ParametrosAlgoritmoGenetico.NUMERO_REINAS);
		JLabel etiquetaNumeroReinas = new JLabel("Número de Reinas:");
		etiquetaNumeroReinas.setLabelFor(desplegableNumeroReinas);
		panelParametros.add(etiquetaNumeroReinas);
		panelParametros.add(desplegableNumeroReinas);

		desplegableOperadosCruce = new JComboBox<String>(ParametrosAlgoritmoGenetico.OPERADORES_CRUCE);
		JLabel etiquetaOperadosCruce = new JLabel("Operador de Cruce:");
		etiquetaNumeroReinas.setLabelFor(desplegableOperadosCruce);
		panelParametros.add(etiquetaOperadosCruce);
		panelParametros.add(desplegableOperadosCruce);

		desplegableOperadosMutacion = new JComboBox<String>(ParametrosAlgoritmoGenetico.OPERADORES_MUTACION);
		JLabel etiquetaOperadosMutacion = new JLabel("Operador de Mutación:");
		etiquetaOperadosMutacion.setLabelFor(desplegableOperadosMutacion);
		panelParametros.add(etiquetaOperadosMutacion);
		panelParametros.add(desplegableOperadosMutacion);

		desplegableOperadosSeleccion = new JComboBox<String>(ParametrosAlgoritmoGenetico.OPERADORES_SELECCION);
		JLabel etiquetaOperadosSeleccion = new JLabel("Operador de Selección:");
		etiquetaOperadosSeleccion.setLabelFor(desplegableOperadosSeleccion);
		panelParametros.add(etiquetaOperadosSeleccion);
		panelParametros.add(desplegableOperadosSeleccion);

		textoProbabilidadCruce = new JTextField(3);
		JLabel etiquetaProbabilidadCruce = new JLabel("Probabilidad de Cruce:");
		etiquetaProbabilidadCruce.setLabelFor(textoProbabilidadCruce);
		panelParametros.add(etiquetaProbabilidadCruce);
		panelParametros.add(textoProbabilidadCruce);

		textoProbabilidadMutacion = new JTextField(3);
		JLabel etiquetaProbabilidadMutacion = new JLabel("Probabilidad de Mutación:");
		etiquetaProbabilidadMutacion.setLabelFor(textoProbabilidadMutacion);
		panelParametros.add(etiquetaProbabilidadMutacion);
		panelParametros.add(textoProbabilidadMutacion);

		textoNumeroIndividuos = new JTextField(3);
		JLabel etiquetaNumeroIndividuos = new JLabel("Número de Individuos:");
		etiquetaNumeroIndividuos.setLabelFor(textoNumeroIndividuos);
		panelParametros.add(etiquetaNumeroIndividuos);
		panelParametros.add(textoNumeroIndividuos);

		TitledBorder borderPanelParametros = new TitledBorder("Parametros");
		borderPanelParametros.setTitleJustification(TitledBorder.LEFT);
		borderPanelParametros.setTitlePosition(TitledBorder.TOP);
		panelParametros.setBorder(borderPanelParametros);		
		
		// Panel Log
		panelLog = new JPanel();

		// Componentes Panel Parametros
		textoAreaLog = new JTextArea(16, 26);
		textoAreaLog.setEditable(false);
		textoAreaLog.setLineWrap(true);
		textoAreaLog.setWrapStyleWord(true);
		DefaultCaret caret = (DefaultCaret) textoAreaLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		TitledBorder border = new TitledBorder("Log");
		border.setTitleJustification(TitledBorder.LEFT);
		border.setTitlePosition(TitledBorder.TOP);
		panelLog.setBorder(border);
		panelLog.add(new JScrollPane(textoAreaLog));

		framePrincipal.getContentPane().add(panelControl, BorderLayout.NORTH);
		framePrincipal.getContentPane().add(panelParametros, BorderLayout.WEST);
		framePrincipal.getContentPane().add(panelLog, BorderLayout.EAST);
	}

	private void anyadirListener() {
		botonArrancar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						arrancar();
					}
				}.start();

			}
		});

		botonParar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						AlgoritmoGenetico.parar();
						parar();
					}
				}.start();

			}
		});

		botonConfigurar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						boolean entradaValida = entradaValida();
						if (entradaValida) {
							configurar();
						} else {
							JOptionPane.showMessageDialog(framePrincipal,
									" - Probablidades => (0,1]\n - Número de Individuos => [5,5000]",
									"Entrada inválida", JOptionPane.WARNING_MESSAGE);
						}
					}
				}.start();

			}
		});

		botonRestablecer.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						restrablecer();
					}
				}.start();

			}
		});
		
		botonInformacion.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						JOptionPane.showMessageDialog(framePrincipal,
								"                    Author: José Antonio Pozo Núñez\n"+
								"          - Métodos Computacionales en Vida Artificial -\n"
								+ "- Máster en Lógica, Computación e Inteligencia Artificial -\n"
								+ "                      - Universidad de Sevilla - ",
								"Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}.start();
			}
		});
	}

	private void lanzarAplicacion() {
		framePrincipal.pack();
		framePrincipal.setVisible(true);
		framePrincipal.setResizable(false);
	}

	private boolean entradaValida() {
		boolean entradaValida = true;
		String valorProbabilidadCruce = textoProbabilidadCruce.getText();
		String valorProbabilidadMutacion = textoProbabilidadMutacion.getText();
		String valorNumeroIndividuos = textoNumeroIndividuos.getText();
		if (valorProbabilidadCruce != null || valorProbabilidadMutacion != null || valorNumeroIndividuos != null) {
			if (valorProbabilidadCruce.length() > 4 || !esDecimalCero(valorProbabilidadCruce)
					|| valorProbabilidadMutacion.length() > 4 || !esDecimalCero(valorProbabilidadMutacion)
					|| valorNumeroIndividuos.length() > 4 || !esEnteroMayorCinco(valorNumeroIndividuos)) {
				entradaValida = false;
			}

		} else {
			entradaValida = false;
		}

		return entradaValida;
	}

	public static boolean esDecimalCero(String cadena) {
		try {
			double d = Double.parseDouble(cadena);
			if (d <= 0 || d > 1) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean esEnteroMayorCinco(String cadena) {
		try {
			Integer i = Integer.parseInt(cadena);
			if (i <= 4 || i > 5000) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private void arrancar() {
		iniciarLog();
		configurarEntrada(false, true, false, false, false, false, false, false, false, false, false);
		lanzarTablero();
		AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico(parametrosEntrada);
		algoritmoGenetico.addObserver(this);
		try {
			algoritmoGenetico.lanzarAlgoritmo();
		} catch (Exception e) {
		}
	}

	private void parar() {
		finalizarLog();
		configurarEntrada(false, false, true, true, true, true, true, true, true, true, true);
		valorLog = "";
	}

	private void configurar() {
		configurarEntrada(true, false, false, true, false, false, false, false, false, false, false);
		cerrarTablero();
		textoAreaLog.setText("");
		parametrosEntrada = obtenerParametrosEntrada();
		valorLog = "";
	}

	private void restrablecer() {
		configurarEntrada(false, false, true, true, true, true, true, true, true, true, true);
		cerrarTablero();
		desplegableNumeroReinas.setSelectedIndex(4);
		desplegableOperadosCruce.setSelectedIndex(0);
		desplegableOperadosMutacion.setSelectedIndex(0);
		desplegableOperadosSeleccion.setSelectedIndex(0);
		textoProbabilidadCruce.setText("0.8");
		textoProbabilidadMutacion.setText("0.2");
		textoNumeroIndividuos.setText("50");
		textoAreaLog.setText("");
		valorLog = "";
	}

	private void cerrarTablero() {
		if (frameTablero != null && frameTablero.isVisible()) {
			frameTablero.setVisible(false);
		}
	}

	private void lanzarTablero() {
		int numeroReinas = parametrosEntrada.getNumeroReinas();
		frameTablero = new JFrame("Tablero " + numeroReinas + "x" + numeroReinas);
		panelTablero = new VentanaTablero(numeroReinas);
		frameTablero.add(panelTablero);
		frameTablero.pack();
		frameTablero.setResizable(false);
		URL urlFavicon = getClass().getResource("/resources/iconos/favicon.png");
		ImageIcon favicon = new ImageIcon(urlFavicon);
		frameTablero.setIconImage(favicon.getImage());
		int longitudHorizontal = numeroReinas * TAMANYO_HORIZONTAL;
		int longitudVertical = numeroReinas * TAMANYO_VERTICAL;
		frameTablero.setSize(longitudHorizontal, longitudVertical);
		frameTablero.setLocationRelativeTo(null);
		frameTablero.setVisible(true);
	}

	private void configurarEntrada(boolean habilitarBotonArrancar, boolean habilitarBotonParar,
			boolean habilitarBotonConfigurar, boolean habilitarBotonRestablecer,
			boolean habilitarDesplegableNumeroReinas, boolean habilitarOperadorCruce,
			boolean habilitarOperadorSeleccion, boolean habilitarOperadorMutacion,
			boolean habilitarTextoProbabilidadCruce, boolean habilitarTextoProbabilidadMutacion,
			boolean habilitarTextoNumeroIndividuos) {
		botonArrancar.setEnabled(habilitarBotonArrancar);
		botonParar.setEnabled(habilitarBotonParar);
		botonConfigurar.setEnabled(habilitarBotonConfigurar);
		botonRestablecer.setEnabled(habilitarBotonRestablecer);
		desplegableNumeroReinas.setEnabled(habilitarDesplegableNumeroReinas);
		desplegableOperadosCruce.setEnabled(habilitarOperadorCruce);
		desplegableOperadosMutacion.setEnabled(habilitarOperadorMutacion);
		desplegableOperadosSeleccion.setEnabled(habilitarOperadorSeleccion);
		textoProbabilidadCruce.setEnabled(habilitarTextoProbabilidadCruce);
		textoProbabilidadMutacion.setEnabled(habilitarTextoProbabilidadMutacion);
		textoNumeroIndividuos.setEnabled(habilitarTextoNumeroIndividuos);
	}

	private ParametrosAlgoritmoGenetico obtenerParametrosEntrada() {
		int numeroReinas = (Integer) desplegableNumeroReinas.getSelectedItem();
		int operadorCruce = desplegableOperadosCruce.getSelectedIndex();
		int operadorMutacion = desplegableOperadosMutacion.getSelectedIndex();
		int operadorSeleccion = desplegableOperadosSeleccion.getSelectedIndex();
		double probabilidadCruce = Double.valueOf(textoProbabilidadCruce.getText());
		double probabilidadMutacion = Double.valueOf(textoProbabilidadMutacion.getText());
		int numeroIndividuos = Integer.valueOf(textoNumeroIndividuos.getText());
		ParametrosAlgoritmoGenetico parametrosAlgoritmosGeneticos = new ParametrosAlgoritmoGenetico(numeroReinas,
				operadorCruce, operadorMutacion, operadorSeleccion, probabilidadCruce, probabilidadMutacion,
				numeroIndividuos);
		return parametrosAlgoritmosGeneticos;
	}

	@Override
	public void update(Observable o, Object arg) {
		SalidaAlgoritmoGenetico salidaAlgoritmoGenetico = (SalidaAlgoritmoGenetico) arg;
		actualizarTablero(salidaAlgoritmoGenetico);
		actualizarLog(salidaAlgoritmoGenetico);
		actualizarPantallaPrincipal(salidaAlgoritmoGenetico);
	}

	private void actualizarTablero(SalidaAlgoritmoGenetico salidaAlgoritmoGenetico) {
		Integer[] posicionReinas = salidaAlgoritmoGenetico.getPosicionReinas();
		panelTablero.setPosicionReinas(posicionReinas);
		panelTablero.repaint();
	}

	private void iniciarLog() {
		textoAreaLog.setText("- INICIO EJECUCIÓN -\n\n");
		textoAreaLog.append("###\n");
	}

	private void finalizarLog() {
		textoAreaLog.append("\n- FIN EJECUCIÓN -");
	}

	private void actualizarLog(SalidaAlgoritmoGenetico salidaAlgoritmoGenetico) {
		boolean esFinal = salidaAlgoritmoGenetico.esFinal();
		if (esFinal) {
			long totalMiliSegundos = salidaAlgoritmoGenetico.getTiempoTotal();
			String tiempoTotal = "- Tiempo total: " + totalMiliSegundos + " milisegundos.";
			boolean stop = salidaAlgoritmoGenetico.isStop();

			String solucion = "";
			if (stop) {
				solucion = "- Parado por el usuario.\n- Última solución encontrada: "
						+ Arrays.toString(salidaAlgoritmoGenetico.getPosicionReinas());
			} else {
				boolean esSolucion = salidaAlgoritmoGenetico.esSolucion();
				if (esSolucion) {
					solucion = "- Solución encontrada: " + Arrays.toString(salidaAlgoritmoGenetico.getPosicionReinas());
					;
				} else {
					solucion = "- Número máximo de generaciones alcanzado.\n- Última solución encontrada: "
							+ Arrays.toString(salidaAlgoritmoGenetico.getPosicionReinas());
					;
				}
			}
			valorLog = valorLog + "\n" + solucion + "\n";
			valorLog = valorLog + tiempoTotal + "\n";
		} else {
			String generacion = "- Generación: " + salidaAlgoritmoGenetico.getNumeroGeneracion();
			String valorFuncionFitness = "- Valor Fitness: " + salidaAlgoritmoGenetico.getValorFuncionFitness();
			String individuo = "- Individuo: " + Arrays.toString(salidaAlgoritmoGenetico.getPosicionReinas());
			valorLog = valorLog + generacion + "\n";
			valorLog = valorLog + valorFuncionFitness + "\n";
			valorLog = valorLog + individuo + "\n";
			valorLog = valorLog + "###\n";

		}
		if (esFinal || salidaAlgoritmoGenetico.getNumeroGeneracion() % 100 == 0) {
			textoAreaLog.append(valorLog);
			valorLog = "";
		}
	}

	private void actualizarPantallaPrincipal(SalidaAlgoritmoGenetico salidaAlgoritmoGenetico) {
		boolean esFinal = salidaAlgoritmoGenetico.esFinal();
		if (esFinal) {
			parar();
		}
	}

}
