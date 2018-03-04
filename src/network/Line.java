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
        super();
    }
    
    Line(PowerPlant plant, SubStation station){
        this();
        in = plant;
        out = station;        
    }
    
    /** getters/setters **/
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

    void setPower(int power) {
        this.power = power;
    }
}