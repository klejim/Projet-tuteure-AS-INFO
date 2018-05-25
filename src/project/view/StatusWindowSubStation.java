package project.view;

import project.network.base.SubStation;
import project.network.base.Group;
import project.network.base.Node;
import project.network.base.PowerPlant;
import project.network.base.Line;
import project.network.base.Network;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * Classe qui décrit la vue d'une sous-station.
 * Elle s'occupe de gérer l'affichage des paramètres propres à la sous-station ainsi qu'aux éléments qui lui sont
 * sont connectés.
 * @author yoann
 */
public class StatusWindowSubStation extends StatusWindowElement {

	// liste des éléments connectés à la sous-station
	private ArrayList<StatusWindowElement> connectedElements;

	/**
	 * Constructeur
	 * @param subStation La sous-station à afficher
	 */
	public StatusWindowSubStation(SubStation subStation) {
		this.modelNode = subStation;

		this.content = new ArrayList<>();
		this.connectedElements = new ArrayList<>();

		this.elementDisplay = new JPanel();
	}

	/**
	 * Créé l'affichage statique de la sous-station et des éléments connectés
	 */
	public void createDisplay() {

		// Layout de la sous-station - 1 colonne - espacement vertical 5px
		this.elementDisplay.setLayout(new GridLayout(0, 1, 0, 5));
		this.elementDisplay.setBorder(BorderFactory.createLineBorder(Color.black, 2, false));

		// Affichage infos sous-station - Layout 1 ligne - espacement horizontal 5px.
		JPanel localDisplay = new JPanel();
		localDisplay.setLayout(new GridLayout(1, 0, 5, 0));
		localDisplay.setBorder(BorderFactory.createLineBorder(Color.blue, 1, true));

		for (String param : this.formatData()) {
			JLabel parametre = new JLabel(param);
			this.content.add(parametre);
			localDisplay.add(parametre);
		}

		this.elementDisplay.add(localDisplay);

		// Affichage des groupes et centrales
		for (StatusWindowElement elt : this.connectedElements) {
			this.elementDisplay.add(elt.getDisplay());
		}

	}

	/**
	 * Ajout d'une centrale ou d'un groupe connecté à la sous-station
	 * @param elt l'élément de réseau à rajouter
	 */
	public void addElement(StatusWindowElement elt) {
		elt.createDisplay();
		this.connectedElements.add(elt);
	}

	@Override
	/**
	 * Pour la sous-station, updateDisplay() doit mettre à jour tous les éléments connectés
	 */
	public void updateDisplay() {
		// maj des éléments connectés
		for (StatusWindowElement elt : this.connectedElements) {
			elt.updateDisplay();
		}

		// maj des éléments locaux
		String[] data = this.formatData();
		for (int i = 0; i < data.length; i++) {
			this.content.get(i).setText(data[i]);
		}

	}

	@Override
	/**
	 * @see project.view.StatusWindowElement#formatData()
	 */
	public String[] formatData() {
		String[] data = new String[4];

		int pin = ((SubStation) this.modelNode).getPowerIn();
		int pout = ((SubStation) this.modelNode).getPowerOut();
		String name = ((SubStation) this.modelNode).getName();

		data[0] = (name);
		data[1] = "Pin: " + pin/1000 + " MW";
		data[2] = "Pout: " + pout/1000 + " MW";
		data[3] = (pin >= pout ? "OK" : "P. INSUFISANTE");

		return data;
	}

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

		Network myNetwork = new Network();

		ArrayList<StatusWindowSubStation> stations = new ArrayList<>();
		ArrayList<PowerPlant> plants = new ArrayList<>();
		ArrayList<Group> groups = new ArrayList<>();

		JPanel globalPanel = new JPanel();
		globalPanel.setLayout(new GridLayout(0, 2, 10, 10));

		for (Node node : myNetwork.getNodes()) {
			if (node.getClass().equals(SubStation.class)) {
				StatusWindowSubStation local = new StatusWindowSubStation((SubStation) node);

				// pour chaque centrale reliée à la station
				for (Line line : ((SubStation) node).getLines()) {
					plants.add(line.getIn());
					System.out.println("Plant " + line.getIn().getId());
					local.addElement(new StatusWindowPowerPlant(line));
				}

				// pour chaque groupe relié à la station
				for (Group group : ((SubStation) node).getGroups()) {
					groups.add(group);
					System.out.println("Groupe " + group.getId());
					local.addElement(new StatusWindowGroup(group));
				}

				local.createDisplay();
				stations.add(local);
				globalPanel.add(local.getDisplay());
			}
		}

		JFrame testWindow = new JFrame();
		testWindow.setTitle("Test Substation");
		testWindow.setSize(1200, 600);
		testWindow.setLocationRelativeTo(null);
		testWindow.setResizable(true);
		testWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testWindow.setContentPane(globalPanel);
		testWindow.validate();
		testWindow.repaint();
		testWindow.setVisible(true);

		while (true) {
			Thread.sleep(1000);
			groups.get(0).setConsumption(0);
			groups.get(1).setConsumption(300000);

			for (StatusWindowSubStation station : stations) {
				station.updateDisplay();
			}

			Thread.sleep(1000);
			groups.get(0).setConsumption(100000);
			groups.get(1).setConsumption(0);

			for (StatusWindowSubStation station : stations) {
				station.updateDisplay();
			}

		}

	}

}
