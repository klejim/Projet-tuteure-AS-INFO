package project.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Classe représentant un groupe de consommation (ou groupe de foyers, ou encore groupe d'habitations).
 * @author Jimenez
 * @see SubStation
 */
public class Group extends Node{
    // correspondance entre un nom de suite de données et une tableau de données
    // ex : <hiver, tableau de valeurs> 
    private static HashMap<String, ArrayList<Double>> consumptionModes = new HashMap<>();
    /**
     * Ajoute un modèle de consommation avec ses valeurs.
     * @param name le nom référençant le modèle
     * @param values les valeurs
     * @return vrai si le modèle à été ajouté et faux sinon
     */
    static boolean addConsumptionMode(String name, Double... values){
        boolean ok = false;
        if (!consumptionModes.containsKey(name)){
            ArrayList<Double> tab = new ArrayList<>(Network.SIMULATION_PERIOD);
            tab.addAll(Arrays.asList(values));
            consumptionModes.put(name, tab);
            ok = true;
        }
        return ok;
    }
    //
    private int consumption;
    private SubStation station;
    /**
     * Constructeur.
     * @param power la puissance consommée par le groupe.
     * @param s le nom du groupe.
     */
    Group(int power, String s){
        super(s);
        consumption = power;
    }
    /**
     * Un groupe est considéré connecté s'il est relié à une sous-station.
     * @return true si le groupe est lié à une sous-station et false sinon.
     */
    @Override
    public boolean isConnected(){
        return station != null;
    }
    
    // getters/setters
    /**
     * @return la puissance consommée par le groupe. 
     */
    public int getConsumption() {
        return consumption;
    }
    /**
     * Modifie la consommation du groupe.
     * @param consumption la nouvelle consommation.
     */
    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }
    /**
     * Réaffecte le groupe à une sous-station.
     * @param station la nouvelle sous-station.
     * @see SubStation
     */
    void setStation(SubStation station) {
        this.station = station;
    }
    /**
     * @return la sous-station
     * @see SubStation
     */
    public SubStation getStation() {
        return station;
    }
}
