package project.network.error;

import project.network.base.SubStation;

/**
 *
 * @author Jimenez
 */
abstract public class PowerError extends NetworkError{
    private final int power;
    private final SubStation station;
    public PowerError(SubStation s, int p){
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