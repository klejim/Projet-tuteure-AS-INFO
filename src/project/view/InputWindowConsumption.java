package project.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import project.network.Network;
import project.network.Node;
import project.network.Group;

public class InputWindowConsumption extends InputWindowElement {

	
	private Map<Integer, Integer> groupConsumptions;
	private Map<Integer, JTextField> groupsFields;
	private Map<Integer, JPanel> groupsPanels;
		
	
	/**
	 * Constructeur
	 * @param model le réseau à paramétrer
	 */
	public InputWindowConsumption(Network model) {
		super(model, "Réglage Consommation");
		this.groupConsumptions = new HashMap<>();
		this.groupsFields = new HashMap<>();
		this.groupsPanels = new HashMap<>();
		
		createElement();		
	}

	@Override
	public void createElement() {
		
		// 1 colone  - espacement horizontal et vertical 5px
		this.elementDisplay.setLayout(new GridLayout(0, 1, 5, 5));
		
		// premiere recupération des consommation
		updateConsumptions();
		
		// création des blocs légende-texfield
		for(int groupId : this.groupConsumptions.keySet()) {
			
			// création des textfield
			JTextField field = new JTextField();
			field.setText(this.groupConsumptions.get(groupId).toString());
			this.groupsFields.put(groupId, field);


			// création légende
			JLabel legend = new JLabel ("Group "+ groupId + " : ");
			
			// création Panel
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, 2, 0, 0));
			panel.add(legend);
			panel.add(field);
			this.groupsPanels.put(groupId, panel);
			
			// ajout sous panel groupe à panel principal
			this.elementDisplay.add(panel);
		}
			
		
	}

	@Override
	public void updateElement() {
		updateConsumptions();
		for(int groupId : this.groupConsumptions.keySet()) {
			this.groupsFields.get(groupId).setText(this.groupConsumptions.get(groupId).toString());
		}
		
	}
	
	private void updateConsumptions() {
		
		this.groupConsumptions.clear();
		
		for(Node node : this.model.getNodes()) {
			if(node.getClass().equals(Group.class)) {
				this.groupConsumptions.put(node.getId(),((Group)node).getConsumption());
			}
		}
		
	}

}
