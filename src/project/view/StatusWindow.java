/**
 * 
 */
package project.view;


import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.*;

/**
 * @author yoann
 * 
 * Classe créant la fenêtre de statut de réseau
 * 
 * TODO : Taille sous-paneaux avec autre type Layout ?
 *
 */
public class StatusWindow extends JFrame {

	private int nbSubStations;
	private HashMap<Integer, StatusWindowSubStation> stations;


	/**
	 * Constructeur par défaut
	 */
	public StatusWindow () {
		super();

		this.nbSubStations = 0;
		stations = new HashMap<>();
		buildWindow();
	}

	/**
	 * Initialisation de la JFrame StatusWindow
	 */
	private void buildWindow() {

		// init fenêtre
		this.setTitle("Statut Réseau");
		this.setSize(1200,600);
		this.setLocationRelativeTo(null); 
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(initDisplay());
		this.validate();
		this.repaint();		
	}
	
	/**
	 * Initialise le JPanel principal
	 * @return le JPanel Principal
	 */	
	private JPanel initDisplay() {

		JPanel panel = new JPanel();

		// 2 colonnes et autant de lignes que nécessaires - espacement de 5px
		panel.setLayout(new GridLayout(0,2,5,5));

		return panel;
	}

	/**
	 * Créé l'affichage à partir du réseau. N'utiliser qu'à la création ou au changement d'architecture du réseau
	 * @param ntw réseau à afficher
	 */
	public void buildDisplay(Network ntw) {
		this.nbSubStations = ntw.count(SubStation.class)[0];
		System.out.println("Nombre de sous-sations : " + this.nbSubStations);

		ArrayList<Node> nodes = ntw.getNodes();

		for(Node node : nodes) {
			// pour chaque station
			if(node instanceof SubStation) {
				StatusWindowSubStation station = new StatusWindowSubStation((SubStation)node);
				
				// ajout de la station à la Map stations
				this.stations.put(node.getId(), station);
				this.nbSubStations++;

				// pour chaque centrale reliée à la station
				for(Line line : ((SubStation)node).getLines()) {
					station.addPowerPlant(line.getIn());
				}

				// pour chaque groupe relié à la station
				for(Group group : ((SubStation)node).getGroups()) {
					station.addGroup(group);
				}

				// ajout de la nouvelle station à la fenêtre
				this.getContentPane().add(station.getContainer());
			}
		}

		// mise à jour de la fenêtre
		this.validate();
		this.repaint();
	}

	/**
	 * Met à jour l'affichage des valeurs correspondant au réseau
	 * @param ntw le réseau à afficher
	 */
	public void updateDisplay(Network ntw) {
		// TODO
		
		for(Node currentNode : ntw.getNodes()) {
			if(currentNode instanceof SubStation) {
				this.stations.get(currentNode.getId()).update();
			}
		}
		
		// mise à jour de la fenêtre
		this.validate();
		this.repaint();
	}


	/**
	 * Main de test pour StatusWindows
	 * @param args aucun parametre nécessaire
	 */
	public static void main(String[] args) {
		StatusWindow myWindow = new StatusWindow();
		myWindow.setVisible(true);	

		Scanner sc = new Scanner(System.in);

		// réseau de test basé sur l'archi en dur de Network
		Network myNetwork = new Network(0,0,0);

		myWindow.buildDisplay(myNetwork);

		while(true) {

		}
	}

}
