package project.network;

import java.util.ArrayList;

/**
 * @author Kenny
 *
 */
public class ClusterGroup {
	private ArrayList<Group> groupList;
	private Double randomFactor;
	
	
	public void init(Network net) {
		
		ArrayList<Node> nodes=net.getNodes();
		this.groupList=new ArrayList<Group>();
		
		randomFactor=(double) 1;
		
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


	
	
	
}
