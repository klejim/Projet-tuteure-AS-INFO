package project.network;

/**
 * Une classe regroupant des modèles d'erreurs susceptibles d'être générées à l'analyse du réseau.
 * @author Jimenez
 */
public class NetworkError {
    public class NotEnoughPower extends NetworkError{
        public int power;
        public SubStation station;
    }
    
    public class TooMuchPower extends NetworkError{
        public int power;
        public SubStation station;
    }
    
    public class NodeNotConnected extends NetworkError{
        public Node node;
    }
}
