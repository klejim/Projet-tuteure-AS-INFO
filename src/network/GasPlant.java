package network;

/**
 *
 * @author Jimenez
 */
public class GasPlant extends PowerPlant{
    public static final int DEFAULT_POWER = 300000;
    public static final int DEFAULT_DELAY = 5;

    public GasPlant(String n) {
        super(n);
        power = DEFAULT_POWER;
        startDelay = DEFAULT_DELAY;
    }
}
