package project.network;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe représentant une sous-station. Elle stocke des références aux lignes de transmission qui l'alimente ainsi qu'aux groupes de
 * consommation qu'elle dessert.
 * 
 * @author Jimenez
 */
public class SubStation extends Node{
    private int powerIn, powerOut;
    private ArrayList<Group> groups;
    private ArrayList<Line> lines;
    /**
     * Constructeur par défaut.
     */
    private SubStation(){
       super();
       groups = new ArrayList<>();
       lines = new ArrayList<>();
    }
    /**
     * Constructeur.
     * @param s une chaîne contenant le nom de la station.
     */
    SubStation(String s){
        this();
        setName(s);
    }
    /**
     * 
     * @param s le nom de la station.
     * @param plants les centrales auxquelles cette station est liée.
     * @param gr les groupes auxquels cette station est liée.
     */
    SubStation(String s, PowerPlant[] plants, Group[] gr){
        this(s);
        for (PowerPlant p : plants){
            Line line = new Line(p, this);
            lines.add(line);
            powerIn += line.getPower();
        }
        groups.addAll(Arrays.asList(gr));
        groups.forEach(g -> powerOut += g.getConsumption());
        
    }
    /**
     * 
     * @return true (une station est par définition toujours connectée). 
     */
    @Override
    public boolean isConnected(){
        return true;
    }
    /**
     * Met à jour la puissance d'entrée à partir des puissances venant de chaque lignes de transmission.
     */
    void updateInput(){
        powerIn = 0;
        lines.forEach(line -> powerIn += line.getPower());
    }
    /**
     * Met à jour la puissance de sortie à partir de la demande de chaque groupe alimenté par la station.
     */
    void updateOutput(){
        powerOut = 0;
        groups.forEach(g -> powerOut += g.getConsumption());
    }
    /**
     * Raccourci pour {@link #updateInput() updateInput} et {@link #updateOutput() updateOutput}.
     */
    void updatePowers(){
        updateInput();
        updateOutput();
    }
    /** méthodes acessibles **/
    /**
     * Ajoute des groupes de consommation à la station.
     * @param gr les groupes à ajouter.
     */
    void addGroups(Group... gr){
        groups.addAll(Arrays.asList(gr));
    }
    /**
     * Ajoute des lignes de transmission à la station.
     * @param li les lignes à ajouter.
     */
    void addLines(Line... li){
        lines.addAll(Arrays.asList(li));
    }
    /** getters/setters **/
    /**
     * @return la puissance en entrée de la station. 
     */
    public int getPowerIn() {
        return powerIn;
    }
    /**
     * @return la puissance exigée en sortie de la station. 
     */
    public int getPowerOut() {
        return powerOut;
    }
    /**
     * @return les groupes alimentés par la station.
     */
    public ArrayList<Group> getGroups() {
        return groups;
    }
    /**
     * 
     * @return les lignes alimentant la station.
     */
    public ArrayList<Line> getLines() {
        return lines;
    }
}