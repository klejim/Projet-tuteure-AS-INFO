package network;

import java.util.ArrayList;

/**
 *
 * @author Jimenez
 */

public class Network {
    private int production, consumption;
    private ArrayList<SubStation> stations;
    
    private Network(){
        stations = new ArrayList<>();
    }
    // todo : rendre le constructeur plus flexible
    public Network(int nStations, int nPowerPlants, int nGroups){
        this();
        Group tabG[] = new Group[nGroups];
        for (int i=0;i<nGroups;i++){
            tabG[i] = new Group(250, "group n°" + i);
        }
        PowerPlant tabP[] = new PowerPlant[nPowerPlants];
        for (int i=0;i<nPowerPlants;i++){
            tabP[i] = new GasPlant("power plant n°" + i);
        }
        for (int i=0;i<nStations;i++){
            stations.add(new SubStation("station n°" + i, tabP, tabG));
        }
        stations.forEach(s -> {
            production += s.getPowerIn();
            consumption += s.getPowerOut();
        });
    }
}
