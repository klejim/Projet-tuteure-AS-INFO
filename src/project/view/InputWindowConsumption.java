package project.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import project.network.Network;
import project.network.Node;
import project.network.Group;

public class InputWindowConsumption extends InputWindowElement implements ActionListener {

	// consommations courantes
	private Map<Integer, Integer> consumptions;
	
	// champs textuels
	private Map<Integer, JTextField> fields;
	
	
	/**
	 * Constructeur
	 * @param model le réseau à paramétrer
	 */
	public InputWindowConsumption(Network model) {
		super(model, "Réglage Consommation");
		this.consumptions = new HashMap<>();
		this.fields = new HashMap<>();

		createElement();	
		System.out.println("construct");
	}

	/**
	 * @see project.view.InputWindowElement#createElement()
	 */
	@Override
	public void createElement() {

		// premiere recupération des consommation
		updateConsumptions();

		// création des blocs légende-texfield
		for(int groupId : this.consumptions.keySet()) {

			// création des textfield
			JTextField field = new JTextField();
			field.setText(this.consumptions.get(groupId).toString());
			this.fields.put(groupId, field);


			// création légende
			String name = "grp";
			for(Node node : this.model.getNodes()) {
				if(node.getId() == groupId) {
					name = node.getName();
				}
			}
			JLabel legend = new JLabel ( name + " (MW) : ");

			// création Panel Groupes
			JPanel groupPanel = new JPanel();
			groupPanel.setLayout(new GridLayout(1, 2, 0, 0));
			groupPanel.add(legend);
			groupPanel.add(field);

			// ajout sous panel groupe à panel principal
			this.elementDisplay.add(groupPanel);


		}
		// Panel boutons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));


		// Bouton Reset
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		resetButton.setActionCommand("Reset");
		buttonsPanel.add(resetButton);


		// Bouton valider
		JButton validateButton = new JButton("Valider");
		validateButton.addActionListener(this);
		validateButton.setActionCommand("Valider");
		buttonsPanel.add(validateButton);

		this.elementDisplay.add(buttonsPanel);
	}
	
	
	/**
	 * @see project.view.InputWindowElement#updateElement()
	 */
	@Override
	public void updateElement() {
		// rien à faire
	}
	
	/**
	 * Récupère les valeurs courantes de consommation depuis le réseau
	 */
	private void updateConsumptions() {

		this.consumptions.clear();

		for(Node node : this.model.getNodes()) {
			if(node.getClass().equals(Group.class)) {
				this.consumptions.put(node.getId(),((Group)node).getConsumption()/1000);
			}
		}

	}
	
	/**
	 * Récupère les nouvelles valeurs de consommation depuis la fenêtre et met à jour le réseau
	 */
	private void setConsumptions() {
		
		// récupération des consommations
		for(int groupId: this.fields.keySet()) {
			int valeur;
			try {
				valeur = Integer.parseInt(this.fields.get(groupId).getText());
				valeur = valeur*1000;
			}catch(NumberFormatException e) {
				// valeur non convertible en nombre entier - on ignore cette valeur
				continue;
			}
			this.consumptions.put(groupId, valeur);
		}

		// Mise à jour des groupes dans le réseau
		for(Node node : this.model.getNodes()) {
			if (node.getClass().equals(Group.class)) {
				((Group)node).setOriginalconsumption(this.consumptions.get(node.getId()));
			}
		}
	}
	
	
	/**
	 * Synchronise la fenêtre avec les consommations courantes du réseau
	 */
	public void resetConsumptions() {
		updateConsumptions();
		for(int groupId : this.consumptions.keySet()) {
			this.fields.get(groupId).setText(this.consumptions.get(groupId).toString());
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if("Reset".equals(arg0.getActionCommand())) {
			// Action bouton reset - 
			resetConsumptions();

		}else if ("Valider".equals(arg0.getActionCommand())) {
			// Action bouton valider
			setConsumptions();
		}
	}





}
