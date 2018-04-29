package project.view;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import project.network.Network;
import project.network.Node;
import project.network.SubStation;
/**
 * Classe qui génère une fenêtre pour les entrées utilisateur
 * @author yoann
 *
 */

@SuppressWarnings("serial")
public class InputWindow extends JFrame {
	
	// Réseau associé
	private Network modelNetwork;
	
	
	// Liste d'éléments à afficher
	private ArrayList<InputWindowElement> inputElements;
	
	
	/**
	 * Constructeur
	 * @param ntw Le réseau à afficher
	 */
	public InputWindow(Network ntw) {
		super();
		this.modelNetwork = ntw;
		this.inputElements = new ArrayList<>();
		createElements();
		buildWindow();	
	}
	
	

	/**
	 * Initialisation de la JFrame InputWindow
	 */
	private void buildWindow() {

		// init fenêtre
		this.setTitle("Commandes Réseau");
		this.setSize(300, 400);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();

		// Layout principal - Grille
		panel.setLayout(new GridLayout(1, 0, 5, 5));

		this.setContentPane(panel);
		this.validate();
		this.repaint();
		this.setVisible(true);
	}
	
	/**
	 * Créé les blocs d'input
	 */
	public void createElements() {
		
		// Modification des consommations des groupes
		InputWindowElement elt = new InputWindowConsumption(modelNetwork);
		inputElements.add(elt);
		
		// Ajouter les nouveaux blocs d'input/output ici
	}
	
	/**
	 * Créé l'affichage de la fenêtre
	 */
	public void createDisplay() {
		
		for(InputWindowElement elt : inputElements) {
			this.add(elt.getDisplay());
		}
			
		// mise à jour de la fenêtre
		this.validate();
		this.repaint();
	
	}
	
	/**
	 * Met à jour l'affichage de la fenêtre
	 */
	public void updateDisplay() {
		
		for(InputWindowElement elt : inputElements) {
			elt.updateElement();
		}
		// mise à jour de la fenêtre
		this.validate();
		this.repaint();		
	}

}
