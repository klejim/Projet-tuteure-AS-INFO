package project.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.*;
import project.network.PowerPlant.State;
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
 * TODO : gestion mutualisé via fonction private de l'ajout/retrait dans content et subStationDisplay FINIR !
 * TODO : update(). 
 * TODO : Faire des classes pour groupe ou Powerplant si les données ou leur traitement deviennent compliqués
 *
 */
public class StatusWindowSubStation {

	// Collection des lignes avec Labels
	private HashMap<String, ArrayList<JLabel>> content;

	// JPanel de la sous-station
	private JPanel subStationDisplay;

	/**
	 * Constructeur par défaut
	 */
	public StatusWindowSubStation() {
		this.content = new HashMap<>();

		this.subStationDisplay = new JPanel();

		// 1 colonne - espacement vertical 5px
		this.subStationDisplay.setLayout(new GridLayout(0,1,0,5));
		this.subStationDisplay.setBorder(BorderFactory.createLineBorder(Color.black, 2, false));
	}


	/**
	 * Constructeur avec SubStation
	 * @param station la sous-station à construire
	 */
	public StatusWindowSubStation(SubStation station) {

		this();

		JLabel key = new JLabel("Sous-station "+ station.getId());

		ArrayList<JLabel> display = new ArrayList<>();
		display.add(key);
		for(String data : subStationDataFormat(station)) {
			display.add(new JLabel(data));
		}

		addToDisplay("station", display, true);
	}
	

	/**
	 * Ajout d'un groupe de consommateurs
	 * @param item le groupe
	 */
	public void addGroup(Group item) {
		JLabel groupeId = new JLabel("| --> Group "+ item.getId());


		ArrayList<JLabel> display = new ArrayList<>();
		display.add(groupeId);
		for(String data : groupDataFormat(item)) {
			display.add(new JLabel(data));
		}
		addToDisplay("group" + item.getId(), display);

	}

	/**
	 * Ajout d'une centrale électrique
	 * @param item la centrale
	 */	
	public void addPowerPlant(PowerPlant item) {

		JLabel powerPlantId = new JLabel("| <-- PowerPlant "+ item.getId());

		ArrayList<JLabel> display = new ArrayList<>();
		display.add(powerPlantId);
		for(String data : powerPlantDataFormat(item)) {
			display.add(new JLabel(data));
		}
		addToDisplay("powerplant"+ item.getId(), display);
	}


	/**
	 * Formate les données pour un groupe de consommation
	 * @param item le groupe
	 * @return Tableau de String avec les données à afficher
	 */
	private static String[] groupDataFormat(Group item) {		

		String[] data = new String[1];
		data[0] = "(Conso: "+item.getConsumption()+" kW)";
		return data;
	}
	/**
	 * Formate les données pour une sous station
	 * @param item la sous station
	 * @return Tableau de String avec les données à afficher
	 */
	private static String[] subStationDataFormat(SubStation item) {

		String[] data = new String[3];

		int pin = item.getPowerIn();
		int pout = item.getPowerOut();

		data[0] = "(Pin: "+pin+" kW)";
		data[1] = "(Pout: "+pout+" kW)";
		data[2] = (pin>=pout?"OK":"P. INSUFISANTE");

		return data;
	}
	/**
	 * Formate les données pour une centrale
	 * @param item la centrale
	 * @return Tableau de String avec les données à afficher
	 */
	private static String[] powerPlantDataFormat(PowerPlant item) {
		String[] data = new String[2];

		State state = item.getState();
		int pout = item.getActivePower();

		data[0] = "(Etat: "+state+")";
		data[1] = "(Pout: "+pout+" kW)";

		return data;
	}

	/**
	 * Met à jour les champs de valeurs pour chaque objet
	 */	
	public void update() {

		// pour tous les éléments
	}



	/**
	 * Ajoute une ligne de données à l'affichage. Sans bordure par défaut
	 * @param values tableau de labels à afficher
	 */
	private void addToDisplay(String key, ArrayList<JLabel> values) {
		this.addToDisplay(key, values, false);
	}

	
	
	/**
	 * Ajoute une ligne de donnée à l'affichage
	 * @param key la clé de sauvegarde du bloc
	 * @param values les valeurs à afficher
	 * @param border si true, affiche une bordure autour de la ligne
	 */
	private void addToDisplay(String key, ArrayList<JLabel> values, boolean border) {
		
		// ajout à content
		this.content.put(key, values);
		
		
		JPanel row = new JPanel();

		// une ligne - autant de colones que nécessaire
		row.setLayout(new GridLayout(1,0,5,0));

		for(JLabel label : values) {
			row.add(label);
		}

		for(int i= 4-values.size(); i > 0; i--) 
			row.add(new JLabel(""));


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
