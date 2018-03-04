package network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Jimenez
 */

abstract public class PowerPlant extends Node{
    public enum State {ON, OFF, STARTING};
    private State state;
    private int power, startDelay;
    private String name;
    private ArrayList<Line> lines;
    
    private PowerPlant(){
        super();
        state = State.ON; // jusqu'à ce qu'on gère un réseau dynamique, pour avoir des centrales qui produisent
        lines = new ArrayList<>();
    }

    PowerPlant(String n, int p, int sd){
        this();
        name = n;
        power = p;
        startDelay = sd;
    }
    
    /** méthodes accessibles **/
    @Override
    boolean isConnected(){
        return !lines.isEmpty();
    }
    void updateStations(){
        lines.forEach((line) -> {
            line.getOut().updateInput();
        });
    }
    void updateLines(){
        lines.forEach(line -> line.setPower(power/lines.size())); // 
    }
    
    void addLines(Line... l){
        lines.addAll(Arrays.asList(l));
    }
    /**
     * @return la puissance de sortie effective (pour l'instant c'est power si la centrale fonctionne et 0 sinon) 
     */
    public int getActivePower(){
        return (state == State.ON)?power:0;
    }
    
    boolean stop(){
        boolean stopped = false;
        if (state == State.ON || state == State.STARTING){
            state = State.OFF;
            stopped = true;
        }
        return stopped;
    }
    
    /** getters/setters **/
 
    boolean setStartDelay(int delay) {
        boolean ok = false;
        if(delay >= 0){
            startDelay = delay;
            ok = true;
        }
        return ok;
    }
    
    boolean setPower(int p) {
        boolean ok = false;
        if (p >= 0){
            power = p;
            ok = true;
        }
        return ok;
    }
    
    void setName(String name) {
        this.name = name;
    }

    void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public State getState() {
        return state;
    }

    public int getPower() {
        return power;
    }

    public int getStartDelay() {
        return startDelay;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }
}
