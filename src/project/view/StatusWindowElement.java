package project.view;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.*;

/**
 *Classe abstraite qui décrit un élément de la vue calqué sur son équivalent modèle
 * @author yoann
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
		for (String param : this.formatData()) {
			it.next().setText(param);
		}
	}

	/**
	 * Ajoute les labels de l'élément à son panel d'affichage
	 */
	public void createDisplay() {

		// efface tous les labels
		this.elementDisplay.removeAll();

		// création des labels
		for (String data : formatData()) {
			JLabel block = new JLabel(data);
			this.content.add(block);
			this.elementDisplay.add(block);
		}

		// Rajoute des labels vides pour l'affichage
		for (int i = 4 - this.content.size(); i > 0; i--) {
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
