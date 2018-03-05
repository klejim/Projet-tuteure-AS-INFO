package project.network;

/**
 * Classe représentant un groupe de consommation (ou groupe de foyers, ou encore groupe d'habitations).
 * @author Jimenez
 * @see SubStation
 */
public class Group extends Node{
    private int consumption;
    private SubStation station;
    /**
     * Constructeur.
     * @param power la puissance consommée par le groupe.
     * @param s le nom du groupe.
     */
    Group(int power, String s){
        super(s);
        consumption = power;
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
}
