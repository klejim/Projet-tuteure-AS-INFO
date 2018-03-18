package project.view;

import java.awt.Color;
import java.awt.GridLayout;
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
	

	private HashMap<String, HashMap<JLabel, JLabel>> content;
		
	// Conteneur représentatif de la station
	private JPanel subStationDisplay;

	/**
	 * Constructeur par défaut. Init à un champ station
	 */
	public StatusWindowSubStation() {
		this.nbGroups = 0;
		this.nbPowerPlants = 0;
		
		this.content = new HashMap<>();
		
		this.subStationDisplay = new JPanel();
		
		// 1 colonne
		this.subStationDisplay.setLayout(new GridLayout(0,1));
		this.subStationDisplay.setBorder(BorderFactory.createLineBorder(Color.black, 2, false));
				
		
		JLabel key = new JLabel("Station #");
		JLabel value = new JLabel("(-DEFAULT-)");

		HashMap<JLabel, JLabel> display = new HashMap<>();

		display.put(key, value);

		content.put("station", display);
		this.addToDisplay(key, value);

	}
	
	
	/**
	 * Ajout d'un groupe de consommateurs
	 * @param item le groupe
	 */
	public void addGroup(Group item) {
		JLabel key = new JLabel("| --> Group "+ item.getId());
		JLabel value = new JLabel("(-DEFAULT-)");

		HashMap<JLabel, JLabel> display = new HashMap<>();

		display.put(key, value);

		content.put("group" + item.getId(), display);
		this.addToDisplay(key, value);
		this.nbGroups++;
	}
	
	/**
	 * Ajout d'une centrale électrique
	 * @param item la centrale
	 */	
	public void addPowerPlant(PowerPlant item) {
		
		JLabel key = new JLabel("| <-- PowerPlant "+ item.getId());
		JLabel value = new JLabel("(-DEFAULT-)");

		HashMap<JLabel, JLabel> display = new HashMap<>();

		display.put(key, value);

		this.content.put("powerplant"+ item.getId(), display);
		this.addToDisplay(key, value);
		this.nbPowerPlants++;
	}
	
	/**
	 * Ajoute une ligne de données à l'affichage
	 * @param key JLabel clé
	 * @param value JLabel valeur
	 */
	private void addToDisplay(JLabel key, JLabel value) {
		JPanel row = new JPanel();
		row.setLayout(new GridLayout(1,2));
		
		row.add(key);
		row.add(value);
		row.setBorder(BorderFactory.createLineBorder(Color.RED));	
		
		this.subStationDisplay.add(row);
	}
	
	/**
	 * Retourne le JPanel contenant l'affichage de la station
	 * @return JPanel
	 */
	public JPanel getContainer() {
		
		return this.subStationDisplay;
	}



}
