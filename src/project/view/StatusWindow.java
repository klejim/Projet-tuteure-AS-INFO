package project.view;

import project.network.base.SubStation;
import project.network.base.Group;
import project.network.base.Node;
import project.network.base.Line;
import project.network.base.Network;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import project.Test;

/**
 * Classe gérant la fenêtre de statut de réseau.
 * 
 * Elle créé la fenêtre, récupère les sous-station du réseau, créé leur affichage et celui des éléments
 * qui lui sont connectés. Le réseau se créé en trois temps :
 * 
 *  1. Utiliser le constructeur StatusWindow(Network) pour créer la fenêtre principale.
 *  2. Utiliser StatusWindow.createDisplay() pour créer l'affichage du réseau.
 *  3. Utiliser StatusWindow.updateDisplay() pour mettre à jour l'affichage des paramètres des éléments du réseau.
 * 
 * @author yoann
 *  
 * TODO : Tester un autre type de Layout pour la fenêtre 
 */
@SuppressWarnings("serial")
public class StatusWindow extends JFrame implements WindowListener{

	private Network modelNetwork;
	private ArrayList<StatusWindowSubStation> subStations;

	private HashMap<Integer, Node> TESTNetworkElements;

	/**
	 * Constructeur
	 * @param ntw Le réseau à afficher
	 */
	public StatusWindow(Network ntw) {
		super();
		this.modelNetwork = ntw;

		this.subStations = new ArrayList<>();
		buildWindow();

		TESTNetworkElements = new HashMap<>();
                addWindowListener(this);
	}

	/**
	 * Initialisation de la JFrame StatusWindow
	 */
	private void buildWindow() {

		// init fenêtre
		this.setTitle("Statut Réseau");
		this.setSize(1200, 600);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();

		// Layout principal - 2 colonnes et autant de lignes que nécessaires - espacement de 5px
		panel.setLayout(new GridLayout(0, 2, 5, 5));

		this.setContentPane(panel);
		this.validate();
		this.repaint();
		this.setVisible(true);
	}

	/**
	 * Créé l'affichage à partir du réseau. N'utiliser qu'une fois, à la création de la fenêtre
	 */
	public void createDisplay() {

		ArrayList<Node> nodes = this.modelNetwork.getNodes();

		for (Node node : nodes) {
			// Sauvegarde node pour test
			TESTNetworkElements.put(node.getId(), node);

			// pour chaque station
			if (node.getClass().equals(SubStation.class)) {
				StatusWindowSubStation station = new StatusWindowSubStation((SubStation) node);

				// ajout de la sous-station à la collection
				this.subStations.add(station);

				// pour chaque centrale reliée à la station
				for (Line line : ((SubStation) node).getLines()) {
					station.addElement(new StatusWindowPowerPlant(line));
				}

				// pour chaque groupe relié à la station
				for (Group group : ((SubStation) node).getGroups()) {
					station.addElement(new StatusWindowGroup(group));
				}

				// creation de l'affichage sous-station
				station.createDisplay();

				// ajout à la fenêtre
				this.getContentPane().add(station.getDisplay());
			}
		}

		// mise à jour de la fenêtre
		this.validate();
		this.repaint();
	}

	/**
	 * Met à jour l'affichage des valeurs de paramètres. Utiliser après createDisplay()
	 */
	public void updateDisplay() {
		for (StatusWindowSubStation station : this.subStations) {
			station.updateDisplay();
		}
	}

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

		// réseau de test basé sur l'archi en dur de Network
		Network myNetwork = new Network();

		StatusWindow myWindow = new StatusWindow(myNetwork);

		myWindow.createDisplay();

		// TEST update data
		Group group7 = null;
		Group group12 = null;
		if (myWindow.TESTNetworkElements.containsKey(7)) {
			group7 = (Group) myWindow.TESTNetworkElements.get(7);
		}

		if (myWindow.TESTNetworkElements.containsKey(7)) {
			group12 = (Group) myWindow.TESTNetworkElements.get(12);
		}

		while (true) {
			Thread.sleep(2000);
			if (group7 != null)
				group7.setConsumption(0);
			if (group12 != null)
				group12.setConsumption(300000);
			myWindow.updateDisplay();

			Thread.sleep(2000);
			if (group7 != null)
				group7.setConsumption(400000);
			if (group12 != null)
				group12.setConsumption(0);
			myWindow.updateDisplay();
		}
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
