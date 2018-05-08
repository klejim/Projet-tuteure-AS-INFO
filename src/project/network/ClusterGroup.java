package project.network;

import java.util.ArrayList;

/**
 * Classe servant à regrouper les Groupes en vue de leur affecter un facteur de consommation aléatoire.
 * 
 * Ils sont initialisés dans RandomMacro
 * @see RandomMacro
 * @author Kenny
 *
 */
public class ClusterGroup {
	private ArrayList<Group> groupList;
	private Double randomFactor;
	/**
	 * Constructeur du clusterGroup.
	 * @param groupList Groupes appartenant au clusterGroup
	 */
	public ClusterGroup(ArrayList<Group> groupList) {
		this.groupList = groupList;

	}

	/**
	 * @return the randomFactor
	 */
	public Double getRandomFactor() {
		return randomFactor;
	}

	/**
	 * @param randomFactor the randomFactor to set
	 */
	public void setRandomFactor(Double randomFactor) {
		this.randomFactor = randomFactor;
	}
	/**
	 * Applique à chaque Group du ClusterGroup le facteur de randomisation (de ce ClusterGroup)
	 */
	public void applyRandomFactor() {
		for (Group group : this.groupList) {
			group.setRandomConsumption(this.randomFactor);
		}
	}

}
