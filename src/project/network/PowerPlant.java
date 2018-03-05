package project.network;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe abstraite représentant une centrale. Elle contient l'essentiel des méthodes utiles à la manipulation des centrales.
 * @author Jimenez
 */
abstract public class PowerPlant extends Node{
    public enum State {ON, OFF, STARTING};
    
    private State state;
    private int power, startDelay;
    private ArrayList<Line> lines;
    /**
     * Constructeur par défaut.
     */
    private PowerPlant(){
        super();
        state = State.ON; // jusqu'à ce qu'on gère un réseau dynamique, pour avoir des centrales qui produisent
        lines = new ArrayList<>();
    }
    /**
     * Constructeur.
     * @param s le nom de la centrale
     * @param p la puissance produite par la centrale
     * @param sd le délai de démarrage pour la centrale
     */
    PowerPlant(String s, int p, int sd){
        this();
        setName(s);
        power = p;
        startDelay = sd;
    }
    
    /** méthodes accessibles **/
    /**
     * Une centrale est définie comme connectée si elle est alimente une sous-station.
     * @return true si la centrale est connectée et false sinon.
     */
    @Override
    public boolean isConnected(){
        return !lines.isEmpty();
    }
    /**
     * Met à jour la puissance d'entrée de chaque sous-station alimentée par la centrale.
     * @see SubStation#updateInput
     */
    void updateStations(){
        lines.forEach((line) -> {
            line.getOut().updateInput();
        });
    }
    /**
     * Met à jour la puissance distribuée par la centrale à chaque ligne. 
     */
    void updateLines(){
        lines.forEach(line -> line.setPower(power/lines.size())); // naïf mais il fallait quelque chose et c'est aussi bien ainsi
    }
    /**
     * Relie des lignes de transmission à la centrale.
     * @param l les lignes à ajouter.
     */
    void addLines(Line... l){
        lines.addAll(Arrays.asList(l));
    }
    /**
     * Calcule la puissance effective de la centrale.
     * @return {@link #power power} si la centrale fonctionne et 0 sinon) 
     */
    public int getActivePower(){
        return (state == State.ON)?power:0;
    }
    /**
     * Arrête la centrale si c'est possible.
     * @return true si la centrale a été arrêtée et false sinon.
     */
    boolean stop(){
        boolean stopped = false;
        if (state == State.ON || state == State.STARTING){
            state = State.OFF;
            stopped = true;
        }
        return stopped;
    }
    /**
     * Met en marche la centrale si c'est possible.
     * @return true si la centrale a été mise en marche et false sinon.
     */
    boolean start(){
        boolean started = false;
        if (state == State.OFF){
            state = State.ON;
            started = true;
        }
        return started;
    }
    
    /** getters/setters **/
    /**
     * Modifie le délai au démarrage. Ne fait rien si la valeur donnée en paramètre est négative.
     * @param delay le nouveau délai
     * @return true si le délai a été modifié et false sinon.
     */
    boolean setStartDelay(int delay) {
        boolean ok = false;
        if(delay >= 0){
            startDelay = delay;
            ok = true;
        }
        return ok;
    }
    /**
     * Modifie la puissance de sortie de la centrale. Ne fait rien si la valeur donnée en paramètre est négative.
     * @param p la nouvelle puissance.
     * @return true si la puissance a été modifiée et false sinon.
     */
    boolean setPower(int p) {
        boolean ok = false;
        if (p >= 0){
            power = p;
            ok = true;
        }
        return ok;
    }
    /**
     * Affecte un nouveau tableau de lignes de transmission à la centrale.
     * @param lines les nouvelles lignes.
     */
    void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }
    /**
     * @return l'état de la centrale. 
     */
    public State getState() {
        return state;
    }
    /**
     * @return la puissance produite par la centrale. 
     */
    public int getPower() {
        return power;
    }
    /**
     * @return le délai de démarrage de la station.
     */
    public int getStartDelay() {
        return startDelay;
    }
    /**
     * @return les lignes de transmission. 
     */
    public ArrayList<Line> getLines() {
        return lines;
    }
}
