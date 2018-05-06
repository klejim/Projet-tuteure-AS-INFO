package project.view;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import project.network.GasPlant;
import project.network.HydraulicPlant;
import project.network.Line;
import project.network.NuclearPlant;
import project.network.PowerPlant;
import project.network.PowerPlant.State;

/**
 * Classe qui décrit la vue d'une centrale électrique.
 * @author yoann 
 */
public class StatusWindowPowerPlant extends StatusWindowElement {

	private Line connexion;

	/**
	 * Constructeur
	 * @param connexionLine ligne au départ de la sous-station qui est reliée à l'usine
	 */
	public StatusWindowPowerPlant(Line connexionLine) {
		this.connexion = connexionLine;
		this.modelNode = connexionLine.getIn();

		this.content = new ArrayList<>();

		this.elementDisplay = new JPanel();

		// 1 ligne - espacement horizontal 5px
		this.elementDisplay.setLayout(new GridLayout(1, 0, 5, 0));
	}

	@Override
	/**
	 * @see project.view.StatusWindow.Element#formatData()
	 */
	public String[] formatData() {
		String[] data = new String[4];

		State state = ((PowerPlant) this.modelNode).getState();
		Class type = ((PowerPlant)this.modelNode).getClass();
		int id = ((PowerPlant) this.modelNode).getId();
		int usedPower = connexion.getActivePower();
		

		data[0] = "| <-- PowerPlant " + id;
		data[1] = "Etat: " + state;
		data[2] = "P. utilisée: " + usedPower + " kW";
		if(type.equals(NuclearPlant.class)) {
			data[3] = "Nucléaire (1500000 kW)";
		}else if (type.equals(HydraulicPlant.class)) {
			data[3] = "Hydraulique (5000 kW)";
		}else if (type.equals(GasPlant.class)) {
			data[3] = "Gaz (300000 kW)";
		}else {
			data[3] = "type centrale inconnu";
		}

		return data;
	}

}
