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
        lines.forEach(line -> powerIn += line.getActivePower());
    }
    /**
     * Met à jour la puissance de sortie à partir de la demande de chaque groupe alimenté par la station.
     */
    void updateOutput(){
        powerOut = 0;
        groups.forEach(g -> powerOut += g.getConsumption());
    }
    /**
     * Met à jour les puissances d'entrée et de sortie de la sous-station.
     */
    @Override
    public void update(){
        updateInput();
        updateOutput();
    }
    /**
     * Retourne une certaine quantité de puissance à la centrale la fournissant, augmentant sa puissance disponible. 
     * @param line la ligne de transmission concernée menant à la centrale.
     * @param p la puissance à rendre.
     * @return la puissance restituée ou 0
     * @see Line#substractPower(int) 
     */
    int giveBackPower(Line line, int p){
        int returned = line.substractPower(p);
        // on pourrait se contenter de mettre à jour la puissance disponible de la centrale au prochain début de cycle
        // mais il est mieux de le faire maintenant car elle pourrait être nécessaire pour gérer une autre erreur.
        line.getIn().computeActivePower();
        return returned;
    }
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
    /**
     * @return La différence entre la puissance d'entrée et la puissance de sortie.
     */
    public int getDiff(){
        // alimentation - demande
        return powerIn - powerOut;
    }
    
    public ArrayList<PowerPlant> getPlants(){
		ArrayList<Line> lines =this.getLines();
    	ArrayList<PowerPlant> plants=new ArrayList<PowerPlant>();
		for(Line l : lines){
    		plants.add(l.getIn());
    	}
		
		return plants;
    }
}