package project.view;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.PowerPlant;
import project.network.PowerPlant.State;

/**
 * 
 * @author yoann
 * 
 * Classe qui décrit la vue d'une centrale électrique selon le Modèle
 */

public class StatusWindowPowerPlant extends StatusWindowElement {

	public StatusWindowPowerPlant(PowerPlant plant) {
		this.modelNode = plant;

		this.content = new ArrayList<>();

		this.elementDisplay = new JPanel();

		// 1 ligne - espacement horizontal 5px
		this.elementDisplay.setLayout(new GridLayout(1,0,5,0));

		for(String data : formatData()) {
			this.content.add(new JLabel(data));
		}
	}

	@Override
	/**
	 * @see project.view.StatusWindow.Element#formatData()
	 */
	public String[] formatData() {
		String[] data = new String[3];

		State state = ((PowerPlant)this.modelNode).getState();
		int pout = ((PowerPlant)this.modelNode).getActivePower();
		int id = ((PowerPlant)this.modelNode).getId();

		data[0] = "| <-- PowerPlant "+ id;
		data[1] = "Etat: "+state;
		data[2] = "Pout: "+pout+" kW";

		return data;
	}

}
