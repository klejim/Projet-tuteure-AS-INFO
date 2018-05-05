package project.network;

/**
 *
 * @author Jimenez
 */
public class TooMuchPowerError extends PowerError{
    TooMuchPowerError(SubStation s, int p){
        super(s, p);
    }
}
