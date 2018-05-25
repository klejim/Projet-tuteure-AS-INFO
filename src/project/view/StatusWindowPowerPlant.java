package project.view;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import project.network.base.GasPlant;
import project.network.base.HydraulicPlant;
import project.network.base.Line;
import project.network.base.NuclearPlant;
import project.network.base.PowerPlant;
import project.network.base.PowerPlant.State;

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
		String name = ((PowerPlant) this.modelNode).getName();
		int usedPower = connexion.getActivePower();
		

		data[0] = "| <-- " + name;
		data[1] = "Etat: " + state;
		data[2] = "P. utilisée: " + usedPower/1000 + " MW";
		if(type.equals(NuclearPlant.class)) {
			data[3] = " (Pmax : 1500 MW)";
		}else if (type.equals(HydraulicPlant.class)) {
			data[3] = "(Pmax : 5 MW)";
		}else if (type.equals(GasPlant.class)) {
			data[3] = "(Pmax : 300 MW)";
		}else {
			data[3] = "type centrale inconnu";
		}

		return data;
	}

}
