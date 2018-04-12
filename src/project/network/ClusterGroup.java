package project.network;

import java.util.ArrayList;

/**
 * @author Kenny
 *
 */
public class ClusterGroup {
	private ArrayList<Group> groupList;
	private Double randomFactor;
	
	
	public ClusterGroup(ArrayList<Group> groupList) {
		this.groupList=groupList;
		
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
	
	public void applyRandomFactor() {
		for(Group group : this.groupList) {
			group.setRandomConsumption(this.randomFactor);
		}
	}

	
	
	
}
