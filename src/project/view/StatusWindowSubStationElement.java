package project.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.*;


public class StatusWindowSubStationElement extends StatusWindowElement {

	// liste des éléments connectés à la sous-station
	private ArrayList<StatusWindowElement> connectedElements;
	
	
	public StatusWindowSubStationElement(SubStation subStation) {
		this.modelNode = subStation;

		this.content = new ArrayList<>();
		this.connectedElements = new ArrayList<>();

		this.elementDisplay = new JPanel();

		// 1 colonne - espacement vertical 5px
		this.elementDisplay.setLayout(new GridLayout(0,1,0,5));
		this.elementDisplay.setBorder(BorderFactory.createLineBorder(Color.black, 2, false));

	}

	/**
	 * Ajout d'une centrale ou d'un groupe à la sous-station
	 * @param elt l'élément à rajouter
	 */	
	public void addElement(StatusWindowElement elt) {
		this.connectedElements.add(elt);		
	}

	@Override
	/**
	 * Pour la sous-station, update doit maj tous les éléments connectés
	 */
	public void update() {
		// maj des éléments connectés
		for(StatusWindowElement elt : this.connectedElements) {
			elt.update();
		}

		// maj des éléments locaux		
		for(String param : this.formatData()) {
			this.content.add(new JLabel(param));
		}
		updateDisplay();

	}

	@Override
	/**
	 * Pour la sous-station, updateDisplay doit prendre en compte tous les éléments connectés
	 */
	public void updateDisplay() {

		this.elementDisplay.removeAll();

		// rajoute les nouveaux labels pour la sous-stations
		JPanel row = new JPanel();

		// 1 ligne - espacement horizontal 5px - bordure noire
		row.setLayout(new GridLayout(1,0,5,0));
		row.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
		
		for(JLabel label : this.content) {
			row.add(label);
		}
		this.elementDisplay.add(row);

		for(StatusWindowElement elt : this.connectedElements) {
			this.elementDisplay.add(elt.getDisplay());
		}
	}

	@Override
	/**
	 * @see project.view.StatusWindowElement#formatData()
	 */
	public String[] formatData() {
		String[] data = new String[4];

		int pin = ((SubStation)this.modelNode).getPowerIn();
		int pout = ((SubStation)this.modelNode).getPowerOut();
		int id = ((SubStation)this.modelNode).getId();
		
		data[0] = ("Sous-station "+ id);
		data[1] = "Pin: "+pin+" kW";
		data[2] = "Pout: "+pout+" kW";
		data[3] = (pin>=pout?"OK":"P. INSUFISANTE");

		return data;
	}
	
	/**
	 * Main de test de SubstationElement
	 * @param args Aucun
	 */
	public static void main(String[] args) {
		
		Network myNetwork = new Network(0,0,0);
		
		ArrayList<StatusWindowSubStationElement> stations = new ArrayList<>();
		
		JPanel globalPanel = new JPanel();
		globalPanel.setLayout(new GridLayout(0,2,10,10));
		
		for(Node node : myNetwork.getNodes()) {
			if(node.getClass().equals(SubStation.class)) {
				StatusWindowSubStationElement local = new StatusWindowSubStationElement((SubStation)node);
				local.update();
				stations.add(local);
				globalPanel.add(local.getDisplay());
			}
		}

		
		JFrame testWindow = new JFrame();
		testWindow.setTitle("Test Substation");
		testWindow.setSize(400,600);
		testWindow.setLocationRelativeTo(null); 
		testWindow.setResizable(true);
		testWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testWindow.setContentPane(globalPanel);
		testWindow.validate();
		testWindow.repaint();		
		testWindow.setVisible(true);
		
	}

}
