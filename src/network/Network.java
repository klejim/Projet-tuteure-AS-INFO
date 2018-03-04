package network;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Jimenez
 */

public class Network {
    private int production, consumption;
    private ArrayList<Node> nodes;
    private Network(){
        nodes = new ArrayList<>();
    }
    // todo : un vrai constructeur
    public Network(int nStations, int nPowerPlants, int nGroups){
        this();
        TESTInitNetwork();
    }
    
    private void TESTInitNetwork(){
        NuclearPlant np = new NuclearPlant("nucléaire");
        HydraulicPlant hp1 = new HydraulicPlant("hydraulique 1"), hp2 = new HydraulicPlant("hydraulique 2");
        GasPlant gp1 = new GasPlant("gaz 1");
        SubStation s1 = new SubStation("station 1"), s2 = new SubStation("station 2"), s3 = new SubStation("station 3");
        Group g1 = new Group(400000, "groupe 1"), g2 = new Group(250000, "groupe 2"), g3 = new Group(610000,"groupe 3");
        Group g4 = new Group(30000, "groupe 4"), g5 = new Group(200000, "groupe 5"), g6 = new Group(400000, "groupe 6");
        Group g7 = new Group(240000, "groupe 7"), g8 = new Group(250000, "groupe 8");
        Node tab[] = {np, hp1, hp2, s1, s2, s3, g1, g2, g3, g4, g5, g6, g7, gp1, g8};
        nodes.addAll(Arrays.asList(tab));
        addGroupsToStation(s1, g1, g2);
        addGroupsToStation(s2, g3, g4, g5);
        addGroupsToStation(s3, g6, g7);
        addPlantsToStation(s1, np, gp1);
        addPlantsToStation(s2, np, gp1, hp2);
        addPlantsToStation(s3, np, hp2);
        
    }
        
    public void addGroupsToStation(SubStation s, Group... gr){
        for (Group g : gr){
            g.setStation(s);
        }
        s.addGroups(gr);
        s.updateOutput();
        computeConsumption();
    }
    
    public void computeConsumption(){
        consumption = 0;
        for (Node n : nodes){
            if (n instanceof SubStation){
                consumption += ((SubStation) n).getPowerOut();
            }
        }
    }
    /**
     * 
     * @param classes : les classes dont on veut compter les instances
     * @return un tableau contenant le nombre d'instance de chaque classe
     */
    public int[] count(Class... classes){
        int numbers[] = new int[classes.length];
        for (Node n : nodes){
            for (int i=0;i<classes.length;i++){
                Class c = classes[i];
                if (c.isAssignableFrom(n.getClass())){
                    numbers[i]++;
                }
            }
        }
        return numbers;
    }
    
    public void computeProduction(){
        production = 0;
        for (Node n : nodes){
            if (n instanceof SubStation){
                production += ((SubStation) n).getPowerIn();
            }
        }
    }

    public void addPlantsToStation(SubStation s, PowerPlant... plants){
        for (PowerPlant p : plants){
            Line line = new Line(p, s);
            p.addLines(line);
            s.addLines(line);
            p.updateLines();
            p.updateStations();
        }
        computeProduction();
    }
    
    
    public int getProduction() {
        return production;
    }

    public int getConsumption() {
        return consumption;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }
    
}
