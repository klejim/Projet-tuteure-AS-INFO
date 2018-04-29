package project.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import project.network.Network;
import project.network.Node;

/**
 * Classe qui génère le bloc d'affichage et de modification des consommations des groupes présents sur le réseau
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
	 * Créé et initialisé l'élément
	 */
	public abstract void createElement();
	
	/**
	 * Met à jour l'affichage de l'éléementd
	 */
	public abstract void updateElement();
	

}
