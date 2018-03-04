package network;

/**
 *
 * @author Jimenez
 */
abstract class Node {
    public static int nextId = 0;
    private final int id;
    Node(){
        id = nextId++;
    }
    
    public int getId() {
        return id;
    }
    /**
     * Chaque élément du réseau définit la manière dont on détermine s'il est connecté ou non. La règle générale est qu'un élément
     * est considéré connecté s'il est relié à une sous-station (les sous-stations sont toujours connectées).
     */
    abstract boolean isConnected();

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
