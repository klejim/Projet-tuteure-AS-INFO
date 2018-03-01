package network;

/**
 *
 * @author Jimenez
 */
public class HydraulicPlant extends PowerPlant{
    public static final int DEFAULT_POWER = 5000;
    public static final int DEFAULT_DELAY = 0;

    public HydraulicPlant(String n) {
        super(n);
        power = DEFAULT_POWER;
        startDelay = DEFAULT_DELAY;
    }
}
