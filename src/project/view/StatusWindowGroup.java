package project.view;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.*;

/**
 * Classe qui décrit la vue d'un élément groupe de consommation
 * @author yoann 
 */
public class StatusWindowGroup extends StatusWindowElement {
	
	public StatusWindowGroup(Group group) {
		this.modelNode = group;
		
		this.content = new ArrayList<>();

		this.elementDisplay = new JPanel();

		// 1 ligne - espacement horizontal 5px
		this.elementDisplay.setLayout(new GridLayout(1,0,5,0));

	}


	@Override
	/**
	 * @see project.view.StatusWindow.Element#formatData()
	 */
	public String[] formatData() {
		
		String[] data = new String[2];
		data[0] = ("| --> Group "+ this.modelNode.getId());
		data[1] = "Conso: "+((Group)modelNode).getConsumption()+" kW";
		return data;
	}

}
