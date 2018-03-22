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
		//this.elementDisplay.setBorder(BorderFactory.createLineBorder(Color.black, 2, false));

		for(String data : formatData()) {
			this.content.add(new JLabel(data));
		}
		updateDisplay();
	}

	@Override
	/**
	 * @see project.view.StatusWindow.Element#formatData()
	 */
	public String[] formatData() {
		String[] data = new String[2];

		State state = ((PowerPlant)this.modelNode).getState();
		int pout = ((PowerPlant)this.modelNode).getActivePower();

		data[0] = "Etat: "+state;
		data[1] = "Pout: "+pout+" kW";

		return data;
	}

}
