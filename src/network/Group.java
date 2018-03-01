package network;

/**
 *
 * @author Jimenez
 */
public class Group {
    // membres et méthodes statiques
    private static int nextId = 0;
    private int consumption, id;
    private String name;
    
    private Group(){
        id = nextId++;
    }
    
    public Group(int power, String s){
        this();
        consumption = power;
        name = s;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }
}
