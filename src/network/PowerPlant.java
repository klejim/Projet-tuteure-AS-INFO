package network;

/**
 *
 * @author Jimenez
 */

public abstract class PowerPlant {
    public enum State {ON, OFF, STARTING};
    protected static int nextId = 0;
    private State state;
    protected int power, id, startDelay;
    protected String name;
    // cables ?
    
    protected PowerPlant(){
        id = nextId++;
        state = State.OFF;
    }

    public PowerPlant(String n){
        this();
        name = n;  
    }
    /**
     * 
     * @return la puissance de sortie effective (pour l'instant c'est power si la centrale fonctionne et 0 sinon) 
     */
    public int getActivePower(){
        return (state == State.ON)?power:0;
    }
    
    public int getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
