package network;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Jimenez
 */
public class SubStation {
    public static int nextId = 0;
    private int id, powerIn, powerOut;
    private String name;
    private ArrayList<Group> groups;
    private ArrayList<Line> lines;
    
    private SubStation(){
       id = nextId++;
       groups = new ArrayList<>();
       lines = new ArrayList<>();
    }
    
    public SubStation(String n, PowerPlant[] plants, Group[] gr){
        this();
        name = n;
        for (PowerPlant p : plants){
            lines.add(new Line(p,this));
        }
        groups.addAll(Arrays.asList(gr));
        computePower();
    }
    
    private void computePower(){
        lines.forEach(line -> powerIn += line.getPower());
        groups.forEach(g -> powerOut += g.getConsumption());
    }
    // getters/setters
    public int getId() {
        return id;
    }

    public int getPowerIn() {
        return powerIn;
    }

    public int getPowerOut() {
        return powerOut;
    }

    public String getName() {
        return name;
    }
}