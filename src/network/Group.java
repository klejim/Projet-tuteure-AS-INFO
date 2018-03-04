package network;

/**
 *
 * @author Jimenez
 */
public class Group extends Node{
    // membres et méthodes statiques
    private int consumption;
    private SubStation station;
    
    Group(int power, String s){
        super(s);
        consumption = power;
    }
    
    @Override
    public boolean isConnected(){
        return station != null;
    }
    
    // getters/setters
    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    void setStation(SubStation station) {
        this.station = station;
    }

    public SubStation getStation() {
        return station;
    }
}
