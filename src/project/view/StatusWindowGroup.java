package project.view;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.*;

/**
 * 
 * @author yoann
 *
 * Classe qui décrit la vue d'un élément groupe de consommation
 */
public class StatusWindowGroup extends StatusWindowElement {
	
	public StatusWindowGroup(Group group) {
		this.modelNode = group;
		
		this.content = new ArrayList<>();

		this.elementDisplay = new JPanel();

		// 1 ligne - espacement horizontal 5px
		this.elementDisplay.setLayout(new GridLayout(1,0,5,0));
		//this.elementDisplay.setBorder(BorderFactory.createLineBorder(Color.black, 2, false));
				
		for(String data : formatData()) {
			this.content.add(new JLabel(data));
		}
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
