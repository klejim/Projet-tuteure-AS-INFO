package network;

/**
 *
 * @author Jimenez
 */
public abstract class Node {
    public static int nextId = 0;
    private final int id;
    private String name;
    Node(){
        id = nextId++;
    }
    Node(String n){
        this();
        name = n;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName(){
        return name;
    }
    /**
     * Chaque élément du réseau définit la manière dont on détermine s'il est connecté ou non. La règle générale est qu'un élément
     * est considéré connecté s'il est relié à une sous-station (les sous-stations sont toujours connectées).
     * @return true si l'élement est connecté au réseau et faux sinon
     */
    public abstract boolean isConnected();

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
