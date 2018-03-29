package project.network;

/**
 * Classe représentant un groupe de consommation (ou groupe de foyers, ou encore groupe d'habitations).
 * @author Jimenez
 * @see SubStation
 */
public class Group extends Node{
    private int consumption;
    private int originalconsumption; // a manipulé avec le facteur
    private SubStation station;
    
    	
    /**
     * Constructeur.
     * @param power la puissance consommée par le groupe.
     * @param s le nom du groupe.
     */
    Group(int power, String s){
        super(s);
        consumption = power;
        originalconsumption=power;
    }
    /**
     * Un groupe est considéré connecté s'il est relié à une sous-station.
     * @return true si le groupe est lié à une sous-station et false sinon.
     */
    @Override
    public boolean isConnected(){
        return station != null;
    }
    
    // getters/setters
    
    /**
	 * @return the originalconsumption
	 */
	public int getOriginalconsumption() {
		return originalconsumption;
	}
	/**
	 * @param originalconsumption the originalconsumption to set
	 */
	public void setOriginalconsumption(int originalconsumption) {
		this.originalconsumption = originalconsumption;
	}
    
    /**
     * @return la puissance consommée par le groupe. 
     */
    public int getConsumption() {
        return consumption;
    }
    /**
     * Modifie la consommation du groupe. 
     * @param consumption la nouvelle consommation.
     */
    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }
    /**
     * Réaffecte le groupe à une sous-station.
     * @param station la nouvelle sous-station.
     * @see SubStation
     */
    void setStation(SubStation station) {
        this.station = station;
    }
    /**
     * @return la sous-station
     * @see SubStation
     */
    public SubStation getStation() {
        return station;
    }
    /**
     * Met à jour la consommation d'un groupe et les sous stations associées sur une valeur précise, supposée être appellée par update (laissé en public pour le test)
     * @param Nouvelle Consommation
     * @see SubStation
     */
    public void updateConsumption(int consumption){
    	this.setConsumption(consumption);
    	if(this.getStation()!=null){
    		this.getStation().updatePowers();
    	}
    	
    }
    /**
     * Met à jour la consommation d'un groupe via updateConsumption suivant un facteur, et limite éventuellement la consommation si une limitation IN venait à venir
     * @param Nouvelle Consommation
     * @see SubStation
     */
    public void update(int factor,int maxPower){
    	
    }
    
}
