package project.network;

/**
 * Classe abstraite représentant un élément du réseau, qu'il soit une centrale, une sous-station ou un groupe de consommation.
 * @author Jimenez
 */
public abstract class Node {
    public static int nextId = 0;
    private final int id;
    private String name;
    /**
     * Constructeur par défaut.
     */
    Node(){
        id = nextId++;
    }
    /**
     * Constructeur.
     * @param s le nom de l'élément. 
     */
    Node(String s){
        this();
        name = s;
    }
    /**
     * Cette fonction étant abstraite chaque élément du réseau définit la manière dont on détermine s'il est connecté ou non.
     * La règle générale est qu'un élément est considéré connecté s'il est relié à une sous-station (les sous-stations sont toujours connectées).
     * @return true si l'élement est connecté au réseau et false sinon.
     */
    public abstract boolean isConnected();
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        return hash;
    }
    /**
     * Un élément est considéré égal à un autre si leurs ids sont les même. Aucun autre attribut n'est considéré.
     * @param obj l'objet soumis à la comparaison.
     * @return true si l'objet est égal à this et false sinon.
     */
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
    /** 
     * @return l'identifiant de l'élément. 
     */
    public int getId() {
        return id;
    }
    /**
     * @return le nom de l'élément. 
     */
    public String getName(){
        return name;
    }
    /**
     * Change le nom de l'élément.
     * @param name le nouveau nom.
     */
    final void setName(String name) {
        this.name = name;
    }
}
