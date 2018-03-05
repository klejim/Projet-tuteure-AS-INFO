package project.network;

/**
 * Classe représentant une ligne de transmission de la puissance entre une centrale et une station. Elle n'est pas considérée comme un élément
 * du réseau, étant uniquement référencée par les instances de {@link PowerPlant PowerPlant} et {@link SubStation SubStation}
 * @author Jimenez
 * @see SubStation
 * @see PowerPlant
 */
public class Line {
    public static int nextId = 0;
    private int id, power;
    private PowerPlant in;
    private SubStation out;
    /**
     * Constructeur par défaut.
     */
    private Line(){
        super();
    }
    /**
     * Constructeur.
     * @param plant la centrale à l'extrémité de la ligne.
     * @param station la sous-station à l'autre extrémité de la ligne.
     */
    Line(PowerPlant plant, SubStation station){
        this();
        in = plant;
        out = station;        
    }
    
    /** getters/setters **/
    /**
     * @return l'identifiant de la ligne. 
     */
    public int getId() {
        return id;
    }
    /**
     * @return la puissance que transporte la ligne.
     */
    public int getPower() {
        return power;
    }
    /**
     * @return la centrale à laquelle la ligne est reliée.
     * @see PowerPlant
     */
    public PowerPlant getIn() {
        return in;
    }
    /**
     * @return la sous-station à laquelle la ligne est reliée.
     * @see SubStation
     */
    public SubStation getOut() {
        return out;
    }
    /**
     * Modifie la puissance de la ligne.
     * @param power la nouvelle puissance.
     */
    void setPower(int power) {
        this.power = power;
    }
}