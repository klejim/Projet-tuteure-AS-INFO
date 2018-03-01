package network;

/**
 *
 * @author Jimenez
 */
public class Line {
    public static int nextId = 0;
    private int id, power;
    private PowerPlant in;
    private SubStation out;
    
    private Line(){
        id = nextId++;
    }
    
    public Line(PowerPlant plant, SubStation station){
        in = plant;
        out = station;        
        power = plant.getActivePower();
    }

    public int getId() {
        return id;
    }

    public int getPower() {
        return power;
    }

    public PowerPlant getIn() {
        return in;
    }

    public SubStation getOut() {
        return out;
    }
}