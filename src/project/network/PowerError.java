package project.network;

/**
 *
 * @author Jimenez
 */
public class PowerError extends NetworkError{
    private final int power;
    private final SubStation station;
    PowerError(SubStation s, int p){
        super();
        power = p;
        station = s;
    }
    public int getPower() {
        return power;
    }
    public SubStation getStation() {
        return station;
    }
}