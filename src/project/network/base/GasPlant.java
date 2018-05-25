package project.network.base;

/**
 * Classe représentant une centrale à gaz.
 * @author Jimenez
 * @see PowerPlant
 */
public class GasPlant extends PowerPlant{
    public static final int DEFAULT_POWER = 300000;
    public static final int DEFAULT_DELAY = 5;
    /**
     * Constructeur.
     * @param s le nom de la centrale.
     */
    GasPlant(String s) {
        super(s, DEFAULT_POWER, DEFAULT_DELAY);
    }
}
