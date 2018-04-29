package project.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import project.network.Network;

/**
 * Classe abstraite qui représente un élément dans la fenêtre InputWindow. 
 * 
 * @author yoann
 *
 */
public abstract class InputWindowElement {

	// JPanel de l'élément
	protected JPanel elementDisplay;

	// Réseau
	protected Network model;


	// titre de l'élément
	protected JLabel titre;


	/**
	 * Constructeur
	 * @param ntw le réseau rattaché à l'élément
	 * @param titre le titre de l'élément
	 */
	public InputWindowElement(Network ntw, String titre) {
		this.titre = new JLabel(titre, SwingConstants.CENTER);
		this.model = ntw;

		this.elementDisplay = new JPanel();

		// 1 colone, n lignes - espacement horizontal et vertical 5px
		this.elementDisplay.setLayout(new GridLayout(0, 1, 5, 5));

		// ajout du titre au panel
		this.titre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		this.elementDisplay.add(this.titre);
		this.elementDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));

	}

	/**
	 * @return Le JPanel de l'élément
	 */
	public JPanel getDisplay() {
		return elementDisplay;
	}

	/**
	 * Créé et initialise l'élément
	 */
	public abstract void createElement();

	/**
	 * Met à jour l'affichage de l'éléement
	 */
	public abstract void updateElement();


}
