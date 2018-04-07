package project.network;

/**
 * Classe représentant une centrale nucléaire.
 * @author Jimenez
 * @see PowerPlant
 */
public class NuclearPlant extends PowerPlant{
    public static final int DEFAULT_POWER = 1500000; // en kiloWatt
    public static final int DEFAULT_DELAY = 5; // en heures / iterations

    /**
     * Constructeur.
     * @param s le nom de la centrale.
     */
    NuclearPlant(String s) {
        super(s, DEFAULT_POWER, DEFAULT_DELAY);
    }
    
}
