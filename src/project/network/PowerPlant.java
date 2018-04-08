package project.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Classe abstraite représentant une centrale. Elle contient l'essentiel des méthodes utiles à la manipulation des centrales.
 * @author Jimenez
 */
abstract public class PowerPlant extends Node{
    public enum State {ON, OFF, STARTING};
    /* quelques comparateurs utilisés dans la gestion des erreurs */
    // comparaison sur la puissance disponible
    static final Comparator<PowerPlant> powerComparator = (p1, p2)->{
            return p1.getActivePower() - p2.getActivePower();
        };
    // ON  < STARTING < OFF | à état égal on compare la puissance
    static final Comparator<PowerPlant> stateAndPowerComparator = (p1, p2)->{
            int cmp = 0;
            if (p1.getState() == p2.getState()){
                cmp = p1.getActivePower() - p2.getActivePower();
            }
            else if (p1.getState() == PowerPlant.State.OFF){
                cmp = -1;
            }
            else if (p1.getState() == PowerPlant.State.ON && p2.getState() == PowerPlant.State.OFF){
                cmp = 1;
            }
            else if (p1.getState() == PowerPlant.State.ON && p2.getState() == PowerPlant.State.STARTING){
                cmp = -1;
            }
            else if (p1.getState() == PowerPlant.State.STARTING){
                cmp = 1;
            }     
            return cmp;
        };
    // comparaison sur le délai de démarrage
    static final Comparator<PowerPlant> startDelayComparator = (p1, p2)->{
            return p1.startDelay - p2.startDelay;
        };
    private State state;
    private int power, activePower, startDelay;
    private ArrayList<Line> lines;
    private int framesSinceStart;
    
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
    PowerPlant(String s, int p,int sd){
        this();
        setName(s);
        power = p;
        activePower = p;
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
     * Relie des lignes de transmission à la centrale.
     * @param l les lignes à ajouter.
     */
    void addLines(Line... l){
        lines.addAll(Arrays.asList(l));
    }
    /**
     * Calcule la puissance disponible sur la centrale.
     * 
     * Chaque emprunt sur une centrale allumée diminue sa puissance disponible. Afin de supporter les emprunts sur les centrales en
     * démarrage ou éteintes la puissance disponible sur celles-ci n'est pas nulle mais négative (le fonctionnement est symétrique à 
     * celui des centrales allumées).
     */
    public void computeActivePower(){
        if (state == State.ON){
            activePower = power;
            for (Line l : lines){
                activePower -= l.getPower();
                // la somme des puissances demandées par les lignes ne peut être supérieure à this.power car les stations
                // n'empruntent de la puissance que si la centrale en a de disponible.
                // Evidemment si la puissance disponible change indépendamment des emprunts des stations on aura un problème
            }
        }
        else{
            activePower = -1*power;
            for (Line l : lines){
                activePower += l.getPower();
            }
        }
    }
    /**
     * Distribue plus de puissance à une station donnée. Ne fait rien si la puissance disponible est inférieure à la demande.
     * 
     * Si la station est allumée la puissance est ajoutée à la ligne de transmission et la puissance disponible de la centrale est
     * diminuée. Si la station est en cours de démarrage la puissance disponible est diminuée (devenant négative) et la ligne est
     * marquée comme non active.
     * @param station la station concernée.
     * @param p la quantité de puissance à ajouter.
     * @return la puissance supplémentaire affectée à la ligne ou -1 si la puissance disponible est inférieure à la demande.
     */
    public int grantToStation(SubStation station, int p){
        boolean ok = false;
        if (Math.abs(activePower) >= p && (state == State.ON || state == State.STARTING)){
            for (Line line : lines){
                if (line.getOut() == station){
                    if (state == State.STARTING){
                        activePower += p;
                        line.setState(Line.State.DISABLED);
                    }
                    else{
                        activePower -= p;
                    }
                    line.addPower(p);
                    ok = true;
                }
            }
        }
        return ok?p:-1;
    }
    /**
     * @return la puissance disponible.
     * @see #computeActivePower() 
    */
    public int getActivePower(){
        return activePower;
    }
    /**
     * Arrête la centrale si c'est possible.Met à jour les lignes et stations correspondantes.
     * @return true si la centrale a été arrêtée et false sinon.
     */
    boolean stop(){
        boolean stopped = false;
        if (state == State.ON || state == State.STARTING){
            state = State.OFF;
            activePower = -1*power;
            stopped = true;
        }
        return stopped;
    }
    /**
     * Met en marche la centrale si c'est possible.Met à jour les lignes et stations correspondantes.
     * @return true si la centrale a été mise en marche et false sinon.
     */
    //Il faudrait passer started en int éventuellement, pour distinguer ON OFF & STARTING
    //
    boolean start(){
        boolean started = false;
        if (state == State.OFF){
        	if(this.startDelay==0){
                    state = State.ON;
                    activePower = power;
                    started = true;
        	}
        	else{
                    state=State.STARTING;
                    this.framesSinceStart=0;
                    started=true; 
        	}    
        }
        return started;
    }
    /**
     * Met à jour l'état de la centrale et sa puissance effective.
     */
    @Override
    public void update(){
        computeActivePower();
    	if (this.state==State.STARTING){
            if (this.framesSinceStart>=this.startDelay){
                this.state=State.ON;
                this.framesSinceStart=0;
            }
            else{
                this.framesSinceStart++;
            }
    	}
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