package project.network.error;

import project.network.Node;

/**
 *
 * @author Jimenez
 */
public class NodeNotConnectedError extends NetworkError{
    private final Node node;
    public NodeNotConnectedError(Node n){
        super();
        node = n;
    }

    public Node getNode() {
        return node;
    }
    
    /**
     * @see NetworkError#solve() 
     */
    @Override
    public void solve(){
        this.setMessage("Connexion manuelle nécessaire");
    }
}