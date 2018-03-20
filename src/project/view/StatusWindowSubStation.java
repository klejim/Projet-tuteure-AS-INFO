package project.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.*;
/**
 * 
 * @author yoann
 * 
 * Classe qui décrit une sous-station du réseau dans l'affichage Swing. Elle permet :
 * de construire une sous-station à partir du modèle, 
 * de mettre les informations relatives à cette sous-station et aux éléments qui lui sont reliés, 
 * de mettre en forme l'affichage de ces informations,
 * de fournir un conteneur swing pour l'affichage
 * 
 * TODO : gestion mutualisé via fonction private de l'ajout/retrait dans content et subStationDisplay
 *
 */
public class StatusWindowSubStation {
	
	private int nbGroups;
	private int nbPowerPlants;
	
	// Collection des lignes avec Labels
	private HashMap<String, ArrayList<JLabel>> content;
		
	// JPanel de la sous-station
	private JPanel subStationDisplay;

	/**
	 * Constructeur par défaut. Init à un champ station
	 */
	public StatusWindowSubStation() {
		this.nbGroups = 0;
		this.nbPowerPlants = 0;
		
		this.content = new HashMap<>();
		
		this.subStationDisplay = new JPanel();
		
		// 1 colonne - espacement vertical 5px
		this.subStationDisplay.setLayout(new GridLayout(0,1,0,5));
		this.subStationDisplay.setBorder(BorderFactory.createLineBorder(Color.black, 2, false));
				
		
		JLabel key = new JLabel("Sous-station #");
		JLabel value = new JLabel("(-DEFAULT-)");

		ArrayList<JLabel> display = new ArrayList<>();
		
		display.add(key);
		display.add(value);
		
		content.put("station", display);
		this.addToDisplay(display, true);

	}
	
	
	/**
	 * Ajout d'un groupe de consommateurs
	 * @param item le groupe
	 */
	public void addGroup(Group item) {
		JLabel key = new JLabel("| --> Group "+ item.getId());
		JLabel value = new JLabel("(-DEFAULT-)");

		ArrayList<JLabel> display = new ArrayList<>();

		display.add(key);
		display.add(value);
		
		this.content.put("group" + item.getId(), display);
		this.addToDisplay(display);
		this.nbGroups++;
	}
	
	/**
	 * Ajout d'une centrale électrique
	 * @param item la centrale
	 */	
	public void addPowerPlant(PowerPlant item) {
		
		JLabel key = new JLabel("| <-- PowerPlant "+ item.getId());
		JLabel value = new JLabel("(-DEFAULT-)");

		ArrayList<JLabel> display = new ArrayList<>();

		display.add(key);
		display.add(value);

		this.content.put("powerplant"+ item.getId(), display);
		this.addToDisplay(display);
		this.nbPowerPlants++;
	}
	
	/**
	 * Ajoute une ligne de données à l'affichage. Sans bordure par défaut
	 * @param values tableau de labels à afficher
	 */
	private void addToDisplay(ArrayList<JLabel> values) {
		this.addToDisplay(values, false);
	}
	
	/**
	 * Ajoute une ligne de données à l'affichage
	 * @param values tableau de labels à afficher
	 * @param border affiche une bordure autour de la ligne si true
	 */
	private void addToDisplay(ArrayList<JLabel> values, boolean border) {
		JPanel row = new JPanel();
		
		// une ligne - autant de colones que nécessaire
		row.setLayout(new GridLayout(1,0));
		
		for(JLabel label : values) {
			row.add(label);
		}
		
		if(border) {
			row.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
				
		this.subStationDisplay.add(row);
	}
	
	/**
	 * Fournit le JPanel contenant l'affichage de la station
	 * @return JPanel 
	 */
	public JPanel getContainer() {
		
		return this.subStationDisplay;
	}



}
