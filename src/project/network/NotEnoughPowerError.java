package project.network;

/**
 *
 * @author Jimenez
 */
public class NotEnoughPowerError extends NetworkError{
    private final int power;
    private final SubStation station;
    NotEnoughPowerError(SubStation s, int p){
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
