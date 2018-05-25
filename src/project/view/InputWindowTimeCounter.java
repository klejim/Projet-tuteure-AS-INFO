package project.view;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import project.network.base.Network;
/**
 * Compteur d'itérations du simulateur. Fonctionne avec un compteur local synchronisé à la création de la vue;
 * @author yoann
 *
 */

public class InputWindowTimeCounter extends InputWindowElement {
	
	// compteur d'itérations
	private int counter;
	private JLabel counterLabel;
	
	/**
	 * Constructeur
	 * @param model le réseau à paramétrer
	 */
	public InputWindowTimeCounter(Network ntw) {
		super(ntw, "Itération n°");
		this.counter = 0;
		createElement();

	}

	/**
	 * @see project.view.InputWindowElement#createElement()
	 */
	@Override
	public void createElement() {
		counterLabel = new JLabel(Integer.toString(counter), SwingConstants.CENTER);
		counterLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		this.elementDisplay.add(this.counterLabel);
	}
	
	/**
	 * @see project.view.InputWindowElement#updateElement()
	 */
	@Override
	public void updateElement() {
		counter++;
		counterLabel.setText(Integer.toString(counter));
	}

}
