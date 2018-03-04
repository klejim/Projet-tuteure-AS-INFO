package network;

/**
 * @author Jimenez
 */
public class NuclearPlant extends PowerPlant{
    public static final int DEFAULT_POWER = 1500000; // en kiloWatt
    public static final int DEFAULT_DELAY = 20; // en heures

    NuclearPlant(String n) {
        super(n, DEFAULT_POWER, DEFAULT_DELAY);
    }
    
}
