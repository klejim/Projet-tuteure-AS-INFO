package project.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.*;
/**
 * 
 * @author yoann
 * Classe abstraite qui décrit un élément de la vue calqué sur son équivalent modèle
 */

public abstract class StatusWindowElement {

	// node référent au model
	protected Node modelNode;

	// Collection des Labels d'affichage
	protected ArrayList<JLabel> content;

	// JPanel de l'élément
	protected JPanel elementDisplay;
	
	/**
	 * Récupère et met en forme les données du modèle pour l'affichage
	 * @return tableaux de {@code Strings} pour chaque champ à afficher dans la ligne
	 */
	public abstract String[] formatData();


	/**
	 * Met à jour l'affichage par rapport au modèle.
	 */
	public void updateDisplay() {
		Iterator<JLabel> it = content.iterator();
		for(String param : this.formatData()) {
			it.next().setText(param);
		}
	}
	
	/**
	 * Créé les labels dans elementDisplay à partir de content
	 */
	public void createDisplay() {
		
		// efface tous les labels
		this.elementDisplay.removeAll();

		// rajoute les nouveaux labels
		for(JLabel label : this.content) {
			this.elementDisplay.add(label);
		}

		// Rajoute des labels vides pour l'affichage
		for(int i= 4-this.content.size(); i > 0; i--) {
			this.elementDisplay.add(new JLabel(""));
		}
	}

	/**
	 * Vérifie que l'élément correspond encore à un élément du réseau dans le modèle.
	 * INUTILE dans un réseau statique
	 * @return {@code true} si le modèle existe, {@code false} sinon
	 */
	public boolean isAlive() {
		return modelNode != null;
	}
	
	/**
	 * Retourne le panel de l'élément
	 * @return JPanel
	 */	
	public JPanel getDisplay() {
		return this.elementDisplay;
	}




}
