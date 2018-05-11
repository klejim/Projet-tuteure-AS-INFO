package project.network;

/**
 *
 * @author Jimenez
 */
public class NotEnoughPowerError extends PowerError{
    NotEnoughPowerError(SubStation s, int p){
        super(s, p);
    }
}
