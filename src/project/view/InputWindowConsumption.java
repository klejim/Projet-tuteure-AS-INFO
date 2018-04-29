package project.view;

import java.awt.GridLayout;

import project.network.Network;

public class InputWindowConsumption extends InputWindowElement {

	/**
	 * Constructeur
	 * @param model le réseau à paramétrer
	 */
	public InputWindowConsumption(Network model) {
		super(model, "Réglage Consommation");
		createElement();
		
	}

	@Override
	public void createElement() {
		
		// 1 colones  - espacement horizontal et vertical 5px
		this.elementDisplay.setLayout(new GridLayout(0, 1, 5, 5));
		
		// titre
		this.elementDisplay.add(this.titre);
		
	}

	@Override
	public void updateElement() {
		// TODO Auto-generated method stub
		
	}

}
