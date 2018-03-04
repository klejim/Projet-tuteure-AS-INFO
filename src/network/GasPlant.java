package network;

/**
 *
 * @author Jimenez
 */
public class GasPlant extends PowerPlant{
    public static final int DEFAULT_POWER = 300000;
    public static final int DEFAULT_DELAY = 5;

    GasPlant(String n) {
        super(n, DEFAULT_POWER, DEFAULT_DELAY);
    }
}
