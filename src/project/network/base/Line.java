package project.network.base;

import java.util.Comparator;

/**
 * Classe représentant une ligne de transmission de la puissance entre une centrale et une station. Elle n'est pas considérée comme un élément
 * du réseau, étant uniquement référencée par les instances de {@link PowerPlant PowerPlant} et {@link SubStation SubStation}
 * @author Jimenez
 * @see SubStation
 * @see PowerPlant
 */
public class Line {
    /**
     * Représente l'état d'une ligne de transmission.
     * ONLINE: la ligne peut transmettre (sa puissance peut être nulle)
     * OFFLINE: la ligne ne transmet pas. Sa puissance peut ne pas être nulle.
     * WAITING: la ligne est "en attente", c'est-à-dire qu'une station a réclamé de la puissance et que la centrale concernée
     * est en train de démarrer. La ligne ne transmet pas encore.
     */
    public enum State{ONLINE, OFFLINE, WAITING};
    public static final Comparator<Line> powerComparator = (l1, l2)->{
            return l1.getActivePower() - l2.getActivePower();
        };
    public static final Comparator<Line> TypeAndPowerComparator = (l1, l2)->{
            int cmp = 0;
            if (l1.getIn().getStartDelay() == l2.getIn().getStartDelay()){
                cmp =  Line.powerComparator.compare(l1,l2);
            }
            else{
                cmp = l1.getIn().getStartDelay() - l2.getIn().getStartDelay(); // équivalent à comparer les classes
            }
            return cmp;
        };
    public static int nextId = 0;
    private int id;
    private int power;
    private PowerPlant in;
    private SubStation out;
    private State state;
    /**
     * Constructeur par défaut.
     */
    private Line(){
        super();
        state = State.OFFLINE;
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
     * @return la puissance que transporte la ligne. La valeur retournée ne prend pas en compte l'état de la ligne et
     * peut ne pas correspondre à la valeur voulue si la ligne n'est pas dans son état normal.
     * @see #getActivePower()
     */
    public int getPower() {
        return power;
    }
    /**
     * @return la puissance transmise par la ligne en prenant en compte son état. 
     */
    public int getActivePower(){
        return (state == State.ONLINE) ? power : 0;
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
    /**
     * Augmente la puissance de la ligne par le paramètre.
     * @param p la puissance à ajouter.
     */
    public void addPower(int p){
        power += p;
    }
    /**
     * Réduit la puissance de la ligne. Ne fait rien si la puissance à retirer est supérieure à la puissance de la ligne.
     * @param p la puissance à enlever.
     * @return la puissance restituée.
     */
    public int substractPower(int p){
        boolean ok = false;
        if (p <= power){
            power -= p;
            ok = true;
        }
        return ok?p:0;
    }
    /**
     * @return l'état de la ligne de transmission.
     */
    public State getState() {
        return state;
    }
    /**
     * Modifie l'état de la ligne.
     * @param state le nouvel état.
     */
    public void setState(State state) {
        this.state = state;
    }
}