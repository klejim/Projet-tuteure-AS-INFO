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
	public static ArrayList<Integer> listId;
    private final int id;
	
    static {
    	listId=new ArrayList<Integer>();
    }
    
	/**
	 * Constructeur du clusterGroup.Version avec ID et un groupe unique destinée au parser
	 * @param groupList Groupes appartenant au clusterGroup
	 * @throws Exception 
	 */
	public ClusterGroup(Group group,int p_id) throws Exception {
		this.groupList=new ArrayList<Group>();
		this.groupList.add(group);
		if(this.listId.contains(p_id)) {
			throw new Exception("Erreur assignation id");
		}		
		this.id=p_id;
		listId.add(this.id);
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

	/**
	 * @return the groupList
	 */
	public ArrayList<Group> getGroupList() {
		return groupList;
	}

	/**
	 * @param groupList the groupList to set
	 */
	public void setGroupList(ArrayList<Group> groupList) {
		this.groupList = groupList;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	

}
