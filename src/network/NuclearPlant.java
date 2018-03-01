package network;

/**
 * @author Jimenez
 */
public class NuclearPlant extends PowerPlant{
    public static final int DEFAULT_POWER = 1500000; // en kiloWatt
    public static final int DEFAULT_DELAY = 20; // en heures

    public NuclearPlant(String n) {
        super(n);
        power = DEFAULT_POWER;
        startDelay = DEFAULT_DELAY;
    }
    
}
