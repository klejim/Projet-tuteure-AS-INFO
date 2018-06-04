package project.view;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import project.Test;

import project.network.base.Network;

/**
 * Classe qui génère une fenêtre pour les entrées utilisateur. Cette fenêtre peut regrouper plusieurs interfaces utilisateur
 * sous la forme d'élements qui héritent de InputWindowElement.
 * 
 * @author yoann
 */

@SuppressWarnings("serial")
public class InputWindow extends JFrame implements WindowListener{
	
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
                addWindowListener(this);
	}
	
	

	/**
	 * Initialisation de la JFrame InputWindow
	 */
	private void buildWindow() {

		// init fenêtre
		this.setTitle("Commandes Réseau");
		this.setSize(600, 400);
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
		
		// Compteur d'itérations simulateur
		InputWindowElement elt2 = new InputWindowTimeCounter(modelNetwork);	
		inputElements.add(elt2);
		
		
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

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("closing");
        try{
            Test.printResult(this.modelNetwork, Test.finalIter);
        }
        catch (IOException exc){
            
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }

}
